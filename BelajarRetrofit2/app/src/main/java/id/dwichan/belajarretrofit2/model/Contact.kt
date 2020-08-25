package id.dwichan.belajarretrofit2.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Contact(
    @SerializedName("id")
    var id: String? = null,

    @SerializedName("nama")
    var nama: String? = null,

    @SerializedName("nomor")
    var nomor: String? = null
) : Parcelable