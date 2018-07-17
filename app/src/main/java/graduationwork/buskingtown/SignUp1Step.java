package graduationwork.buskingtown;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import graduationwork.buskingtown.api.RestApiService;
import graduationwork.buskingtown.model.SignUp;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUp1Step extends AppCompatActivity {
    private RestApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up1_step);

        ImageButton backBtn = (ImageButton) findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignUp1Step.super.onBackPressed();
            }
        });

        //이메일 에디터 텍스트 입력 변수
        final EditText emailEdit = (EditText) findViewById(R.id.email);

        //비밀번호 에디터 텍스트 입력 변수
        final EditText passWdEdit = (EditText) findViewById(R.id.passWd);
        final EditText passWdCheckEdit = (EditText) findViewById(R.id.passWdCheck);

        //이메일, 비밀번호, 비밀번호 체크 유효성 체크값 담음
        final boolean[] emailOk = new boolean[1];
        final boolean[] pwOk = new boolean[1];
        final boolean[] pwCheckOk = new boolean[1];

        //이메일 에디터 텍스트 메소드
        emailEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            // 입력이 끝났을 때
            @Override
            public void afterTextChanged(Editable editable) {
                //이메일 입력 값 문자열화
                final String checkEmail = emailEdit.getText().toString();
                //이메일 체크 값 담음
                emailOk[0] = checkEmail(checkEmail);

                //다음 버튼 변수
                final Button nextBtn = (Button) findViewById(R.id.nextBtn);

                //이메일 형식, 비밀번호 형식, 비밀번호 확인 형식 모두 맞으면 버튼 활성화
                if(emailOk[0]&&pwOk[0]&&pwCheckOk[0]){
                    //색지정 할때 getApplicationContext().getResources().getColor(컬러이름)으로 해주세요.
                    nextBtn.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.mainPurple));
                    //다음 로그인 버튼
                    nextBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            nextSignUp();
                        }
                    });
                }else {
                    nextBtn.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.subGray));
                    nextBtn.setOnClickListener(null);
                }
            }

            // 입력하기 전에
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
        });

        //패스워드 에디터 텍스트 메소드
        passWdEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            // 입력이 끝났을 때
            @Override
            public void afterTextChanged(Editable editable) {
                //비밀번호 입력 값 문자열화
                final String checkPw = passWdEdit.getText().toString();
                //비밀번호 체크 값 담음
                pwOk[0] = checkPW(checkPw);

                //다음 버튼 변수
                final Button nextBtn = (Button) findViewById(R.id.nextBtn);

                if(emailOk[0]&&pwOk[0]&&pwCheckOk[0]){//이메일 형식, 비밀번호 형식, 비밀번호 확인 형식 모두 맞으면 버튼 활성화
                    nextBtn.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.mainPurple));
                    nextBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            nextSignUp();
                        }
                    });
                }else {
                    nextBtn.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.subGray));
                    nextBtn.setOnClickListener(null);
                }
            }

            // 입력하기 전에
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
        });

        //동일한 패스워드 확인 에디터 텍스트 메소드
        passWdCheckEdit.addTextChangedListener(new TextWatcher() {
            //입력 전
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            //입력 시
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            //컬러이름 불러오기
            @SuppressLint("ResourceAsColor")
            //입력이 끝난 후
            @Override
            public void afterTextChanged(Editable s) {
                //비밀번호 동일 입력 값 문자열화
                final String checkpw = passWdCheckEdit.getText().toString();
                //비빌번호 동일 값 담음
                pwCheckOk[0] = checkpw.equals(passWdEdit.getText().toString());

                //다음 버튼 변수
                final Button nextBtn = (Button) findViewById(R.id.nextBtn);

                if(emailOk[0]&&pwOk[0]&&pwCheckOk[0]){//이메일 형식, 비밀번호 형식, 비밀번호 확인 형식 모두 맞으면 버튼 활성화
                    nextBtn.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.mainPurple));
                    nextBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            nextSignUp();
                        }
                    });
                }else {
                    nextBtn.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.subGray));
                    nextBtn.setOnClickListener(null);
                }
            }
        });
    }

    //이메일 형식이 제대로 되어있나 체크 메소드
    public static boolean checkEmail(String email){
        String regex = "^[_a-zA-Z0-9-\\.]+@[\\.a-zA-Z0-9-]+\\.[a-zA-Z]+$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(email);
        boolean isNormal = m.matches();
        return isNormal;
    }

    //비밀번호 형식이 제대로 되어있나 체크 메소드
    public static boolean checkPW(String pw){
        String regex =  "^(?=.*[a-zA-Z]+)(?=.*[!@#$%^*+=-]|.*[0-9]+).{8,16}$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(pw);
        boolean isNormal = m.matches();
        return isNormal;
    }

    //회원가입2 액티비티로 넘어가기
    public void nextSignUp(){
        Intent nextSignUpActivity = new Intent(getApplication(),SignUp2Step.class);

        final EditText emailEdit = (EditText) findViewById(R.id.email);
        final String checkEmail = emailEdit.getText().toString();

        final EditText passWdEdit = (EditText) findViewById(R.id.passWd);
        final String checkPw = passWdEdit.getText().toString();

        //회원가입2단계로 데이터 넘김
        nextSignUpActivity.putExtra("email",checkEmail);
        nextSignUpActivity.putExtra("password",checkPw);

        startActivity(nextSignUpActivity);
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
}
