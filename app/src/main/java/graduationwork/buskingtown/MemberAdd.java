package graduationwork.buskingtown;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import graduationwork.buskingtown.api.RestApiService;
import graduationwork.buskingtown.model.Busker;
import graduationwork.buskingtown.model.User;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MemberAdd extends AppCompatActivity {

    String user_token,user_name,user_image,team_name,tag,real_album_path;
    int user_id;

    RestApiService apiService;

    private List<String> list;          // 데이터를 넣은 리스트변수
    private ListView listView;          // 검색을 보여줄 리스트변수
    private EditText editSearch;        // 검색어를 입력할 Input 창
    private HashMap<String,Integer> userlist_id_and_name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_add);

        ImageButton backBtn = (ImageButton) findViewById(R.id.clearBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { MemberAdd.super.onBackPressed(); }
        });

        getLocalData();

        restApiBuilder();

        editSearch = (EditText) findViewById(R.id.memberSearch);

        // 리스트를 생성한다.
        list = new ArrayList<String>();

        userlist_id_and_name = new HashMap<>();

        Call<List<User>> user_list = apiService.userList(user_token);
        user_list.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if(response.isSuccessful()){
                    List<User> users = response.body();
                    for(int i=0;i<users.size();i++){
                        if(users.get(i).getBusker()==null){
                            String user_name = users.get(i).getUsername();
                            int user_id = users.get(i).getId();
                            userlist_id_and_name.put(user_name,user_id);
                        }
                    }
                    editSearch.setOnKeyListener(new View.OnKeyListener() {
                        @Override
                        public boolean onKey(View v, int keyCode, KeyEvent event) {
                            //버튼을 눌렀을때
                            if(event.getAction() == KeyEvent.ACTION_DOWN){

                            }
                            //버튼을 뗄때,
                            else if(event.getAction() == KeyEvent.ACTION_UP){

                            }
                            //엔터키
                            if(keyCode == KeyEvent.KEYCODE_ENTER){
                                String text = editSearch.getText().toString();
                                search(userlist_id_and_name,text);
                                return true;
                            }
                            return false;
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
            public void onFailure(Call<List<User>> call, Throwable t) {
                Log.i(ApplicationController.TAG, "유저 리스트 서버 연결 실패 Message : " + t.getMessage());
            }
        });





    }

    public void previousActivity(View v){
        onBackPressed();
    }

    // 검색을 수행하는 메소드
    public void search(HashMap memberlist,String charText) {
        RelativeLayout noneId = (RelativeLayout) findViewById(R.id.noneId) ;
        RelativeLayout beingId = (RelativeLayout) findViewById(R.id.beingMember) ;

        Log.e("리스트",String.valueOf(memberlist));
        Log.e("검색된 내용",String.valueOf(charText));
        // 리스트의 모든 데이터를 검색한다.
        for(int i = 0;i < memberlist.size(); i++)
        {
            // arraylist의 모든 데이터에 입력받은 단어(charText)가 포함되어 있으면 true를 반환한다.
            if (memberlist.containsKey(charText)) {
                // 검색된 데이터를 리스트에 추가한다.
                Log.e("검색결과","존재하는 회원");
                noneId.setVisibility(View.GONE);

                //검색된 회원 세팅
                ImageView searchImg = (ImageView) findViewById(R.id.buskerProfileAdd);
                TextView searchName = (TextView) findViewById(R.id.buskerIdAdd);
                Button addmember = (Button) findViewById(R.id.addMember);

                //해쉬맵으로 가져온 객체형 유저 아이디를 인티저로 반환하여 url파라미터 값으로 넘겨주어 데이터를 가져옴
                Integer wrapper_userid = (Integer)memberlist.get(charText);
                int user_id = wrapper_userid.intValue();

                Call<User> userCall = apiService.getUserDetail(user_token,user_id);
                userCall.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        if(response.isSuccessful()){
                            User userDetail = response.body();
                            String userName = userDetail.getUsername();
                            String search_userImg = userDetail.getProfile().getUser_image();
                            String userPhone = userDetail.getProfile().getUser_phone();

                            Picasso.with(getApplicationContext()).load(search_userImg).transform(new CircleTransForm()).into(searchImg);
                            searchName.setText(userName);

                            beingId.setVisibility(View.VISIBLE);

                            addmember.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    addBusker(team_name,userName,userPhone,tag,user_id);
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
                    public void onFailure(Call<User> call, Throwable t) {
                        Log.i(ApplicationController.TAG, "유저 디테일 서버 연결 실패 Message : " + t.getMessage());
                    }
                });



                break;
            } else {
                Log.e("검색결과","없는 회원");
                noneId.setVisibility(View.VISIBLE);
                beingId.setVisibility(View.GONE);
            }

        }
    }

    public void addBusker(String teamname, String buskername, String buskerphone, String tag, int user_id){
        // add another part within the multipart request
        RequestBody user = RequestBody.create(MediaType.parse("multipart/form-data"),String.valueOf(user_id));
        RequestBody busker_type = RequestBody.create(MediaType.parse("multipart/form-data"),String.valueOf(2));
        RequestBody busker_name = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(buskername));
        RequestBody team_name = RequestBody.create(MediaType.parse("multipart/form-data"),String.valueOf(teamname));
        RequestBody busker_phone = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(buskerphone));
        RequestBody busker_tag = RequestBody.create(MediaType.parse("multipart/form-data"),String.valueOf(tag));


        Call<Busker> postBusker = apiService.postBusker(user_token,user,busker_name,busker_type,team_name,busker_tag,busker_phone,null);
        postBusker.enqueue(new Callback<Busker>() {
            @Override
            public void onResponse(Call<Busker> call, Response<Busker> response) {
                if (response.isSuccessful()) {
                    Log.e("버스커 멤버 추가:", "성공");
                    Intent team = new Intent(getApplication(), TeamMember.class);
                    team.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(team);
                } else {
                    Toast.makeText(getApplicationContext(), "새로운 멤버를 초대하는데 실패했습니다.\n다시 시도해주세요", Toast.LENGTH_SHORT).show();
                    int StatusCode = response.code();
                    Log.i(ApplicationController.TAG, "상태 Code : " + StatusCode);
                    Log.e("메세지", String.valueOf(response.message()));
                    Log.e("리스폰스에러바디", String.valueOf(response.errorBody()));
                    Log.e("리스폰스바디", String.valueOf(response.body()));
                }
            }

            @Override
            public void onFailure(Call<Busker> call, Throwable t) {
                Log.e("call","실패");
                Log.i(ApplicationController.TAG, "버스커멤버추가 서버 연결 실패 Message : " + t.getMessage());
                Toast.makeText(getApplicationContext(), "새로운 멤버를 초대하는데 실패했습니다.\n다시 시도해주세요", Toast.LENGTH_SHORT).show();
            }
        });
    }


    // 저장 되어 있는 busker 불러오기
    public void getLocalData(){
        SharedPreferences pref = getSharedPreferences("User", Activity.MODE_PRIVATE);
        SharedPreferences busker_pref = getSharedPreferences("BuskerUser", Activity.MODE_PRIVATE);
        user_token = pref.getString("auth_token",null);
        user_name = pref.getString("username",null);
        user_id = pref.getInt("user_id",0);

        team_name = busker_pref.getString("team_name",null);
        tag = busker_pref.getString("tag",null);
    }

    public void restApiBuilder() {
        ApplicationController application = ApplicationController.getInstance();
        application.buildNetworkService();
        apiService = ApplicationController.getInstance().getRestApiService();
    }

}
