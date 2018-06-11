package graduationwork.buskingtown.model;

public class UserDetail {
    private String email;
    private String username;
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
