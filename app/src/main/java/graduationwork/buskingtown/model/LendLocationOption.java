package graduationwork.buskingtown.model;

public class LendLocationOption {
    int user,provide;
    String provide_option_id, provide_option_name, provide_price;

    public int getUser() {
        return user;
    }

    public void setUser(int user) {
        this.user = user;
    }

    public int getProvide() {
        return provide;
    }

    public void setProvide(int provide) {
        this.provide = provide;
    }

    public String getProvide_option_id() {
        return provide_option_id;
    }

    public void setProvide_option_id(String provide_option_id) {
        this.provide_option_id = provide_option_id;
    }

    public String getProvide_option_name() {
        return provide_option_name;
    }

    public void setProvide_option_name(String provide_option_name) {
        this.provide_option_name = provide_option_name;
    }

    public String getProvide_price() {
        return provide_price;
    }

    public void setProvide_price(String provide_price) {
        this.provide_price = provide_price;
    }
}
