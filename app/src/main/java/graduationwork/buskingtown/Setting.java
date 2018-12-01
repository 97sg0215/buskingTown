package graduationwork.buskingtown;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

public class Setting extends AppCompatActivity {

    private RelativeLayout versionInformation, settingnoticeLayout, PassWdChangeLayout, withdrawal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        ImageButton backBtn = (ImageButton) findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Setting.super.onBackPressed();
            }
        });

        //버전정보
        versionInformation = (RelativeLayout) findViewById(R.id.versionInformation);
        versionInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent versionInformation = new Intent(getApplication(), VersionInformation.class);
                startActivity(versionInformation);
            }
        });

        //알림
        settingnoticeLayout = (RelativeLayout) findViewById(R.id.settingnoticeLayout);
        settingnoticeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent settingnotice = new Intent(getApplication(), SettingNotice.class);
                startActivity(settingnotice);
            }
        });

        //비밀번호변경
        PassWdChangeLayout = (RelativeLayout) findViewById(R.id.PassWdChangeLayout);
        PassWdChangeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent passwordchange = new Intent(getApplication(), PasswdChange.class);
                startActivity(passwordchange);
            }
        });

        //탈퇴하기 부분 - 수경이가 합니당.
        withdrawal = (RelativeLayout) findViewById(R.id.withdrawal);
        withdrawal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent withdrawalAct = new Intent(getApplication(), WithdrawalPop.class);
                startActivity(withdrawalAct);
            }
        });
    }

    //백버튼 메소드
    public void previousActivity(View v) {
        onBackPressed();
    }
}