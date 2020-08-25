package id.dwichan.belajarretrofit2

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import id.dwichan.belajarretrofit2.api.ApiClient
import id.dwichan.belajarretrofit2.api.ApiInterface
import id.dwichan.belajarretrofit2.model.Contact
import id.dwichan.belajarretrofit2.requests.PostPutDelContact
import kotlinx.android.synthetic.main.activity_insert_update.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class InsertUpdateActivity : AppCompatActivity() {

    private var id: String? = null

    companion object {
        const val EXTRA_PARCEL_KONTAK = "extra_parcel_kontak"
        const val REQUEST_SAVE = 10
        const val REQUEST_UPDATE = 11
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insert_update)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""

        val bundle = intent.extras
        val kontak = bundle?.getParcelable<Contact>(EXTRA_PARCEL_KONTAK)

        val mApiInterface = ApiClient().getClient()!!.create(ApiInterface::class.java)

        if (kontak != null) {
            edtContactName.setText(kontak.nama)
            edtContactNumber.setText(kontak.nomor)
            id = kontak.id
            tvTitle.text = resources.getString(R.string.edit_contact)
            btnAddUpdate.text = resources.getString(R.string.edit_contact)
        } else {
            tvTitle.text = resources.getString(R.string.add_contact)
            btnAddUpdate.text = resources.getString(R.string.add_contact)
        }

        btnAddUpdate.setOnClickListener {
            if (id.isNullOrEmpty()) {
                val insertNewContact: Call<PostPutDelContact> = mApiInterface.postKontak(
                    edtContactName.text.toString(),
                    edtContactNumber.text.toString()
                )
                insertNewContact.enqueue(object : Callback<PostPutDelContact> {
                    override fun onFailure(call: Call<PostPutDelContact>, t: Throwable) {
                        Toast.makeText(
                            this@InsertUpdateActivity,
                            "Error: " + t.message,
                            Toast.LENGTH_LONG
                        ).show()
                    }

                    override fun onResponse(
                        call: Call<PostPutDelContact>,
                        response: Response<PostPutDelContact>
                    ) {
                        setResult(RESULT_OK)
                        finish()
                    }
                })
            } else {
                val updateContact: Call<PostPutDelContact> = mApiInterface.putKontak(
                    id!!,
                    edtContactName.text.toString(),
                    edtContactNumber.text.toString()
                )
                updateContact.enqueue(object : Callback<PostPutDelContact> {
                    override fun onFailure(call: Call<PostPutDelContact>, t: Throwable) {
                        Toast.makeText(
                            this@InsertUpdateActivity,
                            "Error: " + t.message,
                            Toast.LENGTH_LONG
                        ).show()
                    }

                    override fun onResponse(
                        call: Call<PostPutDelContact>,
                        response: Response<PostPutDelContact>
                    ) {
                        val i = Intent()
                        val contact = Contact()
                        contact.id = id
                        contact.nama = edtContactName.text.toString()
                        contact.nomor = edtContactNumber.text.toString()
                        i.putExtra(EXTRA_PARCEL_KONTAK, contact)
                        setResult(RESULT_OK, i)
                        finish()
                    }
                })
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) super.onBackPressed()
        return true
    }
}