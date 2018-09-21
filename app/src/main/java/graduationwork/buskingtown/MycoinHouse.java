package graduationwork.buskingtown;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.ViewGroup;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.zip.Inflater;

import com.tsongkha.spinnerdatepicker.DatePicker;
import com.tsongkha.spinnerdatepicker.DatePickerDialog;
import com.tsongkha.spinnerdatepicker.SpinnerDatePickerDialogBuilder;

public class MycoinHouse extends AppCompatActivity implements View.OnClickListener{

    Button oneMonth, threeMonth, sixMonth, oneYearz, startDate, endDate;
    int coinStartYear, coinStartMonth, coinStartDay , coinEndYear, coinEndMonth, coinEndDay;
    SimpleDateFormat simpleDateFormat;

    int test__coin=3;
    private FrameLayout coinContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mycoin_house);

       for (int listCount=0; listCount<test__coin; listCount++) {
           LinearLayout coinBox = (LinearLayout)findViewById(R.id.coinContainer);

            TextView transactionText = (TextView)findViewById(R.id.transactionText);
            if (test__coin > 1 ){
                transactionText.setVisibility(View.GONE);
                View coinlist = getLayoutInflater().inflate(R.layout.transactionlist,coinBox,false);
               if(coinlist.getParent()!= null)
                    ((ViewGroup)coinlist.getParent()).removeView(coinlist);
                coinBox.addView(coinlist);
            }
       }


        //버튼 참조변수
        oneMonth = (Button) findViewById(R.id.oneMonth);
        threeMonth = (Button) findViewById(R.id.threeMonth);
        sixMonth = (Button) findViewById(R.id.sixMonth);
        oneYearz = (Button) findViewById(R.id.oneYearz);

        //클릭메소드 연결
        oneMonth.setOnClickListener(this);
        threeMonth.setOnClickListener(this);
        sixMonth.setOnClickListener(this);
        oneYearz.setOnClickListener(this);

        //아래는 뒤로가기 버튼 클릭시 뒤로가는거임
        ImageButton backBtn = (ImageButton) findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { MycoinHouse.super.onBackPressed(); }
        });


        //달력 객체 불러옴
        final GregorianCalendar startCalendar = new GregorianCalendar();

        coinStartYear = startCalendar.get(Calendar.YEAR);
        coinStartMonth = startCalendar.get(Calendar.MONTH);
        coinStartDay = startCalendar.get(Calendar.DAY_OF_MONTH);

        final GregorianCalendar endCalendar = new GregorianCalendar();
        coinEndYear = endCalendar.get(Calendar.YEAR);
        coinEndMonth = endCalendar.get(Calendar.MONTH);
        coinEndDay = endCalendar.get(Calendar.DAY_OF_MONTH);

        //날짜형태
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);

        //데이트 피커
        startDate = (Button) findViewById(R.id.coinStartDate);
        endDate = (Button) findViewById(R.id.coinEndDate);

        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatePickerDialog.OnDateSetListener dateStartPicker = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        startDate.setText(year + "년 " + (monthOfYear + 1) + "월 " + dayOfMonth + "일");
                    }
                };


                new SpinnerDatePickerDialogBuilder()
                        .callback(dateStartPicker)
                        .context(MycoinHouse.this)
                        .spinnerTheme(R.style.DatePickerSpinner)
                        .showTitle(true)
                        .defaultDate(coinStartYear,coinStartMonth,coinStartDay)
                        .build()
                        .show();
            }
        });

        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog.OnDateSetListener dateEndPicker = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        endDate.setText(year + "년 " + (monthOfYear + 1) + "월 " + dayOfMonth + "일");
                    }
                };

                new SpinnerDatePickerDialogBuilder()
                        .callback(dateEndPicker)
                        .context(MycoinHouse.this)
                        .spinnerTheme(R.style.DatePickerSpinner)
                        .showTitle(true)
                        .defaultDate(coinEndYear,coinEndMonth,coinEndDay)
                        .build()
                        .show();
            }
        });
    }

    public void previousActivity(View v){
        onBackPressed();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.oneMonth :
                //1개월 버튼색 활성화
                oneMonth.setBackground(getDrawable(R.drawable.able_btn));
                oneMonth.setTextColor(Color.parseColor("#ffffff"));
                //나머지 버튼색 비활성화
                threeMonth.setBackground(getDrawable(R.drawable.date_rounded));
                threeMonth.setTextColor(getResources().getColor(R.color.mainPurple));
                sixMonth.setBackground(getDrawable(R.drawable.date_rounded));
                sixMonth.setTextColor(getResources().getColor(R.color.mainPurple));
                oneYearz.setBackground(getDrawable(R.drawable.date_rounded));
                oneYearz.setTextColor(getResources().getColor(R.color.mainPurple));
                break;

            case R.id.threeMonth :
                //3개월 버튼색 활성화
                threeMonth.setBackground(getDrawable(R.drawable.able_btn));
                threeMonth.setTextColor(Color.parseColor("#ffffff"));
                //나머지 버튼색 비활성화
                oneMonth.setBackground(getDrawable(R.drawable.date_rounded));
                oneMonth.setTextColor(getResources().getColor(R.color.mainPurple));
                sixMonth.setBackground(getDrawable(R.drawable.date_rounded));
                sixMonth.setTextColor(getResources().getColor(R.color.mainPurple));
                oneYearz.setBackground(getDrawable(R.drawable.date_rounded));
                oneYearz.setTextColor(getResources().getColor(R.color.mainPurple));
                break;

            case R.id.sixMonth :
                //6개월 버튼색 활성화
                sixMonth.setBackground(getDrawable(R.drawable.able_btn));
                sixMonth.setTextColor(Color.parseColor("#ffffff"));
                //나머지 버튼색 비활성화
                oneMonth.setBackground(getDrawable(R.drawable.date_rounded));
                oneMonth.setTextColor(getResources().getColor(R.color.mainPurple));
                threeMonth.setBackground(getDrawable(R.drawable.date_rounded));
                threeMonth.setTextColor(getResources().getColor(R.color.mainPurple));
                oneYearz.setBackground(getDrawable(R.drawable.date_rounded));
                oneYearz.setTextColor(getResources().getColor(R.color.mainPurple));
                break;

            case R.id.oneYearz :
                //1년 버튼색 활성화
                oneYearz.setBackground(getDrawable(R.drawable.able_btn));
                oneYearz.setTextColor(Color.parseColor("#ffffff"));
                //나머지 버튼색 비활성화
                oneMonth.setBackground(getDrawable(R.drawable.date_rounded));
                oneMonth.setTextColor(getResources().getColor(R.color.mainPurple));
                threeMonth.setBackground(getDrawable(R.drawable.date_rounded));
                threeMonth.setTextColor(getResources().getColor(R.color.mainPurple));
                sixMonth.setBackground(getDrawable(R.drawable.date_rounded));
                sixMonth.setTextColor(getResources().getColor(R.color.mainPurple));
                break;
        }
    }

}
