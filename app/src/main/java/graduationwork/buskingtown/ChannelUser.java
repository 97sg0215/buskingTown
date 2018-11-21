package graduationwork.buskingtown;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.Image;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.Channel;
import java.util.ArrayList;
import java.util.List;

import graduationwork.buskingtown.api.RestApiService;
import graduationwork.buskingtown.model.Busker;
import graduationwork.buskingtown.model.Connections;
import graduationwork.buskingtown.model.LikePost;
import graduationwork.buskingtown.model.Post;
import graduationwork.buskingtown.model.Profile;
import graduationwork.buskingtown.model.RoadConcert;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static graduationwork.buskingtown.api.RestApiService.API_URL;

public class ChannelUser extends AppCompatActivity {

    int busker_id, user_id, connection_id, busker_coin;
    String user_token, user_name, busker_team_name, busker_tag, busker_image,team_name;

    //버스커 정보 세팅
    TextView mainTeamName, subTeamName, tag, smileCount;
    ImageView busker_main_image;

    Button following_btn;

    View postLists;
    LinearLayout postingBox;

    ArrayList<Integer> all_user_id = new ArrayList<>();
    ArrayList<Integer> all_busker_id = new ArrayList<>();
    ArrayList<Integer> get_follower_id = new ArrayList<>();
    ArrayList<Integer> all_post_id = new ArrayList<>();

