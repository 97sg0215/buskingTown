package graduationwork.buskingtown;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.text.format.DateFormat;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;

public class CalendarView extends Fragment  {

    private ImageView prevMonth;
    private ImageView nextMonth;
    private TextView currentDate;
    private Calendar _calendar;
    private int month;
    private int yearSelected;
    private int day;
    private Calendar cal;
    private String startDate;
    private Context mContext;
    int margin = 0;
    RelativeLayout monRelative;
    Animation animFlipInForeward;
    Animation animFlipOutForeward;
    Animation animFlipInBackward;
    Animation animFlipOutBackward;
    private String[] timevalues = new String[]{"00:00 AM", "00:30 AM", "01:00 AM", "01:30 AM", "02:00 AM", "02:30 AM", "03:00 AM", "03:30 AM", "04:00 AM", "04:30 AM", "05:00 AM", "05:30 AM", "06:00 AM", "06:30 AM", "07:00 AM", "07:30 AM", "08:00 AM", "08:30 AM", "09:00 AM", "09:30 AM", "10:00 AM", "10:30 AM", "11:00 AM", "11:30 AM", "12:00 PM", "12:30 PM", "13:00 PM", "13:30 PM", "14:00 PM", "14:30 PM", "15:00 PM", "15:30 PM", "16:00 PM", "16:30 PM", "17:00 PM", "17:30 PM", "18:00 PM", "18:30 PM", "19:00 PM", "19:30 PM", "20:00 PM", "20:30 PM", "21:00 PM", "21:30 PM", "22:00 PM", "22:30 PM", "23:00 PM", "23:30 PM"};
    private final DateFormat dateformatter = new DateFormat();
    private static final String dateformat = "yyyy/MM/dd";
    private String[] months = new String[]{"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
    private String[] monthsNumbers = new String[]{"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"};
    String eventdate;
    String title;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View containerView = inflater.inflate(R.layout.dayview, container, false);

//        int permissionCheck = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_CALENDAR);
//
//        if(permissionCheck== PackageManager.PERMISSION_DENIED){
//
//            // 권한 없음
//        }else{
//            // 권한 있음
//        }

        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                Toast.makeText(getActivity(), "권한 허가", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                Toast.makeText(getActivity(), "권한 거부\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            }


        };

        TedPermission.with(getContext())
                .setPermissionListener(permissionlistener)
                .setRationaleMessage("구글 로그인을 하기 위해서는 주소록 접근 권한이 필요해요")
                .setDeniedMessage("왜 거부하셨어요...\n하지만 [설정] > [권한] 에서 권한을 허용할 수 있어요.")
                .setPermissions(Manifest.permission.READ_CALENDAR)
                .setPermissions(Manifest.permission.WRITE_CALENDAR)
                .check();


        monRelative = (RelativeLayout) containerView
                .findViewById(R.id.monRelative);
        prevMonth = (ImageView) containerView.findViewById(R.id.prevDay);
        nextMonth = (ImageView) containerView.findViewById(R.id.nextDay);
        currentDate = (TextView) containerView.findViewById(R.id.currentDate);
        _calendar = Calendar.getInstance(Locale.getDefault());
        cal = Calendar.getInstance(Locale.getDefault());
        mContext = getActivity().getApplicationContext();

        cal = Calendar.getInstance(Locale.getDefault());
        month = _calendar.get(Calendar.MONTH);
        yearSelected = _calendar.get(Calendar.YEAR);
        day = _calendar.get(Calendar.DAY_OF_MONTH);
        if (startDate == null) {
            // startDate = yearSelected + "/" + (month + 1) + "/" + day;
            startDate = (month + 1) + "/" + day + "/" + yearSelected;
        }

        Date date = new Date(startDate);
        startDate = (String) dateformatter.format(dateformat, date);
        eventdate = (String) dateformatter.format("MM/dd/yyyy", date);

        date = new Date(startDate);
        cal.setTime(date);

        String month = months[cal.get(Calendar.MONTH)];
        String date1 = " " + cal.get(Calendar.DATE) + " ";

        String year = "" + cal.get(Calendar.YEAR);

        String currentDate1 = date1 + month + "," + year;
        currentDate.setText(currentDate1);
        try {
            loadDataForDay();
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // scrollView.setOnTouchListener(gestureListener);
        prevMonth.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                try {
                    onLTRFling();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

        nextMonth.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                try {
                    onRTLFling();
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        });



        return containerView;
    }


    private void loadDataForDay() throws ParseException {
        new DataInterface(this.mContext);
        ArrayList<Event> appointments = DataInterface.getCurrentDayEvents(this.eventdate);
        Iterator var4 = appointments.iterator();

        while(var4.hasNext()) {
            Event appointment = (Event)var4.next();
            String startTime = appointment.getDtstart();
            String endTime = appointment.getDtend();
            this.title = appointment.getTitle();

            for(int i = 0; i < this.timevalues.length; ++i) {
                if (startTime.contains(this.timevalues[i])) {
                    this.createViewForAppointment(startTime, endTime);
                }
            }
        }

    }

    @SuppressLint("ResourceType")
    private void createViewForAppointment(String startTime, String endTime) {
        int marginTop = this.calculateMargin(startTime);
        int height = (int)this.calculateDiffInTime(startTime, endTime);
        ViewGroup.LayoutParams lprams = new ViewGroup.LayoutParams(-1, height);
        int marginLeft = 30;

//        lprams.setMargins(marginLeft, 0, 0, 0);
//        lprams.topMargin = marginTop;
        Button button = new Button(this.mContext);
        button.setBackgroundResource(2130837505);
        button.setLayoutParams(lprams);
        button.setTextColor(-16777216);
        button.setTextAppearance(this.mContext, 2131034114);
        button.setText("Event");
        if (height <= 18) {
            button.setSingleLine();
        }

        button.setEllipsize(TextUtils.TruncateAt.END);
        this.monRelative.addView(button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(CalendarView.this.mContext, "Button Click1", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private long calculateDiffInTime(String startTime, String endTime) {
        String startTimeAppointment = startTime;
        String endTimeAppointment = endTime;
        long diffMinutes = 0L;
        SimpleDateFormat format = new SimpleDateFormat("hh:mm aa");
        Date d1 = null;
        Date d2 = null;

        try {
            d1 = format.parse(startTimeAppointment);
            d2 = format.parse(endTimeAppointment);
            long diff = d2.getTime() - d1.getTime();
            diffMinutes = diff / 60000L;
            float var12 = (float)(diffMinutes / 60L);
        } catch (ParseException var13) {
            var13.printStackTrace();
        }

        return diffMinutes;
    }

    private int calculateMargin(String startTime) {
        this.margin = 3;

        for(int i = 0; startTime.compareToIgnoreCase(this.timevalues[i]) != 0; ++i) {
            this.margin += 30;
        }

        return this.margin;
    }

    private void setGridCellAdapterToDate(int month, int year) throws ParseException {
        Calendar date = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        @SuppressLint("WrongConstant") String monthTemp = this.months[this.cal.get(2)];
        @SuppressLint("WrongConstant") String newdate = " " + this.cal.get(5) + " ";
        sdf.format(date.getTime());
        @SuppressLint("WrongConstant") String yearTemp = "" + this.cal.get(1);
        @SuppressLint("WrongConstant") String monthTemp1 = this.monthsNumbers[this.cal.get(2)];
        @SuppressLint("WrongConstant") int changedmonth = this.cal.get(2);
        @SuppressLint("WrongConstant") int changedYear = this.cal.get(1);
        @SuppressLint("WrongConstant") int day = this.cal.get(5);
        String changedNewDate = monthTemp1 + "/" + day + "/" + this.yearSelected;
        this.eventdate = changedNewDate;

        this.loadDataForDay();
        String changedDate = newdate + monthTemp + "," + yearTemp;
        this.currentDate.setText(changedDate);
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @SuppressLint("WrongConstant")
    private void onLTRFling() throws ParseException {
        this.removeViews();
        this.cal.add(5, -1);
        this.setGridCellAdapterToDate(2, 1);
    }

    @SuppressLint("WrongConstant")
    private void onRTLFling() throws ParseException {
        this.removeViews();
        this.cal.add(5, 1);
        this.setGridCellAdapterToDate(2, 1);
    }

    private void removeViews() {
        this.monRelative.removeAllViews();
    }
}
