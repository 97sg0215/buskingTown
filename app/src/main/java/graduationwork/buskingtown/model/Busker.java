package graduationwork.buskingtown.model;

public class Busker {
    int user;
    int busker_id;
    int busker_type;
    String busker_name;
    String team_name;
    String busker_tag;
    String busker_image;
    Boolean certification;
    int received_coin;

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

    public int getBusker_type() {
        return busker_type;
    }

    public String getBusker_name() {
        return busker_name;
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

    public Boolean getCertification() {
        return certification;
    }

    public String getBusker_image() {
        return busker_image;
    }

    public int getReceived_coin() {
        return received_coin;
    }


}
