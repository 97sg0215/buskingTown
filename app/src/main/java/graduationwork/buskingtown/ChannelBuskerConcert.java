package graduationwork.buskingtown;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ChannelBuskerConcert extends Fragment {

    int test__concert=3;

    public ChannelBuskerConcert(){
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.activity_channel_busker_concert, container, false);

        for (int concertCount=0; concertCount<test__concert; concertCount++) {
            LinearLayout concertBox = (LinearLayout) v.findViewById(R.id.concertContainer);

            TextView concertText = (TextView)v.findViewById(R.id.concertText);
            if (test__concert > 1 ){
                concertText.setVisibility(View.GONE);
                View concertlist = inflater.inflate(R.layout.concertbox,concertBox,false);
                if(concertlist.getParent()!= null)
                    ((ViewGroup)concertlist.getParent()).removeView(concertlist);
               concertBox.addView(concertlist);
            }
        }

        return v;
    }

}
