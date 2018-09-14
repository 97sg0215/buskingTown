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
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

public class ChannelBuskerSchedule extends Fragment {

    int test__schedule=5;
    int test__concert=5;
    int test__post=3;

    private FrameLayout postingContainer;

    public ChannelBuskerSchedule(){
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.activity_channel_busker_schedule, container, false);



        for (int postCount=0; postCount<test__schedule; postCount++) {
            LinearLayout postingBox = (LinearLayout) v.findViewById(R.id.postingContainer);

            TextView postText = (TextView)v.findViewById(R.id.posttext);
            if (test__post > 1 ){
                postText.setVisibility(View.GONE);
                View postlist = inflater.inflate(R.layout.busker_posting,postingBox,false);
                if(postlist.getParent()!= null)
                    ((ViewGroup)postlist.getParent()).removeView(postlist);
                postingBox.addView(postlist);
            }
        }






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
        }
        return v;
    }

    public void onClickPost(View v){
        Intent writePost = new Intent(getActivity(), WritePost.class);
        startActivity(writePost);
    }

}