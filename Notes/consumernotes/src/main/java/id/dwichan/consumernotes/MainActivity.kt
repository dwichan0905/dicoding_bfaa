package id.dwichan.consumernotes

import android.content.Intent
import android.database.ContentObserver
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import id.dwichan.consumernotes.db.DatabaseContract.NoteColumns.Companion.CONTENT_URI
import id.dwichan.consumernotes.entity.Note
import id.dwichan.consumernotes.helper.MappingHelper
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var adapter: NoteAdapter
    //private lateinit var noteHelper: NoteHelper

    companion object {
        private const val EXTRA_STATE = "extra_state"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.title = "Consumer Notes"

        rvNotes.layoutManager = LinearLayoutManager(this)
        rvNotes.setHasFixedSize(true)
        adapter = NoteAdapter(this)
        rvNotes.adapter = adapter

        fabAdd.setOnClickListener {
            val intent = Intent(this, NoteAddUpdateActivity::class.java)
            startActivityForResult(intent, NoteAddUpdateActivity.REQUEST_ADD)
        }

        /* Diganti pake Content Provider
        noteHelper = NoteHelper.getInstance(applicationContext)
        noteHelper.open()
        */

        val handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)
        val myObserver = object: ContentObserver(handler) {
            override fun onChange(selfChange: Boolean) {
                loadNotesAsync()
            }
        }
        contentResolver.registerContentObserver(CONTENT_URI, true, myObserver)

        // ambil data async
        if (savedInstanceState == null) loadNotesAsync() else {
            val list = savedInstanceState.getParcelableArrayList<Note>(EXTRA_STATE)
            if (list != null) adapter.listNotes = list
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(EXTRA_STATE, adapter.listNotes)
    }

    private fun loadNotesAsync() {
        GlobalScope.launch (Dispatchers.Main) {
            progressBar.visibility = View.VISIBLE
            val deferredNotes = async (Dispatchers.IO) {
                //val cursor = noteHelper.queryAll() <-- diganti pake ContentResolver
                val cursor = contentResolver.query(CONTENT_URI,
                    null, null, null, null)
                MappingHelper.mapCursorToArrayList(cursor)
            }

            progressBar.visibility = View.INVISIBLE
            val notes = deferredNotes.await()

            if (notes.size > 0) adapter.listNotes = notes else {
                adapter.listNotes = ArrayList()
                showSnachbarMessage("Tidak ada data.")
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        //noteHelper?.close()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

//        if (data != null) {
//            when (requestCode) {
//                NoteAddUpdateActivity.REQUEST_ADD -> if (resultCode == NoteAddUpdateActivity.RESULT_ADD) {
//                    val note = data.getParcelableExtra<Note>(NoteAddUpdateActivity.EXTRA_NOTE)!!
//                    adapter.addItem(note)
//                    rvNotes.smoothScrollToPosition(adapter.itemCount - 1)
//                    showSnachbarMessage("1 item berhasil ditambahkan")
//                }
//                NoteAddUpdateActivity.REQUEST_UPDATE -> {
//                    when (resultCode) {
//                        NoteAddUpdateActivity.RESULT_UPDATE -> {
//                            val note = data.getParcelableExtra<Note>(NoteAddUpdateActivity.EXTRA_NOTE)!!
//                            val position = data.getIntExtra(NoteAddUpdateActivity.EXTRA_POSITION, 0)
//
//                            adapter.updateItem(position, note)
//                            rvNotes.smoothScrollToPosition(position)
//
//                            showSnachbarMessage("1 item berhasil diubah")
//                        }
//                        NoteAddUpdateActivity.RESULT_DELETE -> {
//                            val position = data.getIntExtra(NoteAddUpdateActivity.EXTRA_POSITION, 0)
//                            adapter.removeItem(position)
//                            showSnachbarMessage("1 item berhasil dihapus")
//                        }
//                    }
//                }
//            }
//        }
    }

    private fun showSnachbarMessage(s: String) {
        Snackbar.make(rvNotes, s, Snackbar.LENGTH_SHORT).show()
    }
}