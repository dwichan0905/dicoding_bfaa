package id.dwichan.githubusers.database

import android.net.Uri
import android.provider.BaseColumns

object DatabaseContract {

    const val AUTHORITY = "id.dwichan.githubusers"
    const val SCHEME = "content"

    internal class FavoriteColumns : BaseColumns {
        companion object {
            const val TABLE_NAME = "favorite"
            const val LOGIN_USERNAME = "login_username"
            const val FULLNAME = "fullname"
            const val AVATAR_URL = "avatar_url"
            const val PUBLIC_REPOS = "public_repos"
            const val TIMESTAMP = "timestamp"

            // content://id.dwichan.githubusers/favorite
            val CONTENT_URI: Uri = Uri.Builder()
                .scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_NAME)
                .build()
        }
    }
}