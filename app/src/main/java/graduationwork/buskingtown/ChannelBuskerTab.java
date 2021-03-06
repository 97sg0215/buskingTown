package graduationwork.buskingtown;

import android.support.v4.app.Fragment;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ChannelBuskerTab extends Fragment {

    private TextView roadConcert, practiceRoom, concert;
    private RelativeLayout roadTab, practiceTab, concertTab;

    public ChannelBuskerTab() {
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
        roadTab = (RelativeLayout) v.findViewById(R.id.roadTab);
        practiceTab = (RelativeLayout) v.findViewById(R.id.practiceTab);
        concertTab = (RelativeLayout) v.findViewById(R.id.concertTab);

        final View roadConcertBar = (View) v.findViewById(R.id.roadConcertBar);
        final View practiceRoomBar = (View) v.findViewById(R.id.practiceRoomBar);
        final View concertBar = (View) v.findViewById(R.id.concertBar);

        //처음 childfragment 지정
        getFragmentManager().beginTransaction().add(R.id.buskerFragmentContainer, new ChannelBuskerReservation()).commit();

        //클릭메소드 연결
        roadTab.setOnClickListener(new View.OnClickListener() {

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
        practiceTab.setOnClickListener(new View.OnClickListener() {

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
        concertTab.setOnClickListener(new View.OnClickListener() {

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