package id.dwichan.githubconsumerapps.items

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserItems(
    var login: String? = null,
    var name: String? = null,
    var avatar: String? = null,
    var public_repos: Int = 0
) : Parcelable