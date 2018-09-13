package graduationwork.buskingtown;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.telecom.Call;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import graduationwork.buskingtown.api.RestApiService;
import graduationwork.buskingtown.model.Busker;
import graduationwork.buskingtown.model.User;
import okhttp3.ResponseBody;
import retrofit2.Callback;
import retrofit2.Response;

public class Home extends Fragment {

    RestApiService apiService;

    String user_token, user_name;
    int user_id;

    LinearLayout top_busker_list;

    ArrayList<Integer> busker_id = new ArrayList<>();
    ArrayList<String> busker_image = new ArrayList<>();

    public Home(){
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        restApiBuilder();

        getLocalData();

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.activity_home, container, false);

        top_busker_list = (LinearLayout)v.findViewById(R.id.busker_top_list);

        return v;
    }

    public void getTopBuskerList(LayoutInflater inflater,String token){
        retrofit2.Call<List<Busker>> busker_list = apiService.get_ranker(token);
        busker_list.enqueue(new Callback<List<Busker>>() {
            @Override
            public void onResponse(retrofit2.Call<List<Busker>> call, Response<List<Busker>> response) {
                if(response.isSuccessful()){
                    List<Busker> busker = response.body();
                    //랭킹 view에 정렬이 되어있으므로 순서대로 객체10개만 불러오면 됨
                    for(int i=0; i<= 10 ;i++) {
                            try{
                                //허용된 버스커만 각 개인 아이디 확인
                                if(busker.get(i).getCertification()!=null&&busker.get(i).getCertification()!=false) {
                                    busker_id.add(busker.get(i).getBusker_id());
                                    busker_image.add(busker.get(i).getBusker_image());

                                    //버스커 리스트 세팅
                                    View list = inflater.inflate(R.layout.top_busker, top_busker_list, false);

                                    Log.e("버스커", String.valueOf(busker.get(i).getTeam_name()));
                                    TextView top_team_name = (TextView) list.findViewById(R.id.buskerTeamName);
                                    ImageView top_team_image = (ImageView) list.findViewById(R.id.buskerProfileImangeFirst);
                                    top_team_name.setText(String.valueOf(busker.get(i).getTeam_name()));

                                    if (busker_image.get(i) != null) {
                                        Picasso.with(getActivity()).load(busker_image.get(i)).transform(new CircleTransForm()).into(top_team_image);
                                    }
                                    if (list.getParent() != null)
                                        ((ViewGroup) list.getParent()).removeView(list);
                                    top_busker_list.addView(list);

                                    //각 버스커 채널 들어가기
                                    String final_busker_team = busker.get(i).getTeam_name();
                                    int finalI = busker.get(i).getBusker_id();
                                    int final_id = busker.get(i).getUser();
                                    list.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            my_channel_check(final_busker_team, finalI, final_id);
                                        }
                                    });
                                }
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                    }
                } else{
                    //에러 상태 보려고 해둔 코드
                    int StatusCode = response.code();
                    String s = response.message();
                    ResponseBody d = response.errorBody();
                    Log.i(ApplicationController.TAG, "홈 상태 Code : " + StatusCode);
                    Log.e("메세지", s);
                    Log.e("리스폰스에러바디", String.valueOf(d));
                    Log.e("리스폰스바디", String.valueOf(response.body()));
                }

            }

            @Override
            public void onFailure(retrofit2.Call<List<Busker>> call, Throwable t) {
                Log.i(ApplicationController.TAG, "버스커 정보 서버 연결 실패 Message : " + t.getMessage());
            }
        });

    }

    public void my_channel_check(String team_name,int final_busker_id, int final_user_id){
        retrofit2.Call<User> userDetail = apiService.getUserDetail(user_token,user_id);
        userDetail.enqueue(new Callback<User>() {
            @Override
            public void onResponse(retrofit2.Call<User> call, Response<User> response) {
                if(response.isSuccessful()){
                    User user = response.body();
                    if(user.getBusker()!=null){ //유저가 버스커일 경우
                        //내 채널이 아닐 경우, 팀네임이 같지않은 경우
                        if(!user.getBusker().getTeam_name().equals(team_name)){
                            Intent buskerChannel = new Intent(getActivity(), ChannelUser.class);
                            //개인 아이디를 다음 액티비티에서 받아 세팅
                            buskerChannel.putExtra("busker_id",final_busker_id);
                            buskerChannel.putExtra("busker_user_id",final_user_id);
                            startActivity(buskerChannel);
                        } //내 채널일 경우, 팀네임이 같을 경우
                        else {
                            Intent buskerChannel = new Intent(getActivity(), ChannelBusker.class);
                            //개인 아이디를 다음 액티비티에서 받아 세팅
                            buskerChannel.putExtra("busker_id",final_busker_id);
                            buskerChannel.putExtra("busker_user_id",final_user_id);
                            startActivity(buskerChannel);
                        }
                    } //내 채널이 아닐 경우, 유저가 버스커가 아닐 경우 정상 진행
                    else {
                        Intent buskerChannel = new Intent(getActivity(), ChannelUser.class);
                        //개인 아이디를 다음 액티비티에서 받아 세팅
                        buskerChannel.putExtra("busker_id",final_busker_id);
                        buskerChannel.putExtra("busker_user_id",final_user_id);
                        startActivity(buskerChannel);
                    }
                }
            }

            @Override
            public void onFailure(retrofit2.Call<User> call, Throwable t) {
                Log.i(ApplicationController.TAG, "유저 정보 서버 연결 실패 Message : " + t.getMessage());
            }
        });
    }

    //user데이터 얻어오기
    public void getLocalData(){
        SharedPreferences pref = getActivity().getSharedPreferences("User", Activity.MODE_PRIVATE);
        user_token = pref.getString("auth_token",null);
        user_name = pref.getString("username",null);
        user_id = pref.getInt("user_id",0);

        getTopBuskerList(getLayoutInflater(),user_token);
    }

    //api연결
    public void restApiBuilder() {
        ApplicationController application = ApplicationController.getInstance();
        application.buildNetworkService();
        apiService = ApplicationController.getInstance().getRestApiService();
    }
}
