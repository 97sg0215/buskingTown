package graduationwork.buskingtown;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;

public class ChannelUser extends AppCompatActivity {

    int test_schedule=5;
    int test_concert=5;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channel_user);

        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        for (int scheduleCount=0; scheduleCount<test_schedule; scheduleCount++) {
            addSchedule(inflater);
            Log.e("for문 횟수", String.valueOf(scheduleCount));
        }
        for(int concertCount=0; concertCount<test_concert; concertCount++){
            addConcert(inflater);
            Log.e("fot문 횟수", String.valueOf(concertCount));
        }

    }

    public void addSchedule(final LayoutInflater inflater){
        final LinearLayout scheduleBox = (LinearLayout) findViewById(R.id.addSchedule);
        final ImageButton dropdownBtn = (ImageButton) findViewById(R.id.dropdown);
        if (test_schedule > 1 ){
            dropdownBtn.setVisibility(View.VISIBLE);
            View list = inflater.inflate(R.layout.schedule_list,scheduleBox,false);
            if(list.getParent()!=null)
                ((ViewGroup)list.getParent()).removeView(list);
            scheduleBox.addView(list);
            scheduleBox.setVisibility(View.GONE);
            dropdownBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    scheduleBox.setVisibility(View.VISIBLE);
                    dropdownBtn.setBackground(getDrawable(R.drawable.more));
                }
            });
        }
    }

    public void addConcert(final LayoutInflater inflater){
        final LinearLayout concertBox = (LinearLayout) findViewById(R.id.addConcert);
        final ImageButton dropdownBtn2 = (ImageButton) findViewById(R.id.dropdown2);
        if(test_concert > 1){
            dropdownBtn2.setVisibility(View.VISIBLE);
            View list = inflater.inflate(R.layout.concert_list,concertBox);
            if(list.getParent()!=null)
                ((ViewGroup)list.getParent()).removeView(list);
            concertBox.addView(list);
            concertBox.setVisibility(View.GONE);
            dropdownBtn2.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    concertBox.setVisibility(View.VISIBLE);
                    dropdownBtn2.setBackground(getDrawable(R.drawable.more));
                }
            });
        }
    }
}