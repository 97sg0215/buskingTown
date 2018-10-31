package graduationwork.buskingtown;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;

import graduationwork.buskingtown.api.RestApiService;
import graduationwork.buskingtown.model.SupportCoin;
import graduationwork.buskingtown.model.User;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CoinSendBefore_pop extends Activity implements View.OnClickListener{

    EditText coin_Count;
    String c_Count,user_token;
    int busker_id,user_id,coin_amount;
    RestApiService apiService;
    Button sendBtn;
    TextView coinTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_coin_send_before_pop);

        restApiBuilder();

        getLocalData();

        coinTotal = (TextView) findViewById(R.id.coinTotal);

        sendBtn = findViewById(R.id.sendBtn);
        sendBtn.setOnClickListener(null);

        busker_id = getIntent().getIntExtra("busker_id", 0);

        findViewById(R.id.delete).setOnClickListener(this);

        coin_Count = (EditText) findViewById(R.id.coinCount);

        Call<User> userCall = apiService.getUserDetail(user_token,user_id);
        userCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.isSuccessful()){
                    User user = response.body();
                    coin_amount = user.getProfile().getPurchase_coin();
                    coinTotal.setText("내 코인 "+ coin_amount+ " 개");
                    coin_Count.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            c_Count = coin_Count.getText().toString();
                            if (!coin_Count.getText().toString().replace(" ", "").equals("")) {
                                sendBtn.setBackground(getDrawable(R.drawable.able_btn));
                                sendBtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if(coin_amount>Integer.parseInt(c_Count)){
                                            user.getProfile().setPurchase_coin(coin_amount-Integer.parseInt(c_Count));
                                            SupportCoin coin = new SupportCoin();
                                            coin.setCoin_amount(Integer.parseInt(c_Count));
                                            coin.setBusker(busker_id);
                                            coin.setUser(user_id);
                                            retrofit2.Call<SupportCoin> c_coin = apiService.supportCoin(user_token,coin);
                                            c_coin.enqueue(new Callback<SupportCoin>() {
                                                @Override
                                                public void onResponse(retrofit2.Call<SupportCoin> call, Response<SupportCoin> response){
                                                    if (response.isSuccessful()) {
                                                        Intent successfulcoin = new Intent(CoinSendBefore_pop.this, CoinSendSuccess_pop.class);
                                                        startActivity(successfulcoin);
                                                    }else{
                                                        int StatusCode = response.code();
                                                        String s = response.message();
                                                        ResponseBody d = response.errorBody();
                                                        Log.i(ApplicationController.TAG, "상태 Code : " + StatusCode);
                                                        Log.e("메세지", String.valueOf(user_id));
                                                        Log.e("메세지", String.valueOf(busker_id));
                                                        Log.e("메세지", s);
                                                        Log.e("리스폰스에러바디", String.valueOf(d));
                                                        Log.e("리스폰스바디", String.valueOf(response.body()));
                                                    }
                                                }

                                                @Override
                                                public void onFailure(retrofit2.Call<SupportCoin> call, Throwable t) {

                                                }
                                            });
                                        }else {
                                            Intent fail = new Intent(getApplication(),CoinSendFail_pop.class);
                                            startActivity(fail);
                                        }
                                    }
                                });
                            }
                        }
                    });

                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });


    }

    public void userCoinCheck(String user_token,int user_id){

    }
    public void restApiBuilder() {
        ApplicationController application = ApplicationController.getInstance();
        application.buildNetworkService();
        apiService = ApplicationController.getInstance().getRestApiService();
    }

    public void getLocalData() {
        SharedPreferences pref = getSharedPreferences("User", Activity.MODE_PRIVATE);
        user_token = pref.getString("auth_token", null);
        user_id = pref.getInt("user_id", 0);

    }


    public void onClick(View v){
        switch (v.getId()){
            case R.id.delete:
                this.finish();
                break;
            }
        }
}
