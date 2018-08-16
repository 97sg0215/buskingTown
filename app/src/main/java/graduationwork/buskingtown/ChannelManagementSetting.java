package graduationwork.buskingtown;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

public class ChannelManagementSetting extends AppCompatActivity {

    private RelativeLayout buskerTeamLayout,statsLayout,coinLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channel_management_setting);

        ImageButton backBtn = (ImageButton) findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { ChannelManagementSetting.super.onBackPressed(); }
        });

        //팀원관리
        buskerTeamLayout = (RelativeLayout) findViewById(R.id.buskerTeamLayout);
        buskerTeamLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent MemberManagement = new Intent(getApplication(),MemberManagement.class);
                startActivity(MemberManagement);
            }
        });

        //거리공연,연습실,콘서트


        //통계보기
        statsLayout = (RelativeLayout) findViewById(R.id.coinLayout);
        statsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Statistics = new Intent(getApplication(),Statistics.class);
                startActivity(Statistics);
            }
        });


        //코인관리
        coinLayout = (RelativeLayout) findViewById(R.id.coinLayout);
        coinLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent CoinManagement = new Intent(getApplication(),CoinManagement.class);
                startActivity(CoinManagement);
            }
        });

        //채널삭제

    }

    public void previousActivity(View v){
        onBackPressed();
    }
}
