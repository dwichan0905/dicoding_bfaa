package id.dwichan.services

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnStartService.setOnClickListener(this)
        btnStartIntentService.setOnClickListener(this)
        btnStartBoundService.setOnClickListener(this)
        btnStopBoundService.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btnStartService -> {
                val mStartServiceIntent = Intent(this@MainActivity, MyService::class.java)
                startService(mStartServiceIntent)
            }

            R.id.btnStartIntentService -> {
                val mStartIntentService = Intent(this@MainActivity, MyIntentService::class.java)
                mStartIntentService.putExtra(MyIntentService.EXTRA_DURATION, 5000L)
                startService(mStartIntentService)
            }

            R.id.btnStartBoundService -> {

            }

            R.id.btnStopBoundService -> {

            }
        }
    }
}