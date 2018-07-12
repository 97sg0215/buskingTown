package graduationwork.buskingtown.model;

import com.google.gson.annotations.SerializedName;

public class SignUp {
    @SerializedName("username")
    private String username;
    @SerializedName("password")
    private String password;
    @SerializedName("email")
    private String email;
    @SerializedName("profile")
    private Profile profile;
    @SerializedName("busker")
    private Busker busker;


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public Busker getBusker() {
        return busker;
    }

    public void setBusker(Busker busker) {
        this.busker = busker;
    }
}