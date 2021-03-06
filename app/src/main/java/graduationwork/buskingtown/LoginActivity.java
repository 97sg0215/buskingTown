package graduationwork.buskingtown;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
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

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import graduationwork.buskingtown.api.RestApiService;
import graduationwork.buskingtown.model.Login;
import graduationwork.buskingtown.model.User;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

public class LoginActivity extends AppCompatActivity {

    private RestApiService apiService;

    String inputValue = null, user_token, user_name, user_image;
    int user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        restApiBuilder();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //runtime permission
        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                Toast.makeText(LoginActivity.this, "위치 권한허가", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                Toast.makeText(LoginActivity.this, "위치 권한거부\n위치 접근이 거부될 경우 앱 사용에 제한이 있을 수 있어요." + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();

            }

        };
        TedPermission.with(LoginActivity.this)
                .setPermissionListener(permissionListener)
                .setRationaleMessage("위치에 접근하기위해서는 위치 접근 권한이 필요해요\n위치 접근이 거부될 경우 앱 사용에 제한이 있을 수 있어요.")
                .setDeniedMessage("접근을 거부 하셨군요 \n [설정]->[권한]에서 권한을 허용할 수 있어요.")
                .setPermissions(Manifest.permission.ACCESS_COARSE_LOCATION)
                .setPermissions(Manifest.permission.ACCESS_FINE_LOCATION)
                .check();

        try {
            PackageInfo info = getPackageManager().getPackageInfo("graduationwork.buskingtown", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }


        //아이디 에디터 텍스트 입력 변수
        final EditText idEdit = (EditText) findViewById(R.id.idEdit);

        //비밀번호 에디터 텍스트 입력 변수
        final EditText passWdEdit = (EditText) findViewById(R.id.passWdEdit);

        //아이디, 비밀번호 유효성 체크값 담음
        final boolean[] idOk = new boolean[1];
        final boolean[] pwOk = new boolean[1];

        getLocalData();

        //로그인 유지, 어플리케이션에 회원정보가 저장되어 있으면 로그인 액티비티 종료하고 바로 메인 화면으로 넘어감
        if (user_token != null) {
            Intent i = new Intent(getApplication(), TabBar.class);
            startActivity(i);
            saveUserInfo(user_token, user_id, user_name, user_image);
            finish();
        }


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
                    loginBtn.setOnClickListener(new OnSingleClickListener() {
                        @Override
                        public void onSingleClick(View v) {
                            String userID = idEdit.getText().toString();
                            String userPW = passWdEdit.getText().toString();
                            login(userID, userPW);
                        }
                    });
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
                    loginBtn.setOnClickListener(new OnSingleClickListener() {
                        @Override
                        public void onSingleClick(View v) {
                            String userID = idEdit.getText().toString();
                            String userPW = passWdEdit.getText().toString();
                            login(userID, userPW);
                        }
                    });
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

    //로그인 메소드
    public void login(String username, String password) {
        //로그인 객체 생성
        Login login = new Login(username, password);
        //로그인 모델 call
        Call<User> userCall = apiService.login(login);
        userCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                User user = response.body();
                if (response.isSuccessful()) {
                    Log.e("로그인:", "성공");
                    //성공시 토큰 생성
                    String base = username + ":" + password;
                    String auth_header = "Basic " + android.util.Base64.encodeToString(base.getBytes(), android.util.Base64.NO_WRAP);

                    //토큰에 접근하여 권한 생성
                    accessToken(auth_header);
                    Log.e("토큰", user.getToken());

                    //유저 정보 보내기
                    int id = user.getId();
                    getUserDetail(auth_header, id);
                } else {
                    Toast.makeText(getApplicationContext(), "아이디 비밀번호를 확인해주세요", Toast.LENGTH_SHORT).show();
                    //에러 상태 보려고 해둔 코드
                    int StatusCode = response.code();
                    Log.i(ApplicationController.TAG, "상태 Code : " + StatusCode);
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "아이디 비밀번호를 확인해주세요", Toast.LENGTH_SHORT).show();
                Log.i(ApplicationController.TAG, "실패 Message : " + t.getMessage());
            }
        });
    }

    //user 정보를 얻어와 저장 하기 위함
    public void getUserDetail(String token, int id) {
        final User[] userDetail = {new User()};
        Call<User> userDetailCall = apiService.getUserDetail(token, id);
        userDetailCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                userDetail[0] = response.body();
                String username = userDetail[0].getUsername();
                String user_image = userDetail[0].getProfile().getUser_image();

                if (response.isSuccessful()) {

                    //유저 정보 저장 메소드
                    saveUserInfo(token, id, username, user_image);

                    //메인 홈 진행
                    mainEnter();

                } else {
                    //에러 상태 보려고 해둔 코드
                    int StatusCode = response.code();
                    Log.i(ApplicationController.TAG, "상태 Code : " + StatusCode);
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.i(ApplicationController.TAG, "유저 정보 서버 연결 실패 Message : " + t.getMessage());
            }
        });
    }

    //토큰에 접근하여 권한 부여
    public void accessToken(String token) {
        Call<ResponseBody> call = apiService.getSecret(token);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                } else {
                    //에러 상태 보려고 해둔 코드
                    int StatusCode = response.code();
                    Log.i(ApplicationController.TAG, "상태 Code : " + StatusCode);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }
        });
    }

    //유저 정보를 저장하여 다른 액티비티에서 불러오기 위함
    public void saveUserInfo(String token, int user, String username, String user_image) {
        SharedPreferences pref = getSharedPreferences("User", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("auth_token", token);
        editor.putInt("user_id", user);
        editor.putString("username", username);
        editor.putString("user_image", user_image);
        editor.commit();
    }

    //네트워크 연결 메소드
    public void restApiBuilder() {
        ApplicationController application = ApplicationController.getInstance();
        application.buildNetworkService();
        apiService = ApplicationController.getInstance().getRestApiService();
    }

    //메인액티비티로 넘어가기
    public void mainEnter() {
        Intent tabActivity = new Intent(getApplication(), TabBar.class);
        startActivity(tabActivity);
        finish();
    }

    // 저장 되어 있는 user 불러오기
    public void getLocalData() {
        SharedPreferences pref = getSharedPreferences("User", Activity.MODE_PRIVATE);
        user_token = pref.getString("auth_token", null);
        user_name = pref.getString("username", null);
        user_id = pref.getInt("user_id", 0);
        user_image = pref.getString("user_image", null);
    }


    //백버튼 2번이면 종료
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}