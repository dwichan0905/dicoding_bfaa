package id.dwichan.quotesapi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.activity_list_quotes.*
import org.json.JSONArray

class ListQuotesActivity : AppCompatActivity() {

    companion object {
        private val TAG = ListQuotesActivity::class.java.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_quotes)

        supportActionBar?.title = "Daftar Kutipan"

        getListQuotes()
    }

    fun getListQuotes () {
        progressBar.visibility = View.VISIBLE
        val client = AsyncHttpClient()
        val url = "https://programming-quotes-api.herokuapp.com/quotes/page/1"
        client.get(url, object: AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?
            ) {
                progressBar.visibility = View.INVISIBLE

                // parse JSON
                // Pake loop buat dapetin semua data dalam JSONArray
                val listQuotes = ArrayList<String>()

                val result = responseBody?.let { String(it) }
                Log.d(TAG, result)
                try {
                    val jsonArray = JSONArray(result)
                    for (i in 0 until jsonArray.length()) {
                        val jsonObject = jsonArray.getJSONObject(i)
                        val quote = jsonObject.getString("en")
                        val author = jsonObject.getString("author")
                        listQuotes.add("\n$quote - $author\n")
                    }

                    val adapter = ArrayAdapter(this@ListQuotesActivity, android.R.layout.simple_list_item_1, listQuotes)
                    lvListQuotes.adapter = adapter
                } catch (e: Exception) {
                    Toast.makeText(this@ListQuotesActivity, e.message, Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable?
            ) {
                progressBar.visibility = View.INVISIBLE

                val errMessage = when (statusCode) {
                    401 -> "$statusCode: Bad Request"
                    403 -> "$statusCode: Akses dibaasi"
                    404 -> "$statusCode: Tidak ditemukan"
                    else -> "$statusCode: ${error?.message}"
                }

                Toast.makeText(this@ListQuotesActivity, errMessage, Toast.LENGTH_SHORT).show()
            }

        })
    }
}