package graduationwork.buskingtown;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class ChannelBuskerReservation extends Fragment {
    //호출될프래그먼트 변수들
    private final int roadFRAGMENT = 1;
    private final int practicePlaceFRAGMENT = 2;
    private final int concertPlaceFRAGMENT = 3;

    //탭바 아이콘 변수들
    private TextView roadConcert, practiceRoom, concert;

    public ChannelBuskerReservation(){
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.activity_channel_busker_reservation, container, false);

        return v;

        // 프래그먼트는 getView해서 찾아와야함 !!!!
    }

}
