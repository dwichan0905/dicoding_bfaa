package id.dwichan.githubusers

import android.annotation.SuppressLint
import android.content.Intent
import android.database.ContentObserver
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.os.PersistableBundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import id.dwichan.githubusers.adapter.UserAdapter
import id.dwichan.githubusers.database.DatabaseContract.FavoriteColumns.Companion.CONTENT_URI
import id.dwichan.githubusers.database.FavoriteHelper
import id.dwichan.githubusers.helper.MappingHelper
import id.dwichan.githubusers.items.UserItems
import kotlinx.android.synthetic.main.activity_favorite.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class FavoriteActivity : AppCompatActivity() {

    private var adapter = UserAdapter()
    private lateinit var favoriteHelper: FavoriteHelper

    companion object {
        private const val EXTRA_STATE = "extra_state"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = getString(R.string.favorite)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        adapter = UserAdapter()
        adapter.notifyDataSetChanged()

        recView.layoutManager = LinearLayoutManager(this)
        recView.adapter = adapter

        /*favoriteHelper = FavoriteHelper.getInstance(this)
        favoriteHelper.openDatabase()*/

        val handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)
        val observer = object : ContentObserver(handler) {
            override fun onChange(self: Boolean) {
                loadFavoriteListAsync()
            }
        }
        contentResolver.registerContentObserver(CONTENT_URI, true, observer)

        if (savedInstanceState == null) {
            loadFavoriteListAsync()
        } else {
            val list = savedInstanceState.getParcelableArrayList<UserItems>(EXTRA_STATE)
            if (list != null) adapter.setData(list)
        }
    }

    override fun onResume() {
        super.onResume()
        loadFavoriteListAsync()
    }

    private fun loadFavoriteListAsync() {
        GlobalScope.launch(Dispatchers.Main) {
            showLoading(true)
            val deferredFavorites = async(Dispatchers.IO) {
                /*val cursor = favoriteHelper.queryShowAll()*/
                val cursor = contentResolver?.query(
                    CONTENT_URI,
                    null,
                    null,
                    null,
                    null)
                MappingHelper.mapCursorToArrayList(cursor)
            }
            showLoading(false)
            val favorites = deferredFavorites.await()
            if (favorites.size > 0) {
                adapter.setData(favorites)
            } else {
                adapter.setData(ArrayList())
                showErrorMessages(getString(R.string.no_favorite_message))
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        outState.putParcelableArrayList(EXTRA_STATE, adapter.getData())
    }

    override fun onDestroy() {
        super.onDestroy()
        // mengatasi error kotlin.UninitializedPropertyAccessException: lateinit property favoriteHelper has not been initialized
        if (this::favoriteHelper.isInitialized) favoriteHelper.closeDatabase()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> super.onBackPressed()
            R.id.settings -> startActivity(Intent(this, SettingsActivity::class.java))
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_favorite, menu)
        return true
    }

    private fun showLoading(state: Boolean) {
        when (state) {
            true -> {
                progressBar.visibility = View.VISIBLE
                progressBarMessage.visibility = View.VISIBLE
                recView.visibility = View.GONE
                octocat_sad.visibility = View.GONE
                tvErrorMessage.visibility = View.GONE
            }
            false -> {
                progressBar.visibility = View.GONE
                progressBarMessage.visibility = View.GONE
                recView.visibility = View.VISIBLE
            }
        }
    }

    @SuppressLint("SetTextI18n")
    fun showErrorMessages(text: String) {
        octocat_sad.visibility = View.VISIBLE
        tvErrorMessage.visibility = View.VISIBLE
        tvErrorMessage.text = getString(R.string.error_message) + text
    }
}