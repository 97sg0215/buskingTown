package graduationwork.buskingtown;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

public class CoinSendSuccess_pop extends Activity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_coin_send_success_pop);

        findViewById(R.id.delete).setOnClickListener(this);
    }
    public void onClick(View v){
        switch (v.getId()){
            case R.id.delete:
                this.finish();
                break;
        }
    }
}
