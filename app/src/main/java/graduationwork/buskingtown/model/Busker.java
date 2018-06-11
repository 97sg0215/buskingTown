package graduationwork.buskingtown.model;

public class Busker {
     int user;
     String busker_user_name;
     String busker_team_name;
     String busker_phone;
     String busker_tag;
     String busker_image;
     boolean certification;

    public int getUser() {
        return user;
    }

    public void setUser(int user) {
        this.user = user;
    }

    public String getBusker_user_name() {
        return busker_user_name;
    }

    public void setBusker_user_name(String busker_user_name) {
        this.busker_user_name = busker_user_name;
    }

    public String getBusker_team_name() {
        return busker_team_name;
    }

    public void setBusker_team_name(String busker_team_name) {
        this.busker_team_name = busker_team_name;
    }

    public String getBusker_phone() {
        return busker_phone;
    }

    public void setBusker_phone(String busker_phone) {
        this.busker_phone = busker_phone;
    }

    public String getBusker_tag() {
        return busker_tag;
    }

    public void setBusker_tag(String busker_tag) {
        this.busker_tag = busker_tag;
    }

    public String getBusker_image() {
        return busker_image;
    }

    public void setBusker_image(String busker_image) {
        this.busker_image = busker_image;
    }

    public boolean isCertification() {
        return certification;
    }

    public void setCertification(boolean certification) {
        this.certification = certification;
    }
}
