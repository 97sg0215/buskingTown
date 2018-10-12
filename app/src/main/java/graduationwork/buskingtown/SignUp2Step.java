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

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import graduationwork.buskingtown.api.RestApiService;
import graduationwork.buskingtown.model.Profile;
import graduationwork.buskingtown.model.SignUp;
import graduationwork.buskingtown.model.User;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUp2Step extends AppCompatActivity {

    private RestApiService apiService;

    //이전 액티비티에서 받아온 회원가입 포스트할 변수
    String userEmail, userPassword, userId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        restApiBuilder();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up2_step);


        //이전 액티비티에서 이메일, 비밀번호 받아와서 이어서 작성
        userEmail = getIntent().getStringExtra("email");
        Log.e("이메일", String.valueOf(userEmail));
        userPassword = getIntent().getStringExtra("password");
        Log.e("비밀번호", String.valueOf("보안을 위해 띄우지 않습니다."));

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
                final String userId = inputIDEdit.getText().toString();
                //이메일 체크 값 담음
                idOk[0] = checkId(userId);

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

                            signUp(userID,userEmail,userPassword,userPhone);
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
                final String userPhone = phoneNumEdit.getText().toString();
                //비밀번호 체크 값 담음
                phoneOk[0] = checkPhone(userPhone);


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

                            signUp(userID,userEmail,userPassword,userPhone);

                            Log.e("아이디",String.valueOf(userID));
                            Log.e("이메일",String.valueOf(userEmail));
                            Log.e("폰",String.valueOf(userPhone));
                        }
                    });
                } else {
                    confirmBtn.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.subGray));
                    confirmBtn.setOnClickListener(null);
                }

            }
        });

    }

    //아이디 형식이 제대로 되어있나 체크 메소드
    public static boolean checkId(String inputID) {
        String regex = "^[A-Za-z0-9_]{4,15}$";
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

    public void signUp(String id, String email, String password, String phone) {
        //회원가입 서버로 보내기
        //POST
        SignUp signUp = new SignUp();

        Profile profile = new Profile();
        profile.setUser_phone(phone);
        profile.setUser_image(null);

        //signUp POST
        signUp.setEmail(email);
        signUp.setUsername(id);
        signUp.setPassword(password);
        signUp.setProfile(profile);
        Log.e("프로필", String.valueOf(profile));

        Call<SignUp> postCall = apiService.postSignUp(signUp);
        postCall.enqueue(new Callback<SignUp>() {
            @Override
            public void onResponse(Call<SignUp> call, Response<SignUp> response) {
                if (response.isSuccessful()) {
                    Log.e("회원가입:", "성공");
                    Toast.makeText(getApplicationContext(), "버스킹타운 회원이 되신걸 축하드립니다!!", Toast.LENGTH_SHORT).show();
                    startLogin();
                } else {
                    int StatusCode = response.code();
                    Log.i(ApplicationController.TAG, "상태 Code : " + StatusCode);
                    if(StatusCode == 400){
                        Toast.makeText(getApplicationContext(), "중복된 아이디가 존재 합니다.\n다른 아이디로 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(getApplicationContext(), "회원가입에 실패했습니다.\n다시 시도해주세요", Toast.LENGTH_SHORT).show();
                    }
                    Log.e("메세지", String.valueOf(response.message()));
                    Log.e("리스폰스에러바디", String.valueOf(response.errorBody()));
                    Log.e("리스폰스바디", String.valueOf(response.raw().toString()));
                }
            }

            @Override
            public void onFailure(Call<SignUp> call, Throwable t) {
                Log.i(ApplicationController.TAG, "실패 Message : " + t.getMessage());
                Toast.makeText(getApplicationContext(), "회원가입에 실패했습니다.\n다시 시도해주세요", Toast.LENGTH_SHORT).show();
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
        this.finish();
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
}


