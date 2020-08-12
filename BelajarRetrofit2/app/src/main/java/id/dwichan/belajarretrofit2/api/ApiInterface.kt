package id.dwichan.belajarretrofit2.api

import id.dwichan.belajarretrofit2.requests.GetKontak
import id.dwichan.belajarretrofit2.requests.PostPutDelKontak
import retrofit2.Call
import retrofit2.http.*

interface ApiInterface {
    // ini endpoint dengan request GET
    @GET("kontak")
    fun getKontak(): Call<GetKontak>

    // ini endpoint dengan request POST
    @FormUrlEncoded
    @POST("kontak")
    fun postKontak(
        @Field("nama") nama: String,
        @Field("nomor") nomor: String
    ): Call<PostPutDelKontak>

    // ini endpoint dengan request PUT
    @FormUrlEncoded
    @PUT("kontak")
    fun putKontak(
        @Field("id") id: String,
        @Field("nama") nama: String,
        @Field("nomor") nomor: String
    ): Call<PostPutDelKontak>

    // ini endpoint dengan request DELETE
    @FormUrlEncoded
    @HTTP(method = "DELETE", path = "kontak", hasBody = true)
    fun deleteKontak(
        @Field("id") id: String
    ): Call<PostPutDelKontak>
}