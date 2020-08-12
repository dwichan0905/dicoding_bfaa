package id.dwichan.belajarretrofit2

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import id.dwichan.belajarretrofit2.adapter.KontakAdapter
import id.dwichan.belajarretrofit2.api.ApiClient
import id.dwichan.belajarretrofit2.api.ApiInterface
import id.dwichan.belajarretrofit2.model.Kontak
import id.dwichan.belajarretrofit2.requests.GetKontak
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private val mKontakAdapter = KontakAdapter()
    private lateinit var apiInterface: ApiInterface

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = resources.getString(R.string.app_name)

        fabAddContact.setOnClickListener(this)

        rvContact.layoutManager = LinearLayoutManager(this)
        rvContact.adapter = mKontakAdapter

        val apiClient = ApiClient()
        apiInterface = apiClient.getClient()!!.create(ApiInterface::class.java)

        refresh()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.fabAddContact -> {
                val i = Intent(this, InsertUpdateActivity::class.java)
                startActivityForResult(i, InsertUpdateActivity.RESULT_SAVE)
            }
        }
    }

    private fun refresh() {
        val kontakCall: Call<GetKontak> = apiInterface.getKontak()
        kontakCall.enqueue(object: Callback<GetKontak> {
            override fun onFailure(call: Call<GetKontak>, t: Throwable) {
                Log.e("Retrofit Error", t.message!!.toString())
            }

            override fun onResponse(call: Call<GetKontak>, response: Response<GetKontak>) {
                val kontakList: ArrayList<Kontak> = response.body()!!.listKontak
                Log.d("Retrofit Get", "Jumlah Kontak: " + kontakList.size.toString())
                mKontakAdapter.setData(kontakList)
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            refresh()
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}