package id.dwichan.githubusersparcellable;

import android.os.Parcel;
import android.os.Parcelable;

public class Users implements Parcelable {
    public Users() {
        // disini emang kosong ya? :v
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getRepository() {
        return repository;
    }

    public void setRepository(String repository) {
        this.repository = repository;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getFollower() {
        return follower;
    }

    public void setFollower(String follower) {
        this.follower = follower;
    }

    public String getFollowing() {
        return following;
    }

    public void setFollowing(String following) {
        this.following = following;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    private String username, name, location, repository, company, follower, following, avatar;

    protected Users(Parcel in) {
        username = in.readString();
        name = in.readString();
        location = in.readString();
        repository = in.readString();
        company = in.readString();
        follower = in.readString();
        following = in.readString();
        avatar = in.readString();
    }

    public static final Creator<Users> CREATOR = new Creator<Users>() {
        @Override
        public Users createFromParcel(Parcel in) {
            return new Users(in);
        }

        @Override
        public Users[] newArray(int size) {
            return new Users[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(username);
        dest.writeString(name);
        dest.writeString(location);
        dest.writeString(repository);
        dest.writeString(company);
        dest.writeString(follower);
        dest.writeString(following);
        dest.writeString(avatar);
    }
}
