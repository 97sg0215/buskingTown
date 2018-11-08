package graduationwork.buskingtown.model;

public class SupportCoin {
    int supportCoin_id;
    int busker;
    int user;
    int coin_amount;
    int coin_balance;
    String support_message;
    String date_created;
    boolean view_check;

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

    public String getDate_created() {
        return date_created;
    }

    public void setDate_created(String date_created) {
        this.date_created = date_created;
    }

    public String getSupport_message() {
        return support_message;
    }

    public void setSupport_message(String support_message) {
        this.support_message = support_message;
    }

    public int getCoin_balance() {
        return coin_balance;
    }

    public void setCoin_balance(int coin_balance) {
        this.coin_balance = coin_balance;
    }

    public boolean isView_check() {
        return view_check;
    }

    public void setView_check(boolean view_check) {
        this.view_check = view_check;
    }
}
