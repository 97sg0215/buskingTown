package graduationwork.buskingtown.model;

import com.google.gson.annotations.SerializedName;

public class Connections {
    private int following;
    private int user;
    private int connection_id;
    private String connection_date;
    private int follower_count;

    public int getFollowing() {
        return following;
    }

    public void setFollowing(int following) {
        this.following = following;
    }

    public int getUser() {
        return user;
    }

    public void setUser(int user) {
        this.user = user;
    }

    public int getConnection_id() {
        return connection_id;
    }

    public void setConnection_id(int connection_id) {
        this.connection_id = connection_id;
    }

    public String getConnection_date() {
        return connection_date;
    }

    public void setConnection_date(String connection_date) {
        this.connection_date = connection_date;
    }

    public int getFollower_count() {
        return follower_count;
    }

    public void setFollower_count(int follower_count) {
        this.follower_count = follower_count;
    }
}
