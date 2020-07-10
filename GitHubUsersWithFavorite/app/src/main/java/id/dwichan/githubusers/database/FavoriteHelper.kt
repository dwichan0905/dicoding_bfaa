package id.dwichan.githubusers.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import id.dwichan.githubusers.database.DatabaseContract.FavoriteColumns.Companion.FULLNAME
import id.dwichan.githubusers.database.DatabaseContract.FavoriteColumns.Companion.LOGIN_USERNAME
import id.dwichan.githubusers.database.DatabaseContract.FavoriteColumns.Companion.TABLE_NAME
import id.dwichan.githubusers.database.DatabaseContract.FavoriteColumns.Companion.TIMESTAMP

class FavoriteHelper(context: Context) {
    companion object {
        private const val DATABASE_TABLE = TABLE_NAME
        private lateinit var databaseHelper: DatabaseHelper
        private var INSTANCE: FavoriteHelper? = null
        private lateinit var database: SQLiteDatabase

        fun getInstance(context: Context): FavoriteHelper =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: FavoriteHelper(context)
            }
    }

    init {
        databaseHelper = DatabaseHelper(context)
    }

    @Throws(SQLiteException::class)
    fun openDatabase() {
        database = databaseHelper.writableDatabase
    }

    fun closeDatabase() {
        databaseHelper.close()
        if (database.isOpen) database.close()
    }

    fun isDatabaseOpened(): Boolean = database.isOpen

    fun insert(values: ContentValues?): Long {
        return database.insert(DATABASE_TABLE, null, values)
    }

    fun delete(login: String): Int {
        // DELETE FROM $DATABASE_TABLE WHERE $LOGIN_USERNAME = $login
        return database.delete(DATABASE_TABLE, "$LOGIN_USERNAME = ?", arrayOf(login))
    }

    fun queryShowAll(): Cursor {
        // SELECT * FROM $TABLE_NAME ORDER BY $TIMESTAMP DESC
        return database.query(
            DATABASE_TABLE,
            null, null,
            null, null,
            null, "$TIMESTAMP ASC"
        )
    }

    fun queryByUsername(login: String?): Cursor {
        // SELECT * FROM $DATABASE_TABLE WHERE $LOGIN_USERNAME = $login OR $FULLNAME = $login ORDER BY $TIMESTAMP DESC
        return database.query(
            DATABASE_TABLE, null,
            "$LOGIN_USERNAME LIKE ? OR $FULLNAME LIKE ?", arrayOf(login, login),
            null, null, "$TIMESTAMP DESC"
        )
    }

}