package graduationwork.buskingtown.model;


public class Profile {
    String user;
    String user_phone;
    String user_image;
    int purchase_coin;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getUser_phone() {
        return user_phone;
    }

    public void setUser_phone(String user_phone) {
        this.user_phone = user_phone;
    }

    public String getUser_image() {
        return user_image;
    }

    public void setUser_image(String user_image) {
        this.user_image = user_image;
    }

    public int getPurchase_coin() {
        return purchase_coin;
    }

    public void setPurchase_coin(int purchase_coin) {
        this.purchase_coin = purchase_coin;
    }
}
