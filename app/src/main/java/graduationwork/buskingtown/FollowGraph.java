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
import java.util.List;

import graduationwork.buskingtown.api.RestApiService;

public class FollowGraph extends Fragment {

    SharedPreferences prefUser, prefBusker;

    RestApiService apiService;

    String user_token;
    int busker_id;
    String start_date,end_date;

    private LineChart lineChart;

    public FollowGraph(){
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            start_date = bundle.getString(start_date);
            end_date = bundle.getString(end_date);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.graph, container, false);

        prefBusker = this.getActivity().getSharedPreferences("BuskerUser", Activity.MODE_PRIVATE);
        prefUser = this.getActivity().getSharedPreferences("User", Activity.MODE_PRIVATE);

        restApiBuilder();

        getLocalData();

        lineChart = (LineChart)v.findViewById(R.id.chart);

        Log.e("조회 날짜", String.valueOf(start_date));
        Log.e("조회 날짜", String.valueOf(end_date));

        //setValues
        List<Entry> entries = new ArrayList<>();
        entries.add(new Entry(0, 0));
        entries.add(new Entry(1, 2));
        entries.add(new Entry(2, 1));
        entries.add(new Entry(3, 1));


        LineDataSet lineDataSet = new LineDataSet(entries, "좋아하는 팬");
//        lineDataSet.setAxisDependency(VAxis/.;
        lineDataSet.setLineWidth(2);
        lineDataSet.setCircleRadius(3);
        lineDataSet.setCircleColor(Color.parseColor("#FFA1B4DC"));
        lineDataSet.setCircleColor(getResources().getColor(R.color.mainYellow));
        lineDataSet.setCircleColorHole(Color.WHITE);
        lineDataSet.setColor(getResources().getColor(R.color.mainYellow));
        lineDataSet.setDrawCircleHole(true);
        lineDataSet.setDrawCircles(true);
        lineDataSet.setDrawHorizontalHighlightIndicator(false);
        lineDataSet.setDrawHighlightIndicators(false);
        lineDataSet.setDrawValues(false);


        LineData lineData = new LineData(lineDataSet);
        lineChart.setData(lineData);


        String[] values_one_month = { "1주", "2주", "3주","4주"};

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextColor(Color.BLACK);
        xAxis.setDrawGridLines(false);
      //  xAxis.enableGridDashedLine(5, 20, 0);
        xAxis.setValueFormatter(new MyXAxisValueFormatter(values_one_month));
        xAxis.setGranularity(1f);
        xAxis.setTextSize(15);


        YAxis yLAxis = lineChart.getAxisLeft();
        yLAxis.setTextColor(Color.BLACK);

        YAxis yRAxis = lineChart.getAxisRight();
        yRAxis.setDrawLabels(false);
        yRAxis.setDrawAxisLine(false);
        yRAxis.setDrawGridLines(false);

        Description description = new Description();
        description.setText("");

        lineChart.setDoubleTapToZoomEnabled(false);
        lineChart.setDrawGridBackground(false);
        lineChart.setDescription(description);
        lineChart.animateY(2000, Easing.EasingOption.EaseInCubic);
        lineChart.invalidate();




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
}
