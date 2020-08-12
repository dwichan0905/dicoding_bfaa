package id.dwichan.belajarretrofit2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import id.dwichan.belajarretrofit2.api.ApiClient
import id.dwichan.belajarretrofit2.api.ApiInterface
import id.dwichan.belajarretrofit2.model.Kontak
import id.dwichan.belajarretrofit2.requests.PostPutDelKontak
import kotlinx.android.synthetic.main.activity_rincian.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RincianActivity : AppCompatActivity() {

    private var id: String = ""
    private lateinit var mApiInterface: ApiInterface

    companion object {
        const val EXTRA_ID = "extra_id"
        const val EXTRA_NAMA = "extra_nama"
        const val EXTRA_NOMOR = "extra_nomor"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rincian)

        mApiInterface = ApiClient().getClient()!!.create(ApiInterface::class.java)

        val bundle = intent.extras!!
        id = bundle.getString(EXTRA_ID, "")
        val nama = bundle.getString(EXTRA_NAMA, "")
        val nomor = bundle.getString(EXTRA_NOMOR, "")
        edtId.setText(id)
        edtNama.setText(nama)
        edtNomor.setText(nomor)

        btUpdate2.setOnClickListener {
            val kontak = Kontak()
            kontak.id = id
            kontak.nama = nama
            kontak.nomor = nomor
            val i = Intent(this, InsertUpdateActivity::class.java)
            i.putExtra(InsertUpdateActivity.EXTRA_PARCEL_KONTAK, kontak)
            startActivityForResult(i, InsertUpdateActivity.RESULT_SAVE)
        }

        btDelete2.setOnClickListener {
            val delKontak: Call<PostPutDelKontak> = mApiInterface.deleteKontak(id)
            delKontak.enqueue(object: Callback<PostPutDelKontak> {
                override fun onFailure(call: Call<PostPutDelKontak>, t: Throwable) {
                    Toast.makeText(applicationContext, "Error", Toast.LENGTH_LONG).show();
                }

                override fun onResponse(
                    call: Call<PostPutDelKontak>,
                    response: Response<PostPutDelKontak>
                ) {
                    setResult(RESULT_OK)
                    finish()
                }

            })
        }

        btBackGo.setOnClickListener { super.onBackPressed() }
    }
}