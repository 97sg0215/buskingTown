package graduationwork.buskingtown;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class ConcertReservation extends AppCompatActivity {

    String date,start_time,end_time,loc_name,address;
    int provide,reservation_id;

    TextView date_text, time_text, loc_name_text, address_text;

    Button reject, update;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_concert_reservation);

        ImageButton backBtn = (ImageButton) findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { ConcertReservation.super.onBackPressed(); }
        });

        date = getIntent().getStringExtra("date");
        start_time = getIntent().getStringExtra("start_time");
        end_time = getIntent().getStringExtra("end_time");
        loc_name = getIntent().getStringExtra("loc_name");
        provide = getIntent().getIntExtra("provide",0);
        address = getIntent().getStringExtra("address");
        reservation_id = getIntent().getIntExtra("reservation_id",0);

        date_text = (TextView) findViewById(R.id.showDateIn);
        time_text = (TextView) findViewById(R.id.showTimeIn);
        loc_name_text = (TextView) findViewById(R.id.locationNameIn);
        address_text = (TextView) findViewById(R.id.addressIn);

        date_text.setText(date);
        time_text.setText(start_time +" ~ "+end_time);
        loc_name_text.setText(loc_name);
        address_text.setText(address);

        reject = (Button) findViewById(R.id.reject);
        reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cacel = new Intent(getApplication(),LocationCancelConcert.class);
                cacel.putExtra("reservation_id",reservation_id);
                startActivity(cacel);
            }
        });
    }
    public void previousActivity(View v){
        onBackPressed();
    }
}

