
package graduationwork.buskingtown;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import graduationwork.buskingtown.api.RestApiService;
import graduationwork.buskingtown.model.Login;
import graduationwork.buskingtown.model.User;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WithdrawalPop extends Activity implements View.OnClickListener{

    RestApiService apiService;
    String user_token;
    int user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_withdrawal_pop);

        restApiBuilder();

        SharedPreferences pref = getSharedPreferences("User", Activity.MODE_PRIVATE);
        user_token = pref.getString("auth_token",null);
        user_id = pref.getInt("user_id",0);
        Log.e("아이디", String.valueOf(user_id));

        findViewById(R.id.delete).setOnClickListener(this);
        findViewById(R.id.confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Call<User> memberwithdrawoal = apiService.withdrawal(user_token,user_id);
                memberwithdrawoal.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        if(response.isSuccessful()){
                            SharedPreferences.Editor editor = pref.edit();
                            editor.clear(); //clear all stored data
                            editor.commit();

                            Log.e("메세지", "탈퇴성공");
                            Intent main = new Intent(getApplication(), LoginActivity.class);
                            main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(main);
                        }else {
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
                    public void onFailure(Call<User> call, Throwable t) {
                        Log.i(ApplicationController.TAG, "회원탈퇴 서버 연결 실패 Message : " + t.getMessage());
                    }
                });
            }
        });
    }
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.delete:
                this.finish();
                break;
        }
    }

    public void restApiBuilder() {
        ApplicationController application = ApplicationController.getInstance();
        application.buildNetworkService();
        apiService = ApplicationController.getInstance().getRestApiService();
    }
}
