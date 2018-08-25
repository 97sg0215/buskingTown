package graduationwork.buskingtown;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import graduationwork.buskingtown.api.RestApiService;
import graduationwork.buskingtown.model.Busker;
import graduationwork.buskingtown.model.Connection;
import graduationwork.buskingtown.model.Profile;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChannelUser extends AppCompatActivity {

    int test_schedule=5;
    int test_concert=5;

    int busker_id, user_id;
    String user_token, user_name, busker_team_name, busker_tag;

    //버스커 정보 세팅
    TextView mainTeamName, subTeamName, tag;

    Button following_btn;

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

        following_btn = (Button) findViewById(R.id.fan_on);

        //팔로우 인지 아닌지 체크 우선
        final Connection[] getConnection = {new Connection()};
        Call<Connection> get_following = apiService.get_followings(user_token);
        get_following.enqueue(new Callback<Connection>() {
            @Override
            public void onResponse(Call<Connection> call, Response<Connection> response) {
                if(response.isSuccessful()){
                    getConnection[0] = response.body();
                }
                else {

                }
            }

            @Override
            public void onFailure(Call<Connection> call, Throwable t) {
                Log.i(ApplicationController.TAG, "유저 정보 서버 연결 실패 Message : " + t.getMessage());
            }
        });


        //아닐 경우 팔로우 버튼 누르면 팔로우
        following_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Connection connection = new Connection();
                connection.setCreator(user_id);
                connection.setFollowing(busker_id);
                Call<Connection> set_following = apiService.following(user_token,connection);
                set_following.enqueue(new Callback<Connection>() {
                    @Override
                    public void onResponse(Call<Connection> call, Response<Connection> response) {
                        if(response.isSuccessful()){
                            Log.e("팔로워", String.valueOf(user_id));
                            Log.e("버스커",String.valueOf(busker_id));
                            following_btn.setBackground(getDrawable(R.drawable.disable_btn));
                            following_btn.setText("팬이에요");
                        } else{
                            //에러 상태 보려고 해둔 코드
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
                    public void onFailure(Call<Connection> call, Throwable t) {
                        Log.i(ApplicationController.TAG, "유저 정보 서버 연결 실패 Message : " + t.getMessage());
                    }
                });
            }
        });

        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        for (int scheduleCount=0; scheduleCount<test_schedule; scheduleCount++) {
            addSchedule(inflater);
            Log.e("for문 횟수", String.valueOf(scheduleCount));
        }
        for (int scheduleCount=0; scheduleCount<test_concert; scheduleCount++) {
            addConcert(inflater);
            Log.e("for문 횟수", String.valueOf(scheduleCount));
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
    public void buskerSetting(String busker_team_name, String busker_tag){
        Log.e("버스커 정보", String.valueOf(busker_team_name));
        mainTeamName.setText(busker_team_name);
        subTeamName.setText(busker_team_name);
        tag.setText(busker_tag);
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
                    buskerSetting(busker_team_name,busker_tag);
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

    public void previousActivity(View v){
        onBackPressed();
    }
}