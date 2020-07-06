package id.dwichan.notes.db

import android.net.Uri
import android.provider.BaseColumns

object DatabaseContract  {

    const val AUTHORITY = "id.dwichan.notes"
    const val SCHEME = "content"

    internal class NoteColumns: BaseColumns {
        companion object {
            const val TABLE_NAME = "note"
            const val _ID = "_id"
            const val TITLE = "title"
            const val DESCRIPTION = "description"
            const val DATE = "date"

            // untuk membuat URI content://id.dwichan.notes/note
            val CONTENT_URI: Uri = Uri.Builder().scheme(SCHEME).authority(AUTHORITY).appendPath(
                TABLE_NAME).build()
        }
    }
}