package graduationwork.buskingtown;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import graduationwork.buskingtown.api.RestApiService;
import graduationwork.buskingtown.model.Connections;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FollowGraph extends Fragment {

    SharedPreferences prefUser, prefBusker;

    RestApiService apiService;



    String user_token;
    int busker_id;
    String get_start_date,get_end_date;

    List<Entry> entries = new ArrayList<>();

    private LineChart lineChart;

    int follower_cnt;
    int result = 0;
    ArrayList<Integer> follower_data = new ArrayList<>();
    ArrayList<Integer> follower_entry = new ArrayList<>();

    public FollowGraph(){
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefBusker = this.getActivity().getSharedPreferences("BuskerUser", Activity.MODE_PRIVATE);
        prefUser = this.getActivity().getSharedPreferences("User", Activity.MODE_PRIVATE);

        restApiBuilder();

        getLocalData();

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            get_start_date = bundle.getString("start_date");
            get_end_date = bundle.getString("end_date");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.graph, container, false);

        lineChart = (LineChart)v.findViewById(R.id.chart);

        Log.e("조회 날짜", String.valueOf(get_start_date));
        Log.e("조회 날짜", String.valueOf(get_end_date));

        String[] start_month_words = get_start_date.split("-");
        String[] end_month_words = get_end_date.split("-");
        int start_month = Integer.parseInt(start_month_words[1]);
        int end_month = Integer.parseInt(end_month_words[1]);

        Call<List<Connections>> connectionsCall = apiService.getFollowerStatistic(user_token,busker_id,get_start_date,get_end_date);
        connectionsCall.enqueue(new Callback<List<Connections>>() {
            @Override
            public void onResponse(Call<List<Connections>> call, Response<List<Connections>> response) {
                if(response.isSuccessful()) {
                    List<Connections> connections = response.body();
                    if (connections.size() != 0) {
                        //1개월
                        if ((end_month - start_month) == 1) {
                            for(int x=0;x<connections.size();x++){
                                follower_cnt = connections.get(x).getFollower_count();
                                follower_data.add(follower_cnt);
                            }
                            for (int day = 0; day < 28-connections.size(); day++) {
                                follower_data.add(0);
                            }

                            for (int i = 0; i < follower_data.size(); i++) {
                                result += follower_data.get(i);
                                Log.e("데이터", String.valueOf(result));
                                if(i%7==0){
                                    follower_entry.add(result);
                                }
                            }

                            Log.e("총 데이터", String.valueOf(follower_entry));


//                            LineDataSet lineDataSet = new LineDataSet(entries, "좋아하는 팬");
////                          lineDataSet.setAxisDependency(VAxis/.;
//                            lineDataSet.setLineWidth(2);
//                            lineDataSet.setCircleRadius(3);
//                            lineDataSet.setCircleColor(Color.parseColor("#FFA1B4DC"));
//                            lineDataSet.setCircleColor(getResources().getColor(R.color.mainYellow));
//                            lineDataSet.setCircleColorHole(Color.WHITE);
//                            lineDataSet.setColor(getResources().getColor(R.color.mainYellow));
//                            lineDataSet.setDrawCircleHole(true);
//                            lineDataSet.setDrawCircles(true);
//                            lineDataSet.setDrawHorizontalHighlightIndicator(false);
//                            lineDataSet.setDrawHighlightIndicators(false);
//                            lineDataSet.setDrawValues(false);
//
//
//                            LineData lineData = new LineData(lineDataSet);
//                            lineChart.setData(lineData);
//
//
//                            String[] values_one_month = { "1주", "2주", "3주","4주"};
//
//                            XAxis xAxis = lineChart.getXAxis();
//                            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
//                            xAxis.setTextColor(Color.BLACK);
//                            xAxis.setDrawGridLines(false);
//                            //  xAxis.enableGridDashedLine(5, 20, 0);
//                            xAxis.setValueFormatter(new MyXAxisValueFormatter(values_one_month));
//                            xAxis.setGranularity(1f);
//                            xAxis.setTextSize(15);
//
//
//                            YAxis yLAxis = lineChart.getAxisLeft();
//                            yLAxis.setTextColor(Color.BLACK);
//
//                            YAxis yRAxis = lineChart.getAxisRight();
//                            yRAxis.setDrawLabels(false);
//                            yRAxis.setDrawAxisLine(false);
//                            yRAxis.setDrawGridLines(false);
//
//                            Description description = new Description();
//                            description.setText("");
//
//                            lineChart.setDoubleTapToZoomEnabled(false);
//                            lineChart.setDrawGridBackground(false);
//                            lineChart.setDescription(description);
//                            lineChart.animateY(2000, Easing.EasingOption.EaseInCubic);
//                            lineChart.invalidate();
                        }
                    }else {

                    }
                }else {
                    int StatusCode = response.code();
                    Log.i(ApplicationController.TAG, "상태 Code : " + StatusCode);
                    Log.e("메세지", String.valueOf(response.message()));
                    Log.e("리스폰스에러바디", String.valueOf(response.errorBody()));
                    Log.e("리스폰스바디", String.valueOf(response.body()));
                }
            }

            @Override
            public void onFailure(Call<List<Connections>> call, Throwable t) {
                Log.e("call","실패");
            }
        });

        //setValues

//
//        entries.add(new Entry(1, 2));
//        entries.add(new Entry(2, 4));
//        entries.add(new Entry(3, 0));





        return v;
    }

    public void restApiBuilder() {
        ApplicationController application = ApplicationController.getInstance();
        application.buildNetworkService();
        apiService = ApplicationController.getInstance().getRestApiService();
    }

    public void getLocalData(){
        user_token = prefUser.getString("auth_token",null);
        busker_id = prefBusker.getInt("busker_id",0);
    }

    //현재 날짜 주차

    public static String getWeek(){

        Calendar c = Calendar.getInstance();
        String week = String.valueOf(c.get(Calendar.WEEK_OF_MONTH));
        return week;

    }
}
