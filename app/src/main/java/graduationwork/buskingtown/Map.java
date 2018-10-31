package graduationwork.buskingtown;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class Map extends Fragment {

    //호출될프래그먼트 변수들
    private final int locationFRAGMENT = 1;
    private final int dateFRAGMENT = 2;

    //탭바 아이콘 변수들
    private TextView map, date;
    private View map_bar, date_bar;

    public Map(){
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.activity_map, container, false);

        callFragment(locationFRAGMENT);

        map = (TextView) v.findViewById(R.id.mapsearch);
        date = (TextView) v.findViewById(R.id.datesearch);

        map_bar = (View) v.findViewById(R.id.mapsearchBar);
        date_bar = (View) v.findViewById(R.id.datesearchBar);

//        map.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                callFragment(locationFRAGMENT);
//
//                map.setTextColor(getResources().getColor(R.color.mainYellow));
//                map_bar.setVisibility(View.VISIBLE);
//
//                date.setTextColor(getResources().getColor(R.color.fontBlack));
//                date_bar.setVisibility(View.GONE);
//            }
//        });

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callFragment(dateFRAGMENT);
                map.setTextColor(getResources().getColor(R.color.fontBlack));
                map_bar.setVisibility(View.GONE);

                date.setTextColor(getResources().getColor(R.color.mainYellow));
                date_bar.setVisibility(View.VISIBLE);
            }
        });

        return v;
    }



    @SuppressLint("ResourceAsColor")
    private void callFragment(int frament_no) {
        // 프래그먼트 사용을 위해
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();

        switch (frament_no) {
            case 1:
                // '지도' 호출
                RealtimeBuskingMap mapFragment = new RealtimeBuskingMap();
                transaction.replace(R.id.realtimeFragmentContainer, mapFragment);
                transaction.commit();

                break;
            case 2:
                // '일정페이지' 호출
                CalendarView calendarFragment = new CalendarView();
                transaction.replace(R.id.realtimeFragmentContainer, calendarFragment);
                transaction.commit();

                break;

        }

    }
}
