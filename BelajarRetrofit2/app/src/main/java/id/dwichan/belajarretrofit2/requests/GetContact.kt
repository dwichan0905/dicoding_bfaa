package id.dwichan.belajarretrofit2.requests

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import id.dwichan.belajarretrofit2.model.Contact
import kotlinx.android.parcel.Parcelize

@Parcelize
data class GetContact(
    @SerializedName("status")
    var status: String? = null,

    @SerializedName("result")
    var listContact: ArrayList<Contact>,

    @SerializedName("message")
    var message: String? = null
) : Parcelable