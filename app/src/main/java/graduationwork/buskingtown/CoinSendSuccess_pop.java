package graduationwork.buskingtown;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

public class CoinSendSuccess_pop extends Activity implements View.OnClickListener{

    Button coinSendCheckBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_coin_send_success_pop);

        findViewById(R.id.delete).setOnClickListener(this);

        coinSendCheckBtn = (Button) findViewById(R.id.coinSendCheckBtn);
        coinSendCheckBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplication(),MycoinHouse.class);
                startActivity(i);
            }
        });
    }
    public void onClick(View v){
        switch (v.getId()){
            case R.id.delete:
                this.finish();
                break;
        }
    }
}
