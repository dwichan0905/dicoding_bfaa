package id.dwichan.githubusersparcellable;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

public class DetailsActivity extends AppCompatActivity {
    TextView tvNama, tvUsername, tvPengikut, tvMengikuti, tvAlamat, tvPerusahaan, tvRepositori;
    ImageView imgUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        // Inisialisasi object view
        tvNama = findViewById(R.id.tvNama);
        tvUsername = findViewById(R.id.tvUsername);
        tvPengikut = findViewById(R.id.tvPengikut);
        tvMengikuti = findViewById(R.id.tvMengikuti);
        tvAlamat = findViewById(R.id.tvAlamat);
        tvPerusahaan = findViewById(R.id.tvPerusahaan);
        tvRepositori = findViewById(R.id.tvRepositori);
        imgUser = findViewById(R.id.imgUser);

        // Ambil Parcellable dari Intent
        Users user = getIntent().getParcelableExtra("userSelected");

        // Ambil semua datanya!
        Glide.with(this)
                .load(Integer.parseInt(user.getAvatar()))
                .apply(new RequestOptions().override(100, 100))
                .into(imgUser);
        tvNama.setText(user.getName());
        tvUsername.setText(user.getUsername());
        tvPengikut.setText(user.getFollower() + " pengikut");
        tvMengikuti.setText(user.getFollowing() + " mengikuti");
        tvAlamat.setText(user.getLocation());
        tvPerusahaan.setText(user.getCompany());
        tvRepositori.setText(user.getRepository() + " repositori");

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Rincian " + user.getName());
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                break;
        }
        return true;
    }
}
