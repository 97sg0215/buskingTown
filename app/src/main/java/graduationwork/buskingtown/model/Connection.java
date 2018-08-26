package graduationwork.buskingtown.model;

import com.google.gson.annotations.SerializedName;

public class Connection {
    private int following;
    private int user;
    private int connection_id;

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
}
