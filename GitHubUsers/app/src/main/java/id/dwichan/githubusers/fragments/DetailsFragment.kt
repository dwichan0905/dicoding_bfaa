package id.dwichan.githubusers.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import id.dwichan.githubusers.R
import id.dwichan.githubusers.UserDetailActivity
import kotlinx.android.synthetic.main.fragment_details.*
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class DetailsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val login = activity?.intent?.getStringExtra(UserDetailActivity.EXTRA_LOGIN)

        showLoadingIndicator(true)
        val apiKey = resources.getString(R.string.github_api_token)
        val url = "https://api.github.com/users/$login"
        val client = AsyncHttpClient()
        client.addHeader("Authorization", "token $apiKey")
        client.addHeader("User-Agent", "request")
        client.get(url, object : AsyncHttpResponseHandler() {
            @SuppressLint("SetTextI18n")
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray
            ) {
                showLoadingIndicator(false)
                tableDetails?.visibility = View.VISIBLE

                val result = String(responseBody)
                val resultObject = JSONObject(result)

                with(resultObject) {
                    val loginName = this.getString("login")
                    val fullname = this.getString("name")
                    val bio = this.getString("bio")
                    val company = this.getString("company")
                    val location = this.getString("location")
                    val publicRepos = this.getInt("public_repos")
                    val publicGists = this.getInt("public_gists")
                    val blog = this.getString("blog")
                    val createdAt = this.getString("created_at")

                    val createdDate =
                        SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault()).parse(
                            createdAt
                        ) as Date
                    val createdDateFormatted =
                        SimpleDateFormat("EEEE, dd MMMM yyyy HH:mm:ss", Locale.getDefault()).format(
                            createdDate
                        )

                    tvLogin?.text = loginName.toString()
                    tvName?.text = if (fullname == "null") "-" else fullname.toString()
                    tvBio?.text = if (bio == "null") "-" else bio.toString()
                    tvCompany?.text = if (company == "null") "-" else company.toString()
                    tvLocation?.text = if (location == "null") "-" else location.toString()
                    tvRepos?.text =
                        (if (publicRepos == 0) resources.getString(R.string.none) else publicRepos.toString()) + " ${resources.getString(
                            R.string.public_repos
                        )}"
                    tvGist?.text =
                        (if (publicGists == 0) resources.getString(R.string.none) else publicGists.toString()) + " ${resources.getString(
                            R.string.public_gists
                        )}"
                    tvBlog?.text = if (blog.isNullOrEmpty()) "-" else blog.toString()
                    tvDateCreated?.text = createdDateFormatted
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable?
            ) {
                showLoadingIndicator(false)
                showErrorMessage(
                    when (statusCode) {
                        401 -> "401: Bad Request"
                        403 -> "403: Access Forbidden"
                        404 -> "404: Not Found"
                        500 -> "500: Internal Server Error"
                        else -> error?.message.toString()
                    }
                )
            }

        })
    }

    fun showLoadingIndicator(state: Boolean) {
        when (state) {
            true -> {
                progressBar?.visibility = View.VISIBLE
                tvLoadingMessage?.visibility = View.VISIBLE
                octocat_sad?.visibility = View.GONE
                tvErrorMessage?.visibility = View.GONE
                tableDetails?.visibility = View.GONE
            }
            false -> {
                progressBar?.visibility = View.GONE
                tvLoadingMessage?.visibility = View.GONE
            }
        }
    }

    @SuppressLint("SetTextI18n")
    fun showErrorMessage(message: String) {
        octocat_sad?.visibility = View.VISIBLE
        tvErrorMessage?.visibility = View.VISIBLE
        progressBar?.visibility = View.GONE
        tvLoadingMessage?.visibility = View.GONE
        tableDetails?.visibility = View.GONE
        tvErrorMessage?.text = """
            ${resources.getString(R.string.error_message)}
            $message
        """.trimIndent()
    }
}