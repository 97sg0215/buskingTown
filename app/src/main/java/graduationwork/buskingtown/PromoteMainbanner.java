package graduationwork.buskingtown;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class PromoteMainbanner extends Fragment {

    TextView publicDateHint;
    SimpleDateFormat simpleDateFormat;
    int promoteYear, promoteMonth, promoteDay, mHour, mMinute;
    RelativeLayout dateImgLayout;
    String p_date;

    public PromoteMainbanner(){
        // Required empty public constructor
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.activity_promote_mainbanner, container, false);

        //달력개체 불러옴
        final GregorianCalendar startCalendar = new GregorianCalendar();
        promoteYear = startCalendar.get(Calendar.YEAR);
        promoteMonth = startCalendar.get(Calendar.MONTH);
        promoteDay = startCalendar.get(Calendar.DAY_OF_MONTH);

        //시간을 가져오기위한 Calendar 인스턴스 선언
        Calendar cal = new GregorianCalendar();
        mHour = cal.get(Calendar.HOUR_OF_DAY);
        mMinute = cal.get(Calendar.MINUTE);

        //날짜형태
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);

        Calendar minDate = Calendar.getInstance();

        dateImgLayout = (RelativeLayout) v.findViewById(R.id.dateImgLayout);
        publicDateHint = (TextView) v.findViewById(R.id.publicDateHint);
        dateImgLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog dialog = new DatePickerDialog(getContext(),R.style.MyDatePickerDialogTheme, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int date) {
                        publicDateHint.setText(year + "년 " + (month + 1) + "월 " + date + "일");
                        String p_start_date = String.valueOf(year+"-"+(month+1)+"-"+date);
                    }
                }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));
                dialog.getDatePicker().setMinDate(new Date().getTime());    //입력한 날짜 이후로 클릭 안되게 옵션
                dialog.show();
            }
        });

        return v;
    }
}
