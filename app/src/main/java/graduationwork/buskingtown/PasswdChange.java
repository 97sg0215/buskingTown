package graduationwork.buskingtown;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import graduationwork.buskingtown.api.RestApiService;
import graduationwork.buskingtown.model.ChangePassword;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PasswdChange extends AppCompatActivity {

    private TextView passwdok;
    private EditText nowPasswd, newPasswd, newPasswdCheck;
    RestApiService apiService;
    String user_token, nowpw, newpw, checkpw;
    int user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passwd_change);

        SharedPreferences pref = getSharedPreferences("User", Activity.MODE_PRIVATE);

        restApiBuilder();

        getLocalData();

        ImageButton backBtn = (ImageButton) findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PasswdChange.super.onBackPressed();
            }
        });

        nowPasswd = (EditText) findViewById(R.id.nowPasswd);
        newPasswd = (EditText) findViewById(R.id.newPasswd);
        newPasswdCheck = (EditText) findViewById(R.id.newPasswdCheck);

        nowPasswd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                nowpw = nowPasswd.getText().toString();
            }
        });

        newPasswd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                newpw = newPasswd.getText().toString();
            }
        });

        newPasswdCheck.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                checkpw = newPasswdCheck.getText().toString();
            }
        });

        //완료
        passwdok = (TextView) findViewById(R.id.passwdOK);
        passwdok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("비번", checkpw);
                if (newpw.equals(checkpw)) {
                    ChangePassword changePassword = new ChangePassword(nowpw, checkpw);
                    Call<ResponseBody> changePassWord = apiService.changePassword(user_token, user_id, changePassword);
                    changePassWord.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(PasswdChange.this, "변경된 비밀번호로 다시 로그인해주세요!", Toast.LENGTH_SHORT).show();
                                SharedPreferences.Editor editor = pref.edit();
                                editor.clear(); //clear all stored data
                                editor.commit();

                                Intent login = new Intent(getApplication(), LoginActivity.class);
                                login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(login);
                                finish();
                            } else {
                                int StatusCode = response.code();
                                String s = response.message();
                                Log.i(ApplicationController.TAG, "상태 Code : " + StatusCode);
                                Toast.makeText(PasswdChange.this, s, Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Log.i(ApplicationController.TAG, "비밀번호 변경 서버 연결 실패 Message : " + t.getMessage());
                        }
                    });
                } else {
                    Toast.makeText(PasswdChange.this, "새로운 비밀번호와 비밀번호 확인이 달라요!", Toast.LENGTH_SHORT).show();
                }
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

    public void getLocalData() {
        SharedPreferences pref = getSharedPreferences("User", Activity.MODE_PRIVATE);
        user_token = pref.getString("auth_token", null);
        user_id = pref.getInt("user_id", 0);
    }

    //api연결
    public void restApiBuilder() {
        ApplicationController application = ApplicationController.getInstance();
        application.buildNetworkService();
        apiService = ApplicationController.getInstance().getRestApiService();
    }

    public void previousActivity(View v) {
        onBackPressed();
    }

}
