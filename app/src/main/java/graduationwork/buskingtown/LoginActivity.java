package graduationwork.buskingtown;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import graduationwork.buskingtown.api.RestApiService;
import graduationwork.buskingtown.model.Login;
import graduationwork.buskingtown.model.User;
import graduationwork.buskingtown.model.UserDetail;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private RestApiService apiService;

    String inputValue = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        restApiBuilder();

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
                if (inputValue.replace(" ", "").equals("") || s.length() < 3) {
                    idOk[0] = false;
                } else {
                    idOk[0] = true;
                }
            }

            // 입력이 끝났을 때
            @Override
            public void afterTextChanged(Editable editable) {

                //로그인 버튼 변수
                final Button loginBtn = (Button) findViewById(R.id.loginBtn);

                if (idOk[0] && pwOk[0]) { //이메일과 패스워드 모두 형식에 맞으면 로그인
                    loginBtn.setBackground(getDrawable(R.drawable.able_btn));
                    //로그인 버튼 클릭시 메소드
                    loginBtn.setOnClickListener(new Button.OnClickListener() {
                                                    public void onClick(View v) {
                                                        String userID = idEdit.getText().toString();
                                                        String userPW = passWdEdit.getText().toString();
                                                        login(userID, userPW);
                                                    }
                                                }
                    );
                } else { //아니면 로그인 비활성화
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


                if (idOk[0] && pwOk[0]) { //이메일과 패스워드 모두 형식에 맞으면 로그인
                    loginBtn.setBackground(getDrawable(R.drawable.able_btn));
                    //로그인 버튼 클릭시 메소드
                    loginBtn.setOnClickListener(new Button.OnClickListener() {
                                                    public void onClick(View v) {
                                                        String userID = idEdit.getText().toString();
                                                        String userPW = passWdEdit.getText().toString();
                                                        login(userID, userPW);
                                                    }
                                                }
                    );
                } else { //아니면 로그인 비활성화
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
                Intent signUp = new Intent(getApplicationContext(), SignUp1Step.class);
                startActivity(signUp);
            }
        });

    }

    //비밀번호 형식이 제대로 되어있나 체크 메소드
    public static boolean checkPW(String pw) {
        String regex = "^(?=.*[a-zA-Z]+)(?=.*[!@#$%^*+=-]|.*[0-9]+).{8,16}$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(pw);
        boolean isNormal = m.matches();
        return isNormal;
    }


    public void login(String username, String password) {
        Login login = new Login(username, password);
        Call<User> userCall = apiService.login(login);
        userCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                User user = response.body();
                if (response.isSuccessful()) {
                    Log.e("로그인:", "성공");
                    //토큰 생성
                    String base = username + ":" + password;
                    String auth_header = "Basic " + android.util.Base64.encodeToString(base.getBytes(), android.util.Base64.NO_WRAP);

                    accessToken(auth_header);
                    Log.e("토큰", user.getToken());

                    //유저 정보 보내기
                    int id = user.getId();
                    getUserDetail(auth_header,id);

                    mainEnter();
                } else {
                    Toast.makeText(getApplicationContext(),"아이디 비밀번호를 확인해주세요",Toast.LENGTH_SHORT).show();
                    //에러 상태 보려고 해둔 코드
                    int StatusCode = response.code();
                    String s = response.message();
                    ResponseBody d = response.errorBody();
                    Log.i(ApplicationController.TAG, "상태 Code : " + StatusCode);
                    Log.e("메세지", s);
                    Log.e("리스폰스에러바디", String.valueOf(d));
                    Log.e("리스폰스바디", String.valueOf(user));
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"아이디 비밀번호를 확인해주세요",Toast.LENGTH_SHORT).show();
                Log.i(ApplicationController.TAG, "실패 Message : " + t.getMessage());
            }
        });
    }

    //유저 정보를 저장하여 다른 액티비티에서 불러오기 위함
    public void saveUserInfo(String token,int user,String username,String user_phone){
        SharedPreferences pref = getSharedPreferences("User", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("auth_token",token);
        editor.putInt("user_id",user);
        editor.putString("username",username);
        editor.putString("user_phone",user_phone);
        editor.commit();
    }

    public void getUserDetail(String token,int id){
        final UserDetail[] userDetail = {new UserDetail()};
        Call<UserDetail> userDetailCall = apiService.getUserDetail(token,id);
        userDetailCall.enqueue(new Callback<UserDetail>() {
            @Override
            public void onResponse(Call<UserDetail> call, Response<UserDetail> response) {
                userDetail[0] = response.body();
                String username = userDetail[0].getUsername();
                String userEmail = userDetail[0].getEmail();
                String user_birth = userDetail[0].getProfile().getUser_birth();
                String user_phone = userDetail[0].getProfile().getUser_phone();

                if(response.isSuccessful()){
                    Log.e("유저 아이디",String.valueOf(id));
                    Log.e("유저정보가져오기:", "성공");
                    saveUserInfo(token,id,username,user_phone);
                } else{
                    //에러 상태 보려고 해둔 코드
                    int StatusCode = response.code();
                    String s = response.message();
                    ResponseBody d = response.errorBody();
                    Log.i(ApplicationController.TAG, "상태 Code : " + StatusCode);
                    Log.e("메세지", s);
                    Log.e("리스폰스에러바디", String.valueOf(d));
                    Log.e("리스폰스바디", String.valueOf(response.body()));
                }
            }

            @Override
            public void onFailure(Call<UserDetail> call, Throwable t) {
                Log.i(ApplicationController.TAG, "유저 정보 서버 연결 실패 Message : " + t.getMessage());
            }
        });
    }

    public void accessToken(String token) {
        Call<ResponseBody> call = apiService.getSecret(token);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Log.e("토큰 불러오기:", "성공");
                    Log.e("토큰 상태 리스폰스바디 : ", String.valueOf(response.body()));
                } else {
                    //에러 상태 보려고 해둔 코드
                    int StatusCode = response.code();
                    String s = response.message();
                    ResponseBody d = response.errorBody();
                    Log.i(ApplicationController.TAG, "상태 Code : " + StatusCode);
                    Log.e("메세지", s);
                    Log.e("리스폰스에러바디", String.valueOf(d));
                    Log.e("리스폰스바디 : ", String.valueOf(response.body()));
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }
        });
    }

    public void restApiBuilder() {
        ApplicationController application = ApplicationController.getInstance();
        application.buildNetworkService();
        apiService = ApplicationController.getInstance().getRestApiService();
    }

    //메인액티비티로 넘어가기
    public void mainEnter() {
        Intent tabActivity = new Intent(getApplication(), TabBar.class);
        startActivity(tabActivity);
    }

    //테스트 액티비티 이건 지울거임
    public void tabActivity(View view) {
        Intent testActivity = new Intent(getApplication(), TabBar.class);
        startActivity(testActivity);
    }

    //테스트 액티비티 이건 지울거임
    public void certification(View view) {
        Intent testActivity2 = new Intent(getApplication(), BuskerCertification.class);
        startActivity(testActivity2);
    }

    //테스트 액티비티 이건 지울거임
    public void channel(View view) {
        Intent testActivity2 = new Intent(getApplication(), ChannelUser.class);
        startActivity(testActivity2);
    }

    //테스트 액티비티 이건 지울거임
    public void coin_send_before(View view) {
        Intent testActivity2 = new Intent(getApplication(), CoinSendBefore_pop.class);
        startActivity(testActivity2);
    }

    //테스트 액티비티 이건 지울거임
    public void failPass(View view) {
        Intent testActivity2 = new Intent(getApplication(), FailPass.class);
        startActivity(testActivity2);
    }

    //테스트 액티비티 이건 지울거임
    public void waitPass(View view) {
        Intent testActivity2 = new Intent(getApplication(), WaitPass.class);
        startActivity(testActivity2);
    }

    //테스트 액티비티 이건 지울거임
    public void coinSendSuccess(View view) {
        Intent testActivity2 = new Intent(getApplication(), CoinSendSuccess_pop.class);
        startActivity(testActivity2);
    }

    //테스트 액티비티 이건 지울거임
    public void buskerMainChannel(View view) {
        Intent testActivity2 = new Intent(getApplication(), ChannelBusker.class);
        startActivity(testActivity2);
    }

    //테스트 액티비티 이건 지울거임
    public void setting(View view) {
        Intent testActivity2 = new Intent(getApplication(), Setting.class);
        startActivity(testActivity2);
    }

    //백버튼 2번이면 종료
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}