package id.dwichan.githubusers

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.widget.NestedScrollView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_user_details.*
import kotlinx.android.synthetic.main.content_user_details.*

class UserDetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_AVATAR = "extra_avatar"
        const val EXTRA_LOGIN = "extra_login"
        const val EXTRA_NAME = "extra_name"
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

        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        viewPager.adapter = sectionsPagerAdapter
        tabs.setupWithViewPager(viewPager)

        val bundle = intent.extras
        val avatar = bundle?.get(EXTRA_AVATAR)
        val login = bundle?.get(EXTRA_LOGIN)
        val name = bundle?.get(EXTRA_NAME)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = if (name.toString() == resources.getString(R.string.no_name)
        ) login.toString() else name.toString()

        Glide.with(this)
            .load(avatar.toString())
            .into(expandedImg)
    }


}