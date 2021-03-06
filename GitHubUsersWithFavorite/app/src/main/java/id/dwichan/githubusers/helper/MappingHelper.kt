package id.dwichan.githubusers.helper

import android.database.Cursor
import id.dwichan.githubusers.database.DatabaseContract.FavoriteColumns.Companion.AVATAR_URL
import id.dwichan.githubusers.database.DatabaseContract.FavoriteColumns.Companion.FULLNAME
import id.dwichan.githubusers.database.DatabaseContract.FavoriteColumns.Companion.LOGIN_USERNAME
import id.dwichan.githubusers.database.DatabaseContract.FavoriteColumns.Companion.PUBLIC_REPOS
import id.dwichan.githubusers.items.UserItems

object MappingHelper {
    fun mapCursorToArrayList(favoriteCursor: Cursor?): ArrayList<UserItems> {
        val favoriteList = ArrayList<UserItems>()
        favoriteCursor?.apply {
            while (moveToNext()) {
                val login = getString(getColumnIndexOrThrow(LOGIN_USERNAME))
                val fullName = getString(getColumnIndexOrThrow(FULLNAME))
                val avatarUrl = getString(getColumnIndexOrThrow(AVATAR_URL))
                val publicRepos = getInt(getColumnIndexOrThrow(PUBLIC_REPOS))
                favoriteList.add(UserItems(login, fullName, avatarUrl, publicRepos))
            }
        }

        return favoriteList
    }
}