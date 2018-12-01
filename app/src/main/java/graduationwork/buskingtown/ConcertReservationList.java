package graduationwork.buskingtown;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import graduationwork.buskingtown.api.RestApiService;
import graduationwork.buskingtown.model.LendLocation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConcertReservationList extends AppCompatActivity {

    String user_token, user_name;
    int busker_id;
    RestApiService apiService;

    LinearLayout reservation_container;

    TextView notice_reservation;
    View reservation_list;

    ListView listView;
    ArrayList<ConcertListItem> listItems;
    ConcertListCustom mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_concert_reservation_list);

        restApiBuilder();
        getLocalData();

        ImageButton backBtn = (ImageButton) findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConcertReservationList.super.onBackPressed();
            }
        });

        notice_reservation = findViewById(R.id.reservationText);

        listView = (ListView) findViewById(R.id.reservationContainer);
        listItems = new ArrayList<ConcertListItem>();
        mAdapter = new ConcertListCustom(listItems);

        Call<List<graduationwork.buskingtown.model.ConcertReservation>> reservationCall = apiService.reservationBuskerConcertCheck(user_token, busker_id);
        reservationCall.enqueue(new Callback<List<graduationwork.buskingtown.model.ConcertReservation>>() {
            @Override
            public void onResponse(Call<List<graduationwork.buskingtown.model.ConcertReservation>> call, Response<List<graduationwork.buskingtown.model.ConcertReservation>> response) {
                if (response.isSuccessful()) {
                    List<graduationwork.buskingtown.model.ConcertReservation> practiceReservation = response.body();
                    if (practiceReservation.size() != 0) {
                        notice_reservation.setVisibility(View.GONE);
                        for (int i = 0; i < practiceReservation.size(); i++) {
                            listItems.add(new ConcertListItem(practiceReservation.get(i).getReservation_id(),
                                    practiceReservation.get(i).getConcert_date(),
                                    practiceReservation.get(i).getConcert_start_time(),
                                    practiceReservation.get(i).getConcert_end_time(),
                                    practiceReservation.get(i).getProvide()));
                        }
                        //화면 리스트 뷰에 정보들이 들어가있는 어댑터를 연결함
                        listView.setAdapter(mAdapter);
                    } else {
                        int StatusCode = response.code();
                        Log.i(ApplicationController.TAG, "상태 Code : " + StatusCode);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<graduationwork.buskingtown.model.ConcertReservation>> call, Throwable t) {
                Log.i(ApplicationController.TAG, "게시물 리스트 서버 연결 실패 Message : " + t.getMessage());
            }
        });

    }

    //리스트 아이템들(화면에 띄워지는 정보들을 세팅)
    public class ConcertListItem {
        private int reservation_id;
        private String date;
        private String start_time;
        private String end_time;
        private int provide_id;

        public ConcertListItem(int reservation_id, String date, String start_time, String end_time, int provide_id) {
            this.reservation_id = reservation_id;
            this.date = date;
            this.start_time = start_time;
            this.end_time = end_time;
            this.provide_id = provide_id;
        }
    }

    //Adapter
    public class ConcertListCustom extends BaseAdapter {

        private ArrayList<ConcertListItem> list;

        public ConcertListCustom(ArrayList<ConcertListItem> list) {
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public ConcertListItem getItem(int position) {
            //현재 position에 따른 list값 반환
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //부모뷰에 리스트로 세팅될 하나의 조각을 세팅하는 것
            reservation_list = getLayoutInflater().inflate(R.layout.reservationlist, reservation_container, false);
            TextView date = reservation_list.findViewById(R.id.reservationDate);
            TextView time = reservation_list.findViewById(R.id.reservationTime);
            TextView loc_name = reservation_list.findViewById(R.id.reservationLocation);
            date.setText(getItem(position).date);
            String start_time_text = getItem(position).start_time;
            String end_time_text = getItem(position).end_time;
            time.setText(start_time_text + " ~ " + end_time_text);
            final String[] loc_name_text = new String[1];
            final String[] address = new String[1];

            Call<LendLocation> roomInfo = apiService.roomInfo(user_token, getItem(position).provide_id);
            roomInfo.enqueue(new Callback<LendLocation>() {
                @Override
                public void onResponse(Call<LendLocation> call, Response<LendLocation> response) {
                    if (response.isSuccessful()) {
                        loc_name_text[0] = response.body().getProvide_location_name();
                        address[0] = response.body().getProvide_location();
                        loc_name.setText(loc_name_text[0]);
                    }
                }

                @Override
                public void onFailure(Call<LendLocation> call, Throwable t) {

                }
            });

            reservation_list.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent check = new Intent(getApplication(), ConcertReservation.class);
                    check.putExtra("reservation_id", getItem(position).reservation_id);
                    check.putExtra("loc_name", loc_name_text[0]);
                    check.putExtra("provide", getItem(position).provide_id);
                    check.putExtra("start_time", getItem(position).start_time);
                    check.putExtra("end_time", getItem(position).end_time);
                    check.putExtra("date", getItem(position).date);
                    check.putExtra("address", address[0]);
                    startActivity(check);
                }
            });

            return reservation_list;
        }
    }

    public void previousActivity(View v) {
        onBackPressed();
    }

    public void getLocalData() {
        SharedPreferences pref = getSharedPreferences("User", Activity.MODE_PRIVATE);
        SharedPreferences busker_pref = getSharedPreferences("BuskerUser", Activity.MODE_PRIVATE);
        user_token = pref.getString("auth_token", null);
        user_name = pref.getString("username", null);
        busker_id = busker_pref.getInt("busker_id", 0);
    }

    public void restApiBuilder() {
        ApplicationController application = ApplicationController.getInstance();
        application.buildNetworkService();
        apiService = ApplicationController.getInstance().getRestApiService();
    }

}
