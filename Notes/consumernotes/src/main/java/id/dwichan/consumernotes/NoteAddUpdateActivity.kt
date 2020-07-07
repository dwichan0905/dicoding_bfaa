package id.dwichan.consumernotes

import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import id.dwichan.consumernotes.db.DatabaseContract
import id.dwichan.consumernotes.db.DatabaseContract.NoteColumns.Companion.CONTENT_URI
import id.dwichan.consumernotes.db.DatabaseContract.NoteColumns.Companion.DATE
import id.dwichan.consumernotes.entity.Note
import id.dwichan.consumernotes.helper.MappingHelper
import kotlinx.android.synthetic.main.activity_note_add_update.*
import java.text.SimpleDateFormat
import java.util.*

class NoteAddUpdateActivity : AppCompatActivity(), View.OnClickListener {

    private var isEdit = false
    private var note: Note? = null
    private var position: Int = 0
    //private lateinit var noteHelper: NoteHelper <-- diganti
    private lateinit var urlWithId: Uri

    companion object {
        const val EXTRA_NOTE = "extra_note"
        const val EXTRA_POSITION = "extra_position"
        const val REQUEST_ADD = 100
        const val RESULT_ADD = 101
        const val REQUEST_UPDATE = 200
        const val RESULT_UPDATE = 201
        const val RESULT_DELETE = 301
        const val ALERT_DIALOG_CLOSE = 10
        const val ALERT_DIALOG_DELETE = 20
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_add_update)

        // diganti untuk menggunakan content resolver
//        noteHelper = NoteHelper.getInstance(applicationContext)
//        noteHelper.open()

        note = intent.getParcelableExtra(EXTRA_NOTE)
        if (note != null) {
            position = intent.getIntExtra(EXTRA_POSITION, 0)
            isEdit = true
        } else note = Note()

        val actionBarTitle: String
        val btnTitle: String

        if (isEdit) {
            // Uri yang didapatkan disini akan digunakan untuk ambil data dari provdier
            // content://id.dwichan.notes/note/id
            urlWithId = Uri.parse(CONTENT_URI.toString() + "/" + note?.id)
            val cursor = contentResolver.query(urlWithId, null, null, null, null)
            if (cursor != null) {
                note = MappingHelper.mapCursorToObject(cursor)
                cursor.close()
            }
            actionBarTitle = "Ubah"
            btnTitle = "Update"
            note?.let {
                edtTitle.setText(it.title)
                edtDescription.setText(it.description)
            }
        } else {
            actionBarTitle = "Tambah"
            btnTitle = "Simpan"
        }

        supportActionBar?.title = actionBarTitle
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        btnSubmit.text = btnTitle
        btnSubmit.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        if (v.id == R.id.btnSubmit) {
            val title = edtTitle.text.toString().trim()
            val description = edtDescription.text.toString().trim()

            if (title.isEmpty()) {
                edtTitle.error = "Field can't be blank!"
                return
            }

            note?.title = title
            note?.description = description

            val intent = Intent()
            intent.putExtra(EXTRA_NOTE, note)
            intent.putExtra(EXTRA_POSITION, position)

            val values = ContentValues()
            values.put(DatabaseContract.NoteColumns.TITLE, title)
            values.put(DatabaseContract.NoteColumns.DESCRIPTION, description)

            if (isEdit) {
                /* Diganti menggunakan content resolver */
                // val result = noteHelper.update(note?.id.toString(), values).toLong()
                contentResolver.update(urlWithId, values, null, null)
                Toast.makeText(this@NoteAddUpdateActivity, "1 item data ditambah", Toast.LENGTH_SHORT).show()
                setResult(RESULT_UPDATE, intent)
                finish()
            } else {
                //note?.date = getCurrentDate()
                values.put(DATE, getCurrentDate())
                /* Diganti menggunakan Content Resolver */
                // val result = noteHelper.insert(values)
                contentResolver.insert(CONTENT_URI, values)
                Toast.makeText(this@NoteAddUpdateActivity, "1 item data ditambah", Toast.LENGTH_SHORT).show()
                setResult(RESULT_ADD, intent)
                finish()
            }
        }
    }

    private fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault())
        val date = Date()

        return dateFormat.format(date)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if (isEdit) menuInflater.inflate(R.menu.menu_form, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_delete -> showAlertDialog(ALERT_DIALOG_DELETE)
            android.R.id.home -> showAlertDialog(ALERT_DIALOG_CLOSE)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        showAlertDialog(ALERT_DIALOG_CLOSE)
    }

    private fun showAlertDialog(type: Int) {
        val isDialogClose = type == ALERT_DIALOG_CLOSE
        val dialogTitle: String
        val dialogMessage: String

        if (isDialogClose) {
            dialogTitle = "Batal"
            dialogMessage = "Apakah Anda ingin membatalkan perubahan pada form?"
        } else {
            dialogTitle = "Hapus"
            dialogMessage = "Apakah Anda yakin ingin menghapus item ini?"
        }

        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle(dialogTitle)
        alertDialogBuilder.setMessage(dialogMessage)
            .setCancelable(false)
            .setPositiveButton("Ya") { dialog, id ->
                if (isDialogClose) finish() else {
                    /* Diganti menggunakan content resolver
                    val result = noteHelper.deleteById(note?.id.toString()).toLong()
                    if (result > 0) {
                        val intent = Intent()
                        intent.putExtra(EXTRA_POSITION, position)
                        setResult(RESULT_DELETE, intent)
                        finish()
                    } else Toast.makeText(this@NoteAddUpdateActivity, "Gagal menghapus data!", Toast.LENGTH_SHORT).show()*/
                    contentResolver.delete(urlWithId, null, null)
                    Toast.makeText(this@NoteAddUpdateActivity, "1 item berhasil dihapus!", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
            .setNegativeButton("Tidak") { dialog, id -> dialog.cancel() }

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }
}