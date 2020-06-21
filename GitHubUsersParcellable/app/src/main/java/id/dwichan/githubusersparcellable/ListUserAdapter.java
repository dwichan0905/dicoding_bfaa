package id.dwichan.githubusersparcellable;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

public class ListUserAdapter extends RecyclerView.Adapter<ListUserAdapter.ListViewHolder> {
    private Context con;
    private String[] nama, alamat, username, repository, follower, following, company;
    private TypedArray pic;

    public ListUserAdapter(Context con, String[] nama, String[] alamat, String[] username, String[] repository, String[] follower, String[] following, TypedArray pic, String[] company) {
        this.con = con;
        this.nama = nama;
        this.alamat = alamat;
        this.username = username;
        this.repository = repository;
        this.follower = follower;
        this.following = following;
        this.pic = pic;
        this.company = company;
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_github_users, parent, false);
        return new ListViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, final int position) {
        Glide.with(con)
                .load(pic.getResourceId(position, -1))
                .apply(new RequestOptions().override(70, 70))
                .into(holder.imgPic);
        holder.tvFullname.setText(nama[position]);
        holder.tvAddress.setText(alamat[position]);

        // Parcellable nya disini
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Users users = new Users();
                users.setUsername(username[position]);
                users.setName(nama[position]);
                users.setAvatar(String.valueOf(pic.getResourceId(position, -1)));
                users.setCompany(company[position]);
                users.setLocation(alamat[position]);
                users.setRepository(repository[position]);
                users.setFollower(follower[position]);
                users.setFollowing(following[position]);

                // Masukin parcellable ke intent sebagai parameter, lalu jalankan activity
                Intent i = new Intent(con, DetailsActivity.class).putExtra("userSelected", users);
                con.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return nama.length;
    }

    public class ListViewHolder extends RecyclerView.ViewHolder {
        ImageView imgPic;
        TextView tvFullname, tvAddress;

        public ListViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPic = itemView.findViewById(R.id.imgPic);
            tvFullname = itemView.findViewById(R.id.tvFullname);
            tvAddress = itemView.findViewById(R.id.tvAddress);
        }
    }
}
