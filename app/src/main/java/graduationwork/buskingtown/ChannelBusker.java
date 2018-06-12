package graduationwork.buskingtown;

import android.app.Activity;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import graduationwork.buskingtown.api.RestApiService;
import graduationwork.buskingtown.model.Busker;
import graduationwork.buskingtown.model.UserDetail;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChannelBusker extends AppCompatActivity implements View.OnClickListener {

    private RestApiService apiService;

    //호출될프래그먼트 변수들
    private final int scheduleFRAGMENT = 1;
    private final int reservationTabFRAGMENT = 2;

    //탭바 아이콘 변수들
    private ImageView board, calendar;

    String user_token;
    int user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channel_busker);

        restApiBuilder();
        getLocalData();

        ImageButton backBtn = (ImageButton) findViewById(R.id.backBtn);

        //아이콘에 대한 참조 변수
        board = (ImageView) findViewById(R.id.board);
        calendar = (ImageView) findViewById(R.id.calendar);

        //클릭메소드 연결
        board.setOnClickListener(this);
        calendar.setOnClickListener(this);

        //액티비티 시작하자마자 실행 될 프래그먼트
        callFragment(scheduleFRAGMENT);

        getUserDetail(user_token,user_id);

    }
    //버튼 클릭시 프레그먼트 호출하는 메소드
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.board:
                callFragment(scheduleFRAGMENT);
                break;

            case R.id.calendar:
                callFragment(reservationTabFRAGMENT);
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

    //유저 정보를 저장하여 다른 액티비티에서 불러오기 위함
    public void saveBuskerUserInfo(String token, int user, String busker_team_name, String tag){
        SharedPreferences pref = getSharedPreferences("Busker", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("auth_token",token);
        editor.putInt("user_id",user);
       // editor.putString("username",username);
       // editor.putString("user_phone",user_phone);
        editor.putString("busker_team_name", busker_team_name);
        editor.putString("busker_tag", tag);
        editor.commit();
    }

    public void getUserDetail(String token,int id){
        final UserDetail[] userDetail = {new UserDetail()};
        Call<UserDetail> userDetailCall = apiService.getUserDetail(token,id);
        userDetailCall.enqueue(new Callback<UserDetail>() {
            @Override
            public void onResponse(Call<UserDetail> call, Response<UserDetail> response) {
                userDetail[0] = response.body();
                String username = userDetail[0].getUsername();
                String busker_team_name = userDetail[0].getBusker().getTeam_name();
                String busker_tag = userDetail[0].getBusker().getBusker_tag();
                if(response.isSuccessful()){
                    Log.e("유저 아이디",String.valueOf(id));
                    Log.e("유저정보가져오기:", "성공");
                   // saveBuskerUserInfo(token,id,busker_team_name,busker_tag);
                    buskerInfoSetting(busker_team_name,busker_tag);
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
            public void onFailure(Call<UserDetail> call, Throwable t) {
                Log.i(ApplicationController.TAG, "유저 정보 서버 연결 실패 Message : " + t.getMessage());
            }
        });
    }

    public void buskerInfoSetting(String team_name, String b_tag){
        TextView main_team_name = (TextView) findViewById(R.id.busker_main_team_name);
        TextView sub_team_name = (TextView) findViewById(R.id.busker_sub_team_name);
        TextView tag = (TextView) findViewById(R.id.tag);

        main_team_name.setText(team_name);
        sub_team_name.setText(team_name);
        tag.setText(b_tag);
    }

    public void getLocalData(){
        SharedPreferences pref = getSharedPreferences("User", Activity.MODE_PRIVATE);
        user_token = pref.getString("auth_token",null);
        user_id = pref.getInt("user_id",0);
    }

    public void restApiBuilder() {
        ApplicationController application = ApplicationController.getInstance();
        application.buildNetworkService();
        apiService = ApplicationController.getInstance().getRestApiService();
    }
}
