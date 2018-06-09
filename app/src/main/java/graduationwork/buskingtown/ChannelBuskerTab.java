package graduationwork.buskingtown;

import android.support.v4.app.Fragment;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.FragmentTransaction;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.activity_channel_busker_tab, container, false);

        //아이콘에 대한 참조 변수
        roadConcert = (TextView) v.findViewById(R.id.roadConcert);
        practiceRoom = (TextView) v.findViewById(R.id.practiceRoom);
        concert = (TextView) v.findViewById(R.id.concert);

        //클릭메소드 연결
        roadConcert.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new ChannelBuskerReservation()).commit();
            }
        });
        practiceRoom.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new ChannelBuskerPracticeroom()).commit();
            }
        });
        concert.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new ChannelBuskerConcert()).commit();
            }
        });

        //액티비티 시작하자마자 실행 될 프래그먼트
        //callFragment(roadFRAGMENT);

        return v;
    }

    //프래그먼트 부르는 메소드
   /* @SuppressLint("ResourceAsColor")
    private void callFragment(int frament_no) {
        final View roadConcertBar = (View) getView().findViewById(R.id.roadConcertBar);
        final View practiceRoomBar = (View) getView().findViewById(R.id.practiceRoomBar);
        final View concertBar = (View) getView().findViewById(R.id.concertBar);

        // 프래그먼트 사용을 위해
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();


        switch (frament_no) {
            case 1:
                // '거리공연페이지' 호출
                ChannelBuskerReservation roadFragment = new ChannelBuskerReservation();
                transaction.replace(R.id.fragmentContainer, roadFragment);
                transaction.commit();

                //버튼색 활성화
                roadConcert.setTextColor(R.color.mainPurple);
                roadConcertBar.setVisibility(View.VISIBLE);
                //버튼색 비활성화
                practiceRoom.setTextColor(R.color.fontBlack);
                practiceRoomBar.setVisibility(View.GONE);
                concert.setTextColor(R.color.fontBlack);
                concertBar.setVisibility(View.GONE);

                break;
            case 2:
                // '연습실페이지' 호출
                ChannelBuskerPracticeroom practiceRoomFragment = new ChannelBuskerPracticeroom();
                transaction.replace(R.id.fragmentContainer, practiceRoomFragment);
                transaction.commit();

                //버튼색 활성화
                practiceRoom.setTextColor(R.color.mainPurple);
                practiceRoomBar.setVisibility(View.VISIBLE);
                //버튼색 비활성화
                roadConcert.setTextColor(R.color.fontBlack);
                roadConcertBar.setVisibility(View.GONE);
                concert.setTextColor(R.color.fontBlack);
                concertBar.setVisibility(View.GONE);

                break;
            case 3:
                // '콘서트페이지' 호출
                ChannelBuskerConcert concertFragment = new ChannelBuskerConcert();
                transaction.replace(R.id.fragmentContainer, concertFragment);
                transaction.commit();

                //버튼색 활성화
                concert.setTextColor(R.color.mainPurple);
                concertBar.setVisibility(View.VISIBLE);
                //버튼색 비활성화
                roadConcert.setTextColor(R.color.fontBlack);
                roadConcertBar.setVisibility(View.GONE);
                practiceRoom.setTextColor(R.color.fontBlack);
                practiceRoomBar.setVisibility(View.GONE);

                break;
        }
    }

    //버튼 클릭시 프레그먼트 호출하는 메소드
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.roadConcert:
                callFragment(roadFRAGMENT);
                break;

            case R.id.practiceRoom:
                callFragment(practicePlaceFRAGMENT);
                break;

            case R.id.concert:
                callFragment(concertPlaceFRAGMENT);
                break;
        }
    }*/
}
