package id.dwichan.myviewmodel

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import java.text.DecimalFormat

class MainActivity : AppCompatActivity() {

    private var adapter = WeatherAdapter()
    private lateinit var mainViewModel: MainViewModel

    companion object {
        val TAG = MainActivity::class.java.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        adapter = WeatherAdapter()
        adapter.notifyDataSetChanged()

        recView.layoutManager = LinearLayoutManager(this)
        recView.adapter = adapter

        btnCity.setOnClickListener {
            val city = editCity.text.toString()
            if (city.isEmpty()) return@setOnClickListener
            showLoading(true)
            mainViewModel.setWeathers(city)
        }

        mainViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(MainViewModel::class.java)

        mainViewModel.getWeathers().observe(this, Observer { weatherItems ->
            if (weatherItems != null) {
                adapter.setData(weatherItems)
                showLoading(false)
            }
        })
    }

    private fun setWeather(cities: String) {

    }

    private fun showLoading(state: Boolean) {
        when (state) {
            true -> progressBar.visibility = View.VISIBLE
            false -> progressBar.visibility = View.GONE
        }
    }

}