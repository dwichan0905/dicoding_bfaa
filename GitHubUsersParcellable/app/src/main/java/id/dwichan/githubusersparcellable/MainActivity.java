package id.dwichan.githubusersparcellable;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {
    RecyclerView rvListUser;
    private String[] nama, alamat, username, repository, follower, following, company;
    private TypedArray foto;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rvListUser = findViewById(R.id.rvListUser);
        rvListUser.setHasFixedSize(true);

        getValues();
        showRecyclerView();
    }

    private void showRecyclerView() {
        rvListUser.setLayoutManager(new LinearLayoutManager(this));
        ListUserAdapter lua = new ListUserAdapter(this, nama, alamat, username, repository, follower, following, foto, company);
        rvListUser.setAdapter(lua);
    }

    private void getValues() {
        nama = getResources().getStringArray(R.array.name);
        foto = getResources().obtainTypedArray(R.array.avatar);
        alamat = getResources().getStringArray(R.array.location);
        username = getResources().getStringArray(R.array.username);
        repository = getResources().getStringArray(R.array.repository);
        follower = getResources().getStringArray(R.array.followers);
        following = getResources().getStringArray(R.array.following);
        company = getResources().getStringArray(R.array.company);
    }

    public void switchMode(MenuItem item) {
        int checked = AppCompatDelegate.getDefaultNightMode();
        if (checked == AppCompatDelegate.MODE_NIGHT_NO) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    public void showAbout(MenuItem item) {
        Intent i = new Intent(MainActivity.this, AboutActivity.class);
        startActivity(i);
    }
}
