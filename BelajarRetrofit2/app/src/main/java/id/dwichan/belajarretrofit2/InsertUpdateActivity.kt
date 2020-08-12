package id.dwichan.belajarretrofit2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import id.dwichan.belajarretrofit2.api.ApiClient
import id.dwichan.belajarretrofit2.api.ApiInterface
import id.dwichan.belajarretrofit2.model.Kontak
import id.dwichan.belajarretrofit2.requests.PostPutDelKontak
import kotlinx.android.synthetic.main.activity_insert_update.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.create

class InsertUpdateActivity : AppCompatActivity() {

    private var id: String? = null

    companion object {
        const val EXTRA_PARCEL_KONTAK = "extra_parcel_kontak"
        const val RESULT_SAVE = 10
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insert_update)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val bundle = intent.extras
        val kontak = bundle?.getParcelable<Kontak>(EXTRA_PARCEL_KONTAK)

        val mApiInterface = ApiClient().getClient()!!.create(ApiInterface::class.java)

        if (kontak != null) {
            edtContactName.setText(kontak.nama)
            edtContactNumber.setText(kontak.nomor)
            id = kontak.id
            supportActionBar?.title = resources.getString(R.string.edit_contact)
            btnAddUpdate.text = resources.getString(R.string.edit_contact)
        } else {
            supportActionBar?.title = resources.getString(R.string.add_contact)
            btnAddUpdate.text = resources.getString(R.string.add_contact)
        }

        btnAddUpdate.setOnClickListener {
            if (id.isNullOrEmpty()) {
                val insertNewKontak: Call<PostPutDelKontak> = mApiInterface.postKontak(
                    edtContactName.text.toString(),
                    edtContactNumber.text.toString()
                )
                insertNewKontak.enqueue(object: Callback<PostPutDelKontak> {
                    override fun onFailure(call: Call<PostPutDelKontak>, t: Throwable) {
                        Toast.makeText(
                            this@InsertUpdateActivity,
                            "Error: " + t.message,
                            Toast.LENGTH_LONG
                        ).show()
                    }

                    override fun onResponse(
                        call: Call<PostPutDelKontak>,
                        response: Response<PostPutDelKontak>
                    ) {
                        setResult(RESULT_SAVE)
                        finish()
                    }
                })
            } else {
                val updateContact: Call<PostPutDelKontak> = mApiInterface.putKontak(
                    id!!,
                    edtContactName.text.toString(),
                    edtContactNumber.text.toString()
                )
                updateContact.enqueue(object: Callback<PostPutDelKontak> {
                    override fun onFailure(call: Call<PostPutDelKontak>, t: Throwable) {
                        Toast.makeText(
                            this@InsertUpdateActivity,
                            "Error: " + t.message,
                            Toast.LENGTH_LONG
                        ).show()
                    }

                    override fun onResponse(
                        call: Call<PostPutDelKontak>,
                        response: Response<PostPutDelKontak>
                    ) {
                        setResult(RESULT_SAVE)
                        finish()
                    }
                })
            }
        }
    }
}