package net.atos.vcs.realtime.demo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import net.atos.vcs.realtime.demo.applicationServer.ApplicationRepository
import net.atos.vcs.realtime.demo.applicationServer.Config
import net.atos.vcs.realtime.demo.applicationServer.Room
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.lang.Exception

class SignInViewModel() : ViewModel() {
    private val TAG = "${this.javaClass.kotlin.simpleName}"

    private val appRepository = ApplicationRepository()

    fun getConfiguration(callback: (config: Config?, error: String?) -> Unit) {
        viewModelScope.launch {
            try {
                val config = appRepository.getConfig()
                callback(config, null)
            } catch (e: Exception) {
                callback(null, e.localizedMessage)
            }
        }
    }

    fun getWaitingRoom(roomName: String, callback: (room: Room?, error: String?) -> Unit) {
        viewModelScope.launch {
            try {
                val room = appRepository.getRoom(roomName)
                callback(room, null)
            } catch (httpErr: HttpException) {
                when (httpErr.code()) {
                    404 -> callback(null, "Room not found")
                    else -> callback(null, httpErr.localizedMessage)
                }
            } catch (e: Exception) {
                callback(null, e.localizedMessage)
            }
        }
    }

    fun createWaitingRoom(roomName: String, callback: (room: Room?, error: String?) -> Unit) {
        viewModelScope.launch {
            try {
                val room = appRepository.createRoom(roomName)
                callback(room, null)
            } catch (httpErr: HttpException) {
                when (httpErr.code()) {
                    409 -> callback(null, "Room already exists")
                    else -> callback(null, httpErr.localizedMessage)
                }
            } catch (e: Exception) {
                callback(null, e.localizedMessage)
            }
        }
    }

}
