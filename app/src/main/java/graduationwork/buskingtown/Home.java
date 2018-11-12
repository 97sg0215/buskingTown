package graduationwork.buskingtown;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import graduationwork.buskingtown.api.RestApiService;
import graduationwork.buskingtown.model.Busker;
import graduationwork.buskingtown.model.RoadConcert;
import graduationwork.buskingtown.model.User;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;

import static android.support.v7.widget.LinearLayoutManager.*;
import static graduationwork.buskingtown.R.id.buskerStory_list;
import static graduationwork.buskingtown.R.id.goingConcert_list;


public class Home extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    SharedPreferences prefUser, prefBusker;

    private static final String TAG = "Home";

    RestApiService apiService;

    String user_token, user_name;
    int user_id;

    LinearLayout top_busker_list,livebusking,goingConcert_list,buskerStory_list;

    ImageView goingconcertImge;

    // 다가오는 버스커
    TextView buskerId,concertInfo;

    ArrayList<Integer> busker_id = new ArrayList<>();
    ArrayList<String> busker_image = new ArrayList<>();


    SwipeRefreshLayout mSwipeRefreshLayout;

    RecyclerView recyclerView;
    PagerSnapHelper pagerSnapHelper;
    LinearLayoutManager layoutManager;
    Handler handler;
    Runnable runnable;

    String live_team_name, live_tag, live_profile;

    public Home(){
        // Required empty public constructor
    }

    @SuppressLint("WrongViewCast")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        prefBusker = this.getActivity().getSharedPreferences("BuskerUser", Activity.MODE_PRIVATE);
        prefUser = this.getActivity().getSharedPreferences("User", Activity.MODE_PRIVATE);

        //로딩코드
        CheckTypesTask task = new CheckTypesTask();
        task.execute();

        restApiBuilder();

        getLocalData();

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.activity_home, container, false);

        mSwipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipe_layout);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        top_busker_list = (LinearLayout)v.findViewById(R.id.busker_top_list);
        livebusking = (LinearLayout)v.findViewById(R.id.livebusking);
        goingConcert_list = (LinearLayout)v.findViewById(R.id.goingConcert_list);
        buskerStory_list = (LinearLayout)v.findViewById(R.id.buskerStory_list);

        recyclerView = (RecyclerView)v.findViewById(R.id.slide_recyclerview);
        layoutManager = new LinearLayoutManager(getContext(), HORIZONTAL, false);
        pagerSnapHelper = new PagerSnapHelper();
        if(handler == null) {
            handler = new Handler();
            runnable = new Runnable() {
                @Override
                public void run() {
                    if (layoutManager.findFirstVisibleItemPosition() != 4) {
                        recyclerView.smoothScrollToPosition(layoutManager.findFirstVisibleItemPosition() + 1);
                    } else {
                        recyclerView.smoothScrollToPosition(0);
                    }
                    handler.postDelayed(this, 5000);
                }
            };
        }
        handler.postDelayed(runnable, 5000);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(new RecyclerViewAdapter(getContext()));
        pagerSnapHelper.attachToRecyclerView(recyclerView);

        //라이브 버스킹
        //현재시간
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat stf = new SimpleDateFormat("HH:MM");
        String getDate = sdf.format(date);
        String getTime = stf.format(date);

        final LinearLayout livebuskingBox = (LinearLayout) v.findViewById(R.id.livebusking);
        final RelativeLayout liveBusker = (RelativeLayout) v.findViewById(R.id.liveBusker);
        Call<List<RoadConcert>> listCall = apiService.getLiveBuking(user_token,getDate,getTime);
        listCall.enqueue(new Callback<List<RoadConcert>>() {
            @Override
            public void onResponse(Call<List<RoadConcert>> call, Response<List<RoadConcert>> response) {
                if(response.isSuccessful()){
                    List<RoadConcert> liveConcert = response.body();
                    if(liveConcert.size()!=0){
                        liveBusker.setVisibility(View.GONE);
                        for(int i=0;i<liveConcert.size();i++){
                            View livelist = inflater.inflate(R.layout.livebusking,livebuskingBox,false);
                            TextView teamname = (TextView) livelist.findViewById(R.id.buskerId);
                            TextView tag = (TextView) livelist.findViewById(R.id.buskertag);
                            TextView loc = (TextView) livelist.findViewById(R.id.liveInfo);
                            ImageView profile = (ImageView) livelist.findViewById(R.id.buskerImg);

                            Call<Busker> buskerCall = apiService.buskerDetail(user_token,liveConcert.get(i).getBusker());
                            buskerCall.enqueue(new Callback<Busker>() {
                                @Override
                                public void onResponse(Call<Busker> call, Response<Busker> response) {
                                    if(response.isSuccessful()){
                                        Busker busker = response.body();
                                        live_team_name = busker.getTeam_name();
                                        live_tag = busker.getBusker_tag();
                                        live_profile = busker.getBusker_image();
                                        Picasso.with(getContext()).load(live_profile).transform(new CircleTransForm()).into(profile);
                                        teamname.setText(live_team_name);
                                        tag.setText(live_tag);
                                    }
                                }

                                @Override
                                public void onFailure(Call<Busker> call, Throwable t) {

                                }
                            });

                            String[] start_time_words = liveConcert.get(i).getRoad_concert_start_time().split(":");
                            String[] end_time_words = liveConcert.get(i).getRoad_concert_end_time().split(":");
                            String start_time = start_time_words[0] + ":" +start_time_words[1];
                            String end_time = end_time_words[0] + ":" +end_time_words[1];

                            String loc_info = liveConcert.get(i).getRoad_name() + " | " + start_time + "~" + end_time;
                            loc.setText(loc_info);

                            if(livelist.getParent()!= null)
                                ((ViewGroup)livelist.getParent()).removeView(livelist);
                            livebuskingBox.addView(livelist);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<List<RoadConcert>> call, Throwable t) {

            }
        });

        //다가오는 콘서트
        View concertlist = inflater.inflate(R.layout.goingconcert, goingConcert_list, false);
        if(concertlist.getParent()!= null)
            ((ViewGroup)concertlist.getParent()).removeView(concertlist);
        goingConcert_list.addView(concertlist);

        //좋아하는 버스커 스토리
        View storylist = inflater.inflate(R.layout.buskerstory, buskerStory_list, false);
        if(storylist.getParent()!= null)
            ((ViewGroup)storylist.getParent()).removeView(storylist);
        buskerStory_list.addView(storylist);



        return v;
    }


    //로딩코드, fragment일때는 getContext()를 씀
    private class CheckTypesTask extends AsyncTask<Void, Void, Void> {

        ProgressDialog asyncDialog = new ProgressDialog(
                getContext());

        @Override
        protected void onPreExecute() {
            asyncDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            asyncDialog.setMessage("로딩중입니다..");

            // show dialog
            asyncDialog.show();
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            try {
                for (int i = 0; i < 5; i++) {
                    //asyncDialog.setProgress(i * 30);
                    Thread.sleep(300);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            asyncDialog.dismiss();
            super.onPostExecute(result);
        }
    }

    public void getTopBuskerList(LayoutInflater inflater,String token){
        retrofit2.Call<List<Busker>> busker_list = apiService.get_ranker(token);
        busker_list.enqueue(new Callback<List<Busker>>() {
            @Override
            public void onResponse(retrofit2.Call<List<Busker>> call, Response<List<Busker>> response) {
                if(response.isSuccessful()){
                    List<Busker> busker = response.body();
                    List<Busker> buskers = null;
                    //랭킹 view에 정렬이 되어있으므로 순서대로 객체10개만 불러오면 됨
                    if(busker.size()>10){
                        for(int i=0; i< 10 ;i++) {
                                getTopAllList(inflater,busker,i);
                        }
                    }else {
                        for (int i=0; i< busker.size();i++){
                                getTopAllList(inflater,busker,i);
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


    public void getTopAllList(LayoutInflater inflater,List<Busker>busker, int i){
        try{
            //허용된 버스커만 각 개인 아이디 확인
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
        }catch (Exception e){
            e.printStackTrace();
        }
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
                            buskerChannel.putExtra("team_name",team_name);
                            buskerChannel.putExtra("busker_user_id",final_user_id);
                            startActivity(buskerChannel);
                        } //내 채널일 경우, 팀네임이 같을 경우
                        else {
                            Intent buskerChannel = new Intent(getActivity(), ChannelBusker.class);
                            //개인 아이디를 다음 액티비티에서 받아 세팅
                            buskerChannel.putExtra("busker_id",final_busker_id);
                            buskerChannel.putExtra("busker_user_id",final_user_id);
                            saveBuskerInfo(final_busker_id,user.getBusker().getBusker_type(),team_name,user.getBusker().getBusker_image());
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
        user_token = prefUser.getString("auth_token",null);
        user_name = prefUser.getString("username",null);
        user_id = prefUser.getInt("user_id",0);
        getTopBuskerList(getLayoutInflater(),user_token);
    }

    //api연결
    public void restApiBuilder() {
        ApplicationController application = ApplicationController.getInstance();
        application.buildNetworkService();
        apiService = ApplicationController.getInstance().getRestApiService();
    }

    @Override
    public void onRefresh() {
        // 새로고침 코드
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(this).attach(this).commit();

        // 새로고침 완료
        mSwipeRefreshLayout.setRefreshing(false);
    }

    public void saveBuskerInfo(int busker_id,int busker_type,String team_name, String busker_image){
        SharedPreferences.Editor editor = prefBusker.edit();
        editor.putInt("busker_type",busker_type);
        editor.putInt("busker_id",busker_id);
        editor.putString("team_name",team_name);
        editor.putString("busker_image",busker_image);
        editor.commit();
    }

}
