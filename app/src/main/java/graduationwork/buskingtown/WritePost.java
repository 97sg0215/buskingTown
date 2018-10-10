package graduationwork.buskingtown;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import graduationwork.buskingtown.api.RestApiService;
import graduationwork.buskingtown.model.Busker;
import graduationwork.buskingtown.model.Post;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WritePost extends AppCompatActivity {

    private RestApiService apiService;

    private Uri mImageCaptureUri;

    final int REQ_CODE_SELECT_IMAGE=100;

    String user_token, user_name, team_name;
    String real_album_path;
    int busker_id;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_post);

        restApiBuilder();
        getLocalData();


        final EditText postEdit = (EditText) findViewById(R.id.writePost);

        //ImageView image = (ImageView) findViewById(R.id.image);
        final TextView imgChoice = (TextView) findViewById(R.id.imgChoice);
        imgChoice.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //갤러리 열기
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
                intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQ_CODE_SELECT_IMAGE);

            }
        });

        ImageButton backBtn = (ImageButton) findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { WritePost.super.onBackPressed(); }
        });

        useConfirmBtn();
    }

    // 선택된 이미지 가져오기
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQ_CODE_SELECT_IMAGE){
            if(resultCode== Activity.RESULT_OK) {
                try {
                    //이미지 데이터를 비트맵으로 받아온다.
                    Bitmap image_bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());

                    //배치해놓은 ImageView에 set
                    ImageView imageS = (ImageView)findViewById(R.id.image);
                    imageS.setVisibility(View.VISIBLE);
                    imageS.setImageBitmap(image_bitmap);

                    mImageCaptureUri = data.getData();
                    Log.e("SmartWheel", mImageCaptureUri.getPath().toString());
                    real_album_path= getPath(mImageCaptureUri);
                    Log.e("real_album_path",real_album_path);


                }catch (FileNotFoundException e) { e.printStackTrace(); }
                catch (IOException e) { e.printStackTrace(); }
                catch (Exception e) { e.printStackTrace();   }
            }
        }
    }

    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        startManagingCursor(cursor);
        int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(columnIndex);
    }

    public void useConfirmBtn() {
        //확인 버튼 변수
        final Button completeBtn = (Button) findViewById(R.id.complete);

        //다음 로그인 버튼
        completeBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                final EditText contentEdit = (EditText) findViewById(R.id.writePost);
                final String postContent = contentEdit.getText().toString();


                postSetting(real_album_path, postContent);
            }
        });
    }

    public void postSetting(String filePath, String contents) {
        RequestBody busker = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(busker_id));
        RequestBody content = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(contents));
        //RequestBody created_at = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(date));

        Log.e("팀네임",String.valueOf(team_name));
        Log.e("게시물내용",String.valueOf(contents));
        Log.e("유저토큰",String.valueOf(user_token));

        MultipartBody.Part filePart;

        if(filePath!=null) {
            //이미지 업로드
            File file = new File(filePath);
            Log.e("파일경로", String.valueOf(filePath));
            Log.e("파일이름", String.valueOf(file.getName()));
            RequestBody surveyBody = RequestBody.create(MediaType.parse("image/*"), file);
            Log.e("이미지", String.valueOf(surveyBody.contentType()));
            filePart = MultipartBody.Part.createFormData("post_image", file.getName(), surveyBody);
        }else {
            filePart = null;
        }
        Call<Post> postUpload = apiService.postUpload(user_token, busker, filePart, content);
        postUpload.enqueue(new Callback<Post>() {

            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                if (response.isSuccessful()) {
                    Log.e("게시물세팅:", "성공");
                    Log.e("게시물이미지:", String .valueOf(response.body().getPost_image()));
                    completeApply();
                } else {
                    Toast.makeText(getApplicationContext(), "게시물 업로드를 실패했습니다.\n다시 시도해주세요", Toast.LENGTH_SHORT).show();
                    int StatusCode = response.code();
                    Log.i(ApplicationController.TAG, "상태 Code : " + StatusCode);
                    Log.e("메세지", String.valueOf(response.message()));
                    Log.e("리스폰스에러바디", String.valueOf(response.errorBody()));
                    Log.e("리스폰스바디", String.valueOf(response.body()));
                }
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                Log.e("call","실패");
                Log.i(ApplicationController.TAG, "게시물 서버 연결 실패 Message : " + t.getMessage());
                Toast.makeText(getApplicationContext(), "게시물업로드에 실패했습니다.\n다시 시도해주세요", Toast.LENGTH_SHORT).show();

            }
        });


    }
    public void getLocalData(){
        SharedPreferences pref = getSharedPreferences("User", Activity.MODE_PRIVATE);
        SharedPreferences busker_pref = getSharedPreferences("BuskerUser", Activity.MODE_PRIVATE);
        user_token = pref.getString("auth_token",null);
        user_name = pref.getString("username",null);
        busker_id = busker_pref.getInt("busker_id",0);
        team_name = busker_pref.getString("team_name",null);
    }

    public void completeApply() {
        Intent ChannelBusker = new Intent(getApplication(),ChannelBusker.class);
        startActivity(ChannelBusker);
    }

    public void restApiBuilder() {
        ApplicationController application = ApplicationController.getInstance();
        application.buildNetworkService();
        apiService = ApplicationController.getInstance().getRestApiService();
    }


    //백버튼 메소드
    public void previousActivity(View v){
        onBackPressed();
    }

}