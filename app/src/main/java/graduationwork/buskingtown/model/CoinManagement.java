package graduationwork.buskingtown.model;

public class CoinManagement {
    int purchase_id,user,purchase_coin_amount;

    int supportCoin_id;
    int busker;
    int coin_amount;
    int coin_balance;
    String date_created;
    String support_message;

    String type;

    public int getPurchase_id() {
        return purchase_id;
    }

    public void setPurchase_id(int purchase_id) {
        this.purchase_id = purchase_id;
    }

    public int getUser() {
        return user;
    }

    public void setUser(int user) {
        this.user = user;
    }

    public int getPurchase_coin_amount() {
        return purchase_coin_amount;
    }

    public void setPurchase_coin_amount(int purchase_coin_amount) {
        this.purchase_coin_amount = purchase_coin_amount;
    }


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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getCoin_balance() {
        return coin_balance;
    }

    public void setCoin_balance(int coin_balance) {
        this.coin_balance = coin_balance;
    }

    public String getDate_created() {
        return date_created;
    }

    public void setDate_created(String date_created) {
        this.date_created = date_created;
    }
}
