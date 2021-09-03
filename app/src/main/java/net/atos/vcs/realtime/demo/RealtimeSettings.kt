package net.atos.vcs.realtime.sdk

import android.content.Context
import androidx.preference.PreferenceManager
import java.lang.ref.WeakReference

object RealtimeSettings {
    private var context: WeakReference<Context>? = null

    fun initialize(context: Context) {
        this.context = WeakReference(context)
    }

    fun autoGainControl(): Boolean {
        return sharedBooleanPreference("auto_gain_control", true)
    }

    fun defaultHdVideo(): Boolean {
        return sharedBooleanPreference("default_hd_video", true)
    }

    fun delayLocalStream(): Boolean {
        return sharedBooleanPreference("delay_local_stream", false)
    }

    fun onlyRelayCandidates(): Boolean {
        return sharedBooleanPreference("only_relay_candidates", false)
    }

    fun monitorQoS(): Boolean {
        return sharedBooleanPreference("monitor_qos", false)
    }

    fun applicationServer (): String {
        return sharedStringPreference("application_server", "")
    }

    fun username (): String {
        return sharedStringPreference("username", "")
    }

    fun username(value: String) {
        setSharedStringPreference("username", value)
    }

    fun password (): String {
        return sharedStringPreference("password", "")
    }

    fun password(value: String) {
        setSharedStringPreference("password", value)
    }

    private fun sharedBooleanPreference(key: String, default: Boolean = false): Boolean {
        context?.get()?.let { context ->
            val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
            return sharedPreferences.getBoolean(key, default)
        }
        return default
    }

    private fun sharedStringPreference(key: String, default: String): String {
        context?.get()?.let { context ->
            val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
            return sharedPreferences.getString(key, default) ?: default
        }
        return default
    }

    private fun setSharedStringPreference(key: String, value: String) {
        context?.get()?.let { context ->
            val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
            sharedPreferences.edit().putString(key, value).commit()
        }
    }
}
