package id.dwichan.firebasecloudmessaging

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object {
        private val TAG = MainActivity::class.java.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnSubscribe.setOnClickListener {
            FirebaseMessaging.getInstance().subscribeToTopic("news")
            val msg = getString(R.string.msg_subscribed)
            Log.d(TAG, msg)
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
        }

        btnToken.setOnClickListener {
            FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener {instanceIdResult ->
                val deviceToken = instanceIdResult.token
                val msg = getString(R.string.msg_token_fmt, deviceToken)
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
                Log.d(TAG, "Refresh token: $deviceToken")
            }
        }
    }
}