    RestApiService apiService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channel_user);

        //로딩코드
        CheckTypesTask task = new CheckTypesTask();
        task.execute();

        restApiBuilder();

        getLocalData();

        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        ImageButton backBtn = (ImageButton) findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {ChannelUser.super.onBackPressed();
            }
        });


        busker_id = getIntent().getIntExtra("busker_id", busker_id);
        team_name = getIntent().getStringExtra("team_name");

        final ImageButton dropdownBtn = (ImageButton) findViewById(R.id.dropdown_sch);
        final LinearLayout scheduleBox = (LinearLayout) findViewById(R.id.addSchedule_sch);
        Call<List<RoadConcert>> listCall = apiService.getNextReservationRoadConcert(user_token,busker_id);
        listCall.enqueue(new Callback<List<RoadConcert>>() {
            @Override
            public void onResponse(Call<List<RoadConcert>> call, Response<List<RoadConcert>> response) {
                if(response.isSuccessful()){
                    List<RoadConcert> roadConcerts = response.body();
                    if(roadConcerts.size()!=0){

                        TextView scheduleList = (TextView) findViewById(R.id.scheduleList);

                        String[] first_date_words = roadConcerts.get(0).getRoad_concert_date().split("-");
                        String first_date = first_date_words[1] + "." +first_date_words[2];

                        String[] first_start_time_words = roadConcerts.get(0).getRoad_concert_start_time().split(":");
                        String[] first_end_time_words = roadConcerts.get(0).getRoad_concert_end_time().split(":");
                        String first_start_time = first_start_time_words[0] + ":" +first_start_time_words[1];
                        String first_end_time = first_end_time_words[0] + ":" +first_end_time_words[1];

                        String setting_txt = first_date +" "+roadConcerts.get(0).getRoad_name() +" "+ first_start_time + "~"+ first_end_time;

                        if(setting_txt.length()>50){
                            scheduleList.setText(setting_txt.substring(0,24)+"...");
                        }else {
                            scheduleList.setText(setting_txt);
                        }

                        if(roadConcerts.size()>1){
                            dropdownBtn.setVisibility(View.VISIBLE);

                            dropdownBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v){
                                    dropdownBtn.setVisibility(View.GONE);
                                    for(int i=1;i<roadConcerts.size();i++){
                                        View list = inflater.inflate(R.layout.schedule_list,scheduleBox,false);

                                        String[] date_words = roadConcerts.get(i).getRoad_concert_date().split("-");
                                        String date = date_words [1] + "." +date_words [2];

                                        String[] start_time_words = roadConcerts.get(i).getRoad_concert_start_time().split(":");
                                        String[] end_time_words = roadConcerts.get(i).getRoad_concert_end_time().split(":");
                                        String start_time = start_time_words[0] + ":" +start_time_words[1];
                                        String end_time = end_time_words[0] + ":" +end_time_words[1];

                                        String setting_txt = roadConcerts.get(i).getRoad_name() +" "+ start_time + "~"+ end_time;

                                        TextView dateSe = (TextView) list.findViewById(R.id.dateSe);
                                        TextView scheduleListtxt = (TextView) list.findViewById(R.id.scheduleList);

                                        dateSe.setText(date);
                                        scheduleListtxt.setText(setting_txt);

                                        if(list.getParent()!= null)
                                            ((ViewGroup)list.getParent()).removeView(list);
                                        scheduleBox.addView(list);

                                    }
                                }
                            });
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<List<RoadConcert>> call, Throwable t) {

            }
        });

        final Busker[] busker = {new Busker()};
        retrofit2.Call<Busker> buskerDetail = apiService.buskerDetail(user_token,busker_id);
        buskerDetail.enqueue(new Callback<Busker>() {
            @Override
            public void onResponse(retrofit2.Call<Busker> call, Response<Busker> response) {
                if(response.isSuccessful()){
                    busker[0] = response.body();
                    busker_team_name = busker[0].getTeam_name();
                    busker_tag = busker[0].getBusker_tag();
                    busker_image = busker[0].getBusker_image();
                    busker_coin = busker[0].getReceived_coin();
                    buskerSetting(busker_team_name,busker_tag,busker_image);
                    following_check(busker[0].getBusker_id());
                }
                else {
                    //에러 상태 보려고 해둔 코드
                    int StatusCode = response.code();
                    String s = response.message();
                    ResponseBody d = response.errorBody();
                    Log.i(ApplicationController.TAG, "상태 Code : " + StatusCode);
                    Log.e("메세지", s);
                    Log.e("리스폰스에러바디", String.valueOf(d));
                    Log.e("리스폰스바디 : ", String.valueOf(response.body()));
                }
            }
            @Override
            public void onFailure(retrofit2.Call<Busker> call, Throwable t) {
                Log.i(ApplicationController.TAG, "유저 정보 서버 연결 실패 Message : " + t.getMessage());
            }
        });

        smileCount = (TextView) findViewById(R.id.smileCount);
        //정보 세팅
        retrofit2.Call<List<Connections>> call2 = apiService.get_followers(user_token,busker_id);
        call2.enqueue(new Callback<List<Connections>>() {
            @Override
            public void onResponse(retrofit2.Call<List<Connections>> call, Response<List<Connections>> response) {
                if(response.isSuccessful()){
                    List<Connections> connections = response.body();
                    if(connections.size()!=0){
                        smileCount.setText(String.valueOf(connections.size())+"명");
                    }
                }
            }

            @Override
            public void onFailure(retrofit2.Call<List<Connections>> call, Throwable t) {

            }
        });

        //코인보내기 창 가기
        android.support.design.widget.FloatingActionButton fab = (android.support.design.widget.FloatingActionButton) findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent CoinSendBefore_pop = new Intent(getApplication(), CoinSendBefore_pop.class);
                CoinSendBefore_pop.putExtra("busker_id",busker_id);
                CoinSendBefore_pop.putExtra("busker_coin",busker_coin);
                startActivity(CoinSendBefore_pop);
            }
        });

        //버스커 정보 세팅
        mainTeamName = (TextView) findViewById(R.id.busker_main_team_name);
        subTeamName = (TextView) findViewById(R.id.busker_sub_team_name);
        tag = (TextView) findViewById(R.id.tag);
        busker_main_image = (ImageView) findViewById(R.id.profilebig);
        following_btn = (Button) findViewById(R.id.fan_on);


        //포스팅
        postingBox = (LinearLayout) findViewById(R.id.postingContainer_view);
        TextView postText = (TextView) findViewById(R.id.posttext_view);
        Call<List<Post>> postList = apiService.postList(user_token,busker_id);
        postList.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                if (response.isSuccessful()){
                    List<Post> posts = response.body();
                    if(posts.size()!=0 ){
                        for (int i=0; i<posts.size();i++){
                            postText.setVisibility(View.GONE);
                            postLists = inflater.inflate(R.layout.busker_posting,postingBox,false);

                            ImageView postingImg = (ImageView)postLists.findViewById(R.id.postingImg);
                            ImageView buskerImg = (ImageView) postLists.findViewById(R.id.profileSmall);
                            TextView postContent = (TextView) postLists.findViewById(R.id.postWriting);
                            TextView postTeamname = (TextView) postLists.findViewById(R.id.buskerId);
                            TextView postMainTeamName = (TextView) postLists.findViewById(R.id.main_team_name);
                            TextView postDate = (TextView) postLists.findViewById(R.id.post_date);
                            ImageButton like = (ImageButton) postLists.findViewById(R.id.like);

                            String post_image = API_URL + posts.get(i).getPost_image();
                            String post_content = posts.get(i).getContent();

                            String post_date = posts.get(i).getCreated_at();
                            String[] date_words = post_date.split("-");

                            Picasso.with(getApplicationContext()).load(busker_image).transform(new CircleTransForm()).into(buskerImg);

                            postContent.setText(post_content);
                            postTeamname.setText(team_name);
                            postMainTeamName.setText(team_name);
                            postDate.setText(date_words[1] +"월 "+date_words[2]+"일 ");

                            like_check(user_token,user_id,posts.get(i).getPost_id(),like);

                            int SDK_INT = android.os.Build.VERSION.SDK_INT;
                            if (SDK_INT > 8) {
                                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                                        .permitAll().build();
                                StrictMode.setThreadPolicy(policy);

                                postingImg.setImageBitmap(getBitmapFromURL(post_image));
                                postingImg.setScaleType(ImageView.ScaleType.FIT_XY);
                            }

                            if(postLists.getParent()!= null)
                                ((ViewGroup)postLists.getParent()).removeView(postLists);
                            postingBox.addView(postLists);
                        }
                    }
                }else {
                    int StatusCode = response.code();
                    Log.i(ApplicationController.TAG, "상태 Code : " + StatusCode);
                    Log.e("메세지", String.valueOf(response.message()));
                    Log.e("리스폰스에러바디", String.valueOf(response.errorBody()));
                    Log.e("리스폰스바디", String.valueOf(response.body()));
                }
            }
            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                Log.i(ApplicationController.TAG, "게시물 리스트 서버 연결 실패 Message : " + t.getMessage());
            }
        });
    }

    public void following_check(int busker_id){
        Call<List<Connections>> getConnections = apiService.get_followers(user_token, busker_id);
        getConnections.enqueue(new Callback<List<Connections>>() {
            @Override
            public void onResponse(Call<List<Connections>> call, Response<List<Connections>> response) {
                if (response.isSuccessful()) {
                    List<Connections> connections = response.body();
                    if(connections.size()!=0){
                        for (int i = 0; i < connections.size(); i++) {
                            all_user_id.add(connections.get(i).getUser());
                            //팔로우 되어있을 경우
                            if (user_id == all_user_id.get(i)) {
                                connection_id = connections.get(i).getConnection_id();
                                following_btn.setBackground(getDrawable(R.drawable.fan_on_btn));
                                following_btn.setTextColor(Color.parseColor("#000000"));
                                following_btn.setText("팬이에요");
                                following_btn.setOnClickListener(new OnSingleClickListener() {
                                    @Override
                                    public void onSingleClick(View v) {
                                        unfollowing(connection_id);
                                    }
                                });

                            }//팔로우 안되어 있을 경우
                            else {
                                following_btn.setOnClickListener(new OnSingleClickListener() {
                                    @Override
                                    public void onSingleClick(View v) {
                                        follow(user_id,busker_id);
                                    }
                                });
                            }
                        }
                    }//커넥션 목록 없을때
                    else {
                        following_btn.setOnClickListener(new OnSingleClickListener() {
                            @Override
                            public void onSingleClick(View v) {
                                follow(user_id,busker_id);
                            }
                        });
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

    public void follow(int user_id, int busker_id){
        Connections connections = new Connections();
        connections.setUser(user_id);
        connections.setFollowing(busker_id);
        following_btn.setBackground(getDrawable(R.drawable.fan_on_btn));
        following_btn.setTextColor(getResources().getColor(R.color.mainPurple));
        following_btn.setText("팬이에요");

        Call<Connections> follow = apiService.follow(user_token,connections);
        follow.enqueue(new Callback<Connections>() {
            @Override
            public void onResponse(Call<Connections> call, Response<Connections> response) {
                if(response.isSuccessful()){
                    Log.e("팔로워ok", String.valueOf(user_id));
                    Log.e("버스커ok", String.valueOf(busker_id));

                    int connection_id = response.body().getConnection_id();
                    Log.e("커넥션 아이디",String.valueOf(connection_id));
                    following_btn.setOnClickListener(new OnSingleClickListener() {
                        @Override
                        public void onSingleClick(View v) {
                            unfollowing(connection_id);
                        }
                    });
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
            public void onFailure(Call<Connections> call, Throwable t) {
                Log.i(ApplicationController.TAG, "팔로우 정보 서버 연결 실패 Message : " + t.getMessage());
                }
        });

    }

    public void unfollowing(int connection_id){
        following_btn.setText("팬 할래요");
        following_btn.setTextColor(Color.parseColor("#FFFFFF"));
        following_btn.setBackground(getDrawable(R.drawable.able_btn));
        Call<Connections> unfollow = apiService.unfollow(user_token,connection_id);
        unfollow.enqueue(new Callback<Connections>() {
            @Override
            public void onResponse(Call<Connections> call, Response<Connections> response) {
                if (response.isSuccessful()) {
                    Log.e("언팔로우:", "완료");
                    Log.e("언팔로우된 커넥션 아이디:", String.valueOf(connection_id));
                    following_btn.setOnClickListener(new OnSingleClickListener() {
                        @Override
                        public void onSingleClick(View v) {
                            follow(user_id,busker_id);
                        }
                    });
                }
                    else {
                    int StatusCode = response.code();
                    Log.i(ApplicationController.TAG, "상태 Code : " + StatusCode);
                    Log.e("메세지", String.valueOf(response.message()));
                    Log.e("리스폰스에러바디", String.valueOf(response.errorBody()));
                    Log.e("리스폰스바디", String.valueOf(response.body()));
                    }
            }

            @Override
            public void onFailure(Call<Connections> call, Throwable t) {
                Log.i(ApplicationController.TAG, "언팔로우 정보 서버 삭제 실패 Message : " + t.getMessage());
                }
        });
    }

    public void like_check(String user_token, int user_id, int post_id, ImageButton heart){
        Call<List<LikePost>> getLikePostCall = apiService.getLikePost(user_token,user_id);
        getLikePostCall.enqueue(new Callback<List<LikePost>>() {
            @Override
            public void onResponse(Call<List<LikePost>> call, Response<List<LikePost>> response) {
                List<LikePost> likePosts = response.body();
                if(response.isSuccessful()){
                    if(likePosts.size()!=0){
                        for(int i=0; i <likePosts.size(); i++){
                            all_post_id.add(likePosts.get(i).getPost());
                            //좋아요 되어 있을 경우
                            if(post_id == all_post_id.get(i)){
                                heart.setBackground(getDrawable(R.drawable.like_f));
                                int finalI = i;
                                heart.setOnClickListener(new OnSingleClickListener() {
                                    @Override
                                    public void onSingleClick(View v) {
                                        post_unlike(user_token,user_id,post_id,likePosts.get(finalI).getLike_post_id(),heart);
                                    }
                                });
                            }
                            else {
                                heart.setOnClickListener(new OnSingleClickListener() {
                                    @Override
                                    public void onSingleClick(View v) {
                                        post_like(user_token,user_id,post_id,heart);
                                    }
                                });
                            }
                        }
                    }else {
                        heart.setOnClickListener(new OnSingleClickListener() {
                            @Override
                            public void onSingleClick(View v) {
                                post_like(user_token,user_id,post_id,heart);
                            }
                        });
                    }
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
            public void onFailure(Call<List<LikePost>> call, Throwable t) {

            }
        });
    }

    public void post_like(String user_token, int user_id,int post_id, ImageButton heart){
        LikePost likePost = new LikePost();
        likePost.setLikes(user_id);
        likePost.setPost(post_id);
        likePost.setBusker(busker_id);
        heart.setBackground(this.getResources().getDrawable(R.drawable.like_f));
        Call<LikePost> likePostCall = apiService.likePost(user_token, likePost);
        likePostCall.enqueue(new Callback<LikePost>() {
            @Override
            public void onResponse(Call<LikePost> call, Response<LikePost> response) {
                if(response.isSuccessful()){
                    heart.setOnClickListener(new OnSingleClickListener() {
                        @Override
                        public void onSingleClick(View v) {
                            post_unlike(user_token,user_id,post_id,response.body().getLike_post_id(),heart);
                        }
                    });
                }else {
                    int StatusCode = response.code();
                    String s = response.message();
                    ResponseBody d = response.errorBody();
                    Log.i(ApplicationController.TAG, "상태 Code : " + StatusCode);
                    Log.e("메세지", String.valueOf(user_id));
                    Log.e("메세지", String.valueOf(post_id));
                    Log.e("메세지", String.valueOf(busker_id));

                    Log.e("메세지", s);
                    Log.e("리스폰스에러바디", String.valueOf(d));
                    Log.e("리스폰스바디", String.valueOf(response.body()));
                }
            }

            @Override
            public void onFailure(Call<LikePost> call, Throwable t) {

            }
        });
    }

    public void post_unlike(String user_token, int user_id,int post_id,int like_post_id, ImageButton heart){
        Call<LikePost> likePostCall = apiService.unlikePost(user_token,like_post_id);
        heart.setBackground(getResources().getDrawable(R.drawable.like));
        likePostCall.enqueue(new Callback<LikePost>() {
            @Override
            public void onResponse(Call<LikePost> call, Response<LikePost> response) {
                if(response.isSuccessful()){
                    heart.setOnClickListener(new OnSingleClickListener() {
                        @Override
                        public void onSingleClick(View v) {
                            post_like(user_token,user_id,post_id,heart);
                        }
                    });
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
            public void onFailure(Call<LikePost> call, Throwable t) {

            }
        });
    }

    //user데이터 얻어오기
    public void getLocalData(){
        SharedPreferences pref = getSharedPreferences("User", Activity.MODE_PRIVATE);
        user_token = pref.getString("auth_token",null);
        user_name = pref.getString("username",null);
        user_id = pref.getInt("user_id",0);
    }

    //버스커 정보 화면에 세팅(이름 및 팔로워등 세팅, 메소드가 길어지면 게시글 같은 건 따로 메소드 정의하세요)
    public void buskerSetting(String busker_team_name, String busker_tag,String busker_image){
        Log.e("버스커 정보", String.valueOf(busker_team_name));
        mainTeamName.setText(busker_team_name);
        subTeamName.setText(busker_team_name);
        tag.setText(busker_tag);

        Picasso.with(getApplication()).load(busker_image).transform(new CircleTransForm()).into(busker_main_image);
    }


    public Bitmap getBitmapFromURL(String src) {
        HttpURLConnection connection = null;
        try {
            URL url = new URL(src);
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }finally{
            if(connection!=null)connection.disconnect();
        }
    }

    //api연결
    public void restApiBuilder() {
        ApplicationController application = ApplicationController.getInstance();
        application.buildNetworkService();
        apiService = ApplicationController.getInstance().getRestApiService();
    }


    public void previousActivity(View v){
        onBackPressed();
    }

    //로딩해주는 코드
    private class CheckTypesTask extends AsyncTask<Void, Void, Void> {

        ProgressDialog asyncDialog = new ProgressDialog(
                ChannelUser.this);

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

}