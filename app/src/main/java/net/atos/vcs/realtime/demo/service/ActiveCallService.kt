package net.atos.vcs.realtime.demo.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.LifecycleService
import dagger.hilt.android.AndroidEntryPoint
import net.atos.vcs.realtime.demo.RoomActivity
import net.atos.vcs.realtime.demo.R
import net.atos.vcs.realtime.demo.RoomManager
import javax.inject.Inject

private const val ROOM_NAME_EXTRA = "ROOM_NAME_EXTRA"
private const val NOTIFICATION_CHANNEL_ID = "room_channel_id"
private const val NOTIFICATION_CHANNEL_NAME = "room_channel"
private const val NOTIFICATION_ID = 5150

@AndroidEntryPoint
class ActiveCallService : LifecycleService() {

    companion object {
        fun startService(context: Context, roomName: String) {
            Intent(context, ActiveCallService::class.java).let { intent ->
                intent.putExtra(ROOM_NAME_EXTRA, roomName)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    context.startForegroundService(intent)
                } else {
                    context.startService(intent)
                }
            }
        }

        fun stopService(context: Context) {
            Intent(context, ActiveCallService::class.java).let { context.stopService(it) }
        }

        private const val TAG = "ActiveCallService"
    }

    private lateinit var notificationManager: NotificationManagerCompat

    private val pendingIntent
        get() =
            Intent(this, RoomActivity::class.java).let { notificationIntent ->
                notificationIntent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
                PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE)
            }

    @Inject
    lateinit var roomManager: RoomManager

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate")
        notificationManager = NotificationManagerCompat.from(this)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        val roomName = intent?.getStringExtra(ROOM_NAME_EXTRA)
        Log.d(TAG, "onStartCommand - room name: $roomName")
        createNotificationChannel()
        startForeground(NOTIFICATION_ID, getNotification(roomName))
        return START_NOT_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy")
    }

    override fun onBind(intent: Intent): IBinder? {
        super.onBind(intent)
        return null
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel(
                    NOTIFICATION_CHANNEL_ID,
                    NOTIFICATION_CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_LOW
                ).apply { lockscreenVisibility = Notification.VISIBILITY_PUBLIC }

            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun getNotification(roomName: String?) : Notification =
        NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setAutoCancel(false)
            .setOngoing(true)
            .setSmallIcon(R.mipmap.ic_launcher_foreground)
            .setContentTitle("Ongoing call${if(roomName != null) { " in $roomName" } else { "" }}")
            .setContentText("Tap to return to the room")
            .setTicker("Tap to return to the room")
            .setContentIntent(pendingIntent)
            .build()
}