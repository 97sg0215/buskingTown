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
    String busker_team_name, busker_tag, busker_image;
    int user_id,busker_id;

    TextView mainTeamName,subTeamName,tag;
    ImageView busker_main_img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channel_busker);

        restApiBuilder();

        getLocalData();

        getBuskerData(user_token,busker_id);

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

    //버스커 정보 화면에 세팅(이름 및 팔로워등 세팅, 메소드가 길어지면 게시글 같은 건 따로 메소드 정의하세요)
    public void buskerSetting(String busker_team_name, String busker_tag,String busker_image){
        Log.e("버스커 정보", String.valueOf(busker_team_name));
        mainTeamName.setText(busker_team_name);
        subTeamName.setText(busker_team_name);
        tag.setText(busker_tag);
        //이미지 원형 처리
        Picasso.with(getApplication()).load(busker_image).transform(new CircleTransForm()).into(busker_main_img);
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

    public void getLocalData(){
        SharedPreferences pref = getSharedPreferences("User", Activity.MODE_PRIVATE);
        SharedPreferences busker_pref = getSharedPreferences("BuskerUser", Activity.MODE_PRIVATE);
        user_token = pref.getString("auth_token",null);
        user_name = pref.getString("username",null);
        busker_id = busker_pref.getInt("busker_id",0);
    }

    public void restApiBuilder() {
        ApplicationController application = ApplicationController.getInstance();
        application.buildNetworkService();
        apiService = ApplicationController.getInstance().getRestApiService();
    }
}
