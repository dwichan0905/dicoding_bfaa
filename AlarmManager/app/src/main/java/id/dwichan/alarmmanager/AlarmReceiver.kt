package id.dwichan.alarmmanager

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class AlarmReceiver : BroadcastReceiver() {

    companion object {
        const val TYPE_ONE_TIME = "OneTimeAlarm"
        const val TYPE_REPEATING = "RepeatingAlarm"
        const val EXTRA_MESSAGE = "message"
        const val EXTRA_TYPE = "type"

        // Alarm ID
        private const val ID_ONE_TIME = 100
        private const val ID_REPEATING = 101

        // DateTime format
        private const val DATE_FORMAT = "yyyy-MM-dd"
        private const val TIME_FORMAT = "HH:mm"
    }

    override fun onReceive(context: Context, intent: Intent) {
        // This method is called when the BroadcastReceiver is receiving an Intent broadcast.
        val type = intent.getStringExtra(EXTRA_TYPE)
        val message = intent.getStringExtra(EXTRA_MESSAGE)

        val title = if (type.equals(TYPE_ONE_TIME, ignoreCase = true)) TYPE_ONE_TIME else TYPE_REPEATING
        val notifyId = if (type.equals(TYPE_ONE_TIME, ignoreCase = true)) ID_ONE_TIME else ID_REPEATING

        showToastMessage(context, title, message)
        showAlarmNotify(context, title, message, notifyId)
    }

    // Untuk menampilkan notifikasi sistem
    private fun showAlarmNotify(context: Context, title: String, message: String?, notifyId: Int) {
        // Atur channel
        val CHANNEL_ID = "Channel_1"
        val CHANNEL_NAME = "AlarmManager Channel"

        // Bangun Notifikasi mulai dari icon, judul, isi, warna, vibrasi, suara, dll
        val notifyManagerCompat = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_baseline_access_time_24)
            .setContentTitle(title)
            .setContentText(message)
            .setColor(ContextCompat.getColor(context, android.R.color.transparent))
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
            .setSound(alarmSound)

        // khusus android O ke atas
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)
            channel.enableVibration(true)
            channel.vibrationPattern = longArrayOf(1000, 1000, 1000, 1000, 1000)

            builder.setChannelId(CHANNEL_ID)

            notifyManagerCompat.createNotificationChannel(channel)
        }

        // Munculkan notifikasi!
        val notification = builder.build()
        notifyManagerCompat.notify(notifyId, notification)
    }

    // untuk atur alarm sekali
    fun setOneTimeAlarm(context: Context, type: String, date: String, time: String, message: String?) {
        // cek takutnya tanggal/jam tidak sesuai format
        if (isDateInvalid(date, DATE_FORMAT) || isDateInvalid(time, TIME_FORMAT)) return

        // ambil service milik system (Alarm Service)
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        // set target untuk alarm ketika di trigger oleh sistem melalui alarm
        // bisa ke activity secara langsung menggunakan intent berikut
        val intent = Intent(context, AlarmReceiver::class.java)
        intent.putExtra(EXTRA_MESSAGE, message)
        intent.putExtra(EXTRA_TYPE, type)

        Log.e("ONE TIME", "$date $time")

        // Split string menjadi array
        val dateArray = date.split("-").toTypedArray()
        val timeArray = time.split(":").toTypedArray()

        // atur jadwal alarm
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, Integer.parseInt(dateArray[0]))
        calendar.set(Calendar.MONTH, Integer.parseInt(dateArray[1]) - 1)
        calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dateArray[2]))
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeArray[0]))
        calendar.set(Calendar.MINUTE, Integer.parseInt(timeArray[1]))
        calendar.set(Calendar.SECOND, 0)

        val pendingIntent = PendingIntent.getBroadcast(context, ID_ONE_TIME, intent, 0)
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)

        Toast.makeText(context, "Alarm sekali telah berhasil diatur!", Toast.LENGTH_SHORT).show()
    }

    private fun isDateInvalid(date: String, format: String): Boolean {
        return try {
            val df = SimpleDateFormat(format, Locale.getDefault())
            df.isLenient = false
            df.parse(date)
            false
        } catch (e: ParseException) {
            true
        }
    }

    fun showToastMessage(context: Context, title: String, message: String?) {
        Toast.makeText(context, "$title: $message", Toast.LENGTH_SHORT).show()
    }
}
