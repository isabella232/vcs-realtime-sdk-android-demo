package net.atos.vcs.realtime.demo

import android.os.Bundle
import android.text.InputType
import android.view.View
import androidx.preference.EditTextPreference
import androidx.preference.PreferenceFragmentCompat

class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val pw = preferenceScreen.preferenceManager.findPreference<EditTextPreference>("password")
        pw?.setOnBindEditTextListener {
            it.inputType = InputType.TYPE_CLASS_TEXT + InputType.TYPE_TEXT_VARIATION_PASSWORD
        }
    }
}