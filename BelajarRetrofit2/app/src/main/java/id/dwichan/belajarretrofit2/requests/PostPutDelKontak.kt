package id.dwichan.belajarretrofit2.requests

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import id.dwichan.belajarretrofit2.model.Kontak
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PostPutDelKontak (
    @SerializedName("status")
    var status: String? = null,

    @SerializedName("result")
    var mKontak: Kontak,

    @SerializedName("message")
    var message: String? = null
): Parcelable