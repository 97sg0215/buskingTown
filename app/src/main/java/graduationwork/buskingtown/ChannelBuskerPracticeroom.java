package graduationwork.buskingtown;


import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ChannelBuskerPracticeroom extends Fragment {

    int test__practiceroom=3;
    public ChannelBuskerPracticeroom(){
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.activity_channel_busker_practiceroom, container, false);

        for (int practiceroomCount=0; practiceroomCount<test__practiceroom; practiceroomCount++) {
            LinearLayout practiceroomBox = (LinearLayout) v.findViewById(R.id.practiceroomContainer);

            TextView practiceRoomText = (TextView)v.findViewById(R.id.practiceroomText);
            if (test__practiceroom > 1 ){
                practiceRoomText.setVisibility(View.GONE);
                View practiceroomlist = inflater.inflate(R.layout.practiceroombox,practiceroomBox,false);
                if(practiceroomlist.getParent()!= null)
                    ((ViewGroup)practiceroomlist.getParent()).removeView(practiceroomlist);
                practiceroomBox.addView(practiceroomlist);
            }
        }

        return v;

    }

}
