package id.dwichan.githubusers

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import id.dwichan.githubusers.alarm.AlarmReceiver

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.settings, SettingsFragment())
            .commit()

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.settings)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) super.onBackPressed()
        return true
    }

    class SettingsFragment : PreferenceFragmentCompat() {

        private lateinit var alarmReceiver: AlarmReceiver

        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            alarmReceiver = AlarmReceiver()
            setPreferencesFromResource(R.xml.root_preferences, rootKey)
            val reminderPreference = findPreference<SwitchPreference>("reminder")
            val languagePreference = findPreference<Preference>("language")
            reminderPreference?.setOnPreferenceClickListener {
                val isReminderOn: Boolean

                isReminderOn = if (reminderPreference.isChecked) {
                    // set alarm on
                    activity?.applicationContext?.let { it1 ->
                        context?.resources?.getString(R.string.reminder_message)?.let { it2 ->
                            alarmReceiver.setRepeatingAlarm(it1, it2)
                        }
                    }
                    true
                } else {
                    //set alarm off
                    activity?.applicationContext?.let { it1 ->
                        alarmReceiver.cancelRepeatingAlarm(it1)
                    }
                    false
                }

                isReminderOn
            }
            languagePreference?.setOnPreferenceClickListener {
                startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
                true
            }
        }
    }
}