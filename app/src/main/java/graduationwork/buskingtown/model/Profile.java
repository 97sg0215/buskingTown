package graduationwork.buskingtown.model;

import com.google.gson.annotations.SerializedName;

public class Profile {
    @SerializedName("user")
    int user;
    @SerializedName("user_birth")
    String user_birth;
    @SerializedName("user_phone")
    String user_phone;

    public int getUser() {
        return user;
    }

    public void setUser(int user) {
        this.user = user;
    }

    public String getUser_birth() {
        return user_birth;
    }

    public void setUser_birth(String user_birth) {
        this.user_birth = user_birth;
    }

    public String getUser_phone() {
        return user_phone;
    }

    public void setUser_phone(String user_phone) {
        this.user_phone = user_phone;
    }
}
