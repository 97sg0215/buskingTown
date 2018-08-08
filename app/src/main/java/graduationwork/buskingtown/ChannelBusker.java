package graduationwork.buskingtown;

import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

public class ChannelBusker extends AppCompatActivity implements View.OnClickListener {

    //호출될프래그먼트 변수들
    private final int scheduleFRAGMENT = 1;
    private final int reservationTabFRAGMENT = 2;

    //탭바 아이콘 변수들
    private ImageView board, calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channel_busker);

        ImageButton backBtn = (ImageButton) findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { ChannelBusker.super.onBackPressed(); }
        });

        //아이콘에 대한 참조 변수
        board = (ImageView) findViewById(R.id.board);
        calendar = (ImageView) findViewById(R.id.calendar);

        //클릭메소드 연결
        board.setOnClickListener(this);
        calendar.setOnClickListener(this);

        //액티비티 시작하자마자 실행 될 프래그먼트
        callFragment(scheduleFRAGMENT);

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
    public void previousActivity(View v){
        onBackPressed();
    }

}
