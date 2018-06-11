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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BuskerCertification extends AppCompatActivity {

    final int REQ_CODE_SELECT_IMAGE=100;


    //활동팀명,태그갯수 체크 유효성 체크값 담음
    final boolean[] teamNameOk = new boolean[1];
    final boolean[] cellPhoneOk = new boolean[1];
    final boolean[] tagOk = new boolean[1];
    final boolean[] imageOk = new boolean[1];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_busker_certification);

        //활동팀명,성명,휴대폰,활동지 및 장르(=태그) 에디터 텍스트 입력 변수
        final EditText teamNameEdit = (EditText) findViewById(R.id.teamName);
        final EditText nameEdit = (EditText) findViewById(R.id.name);
        final EditText cellPhoneEdit = (EditText) findViewById(R.id.cellPhone);
        final EditText tagEdit = (EditText) findViewById(R.id.activity);
//
//        //로그인 액티비티에서 얻어옵니다
//        SharedPreferences pref = getSharedPreferences("User", Activity.MODE_PRIVATE);
//        String user_phone = pref.getString("user_phone",null);
//        Log.e("핸드폰 번호",user_phone);
//
//        cellPhoneEdit.setText(user_phone);

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

                    String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/SmartWheel" + System.currentTimeMillis() + ".jpg";
                    Log.e("filepath",filePath);

                    imageOk[0] = checkImage(filePath);

                }catch (FileNotFoundException e) { e.printStackTrace(); }
                catch (IOException e) { e.printStackTrace(); }
                catch (Exception e) { e.printStackTrace();	}
            }
        }
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
                    completeApply();
                }
            });
        }else {
            confirmBtn.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.subGray));
            confirmBtn.setOnClickListener(null);
        }

    }

    public void completeApply() {
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


        startActivity(waitPassActivity);
    }


}
