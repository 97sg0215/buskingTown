package graduationwork.buskingtown;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.telecom.Call;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import graduationwork.buskingtown.api.RestApiService;
import graduationwork.buskingtown.model.Busker;
import okhttp3.ResponseBody;
import retrofit2.Callback;
import retrofit2.Response;

public class Home extends Fragment {

    RestApiService apiService;

    String user_token, user_name;
    int user_id;

    public Home(){
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        restApiBuilder();

        getLocalData();

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.activity_home, container, false);



        return v;
    }

    //user데이터 얻어오기
    public void getLocalData(){
        SharedPreferences pref = getActivity().getSharedPreferences("User", Activity.MODE_PRIVATE);
        user_token = pref.getString("auth_token",null);
        user_name = pref.getString("username",null);
        user_id = pref.getInt("user_id",0);
    }

    //api연결
    public void restApiBuilder() {
        ApplicationController application = ApplicationController.getInstance();
        application.buildNetworkService();
        apiService = ApplicationController.getInstance().getRestApiService();
    }
}
