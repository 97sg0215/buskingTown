package graduationwork.buskingtown;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import graduationwork.buskingtown.api.RestApiService;
import graduationwork.buskingtown.model.Busker;
import graduationwork.buskingtown.model.Profile;
import graduationwork.buskingtown.model.SupportCoin;
import graduationwork.buskingtown.model.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CoinManagement extends AppCompatActivity {

    String user_token;
    int busker_id;
    RestApiService apiService;

    TextView sponsorCount,message_noti;

    String origin_coin;

    LinearLayout message_container;
    View message_list;

    int user_id;
    String user_name,user_profile ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coin_management);
        getLocalData();

        restApiBuilder();

        message_container = (LinearLayout) findViewById(R.id.message_container);

        sponsorCount = (TextView) findViewById(R.id.sponsorCount);
        message_noti = (TextView) findViewById(R.id.message_noti);

        Call<Busker> buskerCall = apiService.buskerDetail(user_token,busker_id);
        buskerCall.enqueue(new Callback<Busker>() {
            @Override
            public void onResponse(Call<Busker> call, Response<Busker> response) {
                if(response.isSuccessful()){
                    Busker busker = response.body();
                    origin_coin = String.valueOf(busker.getReceived_coin());
                    sponsorCount.setText(origin_coin+" 개");
                }
            }

            @Override
            public void onFailure(Call<Busker> call, Throwable t) {

            }
        });

        Call<List<SupportCoin>> listCall = apiService.getCoin(user_token,busker_id);
        listCall.enqueue(new Callback<List<SupportCoin>>() {
            @Override
            public void onResponse(Call<List<SupportCoin>> call, Response<List<SupportCoin>> response) {
                if(response.isSuccessful()){
                    List<SupportCoin> supportCoins = response.body();
                    if(supportCoins.size()!=0){
                        message_noti.setVisibility(View.GONE);
                        for (int i = 0; i<supportCoins.size();i++){
                            message_list = getLayoutInflater().inflate(R.layout.message,message_container,false);

                            ImageView user_profile_v = (ImageView) message_list.findViewById(R.id.buskerImg);
                            TextView user_id_t = (TextView) message_list.findViewById(R.id.buskerId);

                            Call<User> profileCall = apiService.getUserDetail(user_token,supportCoins.get(i).getUser());
                            int finalI = i;
                            profileCall.enqueue(new Callback<User>() {
                                @Override
                                public void onResponse(Call<User> call, Response<User> response) {
                                    if(response.isSuccessful()){
                                        User user = response.body();
                                        user_id = user.getId();
                                        user_name = user.getUsername();
                                        user_profile = user.getProfile().getUser_image();
                                        Picasso.with(getApplicationContext()).load(String.valueOf(user.getProfile().getUser_image())).transform(new CircleTransForm()).into(user_profile_v);
                                        user_id_t.setText(user.getUsername());
                                    }
                                }

                                @Override
                                public void onFailure(Call<User> call, Throwable t) {

                                }
                            });

                            TextView from_message = (TextView) message_list.findViewById(R.id.editText);
                            TextView countCoin = (TextView) message_list.findViewById(R.id.countCoin);

                            String sub_message = supportCoins.get(finalI).getSupport_message();
                            if(sub_message.length()>50){
                                from_message.setText(sub_message.substring(0,40)+"...");
                            }else {
                                from_message.setText(sub_message);
                            }
                            countCoin.setText(String.valueOf(supportCoins.get(finalI).getCoin_amount()));

                            if(message_list.getParent()!= null)
                                ((ViewGroup)message_list.getParent()).removeView(message_list);
                            message_container.addView(message_list);
                            message_list.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent pop = new Intent(getApplication(),CoinSendAfter_pop.class);
                                    pop.putExtra("busker_id",busker_id);
                                    pop.putExtra("coin_amount",supportCoins.get(finalI).getCoin_amount());
                                    pop.putExtra("message",supportCoins.get(finalI).getSupport_message());
                                    pop.putExtra("username",user_name);
                                    pop.putExtra("user_id",user_id);
                                    pop.putExtra("support_id",supportCoins.get(finalI).getSupportCoin_id());
                                    pop.putExtra("origin_coin",origin_coin);
                                    startActivity(pop);
                                }
                            });

                        }
                    }
                }
            }
            @Override
            public void onFailure(Call<List<SupportCoin>> call, Throwable t) {
            }
        });
    }

    //user데이터 얻어오기
    private void getLocalData(){
        SharedPreferences pref = this.getSharedPreferences("User", Activity.MODE_PRIVATE);
        user_token = pref.getString("auth_token",null);

        SharedPreferences busker_pref = getSharedPreferences("BuskerUser", Activity.MODE_PRIVATE);
        busker_id = busker_pref.getInt("busker_id",0);
    }

    //api연결
    private void restApiBuilder() {
        ApplicationController application = ApplicationController.getInstance();
        application.buildNetworkService();
        apiService = ApplicationController.getInstance().getRestApiService();
    }

}
