package graduationwork.buskingtown;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import graduationwork.buskingtown.api.RestApiService;
import graduationwork.buskingtown.model.Busker;
import graduationwork.buskingtown.model.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TeamMember extends AppCompatActivity {

    String user_token, user_name, user_image, busker_name, team_name, member_image;
    int user_id;

    RestApiService apiService;

    ImageView myProfile, memberProfile;
    TextView myID, memberID;
    LinearLayout standByMember, myMember;

    ArrayList<String> member_names = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_member);

        restApiBuilder();

        getLocalData();

        ImageButton backBtn = (ImageButton) findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TeamMember.super.onBackPressed();
            }
        });

        //내 프로필 보기
        ImageView myprofileimg = (ImageView) findViewById(R.id.my_profileImg);
        myprofileimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myimg = new Intent(getApplication(), MyProfileClick.class);
                startActivity(myimg);
            }
        });

        //멤버 추가
        RelativeLayout add_member_btn = (RelativeLayout) findViewById(R.id.plus_member);
        add_member_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), MemberAdd.class);
                startActivity(intent);
            }
        });

        myProfile = (ImageView) findViewById(R.id.my_profileImg);
        myID = (TextView) findViewById(R.id.my_id);

        if (user_image != null) {
            Picasso.with(getApplication()).load(user_image).transform(new CircleTransForm()).into(myProfile);
        }
        myID.setText(user_name);

        standByMember = (LinearLayout) findViewById(R.id.standByMember);
        myMember = (LinearLayout) findViewById(R.id.myMember);

        Call<List<Busker>> buskerListCall = apiService.buskerTeam(user_token, team_name);
        buskerListCall.enqueue(new Callback<List<Busker>>() {
            @Override
            public void onResponse(Call<List<Busker>> call, Response<List<Busker>> response) {
                if (response.isSuccessful()) {
                    List<Busker> buskers = response.body();
                    for (int i = 0; i < buskers.size(); i++) {
                        //나를 제외한 나와 팀네임이 같은 버스커 세팅
                        if (!user_name.equals(buskers.get(i).getBusker_name())) {
                            member_names.add(buskers.get(i).getBusker_name());
                            //멤버들 사진
                            Call<User> userDetail = apiService.getUserDetail(user_token, buskers.get(i).getUser());
                            int finalI = i;
                            userDetail.enqueue(new Callback<User>() {
                                @Override
                                public void onResponse(Call<User> call, Response<User> response) {
                                    if (response.isSuccessful()) {
                                        User member = response.body();
                                        member_image = member.getProfile().getUser_image();

                                        //certification none인 멤버 , 수락 대기 멤버 > 수락신청이 왔을때 yes를 누르면 수락 확인이 됨
                                        if (buskers.get(finalI).getCertification() == null) {
                                            RelativeLayout waitingMsg = (RelativeLayout) findViewById(R.id.waitingGroup);
                                            waitingMsg.setVisibility(View.VISIBLE);

                                            View standbylist = getLayoutInflater().inflate(R.layout.my_member, standByMember, false);
                                            memberProfile = (ImageView) standbylist.findViewById(R.id.member_profileImg);
                                            memberID = (TextView) standbylist.findViewById(R.id.member_name);

                                            memberID.setText(buskers.get(finalI).getBusker_name());
                                            if (member_image != null) {
                                                Picasso.with(getApplication()).load(member_image).transform(new CircleTransForm()).into(memberProfile);
                                            }
                                            if (standbylist.getParent() != null)
                                                ((ViewGroup) standbylist.getParent()).removeView(standbylist);
                                            standByMember.addView(standbylist);
                                        } //수락 확인한 멤버
                                        else if (buskers.get(finalI).getCertification() == true) {
                                            View successlist = getLayoutInflater().inflate(R.layout.my_member, myMember, false);
                                            memberProfile = (ImageView) successlist.findViewById(R.id.member_profileImg);
                                            memberID = (TextView) successlist.findViewById(R.id.member_name);

                                            memberID.setText(buskers.get(finalI).getBusker_name());
                                            if (member_image != null) {
                                                Picasso.with(getApplication()).load(member_image).transform(new CircleTransForm()).into(memberProfile);
                                            }
                                            if (successlist.getParent() != null)
                                                ((ViewGroup) successlist.getParent()).removeView(successlist);
                                            myMember.addView(successlist);
                                        } //수락 거절한 멤버
                                        else {
                                            Call<Busker> deleteBusker = apiService.deleteBusker(user_token, buskers.get(finalI).getBusker_id());
                                            deleteBusker.enqueue(new Callback<Busker>() {
                                                @Override
                                                public void onResponse(Call<Busker> call, Response<Busker> response) {
                                                    if (response.isSuccessful()) {
                                                    } else {
                                                        int StatusCode = response.code();
                                                        Log.i(ApplicationController.TAG, "상태 Code : " + StatusCode);
                                                    }
                                                }

                                                @Override
                                                public void onFailure(Call<Busker> call, Throwable t) {
                                                }
                                            });
                                        }
                                    } else {
                                        int StatusCode = response.code();
                                        Log.i(ApplicationController.TAG, "상태 Code : " + StatusCode);
                                        ;
                                    }
                                }

                                @Override
                                public void onFailure(Call<User> call, Throwable t) {
                                    Log.i(ApplicationController.TAG, "유저 정보 서버 연결 실패 Message : " + t.getMessage());
                                }
                            });
                        }
                    }
                } else {
                    int StatusCode = response.code();
                    Log.i(ApplicationController.TAG, "상태 Code : " + StatusCode);
                }
            }

            @Override
            public void onFailure(Call<List<Busker>> call, Throwable t) {
                Log.i(ApplicationController.TAG, "버스커인증 서버 연결 실패 Message : " + t.getMessage());
            }
        });
    }

    //user데이터 얻어오기
    public void getLocalData() {
        SharedPreferences pref = getSharedPreferences("User", Activity.MODE_PRIVATE);
        SharedPreferences busker_pref = getSharedPreferences("BuskerUser", Activity.MODE_PRIVATE);

        user_token = pref.getString("auth_token", null);
        user_name = pref.getString("username", null);
        user_id = pref.getInt("user_id", 0);
        user_image = pref.getString("user_image", null);

        busker_name = busker_pref.getString("busker_name", null);
        team_name = busker_pref.getString("team_name", null);

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
