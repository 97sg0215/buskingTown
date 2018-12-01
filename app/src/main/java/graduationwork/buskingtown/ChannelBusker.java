package graduationwork.buskingtown;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import java.util.List;
import graduationwork.buskingtown.api.RestApiService;
import graduationwork.buskingtown.model.Busker;
import graduationwork.buskingtown.model.Connections;
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
    String user_token, user_name;
    String team_name;
    int user_id, busker_id;
    TextView mainTeamName, subTeamName, tag;
    ImageView busker_main_img;
    TextView smileCount, coinAmount;
    android.support.design.widget.FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channel_busker);

        restApiBuilder();

        getLocalData();

        smileCount = (TextView) findViewById(R.id.smileCount);
        coinAmount = (TextView) findViewById(R.id.coinAmount);

        RelativeLayout dotLayout = (RelativeLayout) findViewById(R.id.dotLayout);
        dotLayout.setOnClickListener(new RelativeLayout.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ChannelManagementSetting.class);
                startActivity(intent);
            }
        });

        //로딩코드
        CheckTypesTask task = new CheckTypesTask();
        task.execute();

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
            public void onClick(View v) {
                Intent channelManagementSetting = new Intent(getApplication(), ChannelManagementSetting.class);
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
                ChannelBuskerSchedule scheduleFragment = new ChannelBuskerSchedule();
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

    public void buskerTeamCheck(String team_name) {
        retrofit2.Call<List<Busker>> buskerCall = apiService.buskerTeam(user_token, team_name);
        buskerCall.enqueue(new Callback<List<Busker>>() {
            @Override
            public void onResponse(retrofit2.Call<List<Busker>> call, Response<List<Busker>> response) {
                if (response.isSuccessful()) {
                    busker_id = response.body().get(0).getBusker_id();
                    String busker_team_name = response.body().get(0).getTeam_name();
                    String busker_tag = response.body().get(0).getBusker_tag();
                    mainTeamName.setText(busker_team_name);
                    subTeamName.setText(busker_team_name);
                    tag.setText(busker_tag);
                    coinAmount.setText(String.valueOf(response.body().get(0).getReceived_coin()) + "개");
                    //이미지 원형 처리
                    Picasso.with(getApplication()).load(response.body().get(0).getBusker_image()).transform(new CircleTransForm()).into(busker_main_img);
                    saveBuskerInfo(busker_id, busker_team_name, busker_tag);

                    //정보 세팅
                    retrofit2.Call<List<Connections>> call2 = apiService.get_followers(user_token, busker_id);
                    call2.enqueue(new Callback<List<Connections>>() {
                        @Override
                        public void onResponse(retrofit2.Call<List<Connections>> call, Response<List<Connections>> response) {
                            if (response.isSuccessful()) {
                                List<Connections> connections = response.body();
                                if (connections.size() != 0) {
                                    smileCount.setText(String.valueOf(connections.size()) + "명");
                                }
                            }
                        }

                        @Override
                        public void onFailure(retrofit2.Call<List<Connections>> call, Throwable t) {

                        }
                    });

                } else {
                    int StatusCode = response.code();
                    Log.i(ApplicationController.TAG, "홈 상태 Code : " + StatusCode);
                }
            }

            @Override
            public void onFailure(retrofit2.Call<List<Busker>> call, Throwable t) {
                Log.i(ApplicationController.TAG, "버스커 정보 서버 연결 실패 Message : " + t.getMessage());
            }
        });
    }


    public void saveBuskerInfo(int busker_id, String team_name, String tag) {
        SharedPreferences pref = getSharedPreferences("BuskerUser", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt("busker_id", busker_id);
        editor.putString("team_name", team_name);
        editor.putString("tag", tag);
        editor.commit();
    }

    public void getLocalData() {
        SharedPreferences pref = getSharedPreferences("User", Activity.MODE_PRIVATE);
        SharedPreferences busker_pref = getSharedPreferences("BuskerUser", Activity.MODE_PRIVATE);
        user_token = pref.getString("auth_token", null);
        user_name = pref.getString("username", null);
        team_name = busker_pref.getString("team_name", null);

        buskerTeamCheck(team_name);
    }

    public void restApiBuilder() {
        ApplicationController application = ApplicationController.getInstance();
        application.buildNetworkService();
        apiService = ApplicationController.getInstance().getRestApiService();
    }

    //로딩해주는 코드
    private class CheckTypesTask extends AsyncTask<Void, Void, Void> {

        ProgressDialog asyncDialog = new ProgressDialog(
                ChannelBusker.this);

        @Override
        protected void onPreExecute() {
            asyncDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            asyncDialog.setMessage("로딩중입니다..");

            // show dialog
            asyncDialog.show();
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            try {
                for (int i = 0; i < 5; i++) {
                    //asyncDialog.setProgress(i * 30);
                    Thread.sleep(300);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            asyncDialog.dismiss();
            super.onPostExecute(result);
        }
    }

}
