package net.atos.vcs.realtime.demo

import android.os.Bundle
import android.text.InputType
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.preference.EditTextPreference
import androidx.preference.PreferenceFragmentCompat
import com.google.android.material.appbar.MaterialToolbar

class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val toolbar = view.findViewById<MaterialToolbar>(R.id.toolbar)
        toolbar?.apply {
            title = "Settings"
            setTitleTextColor(resources.getColor(R.color.deep_blue))
            isTitleCentered = true
            setNavigationIcon(R.drawable.action_back)
            setNavigationIconTint(resources.getColor(R.color.deep_blue))
            setNavigationOnClickListener {
                val action = SettingsFragmentDirections.actionSettingsFragmentToSignInFragment()
                findNavController().navigate(action)
            }
        }

        val pw = preferenceScreen.preferenceManager.findPreference<EditTextPreference>("password")
        pw?.setOnBindEditTextListener {
            it.inputType = InputType.TYPE_CLASS_TEXT + InputType.TYPE_TEXT_VARIATION_PASSWORD
        }
    }
}