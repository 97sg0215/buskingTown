package graduationwork.buskingtown;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.mail.MessagingException;
import javax.mail.SendFailedException;

import graduationwork.buskingtown.api.RestApiService;
import graduationwork.buskingtown.model.PracticeReservation;
import retrofit2.Callback;
import retrofit2.Response;

public class ReservationCheck extends AppCompatActivity {

    RestApiService apiService;

    int busker_id, provide_id, option_id, fee;
    String user_token, practice_date, start_time, end_time, provide_image, provide_name, provide_address, option_name, team_name, phone, email, p_email, total_message;
    TextView practice_name, practice_add, rc_name, option_txt, date_txt, time_txt, team_txt, num_txt, email_txt, price_txt;
    ImageView room_image;
    Button confirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation_check);

        getLocalData();

        restApiBuilder();

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .permitDiskReads()
                .permitDiskWrites()
                .permitNetwork().build());


        provide_id = getIntent().getIntExtra("rc_id", 0);
        p_email = getIntent().getStringExtra("p_email");
        option_id = getIntent().getIntExtra("rc_option_id", 0);
        fee = getIntent().getIntExtra("rc_option_price", 0);
        provide_image = getIntent().getStringExtra("rc_image");
        provide_name = getIntent().getStringExtra("rc_name");
        provide_address = getIntent().getStringExtra("rc_address");
        option_name = getIntent().getStringExtra("rc_option_name");
        practice_date = getIntent().getStringExtra("rc_practice_date");
        start_time = getIntent().getStringExtra("rc_start_time");
        end_time = getIntent().getStringExtra("rc_end_time");
        team_name = getIntent().getStringExtra("rc_team_name");
        phone = getIntent().getStringExtra("rc_phone");
        email = getIntent().getStringExtra("rc_email");


        rc_name = (TextView) findViewById(R.id.rc_name);
        practice_name = (TextView) findViewById(R.id.practiceName);
        practice_add = (TextView) findViewById(R.id.practiceAdd);
        option_txt = (TextView) findViewById(R.id.choiceTextEx);
        date_txt = (TextView) findViewById(R.id.lentDateTextEx);
        time_txt = (TextView) findViewById(R.id.timeTextTextEx);
        team_txt = (TextView) findViewById(R.id.teamTextEx);
        num_txt = (TextView) findViewById(R.id.phoneNumberrTextEx);
        email_txt = (TextView) findViewById(R.id.emailTextEx);
        price_txt = (TextView) findViewById(R.id.price);
        room_image = (ImageView) findViewById(R.id.room_image);
        confirm = (Button) findViewById(R.id.confirmBtn);


        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            room_image.setImageBitmap(getBitmapFromURL(provide_image));
            room_image.setScaleType(ImageView.ScaleType.FIT_XY);
        }

        rc_name.setText(provide_name);
        practice_name.setText(provide_name);
        practice_add.setText(provide_address);
        option_txt.setText(option_name);
        date_txt.setText(practice_date);
        String time = start_time + " ~ " + end_time;
        time_txt.setText(time);
        team_txt.setText(team_name);
        num_txt.setText(phone);
        email_txt.setText(email);
        price_txt.setText(String.valueOf(fee));


        //아래는 뒤로가기 버튼 클릭시 뒤로가는거임
        ImageButton backBtn = (ImageButton) findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReservationCheck.super.onBackPressed();
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reservation(provide_id, option_id, practice_date, start_time, end_time, fee);
            }
        });

    }

    public void reservation(int provide_id, int provide_option, String date, String start_time, String end_time, int fee) {

        PracticeReservation practiceReservation = new PracticeReservation();
        practiceReservation.setBusker(busker_id);
        practiceReservation.setProvide(provide_id);
        practiceReservation.setProvide_option(provide_option);
        practiceReservation.setPractice_date(date);
        practiceReservation.setPractice_start_time(start_time);
        practiceReservation.setPractice_end_time(end_time);
        practiceReservation.setPractice_fee(fee);

        total_message = "연습실명: " + provide_name + "\n"
                + "옵션명: " + option_name + "\n"
                + "예약날짜: " + date + "\n"
                + "예약시간: " + start_time + " ~ " + end_time + "\n\n"
                + "예약자 정보" + "\n"
                + "예약자명: " + team_name + "\n"
                + "예약자번호: " + phone + "\n"
                + "예약자이메일: " + email + "\n";

        retrofit2.Call<PracticeReservation> reservationCall = apiService.reservationPractice(user_token, practiceReservation);
        reservationCall.enqueue(new Callback<PracticeReservation>() {
            @Override
            public void onResponse(retrofit2.Call<PracticeReservation> call, Response<PracticeReservation> response) {
                if (response.isSuccessful()) {
                    Intent intent = new Intent(getApplication(), ChannelBusker.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    Toast.makeText(ReservationCheck.this, "예약이 완료 되었습니다!", Toast.LENGTH_SHORT).show();
                    new senmailAsync().execute();
                } else {
                    int StatusCode = response.code();
                    Log.i(ApplicationController.TAG, "예약 상태 Code : " + StatusCode);
                }
            }

            @Override
            public void onFailure(retrofit2.Call<PracticeReservation> call, Throwable t) {
            }
        });

    }

    public void restApiBuilder() {
        ApplicationController application = ApplicationController.getInstance();
        application.buildNetworkService();
        apiService = ApplicationController.getInstance().getRestApiService();
    }

    public void getLocalData() {
        SharedPreferences pref = getSharedPreferences("User", Activity.MODE_PRIVATE);
        SharedPreferences buskerPref = getSharedPreferences("BuskerUser", Activity.MODE_PRIVATE);
        user_token = pref.getString("auth_token", null);
        busker_id = buskerPref.getInt("busker_id", 0);
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
        } finally {
            if (connection != null) connection.disconnect();
        }
    }

    public void previousActivity(View v) {
        onBackPressed();
    }

    private class senmailAsync extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                GMailSender gMailSender = new GMailSender("buskingtown2018@gmail.com", "khphTown123");
                gMailSender.sendMail("[버스킹타운] 연습실 예약 신청입니다.", total_message, p_email);
            } catch (SendFailedException e) {//   Toast.makeText(getActivity().getApplicationContext(), "이메일 형식이 잘못되었습니다.", Toast.LENGTH_SHORT).show();
            } catch (MessagingException e) {
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
