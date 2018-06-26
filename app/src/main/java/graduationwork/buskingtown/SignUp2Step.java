package graduationwork.buskingtown;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.tsongkha.spinnerdatepicker.DatePicker;
import com.tsongkha.spinnerdatepicker.DatePickerDialog;
import com.tsongkha.spinnerdatepicker.SpinnerDatePickerDialogBuilder;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import graduationwork.buskingtown.api.RestApiService;
import graduationwork.buskingtown.model.Profile;
import graduationwork.buskingtown.model.SignUp;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUp2Step extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private RestApiService apiService;

    //이전 액티비티에서 받아온 회원가입 포스트할 변수
    String userEmail, userPassword;
    // String userBirth, userId,  userPhone;
    int birthYear, birthMonth, birthDay;
    String userBirth;
    SimpleDateFormat simpleDateFormat;

    TextView birthTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        restApiBuilder();


        //이전 액티비티에서 이메일, 비밀번호 받아와서 이어서 작성
        userEmail = getIntent().getStringExtra("email");
        Log.e("이메일", String.valueOf(userEmail));
        userPassword = getIntent().getStringExtra("password");
        Log.e("비밀번호", String.valueOf(userPassword));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up2_step);

        ImageButton backBtn = (ImageButton) findViewById(R.id.backBtn);


        //아이디 에디터 텍스트 입력 변수
        final EditText inputIDEdit = (EditText) findViewById(R.id.inputID);

        //휴대폰 에디터 텍스트 입력 변수
        final EditText phoneNumEdit = (EditText) findViewById(R.id.phoneNum);
        phoneNumEdit.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

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
                final String checkUserId = inputIDEdit.getText().toString();
                //이메일 체크 값 담음
                idOk[0] = checkId(checkUserId);

                //확인 버튼 변수
                final Button confirmBtn = (Button) findViewById(R.id.confirmBtn);

                //아이디, 휴대폰 형식 모두 맞으면 버튼 활성화
                if (idOk[0] && phoneOk[0]) {
                    //색지정 할때 getApplicationContext().getResources().getColor(컬러이름)으로 해주세요.
                    confirmBtn.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.mainPurple));
                    //다음 로그인 버튼
                    confirmBtn.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            String userID = inputIDEdit.getText().toString();
                            String userPhone = phoneNumEdit.getText().toString();

                            signUpNow(userID,userEmail,userPassword,userBirth,userPhone);
                        }
                    });
                } else {
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
                final String checkUserPhone = phoneNumEdit.getText().toString();
                //비밀번호 체크 값 담음
                phoneOk[0] = checkPhone(checkUserPhone);


                //확인 버튼 변수
                final Button confirmBtn = (Button) findViewById(R.id.confirmBtn);

                //아이디, 휴대폰 형식 모두 맞으면 버튼 활성화
                if (idOk[0] && phoneOk[0]) {
                    //색지정 할때 getApplicationContext().getResources().getColor(컬러이름)으로 해주세요.
                    confirmBtn.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.mainPurple));
                    //다음 로그인 버튼
                    confirmBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String userID = inputIDEdit.getText().toString();
                            String userPhone = phoneNumEdit.getText().toString();

                            signUpNow(userID,userEmail,userPassword,userBirth,userPhone);

                            Log.e("아이디",String.valueOf(userID));
                            Log.e("이메일",String.valueOf(userEmail));
                            Log.e("비번",String.valueOf(userPassword));
                            Log.e("생일", String.valueOf(userBirth));
                            Log.e("폰",String.valueOf(userPhone));
                        }
                    });
                } else {
                    confirmBtn.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.subGray));
                    confirmBtn.setOnClickListener(null);
                }

            }
        });

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
                userBirth = String.valueOf(simpleDateFormat.format(calendar.getTime()));
            }
        });
    }

    //아이디 형식이 제대로 되어있나 체크 메소드
    public static boolean checkId(String inputID) {
        String regex = "^[a-z0-9_]{4,15}$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(inputID);
        boolean isNormal = m.matches();
        return isNormal;
    }

    //휴대폰번호 형식이 제대로 되어있나 체크 메소드
    public static boolean checkPhone(String phone) {
        String regex = "^01(?:0|1|[6-9])-(?:\\d{3}|\\d{4})-\\d{4}$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(phone);
        boolean isNormal = m.matches();
        return isNormal;
    }

    public void signUpNow(String username, String email, String password, String birth, String phone) {
       SignUp signUp = new SignUp();
       Profile profile = new Profile();

       profile.setUser_birth(birth);
       profile.setUser_phone(phone);

       signUp.setProfile(profile);
       signUp.setPassword(password);
       signUp.setEmail(email);
       signUp.setUsername(username);

       Call<SignUp> postCall = apiService.postSignUp(signUp);
       postCall.enqueue(new Callback<SignUp>() {
           @Override
           public void onResponse(Call<SignUp> call, Response<SignUp> response) {
               if (response.isSuccessful()) {
                   Log.e("회원가입:", "성공");
                   startLogin();
               } else {
                   Toast.makeText(getApplicationContext(),"회원가입에 실패했습니다.\n다시 시도해주세요",Toast.LENGTH_SHORT).show();
                   int StatusCode = response.code();
                   String s = response.message();
                   ResponseBody d = response.errorBody();
                   SignUp a = response.body();
                   Log.i(ApplicationController.TAG, "상태 Code : " + StatusCode);
                   Log.e("메세지", s);
                   Log.e("리스폰스에러바디", String.valueOf(d));
                   Log.e("리스폰스바디", String.valueOf(a));
               }
           }
           @Override
           public void onFailure(Call<SignUp> call, Throwable t) {
               Toast.makeText(getApplicationContext(),"회원가입에 실패했습니다.\n다시 시도해주세요",Toast.LENGTH_SHORT).show();
               Log.i(ApplicationController.TAG, "실패 Message : " + t.getMessage());
           }
       });
    }


    public void restApiBuilder() {
        ApplicationController application = ApplicationController.getInstance();
        application.buildNetworkService();
        apiService = ApplicationController.getInstance().getRestApiService();
    }

    //마이페이지 액티비티로 넘어가기 (나중에 변경할 것)
    public void startLogin() {
        Intent loginActivity = new Intent(getApplication(), LoginActivity.class);
        startActivity(loginActivity);
    }

    //백버튼 메소드
    public void previousActivity(View v) {
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
        birthTextView.setText(year + "년 " + (monthOfYear + 1) + "월 " + dayOfMonth + "일");
    }
}
