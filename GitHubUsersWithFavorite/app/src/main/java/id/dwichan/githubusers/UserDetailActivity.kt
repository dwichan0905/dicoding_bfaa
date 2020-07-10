package id.dwichan.githubusers

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.widget.NestedScrollView
import com.bumptech.glide.Glide
import id.dwichan.githubusers.database.DatabaseContract
import id.dwichan.githubusers.database.FavoriteHelper
import id.dwichan.githubusers.helper.MappingHelper
import id.dwichan.githubusers.pager.SectionsPagerAdapter
import id.dwichan.githubusers.widgets.favorite.FavoriteWidget
import kotlinx.android.synthetic.main.activity_user_details.*
import kotlinx.android.synthetic.main.content_user_details.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class UserDetailActivity : AppCompatActivity() {

    private var favorited = false
    private lateinit var favoriteHelper: FavoriteHelper

    companion object {
        const val EXTRA_AVATAR = "extra_avatar"
        const val EXTRA_LOGIN = "extra_login"
        const val EXTRA_NAME = "extra_name"
        const val EXTRA_REPOS = "extra_repos"
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> super.onBackPressed()
        }
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_details)

        // 2 baris hotfix untuk nestedscrollview berisi viewpager
        val scrollView = findViewById<NestedScrollView>(R.id.nestedView)
        scrollView.isFillViewport = true

        val sectionsPagerAdapter =
            SectionsPagerAdapter(
                this,
                supportFragmentManager
            )
        viewPager.adapter = sectionsPagerAdapter
        tabs.setupWithViewPager(viewPager)

        val bundle = intent.extras
        val avatar = bundle?.get(EXTRA_AVATAR)
        val login = bundle?.get(EXTRA_LOGIN)
        val name = bundle?.get(EXTRA_NAME)
        val repos = bundle?.get(EXTRA_REPOS)

        val title =
            if (name.toString() == resources.getString(R.string.no_name)) login.toString() else name.toString()

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = title

        Glide.with(this)
            .load(avatar.toString())
            .placeholder(R.drawable.ic_placeholder)
            .error(R.drawable.ic_placeholder)
            .into(expandedImg)

        favoriteHelper = FavoriteHelper.getInstance(this)
        favoriteHelper.openDatabase()

        GlobalScope.launch(Dispatchers.Main) {
            val deferredFavorite = async(Dispatchers.IO) {
                val cursor = favoriteHelper.queryByUsername(login.toString())
                MappingHelper.mapCursorToArrayList(cursor)
            }
            val favorite = deferredFavorite.await()
            if (favorite.size == 0) {
                favorited = false
                fabAddToFavorite.setImageResource(R.drawable.ic_baseline_favorite_border_24)
            } else {
                favorited = true
                fabAddToFavorite.setImageResource(R.drawable.ic_baseline_favorite_24)
            }
        }

        fabAddToFavorite.setOnClickListener {
            if (favorited) {
                favorited = false
                favoriteHelper.delete(login.toString())
                fabAddToFavorite.setImageResource(R.drawable.ic_baseline_favorite_border_24)
                Toast.makeText(
                    this,
                    title + " " + getString(R.string.favorite_removed),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                favorited = true
                val values = ContentValues()
                values.put(DatabaseContract.FavoriteColumns.LOGIN_USERNAME, login.toString())
                values.put(DatabaseContract.FavoriteColumns.FULLNAME, name.toString())
                values.put(DatabaseContract.FavoriteColumns.AVATAR_URL, avatar.toString())
                values.put(DatabaseContract.FavoriteColumns.PUBLIC_REPOS, repos.toString().toInt())
                favoriteHelper.insert(values)

                fabAddToFavorite.setImageResource(R.drawable.ic_baseline_favorite_24)
                Toast.makeText(
                    this,
                    title + " " + getString(R.string.favorite_added),
                    Toast.LENGTH_SHORT
                ).show()
            }

            // Buat auto updatenya ke widget
            val widgetIntent = Intent(this, FavoriteWidget::class.java)
            widgetIntent.action = FavoriteWidget.UPDATE_ACTION
            val ids = AppWidgetManager.getInstance(this).getAppWidgetIds(
                ComponentName(
                    this,
                    FavoriteWidget::class.java
                )
            )
            widgetIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids)
            sendBroadcast(widgetIntent)
        }
    }

}