package graduationwork.buskingtown;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import graduationwork.buskingtown.api.RestApiService;
import graduationwork.buskingtown.model.Busker;
import graduationwork.buskingtown.model.Login;
import graduationwork.buskingtown.model.UserDetail;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Mypage extends Fragment {

    private RestApiService apiService;

    RelativeLayout go_Busker;

    public Mypage(){
        // Required empty public constructor
    }

    //유저 정보 변수들
    String user_token,user_name;
    int user_id;
    Busker busker;
    boolean certification;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.activity_my_page, container, false);

        restApiBuilder();

        //프로필 이미지 변수
        ImageView profile = (ImageView) v.findViewById(R.id.profileImg);
        //임시 이미지
        int imageresource = getResources().getIdentifier("@drawable/test_img", "drawable", getActivity().getPackageName());
        //이미지 원형 처리
        Picasso.with(getActivity()).load(imageresource).transform(new CircleTransForm()).into(profile);

        getLocalData();

        TextView userID = (TextView) v.findViewById(R.id.userId);
        userID.setText(user_name);

        //여기다 링크 바로가기 선언하세요
        go_Busker = (RelativeLayout) v.findViewById(R.id.goBusker);

        getBuskerDetail(user_token,user_id);


        return v;
    }

    public void getLocalData(){
        SharedPreferences pref = getActivity().getSharedPreferences("User", Activity.MODE_PRIVATE);
        user_token = pref.getString("auth_token",null);
        user_name = pref.getString("username",null);
        user_id = pref.getInt("user_id",0);
    }

    public void buskerCheck(boolean certification,Busker busker){
        Log.e("인증상태-",String.valueOf(certification));
        Log.e("버스커-",String.valueOf(busker));
        if(busker == null){
            //버스커되기 채널 가기
            go_Busker.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getActivity(),BuskerCertification.class);
                    startActivity(i);
                }
            });
        } else {
            //내 채널 되기 가기
            TextView go_Busker_text = (TextView) getActivity().findViewById(R.id.goBuskerText);
            if(certification == true){
                go_Busker_text.setText("내 채널 가기");
                go_Busker.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(getActivity(),ChannelBusker.class);
                        startActivity(i);
                    }
                });
            }else if(certification == false){
                go_Busker.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(getActivity(),WaitPass.class);
                        startActivity(i);
                    }
                });
            }
        }
    }

    public void getBuskerDetail(String token,int id){
        final UserDetail[] userDetail = {new UserDetail()};
        Call<UserDetail> userDetailCall = apiService.getUserDetail(token,id);
        userDetailCall.enqueue(new Callback<UserDetail>() {
            @Override
            public void onResponse(Call<UserDetail> call, Response<UserDetail> response) {
                userDetail[0] = response.body();
                busker = userDetail[0].getBusker();
                certification = userDetail[0].getBusker().isCertification();
                if(response.isSuccessful()){
                    buskerCheck(certification,busker);
                    Log.e("유저 아이디",String.valueOf(id));
                    Log.e("인증상태",String.valueOf(certification));
                    Log.e("버스커",String.valueOf(busker));
                    Log.e("버스커유저정보가져오기:", "성공");
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

    public void restApiBuilder() {
        ApplicationController application = ApplicationController.getInstance();
        application.buildNetworkService();
        apiService = ApplicationController.getInstance().getRestApiService();
    }
}