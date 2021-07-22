package net.atos.vcs.realtime.demo

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import net.atos.vcs.realtime.sdk.RealtimeSdk
import net.atos.vcs.realtime.sdk.LogWriter

private const val ROOM_NAME = "ROOM_NAME"
private const val NAME = "NAME"

@AndroidEntryPoint
class SignInActivity : AppCompatActivity() {

    var roomName: String = ""
    var name: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        roomName = savedInstanceState?.getString(ROOM_NAME, "") ?: ""
        name = savedInstanceState?.getString(NAME, "") ?: ""
        setContentView(R.layout.sign_in_activity)
        RealtimeSdk.initialize(applicationContext, null, LogWriter.LogLevel.DEBUG)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(ROOM_NAME, roomName)
        outState.putString(NAME, name)
    }

    fun showAlert(title: String?, message: String) {
        val alertDialog = AlertDialog.Builder(this@SignInActivity).create()
        alertDialog.setTitle(title ?: "Alert")
        alertDialog.setMessage(message)
        alertDialog.setButton(
            AlertDialog.BUTTON_NEUTRAL, "OK"
        ) { dialog, _ -> dialog.dismiss() }
        alertDialog.show()
    }

}