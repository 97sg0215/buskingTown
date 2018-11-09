package graduationwork.buskingtown;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import graduationwork.buskingtown.api.RestApiService;
import graduationwork.buskingtown.model.LendLocation;

import graduationwork.buskingtown.model.LendLocationOption;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ChannelBuskerPracticeroom extends Fragment {

    private RestApiService apiService;

    SharedPreferences prefUser;

    String user_token;


    List<LendLocationOption> lendLocationOptions ;
    int user_id;

    LinearLayout practiceroomBox;

    ImageView lendImage;

    TextView practice_name_text, room_address_text, room_price_text;
    String room_image ,practice_name, room_address, room_price;

    public ChannelBuskerPracticeroom(){
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.activity_channel_busker_practiceroom, container, false);

        //로딩코드
        CheckTypesTask task = new CheckTypesTask();
        task.execute();

        prefUser = this.getActivity().getSharedPreferences("User", Activity.MODE_PRIVATE);

        getLocalData();

        restApiBuilder();

        practiceroomBox = (LinearLayout) v.findViewById(R.id.practiceroomContainer);

        TextView practiceRoomText = (TextView)v.findViewById(R.id.practiceroomText);

        Call<List<LendLocation>> callPracticeRoom = apiService.practiceRoomList(user_token);
        callPracticeRoom.enqueue(new Callback<List<LendLocation>>() {
            @Override
            public void onResponse(Call<List<LendLocation>> call, Response<List<LendLocation>> response) {
                if(response.isSuccessful()){
                    List<LendLocation> practiceList = response.body();
                    if(practiceList.size()!=0){
                        for (int i =0; i<practiceList.size(); i++){
                            practiceRoomText.setVisibility(View.GONE);
                            View practiceroomlist = inflater.inflate(R.layout.practiceroombox,practiceroomBox,false);

                            lendImage = (ImageView) practiceroomlist.findViewById(R.id.lendImage);
                            practice_name_text = (TextView) practiceroomlist.findViewById(R.id.practiceroomName);
                            room_address_text = (TextView) practiceroomlist.findViewById(R.id.practiceroomAddress);


                            room_image = practiceList.get(i).getProvide_image();
                            practice_name = practiceList.get(i).getProvide_location_name();
                            room_address = practiceList.get(i).getProvide_location();

                            practice_name_text.setText(practice_name);
                            room_address_text.setText(room_address);

                            int SDK_INT = android.os.Build.VERSION.SDK_INT;
                            if (SDK_INT > 8) {
                                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                                        .permitAll().build();
                                StrictMode.setThreadPolicy(policy);
                                lendImage.setImageBitmap(getBitmapFromURL(room_image));
                                lendImage.setScaleType(ImageView.ScaleType.FIT_XY);
                            }

                            Call<List<LendLocationOption>> callOptionList = apiService.provideOptionList(user_token,practiceList.get(i).getProvide_id());
                            callOptionList.enqueue(new Callback<List<LendLocationOption>>() {
                                @Override
                                public void onResponse(Call<List<LendLocationOption>> call, Response<List<LendLocationOption>> response) {
                                    if(response.isSuccessful()){
                                        lendLocationOptions = response.body();
                                        room_price_text = (TextView) practiceroomlist.findViewById(R.id.practiceroomPrice);
                                        room_price_text.setText(lendLocationOptions.get(0).getProvide_price() + " 원 / 시");
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

                            if(practiceroomlist.getParent()!= null)
                                ((ViewGroup)practiceroomlist.getParent()).removeView(practiceroomlist);
                            practiceroomBox.addView(practiceroomlist);


                            ImageButton detailBtn = (ImageButton)practiceroomlist.findViewById(R.id.practiceRoomDetail);
                            int finalI = i;
                            detailBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent detail = new Intent(getActivity(),ChannelBuskerPracticeroomDetail.class);
                                    detail.putExtra("provide_id",practiceList.get(finalI).getProvide_id());
                                    detail.putExtra("provide_image",practiceList.get(finalI).getProvide_image());
                                    detail.putExtra("provide_name",practiceList.get(finalI).getProvide_location_name());
                                    detail.putExtra("provide_email",practiceList.get(finalI).getProvider_email());
                                    detail.putExtra("provide_address",practiceList.get(finalI).getProvide_location());
                                    detail.putExtra("provider_phone",practiceList.get(finalI).getProvider_phone());
                                    detail.putExtra("provide_description",practiceList.get(finalI).getProvide_description());
                                    detail.putExtra("provide_rule",practiceList.get(finalI).getProvide_rule());
                                    detail.putExtra("provide_refund_rule",practiceList.get(finalI).getProvide_refund_rule());
                                    detail.putExtra("provide_start_time",practiceList.get(finalI).getProvide_start_time());
                                    detail.putExtra("provide_end_time",practiceList.get(finalI).getProvide_end_time());
                                    detail.putExtra("provide_rule",practiceList.get(finalI).getProvide_rule());
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
