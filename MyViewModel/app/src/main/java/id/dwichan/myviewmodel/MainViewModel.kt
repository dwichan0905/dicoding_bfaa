package id.dwichan.myviewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONObject
import java.text.DecimalFormat

class MainViewModel: ViewModel() {

    val listWeathers = MutableLiveData<ArrayList<WeatherItems>>()

    companion object {
        const val API_KEY = "545d07533ee80332c22724bf63e30b6b"
    }

    fun setWeathers(cities: String) {
        // request API
        val listItems = ArrayList<WeatherItems>()
        val url = "https://api.openweathermap.org/data/2.5/group?id=$cities&units=metric&appid=${API_KEY}"

        val client = AsyncHttpClient()
        client.get(url, object: AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?
            ) {
                try {
                    val result = responseBody?.let { String(it) }
                    val responseObject = JSONObject(result)
                    val list = responseObject.getJSONArray("list")

                    for (i in 0 until list.length()) {
                        val weather = list.getJSONObject(i)
                        val weatherItems = WeatherItems()
                        weatherItems.id = weather.getInt("id")
                        weatherItems.name = weather.getString("name")
                        weatherItems.currentWeather = weather.getJSONArray("weather").getJSONObject(0).getString("main")
                        weatherItems.description = weather.getJSONArray("weather").getJSONObject(0).getString("description")

                        val tempInKelvin = weather.getJSONObject("main").getDouble("temp")
                        val tempInCelcius = tempInKelvin - 273
                        weatherItems.temperature = DecimalFormat("##.##").format(tempInCelcius)

                        listItems.add(weatherItems)
                    }

                    // set data ke adapter
                    listWeathers.postValue(listItems)
                } catch (e: Exception) {
                    Log.d(MainActivity.TAG,"Exception: ${e.message.toString()}")
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable?
            ) {
                Log.d(MainActivity.TAG, "onFailure: ${error?.message.toString()}")
            }

        })
    }

    fun getWeathers(): LiveData<ArrayList<WeatherItems>> {
        return listWeathers
    }

}