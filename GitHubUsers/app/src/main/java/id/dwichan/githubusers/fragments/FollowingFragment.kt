package id.dwichan.githubusers.fragments

import android.annotation.SuppressLint
import android.app.Application
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import id.dwichan.githubusers.R
import id.dwichan.githubusers.UserDetailActivity
import id.dwichan.githubusers.userList.UserAdapter
import kotlinx.android.synthetic.main.content_user_details.*
import kotlinx.android.synthetic.main.fragment_follower.*
import kotlinx.android.synthetic.main.fragment_following.*
import kotlinx.android.synthetic.main.fragment_following.octocat_sad
import kotlinx.android.synthetic.main.fragment_following.progressBar
import kotlinx.android.synthetic.main.fragment_following.recView
import kotlinx.android.synthetic.main.fragment_following.tvErrorMessage
import kotlinx.android.synthetic.main.fragment_following.tvLoadingMessage

class FollowingFragment : Fragment() {

    private var adapter = UserAdapter()
    private lateinit var followingViewModel: FollowingViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_following, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showLoading(true)
        val login = activity?.intent?.getStringExtra(UserDetailActivity.EXTRA_LOGIN)

        adapter = UserAdapter()
        adapter.notifyDataSetChanged()
        recView.layoutManager = LinearLayoutManager(context)
        recView.adapter = adapter

        followingViewModel =
            ViewModelProvider.AndroidViewModelFactory.getInstance(activity?.application as Application)
                .create(FollowingViewModel::class.java)
        followingViewModel.getUsers().observe(viewLifecycleOwner, Observer { userItems ->
            if (userItems != null) {
                adapter.setData(userItems)
                showLoading(false)
                if (adapter.itemCount == 0) {
                    showErrorMessages(resources.getString(R.string.no_following_message))
                } else {
                    activity?.tabs?.setBadgeText(2, adapter.itemCount.toString())
                    recView?.visibility = View.VISIBLE
                }
            }
        })

        val output =
            login?.let { followingViewModel.setUsers(it, adapter, activity!!.applicationContext) }
        if (output != null) showErrorMessages(output)
    }

    @SuppressLint("SetTextI18n")
    private fun showErrorMessages(message: String) {
        octocat_sad?.visibility = View.VISIBLE
        tvErrorMessage?.visibility = View.VISIBLE
        progressBar?.visibility = View.GONE
        tvLoadingMessage?.visibility = View.GONE
        recView?.visibility = View.GONE
        tvErrorMessage?.text = """
            ${resources.getString(R.string.error_message)}
            $message
        """.trimIndent()
    }

    private fun showLoading(state: Boolean) {
        when (state) {
            true -> {
                progressBar?.visibility = View.VISIBLE
                tvLoadingMessage?.visibility = View.VISIBLE
                octocat_sad?.visibility = View.GONE
                tvErrorMessage?.visibility = View.GONE
                recView?.visibility = View.GONE
            }
            false -> {
                progressBar?.visibility = View.GONE
                tvLoadingMessage?.visibility = View.GONE
            }
        }
    }
}