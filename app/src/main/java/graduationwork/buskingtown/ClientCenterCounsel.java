package graduationwork.buskingtown;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.util.ArrayList;

public class ClientCenterCounsel extends AppCompatActivity {

    private RelativeLayout counselLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clientcenter_counsel);

        ImageButton backBtn = (ImageButton) findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { ClientCenterCounsel.super.onBackPressed(); }
        });

        counselLayout= (RelativeLayout) findViewById(R.id. counselLayout);
        counselLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ClientCenterCounselDetail = new Intent(getApplication(),ClientCenterCounselDetail.class);
                startActivity(ClientCenterCounselDetail);
            }
        });

    }

    //백버튼 메소드
    public void previousActivity(View v){
        onBackPressed();
    }
}
