package graduationwork.buskingtown;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {
    String inputValue = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //아이디 에디터 텍스트 입력 변수
        final EditText idEdit = (EditText) findViewById(R.id.idEdit);

        //비밀번호 에디터 텍스트 입력 변수
        final EditText passWdEdit = (EditText) findViewById(R.id.passWdEdit);

        //아이디, 비밀번호 유효성 체크값 담음
        final boolean[] idOk = new boolean[1];
        final boolean[] pwOk = new boolean[1];

        //이메일 에디터 텍스트 메소드
        idEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //아이디 공백 체크
                inputValue = s.toString();
                if (inputValue.replace(" ", "").equals("")||s.length()<3){
                    idOk[0]=false;
                } else {
                    idOk[0]=true;
                }
            }

            // 입력이 끝났을 때
            @Override
            public void afterTextChanged(Editable editable) {

                //로그인 버튼 변수
                final Button loginBtn = (Button) findViewById(R.id.loginBtn);

                if(idOk[0]&&pwOk[0]){ //이메일과 패스워드 모두 형식에 맞으면 로그인
                    loginBtn.setBackground(getDrawable(R.drawable.able_btn));
                    //로그인 버튼 클릭시 메소드
                    loginBtn.setOnClickListener(new Button.OnClickListener() {
                        public void onClick(View v) {
                            mainEnter();
                            }
                        }
                    );
                }else { //아니면 로그인 비활성화
                    loginBtn.setBackground(getDrawable(R.drawable.disable_btn));
                    loginBtn.setOnClickListener(null);
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
                final String checkPW = passWdEdit.getText().toString();
                //비밀번호 체크 값 담음
                pwOk[0] = checkPW(checkPW);

                //로그인 버튼 변수
                final Button loginBtn = (Button) findViewById(R.id.loginBtn);


                if(idOk[0]&&pwOk[0]){ //이메일과 패스워드 모두 형식에 맞으면 로그인
                    loginBtn.setBackground(getDrawable(R.drawable.able_btn));
                    //로그인 버튼 클릭시 메소드
                    loginBtn.setOnClickListener(new Button.OnClickListener() {
                                public void onClick(View v) {
                                    mainEnter();
                                }
                            }
                    );
                }else { //아니면 로그인 비활성화
                    loginBtn.setBackground(getDrawable(R.drawable.disable_btn));
                    loginBtn.setOnClickListener(null);
                }
            }

            // 입력하기 전에
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
        });

        //회원가입액티비티로 넘어가기
        TextView signUp = (TextView) findViewById(R.id.signUpBtn);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signUp = new Intent(getApplicationContext(),SignUp1Step.class);
                startActivity(signUp);
            }
        });

    }


    //비밀번호 형식이 제대로 되어있나 체크 메소드
    public static boolean checkPW(String pw){
        String regex =  "^(?=.*[a-zA-Z]+)(?=.*[!@#$%^*+=-]|.*[0-9]+).{8,16}$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(pw);
        boolean isNormal = m.matches();
        return isNormal;
    }

    //메인액티비티로 넘어가기
    public void mainEnter(){
        Intent mainActivity = new Intent(getApplication(),MainActivity.class);
        startActivity(mainActivity);
    }

    //테스트 액티비티 이건 지울거임
    public void tabActivity(View view) {
        Intent testActivity = new Intent(getApplication(),TabBar.class);
        startActivity(testActivity);
    }

    //테스트 액티비티 이건 지울거임
    public void certification(View view) {
        Intent testActivity2 = new Intent(getApplication(),BuskerCertification.class);
        startActivity(testActivity2);
    }

    //테스트 액티비티 이건 지울거임
    public void channel(View view) {
        Intent testActivity2 = new Intent(getApplication(),ChannelBusker.class);
        startActivity(testActivity2);
    }

    //테스트 액티비티 이건 지울거임
    public void coin_send_before(View view) {
        Intent testActivity2 = new Intent(getApplication(),CoinSendBefore_pop.class);
        startActivity(testActivity2);
    }

    //테스트 액티비티 이건 지울거임
    public void failPass(View view) {
        Intent testActivity2 = new Intent(getApplication(),FailPass.class);
        startActivity(testActivity2);
    }

    //테스트 액티비티 이건 지울거임
    public void waitPass(View view) {
        Intent testActivity2 = new Intent(getApplication(),WaitPass.class);
        startActivity(testActivity2);
    }

    //테스트 액티비티 이건 지울거임
    public void coinSendSuccess(View view) {
        Intent testActivity2 = new Intent(getApplication(),CoinSendSuccess_pop.class);
        startActivity(testActivity2);
    }

    //백버튼 2번이면 종료
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
