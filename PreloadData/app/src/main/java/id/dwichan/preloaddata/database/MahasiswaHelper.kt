package id.dwichan.preloaddata.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import id.dwichan.preloaddata.database.DatabaseContract.MahasiswaColumns.Companion.NAMA
import id.dwichan.preloaddata.database.DatabaseContract.MahasiswaColumns.Companion.NIM
import id.dwichan.preloaddata.database.DatabaseContract.MahasiswaColumns.Companion._ID
import id.dwichan.preloaddata.database.DatabaseContract.TABLE_NAME
import java.sql.SQLException

class MahasiswaHelper(context: Context) {
    private val databaseHelper = DatabaseHelper(context)
    private lateinit var database: SQLiteDatabase

    companion object {
        private var INSTANCE: MahasiswaHelper? = null
        fun getInstance(context: Context): MahasiswaHelper {
            if (INSTANCE == null) {
                synchronized(SQLiteOpenHelper::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = MahasiswaHelper(context)
                    }
                }
            }
            return INSTANCE as MahasiswaHelper
        }
    }

    @Throws(SQLException::class)
    fun open() {
        database = databaseHelper.writableDatabase
    }

    fun close() {
        databaseHelper.close()
        if (database.isOpen) database.close()
    }

    fun getAllData(): ArrayList<MahasiswaModel> {
        val cursor = database.query(TABLE_NAME,
            null,
            null,
            null,
            null,
            null,
            "$_ID ASC")

        cursor.moveToFirst()
        val arrayList = ArrayList<MahasiswaModel>()
        var mahasiswaModel: MahasiswaModel

        if (cursor.count > 0) {
            do {
                mahasiswaModel = MahasiswaModel()
                mahasiswaModel.id = cursor.getInt(cursor.getColumnIndexOrThrow(_ID))
                mahasiswaModel.nama = cursor.getString(cursor.getColumnIndexOrThrow(NAMA))
                mahasiswaModel.nim = cursor.getString(cursor.getColumnIndexOrThrow(NIM))

                arrayList.add(mahasiswaModel)
                cursor.moveToNext()
            } while (!cursor.isAfterLast)
        }
        cursor.close()
        return arrayList
    }

    fun insert(mahasiswaModel: MahasiswaModel): Long {
        val initValues = ContentValues()
        initValues.put(NAMA, mahasiswaModel.nama)
        initValues.put(NIM, mahasiswaModel.nim)

        return database.insert(TABLE_NAME, null, initValues)
    }

    fun getDataByName(nama: String): ArrayList<MahasiswaModel> {
        // disini query nya hehe
        val cursor = database.query(TABLE_NAME,
            null,
            "$NAMA LIKE ?",
            arrayOf(nama),
            null,
            "$_ID ASC",
            null)

        cursor.moveToFirst()
        val arrayList = ArrayList<MahasiswaModel>()
        var mahasiswaModel: MahasiswaModel

        if (cursor.count > 0) {
            do {
                mahasiswaModel = MahasiswaModel()
                mahasiswaModel.id = cursor.getInt(cursor.getColumnIndexOrThrow(_ID))
                mahasiswaModel.nama = cursor.getString(cursor.getColumnIndexOrThrow(NAMA))
                mahasiswaModel.nim = cursor.getString(cursor.getColumnIndexOrThrow(NIM))

                arrayList.add(mahasiswaModel)
                cursor.moveToNext()
            } while (!cursor.isAfterLast)
        }
        cursor.close()
        return arrayList
    }

    fun beginTransaction() {
        database.beginTransaction()
    }

    fun setTransactionSuccess() {
        database.setTransactionSuccessful()
    }

    fun endTransaction() {
        database.endTransaction()
    }

    fun insertTransaction(mahasiswaModel: MahasiswaModel) {
        val sql = "INSERT INTO $TABLE_NAME ($NAMA, $NIM) VALUES (?, ?)"
        val stmt = database.compileStatement(sql)
        stmt.bindString(1, mahasiswaModel.nama)
        stmt.bindString(2, mahasiswaModel.nim)
        stmt.execute()
        stmt.clearBindings()
    }

}