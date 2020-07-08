package id.dwichan.soundpool

import android.media.SoundPool
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var sp: SoundPool
    private var soundId: Int = 0
    private var spLoaded = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // SoundPool Loader
        sp = SoundPool.Builder().setMaxStreams(10).build()
        sp.setOnLoadCompleteListener { soundPool, sampleId, status ->
            if (status == 0) {
                spLoaded = true
            } else {
                Toast.makeText(this, "Gagal load raw/trumpets.wav", Toast.LENGTH_SHORT).show()
            }
        }
        soundId = sp.load(this, R.raw.trumpet, 1)
        // SoundPool Loader END

        btnTest.setOnClickListener {
            if (spLoaded) sp.play(soundId, 1f, 1f, 0, 0, 1f)
        }
    }
}