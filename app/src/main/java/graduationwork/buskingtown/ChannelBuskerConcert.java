package graduationwork.buskingtown;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

public class ChannelBuskerConcert extends Fragment {

    private RestApiService apiService;
    SharedPreferences prefUser;

    String user_token;
    List<LendLocationOption> lendLocationOptions ;
    int user_id;

    LinearLayout concertBox;
    ImageView lendImage;

    TextView concert_text, concert_address_text, concert_price_text;
    String concert_image , concert_name, concert_address, concert_price;

    int test__concert=3;

    public ChannelBuskerConcert(){
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.activity_channel_busker_concert, container, false);

        //로딩코드
        CheckTypesTask task = new CheckTypesTask();
        task.execute();

        prefUser = this.getActivity().getSharedPreferences("User", Activity.MODE_PRIVATE);

        getLocalData();

        restApiBuilder();

        concertBox = (LinearLayout) v.findViewById(R.id.concertContainer);

        TextView concertText = (TextView)v.findViewById(R.id.concertText);

        Call<List<LendLocation>> callConcert = apiService.concertRoomList(user_token);
        callConcert.enqueue(new Callback<List<LendLocation>>() {
            @Override
            public void onResponse(Call<List<LendLocation>> call, Response<List<LendLocation>> response) {
                if(response.isSuccessful()){
                    List<LendLocation> concertList = response.body();
                    if(concertList.size()!=0){
                        for (int i =0; i<concertList.size(); i++){
                            concertText.setVisibility(View.GONE);
                            View concertlist = inflater.inflate(R.layout.concertbox,concertBox,false);

                            lendImage = (ImageView) concertlist.findViewById(R.id.lendImage);
                            concert_text = (TextView) concertlist.findViewById(R.id.practiceroomName);
                            concert_address_text = (TextView) concertlist.findViewById(R.id.practiceroomAddress);


                            concert_image = concertList.get(i).getProvide_image();
                            concert_name = concertList.get(i).getProvide_location_name();
                            concert_address = concertList.get(i).getProvide_location();

                            concert_text.setText(concert_name);
                            concert_address_text.setText(concert_address);

                            int SDK_INT = android.os.Build.VERSION.SDK_INT;
                            if (SDK_INT > 8) {
                                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                                        .permitAll().build();
                                StrictMode.setThreadPolicy(policy);
                                lendImage.setImageBitmap(getBitmapFromURL(concert_image));
                                lendImage.setScaleType(ImageView.ScaleType.FIT_XY);
                            }

                            Call<List<LendLocationOption>> callOptionList = apiService.provideOptionList(user_token,concertList.get(i).getProvide_id());
                            callOptionList.enqueue(new Callback<List<LendLocationOption>>() {
                                @Override
                                public void onResponse(Call<List<LendLocationOption>> call, Response<List<LendLocationOption>> response) {
                                    if(response.isSuccessful()){
                                        lendLocationOptions = response.body();
                                        concert_price_text = (TextView) concertlist.findViewById(R.id.practiceroomPrice);
                                        concert_price_text.setText(lendLocationOptions.get(0).getProvide_price() + " 원 / 시");
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

                            if(concertlist.getParent()!= null)
                                ((ViewGroup)concertlist.getParent()).removeView(concertlist);
                            concertBox.addView(concertlist);


                            LinearLayout detailBtn = (LinearLayout) concertlist.findViewById(R.id.concert_box);
                            int finalI = i;
                            detailBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent detail = new Intent(getActivity(),ChannelBuskerConcertDetail.class);
                                    detail.putExtra("provide_id",concertList.get(finalI).getProvide_id());
                                    detail.putExtra("provide_image",concertList.get(finalI).getProvide_image());
                                    detail.putExtra("provide_name",concertList.get(finalI).getProvide_location_name());
                                    detail.putExtra("provide_email",concertList.get(finalI).getProvider_email());
                                    detail.putExtra("provide_address",concertList.get(finalI).getProvide_location());
                                    detail.putExtra("provider_phone",concertList.get(finalI).getProvider_phone());
                                    detail.putExtra("provide_description",concertList.get(finalI).getProvide_description());
                                    detail.putExtra("provide_rule",concertList.get(finalI).getProvide_rule());
                                    detail.putExtra("provide_refund_rule",concertList.get(finalI).getProvide_refund_rule());
                                    detail.putExtra("provide_start_time",concertList.get(finalI).getProvide_start_time());
                                    detail.putExtra("provide_end_time",concertList.get(finalI).getProvide_end_time());
                                    detail.putExtra("provide_rule",concertList.get(finalI).getProvide_rule());
                                    startActivity(detail);
                                }
                            });
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
                Log.i(ApplicationController.TAG, "연습실 리스트 서버 연결 실패 Message : " + t.getMessage());
            }
        });



        return v;
    }

    public void restApiBuilder() {
        ApplicationController application = ApplicationController.getInstance();
        application.buildNetworkService();
        apiService = ApplicationController.getInstance().getRestApiService();
    }

    public void getLocalData(){
        user_token = prefUser.getString("auth_token",null);
        user_id = prefUser.getInt("user_id",0);
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

    //로딩코드, fragment일때는 getContext()를 씀
    private class CheckTypesTask extends AsyncTask<Void, Void, Void> {

        ProgressDialog asyncDialog = new ProgressDialog(
                getContext());

        @Override
        protected void onPreExecute() {
            asyncDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            asyncDialog.setMessage("로딩중입니다..");

            // show dialog
            asyncDialog.show();
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            try {
                for (int i = 0; i < 5; i++) {
                    //asyncDialog.setProgress(i * 30);
                    Thread.sleep(300);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            asyncDialog.dismiss();
            super.onPostExecute(result);
        }
    }

}
