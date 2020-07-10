package id.dwichan.githubconsumerapps.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import id.dwichan.githubconsumerapps.BuildConfig
import id.dwichan.githubconsumerapps.R
import id.dwichan.githubconsumerapps.UserDetailActivity
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
        showLoadingIndicator(true)

        val login = activity?.intent?.getStringExtra(UserDetailActivity.EXTRA_LOGIN)
        val apiKey = BuildConfig.GITHUB_API_KEY
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
                    val fullName = if (this.getString("name")
                            .isNullOrEmpty() || this.getString("name") == "null"
                    ) "-" else this.getString("name")
                    val bio = if (this.getString("bio")
                            .isNullOrEmpty() || this.getString("bio") == "null"
                    ) "-" else this.getString("bio")
                    val company = if (this.getString("company")
                            .isNullOrEmpty() || this.getString("company") == "null"
                    ) "-" else this.getString("company")
                    val location = if (this.getString("location").isNullOrEmpty() || this.getString(
                            "location"
                        ) == "null"
                    ) "-" else this.getString("location")
                    val publicRepos =
                        (if (this.getInt("public_repos") == 0) context?.resources?.getString(R.string.none) else this.getInt(
                            "public_repos"
                        ).toString()) + " ${context?.resources?.getString(
                            R.string.public_repos
                        )}"
                    val publicGists =
                        (if (this.getInt("public_gists") == 0) context?.resources?.getString(R.string.none) else this.getInt(
                            "public_gists"
                        ).toString()) + " ${context?.resources?.getString(
                            R.string.public_gists
                        )}"
                    val blog = if (this.getString("blog")
                            .isNullOrEmpty() || this.getString("blog") == "null"
                    ) "-" else this.getString("blog")
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
                    tvName?.text = fullName
                    tvBio?.text = bio
                    tvCompany?.text = company
                    tvLocation?.text = location
                    tvRepos?.text = publicRepos
                    tvGist?.text = publicGists
                    tvBlog?.text = blog
                    tvDateCreated?.text = createdDateFormatted
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable?
            ) {
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

    private fun showLoadingIndicator(state: Boolean) {
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