package net.atos.vcs.realtime.demo

import android.Manifest
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import net.atos.vcs.realtime.demo.applicationServer.Room
import net.atos.vcs.realtime.demo.databinding.SignInFragmentBinding
import net.atos.vcs.realtime.sdk.RealtimeSettings
import java.lang.Exception

class SignInFragment : Fragment(), AdapterView.OnItemSelectedListener {

    private lateinit var binding: SignInFragmentBinding
    private val TAG = "${this.javaClass.kotlin.simpleName}"
    private var audio = false
    private var video = false

    // To use the viewModels() extension function, include
    // "androidx.fragment:fragment-ktx:latest-version" in your app
    // module's build.gradle file.
    private val viewModel: SignInViewModel by viewModels()

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { map ->
            val response = map.entries.first()
            val permission = response.key
            val isGranted = response.value
            if (isGranted) {
                permissionGranted(permission)
            } else {
                permissionDenied(permission)
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

        // Inflate the layout for this fragment
        binding = SignInFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        binding.roomName.editText?.setText((activity as SignInActivity).roomName)
        binding.personName.editText?.setText((activity as SignInActivity).name)

        binding.progressBar.visibility = View.GONE

        binding.joinButton.setOnClickListener {
            getRoom(binding.roomName.editText?.text.toString())
        }

        binding.createButton.setOnClickListener {
            createRoom(binding.roomName.editText?.text.toString())
        }

        binding.settingsButton.setOnClickListener {
            val action = SignInFragmentDirections.actionSignInFragmentToSettingsFragment()
            findNavController().navigate(action)
        }

        displayVersions()

        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.resource_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            binding.resourceSpinner.adapter = adapter
            binding.resourceSpinner.onItemSelectedListener = this
        }

        audio = hasPermission(Manifest.permission.RECORD_AUDIO)
        video = hasPermission(Manifest.permission.CAMERA)

        setSpinnerSelection()
    }

    private fun getRoom(roomName: String) {
        if (!roomName.isEmpty()) {
            binding.progressBar.visibility = View.VISIBLE
            binding.joinButton.isClickable = false
            binding.createButton.isClickable = false

            viewModel.getRoom(roomName, RealtimeSettings.applicationServer()) { room, error ->
                binding.progressBar.visibility = View.GONE
                binding.joinButton.isClickable = true
                binding.createButton.isClickable = true

                room?.also {
                    Log.d(TAG, "room retrieved - name: ${room.room.name}, domain: ${room.domain}, token: ${room.room.token}")
                    navigateToRoom(room)
                }
                error?.also {
                    Log.e(TAG, "error: $error")
                    (activity as SignInActivity).showAlert("Error", error)
                }
            }
        } else {
            showMessage(getText(R.string.room_name_missing).toString())
        }
    }

    private fun createRoom(roomName: String) {
        if (!roomName.isEmpty()) {
            binding.progressBar.visibility = View.VISIBLE
            binding.createButton.isClickable = false
            binding.createButton.isClickable = false
            viewModel.createRoom(roomName, RealtimeSettings.applicationServer()) { room, error ->
                binding.progressBar.visibility = View.GONE
                binding.createButton.isClickable = true
                binding.createButton.isClickable = true

                room?.also {
                    Log.d(TAG, "room retrieved - name: ${room.room.name}, domain: ${room.domain}, token: ${room.room.token}")
                    navigateToRoom(room)
                }
                error?.also {
                    Log.e(TAG, "error: $error")
                    (activity as SignInActivity).showAlert("Error", error)
                }
            }
        } else {
            showMessage(getText(R.string.room_name_missing).toString())
        }
    }

