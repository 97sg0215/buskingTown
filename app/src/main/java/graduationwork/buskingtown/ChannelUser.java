package graduationwork.buskingtown;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class ChannelUser extends AppCompatActivity {

    int test_schedule=5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channel_user);

        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        for (int scheduleCount=0; scheduleCount<test_schedule; scheduleCount++) {
            addSchedule(inflater);
            Log.e("for문 횟수",String.valueOf(scheduleCount));
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
}
