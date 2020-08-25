package id.dwichan.belajarretrofit2.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiClient {

    private var retrofit: Retrofit? = null

    companion object {
        const val BASE_URL = "http://192.168.8.102/kontak/"
    }

    fun getClient(): Retrofit? {
        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return retrofit
    }
}