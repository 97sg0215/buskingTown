package graduationwork.buskingtown.model;

public class PurchaseCoin {
    int user, purchase_coin_amount, coin_balance;

    public PurchaseCoin(int user, int purchase_coin_amount, int coin_balance) {
        this.user = user;
        this.purchase_coin_amount = purchase_coin_amount;
        this.coin_balance = coin_balance;
    }

    public int getUser() {
        return user;
    }

    public void setUser(int user) {
        this.user = user;
    }

}
