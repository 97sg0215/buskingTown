package graduationwork.buskingtown.model;

public class CoinManagement {
    int user,purchase_coin_amount;
    int supportCoin_id;
    int busker;
    int coin_amount;
    int coin_balance;
    String date_created;
    String support_message;
    String type;

    public int getUser() {
        return user;
    }

    public void setUser(int user) {
        this.user = user;
    }

    public int getPurchase_coin_amount() {
        return purchase_coin_amount;
    }

    public int getBusker() {
        return busker;
    }

    public void setBusker(int busker) {
        this.busker = busker;
    }

    public int getCoin_amount() {
        return coin_amount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getCoin_balance() {
        return coin_balance;
    }

    public String getDate_created() {
        return date_created;
    }

}
