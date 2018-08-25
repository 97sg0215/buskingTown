package graduationwork.buskingtown.model;

import com.google.gson.annotations.SerializedName;

public class Connection {
    private int following;
    private int creator;
    private String created;

    public int getFollowing() {
        return following;
    }

    public void setFollowing(int following) {
        this.following = following;
    }

    public int getCreator() {
        return creator;
    }

    public void setCreator(int creator) {
        this.creator = creator;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }
}
