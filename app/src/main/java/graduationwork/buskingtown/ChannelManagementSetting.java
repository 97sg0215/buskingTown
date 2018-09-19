package graduationwork.buskingtown;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telecom.Call;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import graduationwork.buskingtown.api.RestApiService;
import graduationwork.buskingtown.model.Busker;
import okhttp3.ResponseBody;
import retrofit2.Callback;
import retrofit2.Response;

public class ChannelManagementSetting extends AppCompatActivity {

    String user_token,user_name,user_image,team_name;
    int user_id;

    private RelativeLayout buskerTeamLayout,roadReservation,practiceRoomReservation,concertReservation,statsLayout,coinLayout;

    RestApiService apiService;

    ArrayList<String> teamList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channel_management_setting);

        getLocalData();

        restApiBuilder();

        ImageButton backBtn = (ImageButton) findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { ChannelManagementSetting.super.onBackPressed(); }
        });

        //팀원관리
        buskerTeamLayout = (RelativeLayout) findViewById(R.id.buskerTeamLayout);
        buskerTeamLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                retrofit2.Call<List<Busker>> busker_list = apiService.all_busker(user_token);
                busker_list.enqueue(new Callback<List<Busker>>() {
                    @Override
                    public void onResponse(retrofit2.Call<List<Busker>> call, Response<List<Busker>> response) {
                        if(response.isSuccessful()){
                            List<Busker> buskers = response.body();
                            for (int i=0; i<buskers.size();i++){
                                //본인 일 경우에도 동일하게 검색이 되므로 이중 if문으로 처리
                                if(team_name.equals(buskers.get(i).getTeam_name())){
                                    String member = buskers.get(i).getTeam_name();
                                    teamList.add(member);
                                    //버스커 멤버가 있을때
                                    if (teamList.size()>=2){
                                        Intent MemberManagement = new Intent(getApplication(),TeamMember.class);
                                        MemberManagement.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(MemberManagement);
                                    } //멤버가 없을때
                                    else {
                                        Intent NoneMemberManagement = new Intent(getApplication(),MemberManagement.class);
                                        startActivity(NoneMemberManagement);
                                    }
                                }
                            }
                        }else {
                            //에러 상태 보려고 해둔 코드
                            int StatusCode = response.code();
                            String s = response.message();
                            ResponseBody d = response.errorBody();
                            Log.i(ApplicationController.TAG, "홈 상태 Code : " + StatusCode);
                            Log.e("메세지", s);
                            Log.e("리스폰스에러바디", String.valueOf(d));
                            Log.e("리스폰스바디", String.valueOf(response.body()));
                        }
                    }

                    @Override
                    public void onFailure(retrofit2.Call<List<Busker>> call, Throwable t) {
                        Log.i(ApplicationController.TAG, "버스커 리스트 서버 연결 실패 Message : " + t.getMessage());
                    }
                });
            }
        });

        //거리공연,연습실,콘서트
        roadReservation = (RelativeLayout) findViewById(R.id.roadReservation);
        roadReservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent road = new Intent(getApplication(),BuskingOpen.class);
                startActivity(road);
            }
        });

        practiceRoomReservation = (RelativeLayout) findViewById(R.id.practiceReservation);
        practiceRoomReservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent road = new Intent(getApplication(),PracticeRoomReservation.class);
                startActivity(road);
            }
        });


        concertReservation = (RelativeLayout) findViewById(R.id.concertReservation);
        concertReservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent concert= new Intent(getApplication(),OpenConcert.class);
                startActivity(concert);
            }
        });

        //통계보기
        statsLayout = (RelativeLayout) findViewById(R.id.statsLayout);
        statsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Statistics = new Intent(getApplication(),Statistics.class);
                startActivity(Statistics);
            }
        });

        //코인관리
        coinLayout = (RelativeLayout) findViewById(R.id.coinLayout);
        coinLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent CoinManagement = new Intent(getApplication(),CoinManagement.class);
                startActivity(CoinManagement);
            }
        });

        //채널삭제

    }

    //user 정보 불러오기
    public void getLocalData(){
        SharedPreferences pref = getSharedPreferences("User", Activity.MODE_PRIVATE);
        SharedPreferences busker_pref = getSharedPreferences("BuskerUser", Activity.MODE_PRIVATE);
        user_token = pref.getString("auth_token",null);
        user_name = pref.getString("username",null);
        user_id = pref.getInt("user_id",0);

        team_name = busker_pref.getString("team_name",null);
    }

    public void restApiBuilder() {
        ApplicationController application = ApplicationController.getInstance();
        application.buildNetworkService();
        apiService = ApplicationController.getInstance().getRestApiService();
    }

    public void previousActivity(View v){
        onBackPressed();
    }
}
