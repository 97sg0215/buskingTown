package graduationwork.buskingtown;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.tsongkha.spinnerdatepicker.DatePicker;
import com.tsongkha.spinnerdatepicker.DatePickerDialog;
import com.tsongkha.spinnerdatepicker.SpinnerDatePickerDialogBuilder;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


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

        ImageButton backBtn = (ImageButton) findViewById(R.id.backBtn);


        //아이디 에디터 텍스트 입력 변수
        final EditText inputIDEdit = (EditText) findViewById(R.id.inputID);

        //휴대폰 에디터 텍스트 입력 변수
        final EditText phoneNumEdit = (EditText) findViewById(R.id.phoneNum);

        //아이디, 휴대폰 체크 유효성 체크값 담음
        final boolean[] idOk = new boolean[1];
        final boolean[] phoneOk = new boolean[1];

        inputIDEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {
                //아이디 입력 값 문자열화
                final String userId = inputIDEdit.getText().toString();
                //이메일 체크 값 담음
                idOk[0] = checkId(userId);

                //확인 버튼 변수
                final Button confirmBtn = (Button) findViewById(R.id.confirmBtn);

                //아이디, 휴대폰 형식 모두 맞으면 버튼 활성화
                if(idOk[0]&&phoneOk[0]){
                    //색지정 할때 getApplicationContext().getResources().getColor(컬러이름)으로 해주세요.
                    confirmBtn.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.mainPurple));
                    //다음 로그인 버튼
                    confirmBtn.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            startTabBar();
                        }
                    });
                }else {
                    confirmBtn.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.subGray));
                    confirmBtn.setOnClickListener(null);
                }

            }
        });


        //휴대폰번호 에디터 텍스트 메소드
        phoneNumEdit.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                //휴대폰번호 입력 값 문자열화
                final String userPhone = phoneNumEdit.getText().toString();
                //비밀번호 체크 값 담음
                phoneOk[0] = checkPhone(userPhone);


                //확인 버튼 변수
                final Button confirmBtn = (Button) findViewById(R.id.confirmBtn);

                //아이디, 휴대폰 형식 모두 맞으면 버튼 활성화
                if(idOk[0]&&phoneOk[0]){

                    //색지정 할때 getApplicationContext().getResources().getColor(컬러이름)으로 해주세요.
                    confirmBtn.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.mainPurple));
                    //다음 로그인 버튼
                    confirmBtn.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            startTabBar();
                        }
                    });
                }else {
                    confirmBtn.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.subGray));
                    confirmBtn.setOnClickListener(null);
                }

            }
        });

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

    //아이디 형식이 제대로 되어있나 체크 메소드
    public static boolean checkId(String inputID){
        String regex =  "^[a-z0-9_]{4,15}$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(inputID);
        boolean isNormal = m.matches();
        return isNormal;
    }

    //휴대폰번호 형식이 제대로 되어있나 체크 메소드
    public static boolean checkPhone(String phone){
        String regex =  "^(?=.*[0-9]+).{3,16}$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(phone);
        boolean isNormal = m.matches();
        return isNormal;
    }

    //마이페이지 액티비티로 넘어가기 (나중에 변경할 것)
    public void startTabBar() {
        Intent tabBarActivity = new Intent(getApplication(),TabBar.class);

        final EditText userIdEdit = (EditText) findViewById(R.id.inputID);
        final String userId = userIdEdit.getText().toString();

        final EditText userPhoneEdit = (EditText) findViewById(R.id.phoneNum);
        final String userPhone = userPhoneEdit.getText().toString();

        //아이디, 휴대폰번호 로그 띄우기
        Log.e("아이디",String.valueOf(userId));
        Log.e("휴대폰번호",String.valueOf(userPhone));

        startActivity(tabBarActivity);


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

