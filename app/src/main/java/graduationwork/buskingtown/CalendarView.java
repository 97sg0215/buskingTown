package graduationwork.buskingtown;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import static graduationwork.buskingtown.R.layout.dayview;

public class CalendarView extends Fragment {

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
    RelativeLayout monRelative;
    String[] strTime = {"00:00", "01:00", "02:00", "03:00", "04:00", "05:00", "06:00", "07:00", "08:00", "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00", "19:00", "20:00", "21:00", "22:00", "23:00", "24:00"};
    private final DateFormat dateformatter = new DateFormat();
    private static final String dateformat = "yyyy/MM/dd";
    private String[] months = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"};
    private String[] monthsNumbers = new String[]{"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"};
    String eventdate;
    String title;
    ListView listView;
    ListView time_ListView = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View containerView = inflater.inflate(dayview, container, false);

        //리스트연결
        int nTimCnt = 0;
        ArrayList<ItemData> tData = new ArrayList<>();
        for (int i = 0; i < 1000; ++i) {
            ItemData tItem = new ItemData();
            tItem.strTime = strTime[nTimCnt++];
            tData.add(tItem);
            if (nTimCnt >= strTime.length) nTimCnt = 0;
        }

        time_ListView = containerView.findViewById(R.id.time_ListView);
        ListAdapter Adapter = new ListAdapter(tData);
        time_ListView.setAdapter(Adapter);


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
            startDate = yearSelected + "/" + (month + 1) + "/" + day;
        }

        Date date = new Date(startDate);
        startDate = (String) dateformatter.format(dateformat, date);
        eventdate = (String) dateformatter.format("MM/dd/yyyy", date);

        date = new Date(startDate);
        cal.setTime(date);

        String month = months[cal.get(Calendar.MONTH)] + "월";
        String date1 = " " + cal.get(Calendar.DATE) + "일";

        String year = "" + cal.get(Calendar.YEAR) + "년";

        String currentDate1 = year + month + date1;
        //String currentDate1 = date1 + month + "," + year;
        currentDate.setText(currentDate1);
        try {
            loadDataForDay();
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
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

        while (var4.hasNext()) {
            Event appointment = (Event) var4.next();
            String startTime = appointment.getDtstart();
            String endTime = appointment.getDtend();
            this.title = appointment.getTitle();

            for (int i = 0; i < this.strTime.length; ++i) {
                if (startTime.contains(this.strTime[i])) {
                    this.createViewForAppointment(startTime, endTime);
                }
            }
        }

    }

    @SuppressLint("ResourceType")
    private void createViewForAppointment(String startTime, String endTime) {
        int height = (int) this.calculateDiffInTime(startTime, endTime);
        ViewGroup.LayoutParams lprams = new ViewGroup.LayoutParams(-1, height);
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
            float var12 = (float) (diffMinutes / 60L);
        } catch (ParseException var13) {
            var13.printStackTrace();
        }

        return diffMinutes;
    }

    private void setGridCellAdapterToDate(int month, int year) throws ParseException {
        Calendar date = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        @SuppressLint("WrongConstant") String monthTemp = this.months[this.cal.get(2)];
        @SuppressLint("WrongConstant") String newdate = " " + this.cal.get(5) + " ";
        sdf.format(date.getTime());
        @SuppressLint("WrongConstant") String yearTemp = "" + this.cal.get(1);
        @SuppressLint("WrongConstant") String monthTemp1 = this.monthsNumbers[this.cal.get(2)];
        @SuppressLint("WrongConstant") int day = this.cal.get(5);
        String changedNewDate = monthTemp1 + "/" + day + "/" + this.yearSelected;
        this.eventdate = changedNewDate;

        this.loadDataForDay();
        String changedDate = yearTemp + "년" + monthTemp + "월" + newdate + "일";
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

    //리스트 아이템
    public class ItemData {
        public String strTime;
    }

    //리스트 Adapter
    public class ListAdapter extends BaseAdapter {

        LayoutInflater inflater = null;
        private ArrayList<ItemData> time_Data = null;
        private int timeListCnt = 0;

        public ListAdapter(ArrayList<ItemData> t_Data) {
            time_Data = t_Data;
            timeListCnt = time_Data.size();
        }

        @Override
        public int getCount() {
            Log.i("TAG", "getCount");
            return timeListCnt;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                final Context context = parent.getContext();
                if (inflater == null) {
                    inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                }
                assert inflater != null;
                convertView = inflater.inflate(R.layout.calendarlistview, parent, false);
            }

            TextView timeText = (TextView) convertView.findViewById(R.id.timeText);

            timeText.setText(time_Data.get(position).strTime);

            return convertView;
        }
    }

}

