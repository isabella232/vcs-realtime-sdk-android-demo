package net.atos.vcs.realtime.demo

import android.util.Base64
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import net.atos.vcs.realtime.demo.applicationServer.ApplicationRepository
import net.atos.vcs.realtime.demo.applicationServer.Room
import kotlinx.coroutines.launch
import net.atos.vcs.realtime.demo.applicationServer.Config
import java.lang.Exception

data class BasicAuthCredentials (
    val username: String,
    val password: String
)

class SignInViewModel() : ViewModel() {

    fun getConfig(applicationServer: String, callback: (config: Config?, error: Exception?) -> Unit) {
        viewModelScope.launch {
            try {
                val appRepository = ApplicationRepository(applicationServer)
                val config = appRepository.getConfig()
                callback(config, null)
            } catch (e: Exception) {
                callback(null, e)
            }
        }
    }

    fun getRoom(roomName: String, applicationServer: String, callback: (room: Room?, error: Exception?) -> Unit) {
        viewModelScope.launch {
            try {
                val appRepository = ApplicationRepository(applicationServer)
                val room = appRepository.getRoom(roomName)
                callback(room, null)
            } catch (e: Exception) {
                callback(null, e)
            }
        }
    }

    fun createRoom(roomName: String,
                   applicationServer: String,
                   basicAuth: BasicAuthCredentials?,
                   callback: (room: Room?, error: Exception?) -> Unit) {
        viewModelScope.launch {
            try {
                val appRepository = ApplicationRepository(applicationServer)
                val room = if (basicAuth != null) {
                    val authPayload = "${basicAuth.username}:${basicAuth.password}"
                    val data = authPayload.toByteArray()
                    val base64 = Base64.encodeToString(data, Base64.NO_WRAP)
                    appRepository.createRoom(roomName, "Basic $base64".trim())
                } else {
                    appRepository.createRoom(roomName)
                }
                callback(room, null)
            } catch (e: Exception) {
                callback(null, e)
            }
        }
    }
}