    private fun navigateToRoom(room: Room) {
        (activity as SignInActivity).roomName = room.room.name
        (activity as SignInActivity).name = binding.personName.editText?.text.toString()

        if (!audio && !video) {
            (activity as SignInActivity).showAlert("Error", "Unable to connect to ${room.room.name}. Neither audio nor video permissions were granted")
            return
        }

        val action = SignInFragmentDirections.actionSignInFragmentToRoomActivity(
            token = room.room.token,
            roomName = room.room.name,
            name = binding.personName.editText?.text.toString(),
            audio = audio,
            video = video,
            host = room.domain,
            country = binding.country.editText?.text.toString()
        )
        findNavController().navigate(action)
    }

    private fun showMessage(message: String) {
        view?.let { context ->
            Snackbar.make(context, message, Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun setSpinnerSelection() {
        var pos = 0
        if (audio && video) {
            pos = 2
        } else if (video) {
            pos = 1
        } else if (audio) {
            pos = 0
        }
        binding.resourceSpinner.setSelection(pos)
        Log.d(TAG, "Spinner item ($pos) selected")
    }

    private fun displayVersions() {
        context?.let { ctx ->
            val manager = ctx.packageManager
            try {
                // This gets the application version. We want the individual versions of the demo and and the sdk.
                // To do that we add 'VERSION_NAME' to each of their build.gradle files
                binding.appVersionText.setText(getString(R.string.app_version, BuildConfig.VERSION_NAME))
                binding.sdkVersionText.setText(getString(R.string.sdk_version, net.atos.vcs.realtime_sdk.BuildConfig.VERSION_NAME))
            } catch (e: Exception) {
                Log.e(TAG, "Error getting package versions: ${e.message ?: e}")
            }
        }
    }

    private fun hasPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(requireContext(), permission) == PackageManager.PERMISSION_GRANTED
    }

    private fun checkPermission(permission: String) {
        when {
            hasPermission(permission) -> {
                // You can use the API that requires the permission.
            }
            shouldShowRequestPermissionRationale(permission) -> {
                // In an educational UI, explain to the user why your app requires this
                // permission for a specific feature to behave as expected. In this UI,
                // include a "cancel" or "no thanks" button that allows the user to
                // continue using your app without granting the permission.
                showPermissionDialog(permission)
            }
            else -> {
                // You can directly ask for the permission.
                // The registered ActivityResultCallback gets the result of this request.
                requestPermissionLauncher.launch(arrayOf(permission))
            }
        }
    }

    private fun showPermissionDialog(permission: String) {
        AlertDialog.Builder(requireContext())
            .setTitle("Permission $permission Required")
            .setMessage("Permit application to use $permission")
            .setPositiveButton("Grant") { dialog, _ ->
                dialog.dismiss()
                requestPermissionLauncher.launch(arrayOf(permission))
            }
            .setNegativeButton("Deny") { dialog, _ ->
                dialog.dismiss()
                permissionDenied(permission)
            }
            .show()
    }
    private fun permissionGranted(permission: String) {
        Log.i(TAG, "Permission granted: $permission")
        when (permission) {
            Manifest.permission.CAMERA -> { video = true }
            Manifest.permission.RECORD_AUDIO -> { audio = true }
            else -> {}
        }
        setSpinnerSelection()
    }

    private fun permissionDenied(permission: String) {
        Log.i(TAG, "Permission denied: $permission")
        when (permission) {
            Manifest.permission.CAMERA -> { video = false}
            Manifest.permission.RECORD_AUDIO -> { audio = false}
            else -> {}
        }
        setSpinnerSelection()
    }

    override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
        // An item was selected.
        Log.d(TAG, "Spinner item ($pos) was selected")
        when(pos) {
            0 -> {
                audio = true
                video = false
                checkPermission(Manifest.permission.RECORD_AUDIO)
            }
            1 -> {
                audio = false
                video = true
                checkPermission(Manifest.permission.CAMERA)
            }
            2 -> {
                audio = true
                video = true
                checkPermission(Manifest.permission.RECORD_AUDIO)
                checkPermission(Manifest.permission.CAMERA)
            }
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>) {
        // Another interface callback
        Log.d(TAG, "Nothing selected")
    }

}
