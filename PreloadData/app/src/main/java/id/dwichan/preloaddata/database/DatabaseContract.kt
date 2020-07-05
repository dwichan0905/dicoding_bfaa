package id.dwichan.preloaddata.database

import android.provider.BaseColumns

internal object DatabaseContract {
    var TABLE_NAME = "table_mahasiswa"
    internal class MahasiswaColumns: BaseColumns {
        companion object {
            const val _ID = "_id"
            const val NAMA = "nama"
            const val NIM = "nim"
        }
    }
}