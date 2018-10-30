package graduationwork.buskingtown;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telecom.Call;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

//import com.tsongkha.spinnerdatepicker.DatePicker;
//import com.tsongkha.spinnerdatepicker.DatePickerDialog;
//import com.tsongkha.spinnerdatepicker.SpinnerDatePickerDialogBuilder;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import graduationwork.buskingtown.api.RestApiService;
import graduationwork.buskingtown.model.PracticeReservation;
import okhttp3.ResponseBody;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Body;

public class PracticeRoomReservation extends AppCompatActivity {

    Button practiceStartDate, practiceEndDate, timeStartDate, timeEndDate, confirmBtn;
    int mHour, mMinute, practiceStartYear, practiceStartMonth, practiceStartDay , practiceEndYear, practiceEndMonth, practiceEndDay, busker_id;

    EditText busker_num, busker_email;

    SimpleDateFormat simpleDateFormat;

    TextView roomChoice, main_name, loc_name, loc_info, price, team ;
    ImageView practice_img;

    LinearLayout choiceTimeBtnContainer;

    RestApiService apiService;

    String p_name, team_name;
    String p_info;
    String p_address;
    String p_image;
    String user_token;
    String choice_option_price;
    String choice_option_name;
    Integer choice_option;
    String p_start_time;
    String p_end_time;
    int provide_id;

    String num, email;

