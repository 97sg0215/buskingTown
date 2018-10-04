package graduationwork.buskingtown;

import android.app.TimePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TimePicker;

import com.tsongkha.spinnerdatepicker.DatePicker;
import com.tsongkha.spinnerdatepicker.DatePickerDialog;
import com.tsongkha.spinnerdatepicker.SpinnerDatePickerDialogBuilder;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

public class PracticeRoomReservation extends AppCompatActivity {

    Button practiceStartDate, practiceEndDate, timeStartDate, timeEndDate;
    int mHour, mMinute, practiceStartYear, practiceStartMonth, practiceStartDay , practiceEndYear, practiceEndMonth, practiceEndDay;

    EditText busker_num;

    SimpleDateFormat simpleDateFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice_room_reservation);

        ImageButton backBtn = (ImageButton) findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { PracticeRoomReservation.super.onBackPressed(); }
        });

        busker_num = (EditText) findViewById(R.id.busker_num);
        busker_num.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

        //시간을 가져오기위한 Calendar 인스턴스 선언
        Calendar cal = new GregorianCalendar();
        mHour = cal.get(Calendar.HOUR_OF_DAY);
        mMinute = cal.get(Calendar.MINUTE);

        //달력개체 불러옴
        final GregorianCalendar startCalendar = new GregorianCalendar();
        practiceStartYear = startCalendar.get(Calendar.YEAR);
        practiceStartMonth = startCalendar.get(Calendar.MONTH);
        practiceStartDay = startCalendar.get(Calendar.DAY_OF_MONTH);

        final GregorianCalendar endCalendar = new GregorianCalendar();
        practiceEndYear = endCalendar.get(Calendar.YEAR);
        practiceEndMonth = endCalendar.get(Calendar.MONTH);
        practiceEndDay = endCalendar.get(Calendar.DAY_OF_MONTH);

        //날짜형태
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);

        practiceStartDate = (Button) findViewById(R.id.practiceStartDate);
        practiceStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatePickerDialog.OnDateSetListener dateStartPicker = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        practiceStartDate.setText(year + "년 " + (monthOfYear + 1) + "월 " + dayOfMonth + "일");
                    }
                };

                new SpinnerDatePickerDialogBuilder()
                        .callback(dateStartPicker)
                        .context(PracticeRoomReservation.this)
                        .spinnerTheme(R.style.DatePickerSpinner)
                        .showTitle(true)
                        .defaultDate(practiceStartYear,practiceStartMonth,practiceStartDay)
                        .build()
                        .show();
            }
        });

        practiceEndDate = (Button) findViewById(R.id.practiceEndDate);
        practiceEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog.OnDateSetListener dateEndPicker = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        practiceEndDate.setText(year + "년 " + (monthOfYear + 1) + "월 " + dayOfMonth + "일");
                    }
                };

                new SpinnerDatePickerDialogBuilder()
                        .callback(dateEndPicker)
                        .context(PracticeRoomReservation.this)
                        .spinnerTheme(R.style.DatePickerSpinner)
                        .showTitle(true)
                        .defaultDate(practiceEndYear,practiceEndMonth,practiceEndDay)
                        .build()
                        .show();
            }
        });
    }

    public void pOnClick(View v){
        switch (v.getId()){
            //시간 대화상자 버튼이 눌리면 대화상자를 보여줌

            case R.id.timeStartDate:
                //여기서 리스너도 등록함
                new TimePickerDialog(PracticeRoomReservation.this, R.style.TimePicker, startTimeSetListener, mHour, mMinute, false).show();

                break;

            case R.id.timeEndDate:
                //여기서 리스너도 등록함
                new TimePickerDialog(PracticeRoomReservation.this, R.style.TimePicker, endTimeSetListener, mHour, mMinute, false).show();

                break;
        }
    }

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

    public void previousActivity(View v){
        onBackPressed();
    }
}