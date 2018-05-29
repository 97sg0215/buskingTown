package graduationwork.buskingtown;

import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.tsongkha.spinnerdatepicker.DatePicker;
import com.tsongkha.spinnerdatepicker.DatePickerDialog;
import com.tsongkha.spinnerdatepicker.SpinnerDatePickerDialogBuilder;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;


import graduationwork.buskingtown.model.SignUp;

public class SignUp2Step extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    //이전 액티비티에서 받아온 회원가입 포스트할 변수
    String userEmail, userPassword;
    int birthYear, birthMonth, birthDay;
    SimpleDateFormat simpleDateFormat;

    TextView birthTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up2_step);

        //이전 액티비티에서 이메일, 비밀번호 받아와서 이어서 작성
        userEmail = getIntent().getStringExtra("email");
        Log.e("이메일",String.valueOf(userEmail));
        userPassword = getIntent().getStringExtra("password");
        Log.e("비밀번호",String.valueOf(userPassword));
        birthTextView = (TextView) findViewById(R.id.birth);

        //날짜형태
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);

        //달력 객체 불러옴
        final GregorianCalendar calendar = new GregorianCalendar();
        birthYear = calendar.get(Calendar.YEAR);
        birthMonth = calendar.get(Calendar.MONTH);
        birthDay = calendar.get(Calendar.DAY_OF_MONTH);

        //생년월일 클릭시 데이트피커
        TextView birthBtn = (TextView) findViewById(R.id.birth);
        birthBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDate(birthYear, birthMonth, birthDay, R.style.DatePickerSpinner);
            }
        });
    }

    //백버튼 메소드
    public void previousActivity(View v){
        onBackPressed();
    }

    //백버튼 2번 종료
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    //데이트피커다이얼로그 보여주는 메소드
    void showDate(int year, int monthOfYear, int dayOfMonth, int spinnerTheme) {
        new SpinnerDatePickerDialogBuilder()
                .context(SignUp2Step.this)
                .callback(SignUp2Step.this)
                .spinnerTheme(spinnerTheme)
                .showTitle(true)
                .defaultDate(year, monthOfYear, dayOfMonth)
                .build()
                .show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        Calendar calendar = new GregorianCalendar(year, monthOfYear, dayOfMonth);
        birthTextView.setText(year+"년 "+(monthOfYear+1)+"월 "+dayOfMonth+"일");
        Log.e("생년월일",String.valueOf(simpleDateFormat.format(calendar.getTime())));
    }
}
/*
    회원가입 서버로 보내기
    //POST
        SignUp signUp = new SignUp();

//signUp POST
        signUp.setEmail(checkEmail);
        //signUp.setUsername("안드로이드Test");
        signUp.setPassword(checkPw);

        Call<SignUp> postCall = apiService.postSignUp(signUp);
        postCall.enqueue(new Callback<SignUp>() {
@Override
public void onResponse(Call<SignUp> call, Response<SignUp> response) {
        if( response.isSuccessful()) {
        } else {
        int StatusCode = response.code();
        Log.i(ApplicationController.TAG, "상태 Code : " + StatusCode);
        }
        }

@Override
public void onFailure(Call<SignUp> call, Throwable t) {
        Log.i(ApplicationController.TAG, "실패 Message : " + t.getMessage());
        }
        });
*/
