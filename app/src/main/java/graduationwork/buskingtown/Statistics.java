package graduationwork.buskingtown;

import android.graphics.Color;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

public class Statistics extends AppCompatActivity {

    Button oneMonth, threeMonth, sixMonth, oneYearz, coinSD, coinED;
    int coinStartYear, coinStartMonth, coinStartDay, coinEndYear, coinEndMonth, coinEndDay;
    SimpleDateFormat simpleDateFormat;

    private final int oneFRAGMENT = 1;
    private final int twoFRAGMENT = 2;
    private final int threeFRAGMENT = 3;

    String put_start_date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        //버튼 참조변수
        oneMonth = (Button) findViewById(R.id.oneMonth);
        threeMonth = (Button) findViewById(R.id.threeMonth);
        sixMonth = (Button) findViewById(R.id.sixMonth);
        oneYearz = (Button) findViewById(R.id.oneYearz);

        //달력 객체 불러옴
        final GregorianCalendar startCalendar = new GregorianCalendar();

        coinStartYear = startCalendar.get(Calendar.YEAR);
        coinStartMonth = startCalendar.get(Calendar.MONTH);
        coinStartDay = startCalendar.get(Calendar.DAY_OF_MONTH);
        put_start_date = coinStartYear + "-" + coinStartMonth + "-" + coinStartDay;


        final GregorianCalendar endCalendar = new GregorianCalendar();
        coinEndYear = endCalendar.get(Calendar.YEAR);
        coinEndMonth = endCalendar.get(Calendar.MONTH);
        coinEndDay = endCalendar.get(Calendar.DAY_OF_MONTH);

