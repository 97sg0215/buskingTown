package graduationwork.buskingtown;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import graduationwork.buskingtown.api.RestApiService;
import graduationwork.buskingtown.model.Busker;
import graduationwork.buskingtown.model.Connections;
import graduationwork.buskingtown.model.Profile;
import graduationwork.buskingtown.model.User;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Mypage extends Fragment {

    SharedPreferences prefUser, prefBusker;

    private RestApiService apiService;

    private Uri mImageCaptureUri;

    final int REQ_CODE_SELECT_IMAGE=100;

    RelativeLayout go_Busker,coinLayout,coinhouseLayout,locationLendLayout,noticeLayout,clientcenterLayout,logout;

    TextView go_Busker_text, followingAmount, coinAmount;

    ImageView profile;

    ArrayList<Integer> all_user_id = new ArrayList<>();
    ArrayList<Integer> all_busker_id = new ArrayList<>();
    ArrayList<Integer> get_follower_id = new ArrayList<>();

    public Mypage(){
        // Required empty public constructor
    }

    //유저 정보 변수들
    String user_token,user_name,user_image, real_album_path,team_name, busker_image;
    int user_id,busker_id,busker_type;
    Boolean certification;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.activity_my_page, container, false);

        prefBusker = this.getActivity().getSharedPreferences("BuskerUser", Activity.MODE_PRIVATE);
        prefUser = this.getActivity().getSharedPreferences("User", Activity.MODE_PRIVATE);

        restApiBuilder();

        //설정버튼
        ImageButton settingBtn = (ImageButton) v.findViewById(R.id.settingBtn);
        settingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent setting = new Intent(getActivity(),Setting.class);
                startActivity(setting);
            }
        });

        getLocalData();

        getBusker(user_token,user_id);

        //프로필 이미지 변수
        profile  = (ImageView) v.findViewById(R.id.profileImg);

        if(user_image!=null){
            Picasso.with(getActivity()).load(user_image).transform(new CircleTransForm()).into(profile);
        }

        followingAmount = (TextView) v.findViewById(R.id.followingAmount);


        coinAmount = (TextView) v.findViewById(R.id.coinAmount);
        TextView userID = (TextView) v.findViewById(R.id.userId);
        userID.setText(user_name);

        //여기다 링크 바로가기 선언하세요
        go_Busker = (RelativeLayout) v.findViewById(R.id.goBusker);
        go_Busker_text = (TextView) v.findViewById(R.id.goBuskerText);
        coinLayout = (RelativeLayout) v.findViewById(R.id.coinLayout);
        coinhouseLayout = (RelativeLayout) v.findViewById(R.id.coinhouseLayout);
        locationLendLayout = (RelativeLayout) v.findViewById(R.id.locationLendLyout);
        noticeLayout = (RelativeLayout) v.findViewById(R.id.noticeLayout);
        clientcenterLayout = (RelativeLayout) v.findViewById(R.id.clientcenterLayout);
        logout = (RelativeLayout)v.findViewById(R.id.logOut);


        //코인충전소
        coinLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent coinCharge = new Intent(getActivity(),CoinCharge.class);
                startActivity(coinCharge);
            }
        });

        //보낸코인함
        coinhouseLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mycoinHouse = new Intent(getActivity(),MycoinHouse.class);
                startActivity(mycoinHouse);
            }
        });

        //장소빌려주기
        locationLendLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent locationLendStart = new Intent(getActivity(),LocationLendStart.class);
                startActivity(locationLendStart);
            }
        });

        //공지사항/이벤트
        noticeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent notice = new Intent(getActivity(),Notice.class);
                startActivity(notice);
            }
        });

        //고객센터
        clientcenterLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent clientCenter = new Intent(getActivity(),ClientCenter.class);
                startActivity(clientCenter);
            }
        });

        //로그아웃
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLogout();
            }
        });

        return v;
    }


    //user 정보 불러오기
    public void getLocalData(){
        SharedPreferences pref = getActivity().getSharedPreferences("User", Activity.MODE_PRIVATE);
        user_token = pref.getString("auth_token",null);
        user_name = pref.getString("username",null);
        user_id = pref.getInt("user_id",0);
        user_image = pref.getString("user_image",null);

       get_following_count(user_token,user_id);
       get_coin_count(user_token,user_id);
    }

    public void get_following_count(String user_token,int user_id){
        Call<List<Connections>> getConnections = apiService.get_followings(user_token,user_id);
        getConnections.enqueue(new Callback<List<Connections>>() {
            @Override
            public void onResponse(Call<List<Connections>> call, Response<List<Connections>> response) {
                if (response.isSuccessful()) {
                    List<Connections> connections = response.body();
                    if(connections.size()!=0){
                        for (int i = 0; i < connections.size(); i++) {
                            followingAmount.setText(String.valueOf(connections.size()));
                        }
                    }//커넥션 목록 없을때
                    else {
                        followingAmount.setText("0");
                    }
                } else {
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
            public void onFailure(Call<List<Connections>> call, Throwable t) {
                Log.i(ApplicationController.TAG, "커넥션 정보 서버 연결 실패 Message : " + t.getMessage());
            }
        });
    }

    public void get_coin_count(String user_token,int user_id){
        Call<User> userCall = apiService.getUserDetail(user_token,user_id);
        userCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.isSuccessful()){
                    coinAmount.setText(String.valueOf(response.body().getProfile().getPurchase_coin()));
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
    }

    /*
    버스커 객체를 확인하여 null 값이면 BuskerCertification 액티비티를 띄우고
    null값이 아니면 인증 상태를 확인하여 true > 버스커 채널 가기, false > 재시도, null > 대기 상태 액티비티로 이동
    */
    public void buskerCheck(Boolean certification, Busker busker){
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
        }
        else {
            //내 채널 가기
            if(certification == null){
                go_Busker.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(getActivity(),WaitPass.class);
                        startActivity(i);
                    }
                });
            } else {
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
                            Intent i = new Intent(getActivity(),FailPass.class);
                            startActivity(i);
                        }
                    });
                }
            }
        }
    }

    //버스커 객체를 확인하기 위함 busker 모델에서 인증 상태와 busker_id를 불러옴
    public void getBusker(String token,int id){
        final Busker[] busker = {new Busker()};
        final User[] userDetail = {new User()};
        Call<User> userDetailCall = apiService.getUserDetail(token,id);
        userDetailCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                userDetail[0] = response.body();
                if (response.isSuccessful()) {
                    if (userDetail[0].getBusker() != null) {
                    certification = userDetail[0].getBusker().getCertification();
                    busker_id = userDetail[0].getBusker().getBusker_id();
                    busker_type = userDetail[0].getBusker().getBusker_type();
                    team_name = userDetail[0].getBusker().getTeam_name();
                    busker_image = userDetail[0].getBusker().getBusker_image();

                        Log.e("유저 아이디", String.valueOf(id));
                        Log.e("버스커 아이디", String.valueOf(busker_id));
                        Log.e("인증상태", String.valueOf(certification));
                        Log.e("버스커", String.valueOf(busker));
                        Log.e("버스커유저정보가져오기:", "성공");
                        buskerCheck(certification, busker[0]);
                        saveBuskerInfo(busker_id,busker_type,team_name,busker_image);
                    } else {
                        buskerCheck(null, null);
                        saveBuskerInfo(0,0,null,null);
                    }
                }
                else {
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
            public void onFailure(Call<User> call, Throwable t) {
                Log.i(ApplicationController.TAG, "유저 정보 서버 연결 실패 Message : " + t.getMessage());
            }
        });
    }


    //busker 정보를 저장하기 위함 key값 : BuskerUser
    public void saveBuskerInfo(int busker_id,int busker_type,String team_name, String busker_image){
        SharedPreferences.Editor editor = prefBusker.edit();
        editor.putInt("busker_type",busker_type);
        editor.putInt("busker_id",busker_id);
        editor.putString("team_name",team_name);
        editor.putString("busker_image",busker_image);
        editor.commit();
    }

    //유저 정보를 저장하여 다른 액티비티에서 불러오기 위함
    public void saveUserInfo(String token,int user,String username,String user_image){
        SharedPreferences.Editor editor = prefUser.edit();
        editor.putString("auth_token",token);
        editor.putInt("user_id",user);
        editor.putString("username",username);
        editor.putString("user_image",user_image);
        editor.commit();
    }

    public void restApiBuilder() {
        ApplicationController application = ApplicationController.getInstance();
        application.buildNetworkService();
        apiService = ApplicationController.getInstance().getRestApiService();
    }

    //로그아웃 메소드
    public void setLogout(){
        SharedPreferences pref = getActivity().getSharedPreferences("User", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.clear(); //clear all stored data
        editor.commit();

        Intent intent = new Intent(getActivity(), LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        getActivity().finish();
    }
}