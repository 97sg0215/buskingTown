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
import okhttp3.ResponseBody;
import retrofit2.Callback;
import retrofit2.Response;

public class Home extends Fragment {

    RestApiService apiService;

    String user_token, user_name;
    int user_id;

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

        retrofit2.Call<List<Busker>> busker_list = apiService.top_10_busker(user_token);
        busker_list.enqueue(new Callback<List<Busker>>() {
            @Override
            public void onResponse(retrofit2.Call<List<Busker>> call, Response<List<Busker>> response) {
                if(response.isSuccessful()){
                    List<Busker> busker = response.body();
                    for(int i=0; i< busker.size();i++) {
                        //허용된 버스커만 각 개인 아이디 확인
                        if(busker.get(i).getCertification()!=null&&busker.get(i).getCertification()!=false){
                            busker_id.add(busker.get(i).getBusker_id());
                            busker_image.add(busker.get(i).getBusker_image());
                            Log.e("버스커 리스트", String.valueOf(busker_id));

                            //버스커 리스트 세팅
                            final LinearLayout top_busker_list = (LinearLayout)v.findViewById(R.id.busker_top_list);
                            View list = inflater.inflate(R.layout.top_busker,top_busker_list,false);

                            Log.e("버스커", String.valueOf(busker.get(i).getTeam_name()));
                            TextView top_team_name = (TextView) list.findViewById(R.id.buskerTeamName);
                            ImageView top_team_image = (ImageView) list.findViewById(R.id.buskerProfileImangeFirst);
                            top_team_name.setText(String.valueOf(busker.get(i).getTeam_name()));
                            Picasso.with(getActivity()).load(busker_image.get(i)).transform(new CircleTransForm()).into(top_team_image);
                            if(list.getParent()!= null)
                                ((ViewGroup)list.getParent()).removeView(list);
                            top_busker_list.addView(list);

                            //각 버스커 채널 들어가기
                            int finalI = busker.get(i).getBusker_id();
                            int final_id = busker.get(i).getUser();
                            Log.e("뿅",String.valueOf(finalI));
                            list.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent buskerChannel = new Intent(getActivity(), ChannelUser.class);
                                    //개인 아이디를 다음 액티비티에서 받아 세팅
                                    buskerChannel.putExtra("busker_id",finalI);
                                    buskerChannel.putExtra("busker_user_id",final_id);
                                    Log.e("버스커 아이디 보내기",String.valueOf(finalI));
                                    Log.e("버스커 유저 아이디 보내기",String.valueOf(final_id));
                                    startActivity(buskerChannel);
                                }
                            });
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
                Log.i(ApplicationController.TAG, "유저 정보 서버 연결 실패 Message : " + t.getMessage());
            }
        });

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
