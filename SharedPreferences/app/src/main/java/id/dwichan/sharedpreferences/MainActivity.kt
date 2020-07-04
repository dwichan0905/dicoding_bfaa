package id.dwichan.sharedpreferences

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var mUserPreference: UserPreference

    private var isPreferenceEmpty = false
    private lateinit var userModel: UserModel

    companion object {
        private const val REQUEST_CODE = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.title = "My User Preferences"

        mUserPreference = UserPreference(this)

        showExistingPreference()

        btnSave.setOnClickListener(this)
    }

    private fun showExistingPreference() {
        userModel = mUserPreference.getUser()
        populateView(userModel)
        checkForm(userModel)
    }

    private fun checkForm(userModel: UserModel) {
        when {
            userModel.name.toString().isNotEmpty() -> {
                btnSave.text = getString(R.string.change)
                isPreferenceEmpty = false
            }
            else -> {
                btnSave.text = getString(R.string.save)
                isPreferenceEmpty = true
            }
        }
    }

    private fun populateView(userModel: UserModel) {
        tvName.text = if (userModel.name.toString().isEmpty()) "Tidak ada" else userModel.name
        tvAge.text = if (userModel.age.toString().isEmpty()) "Tidak ada" else userModel.age.toString()
        tvIsLoveMU.text = if (userModel.isLove) "Ya" else "Tidak"
        tvEmail.text = if (userModel.email.toString().isEmpty()) "Tidak ada" else userModel.email
        tvPhoneNumber.text = if (userModel.phoneNumber.toString().isEmpty()) "Tidak ada" else userModel.phoneNumber
    }

    override fun onClick(v: View?) {
        if (v?.id == R.id.btnSave) {
            val intent = Intent(this@MainActivity, FormUserPreferenceActivity::class.java)
            when {
                isPreferenceEmpty -> {
                    intent.putExtra(FormUserPreferenceActivity.EXTRA_TYPE_FORM, FormUserPreferenceActivity.TYPE_ADD)
                    intent.putExtra("USER", userModel)
                }
                else -> {
                    intent.putExtra(FormUserPreferenceActivity.EXTRA_TYPE_FORM, FormUserPreferenceActivity.TYPE_EDIT)
                    intent.putExtra("USER", userModel)
                }
            }
            startActivityForResult(intent, REQUEST_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE) {
            if (resultCode == FormUserPreferenceActivity.RESULT_CODE) {
                userModel = data?.getParcelableExtra(FormUserPreferenceActivity.EXTRA_RESULT) as UserModel
                populateView(userModel)
                checkForm(userModel)
            }
        }
    }
}