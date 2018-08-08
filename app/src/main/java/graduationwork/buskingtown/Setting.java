package graduationwork.buskingtown;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Setting extends AppCompatActivity {

    private View versionInformation;
    private View noticeLayout;
    private View PassWdChangeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        ImageButton backBtn = (ImageButton) findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { Setting.super.onBackPressed(); }
        });

        //버전정보 화면전환
        versionInformation = (RelativeLayout) findViewById(R.id.versionInformation);
        versionInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent versionInformation = new Intent(getApplication(),VersionInformation.class);
                startActivity(versionInformation);
            }
        });

        //알림 화면전환
        noticeLayout = (RelativeLayout) findViewById(R.id.noticeLayout);
        noticeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent notice = new Intent(getApplication(),Notice.class);
                startActivity(notice);
            }
        });

        //비밀번호변경 화면전환
        PassWdChangeLayout = (RelativeLayout) findViewById(R.id.PassWdChangeLayout);
        PassWdChangeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent passwordchange = new Intent(getApplication(),PasswdChange.class);
                startActivity(passwordchange);
            }
        });

        //탈퇴하기 부분 만들어줘야함

    }

    //백버튼 메소드
    public void previousActivity(View v){
        onBackPressed();
    }
}