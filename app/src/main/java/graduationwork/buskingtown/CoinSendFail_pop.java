package graduationwork.buskingtown;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

public class CoinSendFail_pop extends Activity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_coin_send_fail_pop);

        findViewById(R.id.delete).setOnClickListener(this);

        findViewById(R.id.coinChargeBtn).setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent coinCharge = new Intent(getApplicationContext(), CoinCharge.class);
                startActivity(coinCharge);
            }
        });
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.delete:
                this.finish();
                break;
        }
    }
}
