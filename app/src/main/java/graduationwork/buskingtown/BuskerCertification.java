package graduationwork.buskingtown;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
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
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import graduationwork.buskingtown.api.RestApiService;
import graduationwork.buskingtown.model.Busker;
import graduationwork.buskingtown.model.User;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.provider.ContactsContract.CommonDataKinds.Website.URL;
import static okhttp3.MediaType.parse;

public class BuskerCertification extends AppCompatActivity {

    private RestApiService apiService;

    final int REQ_CODE_SELECT_IMAGE=100;
    private static final int INTENT_REQUEST_CODE = 100;

    //활동팀명,태그갯수 체크 유효성 체크값 담음
    final boolean[] teamNameOk = new boolean[1];
    final boolean[] cellPhoneOk = new boolean[1];
    final boolean[] tagOk = new boolean[1];
    final boolean[] imageOk = new boolean[1];

    String user_token, filePath;
    int user_id;
    Uri imgUri ;

    byte[] imageBytes = new byte[0] ;

    String mImageUrl = "";

    InputStream is ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_busker_certification);

        restApiBuilder();
        getLocalData();

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


                startActivityForResult(intent, INTENT_REQUEST_CODE);

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
        if(requestCode == INTENT_REQUEST_CODE){
            if(resultCode== Activity.RESULT_OK) {
                try {
                    //Uri에서 이미지 이름을 얻어온다.
                    String name_Str = getImageNameToUri(data.getData());
                    Log.e("imageName",String.valueOf(name_Str));

                    imgUri = data.getData();

                    //이미지 데이터를 비트맵으로 받아온다.
                    Bitmap image_bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());

                    //배치해놓은 ImageView에 set
                    final ImageView imageS = (ImageView)findViewById(R.id.imageIcon);
                    imageS.setImageBitmap(image_bitmap);


                    //imageOk[0] = checkImage(filePath);

                    //이미지 업로드 스트림
                    is = getContentResolver().openInputStream(data.getData());
                    imageBytes = getBytes(is);

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



    public String getImageNameToUri(Uri data) {
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(data, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

        cursor.moveToFirst();

        String imgPath = cursor.getString(column_index);
        String imgName = imgPath.substring(imgPath.lastIndexOf("/")+1);

        return imgName;
    }

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



    public static String BitmapToString (Bitmap bitmap) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream ();
            bitmap.compress (Bitmap.CompressFormat.PNG, 100, baos);
            byte [] b = baos.toByteArray ();
            String temp = Base64.encodeToString (b, Base64.DEFAULT);
            return temp;
        } catch (NullPointerException e) {
            return null;
        } catch (OutOfMemoryError e) {
            return null;
        }
    }

    public static Bitmap StringToBitmap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (NullPointerException e) {
            e.getMessage();
            return null;
        } catch (OutOfMemoryError e) {
            return null;
        }
    }

    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }



    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    private String getRealPathFromURI(Uri contentURI) {
        String filePath;
        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { filePath = contentURI.getPath(); }
        else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            filePath = cursor.getString(idx); cursor.close(); }
            return filePath;
    }


    public void useConfirmBtn(){
        //확인 버튼 변수
        final Button confirmBtn = (Button) findViewById(R.id.confirmBtn);

        //팀명, 휴대폰, 태그 형식 모두 맞으면 버튼 활성화
        if(teamNameOk[0]&&cellPhoneOk[0]&&tagOk[0]){

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

                    //final ImageView imageS = (ImageView)findViewById(R.id.imageIcon);
                    //Bitmap bitmap = ((BitmapDrawable)imageS.getDrawable()).getBitmap();
                    //final String filePath = BitmapToString(bitmap);

                    //String filePath = getRealPathFromURI(uri);

                    byte[] imageBytes = new byte[0];
                    try {
                        imageBytes = getBytes(is);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                    //아이디, 휴대폰번호 로그 띄우기
                    Log.e("활동팀명",String.valueOf(teamName));
                    Log.e("이름",String.valueOf(name));
                    Log.e("휴대폰번호",String.valueOf(cellPhone));
                    Log.e("태그",String.valueOf(tag));
                    Log.e("image",String.valueOf(imageBytes));


                    buskerSetting(teamName,name,cellPhone,tag,imageBytes);
                }
            });
        }else {
            confirmBtn.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.subGray));
            confirmBtn.setOnClickListener(null);
        }

    }

    public void buskerSetting(String teamName,String name, String phone, String tag, byte[] imageBytes){
        // add another part within the multipart request
        RequestBody user = RequestBody.create(parse("multipart/form-data"),String.valueOf(user_id));
        RequestBody busker_name = RequestBody.create(parse("multipart/form-data"), String.valueOf(name));
        RequestBody team_name = RequestBody.create(parse("multipart/form-data"),String.valueOf(teamName));
        RequestBody busker_phone = RequestBody.create(parse("multipart/form-data"), String.valueOf(phone));
        RequestBody busker_tag = RequestBody.create(parse("multipart/form-data"),String.valueOf(tag));

        // create upload service client


        //File file = new File(String.valueOf(imageBytes));
        //RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        //MultipartBody.Part busker_image = MultipartBody.Part.createFormData("busker_image", file.getName(), requestFile);

        //RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);
        //MultipartBody.Part busker_image = MultipartBody.Part.createFormData("upload", file.getName(), reqFile);

        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), imageBytes);

        MultipartBody.Part busker_image = MultipartBody.Part.createFormData("image","image.jpg", requestFile);

        Call<Busker> postBusker = apiService.postBusker(user_token,user,busker_name,team_name,busker_tag,busker_phone,busker_image);
        postBusker.enqueue(new Callback<Busker>() {
            @Override
            public void onResponse(Call<Busker> call, Response<Busker> response) {
                if (response.isSuccessful()) {
                    Busker responseBody = response.body();
                    mImageUrl = responseBody.getPath();
                    Log.e("버스커세팅:", "성공");
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
                Log.i(ApplicationController.TAG, "유저 정보 서버 연결 실패 Message : " + t.getMessage());
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
