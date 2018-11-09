package graduationwork.buskingtown;

import android.app.Activity;
import android.app.Fragment;
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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.view.LayoutInflater;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import graduationwork.buskingtown.api.RestApiService;
import graduationwork.buskingtown.model.LendLocationOption;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChannelBuskerPracticeroomDetail extends AppCompatActivity {

    String p_name;
    String p_email;
    String p_phone;
    String p_info;
    String p_rule;
    String p_refund_rule;
    String p_address;
    String p_image;
    String p_start_time;
    String p_end_time;
    String user_token;
    String choice_option_price;
    String choice_option_name;
    Integer choice_option;

    int user_id, p_id;

    SharedPreferences prefUser;

    ArrayList<Integer> o_id = new ArrayList<Integer>();
    ArrayList<String> o_price = new ArrayList<String>();
    ArrayList<String> o_name = new ArrayList<String>();

    List<LendLocationOption> lendLocationOptions ;

    ImageView location_image;

    RadioGroup optionRadioGroup;
    RadioButton optionList;

    LinearLayout addRule, addRefund;

    ImageButton dropdown_sch, dropdown_sch_1;

    Button go_reservation;

    TextView loc_name,loc_main_name, loc_address, option, loc_info, loc_rule, loc_refund_rule, addRule_text,addRefundRule_text, sum_fee;

    RestApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channel_busker_practiceroom_detail);

        prefUser = this.getSharedPreferences("User", Activity.MODE_PRIVATE);

        getLocalData();

        restApiBuilder();

        p_id = getIntent().getIntExtra("provide_id",0);
        p_name = getIntent().getStringExtra("provide_name");
        p_info = getIntent().getStringExtra("provide_description");
        p_phone = getIntent().getStringExtra("provider_phone");
        p_address = getIntent().getStringExtra("provide_address");
        p_refund_rule = getIntent().getStringExtra("provide_refund_rule");
        p_rule = getIntent().getStringExtra("provide_rule");
        p_image = getIntent().getStringExtra("provide_image");
        p_start_time = getIntent().getStringExtra("provide_start_time");
        p_end_time = getIntent().getStringExtra("provide_end_time");
        p_email = getIntent().getStringExtra("provide_email");

        location_image = (ImageView) findViewById(R.id.practiceRoomImage);
        optionRadioGroup = (RadioGroup) findViewById(R.id.optionRadioGroup);
        loc_main_name = (TextView) findViewById(R.id.busker_main_team_name);
        loc_name = (TextView) findViewById(R.id.practiceRoomName);
        loc_address = (TextView) findViewById(R.id.practiceRoomAddress);
        loc_info = (TextView) findViewById(R.id.description);
        addRule_text = (TextView) findViewById(R.id.addRule_text);
        dropdown_sch = (ImageButton) findViewById(R.id.dropdown_sch);
        addRefundRule_text = (TextView) findViewById(R.id.addRule_text_1);
        dropdown_sch_1 = (ImageButton) findViewById(R.id.dropdown_sch_1);
        addRule = (LinearLayout) findViewById(R.id.addRule);
        addRefund =(LinearLayout) findViewById(R.id.addRefund);
        sum_fee = (TextView) findViewById(R.id.sum_fee);

        loc_main_name.setText(p_name);
        loc_name.setText(p_name);

        loc_address.setText(p_address);

        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);

            location_image .setImageBitmap(getBitmapFromURL(p_image));
            location_image .setScaleType(ImageView.ScaleType.FIT_XY);
        }

        Call<List<LendLocationOption>> callOptionList = apiService.provideOptionList(user_token,p_id);
        callOptionList.enqueue(new Callback<List<LendLocationOption>>() {
            @Override
            public void onResponse(Call<List<LendLocationOption>> call, Response<List<LendLocationOption>> response) {
                if(response.isSuccessful()){
                    lendLocationOptions = response.body();
                    for(int i=0;i<lendLocationOptions.size();i++){
                        optionList = (RadioButton) getLayoutInflater().inflate(R.layout.reservation_practiceroom_detail_radio,optionRadioGroup,false);
                        optionList.setText(lendLocationOptions.get(i).getProvide_option_name() +" : "+lendLocationOptions.get(i).getProvide_price() + " 원/시");
                        o_price.add(lendLocationOptions.get(i).getProvide_price());
                        optionList.setId(i);
                        o_id.add(lendLocationOptions.get(i).getProvide_option_id());
                        o_name.add((lendLocationOptions.get(i).getProvide_option_name()));
                        if(optionList.getParent()!= null)
                            ((ViewGroup)optionList.getParent()).removeView(optionList);
                        optionRadioGroup.addView(optionList);
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
            public void onFailure(Call<List<LendLocationOption>> call, Throwable t) {

            }
        });

        loc_info.setText(p_info);


        String[] phone_words = p_phone.split("-");

        String tel = "tel:"+phone_words[0]+phone_words[1]+phone_words[2];

        ImageButton phone = (ImageButton) findViewById(R.id.phone);
        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent("android.intent.action.DIAL", Uri.parse(tel)));
            }
        });


        dropdown_sch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addRule.setVisibility(View.VISIBLE);
                addRule_text.setText(p_rule);
                dropdown_sch.setVisibility(View.GONE);
            }
        });

        dropdown_sch_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addRefund.setVisibility(View.VISIBLE);
                addRefundRule_text.setText(p_refund_rule);
                dropdown_sch_1.setVisibility(View.GONE);
            }
        });

        optionRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                sum_fee.setText(o_price.get(checkedId) + " 원 / 시");
                choice_option = o_id.get(checkedId);
                choice_option_name = o_name.get(checkedId);
                choice_option_price = o_price.get(checkedId);

            }
        });

        go_reservation = (Button) findViewById(R.id.go_reservation);
        go_reservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent goReservaion = new Intent(getApplication(), PracticeRoomReservation.class);
                goReservaion.putExtra("provide_id",p_id);
                goReservaion.putExtra("provide_image",p_image);
                goReservaion.putExtra("provide_name",p_name);
                goReservaion.putExtra("provide_email",p_email);
                goReservaion.putExtra("provide_address",p_address);
                goReservaion.putExtra("provide_description",p_info);
                goReservaion.putExtra("provide_option_id", choice_option);
                goReservaion.putExtra("provide_option_price", choice_option_price);
                goReservaion.putExtra("provide_option_name",choice_option_name);
                goReservaion.putExtra("provide_start_time",p_start_time);
                goReservaion.putExtra("provide_end_time",p_end_time);
                Log.e("옵션아이디",String.valueOf(choice_option));
                startActivity(goReservaion);
                finish();
            }
        });
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


    public void restApiBuilder() {
        ApplicationController application = ApplicationController.getInstance();
        application.buildNetworkService();
        apiService = ApplicationController.getInstance().getRestApiService();
    }

    public void getLocalData(){
        user_token = prefUser.getString("auth_token",null);
        user_id = prefUser.getInt("user_id",0);
    }

}
