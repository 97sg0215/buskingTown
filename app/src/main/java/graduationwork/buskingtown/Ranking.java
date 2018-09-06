package graduationwork.buskingtown;

import android.annotation.SuppressLint;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.nio.BufferUnderflowException;


public class Ranking extends Fragment {

    private final int dayFRAGMENT = 1;
    private final int weekFRAGMENT = 2;
    private final int monthFRAGEMNT = 3;

    private TextView day, week, month;
    private View dayBar, weekBar, monthBar;

    public Ranking() {
        // Required empty public constructor
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup containter, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.activity_ranking, containter, false);

        day = (TextView) v.findViewById(R.id.dayRanking);
        week = (TextView) v.findViewById(R.id.weekRanking);
        month = (TextView) v.findViewById(R.id.monthRanking);

        final View dayBar = (View) v.findViewById(R.id.dayRankingBar);
        final View weekBar = (View) v.findViewById(R.id.weekRankingBar);
        final View monthBar = (View) v.findViewById(R.id.monthRankingBar);

        getFragmentManager().beginTransaction().add(R.id.buskerrankingContainer, new RankingListView()).commit();

        day.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.buskerrankingContainer, new RankingListView()).commit();
                //버튼색 활성화
                day.setTextColor(getResources().getColor(R.color.mainYellow));
                dayBar.setVisibility(View.VISIBLE);
                //버튼색 비활성화
                week.setTextColor(getResources().getColor(R.color.fontBlack));
                weekBar.setVisibility(View.GONE);
                month.setTextColor(getResources().getColor(R.color.fontBlack));
                monthBar.setVisibility(View.GONE);
            }
        });

        week.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.buskerrankingContainer, new RankingListView()).commit();
                //버튼색 활성화
                week.setTextColor(getResources().getColor(R.color.mainYellow));
                weekBar.setVisibility(View.VISIBLE);
                //버튼색 비활성화
                day.setTextColor(getResources().getColor(R.color.fontBlack));
                dayBar.setVisibility(View.GONE);
                month.setTextColor(getResources().getColor(R.color.fontBlack));
                monthBar.setVisibility(View.GONE);


            }
        });

        month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.buskerrankingContainer, new RankingListView()).commit();//버튼색 활성화
                //버튼색 활성화
                month.setTextColor(getResources().getColor(R.color.mainYellow));
                monthBar.setVisibility(View.VISIBLE);
                //버튼색 비활성화
                day.setTextColor(getResources().getColor(R.color.fontBlack));
                dayBar.setVisibility(View.GONE);
                week.setTextColor(getResources().getColor(R.color.fontBlack));
                weekBar.setVisibility(View.GONE);

            }
        });
        return v;
    }
}
