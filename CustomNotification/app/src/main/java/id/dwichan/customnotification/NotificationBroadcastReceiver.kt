package id.dwichan.customnotification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.Toast
import androidx.core.app.NotificationCompat
import id.dwichan.customnotification.NotificationService.Companion.CHANNEL_ID
import id.dwichan.customnotification.NotificationService.Companion.CHANNEL_NAME
import id.dwichan.customnotification.NotificationService.Companion.REPLY_ACTION

class NotificationBroadcastReceiver : BroadcastReceiver() {

    companion object {
        private const val KEY_NOTIFICATION_ID = "key_notification_id"
        private const val KEY_MESSAGE_ID = "key_message_id"

        fun getReplyMessageIntent(context: Context, notificationId: Int, messageId: Int): Intent {
            val intent = Intent(context, NotificationBroadcastReceiver::class.java)
            intent.action = REPLY_ACTION
            intent.putExtra(KEY_NOTIFICATION_ID, notificationId)
            intent.putExtra(KEY_MESSAGE_ID, messageId)
            return intent
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        // This method is called when the BroadcastReceiver is receiving an Intent broadcast.
        if (REPLY_ACTION == intent.action) {
            val message = NotificationService.getReplyMessage(intent)
            val messageId = intent.getIntExtra(KEY_MESSAGE_ID, 0)

            Toast.makeText(context, "Message ID: $messageId\nMessage: $message", Toast.LENGTH_SHORT).show()

            val notifyId = intent.getIntExtra(KEY_NOTIFICATION_ID, 1)
            updateNotification(context, notifyId)
        }
    }

    private fun updateNotification(context: Context, notifyId: Int) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_baseline_notifications_24)
            .setContentTitle(context.getString(R.string.notif_title_sent))
            .setContentText(context.getString(R.string.notif_content_sent))

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)
            channel.enableVibration(true)
            channel.vibrationPattern = longArrayOf(1000, 1000, 1000, 1000, 1000)
            builder.setChannelId(CHANNEL_ID)
            notificationManager.createNotificationChannel(channel)
        }

        val notification = builder.build()
        notificationManager.notify(notifyId, notification)
    }
}
