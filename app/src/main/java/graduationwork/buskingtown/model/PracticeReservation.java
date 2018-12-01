package graduationwork.buskingtown.model;

public class PracticeReservation {
    int  busker, provide, reservation_id,provide_option, practice_fee;
    String practice_date, practice_start_time, practice_end_time;

    public int getBusker() {
        return busker;
    }

    public void setBusker(int busker) {
        this.busker = busker;
    }

    public int getProvide() {
        return provide;
    }

    public void setProvide(int provide) {
        this.provide = provide;
    }

    public int getProvide_option() {
        return provide_option;
    }

    public void setProvide_option(int provide_option) {
        this.provide_option = provide_option;
    }

    public int getReservation_id() {
        return reservation_id;
    }

    public String getPractice_date() {
        return practice_date;
    }

    public void setPractice_date(String practice_date) {
        this.practice_date = practice_date;
    }

    public String getPractice_start_time() {
        return practice_start_time;
    }

    public void setPractice_start_time(String practice_start_time) {
        this.practice_start_time = practice_start_time;
    }

    public String getPractice_end_time() {
        return practice_end_time;
    }

    public void setPractice_end_time(String practice_end_time) {
        this.practice_end_time = practice_end_time;
    }

    public void setPractice_fee(int practice_fee) {
        this.practice_fee = practice_fee;
    }
}
