package graduationwork.buskingtown;

import android.app.Activity;
import android.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.view.LayoutInflater;

public class ChannelBuskerPracticeroomDetail extends AppCompatActivity {

    int test__refund;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channel_busker_practiceroom_detail);

        for (int refundCount=0; refundCount<test__refund; refundCount++) {
            ImageButton dropdownBtn = (ImageButton) findViewById(R.id.dropdown_sch);
            LinearLayout refundBox = (LinearLayout) findViewById(R.id.addRule);

            if (test__refund > 1) {
                dropdownBtn.setVisibility(View.VISIBLE);
                View refund= getLayoutInflater().inflate(R.layout.concert_list, refundBox, false);
                if (refund.getParent() != null)
                    ((ViewGroup) refund.getParent()).removeView(refund);
                refundBox.addView(refund);
                refundBox.setVisibility(View.GONE);
                dropdownBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        refundBox.setVisibility(View.VISIBLE);
                        dropdownBtn.setVisibility(View.INVISIBLE);
                    }
                });
            }
        }
    }
}
