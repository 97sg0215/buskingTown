package graduationwork.buskingtown.model;
import java.util.HashMap;
import java.util.Map;

public class SignUp {
    private String email;
    private String username;
    private String password;
    Profile profile;
    Busker busker;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

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
