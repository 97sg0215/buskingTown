package graduationwork.buskingtown.model;

import java.util.ArrayList;
import java.util.List;

public class Profile {
    String user;
    String user_phone;
    List<Busker> busker;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getUser_phone() {
        return user_phone;
    }

    public void setUser_phone(String user_phone) {
        this.user_phone = user_phone;
    }

    public List<Busker> get_followings(){
        return busker;
    }
}
