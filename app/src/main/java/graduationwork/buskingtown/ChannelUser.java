package graduationwork.buskingtown;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import graduationwork.buskingtown.api.RestApiService;
import graduationwork.buskingtown.model.Busker;
import graduationwork.buskingtown.model.Connections;
import graduationwork.buskingtown.model.Profile;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChannelUser extends AppCompatActivity {

    int test_schedule=5;
    int test_concert=5;

    int busker_id, user_id, connection_id;
    String user_token, user_name, busker_team_name, busker_tag, busker_image;

    //버스커 정보 세팅
    TextView mainTeamName, subTeamName, tag;
    ImageView busker_main_image;

    Button following_btn;

    ArrayList<Integer> all_user_id = new ArrayList<>();
    ArrayList<Integer> all_busker_id = new ArrayList<>();
    ArrayList<Integer> get_follower_id = new ArrayList<>();

    RestApiService apiService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channel_user);

        restApiBuilder();

        getLocalData();

        busker_id = getIntent().getIntExtra("busker_id", busker_id);

        getBuskerData(user_token,busker_id);

        //버스커 정보 세팅
        mainTeamName = (TextView) findViewById(R.id.busker_main_team_name);
        subTeamName = (TextView) findViewById(R.id.busker_sub_team_name);
        tag = (TextView) findViewById(R.id.tag);
        busker_main_image = (ImageView) findViewById(R.id.profilebig);
        following_btn = (Button) findViewById(R.id.fan_on);


        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        for (int scheduleCount=0; scheduleCount<test_schedule; scheduleCount++) {
            addSchedule(inflater);
        }
        for (int scheduleCount=0; scheduleCount<test_concert; scheduleCount++) {
            addConcert(inflater);
        }
    }

    public void following_check(int busker_id){
        Call<List<Connections>> getConnections = apiService.get_followers(user_token, busker_id);
        getConnections.enqueue(new Callback<List<Connections>>() {
            @Override
            public void onResponse(Call<List<Connections>> call, Response<List<Connections>> response) {
                if (response.isSuccessful()) {
                    List<Connections> connections = response.body();
                    if(connections.size()!=0){
                        for (int i = 0; i < connections.size(); i++) {
                            all_user_id.add(connections.get(i).getUser());
                            //팔로우 되어있을 경우
                            if (user_id == all_user_id.get(i)) {
                                connection_id = connections.get(i).getConnection_id();
                                following_btn.setBackground(getDrawable(R.drawable.fan_on_btn));
                                following_btn.setTextColor(Color.parseColor("#000000"));
                                following_btn.setText("팬이에요");
                                String fan_stat = following_btn.getText().toString();
                                unfollowing(connection_id, fan_stat);
                            }//팔로우 안되어 있을 경우
                            else {
                                follow(user_id,busker_id);
                            }
                        }
                    }//커넥션 목록 없을때
                    else {
                        follow(user_id,busker_id);
                    }
                } else {
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
            public void onFailure(Call<List<Connections>> call, Throwable t) {
                Log.i(ApplicationController.TAG, "커넥션 정보 서버 연결 실패 Message : " + t.getMessage());
            }
        });
    }

    public void follow(int user_id, int busker_id){
        following_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Connections connections = new Connections();
                connections.setUser(user_id);
                connections.setFollowing(busker_id);
                Call<Connections> follow = apiService.follow(user_token,connections);
                follow.enqueue(new Callback<Connections>() {
                    @Override
                    public void onResponse(Call<Connections> call, Response<Connections> response) {
                        if(response.isSuccessful()){
                            Log.e("팔로워ok", String.valueOf(user_id));
                            Log.e("버스커ok", String.valueOf(busker_id));
                            following_btn.setBackground(getDrawable(R.drawable.fan_on_btn));
                            following_btn.setTextColor(getResources().getColor(R.color.mainPurple));
                            following_btn.setText("팬이에요");
                            int connection_id = response.body().getConnection_id();
                            Log.e("커넥션 아이디",String.valueOf(connection_id));
                            unfollowing(connection_id,following_btn.getText().toString());
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
                    public void onFailure(Call<Connections> call, Throwable t) {
                        Log.i(ApplicationController.TAG, "팔로우 정보 서버 연결 실패 Message : " + t.getMessage());
                    }
                });
            }
        });
    }

    public void unfollowing(int connection_id,String text){
        if(text.equals("팬이에요")){
            following_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Call<Connections> unfollow = apiService.unfollow(user_token,connection_id);
                    unfollow.enqueue(new Callback<Connections>() {
                        @Override
                        public void onResponse(Call<Connections> call, Response<Connections> response) {
                            if (response.isSuccessful()) {
                                Log.e("언팔로우:", "완료");
                                Log.e("언팔로우된 커넥션 아이디:", String.valueOf(connection_id));
                                following_btn.setText("팬 할래요");
                                following_btn.setTextColor(Color.parseColor("#FFFFFF"));
                                following_btn.setBackground(getDrawable(R.drawable.able_btn));
                                follow(user_id,busker_id);
                            } else {
                                int StatusCode = response.code();
                                Log.i(ApplicationController.TAG, "상태 Code : " + StatusCode);
                                Log.e("메세지", String.valueOf(response.message()));
                                Log.e("리스폰스에러바디", String.valueOf(response.errorBody()));
                                Log.e("리스폰스바디", String.valueOf(response.body()));
                            }
                        }

                        @Override
                        public void onFailure(Call<Connections> call, Throwable t) {
                            Log.i(ApplicationController.TAG, "언팔로우 정보 서버 삭제 실패 Message : " + t.getMessage());
                        }
                    });
                }
            });
        }
    }

    public void addSchedule(final LayoutInflater inflater){
        final LinearLayout scheduleBox = (LinearLayout) findViewById(R.id.addSchedule);
        final ImageButton dropdownBtn = (ImageButton) findViewById(R.id.dropdown);
        if (test_schedule > 1 ){
            dropdownBtn.setVisibility(View.VISIBLE);
            View list = inflater.inflate(R.layout.schedule_list,scheduleBox,false);
            if(list.getParent()!= null)
                ((ViewGroup)list.getParent()).removeView(list);
            scheduleBox.addView(list);
            scheduleBox.setVisibility(View.GONE);
            dropdownBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    scheduleBox.setVisibility(View.VISIBLE);
                    dropdownBtn.setBackground(getDrawable(R.drawable.more));
                }
            });
        }
    }

    public void addConcert(final LayoutInflater inflater){
        final LinearLayout concertBox = (LinearLayout) findViewById(R.id.addConcert);
        final ImageButton dropdownBtn2 = (ImageButton) findViewById(R.id.dropdown2);
        if(test_concert > 1){
            dropdownBtn2.setVisibility(View.VISIBLE);
            View list = inflater.inflate(R.layout.concert_list,concertBox,false);
            if(list.getParent()!=null)
                ((ViewGroup)list.getParent()).removeView(list);
            concertBox.addView(list);
            concertBox.setVisibility(View.GONE);
            dropdownBtn2.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    concertBox.setVisibility(View.VISIBLE);
                    dropdownBtn2.setBackground(getDrawable(R.drawable.more));
                }
            });
        }
    }

    //user데이터 얻어오기
    public void getLocalData(){
        SharedPreferences pref = getSharedPreferences("User", Activity.MODE_PRIVATE);
        user_token = pref.getString("auth_token",null);
        user_name = pref.getString("username",null);
        user_id = pref.getInt("user_id",0);
    }

    //버스커 정보 화면에 세팅(이름 및 팔로워등 세팅, 메소드가 길어지면 게시글 같은 건 따로 메소드 정의하세요)
    public void buskerSetting(String busker_team_name, String busker_tag,String busker_image){
        Log.e("버스커 정보", String.valueOf(busker_team_name));
        mainTeamName.setText(busker_team_name);
        subTeamName.setText(busker_team_name);
        tag.setText(busker_tag);

        Picasso.with(getApplication()).load(busker_image).transform(new CircleTransForm()).into(busker_main_image);
    }

    //busker데이터 얻어오기
    public void getBuskerData(String token, int busker_id){
        final Busker[] busker = {new Busker()};
        retrofit2.Call<Busker> buskerDetail = apiService.buskerDetail(token,busker_id);
        buskerDetail.enqueue(new Callback<Busker>() {
            @Override
            public void onResponse(retrofit2.Call<Busker> call, Response<Busker> response) {
                if(response.isSuccessful()){
                    busker[0] = response.body();
                    busker_team_name = busker[0].getTeam_name();
                    busker_tag = busker[0].getBusker_tag();
                    busker_image = busker[0].getBusker_image();
                    buskerSetting(busker_team_name,busker_tag,busker_image);
                    following_check(busker[0].getBusker_id());
                }
                else {
                    //에러 상태 보려고 해둔 코드
                    int StatusCode = response.code();
                    String s = response.message();
                    ResponseBody d = response.errorBody();
                    Log.i(ApplicationController.TAG, "상태 Code : " + StatusCode);
                    Log.e("메세지", s);
                    Log.e("리스폰스에러바디", String.valueOf(d));
                    Log.e("리스폰스바디 : ", String.valueOf(response.body()));
                }
            }
            @Override
            public void onFailure(retrofit2.Call<Busker> call, Throwable t) {
                Log.i(ApplicationController.TAG, "유저 정보 서버 연결 실패 Message : " + t.getMessage());
            }
        });
    }

    //api연결
    public void restApiBuilder() {
        ApplicationController application = ApplicationController.getInstance();
        application.buildNetworkService();
        apiService = ApplicationController.getInstance().getRestApiService();
    }

    public void message(View view){
        Intent message = new Intent(getApplication(),CoinSendBefore_pop.class);
        startActivity(message);
    }

    public void previousActivity(View v){
        onBackPressed();
    }
}