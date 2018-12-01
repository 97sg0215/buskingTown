package graduationwork.buskingtown.model;

public class LendLocation {
    int provide_id,user;
    String provide_image, provider_phone, provider_email, provide_start_time, provide_end_time,provide_location, provide_description
            ,provide_rule, provide_refund_rule,provide_location_name;
    double provide_lon , provide_lat;

    public int getProvide_id() {
        return provide_id;
    }

    public void setProvide_id(int provide_id) {
        this.provide_id = provide_id;
    }

    public int getUser() {
        return user;
    }

    public void setUser(int user) {
        this.user = user;
    }

    public String getProvide_image() {
        return provide_image;
    }

    public void setProvide_image(String provide_image) {
        this.provide_image = provide_image;
    }

    public String getProvider_phone() {
        return provider_phone;
    }

    public void setProvider_phone(String provider_phone) {
        this.provider_phone = provider_phone;
    }

    public String getProvider_email() {
        return provider_email;
    }

    public void setProvider_email(String provider_email) {
        this.provider_email = provider_email;
    }

    public String getProvide_start_time() {
        return provide_start_time;
    }

    public String getProvide_end_time() {
        return provide_end_time;
    }

    public String getProvide_location() {
        return provide_location;
    }

    public String getProvide_description() {
        return provide_description;
    }

    public String getProvide_rule() {
        return provide_rule;
    }

    public String getProvide_refund_rule() {
        return provide_refund_rule;
    }

    public String getProvide_location_name() {
        return provide_location_name;
    }

    public double getProvide_lon() {
        return provide_lon;
    }

    public double getProvide_lat() {
        return provide_lat;
    }
}
