package graduationwork.buskingtown;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import graduationwork.buskingtown.api.RestApiService;
import graduationwork.buskingtown.model.RoadConcert;
import okhttp3.ResponseBody;
import retrofit2.Callback;
import retrofit2.Response;


public class BuskingOpen extends AppCompatActivity {
    String user_token;
    int busker_id;
    RestApiService apiService;
    int concertYear, concertMonth, concertDay, mHour, mMinute;
    SimpleDateFormat simpleDateFormat;
    RelativeLayout placeLayout;

    Button concertDateButton;
    Button timeStartDate;
    Button timeEndDate;

    TextView addressChoice;
    String location_name;
    String location_detail;
    String address_data;

    double lon, lat; //위도,경도 받아오기

    ArrayList<Integer> selectCheck = new ArrayList<>();
    ArrayList<Integer> selectNoneCheck = new ArrayList<>();
    ArrayList<Integer> ableCheck = new ArrayList<>();
    ArrayList<String> selectCheckTime = new ArrayList<>();
    int checkIndex;
    String checkTime;

    LinearLayout choiceTimeBtnContainer;

    Button confirmBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_busking_open);

        getLocalData();

        restApiBuilder();

        choiceTimeBtnContainer = (LinearLayout) findViewById(R.id.choiceTimeBtnContainer);

        ImageButton backBtn = (ImageButton) findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BuskingOpen.super.onBackPressed();
            }
        });

        //장소검색
        placeLayout = (RelativeLayout) findViewById(R.id.placesLayout);
        placeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent placeSearch = new Intent(getApplication(), RoadSearch.class);
                startActivity(placeSearch);
            }
        });

        addressChoice = (TextView) findViewById(R.id.addressChoice);

        location_name = getIntent().getStringExtra("location_name");
        location_detail = getIntent().getStringExtra("location_detail");
        lon = getIntent().getDoubleExtra("lon", lon);
        lat = getIntent().getDoubleExtra("lat", lat);


        if (location_name != null) {
            addressChoice.setText(location_name + " " + location_detail);
            address_data = location_detail.replaceAll(" ", "");
            address_data = location_detail.replaceAll("\\p{Z}", "");
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

        concertDateButton = (Button) findViewById(R.id.concertStartDate);
        concertDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(BuskingOpen.this, R.style.MyDatePickerDialogTheme, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int date) {
                        concertDateButton.setText(year + "년 " + (month + 1) + "월 " + date + "일");
                        //예약 정보 불러오기
                        choiceTimeBtnContainer.removeAllViews();
                        selectCheck.clear();
                        getReservation(user_token, busker_id, address_data, String.valueOf(year + "-" + (month + 1) + "-" + date));
                    }
                }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));
                dialog.getDatePicker().setMinDate(new Date().getTime());    //입력한 날짜 이후로 클릭 안되게 옵션
                dialog.show();


            }
        });

        confirmBtn = findViewById(R.id.confirmBtn);
    }


    public void previousActivity(View v) {
        onBackPressed();
    }


    public void getReservation(String user_token, int busker_id, String address, String concert_date) {
        ArrayList<Integer> start_reservation_time = new ArrayList<>();
        ArrayList<Integer> end_reservation_time = new ArrayList<>();
        retrofit2.Call<List<RoadConcert>> listCall = apiService.getReservationRoadConcert(user_token, address, concert_date);
        listCall.enqueue(new Callback<List<RoadConcert>>() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onResponse(retrofit2.Call<List<RoadConcert>> call, Response<List<RoadConcert>> response) {
                if (response.isSuccessful()) {
                    List<RoadConcert> roadReservations = response.body();
                    //길거리 공연 총 시간
                    int start_time = 9;
                    int end_time = 22;

                    //현재시간
                    long now = System.currentTimeMillis();
                    Date date = new Date(now);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    SimpleDateFormat stf = new SimpleDateFormat("HH");
                    String getDate = sdf.format(date);
                    int getTime = Integer.parseInt(stf.format(date));

                    if (roadReservations.size() == 0 && !concert_date.equals(getDate)) {
                        for (int start = start_time; start <= end_time - 1; start += 1) {
                            Button choiceTimeList = (Button) getLayoutInflater().inflate(R.layout.choice_time_btn, choiceTimeBtnContainer, false);
                            choiceTimeList.setText(start + ":00");
                            if (choiceTimeList.getParent() != null)
                                ((ViewGroup) choiceTimeList.getParent()).removeView(choiceTimeList);
                            choiceTimeBtnContainer.addView(choiceTimeList);
                        }
                        for (int j = 0; j < choiceTimeBtnContainer.getChildCount(); j++) {
                            View v = choiceTimeBtnContainer.getChildAt(j);
                            int finalJ = j;
                            v.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View button) {
                                    v.setSelected(!button.isSelected());
                                    if (button.isSelected()) {//선택하고 난 후
                                        v.setBackground(getDrawable(R.drawable.able_btn));
                                        if (v instanceof Button)
                                            ((Button) v).setTextColor(Color.WHITE);
                                        selectCheck.add(finalJ);
                                        if (selectCheck.size() == 2 && selectCheck.get(0) < finalJ) {
                                            selectCheckTime.clear();
                                            for (int x = selectCheck.get(0); x < finalJ; x++) {
                                                choiceTimeBtnContainer.getChildAt(x).setBackground(getDrawable(R.drawable.able_btn));
                                                if (choiceTimeBtnContainer.getChildAt(x) instanceof Button) {
                                                    ((Button) choiceTimeBtnContainer.getChildAt(x)).setTextColor(Color.WHITE);
                                                    checkTime = String.valueOf(((Button) choiceTimeBtnContainer.getChildAt(x)).getText());
                                                }
                                                selectCheckTime.add(checkTime);
                                            }
                                            selectCheckTime.add(String.valueOf(((Button) choiceTimeBtnContainer.getChildAt(finalJ)).getText()));
                                            Log.e("두번 체크", String.valueOf(selectCheckTime));
                                            selectCheck.clear();

                                            //끝나는 시간
                                            String[] p_end_time_words = String.valueOf(((Button) choiceTimeBtnContainer.getChildAt(finalJ)).getText()).split(":");
                                            int end_time = Integer.parseInt(p_end_time_words[0]) + 1;
                                            String real_end_time = String.valueOf(end_time) + ":00";

                                            confirmBtn.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    confirm(user_token, concert_date, selectCheckTime.get(0), real_end_time, location_name, address, lon, lat);
                                                }
                                            });

                                        } else if (selectCheck.get(0) > finalJ || selectCheck.size() == 1) {
                                            selectCheckTime.clear();
                                            v.setBackground(getDrawable(R.drawable.able_btn));
                                            if (v instanceof Button) {
                                                ((Button) v).setTextColor(Color.WHITE);
                                                checkTime = String.valueOf(((Button) v).getText());
                                            }
                                            selectCheckTime.add(checkTime);
                                            for (int x = 0; x < choiceTimeBtnContainer.getChildCount(); x++) {
                                                if (x != finalJ) {
                                                    choiceTimeBtnContainer.getChildAt(x).setBackground(getDrawable(R.drawable.date_rounded));
                                                    if (choiceTimeBtnContainer.getChildAt(x) instanceof Button)
                                                        ((Button) choiceTimeBtnContainer.getChildAt(x)).setTextColor(R.color.mainPurple);
                                                    selectCheck.clear();
                                                    selectCheck.add(finalJ);
                                                }
                                            }
                                            //끝나는 시간
                                            String[] p_end_time_words = selectCheckTime.get(0).split(":");
                                            int end_time = Integer.parseInt(p_end_time_words[0]) + 1;
                                            String real_end_time = String.valueOf(end_time) + ":00";

                                            confirmBtn.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    confirm(user_token, concert_date, selectCheckTime.get(0), real_end_time, location_name, address, lon, lat);
                                                }
                                            });
                                        }
                                    } else {
                                        v.setBackground(getDrawable(R.drawable.able_btn));
                                        if (v instanceof Button)
                                            ((Button) v).setTextColor(Color.WHITE);
                                        selectCheck.add(finalJ);
                                        if (selectCheck.size() == 2 && selectCheck.get(0) < finalJ) {
                                            selectCheckTime.clear();
                                            for (int x = selectCheck.get(0); x < finalJ; x++) {
                                                choiceTimeBtnContainer.getChildAt(x).setBackground(getDrawable(R.drawable.able_btn));
                                                if (choiceTimeBtnContainer.getChildAt(x) instanceof Button) {
                                                    ((Button) choiceTimeBtnContainer.getChildAt(x)).setTextColor(Color.WHITE);
                                                    checkTime = String.valueOf(((Button) choiceTimeBtnContainer.getChildAt(x)).getText());
                                                }
                                                selectCheckTime.add(checkTime);
                                            }
                                            selectCheckTime.add(String.valueOf(((Button) choiceTimeBtnContainer.getChildAt(finalJ)).getText()));
                                            selectCheck.clear();

                                            //끝나는 시간
                                            String[] p_end_time_words = String.valueOf(((Button) choiceTimeBtnContainer.getChildAt(finalJ)).getText()).split(":");
                                            int end_time = Integer.parseInt(p_end_time_words[0]) + 1;
                                            String real_end_time = String.valueOf(end_time) + ":00";

                                            confirmBtn.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    confirm(user_token, concert_date, selectCheckTime.get(0), real_end_time, location_name, address, lon, lat);
                                                }
                                            });

                                        } else if (selectCheck.get(0) > finalJ || selectCheck.size() == 1) {
                                            selectCheckTime.clear();
                                            v.setBackground(getDrawable(R.drawable.able_btn));
                                            if (v instanceof Button) {
                                                ((Button) v).setTextColor(Color.WHITE);
                                                checkTime = String.valueOf(((Button) v).getText());
                                            }
                                            selectCheckTime.add(checkTime);

                                            //끝나는 시간
                                            String[] p_end_time_words = String.valueOf(((Button) choiceTimeBtnContainer.getChildAt(finalJ)).getText()).split(":");
                                            int end_time = Integer.parseInt(p_end_time_words[0]) + 1;
                                            String real_end_time = String.valueOf(end_time) + ":00";

                                            confirmBtn.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    confirm(user_token, concert_date, selectCheckTime.get(0), real_end_time, location_name, address, lon, lat);
                                                }
                                            });


                                            for (int x = 0; x < choiceTimeBtnContainer.getChildCount(); x++) {
                                                if (x != finalJ) {
                                                    choiceTimeBtnContainer.getChildAt(x).setBackground(getDrawable(R.drawable.date_rounded));
                                                    if (choiceTimeBtnContainer.getChildAt(x) instanceof Button)
                                                        ((Button) choiceTimeBtnContainer.getChildAt(x)).setTextColor(R.color.mainPurple);
                                                    selectCheck.clear();
                                                    selectCheck.add(finalJ);
                                                }
                                            }
                                        }
                                    }
                                }
                            });
                        }
                    } else if (roadReservations.size() == 0 && concert_date.equals(getDate)) {
                        selectNoneCheck.clear();
                        //예약 된 것 없으나 오늘 날짜 일때
                        //버튼 그리기
                        for (int start = start_time; start <= end_time - 1; start += 1) {
                            Button choiceTimeList = (Button) getLayoutInflater().inflate(R.layout.choice_time_btn, choiceTimeBtnContainer, false);
                            choiceTimeList.setText(start + ":00");
                            if (choiceTimeList.getParent() != null)
                                ((ViewGroup) choiceTimeList.getParent()).removeView(choiceTimeList);
                            choiceTimeBtnContainer.addView(choiceTimeList);
                        }
                        for (int j = 0; j < choiceTimeBtnContainer.getChildCount(); j++) {
                            View v = choiceTimeBtnContainer.getChildAt(j);
                            String[] c_time = new String[0];
                            if (v instanceof Button) {
                                c_time = String.valueOf(((Button) v).getText()).split(":");
                            }
                            for (int end = getTime; end >= start_time; end--) {
                                if (Integer.parseInt(c_time[0]) == end) {
                                    if (!selectNoneCheck.contains(j)) {
                                        selectNoneCheck.add(j);
                                    }
                                }
                            }

                            if (!selectNoneCheck.contains(j)) {
                                int finalJ = j;
                                v.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View button) {
                                        v.setSelected(!button.isSelected());
                                        if (button.isSelected()) {//선택하고 난 후
                                            v.setBackground(getDrawable(R.drawable.able_btn));
                                            if (v instanceof Button)
                                                ((Button) v).setTextColor(Color.WHITE);
                                            selectCheck.add(finalJ);

                                            //두개 연속 체크
                                            if (selectCheck.size() == 2 && selectCheck.get(0) < finalJ && !selectNoneCheck.contains(finalJ - 1)) {
                                                selectCheckTime.clear();
                                                for (int x = selectCheck.get(0); x < finalJ; x++) {
                                                    if (!selectNoneCheck.contains(x)) {
                                                        choiceTimeBtnContainer.getChildAt(x).setBackground(getDrawable(R.drawable.able_btn));
                                                        if (choiceTimeBtnContainer.getChildAt(x) instanceof Button) {
                                                            ((Button) choiceTimeBtnContainer.getChildAt(x)).setTextColor(Color.WHITE);
                                                            checkTime = String.valueOf(((Button) choiceTimeBtnContainer.getChildAt(x)).getText());
                                                        }
                                                        selectCheckTime.add(checkTime);
                                                    }
                                                }
                                                selectCheckTime.add(String.valueOf(((Button) choiceTimeBtnContainer.getChildAt(finalJ)).getText()));
                                                selectCheck.clear();

                                                //끝나는 시간
                                                String[] p_end_time_words = String.valueOf(((Button) choiceTimeBtnContainer.getChildAt(finalJ)).getText()).split(":");
                                                int end_time = Integer.parseInt(p_end_time_words[0]) + 1;
                                                String real_end_time = String.valueOf(end_time) + ":00";

                                                confirmBtn.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        confirm(user_token, concert_date, selectCheckTime.get(0), real_end_time, location_name, address, lon, lat);
                                                    }
                                                });

                                            } else if (selectCheck.get(0) > finalJ || selectCheck.size() == 1) {
                                                selectCheckTime.clear();
                                                v.setBackground(getDrawable(R.drawable.able_btn));
                                                if (v instanceof Button) {
                                                    ((Button) v).setTextColor(Color.WHITE);
                                                    checkTime = String.valueOf(((Button) v).getText());
                                                }
                                                selectCheckTime.add(checkTime);

                                                //끝나는 시간
                                                String[] p_end_time_words = String.valueOf(((Button) choiceTimeBtnContainer.getChildAt(finalJ)).getText()).split(":");
                                                int end_time = Integer.parseInt(p_end_time_words[0]) + 1;
                                                String real_end_time = String.valueOf(end_time) + ":00";

                                                confirmBtn.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        confirm(user_token, concert_date, selectCheckTime.get(0), real_end_time, location_name, address, lon, lat);
                                                    }
                                                });

                                                for (int x = 0; x < ableCheck.size(); x++) {
                                                    if (ableCheck.get(x) != finalJ) {
                                                        choiceTimeBtnContainer.getChildAt(ableCheck.get(x)).setBackground(getDrawable(R.drawable.date_rounded));
                                                        if (choiceTimeBtnContainer.getChildAt(ableCheck.get(x)) instanceof Button)
                                                            ((Button) choiceTimeBtnContainer.getChildAt(ableCheck.get(x))).setTextColor(R.color.mainPurple);
                                                        selectCheck.clear();
                                                        selectCheck.add(finalJ);
                                                    }
                                                }
                                            }

                                        } else {
                                            v.setBackground(getDrawable(R.drawable.able_btn));
                                            if (v instanceof Button)
                                                ((Button) v).setTextColor(Color.WHITE);
                                            selectCheck.add(finalJ);
                                            if (selectCheck.size() == 2 && selectCheck.get(0) < finalJ) {
                                                selectCheckTime.clear();
                                                for (int x = selectCheck.get(0); x < finalJ; x++) {
                                                    if (!selectNoneCheck.contains(x)) {
                                                        choiceTimeBtnContainer.getChildAt(x).setBackground(getDrawable(R.drawable.able_btn));
                                                        if (choiceTimeBtnContainer.getChildAt(x) instanceof Button) {
                                                            ((Button) choiceTimeBtnContainer.getChildAt(x)).setTextColor(Color.WHITE);
                                                            checkTime = String.valueOf(((Button) choiceTimeBtnContainer.getChildAt(x)).getText());
                                                        }
                                                        selectCheckTime.add(checkTime);
                                                    }
                                                }
                                                selectCheckTime.add(String.valueOf(((Button) choiceTimeBtnContainer.getChildAt(finalJ)).getText()));
                                                selectCheck.clear();

                                                //끝나는 시간
                                                String[] p_end_time_words = String.valueOf(((Button) choiceTimeBtnContainer.getChildAt(finalJ)).getText()).split(":");
                                                int end_time = Integer.parseInt(p_end_time_words[0]) + 1;
                                                String real_end_time = String.valueOf(end_time) + ":00";

                                                confirmBtn.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        confirm(user_token, concert_date, selectCheckTime.get(0), real_end_time, location_name, address, lon, lat);
                                                    }
                                                });

                                            } else if (selectCheck.get(0) > finalJ || selectCheck.size() == 1) {
                                                selectCheckTime.clear();
                                                v.setBackground(getDrawable(R.drawable.able_btn));
                                                if (v instanceof Button) {
                                                    ((Button) v).setTextColor(Color.WHITE);
                                                    checkTime = String.valueOf(((Button) v).getText());
                                                }
                                                selectCheckTime.add(checkTime);

                                                //끝나는 시간
                                                String[] p_end_time_words = String.valueOf(((Button) choiceTimeBtnContainer.getChildAt(finalJ)).getText()).split(":");
                                                int end_time = Integer.parseInt(p_end_time_words[0]) + 1;
                                                String real_end_time = String.valueOf(end_time) + ":00";

                                                confirmBtn.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        confirm(user_token, concert_date, selectCheckTime.get(0), real_end_time, location_name, address, lon, lat);
                                                    }
                                                });


                                                for (int x = 0; x < ableCheck.size(); x++) {
                                                    if (ableCheck.get(x) != finalJ) {
                                                        choiceTimeBtnContainer.getChildAt(ableCheck.get(x)).setBackground(getDrawable(R.drawable.date_rounded));
                                                        if (choiceTimeBtnContainer.getChildAt(ableCheck.get(x)) instanceof Button)
                                                            ((Button) choiceTimeBtnContainer.getChildAt(ableCheck.get(x))).setTextColor(R.color.mainPurple);
                                                        selectCheck.clear();
                                                        selectCheck.add(finalJ);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                });
                            } else {
                                v.setOnClickListener(null);
                                v.setBackground(getDrawable(R.drawable.disable_btn));
                                if (v instanceof Button) ((Button) v).setTextColor(Color.WHITE);
                            }

                        }
                    } else if (concert_date.equals(getDate) && roadReservations.size() != 0) {
                        selectNoneCheck.clear();
                        for (int start = start_time; start <= end_time - 1; start += 1) {
                            Button choiceTimeList = (Button) getLayoutInflater().inflate(R.layout.choice_time_btn, choiceTimeBtnContainer, false);
                            choiceTimeList.setText(start + ":00");
                            if (choiceTimeList.getParent() != null)
                                ((ViewGroup) choiceTimeList.getParent()).removeView(choiceTimeList);
                            choiceTimeBtnContainer.addView(choiceTimeList);
                        }
                        for (int j = 0; j < choiceTimeBtnContainer.getChildCount(); j++) {
                            View v = choiceTimeBtnContainer.getChildAt(j);
                            if (!ableCheck.contains(j)) {
                                ableCheck.add(j);
                            }
                            String[] c_time = new String[0];
                            if (v instanceof Button) {
                                c_time = String.valueOf(((Button) v).getText()).split(":");
                            }
                            for (int end = getTime; end >= start_time; end--) {
                                if (Integer.parseInt(c_time[0]) == end) {
                                    if (!selectNoneCheck.contains(j)) {
                                        selectNoneCheck.add(j);
                                    }
                                }
                            }
                            for (int i = 0; i < roadReservations.size(); i++) {
                                String reservation_start_time = roadReservations.get(i).getRoad_concert_start_time();
                                String reservation_end_time = roadReservations.get(i).getRoad_concert_end_time();
                                String[] start_reservation_words = reservation_start_time.split(":");
                                String[] end_reservation_words = reservation_end_time.split(":");

                                start_reservation_time.add(Integer.parseInt(start_reservation_words[0]));
                                end_reservation_time.add(Integer.parseInt(end_reservation_words[0]));


                                for (int end = end_reservation_time.get(i) - 1; end >= start_reservation_time.get(i); end -= 1) {
                                    if (Integer.parseInt(c_time[0]) == end) {
                                        if (!selectNoneCheck.contains(j)) {
                                            selectNoneCheck.add(j);
                                        }
                                    }
                                }
                            }
                            ableCheck.removeAll(selectNoneCheck);

                            if (!selectNoneCheck.contains(j)) {
                                int finalJ = j;
                                v.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View button) {
                                        v.setSelected(!button.isSelected());
                                        if (button.isSelected()) {//선택하고 난 후
                                            v.setBackground(getDrawable(R.drawable.able_btn));
                                            if (v instanceof Button)
                                                ((Button) v).setTextColor(Color.WHITE);
                                            selectCheck.add(finalJ);
                                            //두개 연속 체크
                                            for (int listcheck = finalJ; listcheck > selectCheck.get(0); listcheck--) {
                                                if (selectNoneCheck.contains(listcheck)) {
                                                    checkIndex = listcheck;
                                                }
                                            }
                                            if (selectCheck.size() == 2 && selectCheck.get(0) < finalJ && !(selectCheck.get(0) < checkIndex && checkIndex < finalJ)) {
                                                selectCheckTime.clear();
                                                for (int x = selectCheck.get(0); x < finalJ; x++) {
                                                    if (!selectNoneCheck.contains(x)) {
                                                        choiceTimeBtnContainer.getChildAt(x).setBackground(getDrawable(R.drawable.able_btn));
                                                        if (choiceTimeBtnContainer.getChildAt(x) instanceof Button) {
                                                            ((Button) choiceTimeBtnContainer.getChildAt(x)).setTextColor(Color.WHITE);
                                                            checkTime = String.valueOf(((Button) choiceTimeBtnContainer.getChildAt(x)).getText());
                                                        }
                                                        selectCheckTime.add(checkTime);
                                                    }
                                                }
                                                selectCheckTime.add(String.valueOf(((Button) choiceTimeBtnContainer.getChildAt(finalJ)).getText()));
                                                selectCheck.clear();

                                                //끝나는 시간
                                                String[] p_end_time_words = String.valueOf(((Button) choiceTimeBtnContainer.getChildAt(finalJ)).getText()).split(":");
                                                int end_time = Integer.parseInt(p_end_time_words[0]) + 1;
                                                String real_end_time = String.valueOf(end_time) + ":00";

                                                confirmBtn.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        confirm(user_token, concert_date, selectCheckTime.get(0), real_end_time, location_name, address, lon, lat);
                                                    }
                                                });

                                            } else if (selectCheck.get(0) > finalJ || selectCheck.size() == 1 || (selectCheck.get(0) < checkIndex && checkIndex < finalJ)) {
                                                selectCheckTime.clear();
                                                v.setBackground(getDrawable(R.drawable.able_btn));
                                                if (v instanceof Button) {
                                                    ((Button) v).setTextColor(Color.WHITE);
                                                    checkTime = String.valueOf(((Button) v).getText());
                                                }
                                                selectCheckTime.add(checkTime);

                                                //끝나는 시간
                                                String[] p_end_time_words = String.valueOf(((Button) choiceTimeBtnContainer.getChildAt(finalJ)).getText()).split(":");
                                                int end_time = Integer.parseInt(p_end_time_words[0]) + 1;
                                                String real_end_time = String.valueOf(end_time) + ":00";

                                                confirmBtn.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        confirm(user_token, concert_date, selectCheckTime.get(0), real_end_time, location_name, address, lon, lat);
                                                    }
                                                });

                                                for (int x = 0; x < ableCheck.size(); x++) {
                                                    if (ableCheck.get(x) != finalJ) {
                                                        choiceTimeBtnContainer.getChildAt(ableCheck.get(x)).setBackground(getDrawable(R.drawable.date_rounded));
                                                        if (choiceTimeBtnContainer.getChildAt(ableCheck.get(x)) instanceof Button)
                                                            ((Button) choiceTimeBtnContainer.getChildAt(ableCheck.get(x))).setTextColor(R.color.mainPurple);
                                                        selectCheck.clear();
                                                        selectCheck.add(finalJ);
                                                    }
                                                }
                                            }

                                        } else {
                                            v.setBackground(getDrawable(R.drawable.able_btn));
                                            if (v instanceof Button)
                                                ((Button) v).setTextColor(Color.WHITE);
                                            selectCheck.add(finalJ);
                                            for (int listcheck = finalJ; listcheck > selectCheck.get(0); listcheck--) {
                                                if (selectNoneCheck.contains(listcheck)) {
                                                    checkIndex = listcheck;
                                                }
                                            }
                                            if (selectCheck.size() == 2 && selectCheck.get(0) < finalJ && !(selectCheck.get(0) < checkIndex && checkIndex < finalJ)) {
                                                selectCheckTime.clear();
                                                for (int x = selectCheck.get(0); x < finalJ; x++) {
                                                    if (!selectNoneCheck.contains(x)) {
                                                        choiceTimeBtnContainer.getChildAt(x).setBackground(getDrawable(R.drawable.able_btn));
                                                        if (choiceTimeBtnContainer.getChildAt(x) instanceof Button) {
                                                            ((Button) choiceTimeBtnContainer.getChildAt(x)).setTextColor(Color.WHITE);
                                                            checkTime = String.valueOf(((Button) choiceTimeBtnContainer.getChildAt(x)).getText());
                                                        }
                                                    }
                                                    selectCheckTime.add(checkTime);
                                                }
                                                selectCheckTime.add(String.valueOf(((Button) choiceTimeBtnContainer.getChildAt(finalJ)).getText()));
                                                selectCheck.clear();

                                                //끝나는 시간
                                                String[] p_end_time_words = String.valueOf(((Button) choiceTimeBtnContainer.getChildAt(finalJ)).getText()).split(":");
                                                int end_time = Integer.parseInt(p_end_time_words[0]) + 1;
                                                String real_end_time = String.valueOf(end_time) + ":00";

                                                confirmBtn.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        confirm(user_token, concert_date, selectCheckTime.get(0), real_end_time, location_name, address, lon, lat);
                                                    }
                                                });

                                            } else if (selectCheck.get(0) > finalJ || selectCheck.size() == 1 || (selectCheck.get(0) < checkIndex && checkIndex < finalJ)) {
                                                selectCheckTime.clear();
                                                v.setBackground(getDrawable(R.drawable.able_btn));
                                                if (v instanceof Button) {
                                                    ((Button) v).setTextColor(Color.WHITE);
                                                    checkTime = String.valueOf(((Button) v).getText());
                                                }
                                                selectCheckTime.add(checkTime);

                                                //끝나는 시간
                                                String[] p_end_time_words = String.valueOf(((Button) choiceTimeBtnContainer.getChildAt(finalJ)).getText()).split(":");
                                                int end_time = Integer.parseInt(p_end_time_words[0]) + 1;
                                                String real_end_time = String.valueOf(end_time) + ":00";

                                                confirmBtn.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        confirm(user_token, concert_date, selectCheckTime.get(0), real_end_time, location_name, address, lon, lat);
                                                    }
                                                });

                                                for (int x = 0; x < ableCheck.size(); x++) {
                                                    if (ableCheck.get(x) != finalJ) {
                                                        choiceTimeBtnContainer.getChildAt(ableCheck.get(x)).setBackground(getDrawable(R.drawable.date_rounded));
                                                        if (choiceTimeBtnContainer.getChildAt(ableCheck.get(x)) instanceof Button)
                                                            ((Button) choiceTimeBtnContainer.getChildAt(ableCheck.get(x))).setTextColor(R.color.mainPurple);
                                                        selectCheck.clear();
                                                        selectCheck.add(finalJ);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                });
                            } else {
                                v.setOnClickListener(null);
                                v.setBackground(getDrawable(R.drawable.disable_btn));
                                if (v instanceof Button) ((Button) v).setTextColor(Color.WHITE);
                            }

                        }

                    } else if (!concert_date.equals(getDate) && roadReservations.size() != 0) {
                        selectNoneCheck.clear();
                        //오늘 날짜는 아니나 예약 목록이 있을 때
                        for (int start = start_time; start <= end_time - 1; start += 1) {
                            Button choiceTimeList = (Button) getLayoutInflater().inflate(R.layout.choice_time_btn, choiceTimeBtnContainer, false);
                            choiceTimeList.setText(start + ":00");
                            if (choiceTimeList.getParent() != null)
                                ((ViewGroup) choiceTimeList.getParent()).removeView(choiceTimeList);
                            choiceTimeBtnContainer.addView(choiceTimeList);
                        }
                        for (int j = 0; j < choiceTimeBtnContainer.getChildCount(); j++) {
                            View v = choiceTimeBtnContainer.getChildAt(j);
                            if (!ableCheck.contains(j)) {
                                ableCheck.add(j);
                            }
                            String[] c_time = new String[0];
                            if (v instanceof Button) {
                                c_time = String.valueOf(((Button) v).getText()).split(":");
                            }
                            for (int i = 0; i < roadReservations.size(); i++) {
                                String reservation_start_time = roadReservations.get(i).getRoad_concert_start_time();
                                String reservation_end_time = roadReservations.get(i).getRoad_concert_end_time();
                                String[] start_reservation_words = reservation_start_time.split(":");
                                String[] end_reservation_words = reservation_end_time.split(":");

                                start_reservation_time.add(Integer.parseInt(start_reservation_words[0]));
                                end_reservation_time.add(Integer.parseInt(end_reservation_words[0]));

                                for (int end = end_reservation_time.get(i) - 1; end >= start_reservation_time.get(i); end -= 1) {
                                    if (Integer.parseInt(c_time[0]) == end) {
                                        if (!selectNoneCheck.contains(j)) {
                                            selectNoneCheck.add(j);
                                        }
                                    }
                                }
                            }
                            ableCheck.removeAll(selectNoneCheck);

                            if (!selectNoneCheck.contains(j)) {
                                int finalJ = j;
                                v.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View button) {
                                        v.setSelected(!button.isSelected());
                                        if (button.isSelected()) {//선택하고 난 후
                                            v.setBackground(getDrawable(R.drawable.able_btn));
                                            if (v instanceof Button)
                                                ((Button) v).setTextColor(Color.WHITE);
                                            selectCheck.add(finalJ);
                                            //두개 연속 체크
                                            for (int listcheck = finalJ; listcheck > selectCheck.get(0); listcheck--) {
                                                if (selectNoneCheck.contains(listcheck)) {
                                                    checkIndex = listcheck;
                                                }
                                            }
                                            if (selectCheck.size() == 2 && selectCheck.get(0) < finalJ && !(selectCheck.get(0) < checkIndex && checkIndex < finalJ)) {
                                                selectCheckTime.clear();
                                                for (int x = selectCheck.get(0); x < finalJ; x++) {
                                                    if (!selectNoneCheck.contains(x)) {
                                                        choiceTimeBtnContainer.getChildAt(x).setBackground(getDrawable(R.drawable.able_btn));
                                                        if (choiceTimeBtnContainer.getChildAt(x) instanceof Button) {
                                                            ((Button) choiceTimeBtnContainer.getChildAt(x)).setTextColor(Color.WHITE);
                                                            checkTime = String.valueOf(((Button) choiceTimeBtnContainer.getChildAt(x)).getText());
                                                        }
                                                        selectCheckTime.add(checkTime);
                                                    }
                                                }
                                                selectCheckTime.add(String.valueOf(((Button) choiceTimeBtnContainer.getChildAt(finalJ)).getText()));
                                                selectCheck.clear();

                                                //끝나는 시간
                                                String[] p_end_time_words = String.valueOf(((Button) choiceTimeBtnContainer.getChildAt(finalJ)).getText()).split(":");
                                                int end_time = Integer.parseInt(p_end_time_words[0]) + 1;
                                                String real_end_time = String.valueOf(end_time) + ":00";

                                                confirmBtn.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        confirm(user_token, concert_date, selectCheckTime.get(0), real_end_time, location_name, address, lon, lat);
                                                    }
                                                });

                                            } else if (selectCheck.get(0) > finalJ || selectCheck.size() == 1 || (selectCheck.get(0) < checkIndex && checkIndex < finalJ)) {
                                                selectCheckTime.clear();
                                                v.setBackground(getDrawable(R.drawable.able_btn));
                                                if (v instanceof Button) {
                                                    ((Button) v).setTextColor(Color.WHITE);
                                                    checkTime = String.valueOf(((Button) v).getText());
                                                }
                                                selectCheckTime.add(checkTime);

                                                //끝나는 시간
                                                String[] p_end_time_words = String.valueOf(((Button) choiceTimeBtnContainer.getChildAt(finalJ)).getText()).split(":");
                                                int end_time = Integer.parseInt(p_end_time_words[0]) + 1;
                                                String real_end_time = String.valueOf(end_time) + ":00";

                                                confirmBtn.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        confirm(user_token, concert_date, selectCheckTime.get(0), real_end_time, location_name, address, lon, lat);
                                                    }
                                                });

                                                for (int x = 0; x < ableCheck.size(); x++) {
                                                    if (ableCheck.get(x) != finalJ) {
                                                        choiceTimeBtnContainer.getChildAt(ableCheck.get(x)).setBackground(getDrawable(R.drawable.date_rounded));
                                                        if (choiceTimeBtnContainer.getChildAt(ableCheck.get(x)) instanceof Button)
                                                            ((Button) choiceTimeBtnContainer.getChildAt(ableCheck.get(x))).setTextColor(R.color.mainPurple);
                                                        selectCheck.clear();
                                                        selectCheck.add(finalJ);
                                                    }
                                                }
                                            }

                                        } else {// 이미 한번 셀렉한거
                                            v.setBackground(getDrawable(R.drawable.able_btn));
                                            if (v instanceof Button)
                                                ((Button) v).setTextColor(Color.WHITE);
                                            selectCheck.add(finalJ);
                                            for (int listcheck = finalJ; listcheck > selectCheck.get(0); listcheck--) {
                                                if (selectNoneCheck.contains(listcheck)) {
                                                    checkIndex = listcheck;
                                                }
                                            }
                                            if (selectCheck.size() == 2 && selectCheck.get(0) < finalJ && !(selectCheck.get(0) < checkIndex && checkIndex < finalJ)) {
                                                selectCheckTime.clear();
                                                for (int x = selectCheck.get(0); x < finalJ; x++) {
                                                    if (!selectNoneCheck.contains(x)) {
                                                        choiceTimeBtnContainer.getChildAt(x).setBackground(getDrawable(R.drawable.able_btn));
                                                        if (choiceTimeBtnContainer.getChildAt(x) instanceof Button) {
                                                            ((Button) choiceTimeBtnContainer.getChildAt(x)).setTextColor(Color.WHITE);
                                                            checkTime = String.valueOf(((Button) choiceTimeBtnContainer.getChildAt(x)).getText());
                                                        }
                                                    }
                                                    selectCheckTime.add(checkTime);
                                                }
                                                selectCheckTime.add(String.valueOf(((Button) choiceTimeBtnContainer.getChildAt(finalJ)).getText()));
                                                selectCheck.clear();

                                                String[] p_end_time_words = String.valueOf(((Button) choiceTimeBtnContainer.getChildAt(finalJ)).getText()).split(":");
                                                int end_time = Integer.parseInt(p_end_time_words[0]) + 1;
                                                String real_end_time = String.valueOf(end_time) + ":00";

                                                confirmBtn.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        confirm(user_token, concert_date, selectCheckTime.get(0), real_end_time, location_name, address, lon, lat);
                                                    }
                                                });

                                            } else if (selectCheck.get(0) > finalJ || selectCheck.size() == 1 || (selectCheck.get(0) < checkIndex && checkIndex < finalJ)) {
                                                selectCheckTime.clear();
                                                v.setBackground(getDrawable(R.drawable.able_btn));
                                                if (v instanceof Button) {
                                                    ((Button) v).setTextColor(Color.WHITE);
                                                    checkTime = String.valueOf(((Button) v).getText());
                                                }
                                                selectCheckTime.add(checkTime);

                                                //끝나는 시간
                                                String[] p_end_time_words = String.valueOf(((Button) choiceTimeBtnContainer.getChildAt(finalJ)).getText()).split(":");
                                                int end_time = Integer.parseInt(p_end_time_words[0]) + 1;
                                                String real_end_time = String.valueOf(end_time) + ":00";

                                                confirmBtn.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        confirm(user_token, concert_date, selectCheckTime.get(0), real_end_time, location_name, address, lon, lat);
                                                    }
                                                });


                                                for (int x = 0; x < ableCheck.size(); x++) {
                                                    if (ableCheck.get(x) != finalJ) {
                                                        choiceTimeBtnContainer.getChildAt(ableCheck.get(x)).setBackground(getDrawable(R.drawable.date_rounded));
                                                        if (choiceTimeBtnContainer.getChildAt(ableCheck.get(x)) instanceof Button)
                                                            ((Button) choiceTimeBtnContainer.getChildAt(ableCheck.get(x))).setTextColor(R.color.mainPurple);
                                                        selectCheck.clear();
                                                        selectCheck.add(finalJ);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                });
                            } else {
                                v.setOnClickListener(null);
                                v.setBackground(getDrawable(R.drawable.disable_btn));
                                if (v instanceof Button) ((Button) v).setTextColor(Color.WHITE);
                            }

                        }
                    }
                } else {
                    int StatusCode = response.code();
                    String s = response.message();
                    ResponseBody d = response.errorBody();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<List<RoadConcert>> call, Throwable t) {
            }
        });
    }

    public void confirm(String user_token, String concert_date, String start_time, String end_time, String location_name, String location_address, double lon, double lat) {
        RoadConcert roadConcert = new RoadConcert();
        roadConcert.setBusker(busker_id);
        roadConcert.setRoad_concert_date(concert_date);
        roadConcert.setRoad_concert_start_time(start_time);
        roadConcert.setRoad_concert_end_time(end_time);
        roadConcert.setRoad_name(location_name);
        roadConcert.setRoad_address(location_address);
        roadConcert.setRoad_lon(lon);
        roadConcert.setRoad_lat(lat);
        retrofit2.Call<RoadConcert> roadConcertCall = apiService.reservationRoadConcert(user_token, roadConcert);
        roadConcertCall.enqueue(new Callback<RoadConcert>() {
            @Override
            public void onResponse(retrofit2.Call<RoadConcert> call, Response<RoadConcert> response) {
                if (response.isSuccessful()) {
                    Intent checkReservation = new Intent(getApplicationContext(), ChannelBusker.class);
                    startActivity(checkReservation);
                } else {
                    int StatusCode = response.code();
                    Log.i(ApplicationController.TAG, "홈 상태 Code : " + StatusCode);
                }
            }

            @Override
            public void onFailure(retrofit2.Call<RoadConcert> call, Throwable t) {
                Log.i(ApplicationController.TAG, "서버 연결 실패 Message : " + t.getMessage());
            }
        });
    }

    public void getLocalData() {
        SharedPreferences pref = getSharedPreferences("User", Activity.MODE_PRIVATE);
        SharedPreferences buskerPref = getSharedPreferences("BuskerUser", Activity.MODE_PRIVATE);
        user_token = pref.getString("auth_token", null);
        busker_id = buskerPref.getInt("busker_id", 0);
    }

    public void restApiBuilder() {
        ApplicationController application = ApplicationController.getInstance();
        application.buildNetworkService();
        apiService = ApplicationController.getInstance().getRestApiService();
    }

}
