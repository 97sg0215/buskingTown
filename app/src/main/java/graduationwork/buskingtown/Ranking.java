package graduationwork.buskingtown;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import graduationwork.buskingtown.api.RestApiService;
import graduationwork.buskingtown.model.Busker;
import graduationwork.buskingtown.model.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Ranking extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private TextView day, week, month;
    private View dayBar, weekBar, monthBar;

    String user_token, user_name;
    int user_id;

    RestApiService apiService;


    //리스트로 정보를 받아옴
    ArrayList<Integer> busker_id;
    ArrayList<String> busker_image;
    ArrayList<String> busker_tag;
    ArrayList<String> busker_team_name;
    ArrayList<Bitmap> busker_image_bitmap;

    //화면에 세팅되는 리스트뷰
    ListView listView;
    //리스트 연결 어댑터
    RankingCustom mAdapter;
    //리스트에 들어갈 정보들
    ArrayList<RankListItem> listItems;

    SwipeRefreshLayout mSwipeRefreshLayout;

    ImageView buskerImage;
    TextView buskerId, buskerTag;

    public Ranking() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup containter, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.activity_ranking, containter, false);

        restApiBuilder();

        getLocalData();

        //로딩코드
        CheckTypesTask task = new CheckTypesTask();
        task.execute();

        mSwipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipe_layout);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        day = (TextView) v.findViewById(R.id.dayRanking);
        week = (TextView) v.findViewById(R.id.weekRanking);
        month = (TextView) v.findViewById(R.id.monthRanking);

        final View dayBar = (View) v.findViewById(R.id.dayRankingBar);
        final View weekBar = (View) v.findViewById(R.id.weekRankingBar);
        final View monthBar = (View) v.findViewById(R.id.monthRankingBar);

        buskerId = (TextView) v.findViewById(R.id.buskerId);
        buskerTag = (TextView) v.findViewById(R.id.buskerTag);
        buskerImage = (ImageView) v.findViewById(R.id.buskerImage);


        //변수에 선언해준 것을 정의
        listItems = new ArrayList<RankListItem>();
        listView = (ListView) v.findViewById(R.id.busker_list);
        mAdapter = new RankingCustom(listItems);
        busker_id = new ArrayList<>();
        busker_team_name = new ArrayList<>();
        busker_tag = new ArrayList<>();
        busker_image = new ArrayList<>();
        busker_image_bitmap = new ArrayList<>();


        Call<List<Busker>> get_busker_rank = apiService.get_ranker(user_token);
        get_busker_rank.enqueue(new Callback<List<Busker>>() {
            @Override
            public void onResponse(Call<List<Busker>> call, Response<List<Busker>> response) {
                if (response.isSuccessful()) {
                    List<Busker> busker = response.body();
                    for (int i = 0; i < busker.size(); i++) {
                        busker_id.add(busker.get(i).getBusker_id());
                        busker_team_name.add(busker.get(i).getTeam_name());
                        busker_tag.add(busker.get(i).getBusker_tag());
                        busker_image.add(busker.get(i).getBusker_image());
                        int SDK_INT = android.os.Build.VERSION.SDK_INT;
                        if (SDK_INT > 8) {
                            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                                    .permitAll().build();
                            StrictMode.setThreadPolicy(policy);
                            busker_image_bitmap.add(getBitmapFromURL(busker_image.get(i)));
                        }
                        int rank = i + 1;
                        //리스트 아이템(정보, 아래에 class객체 선언 해둠)에 정보를 받아와 세팅함 , 수경이 할것(테스트할때 임시데이터를 넣어서 해주세요)
                        listItems.add(new RankListItem(rank, busker_team_name.get(i), busker_tag.get(i), busker_image_bitmap.get(i), busker_id.get(i)));
                    }
                    //임시
                    int SDK_INT = android.os.Build.VERSION.SDK_INT;
                    if (SDK_INT > 8) {
                        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                                .permitAll().build();
                        StrictMode.setThreadPolicy(policy);
                        buskerImage.setImageBitmap(getBitmapFromURL(busker_image.get(5)));
                    }
                    buskerId.setText(busker.get(5).getBusker_name());
                    buskerTag.setText(busker.get(5).getBusker_tag());
                    //화면 리스트 뷰에 정보들이 들어가있는 어댑터를 연결함
                    listView.setAdapter(mAdapter);
                } else {
                    //에러 상태 보려고 해둔 코드
                    int StatusCode = response.code();
                    Log.i(ApplicationController.TAG, "홈 상태 Code : " + StatusCode);
                }
            }

            @Override
            public void onFailure(Call<List<Busker>> call, Throwable t) {

            }
        });


        return v;
    }

    @Override
    public void onRefresh() {
        // 새로고침 코드
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(this).attach(this).commit();

        // 새로고침 완료
        mSwipeRefreshLayout.setRefreshing(false);
    }


    //리스트 아이템들(화면에 띄워지는 정보들을 세팅)
    public class RankListItem {
        private String team_name;
        private String busker_tag;
        private Bitmap busker_image;
        private int ranking;
        private int busker_id;

        public RankListItem(int ranking, String team_name, String busker_tag, Bitmap busker_image, int busker_id) {
            this.ranking = ranking;
            this.team_name = team_name;
            this.busker_tag = busker_tag;
            this.busker_image = busker_image;
            this.busker_id = busker_id;
        }
    }


    //Adapter
    public class RankingCustom extends BaseAdapter {

        private ArrayList<RankListItem> list;

        public RankingCustom(ArrayList<RankListItem> list) {
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public RankListItem getItem(int position) {
            //현재 position에 따른 list값 반환
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //부모뷰에 리스트로 세팅될 하나의 조각을 세팅하는 것
            LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View result = inflater.inflate(R.layout.activity_ranking_list_item, parent, false);

            TextView ranking = (TextView) result.findViewById(R.id.ranking);
            ImageView busker_image = (ImageView) result.findViewById(R.id.buskerImage);
            TextView team_name = (TextView) result.findViewById(R.id.buskerId);
            TextView busker_tag = (TextView) result.findViewById(R.id.buskerTag);

            ranking.setText(String.valueOf(getItem(position).ranking));
            int SDK_INT = android.os.Build.VERSION.SDK_INT;
            if (SDK_INT > 8) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);
                busker_image.setImageBitmap(getItem(position).busker_image);
                busker_image.setScaleType(ImageView.ScaleType.FIT_XY);
            }
            team_name.setText(getItem(position).team_name);
            busker_tag.setText(getItem(position).busker_tag);

            int busker_id = getItem(position).busker_id;
            String teamName = getItem(position).team_name;
            Bitmap buskerImage = getItem(position).busker_image;

            result.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // 터치 시 해당 아이템 채팅방 불러오기
                    retrofit2.Call<User> userDetail = apiService.getUserDetail(user_token, user_id);
                    userDetail.enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(retrofit2.Call<User> call, Response<User> response) {
                            if (response.isSuccessful()) {
                                User user = response.body();
                                if (user.getBusker() != null) { //유저가 버스커일 경우
                                    //내 채널이 아닐 경우, 팀네임이 같지않은 경우
                                    if (!user.getBusker().getTeam_name().equals(teamName)) {
                                        Intent buskerChannel = new Intent(getActivity(), ChannelUser.class);
                                        //개인 아이디를 다음 액티비티에서 받아 세팅
                                        buskerChannel.putExtra("busker_id", busker_id);
                                        buskerChannel.putExtra("team_name", teamName);
                                        buskerChannel.putExtra("busker_user_id", user_id);
                                        startActivity(buskerChannel);
                                    } //내 채널일 경우, 팀네임이 같을 경우
                                    else {
                                        Intent buskerChannel = new Intent(getActivity(), ChannelBusker.class);
                                        //개인 아이디를 다음 액티비티에서 받아 세팅
                                        buskerChannel.putExtra("busker_id", busker_id);
                                        buskerChannel.putExtra("team_name", teamName);
                                        buskerChannel.putExtra("busker_user_id", user_id);
                                        startActivity(buskerChannel);
                                    }
                                } //내 채널이 아닐 경우, 유저가 버스커가 아닐 경우 정상 진행
                                else {
                                    Intent buskerChannel = new Intent(getActivity(), ChannelUser.class);
                                    //개인 아이디를 다음 액티비티에서 받아 세팅
                                    buskerChannel.putExtra("busker_id", busker_id);
                                    buskerChannel.putExtra("busker_user_id", user_id);
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
            });

            return result;
        }
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
        } finally {
            if (connection != null) connection.disconnect();
        }
    }


    //user데이터 얻어오기
    private void getLocalData() {
        SharedPreferences pref = getActivity().getSharedPreferences("User", Activity.MODE_PRIVATE);
        user_token = pref.getString("auth_token", null);
        user_name = pref.getString("username", null);
        user_id = pref.getInt("user_id", 0);

    }

    //api연결
    private void restApiBuilder() {
        ApplicationController application = ApplicationController.getInstance();
        application.buildNetworkService();
        apiService = ApplicationController.getInstance().getRestApiService();
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
