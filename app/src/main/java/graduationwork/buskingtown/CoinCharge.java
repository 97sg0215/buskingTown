package graduationwork.buskingtown;

import android.app.Activity;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import graduationwork.buskingtown.api.RestApiService;
import graduationwork.buskingtown.model.Profile;
import graduationwork.buskingtown.model.PurchaseCoin;
import graduationwork.buskingtown.model.User;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Callback;
import retrofit2.Response;

public class CoinCharge extends AppCompatActivity {

    private final int coin_10 = 1;
    private final int coin_30 = 2;
    private final int coin_50 = 3;
    private final int coin_100 = 4;
    private final int coin_300 = 5;

    RelativeLayout ten, thirty, fifty, hundred, hundred_300;
    TextView coinOwnCount;

    String user_token;
    int user_id, origin_coin;

    RestApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coin_charge);

        restApiBuilder();

        getLocalData();

        coinOwnCount = (TextView) findViewById(R.id.coinOwnCount);

        retrofit2.Call<User> userCall = apiService.getUserDetail(user_token, user_id);
        userCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(retrofit2.Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    origin_coin = response.body().getProfile().getPurchase_coin();
                    coinOwnCount.setText(String.valueOf(origin_coin));
                }
            }

            @Override
            public void onFailure(retrofit2.Call<User> call, Throwable t) {

            }
        });

        ten = (RelativeLayout) findViewById(R.id.coinTen);
        ten.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chargeCoin(coin_10);
            }
        });
        thirty = (RelativeLayout) findViewById(R.id.coinThirty);
        thirty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chargeCoin(coin_30);
            }
        });
        fifty = (RelativeLayout) findViewById(R.id.coinFifty);
        fifty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chargeCoin(coin_50);
            }
        });
        hundred = (RelativeLayout) findViewById(R.id.coinOnehundred);
        hundred.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chargeCoin(coin_100);
            }
        });
        hundred_300 = (RelativeLayout) findViewById(R.id.coinThreehundred);
        hundred_300.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chargeCoin(coin_300);
            }
        });

        ImageButton backBtn = (ImageButton) findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CoinCharge.super.onBackPressed();
            }
        });
    }

    public void chargeCoin(int coin) {

        RequestBody user = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(user_id));
        RequestBody coin_set = null;
        PurchaseCoin purchaseCoin = null;

        switch (coin) {
            case 1:
                purchaseCoin = new PurchaseCoin(user_id, 10, origin_coin + 10);
                coin_set = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(origin_coin + 10));
                break;
            case 2:
                purchaseCoin = new PurchaseCoin(user_id, 30, origin_coin + 30);
                coin_set = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(origin_coin + 30));
                break;
            case 3:
                purchaseCoin = new PurchaseCoin(user_id, 50, origin_coin + 50);
                coin_set = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(origin_coin + 50));
                break;
            case 4:
                purchaseCoin = new PurchaseCoin(user_id, 100, origin_coin + 100);
                coin_set = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(origin_coin + 100));
                break;
            case 5:
                purchaseCoin = new PurchaseCoin(user_id, 300, origin_coin + 300);
                coin_set = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(origin_coin + 300));
                break;
        }

        retrofit2.Call<PurchaseCoin> purchaseCoinCall = apiService.purchaseCoin(user_token, purchaseCoin);
        RequestBody finalCoin_set = coin_set;
        purchaseCoinCall.enqueue(new Callback<PurchaseCoin>() {
            @Override
            public void onResponse(retrofit2.Call<PurchaseCoin> call, Response<PurchaseCoin> response) {
                if (response.isSuccessful()) {
                    retrofit2.Call<Profile> userCall = apiService.updateCoin(user_token, user_id, user, finalCoin_set);
                    userCall.enqueue(new Callback<Profile>() {
                        @Override
                        public void onResponse(retrofit2.Call<Profile> call, Response<Profile> response) {
                            if (response.isSuccessful()) {
                                origin_coin = response.body().getPurchase_coin();
                                coinOwnCount.setText(String.valueOf(origin_coin));
                            } else {
                                int StatusCode = response.code();
                                Log.i(ApplicationController.TAG, "상태 Code : " + StatusCode);
                            }
                        }

                        @Override
                        public void onFailure(retrofit2.Call<Profile> call, Throwable t) {

                        }
                    });
                } else {
                    int StatusCode = response.code();
                    Log.i(ApplicationController.TAG, "상태 Code : " + StatusCode);
                }
            }

            @Override
            public void onFailure(retrofit2.Call<PurchaseCoin> call, Throwable t) {

            }
        });


    }

    //백버튼 메소드
    public void previousActivity(View v) {
        onBackPressed();
    }

    public void restApiBuilder() {
        ApplicationController application = ApplicationController.getInstance();
        application.buildNetworkService();
        apiService = ApplicationController.getInstance().getRestApiService();
    }

    public void getLocalData() {
        SharedPreferences prefUser = this.getSharedPreferences("User", Activity.MODE_PRIVATE);
        user_token = prefUser.getString("auth_token", null);
        user_id = prefUser.getInt("user_id", 0);
    }
}