package graduationwork.buskingtown;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import graduationwork.buskingtown.api.RestApiService;
import graduationwork.buskingtown.model.LendLocation;

import graduationwork.buskingtown.model.LendLocationOption;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static graduationwork.buskingtown.api.RestApiService.API_URL;

public class LocationLendStart extends AppCompatActivity {

    private RestApiService apiService;

    String user_token,option_list;
    int user_id;

    View lendList;
    LinearLayout lendBox;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_lend_start);

        restApiBuilder();

        getLocalData();

        ImageButton backBtn = (ImageButton) findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocationLendStart.super.onBackPressed();
            }
        });

         lendBox = (LinearLayout) findViewById(R.id.lendContainer);


        Call<List<LendLocation>> provideList = apiService.provideList(user_token,user_id);
        provideList.enqueue(new Callback<List<LendLocation>>() {
            @Override
            public void onResponse(Call<List<LendLocation>> call, Response<List<LendLocation>> response) {
                if (response.isSuccessful()){
                    List<LendLocation> lendLocation = response.body();
                    if(lendLocation.size()!=0){
                        for (int i=0; i<lendLocation.size();i++){
                            lendList = getLayoutInflater().inflate(R.layout.lendbox,lendBox,false);


                            ImageView rentImg = (ImageView)lendList.findViewById(R.id.lendImage);
                            TextView loc_name_text = (TextView) lendList.findViewById(R.id.practiceroomName);
                            TextView loc_text = (TextView) lendList.findViewById(R.id.practiceroomAddress);

                            String loc_image = API_URL + lendLocation.get(i).getProvide_image();
                            String loc_name = lendLocation.get(i).getProvide_location_name();
                            String loc = lendLocation.get(i).getProvide_location();

                            int SDK_INT = android.os.Build.VERSION.SDK_INT;
                            if (SDK_INT > 8) {
                                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                                        .permitAll().build();
                                StrictMode.setThreadPolicy(policy);

                                rentImg.setImageBitmap(getBitmapFromURL(loc_image));
                                rentImg.setScaleType(ImageView.ScaleType.FIT_XY);
                            }

                            loc_name_text.setText(loc_name);
                            loc_text.setText(loc);

                            //옵션 가격 세팅
                            TextView price_text = (TextView) lendList.findViewById(R.id.practiceroomPrice);
                            Call<List<LendLocationOption>> callOptionList = apiService.provideOptionList(user_token,lendLocation.get(i).getProvide_id());
                            callOptionList.enqueue(new Callback<List<LendLocationOption>>() {
                                @Override
                                public void onResponse(Call<List<LendLocationOption>> call, Response<List<LendLocationOption>> response) {
                                    if(response.isSuccessful()){
                                        List<LendLocationOption> lendLocationOptions = response.body();
                                        option_list = lendLocationOptions.get(0).getProvide_price();
                                        price_text.setText(option_list);
                                    }else {
                                        int StatusCode = response.code();
                                        Log.i(ApplicationController.TAG, "상태 Code : " + StatusCode);
                                        Log.e("메세지", String.valueOf(response.message()));
                                        Log.e("리스폰스에러바디", String.valueOf(response.errorBody()));
                                        Log.e("리스폰스바디", String.valueOf(response.body()));
                                    }
                                }

                                @Override
                                public void onFailure(Call<List<LendLocationOption>> call, Throwable t) {

                                }
                            });

                            if(lendList.getParent()!= null)
                                ((ViewGroup)lendList.getParent()).removeView(lendList);
                            lendBox.addView(lendList);
                        }
                    }
                }else {
                    int StatusCode = response.code();
                    Log.i(ApplicationController.TAG, "상태 Code : " + StatusCode);
                    Log.e("메세지", String.valueOf(response.message()));
                    Log.e("리스폰스에러바디", String.valueOf(response.errorBody()));
                    Log.e("리스폰스바디", String.valueOf(response.body()));
                }
            }

            @Override
            public void onFailure(Call<List<LendLocation>> call, Throwable t) {
                Log.i(ApplicationController.TAG, "사용자 장소제공 리스트 서버 연결 실패 Message : " + t.getMessage());
            }
        });

    }

    public void addLocation(View view){
        Intent message = new Intent(getApplication(),LocationLend.class);
        startActivity(message);
        }


    public void restApiBuilder() {
        ApplicationController application = ApplicationController.getInstance();
        application.buildNetworkService();
        apiService = ApplicationController.getInstance().getRestApiService();
    }

    public void getLocalData(){
        SharedPreferences pref = getSharedPreferences("User", Activity.MODE_PRIVATE);
        user_token = pref.getString("auth_token",null);
        user_id = pref.getInt("user_id",0);
    }

    public void previousActivity(View v){
        onBackPressed();
    }

    public Bitmap getBitmapFromURL(String src) {
        HttpURLConnection connection = null;
        try {
            URL url = new URL(src);
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }finally{
            if(connection!=null)connection.disconnect();
        }
    }
}