        //날짜형태
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);

        //데이트 피커
        coinSD = (Button) findViewById(R.id.coinSD);
        coinSD.setText(String.format("%2d년 %02d월 %02d일", coinStartYear, coinStartMonth, coinStartDay));

        coinED = (Button) findViewById(R.id.coinED);
        coinED.setText(String.format("%2d년 %02d월 %02d일", coinEndYear, coinEndMonth + 1, coinEndDay));

        //아래는 뒤로가기 버튼 클릭시 뒤로가는거임
        ImageButton backBtn = (ImageButton) findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Statistics.super.onBackPressed();
            }
        });

        Spinner spinner = (Spinner) findViewById(R.id.spinner_drop);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.statistics_menu, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                switch (position) {
                    case 0:
                        callFragment(twoFRAGMENT, put_start_date, coinEndYear + "-" + (coinEndMonth + 1) + "-" + coinEndDay);
                        break;
                    case 1:
                        callFragment(threeFRAGMENT, put_start_date, coinEndYear + "-" + (coinEndMonth + 1) + "-" + coinEndDay);
                        break;
                    case 2:
                        Toast.makeText(parent.getContext(), "Spinner item 4!", Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void previousActivity(View v) {
        onBackPressed();
    }

    public void onClickDate(int fragmentNum) {
        oneMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oneMonth.setBackground(getDrawable(R.drawable.able_btn));
                oneMonth.setTextColor(Color.parseColor("#ffffff"));
                coinSD.setText(String.format("%2d년 %02d월 %02d일", coinStartYear, coinStartMonth, coinStartDay));
                put_start_date = coinStartYear + "-" + coinStartMonth + "-" + coinStartDay;
                callFragment(fragmentNum, put_start_date, coinEndYear + "-" + (coinEndMonth + 1) + "-" + coinEndDay);
                //나머지 버튼색 비활성화
                threeMonth.setBackground(getDrawable(R.drawable.date_rounded));
                threeMonth.setTextColor(getResources().getColor(R.color.mainPurple));
                sixMonth.setBackground(getDrawable(R.drawable.date_rounded));
                sixMonth.setTextColor(getResources().getColor(R.color.mainPurple));
                oneYearz.setBackground(getDrawable(R.drawable.date_rounded));
                oneYearz.setTextColor(getResources().getColor(R.color.mainPurple));
            }
        });

        threeMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                threeMonth.setBackground(getDrawable(R.drawable.able_btn));
                threeMonth.setTextColor(Color.parseColor("#ffffff"));
                coinSD.setText(String.format("%2d년 %02d월 %02d일", coinStartYear, coinStartMonth - 2, coinStartDay));
                put_start_date = coinStartYear + "-" + (coinStartMonth - 2) + "-" + coinStartDay;
                callFragment(fragmentNum, put_start_date, coinEndYear + "-" + (coinEndMonth + 1) + "-" + coinEndDay);
                //나머지 버튼색 비활성화
                oneMonth.setBackground(getDrawable(R.drawable.date_rounded));
                oneMonth.setTextColor(getResources().getColor(R.color.mainPurple));
                sixMonth.setBackground(getDrawable(R.drawable.date_rounded));
                sixMonth.setTextColor(getResources().getColor(R.color.mainPurple));
                oneYearz.setBackground(getDrawable(R.drawable.date_rounded));
                oneYearz.setTextColor(getResources().getColor(R.color.mainPurple));
            }
        });

        sixMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sixMonth.setBackground(getDrawable(R.drawable.able_btn));
                sixMonth.setTextColor(Color.parseColor("#ffffff"));
                coinSD.setText(String.format("%2d년 %02d월 %02d일", coinStartYear, coinStartMonth - 5, coinStartDay));
                put_start_date = coinStartYear + "-" + (coinStartMonth - 5) + "-" + coinStartDay;
                callFragment(fragmentNum, put_start_date, coinEndYear + "-" + (coinEndMonth + 1) + "-" + coinEndDay);
                //나머지 버튼색 비활성화
                oneMonth.setBackground(getDrawable(R.drawable.date_rounded));
                oneMonth.setTextColor(getResources().getColor(R.color.mainPurple));
                threeMonth.setBackground(getDrawable(R.drawable.date_rounded));
                threeMonth.setTextColor(getResources().getColor(R.color.mainPurple));
                oneYearz.setBackground(getDrawable(R.drawable.date_rounded));
                oneYearz.setTextColor(getResources().getColor(R.color.mainPurple));
            }
        });

        oneYearz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oneYearz.setBackground(getDrawable(R.drawable.able_btn));
                oneYearz.setTextColor(Color.parseColor("#ffffff"));
                coinSD.setText(String.format("%2d년 %02d월 %02d일", coinStartYear - 1, coinStartMonth + 1, coinStartDay));
                put_start_date = (coinStartYear - 1) + "-" + (coinStartMonth + 1) + "-" + coinStartDay;
                callFragment(fragmentNum, put_start_date, coinEndYear + "-" + (coinEndMonth + 1) + "-" + coinEndDay);
                //나머지 버튼색 비활성화
                oneMonth.setBackground(getDrawable(R.drawable.date_rounded));
                oneMonth.setTextColor(getResources().getColor(R.color.mainPurple));
                threeMonth.setBackground(getDrawable(R.drawable.date_rounded));
                threeMonth.setTextColor(getResources().getColor(R.color.mainPurple));
                sixMonth.setBackground(getDrawable(R.drawable.date_rounded));
                sixMonth.setTextColor(getResources().getColor(R.color.mainPurple));
            }
        });
    }


    private void callFragment(int frament_no, String start_date, String end_date) {
        // 프래그먼트 사용을 위해
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        switch (frament_no) {
            case 1:
                // '그래프' 호출
                Graph graphFragment = new Graph();
                transaction.replace(R.id.fragmentContainer, graphFragment);
                transaction.commit();

                break;

            case 2:
                onClickDate(2);
                //팔로워 통계
                FollowGraph followGraph = new FollowGraph();

                Bundle bundle = new Bundle();
                bundle.putString("start_date", start_date);
                bundle.putString("end_date", end_date);
                followGraph.setArguments(bundle);

                transaction.replace(R.id.fragmentContainer, followGraph);
                transaction.commit();

                break;

            case 3:
                onClickDate(3);
                //코인 통계
                CoinGraph coinGraph = new CoinGraph();

                Bundle bundle2 = new Bundle();
                bundle2.putString("start_date", start_date);
                bundle2.putString("end_date", end_date);
                coinGraph.setArguments(bundle2);

                transaction.replace(R.id.fragmentContainer, coinGraph);
                transaction.commit();

                break;

        }
    }


}
