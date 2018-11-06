package graduationwork.buskingtown.model;

public class PurchaseCoin {
    int purchase_id,user,purchase_coin_amount;
    String purchase_date;

    public PurchaseCoin( int user, int purchase_coin_amount) {
        this.user = user;
        this.purchase_coin_amount = purchase_coin_amount;
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

    public String getPurchase_date() {
        return purchase_date;
    }

    public void setPurchase_date(String purchase_date) {
        this.purchase_date = purchase_date;
    }
}
