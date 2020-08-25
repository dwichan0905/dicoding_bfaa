package id.dwichan.belajarretrofit2

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import id.dwichan.belajarretrofit2.adapter.ContactAdapter
import id.dwichan.belajarretrofit2.api.ApiClient
import id.dwichan.belajarretrofit2.api.ApiInterface
import id.dwichan.belajarretrofit2.model.Contact
import id.dwichan.belajarretrofit2.requests.GetContact
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private val mKontakAdapter = ContactAdapter()
    private lateinit var apiInterface: ApiInterface

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        progressBar.visibility = View.VISIBLE
        tvError.visibility = View.GONE

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = ""

        fabAddContact.setOnClickListener(this)

        rvContact.layoutManager = LinearLayoutManager(this)
        rvContact.adapter = mKontakAdapter

        val apiClient = ApiClient()
        apiInterface = apiClient.getClient()!!.create(ApiInterface::class.java)

        refresh()

        pullToRefresh.setOnRefreshListener {
            val contactCall: Call<GetContact> = apiInterface.getKontak()
            contactCall.enqueue(object : Callback<GetContact> {
                override fun onFailure(call: Call<GetContact>, t: Throwable) {
                    Log.e("Retrofit Error", t.message!!.toString())
                    tvError.visibility = View.VISIBLE
                    tvError.text = t.localizedMessage!!
                    pullToRefresh.isRefreshing = false
                }

                override fun onResponse(call: Call<GetContact>, response: Response<GetContact>) {
                    val contactList: ArrayList<Contact> = response.body()!!.listContact
                    Log.d("Retrofit Get", "Jumlah Kontak: " + contactList.size.toString())
                    mKontakAdapter.setData(contactList)
                    tvError.visibility = View.GONE
                    pullToRefresh.isRefreshing = false
                }
            })
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menuAbout) {
            val i = Intent(this, AboutActivity::class.java)
            startActivity(i)
        }
        return true
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.fabAddContact -> {
                val i = Intent(this, InsertUpdateActivity::class.java)
                startActivityForResult(i, InsertUpdateActivity.REQUEST_SAVE)
            }
        }
    }

    private fun refresh() {
        val contactCall: Call<GetContact> = apiInterface.getKontak()
        contactCall.enqueue(object : Callback<GetContact> {
            override fun onFailure(call: Call<GetContact>, t: Throwable) {
                Log.e("Retrofit Error", t.message!!.toString())
                tvError.visibility = View.VISIBLE
                progressBar.visibility = View.GONE
                tvError.text = t.localizedMessage!!
            }

            override fun onResponse(call: Call<GetContact>, response: Response<GetContact>) {
                progressBar.visibility = View.GONE
                val contactList: ArrayList<Contact> = response.body()!!.listContact
                Log.d("Retrofit Get", "Jumlah Kontak: " + contactList.size.toString())
                if (contactList.size > 0) {
                    mKontakAdapter.setData(contactList)
                    tvError.visibility = View.GONE
                } else {
                    mKontakAdapter.setData(ArrayList())
                    tvError.visibility = View.VISIBLE
                    tvError.text = getString(R.string.no_data)
                }
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