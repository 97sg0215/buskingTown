package graduationwork.buskingtown;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import graduationwork.buskingtown.api.RestApiService;
import graduationwork.buskingtown.model.LendLocation;
import graduationwork.buskingtown.model.Post;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static graduationwork.buskingtown.api.RestApiService.API_URL;

public class ChannelBuskerSchedule extends Fragment {

    int test__schedule=5;
    int test__concert=5;
    int test__post=3;

    private RestApiService apiService;
    String user_token;

    View postLists;
    LinearLayout postingBox;

    private FrameLayout postingContainer;

    public ChannelBuskerSchedule(){
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.activity_channel_busker_schedule, container, false);

        for (int postCount=0; postCount<test__post; postCount++) {
            postingBox = (LinearLayout) v.findViewById(R.id.postingContainer);
            TextView postText = (TextView)v.findViewById(R.id.posttext);
            if (test__post > 1 ){
                postText.setVisibility(View.GONE);
                View postlist = inflater.inflate(R.layout.busker_posting,postingBox,false);
                if(postlist.getParent()!= null)
                    ((ViewGroup)postlist.getParent()).removeView(postlist);
                postingBox.addView(postlist);

                Call<List<Post>> postList = apiService.postList(user_token);
                postList.enqueue(new Callback<List<Post>>() {
                    @Override
                    public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                        if (response.isSuccessful()){
                            List<Post> posts = response.body();
                            if(posts.size()!=0 ){
                                for (int i=0; i<posts.size();i++){
                                    postLists = getLayoutInflater().inflate(R.layout.busker_posting,postingBox,false);


                                    ImageView postingImg = (ImageView)postLists.findViewById(R.id.postingImg);
                                    TextView postContent = (TextView) postLists.findViewById(R.id.postWriting);

                                    String loc_image = API_URL + posts.get(i).getPost_image();
                                    String loc_name = posts.get(i).getContent();

                                    int SDK_INT = android.os.Build.VERSION.SDK_INT;
                                    if (SDK_INT > 8) {
                                        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                                                .permitAll().build();
                                        StrictMode.setThreadPolicy(policy);

                                        postingImg.setImageBitmap(getBitmapFromURL(loc_image));
                                        postingImg.setScaleType(ImageView.ScaleType.FIT_XY);
                                    }

                                    postContent.setText(loc_name);


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
        }

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

    public void onClickPost(View v){
        Intent writePost = new Intent(getActivity(), WritePost.class);
        startActivity(writePost);
    }

    public void restApiBuilder() {
        ApplicationController application = ApplicationController.getInstance();
        application.buildNetworkService();
        apiService = ApplicationController.getInstance().getRestApiService();
    }

    public void getLocalData(){
        SharedPreferences pref = this.getActivity().getSharedPreferences("User", Activity.MODE_PRIVATE);
        user_token = pref.getString("auth_token",null);
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