package graduationwork.buskingtown;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import graduationwork.buskingtown.api.RestApiService;
import graduationwork.buskingtown.model.LendLocation;
import graduationwork.buskingtown.model.LikePost;
import graduationwork.buskingtown.model.Post;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static graduationwork.buskingtown.api.RestApiService.API_URL;

public class ChannelBuskerSchedule extends Fragment {

    int test__schedule=5;
    int test__concert=5;
    int test__post=3;

    private RestApiService apiService;
    String user_token,team_name,busker_image;
    int busker_id,user_id;

    SharedPreferences prefUser, prefBusker;

    View postLists;
    LinearLayout postingBox;

    ArrayList<Integer> all_post_id = new ArrayList<>();
    ArrayList<Integer> all_busker_id = new ArrayList<>();
    ArrayList<Integer> all_like_post_id = new ArrayList<>();

    private FrameLayout postingContainer;

    public ChannelBuskerSchedule(){
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.activity_channel_busker_schedule, container, false);

        prefBusker = this.getActivity().getSharedPreferences("BuskerUser", Activity.MODE_PRIVATE);
        prefUser = this.getActivity().getSharedPreferences("User", Activity.MODE_PRIVATE);

        restApiBuilder();

        getLocalData();

        postingBox = (LinearLayout) v.findViewById(R.id.postingContainer);
        TextView postText = (TextView)v.findViewById(R.id.posttext);
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
                            ImageButton post_setting = (ImageButton) postLists.findViewById(R.id.spinner_drop);

                            // 정우 여기!!post_setting 눌렀을때 삭제하기, 수정하기 뜨게


                            String post_image = API_URL + posts.get(i).getPost_image();
                            String post_content = posts.get(i).getContent();

                            String post_date = posts.get(i).getCreated_at();
                            String[] date_words = post_date.split("-");

                            Picasso.with(getActivity()).load(busker_image).transform(new CircleTransForm()).into(buskerImg);

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



        for (int scheduleCount=0; scheduleCount<test__schedule; scheduleCount++) {
            final ImageButton dropdownBtn = (ImageButton)v.findViewById(R.id.dropdown_sch);
            final LinearLayout scheduleBox = (LinearLayout)v.findViewById(R.id.addSchedule_sch);

            if (test__schedule > 1 ){
                dropdownBtn.setVisibility(View.VISIBLE);
                View list = inflater.inflate(R.layout.schedule_list,scheduleBox,false);
                if(list.getParent()!= null)
                    ((ViewGroup)list.getParent()).removeView(list);
                scheduleBox.addView(list);
                scheduleBox.setVisibility(View.GONE);
                dropdownBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v){
                        scheduleBox.setVisibility(View.VISIBLE);
                        dropdownBtn.setVisibility(View.INVISIBLE);
                    }
                });
            }
        }

        for (int concertCount=0; concertCount<test__concert; concertCount++){
            final ImageButton concertdropdownBtn = (ImageButton)v.findViewById(R.id.dropdown_con);
            final LinearLayout concertBox = (LinearLayout)v. findViewById(R.id.addConcert_con);

            if (test__concert > 1 ){
                concertdropdownBtn.setVisibility(View.VISIBLE);
                View concertlist = inflater.inflate(R.layout.concert_list,concertBox,false);
                if(concertlist.getParent()!=null)
                    ((ViewGroup)concertlist.getParent()).removeView(concertlist);
                concertBox.addView(concertlist);
                concertBox.setVisibility(View.GONE);
                concertdropdownBtn.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        concertBox.setVisibility(View.VISIBLE);
                        concertdropdownBtn.setVisibility(View.INVISIBLE);
                    }
                });
            }
        }
        return v;
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
                                heart.setBackground(getActivity().getResources().getDrawable(R.drawable.like_f));
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
        heart.setBackground(getActivity().getResources().getDrawable(R.drawable.like_f));
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
        heart.setBackground(getActivity().getResources().getDrawable(R.drawable.like));
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

    public void restApiBuilder() {
        ApplicationController application = ApplicationController.getInstance();
        application.buildNetworkService();
        apiService = ApplicationController.getInstance().getRestApiService();
    }

    public void getLocalData(){
        user_token = prefUser.getString("auth_token",null);
        user_id = prefUser.getInt("user_id",0);
        busker_id = prefBusker.getInt("busker_id",0);
        team_name = prefBusker.getString("team_name",null);
        busker_image = prefBusker.getString("busker_image",null);
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

}