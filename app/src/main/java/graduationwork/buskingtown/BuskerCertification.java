package graduationwork.buskingtown;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Environment;
import android.provider.MediaStore;
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

import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import graduationwork.buskingtown.api.RestApiService;
import graduationwork.buskingtown.model.Busker;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.google.gson.internal.bind.TypeAdapters.URL;

public class BuskerCertification extends AppCompatActivity {

    private RestApiService apiService;

    final int REQ_CODE_SELECT_IMAGE=100;

    private String mImageUrl = "";

    //활동팀명,태그갯수 체크 유효성 체크값 담음
    final boolean[] teamNameOk = new boolean[1];
    final boolean[] cellPhoneOk = new boolean[1];
    final boolean[] tagOk = new boolean[1];
    final boolean[] imageOk = new boolean[1];

    //이전 사용자 변수
    int user_id;
    String user_token,user_phone, filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_busker_certification);

        restApiBuilder();

        //활동팀명,성명,휴대폰,활동지 및 장르(=태그) 에디터 텍스트 입력 변수
        final EditText teamNameEdit = (EditText) findViewById(R.id.teamName);
        final EditText nameEdit = (EditText) findViewById(R.id.name);
        final EditText cellPhoneEdit = (EditText) findViewById(R.id.cellPhone);
        final EditText tagEdit = (EditText) findViewById(R.id.activity);

        getLocalData();

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

                useConfirmBtn();
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
                    //Uri에서 이미지 이름을 얻어온다.
                    //String name_Str = getImageNameToUri(data.getData());

                    //이미지 데이터를 비트맵으로 받아온다.
                    Bitmap image_bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());

                    //배치해놓은 ImageView에 set
                    ImageView imageS = (ImageView)findViewById(R.id.imageIcon);
                    imageS.setImageBitmap(image_bitmap);

                    filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/SmartWheel" + System.currentTimeMillis() + ".jpg";
                    Log.e("filepath",filePath);

                    imageOk[0] = checkImage(filePath);

                    //이미지 업로드 스트림
                    InputStream is = getContentResolver().openInputStream(data.getData());


                }catch (FileNotFoundException e) { e.printStackTrace(); }
                catch (IOException e) { e.printStackTrace(); }
                catch (Exception e) { e.printStackTrace();	}
            }
        }
    }

    public byte[] getBytes(InputStream is) throws IOException {
        ByteArrayOutputStream byteBuff = new ByteArrayOutputStream();

        int buffSize = 1024;
        byte[] buff = new byte[buffSize];

        int len = 0;
        while ((len = is.read(buff)) != -1) {
            byteBuff.write(buff, 0, len);
        }

        return byteBuff.toByteArray();
    }


