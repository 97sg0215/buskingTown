package graduationwork.buskingtown;

import android.app.Activity;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.pusher.pushnotifications.PushNotifications;

public class TabBar extends AppCompatActivity implements View.OnClickListener {

    String user_token,user_name;
    int user_id;

    //호출될프래그먼트 변수들
    private final int rankingFRAGMENT = 1;
    private final int mapFRAGMENT = 2;
    private final int homeFRAGMENT = 3;
    private final int notificationFRAGMENT = 4;
    private final int myPageFRAGMENT = 5;


    //탭바 아이콘 변수들
    private ImageView ranking, map, home, notification, myPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_bar);

        getLocalData();

        PushNotifications.start(getApplicationContext(),"3634159e-f404-4859-9fa0-44d749815dbc");
        PushNotifications.subscribe("hello");

        //아이콘에 대한 참조 변수
        ranking = (ImageView) findViewById(R.id.ranking);
        map = (ImageView) findViewById(R.id.map);
        home = (ImageView) findViewById(R.id.home);
        notification = (ImageView) findViewById(R.id.notification);
        myPage = (ImageView) findViewById(R.id.myPage);

        //클릭메소드 연결
        ranking.setOnClickListener(this);
        map.setOnClickListener(this);
        home.setOnClickListener(this);
        notification.setOnClickListener(this);
        myPage.setOnClickListener(this);

        //액티비티 시작하자마자 실행 될 프래그먼트
        callFragment(homeFRAGMENT);
    }

    //버튼 클릭시 프레그먼트 호출하는 메소드
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ranking :
                callFragment(rankingFRAGMENT);
                break;

            case R.id.map :
                callFragment(mapFRAGMENT);
                break;

            case R.id.home :
                callFragment(homeFRAGMENT);
                break;

            case R.id.notification :
                callFragment(notificationFRAGMENT);
                break;

            case R.id.myPage :
                callFragment(myPageFRAGMENT);
                break;
        }
    }


    //프래그먼트 부르는 메소드
    private void callFragment(int frament_no) {

        // 프래그먼트 사용을 위해
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        switch (frament_no) {
            case 1:
                // '랭킹페이지' 호출
                Ranking rankingFragment = new Ranking();
                transaction.replace(R.id.fragmentContainer, rankingFragment);
                transaction.commit();

                //버튼색 활성화
                ranking.setImageResource(R.drawable.able_star);
                //버튼색 비활성화
                map.setImageResource(R.drawable.enable_map);
                home.setImageResource(R.drawable.enable_home);
                notification.setImageResource(R.drawable.enable_notic);
                myPage.setImageResource(R.drawable.enable_user);

                break;
            case 2:
                // '위치확인페이지' 호출
                Map mapFragment = new Map();
                transaction.replace(R.id.fragmentContainer, mapFragment);
                transaction.commit();

                //버튼색 활성화
                map.setImageResource(R.drawable.able_map);
                //버튼색 비활성화
                ranking.setImageResource(R.drawable.enable_star);
                home.setImageResource(R.drawable.enable_home);
                notification.setImageResource(R.drawable.enable_notic);
                myPage.setImageResource(R.drawable.enable_user);

                break;
            case 3:
                // '홈' 호출
                Home homeFragment = new Home();
                transaction.replace(R.id.fragmentContainer, homeFragment);
                transaction.commit();

                //버튼색 활성화
                home.setImageResource(R.drawable.able_home);
                //버튼색 비활성화
                ranking.setImageResource(R.drawable.enable_star);
                map.setImageResource(R.drawable.enable_map);
                notification.setImageResource(R.drawable.enable_notic);
                myPage.setImageResource(R.drawable.enable_user);

                break;
            case 4:
                // '알림페이지' 호출
                Notification notificationFragment = new Notification();
                transaction.replace(R.id.fragmentContainer, notificationFragment);
                transaction.commit();

                //버튼색 활성화
                notification.setImageResource(R.drawable.able_notic);
                //버튼색 비활성화
                ranking.setImageResource(R.drawable.enable_star);
                map.setImageResource(R.drawable.enable_map);
                home.setImageResource(R.drawable.enable_home);
                myPage.setImageResource(R.drawable.enable_user);

                break;
            case 5:
                // '마이페이지' 호출
                Mypage myPageFragment = new Mypage();
                transaction.replace(R.id.fragmentContainer, myPageFragment);
                transaction.commit();

                //버튼색 활성화
                myPage.setImageResource(R.drawable.able_user);
                //버튼색 비활성화
                ranking.setImageResource(R.drawable.enable_star);
                map.setImageResource(R.drawable.enable_map);
                home.setImageResource(R.drawable.enable_home);
                notification.setImageResource(R.drawable.enable_notic);

                break;
        }
    }

    public void getLocalData(){
        SharedPreferences pref = getSharedPreferences("User", Activity.MODE_PRIVATE);
        user_token = pref.getString("auth_token",null);
        user_name = pref.getString("username",null);
        user_id = pref.getInt("user_id",0);
    }
}
