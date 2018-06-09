package graduationwork.buskingtown;

import android.support.v4.app.Fragment;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ChannelBuskerTab extends Fragment {

    //호출될프래그먼트 변수들
    private final int roadFRAGMENT = 1;
    private final int practicePlaceFRAGMENT = 2;
    private final int concertPlaceFRAGMENT = 3;

    //탭바 아이콘 변수들
    private TextView roadConcert, practiceRoom, concert;


    public ChannelBuskerTab(){
        // Required empty public constructor
    }

    @Override
    @SuppressLint("ResourceAsColor")
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.activity_channel_busker_tab, container, false);

        //아이콘에 대한 참조 변수
        roadConcert = (TextView) v.findViewById(R.id.roadConcert);
        practiceRoom = (TextView) v.findViewById(R.id.practiceRoom);
        concert = (TextView) v.findViewById(R.id.concert);

        final View roadConcertBar = (View) v.findViewById(R.id.roadConcertBar);
        final View practiceRoomBar = (View) v.findViewById(R.id.practiceRoomBar);
        final View concertBar = (View) v.findViewById(R.id.concertBar);

        //클릭메소드 연결
        roadConcert.setOnClickListener(new View.OnClickListener(){

            @Override

            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.buskerFragmentContainer, new ChannelBuskerReservation()).commit();
                //버튼색 활성화
                roadConcert.setTextColor(getResources().getColor(R.color.mainYellow));
                roadConcertBar.setVisibility(View.VISIBLE);
                //버튼색 비활성화
                practiceRoom.setTextColor(getResources().getColor(R.color.fontBlack));
                practiceRoomBar.setVisibility(View.GONE);
                concert.setTextColor(getResources().getColor(R.color.fontBlack));
                concertBar.setVisibility(View.GONE);
            }


        });
        practiceRoom.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.buskerFragmentContainer, new ChannelBuskerPracticeroom()).commit();
                //버튼색 활성화
                practiceRoom.setTextColor(getResources().getColor(R.color.mainYellow));
                practiceRoomBar.setVisibility(View.VISIBLE);
                //버튼색 비활성화
                roadConcert.setTextColor(getResources().getColor(R.color.fontBlack));
                roadConcertBar.setVisibility(View.GONE);
                concert.setTextColor(getResources().getColor(R.color.fontBlack));
                concertBar.setVisibility(View.GONE);


            }
        });
        concert.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.buskerFragmentContainer, new ChannelBuskerConcert()).commit();//버튼색 활성화
                //버튼색 활성화
                concert.setTextColor(getResources().getColor(R.color.mainYellow));
                concertBar.setVisibility(View.VISIBLE);
                //버튼색 비활성화
                roadConcert.setTextColor(getResources().getColor(R.color.fontBlack));
                roadConcertBar.setVisibility(View.GONE);
                practiceRoom.setTextColor(getResources().getColor(R.color.fontBlack));
                practiceRoomBar.setVisibility(View.GONE);
            }
        });

        return v;
    }

}
