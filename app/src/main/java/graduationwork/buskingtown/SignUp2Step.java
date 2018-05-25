package graduationwork.buskingtown;


import android.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class SignUp2Step extends AppCompatActivity {
    int birthYear, birthMonth, birthDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up2_step);

        //달력 객체 불러옴
        final GregorianCalendar calendar = new GregorianCalendar();
        birthYear = calendar.get(Calendar.YEAR);
        birthMonth = calendar.get(Calendar.MONTH);
        birthDay = calendar.get(Calendar.DAY_OF_MONTH);

        TextView birthBtn = (TextView) findViewById(R.id.birth);
        birthBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.support.v4.app.DialogFragment newFragment = new MyDatePickerFragment();
                newFragment.show(getSupportFragmentManager(), "birth_date picker");
            }
        });
    }

}