//    private void uploadImage(byte[] imageBytes) {
//
//        RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), imageBytes);
//
//        MultipartBody.Part body = MultipartBody.Part.createFormData("image", "image.jpg", requestFile);
//        Call<graduationwork.buskingtown.model.Response> call = apiService.uploadImage(body);
//        call.enqueue(new Callback<graduationwork.buskingtown.model.Response>() {
//            @Override
//            public void onResponse(Call<graduationwork.buskingtown.model.Response> call, Response<graduationwork.buskingtown.model.Response> response) {
//
//                if (response.isSuccessful()) {
//                    graduationwork.buskingtown.model.Response responseBody = response.body();
//                    mImageUrl = URL + responseBody.getPath();
//                } else {
//                    ResponseBody errorBody = response.errorBody();
//                    Gson gson = new Gson();
//                    try {
//                        Response errorResponse = gson.fromJson(errorBody.string(), Response.class);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<graduationwork.buskingtown.model.Response> call, Throwable t) {
//                Log.i(ApplicationController.TAG, "이미지 업로드 실패 Message : " + t.getMessage());
//            }
//
//        });
//    }

    //활동팀명 형식이 제대로 되어있나 체크 메소드
    public static boolean checkName(String buskerName){

        String regex =  "^[a-zA-Z가-힣0-9_]{4,15}$";
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
        if(i>0 && i <=5){
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
                    completeApply(user_id);
                }
            });
        }else {
            confirmBtn.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.subGray));
            confirmBtn.setOnClickListener(null);
        }

    }

    public void completeApply(int id) {
        Intent waitPassActivity = new Intent(getApplication(),WaitPass.class);

        final EditText teamNameEdit = (EditText) findViewById(R.id.teamName);
        final String teamName = teamNameEdit.getText().toString();

        final EditText nameEdit = (EditText) findViewById(R.id.name);
        final String name = nameEdit.getText().toString();

        final EditText cellPhoneEdit = (EditText) findViewById(R.id.cellPhone);
        final String cellPhone = cellPhoneEdit.getText().toString();

        final EditText tagEdit = (EditText) findViewById(R.id.activity);
        final String tag = tagEdit.getText().toString();

        //아이디, 휴대폰번호 로그 띄우기
        Log.e("활동팀명",String.valueOf(teamName));
        Log.e("이름",String.valueOf(name));
        Log.e("휴대폰번호",String.valueOf(cellPhone));
        Log.e("태그",String.valueOf(tag));

        buskerSetting(id, teamName, name, cellPhone, tag, filePath);

        startActivity(waitPassActivity);
    }

    public void buskerSetting(int id,String teamName,String name, String phone, String tag,String filePath){
        Busker busker = new Busker();
//        busker.setUser(id);
        busker.setBusker_name(name);
        busker.setTeam_name(teamName);
        busker.setBusker_phone(phone);
        busker.setBusker_tag(tag);
        busker.setCertification(false);

      //  RequestBody user = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(id));
        RequestBody busker_name = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(name));
        RequestBody team_name = RequestBody.create(MediaType.parse("text/plain"),String.valueOf(teamName));
        RequestBody busker_phone = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(phone));
        RequestBody busker_tag = RequestBody.create(MediaType.parse("text/plain"),String.valueOf(tag));

//       RequestBody busker = RequestBody.create(MediaType.parse("text/plain"),String.valueOf(tag));
        RequestBody requestFile = null;
//
//        MultipartBody.Part busker_name_per = MultipartBody.Part.create(busker_name);
//        MultipartBody.Part team_name_per = MultipartBody.Part.create(team_name);
//        MultipartBody.Part busker_phone_per = MultipartBody.Part.create(busker_phone);
//        MultipartBody.Part busker_tag_per = MultipartBody.Part.create(busker_tag);
//        MultipartBody.Part certification_per = MultipartBody.Part.create(certification);

        MultipartBody.Part imagenPerfil = null;
        if(filePath!=null){
            File file = new File(filePath);
            Log.i("Register","Nombre del archivo "+file.getName());
            // create RequestBody instance from file
            requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            // MultipartBody.Part is used to send also the actual file name
            imagenPerfil = MultipartBody.Part.createFormData("imagenPerfil", file.getName(), requestFile);
        }

        Call<Busker> buskerCall = apiService.postBusker(user_token,busker);
        buskerCall.enqueue(new Callback<Busker>() {
            @Override
            public void onResponse(Call<Busker> call, Response<Busker> response) {
                if (response.isSuccessful()) {
                    Log.e("버스커인증생성:", "성공");
                } else {
                    int StatusCode = response.code();
                    String s = response.message();
                    ResponseBody d = response.errorBody();
                    Busker a = response.body();
                    Log.i(ApplicationController.TAG, "상태 Code : " + StatusCode);
                    Log.e("메세지", s);
                    Log.e("리스폰스에러바디", String.valueOf(d));
                    Log.e("리스폰스바디", String.valueOf(a));
                }
            }

            @Override
            public void onFailure(Call<Busker> call, Throwable t) {
                Log.e("버스커 세팅 메세지", "통신연결 실패");
            }
        });
    }

    public void getLocalData(){
        SharedPreferences pref = getSharedPreferences("User", Activity.MODE_PRIVATE);
        user_token = pref.getString("auth_token",null);
        user_phone = pref.getString("user_phone",null);
        user_id = pref.getInt("user_id",0);
    }

    public void restApiBuilder() {
        ApplicationController application = ApplicationController.getInstance();
        application.buildNetworkService();
        apiService = ApplicationController.getInstance().getRestApiService();
    }
}
