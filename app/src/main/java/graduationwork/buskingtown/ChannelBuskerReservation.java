package graduationwork.buskingtown;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.android.gms.common.api.Releasable;

public class ChannelBuskerReservation extends Fragment {

    int test__reservaion=3;

    public ChannelBuskerReservation(){
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.activity_channel_busker_reservation, container, false);

        for(int reservationCount=0;reservationCount<test__reservaion;reservationCount++){
            final LinearLayout reservationList = (LinearLayout)v.findViewById(R.id.reservation_list);

            if (test__reservaion > 1 ) {
                View list = inflater.inflate(R.layout.roadconcert_reservation,reservationList,false);
                if (list.getParent() != null)
                    ((ViewGroup) list.getParent()).removeView(list);
                reservationList.addView(list);
                reservationList.setVisibility(View.GONE);
            }

            Log.e("for문 횟수", String.valueOf(reservationCount));
        }

        return v;
    }

}