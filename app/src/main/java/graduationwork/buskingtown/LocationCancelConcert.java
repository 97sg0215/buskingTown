package graduationwork.buskingtown;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import graduationwork.buskingtown.api.RestApiService;
import graduationwork.buskingtown.model.ConcertReservation;
import retrofit2.Callback;
import retrofit2.Response;

public class LocationCancelConcert extends Activity {

    RestApiService apiService;
    String user_token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_location_cancel_concert);

        restApiBuilder();
        getLocalData();

        int reservation_id = getIntent().getIntExtra("reservation_id", 0);

        Button cancelBtn = (Button) findViewById(R.id.cancelBtn);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                retrofit2.Call<ConcertReservation> call = apiService.cancelConcertReservation(user_token, reservation_id);
                call.enqueue(new Callback<ConcertReservation>() {
                    @Override
                    public void onResponse(retrofit2.Call<ConcertReservation> call, Response<ConcertReservation> response) {
                        if (response.isSuccessful()) {
                            Intent i = new Intent(getApplication(), ConcertReservationList.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(i);
                        }
                    }

                    @Override
                    public void onFailure(retrofit2.Call<ConcertReservation> call, Throwable t) {

                    }
                });
            }
        });

    }

    public void getLocalData() {
        SharedPreferences pref = getSharedPreferences("User", Activity.MODE_PRIVATE);
        SharedPreferences buskerPref = getSharedPreferences("BuskerUser", Activity.MODE_PRIVATE);
        user_token = pref.getString("auth_token", null);
    }

    public void restApiBuilder() {
        ApplicationController application = ApplicationController.getInstance();
        application.buildNetworkService();
        apiService = ApplicationController.getInstance().getRestApiService();
    }

}