    ArrayList<Integer> selectCheck = new ArrayList<>();
    ArrayList<Integer> selectNoneCheck = new ArrayList<>();
    ArrayList<String> selectCheckTime = new ArrayList<>();
    String checkTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice_room_reservation);

        restApiBuilder();

        getLocalData();

        ImageButton backBtn = (ImageButton) findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { PracticeRoomReservation.super.onBackPressed(); }
        });

        busker_num = (EditText) findViewById(R.id.busker_num);
        busker_num.addTextChangedListener(new PhoneNumberFormattingTextWatcher());


        //정보 세팅
        roomChoice = (TextView) findViewById(R.id.roomChoice);
        main_name = (TextView) findViewById(R.id.loc_main_name);
        loc_name = (TextView) findViewById(R.id.practiceName);
        loc_info = (TextView) findViewById(R.id.practiceAdd);
        practice_img = (ImageView) findViewById(R.id.practice_img);
        price = (TextView)findViewById(R.id.price);
        confirmBtn = (Button) findViewById(R.id.confirmBtn);
        busker_email = (EditText) findViewById(R.id.busker_email);
        busker_num = (EditText) findViewById(R.id.busker_num);
        team = (TextView) findViewById(R.id.team);

        p_name = getIntent().getStringExtra("provide_name");
        p_address = getIntent().getStringExtra("provide_address");
        p_image = getIntent().getStringExtra("provide_image");
        choice_option_name = getIntent().getStringExtra("provide_option_name");
        choice_option = getIntent().getIntExtra("provide_option_id",0);
        choice_option_price = getIntent().getStringExtra("provide_option_price");
        provide_id = getIntent().getIntExtra("provide_id",0);
        p_start_time = getIntent().getStringExtra("provide_start_time");
        p_end_time = getIntent().getStringExtra("provide_end_time");

        main_name.setText(p_name);
        loc_name.setText(p_name);
        loc_info.setText(p_address);
        team.setText(team_name);

        busker_num.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

        busker_num.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                num = busker_num.getText().toString();
            }
        });

        busker_email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                email = busker_email.getText().toString();
            }
        });



        roomChoice.setText(choice_option_name);

        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);

            practice_img.setImageBitmap(getBitmapFromURL(p_image));
            practice_img.setScaleType(ImageView.ScaleType.FIT_XY);
        }


        //시간을 가져오기위한 Calendar 인스턴스 선언
        Calendar cal = new GregorianCalendar();
        mHour = cal.get(Calendar.HOUR_OF_DAY);
        mMinute = cal.get(Calendar.MINUTE);

        //달력개체 불러옴
        final GregorianCalendar startCalendar = new GregorianCalendar();
        practiceStartYear = startCalendar.get(Calendar.YEAR);
        practiceStartMonth = startCalendar.get(Calendar.MONTH);
        practiceStartDay = startCalendar.get(Calendar.DAY_OF_MONTH);

        //날짜형태
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);

        choiceTimeBtnContainer = (LinearLayout) findViewById(R.id.choiceTimeBtnContainer);

        practiceStartDate = (Button) findViewById(R.id.practiceStartDate);
        practiceStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog dialog = new DatePickerDialog(PracticeRoomReservation.this,R.style.MyDatePickerDialogTheme, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int date) {
                        practiceStartDate.setText(year + "년 " + (month + 1) + "월 " + date + "일");
                        String p_start_date = String.valueOf(year+"-"+(month+1)+"-"+date);
                        //예약 정보 불러오기
                        choiceTimeBtnContainer.removeAllViews();
                        selectCheck.clear();
                        getReservation(user_token,provide_id,choice_option,p_start_date,p_start_time,p_end_time);
                    }
                }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));

                dialog.getDatePicker().setMinDate(new Date().getTime());    //입력한 날짜 이후로 클릭 안되게 옵션
                dialog.show();
            }
        });


    }

    public void restApiBuilder() {
        ApplicationController application = ApplicationController.getInstance();
        application.buildNetworkService();
        apiService = ApplicationController.getInstance().getRestApiService();
    }

    public void getReservation(String user_token, int provide_id, int provide_option_id, String practice_date, String p_start_time, String p_end_time){
        ArrayList<Integer> start_reservation_time = new ArrayList<>();
        ArrayList<Integer> end_reservation_time = new ArrayList<>();

        retrofit2.Call<List<PracticeReservation>> getReservation = apiService.reservationCheckPractice(user_token,provide_id,provide_option_id,practice_date);
        getReservation.enqueue(new Callback<List<PracticeReservation>>() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onResponse(retrofit2.Call<List<PracticeReservation>> call, Response<List<PracticeReservation>> response) {
                if(response.isSuccessful()){
                    List<PracticeReservation> practiceReservations = response.body();
                    if(practiceReservations.size()==0){
                        //예약 목록이 하나도 없을때는 그냥 예약
                        //대여 시간
                        String[] start_time_words = p_start_time.split(":");
                        String[] end_time_words = p_end_time.split(":");
                        int start_time = Integer.parseInt(start_time_words[0]);
                        int end_time = Integer.parseInt(end_time_words[0]);
                        for (int start = start_time; start <= end_time-2 ; start +=2){
                            Button choiceTimeList = (Button) getLayoutInflater().inflate(R.layout.choice_time_btn, choiceTimeBtnContainer, false);
                           // final Button btn = choiceTimeList.findViewById(R.id.choice_time);
                            choiceTimeList.setText(start + ":00");
                            if (choiceTimeList.getParent() != null)
                                ((ViewGroup) choiceTimeList.getParent()).removeView(choiceTimeList);
                            choiceTimeBtnContainer.addView(choiceTimeList);
                        }
                        for(int j=0; j < choiceTimeBtnContainer.getChildCount(); j++){
                            View v = choiceTimeBtnContainer.getChildAt(j);
                            int finalJ = j;
                            v.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View button) {
                                    v.setSelected(!button.isSelected());
                                    if (button.isSelected()) {//선택하고 난 후
                                        v.setBackground(getDrawable(R.drawable.able_btn));
                                        if(v instanceof Button) ((Button)v).setTextColor(Color.WHITE);
                                        selectCheck.add(finalJ);
                                        if(selectCheck.size()==2&&selectCheck.get(0)<finalJ){
                                            selectCheckTime.clear();
                                            for (int x=selectCheck.get(0); x<finalJ; x++){
                                                choiceTimeBtnContainer.getChildAt(x).setBackground(getDrawable(R.drawable.able_btn));
                                                if(choiceTimeBtnContainer.getChildAt(x) instanceof Button){
                                                    ((Button)choiceTimeBtnContainer.getChildAt(x)).setTextColor(Color.WHITE);
                                                    checkTime = String.valueOf(((Button)choiceTimeBtnContainer.getChildAt(x)).getText());
                                                }
                                                selectCheckTime.add(checkTime);
                                            }
                                            selectCheckTime.add(String.valueOf(((Button)choiceTimeBtnContainer.getChildAt(finalJ)).getText()));
                                            Log.e("두번 체크",String.valueOf(selectCheckTime));
                                            int sum_price = (Integer.parseInt(choice_option_price) * 2) * selectCheckTime.size();
                                            price.setText(String.valueOf(sum_price));
                                            selectCheck.clear();

                                            //끝나는 시간
                                            String[] p_end_time_words = String.valueOf(((Button)choiceTimeBtnContainer.getChildAt(finalJ)).getText()).split(":");
                                            int end_time = Integer.parseInt(p_end_time_words[0]) + 2;
                                            String real_end_time = String.valueOf(end_time) + ":00";

                                            confirmBtn.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    next(provide_id,provide_option_id,practice_date,selectCheckTime.get(0),real_end_time,sum_price,
                                                            p_image,p_name,p_address,choice_option_name,team_name,num,email);
                                                }
                                            });

                                        }else if(selectCheck.get(0)>finalJ||selectCheck.size()==1){
                                            selectCheckTime.clear();
                                            v.setBackground(getDrawable(R.drawable.able_btn));
                                            if(v instanceof Button){
                                                ((Button)v).setTextColor(Color.WHITE);
                                                checkTime = String.valueOf(((Button)v).getText());
                                            }
                                            selectCheckTime.add(checkTime);
                                            Log.e("한번 체크",String.valueOf(selectCheckTime));
                                            int sum_price = Integer.parseInt(choice_option_price) * 2 ;
                                            price.setText(String.valueOf(sum_price));
                                            for (int x=0; x <choiceTimeBtnContainer.getChildCount(); x++){
                                                if(x!=finalJ){
                                                    choiceTimeBtnContainer.getChildAt(x).setBackground(getDrawable(R.drawable.date_rounded));
                                                    if(choiceTimeBtnContainer.getChildAt(x) instanceof Button) ((Button)choiceTimeBtnContainer.getChildAt(x)).setTextColor(R.color.mainPurple);
                                                    selectCheck.clear();
                                                    selectCheck.add(finalJ);
                                                }
                                            }
                                            //끝나는 시간
                                            String[] p_end_time_words = selectCheckTime.get(0).split(":");
                                            int end_time = Integer.parseInt(p_end_time_words[0]) + 2;
                                            String real_end_time = String.valueOf(end_time) + ":00";

                                            confirmBtn.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    next(provide_id,provide_option_id,practice_date,selectCheckTime.get(0),real_end_time,sum_price,
                                                            p_image,p_name,p_address,choice_option_name,team_name,num,email);
                                                }
                                            });
                                        }
                                    }else {// 이미 한번 셀렉한거
                                        v.setBackground(getDrawable(R.drawable.able_btn));
                                        if(v instanceof Button) ((Button)v).setTextColor(Color.WHITE);
                                        selectCheck.add(finalJ);
                                        if(selectCheck.size()==2&&selectCheck.get(0)<finalJ){
                                            selectCheckTime.clear();
                                            for (int x=selectCheck.get(0); x<finalJ; x++){
                                                choiceTimeBtnContainer.getChildAt(x).setBackground(getDrawable(R.drawable.able_btn));
                                                if(choiceTimeBtnContainer.getChildAt(x) instanceof Button){
                                                    ((Button)choiceTimeBtnContainer.getChildAt(x)).setTextColor(Color.WHITE);
                                                    checkTime = String.valueOf(((Button)choiceTimeBtnContainer.getChildAt(x)).getText());
                                                }
                                                selectCheckTime.add(checkTime);
                                            }
                                            selectCheckTime.add(String.valueOf(((Button)choiceTimeBtnContainer.getChildAt(finalJ)).getText()));
                                            Log.e("두번 체크",String.valueOf(selectCheckTime));
                                            int sum_price = (Integer.parseInt(choice_option_price) * 2) * selectCheckTime.size();
                                            price.setText(String.valueOf(sum_price));
                                            selectCheck.clear();

                                            //끝나는 시간
                                            String[] p_end_time_words = String.valueOf(((Button)choiceTimeBtnContainer.getChildAt(finalJ)).getText()).split(":");
                                            int end_time = Integer.parseInt(p_end_time_words[0]) + 2;
                                            String real_end_time = String.valueOf(end_time) + ":00";

                                            confirmBtn.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    next(provide_id,provide_option_id,practice_date,selectCheckTime.get(0),real_end_time,sum_price,
                                                            p_image,p_name,p_address,choice_option_name,team_name,num,email);
                                                }
                                            });

                                        }else if(selectCheck.get(0)>finalJ||selectCheck.size()==1){
                                            selectCheckTime.clear();
                                            v.setBackground(getDrawable(R.drawable.able_btn));
                                            if(v instanceof Button){
                                                ((Button)v).setTextColor(Color.WHITE);
                                                checkTime = String.valueOf(((Button)v).getText());
                                            }
                                            selectCheckTime.add(checkTime);
                                            int sum_price = Integer.parseInt(choice_option_price) * 2 ;
                                            price.setText(String.valueOf(sum_price));
                                            Log.e("한번 체크",String.valueOf(selectCheckTime));

                                            //끝나는 시간
                                            String[] p_end_time_words = selectCheckTime.get(0).split(":");
                                            int end_time = Integer.parseInt(p_end_time_words[0]) + 2;
                                            String real_end_time = String.valueOf(end_time) + ":00";

                                            confirmBtn.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    next(provide_id,provide_option_id,practice_date,selectCheckTime.get(0),real_end_time,sum_price,
                                                            p_image,p_name,p_address,choice_option_name,team_name,num,email);
                                                }
                                            });


                                            for (int x=0; x <choiceTimeBtnContainer.getChildCount(); x++){
                                                if(x!=finalJ){
                                                    choiceTimeBtnContainer.getChildAt(x).setBackground(getDrawable(R.drawable.date_rounded));
                                                    if(choiceTimeBtnContainer.getChildAt(x) instanceof Button) ((Button)choiceTimeBtnContainer.getChildAt(x)).setTextColor(R.color.mainPurple);
                                                    selectCheck.clear();
                                                    selectCheck.add(finalJ);
                                                }
                                            }
                                        }
                                    }
                                }
                            });
                        }
                    }else {
                        for (int i=0; i < practiceReservations.size(); i ++){
                            //예약 된 시간
                            String reservation_start_time = practiceReservations.get(i).getPractice_start_time();
                            String reservation_end_time = practiceReservations.get(i).getPractice_end_time();
                            String[] start_reservation_words = reservation_start_time.split(":");
                            String[] end_reservation_words = reservation_end_time.split(":");

                            start_reservation_time.add(Integer.parseInt(start_reservation_words[0]));
                            end_reservation_time.add(Integer.parseInt(end_reservation_words[0]));

                            //원래 시간
                            String[] start_time_words = p_start_time.split(":");
                            String[] end_time_words = p_end_time.split(":");
                            int start_time = Integer.parseInt(start_time_words[0]);
                            int end_time = Integer.parseInt(end_time_words[0]);

                            for (int start = start_time; start <= end_time-2 ; start +=2){
                                Button choiceTimeList = (Button) getLayoutInflater().inflate(R.layout.choice_time_btn, choiceTimeBtnContainer, false);
                                // final Button btn = choiceTimeList.findViewById(R.id.choice_time);
                                choiceTimeList.setText(start + ":00");
                                if (choiceTimeList.getParent() != null)
                                    ((ViewGroup) choiceTimeList.getParent()).removeView(choiceTimeList);
                                choiceTimeBtnContainer.addView(choiceTimeList);
                            }

                            for(int j=0; j < choiceTimeBtnContainer.getChildCount(); j++) {
                                View v = choiceTimeBtnContainer.getChildAt(j);
                                String[] c_time = new String[0];
                                if(v instanceof Button){
                                    c_time = String.valueOf(((Button) v).getText()).split(":");
                                }
                                //end는 가져온 시간
                                for(int end = end_reservation_time.get(i)-2; end >= start_reservation_time.get(i); end-=2){
                                    if(Integer.parseInt(c_time[0])==end){
                                        v.setOnClickListener(null);
                                        v.setBackground(getDrawable(R.drawable.disable_btn));
                                        if(v instanceof Button)  ((Button) v).setTextColor(Color.WHITE);
                                        selectNoneCheck.add(j);
                                    }else {
                                        int finalJ = j;
                                        v.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View button) {
                                                v.setSelected(!button.isSelected());
                                                if (button.isSelected()) {//선택하고 난 후
                                                    v.setBackground(getDrawable(R.drawable.able_btn));
                                                    if(v instanceof Button) ((Button)v).setTextColor(Color.WHITE);
                                                    selectCheck.add(finalJ);
                                                    if(selectCheck.size()==2&&selectCheck.get(0)<finalJ){
                                                        selectCheckTime.clear();
                                                        if(finalJ<selectNoneCheck.get(0)){
                                                            for (int x=selectCheck.get(0); x<finalJ; x++){
                                                                choiceTimeBtnContainer.getChildAt(x).setBackground(getDrawable(R.drawable.able_btn));
                                                                if(choiceTimeBtnContainer.getChildAt(x) instanceof Button){
                                                                    ((Button)choiceTimeBtnContainer.getChildAt(x)).setTextColor(Color.WHITE);
                                                                    checkTime = String.valueOf(((Button)choiceTimeBtnContainer.getChildAt(x)).getText());
                                                                }
                                                                selectCheckTime.add(checkTime);
                                                            }
                                                        }else if(finalJ>selectNoneCheck.get(selectNoneCheck.size()-1)) {
                                                            selectCheck.clear();
                                                            selectCheck.add(finalJ);
                                                            for (int x=selectCheck.get(0); x<finalJ; x++){
                                                                choiceTimeBtnContainer.getChildAt(x).setBackground(getDrawable(R.drawable.able_btn));
                                                                if(choiceTimeBtnContainer.getChildAt(x) instanceof Button){
                                                                    ((Button)choiceTimeBtnContainer.getChildAt(x)).setTextColor(Color.WHITE);
                                                                    checkTime = String.valueOf(((Button)choiceTimeBtnContainer.getChildAt(x)).getText());
                                                                }
                                                                selectCheckTime.add(checkTime);
                                                            }
                                                        }

                                                        selectCheckTime.add(String.valueOf(((Button)choiceTimeBtnContainer.getChildAt(finalJ)).getText()));
                                                        Log.e("두번 체크",String.valueOf(selectCheckTime));
                                                        int sum_price = (Integer.parseInt(choice_option_price) * 2) * selectCheckTime.size();
                                                        price.setText(String.valueOf(sum_price));
                                                        selectCheck.clear();

                                                        //끝나는 시간
                                                        String[] p_end_time_words = String.valueOf(((Button)choiceTimeBtnContainer.getChildAt(finalJ)).getText()).split(":");
                                                        int end_time = Integer.parseInt(p_end_time_words[0]) + 2;
                                                        String real_end_time = String.valueOf(end_time) + ":00";

                                                        confirmBtn.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {
                                                                next(provide_id,provide_option_id,practice_date,selectCheckTime.get(0),real_end_time,sum_price,
                                                                        p_image,p_name,p_address,choice_option_name,team_name,num,email);
                                                            }
                                                        });

                                                    }else if(selectCheck.get(0)>finalJ||selectCheck.size()==1){
                                                        selectCheckTime.clear();
                                                        v.setBackground(getDrawable(R.drawable.able_btn));
                                                        if(v instanceof Button){
                                                            ((Button)v).setTextColor(Color.WHITE);
                                                            checkTime = String.valueOf(((Button)v).getText());
                                                        }
                                                        selectCheckTime.add(checkTime);
                                                        Log.e("한번 체크",String.valueOf(selectCheckTime));
                                                        int sum_price = Integer.parseInt(choice_option_price) * 2 ;
                                                        price.setText(String.valueOf(sum_price));
                                                        if(selectCheck.get(0)<selectNoneCheck.get(0)){
                                                            for (int x=0; x<selectNoneCheck.get(0); x++){
                                                                if(x!=finalJ){
                                                                    choiceTimeBtnContainer.getChildAt(x).setBackground(getDrawable(R.drawable.date_rounded));
                                                                    if(choiceTimeBtnContainer.getChildAt(x) instanceof Button) ((Button)choiceTimeBtnContainer.getChildAt(x)).setTextColor(R.color.mainPurple);
                                                                    selectCheck.clear();
                                                                    selectCheck.add(finalJ);
                                                                }
                                                            }
                                                        }else {
                                                            for (int x=choiceTimeBtnContainer.getChildCount()-1; x>selectNoneCheck.get(selectNoneCheck.size()-1); x--){
                                                                if(x!=finalJ){
                                                                    choiceTimeBtnContainer.getChildAt(x).setBackground(getDrawable(R.drawable.date_rounded));
                                                                    if(choiceTimeBtnContainer.getChildAt(x) instanceof Button) ((Button)choiceTimeBtnContainer.getChildAt(x)).setTextColor(R.color.mainPurple);
                                                                    selectCheck.clear();
                                                                    selectCheck.add(finalJ);
                                                                }
                                                            }
                                                        }
                                                        //끝나는 시간
                                                        String[] p_end_time_words = selectCheckTime.get(0).split(":");
                                                        int end_time = Integer.parseInt(p_end_time_words[0]) + 2;
                                                        String real_end_time = String.valueOf(end_time) + ":00";

                                                        confirmBtn.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {
                                                                next(provide_id,provide_option_id,practice_date,selectCheckTime.get(0),real_end_time,sum_price,
                                                                        p_image,p_name,p_address,choice_option_name,team_name,num,email);
                                                            }
                                                        });
                                                    }
                                                }else {// 이미 한번 셀렉한거
                                                    v.setBackground(getDrawable(R.drawable.able_btn));
                                                    if(v instanceof Button) ((Button)v).setTextColor(Color.WHITE);
                                                    selectCheck.add(finalJ);
                                                    if(selectCheck.size()==2&&selectCheck.get(0)<finalJ){
                                                        selectCheckTime.clear();
                                                        if(finalJ<selectNoneCheck.get(0)){
                                                            for (int x=selectCheck.get(0); x<finalJ; x++){
                                                                choiceTimeBtnContainer.getChildAt(x).setBackground(getDrawable(R.drawable.able_btn));
                                                                if(choiceTimeBtnContainer.getChildAt(x) instanceof Button){
                                                                    ((Button)choiceTimeBtnContainer.getChildAt(x)).setTextColor(Color.WHITE);
                                                                    checkTime = String.valueOf(((Button)choiceTimeBtnContainer.getChildAt(x)).getText());
                                                                }
                                                                selectCheckTime.add(checkTime);
                                                            }
                                                        }else if(finalJ>selectNoneCheck.get(selectNoneCheck.size()-1)) {
                                                            selectCheck.clear();
                                                            selectCheck.add(finalJ);
                                                            for (int x=selectCheck.get(0); x<finalJ; x++){
                                                                choiceTimeBtnContainer.getChildAt(x).setBackground(getDrawable(R.drawable.able_btn));
                                                                if(choiceTimeBtnContainer.getChildAt(x) instanceof Button){
                                                                    ((Button)choiceTimeBtnContainer.getChildAt(x)).setTextColor(Color.WHITE);
                                                                    checkTime = String.valueOf(((Button)choiceTimeBtnContainer.getChildAt(x)).getText());
                                                                }
                                                                selectCheckTime.add(checkTime);
                                                            }
                                                        }
                                                        selectCheckTime.add(String.valueOf(((Button)choiceTimeBtnContainer.getChildAt(finalJ)).getText()));
                                                        Log.e("두번 체크",String.valueOf(selectCheckTime));
                                                        int sum_price = (Integer.parseInt(choice_option_price) * 2) * selectCheckTime.size();
                                                        price.setText(String.valueOf(sum_price));
                                                        selectCheck.clear();

                                                        //끝나는 시간
                                                        String[] p_end_time_words = String.valueOf(((Button)choiceTimeBtnContainer.getChildAt(finalJ)).getText()).split(":");
                                                        int end_time = Integer.parseInt(p_end_time_words[0]) + 2;
                                                        String real_end_time = String.valueOf(end_time) + ":00";

                                                        confirmBtn.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {
                                                                next(provide_id,provide_option_id,practice_date,selectCheckTime.get(0),real_end_time,sum_price,
                                                                        p_image,p_name,p_address,choice_option_name,team_name,num,email);
                                                            }
                                                        });

                                                    }else if(selectCheck.get(0)>finalJ||selectCheck.size()==1){
                                                        selectCheckTime.clear();
                                                        v.setBackground(getDrawable(R.drawable.able_btn));
                                                        if(v instanceof Button){
                                                            ((Button)v).setTextColor(Color.WHITE);
                                                            checkTime = String.valueOf(((Button)v).getText());
                                                        }
                                                        selectCheckTime.add(checkTime);
                                                        int sum_price = Integer.parseInt(choice_option_price) * 2 ;
                                                        price.setText(String.valueOf(sum_price));
                                                        Log.e("한번 체크",String.valueOf(selectCheckTime));

                                                        //끝나는 시간
                                                        String[] p_end_time_words = selectCheckTime.get(0).split(":");
                                                        int end_time = Integer.parseInt(p_end_time_words[0]) + 2;
                                                        String real_end_time = String.valueOf(end_time) + ":00";

                                                        confirmBtn.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {
                                                                next(provide_id,provide_option_id,practice_date,selectCheckTime.get(0),real_end_time,sum_price,
                                                                        p_image,p_name,p_address,choice_option_name,team_name,num,email);
                                                            }
                                                        });
                                                        if(selectCheck.get(0)<selectNoneCheck.get(0)){
                                                            for (int x=0; x<selectNoneCheck.get(0); x++){
                                                                if(x!=finalJ){
                                                                    choiceTimeBtnContainer.getChildAt(x).setBackground(getDrawable(R.drawable.date_rounded));
                                                                    if(choiceTimeBtnContainer.getChildAt(x) instanceof Button) ((Button)choiceTimeBtnContainer.getChildAt(x)).setTextColor(R.color.mainPurple);
                                                                    selectCheck.clear();
                                                                    selectCheck.add(finalJ);
                                                                }
                                                            }
                                                        }else {
                                                            for (int x=choiceTimeBtnContainer.getChildCount()-1; x>selectNoneCheck.get(selectNoneCheck.size()-1); x--){
                                                                if(x!=finalJ){
                                                                    choiceTimeBtnContainer.getChildAt(x).setBackground(getDrawable(R.drawable.date_rounded));
                                                                    if(choiceTimeBtnContainer.getChildAt(x) instanceof Button) ((Button)choiceTimeBtnContainer.getChildAt(x)).setTextColor(R.color.mainPurple);
                                                                    selectCheck.clear();
                                                                    selectCheck.add(finalJ);
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        });
                                    }
                                }

                            }
                        }
                    }
                }else {
                    int StatusCode = response.code();
                    String s = response.message();
                    ResponseBody d = response.errorBody();
                    Log.i(ApplicationController.TAG, "홈 상태 Code : " + StatusCode);
                    Log.e("메세지", s);
                    Log.e("리스폰스에러바디", String.valueOf(d));
                    Log.e("리스폰스바디", String.valueOf(response.body()));
                }
            }

            @Override
            public void onFailure(retrofit2.Call<List<PracticeReservation>> call, Throwable t) {
                Log.i(ApplicationController.TAG, "서버 연결 실패 Message : " + t.getMessage());
            }
        });
    }

    public void next(int provide_id, int provide_option, String practice_date, String start_time, String end_time, int fee,
                     String p_image, String provide_name, String provide_address, String option_name, String team_name, String phone, String email){
        Intent checkReservation = new Intent(this, ReservationCheck.class);
        checkReservation.putExtra("rc_id",provide_id);
        checkReservation.putExtra("rc_image",p_image);
        checkReservation.putExtra("rc_name",provide_name);
        checkReservation.putExtra("rc_address",provide_address);
        checkReservation.putExtra("rc_option_id", provide_option);
        checkReservation.putExtra("rc_option_price", fee);
        checkReservation.putExtra("rc_option_name",option_name);
        checkReservation.putExtra("rc_practice_date",practice_date);
        checkReservation.putExtra("rc_start_time",start_time);
        checkReservation.putExtra("rc_end_time",end_time);
        checkReservation.putExtra("rc_team_name",team_name);
        checkReservation.putExtra("rc_phone",phone);
        checkReservation.putExtra("rc_email",email);
        startActivity(checkReservation);
    }



    public Bitmap getBitmapFromURL(String src) {
        HttpURLConnection connection = null;
        try {
            URL url = new URL(src);
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }finally{
            if(connection!=null)connection.disconnect();
        }
    }

    public void getLocalData(){
        SharedPreferences pref = getSharedPreferences("User", Activity.MODE_PRIVATE);
        SharedPreferences buskerPref = getSharedPreferences("BuskerUser", Activity.MODE_PRIVATE);
        user_token = pref.getString("auth_token",null);
        busker_id = buskerPref.getInt("busker_id",0);
        team_name = buskerPref.getString("team_name",null);
    }

    public void previousActivity(View v){
        onBackPressed();
    }

}