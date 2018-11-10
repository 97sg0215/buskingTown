package graduationwork.buskingtown;

import android.app.AlertDialog;
import android.app.DatePickerDialog;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.tsongkha.spinnerdatepicker.SpinnerDatePickerDialogBuilder;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;



public class BuskingOpen extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, com.tsongkha.spinnerdatepicker.DatePickerDialog.OnDateSetListener {
    String concertDatePick;

    int concertYear, concertMonth, concertDay, mHour, mMinute;

    SimpleDateFormat simpleDateFormat;

    RelativeLayout placeLayout;

    Button concertDateButton;
    Button timeStartDate;
    Button timeEndDate;

    TextView addressChoice;
    String location_name;
    String location_detail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_busking_open);

        ImageButton backBtn = (ImageButton) findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { BuskingOpen.super.onBackPressed(); }
        });

        //장소검색
        placeLayout = (RelativeLayout) findViewById(R.id.placesLayout);
        placeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent placeSearch = new Intent(getApplication(),RoadSearch.class);
                startActivity(placeSearch);
            }
        });

        addressChoice = (TextView) findViewById(R.id.addressChoice);

        location_name = getIntent().getStringExtra("location_name");
        location_detail = getIntent().getStringExtra("location_detail");
        if(location_name!=null){
            Log.e("장소이름", String.valueOf(location_name));
            addressChoice.setText(location_name+ " " +location_detail);
        }

        //시간을 가져오기위한 Calendar 인스턴스 선언
        Calendar cal = new GregorianCalendar();
        mHour = cal.get(Calendar.HOUR_OF_DAY);
        mMinute = cal.get(Calendar.MINUTE);


        concertDateButton = (Button) findViewById(R.id.concertStartDate);


        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);

        final GregorianCalendar calendar = new GregorianCalendar();
        concertYear = calendar.get(Calendar.YEAR);
        concertMonth = calendar.get(Calendar.MONTH);
        concertDay = calendar.get(Calendar.DAY_OF_MONTH);

        concertDateButton = (Button)findViewById(R.id.concertStartDate);
        concertDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDate(concertYear, concertMonth, concertDay, R.style.DatePickerSpinner);
                concertDatePick = String.valueOf(simpleDateFormat.format(calendar.getTime()));
            }
        });
    }

    public void mOnClick(View v){
        switch (v.getId()){
            //시간 대화상자 버튼이 눌리면 대화상자를 보여줌

            case R.id.timeStartDate:
                //여기서 리스너도 등록함
                new TimePickerDialog(BuskingOpen.this, R.style.TimePicker, startTimeSetListener, mHour, mMinute, false).show();

                break;

            case R.id.timeEndDate:
                //여기서 리스너도 등록함
                new TimePickerDialog(BuskingOpen.this, R.style.TimePicker, endTimeSetListener, mHour, mMinute, false).show();

                break;
        }
    }

    //시간 대화상자 리스너 부분

    TimePickerDialog.OnTimeSetListener startTimeSetListener =

            new TimePickerDialog.OnTimeSetListener() {

                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    //사용자가 입력한 값을 가져온뒤
                    mHour = hourOfDay;
                    mMinute = minute;

                    //텍스트뷰의 값을 업데이트함
                    Button startD = (Button)findViewById(R.id.timeStartDate);
                    startD.setText(String.format("%d:%d", mHour, mMinute));
                }
            };

    TimePickerDialog.OnTimeSetListener endTimeSetListener =

            new TimePickerDialog.OnTimeSetListener() {

                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    //사용자가 입력한 값을 가져온뒤
                    mHour = hourOfDay;
                    mMinute = minute;

                    //텍스트뷰의 값을 업데이트함
                    Button endD = (Button)findViewById(R.id.timeEndDate);
                    endD.setText(String.format("%d:%d", mHour, mMinute));
                }

            };


    void showDate(int year, int monthOfYear, int dayOfMonth, int spinnerTheme) {
        new SpinnerDatePickerDialogBuilder()
                .context(BuskingOpen.this)
                .callback(BuskingOpen.this)
                .spinnerTheme(spinnerTheme)
                .showTitle(true)
                .defaultDate(year, monthOfYear, dayOfMonth)
                .build()
                .show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        //concertDateButton.setText(year + "년 " + (month + 1) + "월 " + dayOfMonth + "일");
    }

    @Override
    public void onDateSet(com.tsongkha.spinnerdatepicker.DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        concertDateButton.setText(year + "년 " + (monthOfYear + 1) + "월 " + dayOfMonth + "일");
    }

    public void previousActivity(View v){
        onBackPressed();
    }

}
