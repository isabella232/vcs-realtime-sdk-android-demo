package net.atos.vcs.realtime.demo

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.text.Layout
import android.text.SpannableString
import android.text.TextWatcher
import android.text.style.AlignmentSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import net.atos.vcs.realtime.sdk.RealtimeSettings

class CredentialsDialog : DialogFragment() {

    private val TAG = "${this.javaClass.kotlin.simpleName}"

    private var dialogView: View? = null

    // Use this instance of the interface to deliver action events
    var listener: CredentialsDialogListener? = null

    interface CredentialsDialogListener {
        fun onDialogPositiveClick(dialog: DialogFragment, username: String, password: String)
        fun onDialogNegativeClick(dialog: DialogFragment)
    }

    // Override to ensure onViewCreated is called
    // see: https://stackoverflow.com/questions/10594992/android-dialogfragment-onviewcreated-not-called
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return dialogView ?: inflater.inflate(R.layout.sign_in_dialog, null)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            // Get the layout inflater
            val inflater = requireActivity().layoutInflater;

            dialogView = inflater.inflate(R.layout.sign_in_dialog, null)

            // Inflate and set the layout for the dialog
            // Pass null as the parent view because its going in the dialog layout
            builder.setView(dialogView)
                // Add action buttons
                .setPositiveButton(R.string.create,
                    DialogInterface.OnClickListener { _, _ ->
                        if (save()) {
                            RealtimeSettings.username(username())
                            RealtimeSettings.password(password())
                        }
                        listener?.onDialogPositiveClick(this, username(), password())
                    })
                .setNegativeButton(R.string.cancel,
                    DialogInterface.OnClickListener { _, _ ->
                        listener?.onDialogNegativeClick(this)
                        getDialog()?.cancel()
                    })

            val title = SpannableString(getText(R.string.create_room).toString())
            title.setSpan(AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER), 0, title.length, 0)
            builder.setTitle(title)

            builder.create()
       } ?: throw IllegalStateException("Activity cannot be null")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set the username and password fields using settings values
        val username = RealtimeSettings.username()
        val password = RealtimeSettings.password()

        view.findViewById<EditText>(R.id.username)?.apply {
            setText(username)
        }

        view.findViewById<EditText>(R.id.password)?.apply {
            setText(password)
        }

        view.findViewById<CheckBox>(R.id.checkBox)?.apply {
            isChecked = username.isNotEmpty() || password.isNotEmpty()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        dialogView = null
    }

    private fun username(): String {
        return dialogView?.findViewById<EditText>(R.id.username)?.text.toString()
    }

    private fun password(): String {
        return dialogView?.findViewById<EditText>(R.id.password)?.text.toString()
    }

    private fun save(): Boolean {
        return dialogView?.findViewById<CheckBox>(R.id.checkBox)?.isChecked == true
    }

}