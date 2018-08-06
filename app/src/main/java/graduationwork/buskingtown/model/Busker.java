package graduationwork.buskingtown.model;

public class Busker {
    int user;
    int busker_id;
    String busker_name;
    String team_name;
    String busker_tag;
    String busker_phone;
    Boolean certification;

    public int getBusker_id() {
        return busker_id;
    }

    public void setBusker_id(int busker_id) {
        this.busker_id = busker_id;
    }

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

    public String getBusker_tag() {
        return busker_tag;
    }

    public void setBusker_tag(String busker_tag) {
        this.busker_tag = busker_tag;
    }

    public String getBusker_phone() {
        return busker_phone;
    }

    public void setBusker_phone(String busker_phone) {
        this.busker_phone = busker_phone;
    }

    public Boolean getCertification() {
        return certification;
    }

    public void setCertification(Boolean certification) {
        this.certification = certification;
    }
}
