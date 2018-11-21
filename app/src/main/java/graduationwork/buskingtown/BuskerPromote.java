package graduationwork.buskingtown;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.ArrayList;

public class BuskerPromote extends AppCompatActivity implements View.OnClickListener{

    //호출될프래그먼트 변수들
    private final int mainbannerFRAGMENT = 1;
    private final int recommendFRAGMENT = 2;

    //탭바 아이콘 변수들
    private TextView mainbanner,recommend;
    private View mainbannerBar,recommendBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_busker_promote);

        //아이콘에 대한 참조 변수
        mainbanner = (TextView) findViewById(R.id.mainbanner);
        recommend = (TextView) findViewById(R.id.recommend);

        //클릭메소드 연결
        mainbanner.setOnClickListener(this);
        recommend.setOnClickListener(this);

        //액티비티 시작하자마자 실행 될 프래그먼트
        callFragment(mainbannerFRAGMENT);

        //백버튼
        ImageButton back = (ImageButton) findViewById(R.id.backBtn);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mainbanner:
                callFragment(mainbannerFRAGMENT);
                break;

            case R.id.recommend:
                callFragment(recommendFRAGMENT);
                break;
        }

    }

    //프래그먼트 부르는 메소드
    private void callFragment(int frament_no) {
        // 프래그먼트 사용을 위해
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        switch (frament_no) {
            case 1:
                // '메인배너홍보' 호출
                PromoteMainbanner mainbannerFRAGMENT = new  PromoteMainbanner();
                transaction.replace(R.id.fragmentContainer, mainbannerFRAGMENT);
                transaction.commit();

                mainbanner.setTextColor(getResources().getColor(R.color.mainYellow));
                recommend.setTextColor(getResources().getColor(R.color.fontBlack));
                break;



            case 2:
                // '추천순위 노출' 호출
                PromoteRecommend recommendFRAGMENT = new PromoteRecommend();
                transaction.replace(R.id.fragmentContainer, recommendFRAGMENT);
                transaction.commit();

                recommend.setTextColor(getResources().getColor(R.color.mainYellow));
                mainbanner.setTextColor(getResources().getColor(R.color.fontBlack));
                break;

        }
    }
}

