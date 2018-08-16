package graduationwork.buskingtown;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

public class ClientCenter extends AppCompatActivity {

    private RelativeLayout counselbox,conditionsbox,locationbasedbox,personalifbox,opensourcebox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clientcenter);

        ImageButton backBtn = (ImageButton) findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { ClientCenter.super.onBackPressed(); }
        });

        //도움말
        counselbox = (RelativeLayout) findViewById(R.id.counselBox);
        counselbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ClientCenterCounsel = new Intent(getApplication(),ClientCenterCounsel.class);
                startActivity(ClientCenterCounsel);
            }
        });

        //문의하기 화면만들어야함


        //이용약관
        conditionsbox = (RelativeLayout) findViewById(R.id. conditionsBox);
        conditionsbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ClientCenterConditions = new Intent(getApplication(),ClientCenterConditions.class);
                startActivity(ClientCenterConditions);
            }
        });

        //위치기반서비스 이용약관
        locationbasedbox = (RelativeLayout) findViewById(R.id. locationbasedBox);
        locationbasedbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ClientCenterLocationBased = new Intent(getApplication(),ClientCenterLocationBased.class);
                startActivity(ClientCenterLocationBased);
            }
        });

        //개인정보처리방침
        personalifbox = (RelativeLayout) findViewById(R.id. personalifBox);
        personalifbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ClientCenterPersonalInformation = new Intent(getApplication(),ClientCenterPersonalInformation.class);
                startActivity(ClientCenterPersonalInformation);
            }
        });

        //오픈소스
        opensourcebox = (RelativeLayout) findViewById(R.id. opensourceBox);
        opensourcebox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ClientCenterOpenSource = new Intent(getApplication(),ClientCenterOpenSource.class);
                startActivity(ClientCenterOpenSource);
            }
        });
    }

    //백버튼 메소드
    public void previousActivity(View v){
        onBackPressed();
    }
}
