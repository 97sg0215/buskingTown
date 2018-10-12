package graduationwork.buskingtown;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

public class BuskerCertification extends AppCompatActivity {

    private RestApiService apiService;

    private Uri mImageCaptureUri;

    final int REQ_CODE_SELECT_IMAGE=100;

    //활동팀명,태그갯수 체크 유효성 체크값 담음
    final boolean[] teamNameOk = new boolean[1];
    final boolean[] cellPhoneOk = new boolean[1];
    final boolean[] tagOk = new boolean[1];
    final boolean[] imageOk = new boolean[1];

    String user_token;
    String real_album_path;
    int user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_busker_certification);

        restApiBuilder();
        getLocalData();

        //runtime permission
        PermissionListener permissionListener= new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                Toast.makeText(BuskerCertification.this,"권한허가",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                Toast.makeText(BuskerCertification.this,"권한거부\n"+ deniedPermissions.toString(),Toast.LENGTH_SHORT).show();

            }

        };
        TedPermission.with(this)
                .setPermissionListener(permissionListener)
                .setRationaleMessage("사진에 접근하기위해서는 사진 접근 권한이 필요해요")
                .setDeniedMessage("접근을 거부 하셨군요 \n [설정]->[권한]에서 권한을 허용할 수 있어요.")
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .check();


        //활동팀명,성명,휴대폰,활동지 및 장르(=태그) 에디터 텍스트 입력 변수
        final EditText teamNameEdit = (EditText) findViewById(R.id.teamName);
        final EditText nameEdit = (EditText) findViewById(R.id.name);
        final EditText cellPhoneEdit = (EditText) findViewById(R.id.cellPhone);
        final EditText tagEdit = (EditText) findViewById(R.id.activity);

        //휴대폰 번호 입력 시 자동으로 하이픈 추가
        cellPhoneEdit.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

        ImageView imgChoice = (ImageView) findViewById(R.id.imageIcon);
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



        teamNameEdit.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                //활동팀명 입력 값 문자열화
                final String teamName = teamNameEdit.getText().toString();

                //이메일 체크 값 담음
                teamNameOk[0] = checkName(teamName);

                useConfirmBtn();

            }
        });

        nameEdit.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                useConfirmBtn();

            }
        });

        cellPhoneEdit.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                //버스커휴대폰번호 입력 값 문자열화
                final String buskerPhone = cellPhoneEdit.getText().toString();

                //휴대폰 체크 값 담음
                cellPhoneOk[0] = checkPhone(buskerPhone);

                useConfirmBtn();

            }
        });

        tagEdit.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {



                //태그 입력 값 문자열화
                final String buskerTag = tagEdit.getText().toString();

                //태그 체크 값 담음
                tagOk[0] = checktag(buskerTag);


                useConfirmBtn();

            }
        });

    }

    // 선택된 이미지 가져오기
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQ_CODE_SELECT_IMAGE){
            if(resultCode== Activity.RESULT_OK) {
                try {
                    //이미지 데이터를 비트맵으로 받아온다.
                    Bitmap image_bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());

                    //배치해놓은 ImageView에 set
                    ImageView imageS = (ImageView)findViewById(R.id.imageIcon);
                    imageS.setImageBitmap(image_bitmap);

                    mImageCaptureUri = data.getData();
                    Log.e("SmartWheel", mImageCaptureUri.getPath().toString());
                    real_album_path= getPath(mImageCaptureUri);
                    Log.e("real_album_path",real_album_path);

                    imageOk[0] = checkImage(real_album_path);

                }catch (FileNotFoundException e) { e.printStackTrace(); }
                catch (IOException e) { e.printStackTrace(); }
                catch (Exception e) { e.printStackTrace();	}
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


    //활동팀명 형식이 제대로 되어있나 체크 메소드
    public static boolean checkName(String buskerName){

        String regex =  "^[a-zA-Z가-힣0-9_]{1,15}$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(buskerName);
        boolean isNormal = m.matches();
        return isNormal;
    }

    //휴대폰번호 형식이 제대로 되어있나 체크 메소드
    public static boolean checkPhone(String phone){
        String regex =  "^01(?:0|1|[6-9])-(?:\\d{3}|\\d{4})-\\d{4}$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(phone);
        boolean isNormal = m.matches();
        return isNormal;
    }
    public boolean checktag(String buskerTag){
        int i = getCharNumber(buskerTag,'#');
        if(i <=5){
            int z = getCharNumber(buskerTag,' ');
            if(z<=4){
                return true;
            }
            Toast toast = Toast.makeText(this, "태그를 5개이하로 작성하세요.",Toast.LENGTH_SHORT);
            toast.show();
            return false;

        }else {
            return false;
        }

    }

    //이미지가 있는지 체크 메소드
    public static boolean checkImage(String img){
        if(img.equals(null)){
            return false;
        }else{
            return true;
        }

    }

    static int getCharNumber(String str, char c)
    {
        int count = 0;
        for(int i=0;i<str.length();i++)
        {
            if(str.charAt(i) == c){
                count++;
            }

        }
        return count;
    }



    public void useConfirmBtn(){
        //확인 버튼 변수
        final Button confirmBtn = (Button) findViewById(R.id.confirmBtn);

        //팀명, 휴대폰, 태그 형식 모두 맞으면 버튼 활성화
        if(teamNameOk[0]&&cellPhoneOk[0]&&tagOk[0]&&imageOk[0]){

            //색지정 할때 getApplicationContext().getResources().getColor(컬러이름)으로 해주세요.
            confirmBtn.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.mainPurple));
            //다음 로그인 버튼
            confirmBtn.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    final EditText teamNameEdit = (EditText) findViewById(R.id.teamName);
                    final String teamName = teamNameEdit.getText().toString();

                    final EditText nameEdit = (EditText) findViewById(R.id.name);
                    final String name = nameEdit.getText().toString();

                    final EditText cellPhoneEdit = (EditText) findViewById(R.id.cellPhone);
                    final String cellPhone = cellPhoneEdit.getText().toString();

                    final EditText tagEdit = (EditText) findViewById(R.id.activity);
                    final String tag = tagEdit.getText().toString();

                    buskerSetting(teamName,name,cellPhone,tag,real_album_path);
                }
            });
        }else {
            confirmBtn.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.subGray));
            confirmBtn.setOnClickListener(null);
        }

    }

    public void buskerSetting(String teamName,String name, String phone, String tag,String filePath){
        // add another part within the multipart request
        RequestBody user = RequestBody.create(MediaType.parse("multipart/form-data"),String.valueOf(user_id));
        RequestBody busker_type = RequestBody.create(MediaType.parse("multipart/form-data"),String.valueOf(1));
        RequestBody busker_name = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(name));
        RequestBody team_name = RequestBody.create(MediaType.parse("multipart/form-data"),String.valueOf(teamName));
        RequestBody busker_phone = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(phone));
        RequestBody busker_tag = RequestBody.create(MediaType.parse("multipart/form-data"),String.valueOf(tag));



        Log.e("아이디",String.valueOf(user_id));
        Log.e("활동팀명",String.valueOf(teamName));
        Log.e("이름",String.valueOf(name));
        Log.e("휴대폰번호",String.valueOf(phone));
        Log.e("태그",String.valueOf(tag));

        //이미지 업로드
        File file = new File(filePath);
        Log.e("파일경로",String.valueOf(filePath));
        Log.e("파일이름",String.valueOf(file.getName()));
        RequestBody surveyBody = RequestBody.create(MediaType.parse("image/*"), file);
        Log.e("이미지",String.valueOf(surveyBody.contentType()));
        MultipartBody.Part filePart = MultipartBody.Part.createFormData("busker_image", file.getName(), surveyBody);


        Call<Busker> postBusker = apiService.postBusker(user_token,user,busker_name,busker_type,team_name,busker_tag,busker_phone,filePart);
        postBusker.enqueue(new Callback<Busker>() {
            @Override
            public void onResponse(Call<Busker> call, Response<Busker> response) {
                if (response.isSuccessful()) {
                    Log.e("버스커세팅:", "성공");
                    Log.e("버스커이미지:", String .valueOf(response.body().getBusker_image()));
                    completeApply();
                } else {
                    Toast.makeText(getApplicationContext(), "버스커인증하기에 실패했습니다.\n다시 시도해주세요", Toast.LENGTH_SHORT).show();
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
                Log.i(ApplicationController.TAG, "버스커인증 서버 연결 실패 Message : " + t.getMessage());
                Toast.makeText(getApplicationContext(), "버스커인증하기에 실패했습니다.\n다시 시도해주세요", Toast.LENGTH_SHORT).show();
            }
        });
    }




    public void getLocalData(){
        SharedPreferences pref = getSharedPreferences("User", Activity.MODE_PRIVATE);
        user_token = pref.getString("auth_token",null);
        user_id = pref.getInt("user_id",0);
    }


    public void completeApply() {
        Intent waitPassActivity = new Intent(getApplication(),WaitPass.class);
        startActivity(waitPassActivity);
    }

    public void restApiBuilder() {
        ApplicationController application = ApplicationController.getInstance();
        application.buildNetworkService();
        apiService = ApplicationController.getInstance().getRestApiService();
    }
}
