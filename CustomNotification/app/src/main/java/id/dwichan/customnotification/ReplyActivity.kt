package id.dwichan.customnotification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.NotificationCompat
import id.dwichan.customnotification.NotificationService.Companion.CHANNEL_ID
import id.dwichan.customnotification.NotificationService.Companion.CHANNEL_NAME
import id.dwichan.customnotification.NotificationService.Companion.REPLY_ACTION
import kotlinx.android.synthetic.main.activity_reply.*

class ReplyActivity : AppCompatActivity() {

    private var mMessageId = 0
    private var mNotifyId = 0

    companion object {
        private const val KEY_MESSAGE_ID = "key_message_id"
        private const val KEY_NOTIFY_ID = "key_notify_id"

        fun getReplyMessageIntent(context: Context, notifyId: Int, messageId: Int) : Intent {
            val intent = Intent(context, ReplyActivity::class.java)
            intent.action = REPLY_ACTION
            intent.putExtra(KEY_MESSAGE_ID, messageId)
            intent.putExtra(KEY_NOTIFY_ID, notifyId)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reply)

        val intent = intent

        if (REPLY_ACTION == intent.action) {
            mMessageId = intent.getIntExtra(KEY_MESSAGE_ID, 0)
            mNotifyId = intent.getIntExtra(KEY_NOTIFY_ID, 0)
        }

        btnSend.setOnClickListener { sendMessage(mNotifyId, mMessageId) }
    }

    private fun sendMessage(notifyId: Int, messageId: Int) {
        updateNotification(notifyId)

        val message = edit_reply.text.toString().trim { it <= ' ' }
        Toast.makeText(this, "MessageID: $messageId\nMessage: $message", Toast.LENGTH_SHORT).show()
        finish()
    }

    private fun updateNotification(notifyId: Int) {
        val notificationManagerCompat = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_baseline_notifications_24)
            .setContentTitle(getString(R.string.notif_title_sent))
            .setContentText(getString(R.string.notif_content_sent))

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)
            channel.enableVibration(true)
            channel.vibrationPattern = longArrayOf(1000, 1000, 1000, 1000, 1000)
            builder.setChannelId(CHANNEL_ID)
            notificationManagerCompat.createNotificationChannel(channel)
        }

        val notification = builder.build()
        notificationManagerCompat.notify(notifyId, notification)
    }
}