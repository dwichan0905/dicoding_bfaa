package id.dwichan.githubusers.userList

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import id.dwichan.githubusers.R
import org.json.JSONObject

class UserViewModel : ViewModel() {

    val listUsers = MutableLiveData<ArrayList<UserItems>>()

    fun setUsers(query: String, adapter: UserAdapter, context: Context): String? {
        val apiKey = context.resources.getString(R.string.github_api_token)
        var ret: String? = null
        val listItems = ArrayList<UserItems>()
        val url = "https://api.github.com/search/users?q=${query}"
        val client = AsyncHttpClient()
        client.addHeader("Authorization", "token $apiKey")
        client.addHeader("User-Agent", "request")
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray
            ) {
                val result = String(responseBody)
                val resultObject = JSONObject(result)
                val list = resultObject.getJSONArray("items")

                for (i in 0 until list.length()) {
                    val users = list.getJSONObject(i)
                    val userItems = UserItems()
                    // get name and location
                    val urlGetSomeDetails =
                        "https://api.github.com/users/${users.getString("login")}"
                    client.get(urlGetSomeDetails, object : AsyncHttpResponseHandler() {
                        override fun onSuccess(
                            statusCode: Int,
                            headers: Array<out Header>?,
                            responseBody: ByteArray
                        ) {
                            val result2 = String(responseBody)
                            val resultObject2 = JSONObject(result2)
                            userItems.name =
                                if (resultObject2.getString("name") == "null") context.resources.getString(
                                    R.string.no_name
                                ) else resultObject2.getString("name")
                            userItems.public_repos = resultObject2.getInt("public_repos")
                            userItems.login = users.getString("login")
                            userItems.avatar = users.getString("avatar_url")
                            adapter.notifyDataSetChanged() // refresh adapter layout
                        }

                        override fun onFailure(
                            statusCode: Int,
                            headers: Array<out Header>?,
                            responseBody: ByteArray?,
                            error: Throwable?
                        ) {
                            Toast.makeText(
                                context,
                                context.resources.getString(R.string.catch_user_data_error),
                                Toast.LENGTH_SHORT
                            ).show()
                            userItems.name = "?"
                            userItems.public_repos = 0
                            userItems.avatar = ""
                            userItems.login = ""
                            adapter.notifyDataSetChanged() // refresh adapter layout
                        }
                    })
                    listItems.add(userItems)
                }

                listUsers.postValue(listItems)
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable?
            ) {
                ret = when (statusCode) {
                    401 -> "401: Bad Request"
                    403 -> "403: Access Forbidden"
                    404 -> "404: Not Found"
                    500 -> "500: Internal Server Error"
                    else -> "$statusCode: ${error?.message.toString()}"
                }
            }
        })
        return ret
    }

    fun getUsers(): LiveData<ArrayList<UserItems>> {
        return listUsers
    }
}