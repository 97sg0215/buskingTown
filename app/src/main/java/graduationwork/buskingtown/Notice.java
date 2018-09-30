package graduationwork.buskingtown;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class Notice extends AppCompatActivity {

    private RelativeLayout noticebox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);

        ImageButton backBtn = (ImageButton) findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Notice.super.onBackPressed();
            }
        });

        //버전정보 화면전환
        //noticebox = (RelativeLayout) findViewById(R.id.notice_list);
        //noticebox.setOnClickListener(new View.OnClickListener() {
            //@Override
            //public void onClick(View view) {
                //Intent noticeDetail = new Intent(getApplication(), NoticeDetail.class);
                //startActivity(noticeDetail);
            //}
        //});

    }
}
