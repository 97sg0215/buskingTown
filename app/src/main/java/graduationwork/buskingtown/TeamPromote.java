package graduationwork.buskingtown;

import android.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

public class TeamPromote extends Fragment {

    //호출될프래그먼트 변수들
    private final int mainbannerFRAGMENT = 1;
    private final int recommendFRAGMENT = 2;

    //탭바 아이콘 변수들
    private TextView mainbanner,recommend;
    private View mainbannerBar,recommendBar;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.activity_team_promote, container, false);

        //아이콘에 대한 참조 변수
        mainbanner = (TextView) v.findViewById(R.id.mainbanner);
        recommend = (TextView) v.findViewById(R.id.recommend);

        final View mainbannerBar = (View) v.findViewById(R.id.mainbannerBar);
        final View recommendBar = (View) v.findViewById(R.id.recommendBar);

        //처음 childfragment 지정
        getFragmentManager().beginTransaction().add(R.id.publicFragmentContainer, new PromoteMainbanner()).commit();

        //클릭메소드 연결
        mainbanner.setOnClickListener(new View.OnClickListener(){

            @Override

            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.publicFragmentContainer, new PromoteMainbanner()).commit();
                //버튼색 활성화
                mainbanner.setTextColor(getResources().getColor(R.color.mainYellow));
                mainbannerBar.setVisibility(View.VISIBLE);
                //버튼색 비활성화
                recommend.setTextColor(getResources().getColor(R.color.fontBlack));
                recommendBar.setVisibility(View.GONE);
            }
        });

        recommend.setOnClickListener(new View.OnClickListener(){

            @Override

            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.publicFragmentContainer, new PromoteRecommend()).commit();
                //버튼색 활성화
                recommend.setTextColor(getResources().getColor(R.color.mainYellow));
                recommendBar.setVisibility(View.VISIBLE);
                //버튼색 비활성화
                mainbanner.setTextColor(getResources().getColor(R.color.fontBlack));
                mainbannerBar.setVisibility(View.GONE);
            }

        });

        return v;
    }
}
