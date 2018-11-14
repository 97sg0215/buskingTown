package graduationwork.buskingtown.model;

public class ConcertReservation {
    int  busker, provide, reservation_id,provide_option, concert_fee;
    String concert_date, concert_start_time, concert_end_time;

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

    public void setReservation_id(int reservation_id) {
        this.reservation_id = reservation_id;
    }

    public int getConcert_fee() {
        return concert_fee;
    }

    public void setConcert_fee(int concert_fee) {
        this.concert_fee = concert_fee;
    }

    public String getConcert_date() {
        return concert_date;
    }

    public void setConcert_date(String concert_date) {
        this.concert_date = concert_date;
    }

    public String getConcert_start_time() {
        return concert_start_time;
    }

    public void setConcert_start_time(String concert_start_time) {
        this.concert_start_time = concert_start_time;
    }

    public String getConcert_end_time() {
        return concert_end_time;
    }

    public void setConcert_end_time(String concert_end_time) {
        this.concert_end_time = concert_end_time;
    }
}
