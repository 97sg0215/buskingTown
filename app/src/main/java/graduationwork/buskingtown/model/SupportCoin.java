package graduationwork.buskingtown.model;

public class SupportCoin {
    int supportCoin_id;
    int busker;
    int user;
    int coin_amount;
    int coin_balance;
    String support_message;
    boolean view_check;
    int daily_coin_amount;

    public int getSupportCoin_id() {
        return supportCoin_id;
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

    public String getSupport_message() {
        return support_message;
    }

    public void setSupport_message(String support_message) {
        this.support_message = support_message;
    }

    public void setCoin_balance(int coin_balance) {
        this.coin_balance = coin_balance;
    }

    public void setView_check(boolean view_check) {
        this.view_check = view_check;
    }

    public int getDaily_coin_amount() {
        return daily_coin_amount;
    }

}
