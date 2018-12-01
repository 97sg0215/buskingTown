package graduationwork.buskingtown;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TimePicker;

import com.tsongkha.spinnerdatepicker.DatePicker;
import com.tsongkha.spinnerdatepicker.DatePickerDialog;
import com.tsongkha.spinnerdatepicker.SpinnerDatePickerDialogBuilder;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

public class OpenConcert extends AppCompatActivity {

    RelativeLayout placeLayout;
    int mHour, mMinute;

    Button concertStartDate, concertEndDate, timeStartDate, timeEndDate;
    int concertStartYear, concertStartMonth, concertStartDay, concertEndYear, concertEndMonth, concertEndDay;
    SimpleDateFormat simpleDateFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_concert);

        ImageButton backBtn = (ImageButton) findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenConcert.super.onBackPressed();
            }
        });

        //장소검색
        placeLayout = (RelativeLayout) findViewById(R.id.placesLayout);
        placeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent placeSearch = new Intent(getApplication(), LocationSearch.class);
                startActivity(placeSearch);
            }
        });

        //시간과 날짜 가져오기위한 Calendar 인스턴스 선언
        Calendar cal = new GregorianCalendar();
        mHour = cal.get(Calendar.HOUR_OF_DAY);
        mMinute = cal.get(Calendar.MINUTE);

        //달력개체 불러옴
        final GregorianCalendar startCalendar = new GregorianCalendar();
        concertStartYear = startCalendar.get(Calendar.YEAR);
        concertStartMonth = startCalendar.get(Calendar.MONTH);
        concertStartDay = startCalendar.get(Calendar.DAY_OF_MONTH);

        final GregorianCalendar endCalendar = new GregorianCalendar();
        concertEndYear = endCalendar.get(Calendar.YEAR);
        concertEndMonth = endCalendar.get(Calendar.MONTH);
        concertEndDay = endCalendar.get(Calendar.DAY_OF_MONTH);

        //날짜형태
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);

        concertStartDate = (Button) findViewById(R.id.concertStartDate);
        concertStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatePickerDialog.OnDateSetListener dateStartPicker = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        concertStartDate.setText(year + "년 " + (monthOfYear + 1) + "월 " + dayOfMonth + "일");
                    }
                };

                new SpinnerDatePickerDialogBuilder()
                        .callback(dateStartPicker)
                        .context(OpenConcert.this)
                        .spinnerTheme(R.style.DatePickerSpinner)
                        .showTitle(true)
                        .defaultDate(concertStartYear, concertStartMonth, concertStartDay)
                        .build()
                        .show();
            }
        });


        concertEndDate = (Button) findViewById(R.id.concertEndDate);
        concertEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog.OnDateSetListener dateEndPicker = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        concertEndDate.setText(year + "년 " + (monthOfYear + 1) + "월 " + dayOfMonth + "일");
                    }
                };

                new SpinnerDatePickerDialogBuilder()
                        .callback(dateEndPicker)
                        .context(OpenConcert.this)
                        .spinnerTheme(R.style.DatePickerSpinner)
                        .showTitle(true)
                        .defaultDate(concertEndYear, concertEndMonth, concertEndDay)
                        .build()
                        .show();
            }
        });

    }

    public void sOnClick(View v) {
        switch (v.getId()) {
            //시간 대화상자 버튼이 눌리면 대화상자를 보여줌

            case R.id.timeStartDate:
                //여기서 리스너도 등록함
                new TimePickerDialog(OpenConcert.this, R.style.TimePicker, startTimeSetListener, mHour, mMinute, false).show();

                break;

            case R.id.timeEndDate:
                //여기서 리스너도 등록함
                new TimePickerDialog(OpenConcert.this, R.style.TimePicker, endTimeSetListener, mHour, mMinute, false).show();

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
                    Button startD = (Button) findViewById(R.id.timeStartDate);
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
                    Button endD = (Button) findViewById(R.id.timeEndDate);
                    endD.setText(String.format("%d:%d", mHour, mMinute));
                }

            };

    public void previousActivity(View v) {
        onBackPressed();
    }
}
