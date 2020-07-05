package id.dwichan.notes.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import id.dwichan.notes.db.DatabaseContract.NoteColumns.Companion.TABLE_NAME

internal class DatabaseHelper(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VER) {
    companion object {
        private const val DATABASE_NAME = "dbnoteapp"
        private const val DATABASE_VER = 1
        private val SQL_CREATE_TABLE_NOTE = """
            CREATE TABLE $TABLE_NAME (
            ${DatabaseContract.NoteColumns._ID} INTEGER PRIMARY KEY AUTOINCREMENT,
            ${DatabaseContract.NoteColumns.TITLE} TEXT NOT NULL,
            ${DatabaseContract.NoteColumns.DESCRIPTION} TEXT NOT NULL,
            ${DatabaseContract.NoteColumns.DATE} TEXT NOT NULL)
        """.trimIndent()
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(SQL_CREATE_TABLE_NOTE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }
}