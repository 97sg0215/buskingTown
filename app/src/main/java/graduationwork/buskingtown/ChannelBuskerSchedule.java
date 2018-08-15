package graduationwork.buskingtown;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class ChannelBuskerSchedule extends Fragment {

    int test__schedule=5;
    int test__concert=5;

    public ChannelBuskerSchedule(){
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.activity_channel_busker_schedule, container, false);

        ImageView pen = (ImageView)v.findViewById(R.id.pen);
        pen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent writePost = new Intent(getActivity(), WritePost.class);
                startActivity(writePost);
            }
        });

        for (int scheduleCount=0; scheduleCount<test__schedule; scheduleCount++) {
            final ImageButton dropdownBtn = (ImageButton)v.findViewById(R.id.dropdown_sch);
            final LinearLayout scheduleBox = (LinearLayout)v.findViewById(R.id.addSchedule_sch);

            if (test__schedule > 1 ){
                dropdownBtn.setVisibility(View.VISIBLE);
                View list = inflater.inflate(R.layout.schedule_list,scheduleBox,false);
                if(list.getParent()!= null)
                    ((ViewGroup)list.getParent()).removeView(list);
                scheduleBox.addView(list);
                scheduleBox.setVisibility(View.GONE);
                dropdownBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v){
                        scheduleBox.setVisibility(View.VISIBLE);
                        dropdownBtn.setVisibility(View.INVISIBLE);
                    }
                });
            }
            Log.e("for문 횟수", String.valueOf(scheduleCount));
        }

        for (int concertCount=0; concertCount<test__concert; concertCount++){
            final ImageButton concertdropdownBtn = (ImageButton)v.findViewById(R.id.dropdown_con);
            final LinearLayout concertBox = (LinearLayout)v. findViewById(R.id.addConcert_con);

            if (test__concert > 1 ){
                concertdropdownBtn.setVisibility(View.VISIBLE);
                View concertlist = inflater.inflate(R.layout.concert_list,concertBox,false);
                if(concertlist.getParent()!=null)
                    ((ViewGroup)concertlist.getParent()).removeView(concertlist);
                concertBox.addView(concertlist);
                concertBox.setVisibility(View.GONE);
                concertdropdownBtn.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        concertBox.setVisibility(View.VISIBLE);
                        concertdropdownBtn.setVisibility(View.INVISIBLE);
                    }
                });
            }
            Log.e("for문 횟수", String.valueOf(concertCount));
        }

        return v;
    }

}