package graduationwork.buskingtown;


import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

public class CoinSendBefore_pop extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_coin_send_before_pop);
    }
}
