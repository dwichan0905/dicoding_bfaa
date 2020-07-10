package id.dwichan.githubusers.pager

import android.content.Context
import androidx.annotation.Nullable
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import id.dwichan.githubusers.R
import id.dwichan.githubusers.fragments.DetailsFragment
import id.dwichan.githubusers.fragments.FollowerFragment
import id.dwichan.githubusers.fragments.FollowingFragment

class SectionsPagerAdapter(private val mContext: Context, fm: FragmentManager) :
    FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    @StringRes
    private val tabTitles =
        intArrayOf(
            R.string.tab_details,
            R.string.tab_follower,
            R.string.tab_following
        )

    override fun getItem(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = DetailsFragment()
            1 -> fragment = FollowerFragment()
            2 -> fragment = FollowingFragment()
        }
        return fragment as Fragment
    }

    @Nullable
    override fun getPageTitle(position: Int): CharSequence? {
        return mContext.resources.getString(tabTitles[position])
    }

    override fun getCount(): Int {
        return 3
    }
}