package id.dwichan.githubusers.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import id.dwichan.githubusers.R
import id.dwichan.githubusers.UserDetailActivity
import id.dwichan.githubusers.items.UserItems
import kotlinx.android.synthetic.main.user_items.view.*

class UserAdapter : Adapter<UserAdapter.UserViewHolder>() {

    private var mData = ArrayList<UserItems>()

    fun setData(items: ArrayList<UserItems>) {
        mData.clear()
        mData.addAll(items)
        notifyDataSetChanged()
    }

    fun getData(): ArrayList<UserItems> {
        return mData
    }

    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @SuppressLint("SetTextI18n")
        fun bind(userItems: UserItems) {
            with(itemView) {
                tvFullname.text = userItems.name
                tvUsername.text = userItems.login
                tvFollowerCount.text =
                    "${if (userItems.public_repos == 0) {
                        resources.getString(R.string.none)
                    } else {
                        userItems.public_repos.toString()
                    }} ${resources.getString(R.string.public_repos)}"

                Glide.with(context)
                    .load(userItems.avatar)
                    .placeholder(R.drawable.ic_placeholder)
                    .error(R.drawable.ic_placeholder)
                    .apply(RequestOptions().override(60, 60))
                    .into(civUserIcon)

                setOnClickListener {
                    val intent = Intent(context, UserDetailActivity::class.java)
                    intent.putExtra(UserDetailActivity.EXTRA_AVATAR, userItems.avatar)
                    intent.putExtra(UserDetailActivity.EXTRA_LOGIN, userItems.login)
                    intent.putExtra(UserDetailActivity.EXTRA_NAME, userItems.name)
                    intent.putExtra(UserDetailActivity.EXTRA_REPOS, userItems.public_repos)

                    context.startActivity(intent)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserAdapter.UserViewHolder {
        val mView = LayoutInflater.from(parent.context)
            .inflate(R.layout.user_items, parent, false)
        return UserViewHolder(mView)
    }

    override fun getItemCount(): Int = mData.size

    override fun onBindViewHolder(holder: UserAdapter.UserViewHolder, position: Int) {
        holder.bind(mData[position])
    }
}