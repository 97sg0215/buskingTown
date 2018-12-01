package graduationwork.buskingtown;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import com.tsongkha.spinnerdatepicker.DatePicker;
import com.tsongkha.spinnerdatepicker.DatePickerDialog;
import com.tsongkha.spinnerdatepicker.SpinnerDatePickerDialogBuilder;

import graduationwork.buskingtown.api.RestApiService;
import graduationwork.buskingtown.model.CoinManagement;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MycoinHouse extends AppCompatActivity implements View.OnClickListener {

    Button oneMonth, threeMonth, sixMonth, oneYearz, startDate, endDate;
    int coinStartYear, coinStartMonth, coinStartDay, coinEndYear, coinEndMonth, coinEndDay;
    SimpleDateFormat simpleDateFormat;

    RestApiService apiService;

    String user_token, user_name;
    int user_id;

    private ListView listView;
    ArrayList<CoinViewItem> listItems;
    MyCoinHouseCustom mAdapter;

    String q_start_date, q_end_date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mycoin_house);

        getLocalData();

        restApiBuilder();

        //변수에 선언해준 것을 정의
        listItems = new ArrayList<CoinViewItem>();
        listView = (ListView) findViewById(R.id.coinContainer);
        mAdapter = new MyCoinHouseCustom(listItems);

        //버튼 참조변수
        oneMonth = (Button) findViewById(R.id.oneMonth);
        threeMonth = (Button) findViewById(R.id.threeMonth);
        sixMonth = (Button) findViewById(R.id.sixMonth);
        oneYearz = (Button) findViewById(R.id.oneYearz);

        //클릭메소드 연결
        oneMonth.setOnClickListener(this);
        threeMonth.setOnClickListener(this);
        sixMonth.setOnClickListener(this);
        oneYearz.setOnClickListener(this);

        //아래는 뒤로가기 버튼 클릭시 뒤로가는거임
        ImageButton backBtn = (ImageButton) findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MycoinHouse.super.onBackPressed();
            }
        });


        //달력 객체 불러옴
        final GregorianCalendar startCalendar = new GregorianCalendar();

        coinStartYear = startCalendar.get(Calendar.YEAR);
        coinStartMonth = startCalendar.get(Calendar.MONTH);
        coinStartDay = startCalendar.get(Calendar.DAY_OF_MONTH);

        final GregorianCalendar endCalendar = new GregorianCalendar();
        coinEndYear = endCalendar.get(Calendar.YEAR);
        coinEndMonth = endCalendar.get(Calendar.MONTH);
        coinEndDay = endCalendar.get(Calendar.DAY_OF_MONTH);

        //날짜형태
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);

        //데이트 피커
        startDate = (Button) findViewById(R.id.coinStartDate);
        endDate = (Button) findViewById(R.id.coinEndDate);
        startDate.setText(String.format("%2d년 %02d월 %02d일", coinStartYear, coinStartMonth, coinStartDay));
        q_start_date = coinEndYear + "-" + coinEndMonth + "-" + coinEndDay;
        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatePickerDialog.OnDateSetListener dateStartPicker = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        startDate.setText(year + "년 " + (monthOfYear + 1) + "월 " + dayOfMonth + "일");
                        q_start_date = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                        coin_view(user_id, q_start_date, q_end_date);
                    }
                };


                new SpinnerDatePickerDialogBuilder()
                        .callback(dateStartPicker)
                        .context(MycoinHouse.this)
                        .spinnerTheme(R.style.DatePickerSpinner)
                        .showTitle(true)
                        .defaultDate(coinStartYear, coinStartMonth, coinStartDay)
                        .build()
                        .show();
            }
        });

        endDate.setText(String.format("%2d년 %02d월 %02d일", coinEndYear, coinEndMonth + 1, coinEndDay));
        q_end_date = coinEndYear + "-" + (coinEndMonth + 1) + "-" + coinEndDay;
        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog.OnDateSetListener dateEndPicker = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        endDate.setText(year + "년 " + (monthOfYear + 1) + "월 " + dayOfMonth + "일");
                        q_end_date = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                        coin_view(user_id, q_start_date, q_end_date);
                    }
                };

                new SpinnerDatePickerDialogBuilder()
                        .callback(dateEndPicker)
                        .context(MycoinHouse.this)
                        .spinnerTheme(R.style.DatePickerSpinner)
                        .showTitle(true)
                        .defaultDate(coinEndYear, coinEndMonth, coinEndDay)
                        .build()
                        .show();
            }
        });

        coin_view(user_id, q_start_date, q_end_date);
    }

    public void previousActivity(View v) {
        onBackPressed();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.oneMonth:
                //1개월 버튼색 활성화
                oneMonth.setBackground(getDrawable(R.drawable.able_btn));
                oneMonth.setTextColor(Color.parseColor("#ffffff"));
                startDate.setText(String.format("%2d년 %02d월 %02d일", coinStartYear, coinStartMonth, coinStartDay));
                q_start_date = coinStartYear + "-" + coinStartMonth + "-" + coinStartDay;
                coin_view(user_id, q_start_date, q_end_date);
                //나머지 버튼색 비활성화
                threeMonth.setBackground(getDrawable(R.drawable.date_rounded));
                threeMonth.setTextColor(getResources().getColor(R.color.mainPurple));
                sixMonth.setBackground(getDrawable(R.drawable.date_rounded));
                sixMonth.setTextColor(getResources().getColor(R.color.mainPurple));
                oneYearz.setBackground(getDrawable(R.drawable.date_rounded));
                oneYearz.setTextColor(getResources().getColor(R.color.mainPurple));
                break;

            case R.id.threeMonth:
                //3개월 버튼색 활성화
                threeMonth.setBackground(getDrawable(R.drawable.able_btn));
                threeMonth.setTextColor(Color.parseColor("#ffffff"));
                startDate.setText(String.format("%2d년 %02d월 %02d일", coinStartYear, coinStartMonth - 2, coinStartDay));
                q_start_date = coinStartYear + "-" + (coinStartMonth - 2) + "-" + coinStartDay;
                coin_view(user_id, q_start_date, q_end_date);
                //나머지 버튼색 비활성화
                oneMonth.setBackground(getDrawable(R.drawable.date_rounded));
                oneMonth.setTextColor(getResources().getColor(R.color.mainPurple));
                sixMonth.setBackground(getDrawable(R.drawable.date_rounded));
                sixMonth.setTextColor(getResources().getColor(R.color.mainPurple));
                oneYearz.setBackground(getDrawable(R.drawable.date_rounded));
                oneYearz.setTextColor(getResources().getColor(R.color.mainPurple));
                break;

            case R.id.sixMonth:
                //6개월 버튼색 활성화
                sixMonth.setBackground(getDrawable(R.drawable.able_btn));
                sixMonth.setTextColor(Color.parseColor("#ffffff"));
                startDate.setText(String.format("%2d년 %02d월 %02d일", coinStartYear, coinStartMonth - 5, coinStartDay));
                q_start_date = coinStartYear + "-" + (coinStartMonth - 5) + "-" + coinStartDay;
                coin_view(user_id, q_start_date, q_end_date);
                //나머지 버튼색 비활성화
                oneMonth.setBackground(getDrawable(R.drawable.date_rounded));
                oneMonth.setTextColor(getResources().getColor(R.color.mainPurple));
                threeMonth.setBackground(getDrawable(R.drawable.date_rounded));
                threeMonth.setTextColor(getResources().getColor(R.color.mainPurple));
                oneYearz.setBackground(getDrawable(R.drawable.date_rounded));
                oneYearz.setTextColor(getResources().getColor(R.color.mainPurple));
                break;

            case R.id.oneYearz:
                //1년 버튼색 활성화
                oneYearz.setBackground(getDrawable(R.drawable.able_btn));
                oneYearz.setTextColor(Color.parseColor("#ffffff"));
                startDate.setText(String.format("%2d년 %02d월 %02d일", coinStartYear - 1, coinStartMonth + 1, coinStartDay));
                q_start_date = (coinStartYear - 1) + "-" + (coinStartMonth + 1) + "-" + coinStartDay;
                coin_view(user_id, q_start_date, q_end_date);
                //나머지 버튼색 비활성화
                oneMonth.setBackground(getDrawable(R.drawable.date_rounded));
                oneMonth.setTextColor(getResources().getColor(R.color.mainPurple));
                threeMonth.setBackground(getDrawable(R.drawable.date_rounded));
                threeMonth.setTextColor(getResources().getColor(R.color.mainPurple));
                sixMonth.setBackground(getDrawable(R.drawable.date_rounded));
                sixMonth.setTextColor(getResources().getColor(R.color.mainPurple));
                break;
        }
    }

    public void coin_view(int user, String s_date, String e_date) {
        listItems.clear();
        mAdapter.notifyDataSetChanged();
        TextView transactionText = (TextView) findViewById(R.id.transactionText);
        Call<List<CoinManagement>> coinManagementCall = apiService.coinView(user_token, user, s_date, e_date);
        coinManagementCall.enqueue(new Callback<List<CoinManagement>>() {
            @Override
            public void onResponse(Call<List<CoinManagement>> call, Response<List<CoinManagement>> response) {
                List<CoinManagement> coinManagement = response.body();
                if (response.isSuccessful()) {
                    if (coinManagement.size() > 0) {
                        transactionText.setVisibility(View.GONE);
                        for (int i = 0; i < coinManagement.size(); i++) {
                            if (coinManagement.get(i).getType().equals("purchase")) {
                                listItems.add(new CoinViewItem(coinManagement.get(i).getDate_created(),
                                        coinManagement.get(i).getType(),
                                        coinManagement.get(i).getPurchase_coin_amount(),
                                        coinManagement.get(i).getCoin_balance()));
                            } else {
                                listItems.add(new CoinViewItem(coinManagement.get(i).getDate_created(),
                                        coinManagement.get(i).getType(),
                                        coinManagement.get(i).getCoin_amount(),
                                        coinManagement.get(i).getCoin_balance()));
                            }
                        }
                        listView.setAdapter(mAdapter);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<CoinManagement>> call, Throwable t) {

            }
        });
    }


    //user데이터 얻어오기
    private void getLocalData() {
        SharedPreferences pref = this.getSharedPreferences("User", Activity.MODE_PRIVATE);
        user_token = pref.getString("auth_token", null);
        user_name = pref.getString("username", null);
        user_id = pref.getInt("user_id", 0);

    }

    //api연결
    private void restApiBuilder() {
        ApplicationController application = ApplicationController.getInstance();
        application.buildNetworkService();
        apiService = ApplicationController.getInstance().getRestApiService();
    }


    //리스트 아이템들(화면에 띄워지는 정보들을 세팅)
    public class CoinViewItem {
        private String date;
        private String type;
        private int coin_amount;
        private int balance;

        public CoinViewItem(String date, String type, int coin_amount, int balance) {
            this.date = date;
            this.type = type;
            this.coin_amount = coin_amount;
            this.balance = balance;
        }
    }


    //Adapter
    public class MyCoinHouseCustom extends BaseAdapter {

        private ArrayList<CoinViewItem> list;

        public MyCoinHouseCustom(ArrayList<CoinViewItem> list) {
            this.list = list;
        }


        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public CoinViewItem getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @RequiresApi(api = Build.VERSION_CODES.M)
        @SuppressLint("ResourceAsColor")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //부모뷰에 리스트로 세팅될 하나의 조각을 세팅하는 것
            View result = getLayoutInflater().inflate(R.layout.transactionlist, parent, false);
            TextView dateText = (TextView) result.findViewById(R.id.dateText);
            TextView coin_type = (TextView) result.findViewById(R.id.coin_type);
            TextView coin_amount = (TextView) result.findViewById(R.id.transactionText);
            TextView coin_balance = (TextView) result.findViewById(R.id.balanceText);

            if (getItem(position).type.equals("purchase")) {
                coin_type.setText("충전");
                coin_amount.setTextColor(R.color.mainPurple);
                coin_amount.setText("+ " + String.valueOf(getItem(position).coin_amount));
            } else {
                coin_type.setText("코인 보내기");
                coin_amount.setTextColor(getColor(R.color.subPink));
                coin_amount.setText("- " + String.valueOf(getItem(position).coin_amount));
            }

            dateText.setText(getItem(position).date);
            coin_balance.setText(String.valueOf(getItem(position).balance) + " 개");

            return result;
        }
    }


}
