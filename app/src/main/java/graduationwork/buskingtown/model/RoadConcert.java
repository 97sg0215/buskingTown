package graduationwork.buskingtown.model;

public class RoadConcert {
    int road_concert_id, busker;
    String road_address, road_name, road_concert_date, road_concert_start_time, road_concert_end_time;
    double road_lon, road_lat;

    public int getRoad_concert_id() {
        return road_concert_id;
    }

    public void setRoad_concert_id(int road_concert_id) {
        this.road_concert_id = road_concert_id;
    }

    public int getBusker() {
        return busker;
    }

    public void setBusker(int busker) {
        this.busker = busker;
    }

    public String getRoad_address() {
        return road_address;
    }

    public void setRoad_address(String road_address) {
        this.road_address = road_address;
    }

    public String getRoad_name() {
        return road_name;
    }

    public void setRoad_name(String road_name) {
        this.road_name = road_name;
    }

    public String getRoad_concert_date() {
        return road_concert_date;
    }

    public void setRoad_concert_date(String road_concert_date) {
        this.road_concert_date = road_concert_date;
    }

    public String getRoad_concert_start_time() {
        return road_concert_start_time;
    }

    public void setRoad_concert_start_time(String road_concert_start_time) {
        this.road_concert_start_time = road_concert_start_time;
    }

    public String getRoad_concert_end_time() {
        return road_concert_end_time;
    }

    public void setRoad_concert_end_time(String road_concert_end_time) {
        this.road_concert_end_time = road_concert_end_time;
    }

    public double getRoad_lon() { return road_lon; }

    public void setRoad_lon(double lon) { this.road_lon = lon; }

    public double getRoad_lat() { return road_lat; }

    public void setRoad_lat(double lat) { this.road_lat = lat; }
}
