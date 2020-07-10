package id.dwichan.githubusers

import android.annotation.SuppressLint
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import id.dwichan.githubusers.adapter.UserAdapter
import id.dwichan.githubusers.viewmodels.UserViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var adapter = UserAdapter()
    private lateinit var userViewModel: UserViewModel
    private var doubleBackToExitPressedOnce = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = getString(R.string.app_name)

        adapter = UserAdapter()
        adapter.notifyDataSetChanged()

        recView.layoutManager = LinearLayoutManager(this)
        recView.adapter = adapter
        recView.visibility = View.GONE

        userViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(UserViewModel::class.java)
        userViewModel.getUsers().observe(this, Observer { userItems ->
            if (userItems != null) {
                adapter.setData(userItems)
                showLoading(false)
                if (adapter.itemCount == 0) {
                    showErrorMessages(resources.getString(R.string.not_found_message))
                }
            }
        })
    }

    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            return
        }
        doubleBackToExitPressedOnce = true
        Toast.makeText(
            this,
            getString(R.string.double_back_message, getString(R.string.app_name)),
            Toast.LENGTH_SHORT
        ).show()
        Handler().postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_main, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu?.findItem(R.id.search)?.actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                if (query.isEmpty() || query.length < 2) return true
                showLoading(true)
                val output = userViewModel.setUsers(query, adapter, applicationContext)
                if (output != null) showErrorMessages(output)
                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return true
            }
        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.settings -> startActivity(Intent(this, SettingsActivity::class.java))
            R.id.about -> startActivity(Intent(this, AboutActivity::class.java))
            R.id.favorite -> startActivity(Intent(this, FavoriteActivity::class.java))
        }

        return super.onOptionsItemSelected(item)
    }

    fun showLoading(state: Boolean) {
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