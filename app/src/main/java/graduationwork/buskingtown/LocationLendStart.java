package graduationwork.buskingtown;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class LocationLendStart extends AppCompatActivity {

    int test__lend=3;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_lend_start);

        ImageButton backBtn = (ImageButton) findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocationLendStart.super.onBackPressed();
            }
        });

        for (int lendCount=0; lendCount<test__lend; lendCount++) {
            LinearLayout lendBox = (LinearLayout) findViewById(R.id.lendContainer);

            if (test__lend > 1 ){
                View lendlist = getLayoutInflater().inflate(R.layout.lendbox,lendBox,false);
                if(lendlist.getParent()!= null)
                    ((ViewGroup)lendlist.getParent()).removeView(lendlist);
                lendBox.addView(lendlist);
            }
        }
    }


    public void addLocation(View view){
        Intent message = new Intent(getApplication(),LocationLend.class);
        startActivity(message);
        }

    public void previousActivity(View v){
        onBackPressed();
    }
}
