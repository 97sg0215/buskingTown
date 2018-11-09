package graduationwork.buskingtown;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import java.util.Objects;

import graduationwork.buskingtown.api.RestApiService;
import graduationwork.buskingtown.model.Busker;
import graduationwork.buskingtown.model.SupportCoin;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CoinSendAfter_pop extends Activity implements View.OnClickListener {

    RestApiService apiService;

    String user_token,message,username,origin_coin;
    int coin_amount, busker_id,user_id,support_id;

    TextView coinCount_after,message_after,username_after;

    Button ReceiveBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_coin_send_after_pop);

        getLocalData();

        restApiBuilder();

        message = getIntent().getStringExtra("message");
        coin_amount = getIntent().getIntExtra("coin_amount",0);
        username = getIntent().getStringExtra("username");
        busker_id = getIntent().getIntExtra("busker_id",0);
        support_id = getIntent().getIntExtra("support_id",0);
        origin_coin = getIntent().getStringExtra("origin_coin");
        user_id = getIntent().getIntExtra("user_id",0);

        coinCount_after = (TextView) findViewById(R.id.coinCount_after);
        username_after = (TextView) findViewById(R.id.username_after);
        message_after = (TextView) findViewById(R.id.contentsOne);
        ReceiveBtn = (Button) findViewById(R.id.ReceiveBtn);

        username_after.setText("From. "+username);
        message_after.setText(message);
        coinCount_after.setText(String.valueOf(coin_amount));

        findViewById(R.id.delete_after).setOnClickListener(this);

        ReceiveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SupportCoin s = new SupportCoin();
                s.setUser(user_id);
                s.setBusker(busker_id);
                s.setView_check(true);
                Call<SupportCoin> supportCoinCall = apiService.supportCoinCheck(user_token,support_id,s);
                supportCoinCall.enqueue(new Callback<SupportCoin>() {
                    @Override
                    public void onResponse(Call<SupportCoin> call, Response<SupportCoin> response) {
                        if(response.isSuccessful()){
                            RequestBody busker = RequestBody.create(MediaType.parse("multipart/form-data"),String.valueOf(user_id));
                            RequestBody coin_set = RequestBody.create(MediaType.parse("multipart/form-data"),String.valueOf(Integer.parseInt(origin_coin)+coin_amount));
                            Call<Busker> buskerCall = apiService.updateReceivedCoin(user_token,busker_id,busker,coin_set);
                            buskerCall.enqueue(new Callback<Busker>() {
                                @Override
                                public void onResponse(Call<Busker> call, Response<Busker> response) {
                                    if(response.isSuccessful()){

                                    }else {
                                        int StatusCode = response.code();
                                        String s = response.message();
                                        ResponseBody d = response.errorBody();
                                        Log.i(ApplicationController.TAG, "상태 Code : " + StatusCode);
                                        Log.e("메세지", s);
                                        Log.e("리스폰스에러바디", String.valueOf(d));
                                        Log.e("리스폰스바디", String.valueOf(response.body()));
                                    }
                                }

                                @Override
                                public void onFailure(Call<Busker> call, Throwable t) {

                                }
                            });
                            Intent newCoin = new Intent(getApplicationContext(),CoinManagement.class);
                            newCoin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(newCoin);
                        }else {
                            int StatusCode = response.code();
                            String s = response.message();
                            ResponseBody d = response.errorBody();
                            Log.i(ApplicationController.TAG, "상태 Code : " + StatusCode);
                            Log.e("메세지", s);
                            Log.e("리스폰스에러바디", String.valueOf(d));
                            Log.e("리스폰스바디", String.valueOf(response.body()));
                        }
                    }

                    @Override
                    public void onFailure(Call<SupportCoin> call, Throwable t) {

                    }
                });
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.delete:
                this.finish();
                break;
        }
    }

    private void getLocalData(){
        SharedPreferences pref = this.getSharedPreferences("User", Activity.MODE_PRIVATE);
        user_token = pref.getString("auth_token",null);

    }

    //api연결
    private void restApiBuilder() {
        ApplicationController application = ApplicationController.getInstance();
        application.buildNetworkService();
        apiService = ApplicationController.getInstance().getRestApiService();
    }
}
