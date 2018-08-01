package graduationwork.buskingtown;

import android.app.DatePickerDialog;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

import com.tsongkha.spinnerdatepicker.SpinnerDatePickerDialogBuilder;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

public class BuskingOpen extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, com.tsongkha.spinnerdatepicker.DatePickerDialog.OnDateSetListener {
    String concertDatePick;

    int concertYear, concertMonth, concertDay;


    SimpleDateFormat simpleDateFormat;

    Button concertDateButton;
    Button timeStartDate;
    Button timeEndDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_busking_open);

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

}
