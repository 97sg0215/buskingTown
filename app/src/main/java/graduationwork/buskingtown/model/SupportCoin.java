package graduationwork.buskingtown.model;

public class SupportCoin {
    int supportCoin_id;
    int busker;
    int user;
    int coin_amount;
    String supportDate;
    String support_message;

    public int getSupportCoin_id() {
        return supportCoin_id;
    }

    public void setSupportCoin_id(int supportCoin_id) {
        this.supportCoin_id = supportCoin_id;
    }

    public int getBusker() {
        return busker;
    }

    public void setBusker(int busker) {
        this.busker = busker;
    }

    public int getUser() {
        return user;
    }

    public void setUser(int user) {
        this.user = user;
    }

    public int getCoin_amount() {
        return coin_amount;
    }

    public void setCoin_amount(int coin_amount) {
        this.coin_amount = coin_amount;
    }

    public String getSupportDate() {
        return supportDate;
    }

    public void setSupportDate(String supportDate) {
        this.supportDate = supportDate;
    }

    public String getSupport_message() {
        return support_message;
    }

    public void setSupport_message(String support_message) {
        this.support_message = support_message;
    }
}
