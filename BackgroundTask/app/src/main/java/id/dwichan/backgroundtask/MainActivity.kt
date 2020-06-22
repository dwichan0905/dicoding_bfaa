package id.dwichan.backgroundtask

import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.ref.WeakReference

class MainActivity : AppCompatActivity(), MyAsyncCallback {

    companion object {
        private const val INPUT_STRING = "Hai dari AsyncTask!"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val demoAsync = DemoAsync(this)
        demoAsync.execute(INPUT_STRING)
    }

    override fun onPreExecute() {
        tvStatus.text = getString(R.string.status_pre)
        tvDesc.text = INPUT_STRING
    }

    override fun onPostExecute(text: String) {
        tvStatus.text = getString(R.string.status_post)
        tvDesc.text = text
    }

    private class DemoAsync(myListener: MyAsyncCallback): AsyncTask<String, Void, String>() {
        companion object {
            private const val LOG_ASYNC = "DemoAsync"
        }

        private val myListener2: WeakReference<MyAsyncCallback> = WeakReference(myListener)

        override fun onPreExecute() {
            super.onPreExecute()
            Log.d(LOG_ASYNC, "status: onPreExecute()")

            val myListener = myListener2.get()
            myListener?.onPreExecute()
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)

            Log.d(LOG_ASYNC, "status: onPostExecute()")

            val myListener = this.myListener2.get()
            myListener?.onPostExecute(result.toString())
        }

        override fun doInBackground(vararg params: String?): String {
            Log.d(LOG_ASYNC, "status: doInBackground()")

            var output: String? = null

            try {
                val input = params[0]
                output = "$input Selamat belajar!"
                Thread.sleep(2000)
            } catch (e: Exception) {
                Log.d(LOG_ASYNC, e.message)
            }

            return output.toString()
        }
    }
}

internal interface MyAsyncCallback {
    fun onPreExecute()
    fun onPostExecute(text: String)
}