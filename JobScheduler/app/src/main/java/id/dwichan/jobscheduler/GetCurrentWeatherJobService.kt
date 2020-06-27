package id.dwichan.jobscheduler

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Context
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONObject
import java.lang.Exception
import java.text.DecimalFormat

class GetCurrentWeatherJobService: JobService() {

    companion object {
        private val TAG = GetCurrentWeatherJobService::class.java.simpleName

        // Kotamu
        internal const val CITY = "Jakarta"

        // API Key
        internal const val APP_ID = "545d07533ee80332c22724bf63e30b6b"
    }

    override fun onStopJob(params: JobParameters?): Boolean {
        Log.d(TAG, "onStartJob()")
        return true
    }

    private fun getCurrentWeather(params: JobParameters?) {
        Log.d(TAG, "getCurrentWeather: Memulai...")

        val client = AsyncHttpClient()
        val url = "http://api.openweathermap.org/data/2.5/weather?q=$CITY&appid=$APP_ID"
        Log.d(TAG, "getCurrentWeather: URL $url")
        client.get(url, object: AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?
            ) {
                val result = responseBody?.let { String(it) }
                Log.d(TAG, result)
                try {
                    val responseObject = JSONObject(result)

                    val currentWeather = responseObject.getJSONArray("weather").getJSONObject(0).getString("main")
                    val description = responseObject.getJSONArray("weather").getJSONObject(0).getString("description")
                    val tempInKelvin = responseObject.getJSONObject("main").getDouble("temp")
                    
                    val tempInCelcius = tempInKelvin - 273
                    val temp = DecimalFormat("##.##").format(tempInCelcius)
                    
                    val title = "Cuaca hari ini"
                    val message = "$currentWeather, $description with $temp celcius"
                    val notifyId = 100
                    
                    showNotification(applicationContext, title, message, notifyId)
                    
                    Log.d(TAG, "onSuccess(): Selesai")
                    jobFinished(params, false)
                } catch (e: Exception) {
                    Log.d(TAG, "onSuccess(): Gagal")
                    jobFinished(params, true)
                    e.printStackTrace()
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable?
            ) {
                Log.d(TAG, "onFailure(): Gagal")
                jobFinished(params, true)
            }

        })
    }

    private fun showNotification(context: Context, title: String, message: String, notifyId: Int) {
        val CHANNEL_ID = "Channel_1"
        val CHANNEL_NAME = "Job Scheduler Channel"

        val notifyManagerCompat = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle(title)
            .setSmallIcon(R.drawable.ic_baseline_replay_24)
            .setContentText(message)
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
            .setSound(alarmSound)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)

            channel.enableVibration(true)
            channel.vibrationPattern = longArrayOf(1000, 1000, 1000, 1000, 1000)
            builder.setChannelId(CHANNEL_ID)
            notifyManagerCompat.createNotificationChannel(channel)
        }

        val notification = builder.build()
        notifyManagerCompat.notify(notifyId, notification)
    }

    override fun onStartJob(params: JobParameters?): Boolean {
        Log.d(TAG, "onStopJob()")
        getCurrentWeather(params)
        return true
    }

}