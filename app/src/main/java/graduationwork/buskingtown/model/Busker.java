package graduationwork.buskingtown.model;

import java.util.ArrayList;

public class Busker {
    int user;
    int busker_id;
    int busker_type;
    String busker_name;
    String team_name;
    String busker_tag;
    String busker_phone;
    String busker_image;
    Boolean certification;

    int received_coin;
    int follower_count;
    int score;

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

    public void setBusker_type(int busker_type) {
        this.busker_type = busker_type;
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

    public String getBusker_image() {
        return busker_image;
    }

    public void setBusker_image(String busker_image) {
        this.busker_image = busker_image;
    }

    public int getReceived_coin() {
        return received_coin;
    }

    public void setReceived_coin(int received_coin) {
        this.received_coin = received_coin;
    }

    public int getFollower_count() {
        return follower_count;
    }

    public void setFollower_count(int follower_count) {
        this.follower_count = follower_count;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
