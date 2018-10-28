package graduationwork.buskingtown;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class ReservationCheck extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation_check);

        //아래는 뒤로가기 버튼 클릭시 뒤로가는거임
        ImageButton backBtn = (ImageButton) findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { ReservationCheck.super.onBackPressed(); }
        });
    }
    public void previousActivity(View v){
        onBackPressed();
    }
}
