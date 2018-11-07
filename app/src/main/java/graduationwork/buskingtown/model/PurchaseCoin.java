package graduationwork.buskingtown.model;

public class PurchaseCoin {
    int purchase_id,user,purchase_coin_amount, coin_balance;
    String date_created;

    public PurchaseCoin( int user, int purchase_coin_amount, int coin_balance) {
        this.user = user;
        this.purchase_coin_amount = purchase_coin_amount;
        this.coin_balance = coin_balance;
    }

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

    public String getDate_created() {
        return date_created;
    }

    public void setDate_created(String date_created) {
        this.date_created = date_created;
    }

    public int getCoin_balance() {
        return coin_balance;
    }

    public void setCoin_balance(int coin_balance) {
        this.coin_balance = coin_balance;
    }
}
