package graduationwork.buskingtown;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telecom.Call;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import graduationwork.buskingtown.api.RestApiService;
import graduationwork.buskingtown.model.Busker;
import okhttp3.ResponseBody;
import retrofit2.Callback;
import retrofit2.Response;

public class ChannelBusker extends AppCompatActivity implements View.OnClickListener {

    private RestApiService apiService;

    //호출될프래그먼트 변수들
    private final int scheduleFRAGMENT = 1;
    private final int reservationTabFRAGMENT = 2;

    //탭바 아이콘 변수들
    private ImageView board, calendar;

    //유저 정보 변수들
    String user_token,user_name;
    String busker_team_name, busker_tag, busker_image,busker_name,team_name;
    int user_id,busker_id,busker_type;

    TextView mainTeamName,subTeamName,tag;
    ImageView busker_main_img;

    android.support.design.widget.FloatingActionButton fab;

    ArrayList<String> teamList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channel_busker);

        restApiBuilder();

        getLocalData();

        fab = (android.support.design.widget.FloatingActionButton) findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent writePost = new Intent(getApplication(), WritePost.class);
                startActivity(writePost);
            }
        });
        //채널설정
        ImageButton moreBtn = (ImageButton) findViewById(R.id.morebtn);
        moreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent channelManagementSetting = new Intent(getApplication(),ChannelManagementSetting.class);
                startActivity(channelManagementSetting);
            }
        });

        //아이콘에 대한 참조 변수
        board = (ImageView) findViewById(R.id.board);
        calendar = (ImageView) findViewById(R.id.calendar);

        //클릭메소드 연결
        board.setOnClickListener(this);
        calendar.setOnClickListener(this);

        //액티비티 시작하자마자 실행 될 프래그먼트
        callFragment(scheduleFRAGMENT);

        //백버튼
        ImageButton back = (ImageButton) findViewById(R.id.backBtn);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //버스커 정보 세팅
        mainTeamName = (TextView) findViewById(R.id.busker_main_team_name);
        subTeamName = (TextView) findViewById(R.id.busker_sub_team_name);
        tag = (TextView) findViewById(R.id.tag);
        busker_main_img = (ImageView) findViewById(R.id.profilebig);
    }

    //버튼 클릭시 프레그먼트 호출하는 메소드
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.board:
                callFragment(scheduleFRAGMENT);
                fab.setVisibility(View.VISIBLE);
                break;

            case R.id.calendar:
                callFragment(reservationTabFRAGMENT);
                fab.setVisibility(View.GONE);
                break;
        }

    }

    //프래그먼트 부르는 메소드
    private void callFragment(int frament_no) {
        // 프래그먼트 사용을 위해
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        switch (frament_no) {
            case 1:
                // '일정페이지' 호출
                ChannelBuskerSchedule scheduleFragment = new  ChannelBuskerSchedule();
                transaction.replace(R.id.fragmentContainer, scheduleFragment);
                transaction.commit();

                //버튼색 활성화
                board.setImageResource(R.drawable.able_board);
                //버튼색 비활성화
                calendar.setImageResource(R.drawable.unable_calendar);
                break;
            case 2:
                // '예약 탭 페이지' 호출
                ChannelBuskerTab reservationtabFragment = new ChannelBuskerTab();
                transaction.replace(R.id.fragmentContainer, reservationtabFragment);
                transaction.commit();

                //버튼색 활성화
                calendar.setImageResource(R.drawable.able_calendar);
                //버튼색 비활성화
                board.setImageResource(R.drawable.unable_board);
                break;

        }
    }

    //버스커 정보 화면에 세팅(이름 및 팔로워등 세팅, 메소드가 길어지면 게시글 같은 건 따로 메소드 정의하세요)
    public void buskerSetting(String busker_team_name, String busker_tag,String busker_image){
        Log.e("버스커 정보", String.valueOf(busker_team_name));
        mainTeamName.setText(busker_team_name);
        subTeamName.setText(busker_team_name);
        tag.setText(busker_tag);
        //이미지 원형 처리
        Picasso.with(getApplication()).load(busker_image).transform(new CircleTransForm()).into(busker_main_img);
    }

    public void buskerTeamCheck(String team_name, int busker_id){
        HashMap<Integer,Integer> typelist = new HashMap<>();
        retrofit2.Call<List<Busker>> busker_list = apiService.all_busker(user_token);
        busker_list.enqueue(new Callback<List<Busker>>() {
            @Override
            public void onResponse(retrofit2.Call<List<Busker>> call, Response<List<Busker>> response) {
                if(response.isSuccessful()){
                    List<Busker> buskers = response.body();
                    if(buskers.size()!=1){
                        for (int i=0; i<buskers.size();i++){
                            Log.e("팀이름",String.valueOf(team_name));
                            //본인 일 경우에도 동일하게 검색이 되므로 이중 if문으로 처리
                            if(team_name.equals(buskers.get(i).getTeam_name())){
                                //팀 멤버가 몇명이 되나 확인
                                String member = buskers.get(i).getTeam_name();
                                teamList.add(member);
                                //아이디와 타입값을 넘겨줌
                                typelist.put(buskers.get(i).getBusker_id(),buskers.get(i).getBusker_type());
                                Log.e("멤버 리스트", String.valueOf(typelist));

                                //멤버 타입 가져오기
                                Integer wrapper_buskertype = (Integer)typelist.get(buskers.get(i).getBusker_id());
                                int busker_type = wrapper_buskertype.intValue();

                                Integer wrapper_busker_leader = (Integer)typelist.get(getKey(typelist,1));
                                int busker_leader_id = wrapper_busker_leader.intValue();

                                //버스커 멤버가 있을때

                                    //팀장일 경우 그대로 보여줌
                                if(busker_type==1){
                                    getBuskerData(user_token,busker_id);
                                } //팀장이 아닐경우 팀장의 화면을 보여줌
                                else {
                                    getBuskerData(user_token,busker_leader_id);
                                    Log.e("리더 아이디",String.valueOf(busker_leader_id));
                                }
                            }
                        }
                    }else {
                        getBuskerData(user_token,busker_id);
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
                    busker_name = busker[0].getBusker_name();
                    busker_type = busker[0].getBusker_type();
                    buskerSetting(busker_team_name,busker_tag,busker_image);
                    saveBuskerInfo(busker_id,busker_type,busker_team_name,busker_name,busker_tag);
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

    public void saveBuskerInfo(int busker_id,int busker_type,String team_name,String busker_name, String tag){
        SharedPreferences pref = getSharedPreferences("BuskerUser", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt("busker_id",busker_id);
        editor.putInt("busker_type",busker_type);
        editor.putString("team_name", team_name);
        editor.putString("busker_name", busker_name);
        editor.putString("tag", tag);
        editor.commit();
    }

    public void getLocalData(){
        SharedPreferences pref = getSharedPreferences("User", Activity.MODE_PRIVATE);
        SharedPreferences busker_pref = getSharedPreferences("BuskerUser", Activity.MODE_PRIVATE);
        user_token = pref.getString("auth_token",null);
        user_name = pref.getString("username",null);
        busker_id = busker_pref.getInt("busker_id",0);
        team_name = busker_pref.getString("team_name",null);

        buskerTeamCheck(team_name,busker_id);
    }

    public void restApiBuilder() {
        ApplicationController application = ApplicationController.getInstance();
        application.buildNetworkService();
        apiService = ApplicationController.getInstance().getRestApiService();
    }

    public int getKey(HashMap<Integer, Integer> m, int value) {
        for(int o: m.keySet()) {
            if(m.get(o).equals(value)) {
                return o; }
        }
        return 0;
    }

}
