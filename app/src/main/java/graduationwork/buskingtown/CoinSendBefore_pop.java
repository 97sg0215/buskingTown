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
import android.widget.EditText;

import graduationwork.buskingtown.api.RestApiService;
import graduationwork.buskingtown.model.SupportCoin;
import okhttp3.ResponseBody;
import retrofit2.Callback;
import retrofit2.Response;


public class CoinSendBefore_pop extends Activity implements View.OnClickListener{

    EditText coin_Count;
    String c_Count,user_token;
    int busker_id,user_id,post_id;
    RestApiService apiService;
    Response<Object> response;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_coin_send_before_pop);

        restApiBuilder();


        busker_id = getIntent().getIntExtra("busker_id", busker_id);

        findViewById(R.id.delete).setOnClickListener(this);

        coin_Count = (EditText) findViewById(R.id.coinCount);


        SupportCoin coin = new SupportCoin();
        coin.setCoin_amount(coin);
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
                        Log.e("메세지", String.valueOf(post_id));
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
