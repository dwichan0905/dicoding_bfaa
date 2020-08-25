package id.dwichan.belajarretrofit2

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import id.dwichan.belajarretrofit2.api.ApiClient
import id.dwichan.belajarretrofit2.api.ApiInterface
import id.dwichan.belajarretrofit2.model.Contact
import id.dwichan.belajarretrofit2.requests.PostPutDelContact
import kotlinx.android.synthetic.main.activity_details.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailsActivity : AppCompatActivity() {

    private var id: String = ""
    private var name: String = ""
    private lateinit var mApiInterface: ApiInterface

    companion object {
        const val EXTRA_ID = "extra_id"
        const val EXTRA_NAMA = "extra_nama"
        const val EXTRA_NOMOR = "extra_nomor"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""

        mApiInterface = ApiClient().getClient()!!.create(ApiInterface::class.java)

        val bundle = intent.extras!!
        id = bundle.getString(EXTRA_ID, "")
        val nama = bundle.getString(EXTRA_NAMA, "")
        val nomor = bundle.getString(EXTRA_NOMOR, "")

        name = nama

        tvContactName.text = nama
        tvPhoneNumber.text = nomor

        btnUpdate.setOnClickListener {
            val kontak = Contact()
            kontak.id = id
            kontak.nama = nama
            kontak.nomor = nomor
            val i = Intent(this, InsertUpdateActivity::class.java)
            i.putExtra(InsertUpdateActivity.EXTRA_PARCEL_KONTAK, kontak)
            startActivityForResult(i, InsertUpdateActivity.REQUEST_UPDATE)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_details, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> super.onBackPressed()
            R.id.menuDelete -> {
                val alert = AlertDialog.Builder(this)
                with(alert) {
                    setTitle(getString(R.string.sure))
                    setMessage(resources.getString(R.string.delete_confirm) + "$name?")
                    setPositiveButton(getString(R.string.yes)) { _, _ ->
                        val delContact: Call<PostPutDelContact> = mApiInterface.deleteKontak(id)
                        delContact.enqueue(object : Callback<PostPutDelContact> {
                            override fun onFailure(call: Call<PostPutDelContact>, t: Throwable) {
                                Toast.makeText(applicationContext, "Error", Toast.LENGTH_LONG)
                                    .show()
                            }

                            override fun onResponse(
                                call: Call<PostPutDelContact>,
                                response: Response<PostPutDelContact>
                            ) {
                                setResult(RESULT_OK)
                                finish()
                            }
                        })
                    }
                    setNegativeButton(getString(R.string.no), null)
                    setCancelable(false)
                    create().show()
                }
            }
        }
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == RESULT_OK) {
            val contact =
                data?.getParcelableExtra<Contact>(InsertUpdateActivity.EXTRA_PARCEL_KONTAK)!!
            tvContactName.text = contact.nama
            tvPhoneNumber.text = contact.nomor
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}