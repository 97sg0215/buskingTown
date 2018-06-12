package graduationwork.buskingtown.model;

public class Busker {
     private int user;
     private String busker_name;
     private String team_name;
     private String busker_phone;
     private String busker_tag;
     private String busker_image;
     private boolean certification;

    public int getUser() {
        return user;
    }

    public void setUser(int user) {
        this.user = user;
    }

    public String getBusker_name() {
        return busker_name;
    }

    public void setBusker_name(String busker_name) {
        this.busker_name = busker_name;
    }

    public String getTeam_name() {
        return team_name;
    }

    public void setTeam_name(String team_name) {
        this.team_name = team_name;
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
