package id.dwichan.readwrite

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var path: File

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnNew.setOnClickListener(this)
        btnOpen.setOnClickListener(this)
        btnSave.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnNew -> { newFile() }
            R.id.btnOpen -> { showList() }
            R.id.btnSave -> { saveFile() }
        }
    }

    private fun saveFile() {
        when {
            editTitle.text.toString().isEmpty() -> Toast.makeText(this, "Title harus diisi!", Toast.LENGTH_SHORT).show()
            editFile.text.toString().isEmpty() -> Toast.makeText(this, "Konten harus diisi!", Toast.LENGTH_SHORT).show()
            else -> {
                val title = editTitle.text.toString()
                val text = editFile.text.toString()
                val fileModel = FileModel()
                fileModel.filename = title
                fileModel.data = text
                FileHelper.writeToFile(fileModel, this)
                Toast.makeText(this, "Saved: ${fileModel.filename} file", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showList() {
        val arrayList = ArrayList<String>()
        val path: File = filesDir
        Collections.addAll(arrayList, *path.list() as Array<String>)
        val items = arrayList.toTypedArray<CharSequence>()

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Choose file that you want to open.")
        builder.setItems(items) { dialog, item -> loadData(items[item].toString()) }
        val alert = builder.create()
        alert.show()
    }

    private fun loadData(title: String) {
        val fileModel = FileHelper.readFromFile(this, title)
        editTitle.setText(fileModel.filename)
        editFile.setText(fileModel.data)
        Toast.makeText(this, "Loaded: ${fileModel.filename} data", Toast.LENGTH_SHORT).show()
    }

    private fun newFile() {
        editTitle.setText("")
        editFile.setText("")
        Toast.makeText(this, "Clearing file!", Toast.LENGTH_SHORT).show()
    }
}