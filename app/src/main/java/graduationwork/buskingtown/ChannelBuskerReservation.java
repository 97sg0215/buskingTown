package graduationwork.buskingtown;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.common.api.Releasable;

public class ChannelBuskerReservation extends Fragment {

    int test__road=3;

    RelativeLayout addBtn;

    public ChannelBuskerReservation(){
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.activity_channel_busker_reservation, container, false);

        addBtn = (RelativeLayout) v.findViewById(R.id.addBtn);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent add= new Intent(getActivity(),BuskingOpen.class);
                startActivity(add);
            }
        });

        for (int roadCount=0; roadCount<test__road; roadCount++) {
            LinearLayout roadBox = (LinearLayout) v.findViewById(R.id.roadContainer);
            if (test__road > 1 ){
                View roadlist = inflater.inflate(R.layout.roadconcert_reservation,roadBox,false);
                if(roadlist.getParent()!= null)
                    ((ViewGroup)roadlist.getParent()).removeView(roadlist);
                roadBox.addView(roadlist);
            }
        }
        return v;
    }

}