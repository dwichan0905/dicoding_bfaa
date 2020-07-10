package id.dwichan.githubusers.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import id.dwichan.githubusers.database.DatabaseContract.FavoriteColumns.Companion.AVATAR_URL
import id.dwichan.githubusers.database.DatabaseContract.FavoriteColumns.Companion.FULLNAME
import id.dwichan.githubusers.database.DatabaseContract.FavoriteColumns.Companion.LOGIN_USERNAME
import id.dwichan.githubusers.database.DatabaseContract.FavoriteColumns.Companion.PUBLIC_REPOS
import id.dwichan.githubusers.database.DatabaseContract.FavoriteColumns.Companion.TABLE_NAME
import id.dwichan.githubusers.database.DatabaseContract.FavoriteColumns.Companion.TIMESTAMP

internal class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_NAME = "dbGithubUsers"
        private const val DATABASE_VERSION = 1
        private val SQL_CREATE_TABLE_FAVORITE = """
            CREATE TABLE $TABLE_NAME (
            $LOGIN_USERNAME text not null primary key,
            $FULLNAME text,
            $AVATAR_URL text not null,
            $PUBLIC_REPOS int,
            $TIMESTAMP timestamp)
        """.trimIndent()
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(SQL_CREATE_TABLE_FAVORITE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }
}