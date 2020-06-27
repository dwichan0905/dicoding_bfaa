package id.dwichan.quotesapi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    companion object {
        private val TAG = MainActivity::class.java.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getRandomQuotes()

        btnShowAll.setOnClickListener {
            startActivity(Intent(this@MainActivity, ListQuotesActivity::class.java))
        }
    }

    fun getRandomQuotes() {
        progressBar.visibility = View.VISIBLE
        val client = AsyncHttpClient()
        val url = "https://programming-quotes-api.herokuapp.com/quotes/random"
        client.get(url, object: AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?
            ) {
                // ini kalo berhasil konek
                progressBar.visibility = View.INVISIBLE
                val result = responseBody?.let { String(it) }
                Log.d(TAG, result)

                try {
                    val responseObject = JSONObject(result)

                    val quote = responseObject.getString("en")
                    val author = responseObject.getString("author")

                    tvQuotes.text = quote
                    tvAuthor.text = author
                } catch (e: Exception) {
                    Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable?
            ) {
                // ini kalo gagal
                progressBar.visibility = View.INVISIBLE

                val errMessage = when (statusCode) {
                    401 -> "$statusCode: Bad Request"
                    403 -> "$statusCode: Akses dibaasi"
                    404 -> "$statusCode: Tidak ditemukan"
                    else -> "$statusCode: ${error?.message}"
                }

                Toast.makeText(this@MainActivity, errMessage, Toast.LENGTH_SHORT).show()
            }

        })
    }
}