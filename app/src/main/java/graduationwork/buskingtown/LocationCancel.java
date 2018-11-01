package graduationwork.buskingtown;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telecom.Call;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import graduationwork.buskingtown.api.RestApiService;
import graduationwork.buskingtown.model.PracticeReservation;
import okhttp3.ResponseBody;
import retrofit2.Callback;
import retrofit2.Response;

public class LocationCancel extends Activity {

    RestApiService apiService;
    String user_token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_location_cancel);

        restApiBuilder();
        getLocalData();


        int reservation_id = getIntent().getIntExtra("reservation_id",0);

        Button cancelBtn = (Button) findViewById(R.id.cancelBtn);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                retrofit2.Call<PracticeReservation> call = apiService.cancelReservation(user_token,reservation_id);
                call.enqueue(new Callback<PracticeReservation>() {
                    @Override
                    public void onResponse(retrofit2.Call<PracticeReservation> call, Response<PracticeReservation> response) {
                        if(response.isSuccessful()){
                            Intent i = new Intent(getApplication(),PracticeroomReservationList.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(i);
                        }
                    }

                    @Override
                    public void onFailure(retrofit2.Call<PracticeReservation> call, Throwable t) {

                    }
                });

            }
        });
    }

    public void getLocalData(){
        SharedPreferences pref = getSharedPreferences("User", Activity.MODE_PRIVATE);
        SharedPreferences buskerPref = getSharedPreferences("BuskerUser", Activity.MODE_PRIVATE);
        user_token = pref.getString("auth_token",null);
    }

    public void restApiBuilder() {
        ApplicationController application = ApplicationController.getInstance();
        application.buildNetworkService();
        apiService = ApplicationController.getInstance().getRestApiService();
    }
}