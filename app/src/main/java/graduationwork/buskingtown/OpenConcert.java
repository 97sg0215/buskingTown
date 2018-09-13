package graduationwork.buskingtown;

import android.app.TimePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class OpenConcert extends AppCompatActivity {

    int mHour, mMinute;

    Button timeStartDate, timeEndDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_concert);

        ImageButton backBtn = (ImageButton) findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { OpenConcert.super.onBackPressed(); }
        });

        //시간과 날짜 가져오기위한 Calendar 인스턴스 선언
        Calendar cal = new GregorianCalendar();
        mHour = cal.get(Calendar.HOUR_OF_DAY);
        mMinute = cal.get(Calendar.MINUTE);

    }

    public void sOnClick(View v){
        switch (v.getId()){
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
