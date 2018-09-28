package graduationwork.buskingtown;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.tsongkha.spinnerdatepicker.DatePicker;
import com.tsongkha.spinnerdatepicker.DatePickerDialog;
import com.tsongkha.spinnerdatepicker.SpinnerDatePickerDialogBuilder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import graduationwork.buskingtown.api.RestApiService;
import graduationwork.buskingtown.model.LendLocation;
import graduationwork.buskingtown.model.LendLocationOption;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LocationLend extends AppCompatActivity {

    private RestApiService apiService;

    private Uri mImageCaptureUri;

    final int REQ_CODE_SELECT_IMAGE=100;

    String user_token;
    String real_album_path;
    int user_id;

    int mHour, mMinute;

    Button concertSD, concertED, timeSD, timeED, confirmBtn;
    int concertStartYear, concertStartMonth, concertStartDay , concertEndYear, concertEndMonth, concertEndDay;
    SimpleDateFormat simpleDateFormat;

    //정보 입력
    ImageView location_image;
    RadioButton rb, practice_room, concert_room;
    EditText provider_phone, option_name, option_price, location_info, provide_rule, refund_rule, provide_option,provide_option_price;
    LinearLayout optionContainer;
    View optionList;

    ArrayList<String> option_name_list = new ArrayList<>();
    ArrayList<String> option_price_list = new ArrayList<>();


    ArrayList<View> optionListChild = new ArrayList<>();


    ArrayList<EditText> nameList = new ArrayList<>();
    ArrayList<EditText> priceList = new ArrayList<>();

    String option_names,option_prices;

    String p_type, p_phone, o_name, o_price, p_info, p_rule, p_refund_rule, p_start_time, p_end_time, p_start_date, p_end_date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_lend);

        restApiBuilder();
        getLocalData();

        ImageButton backBtn = (ImageButton) findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { LocationLend.super.onBackPressed(); }
        });

        //시간을 가져오기위한 Calendar 인스턴스 선언
        Calendar cal = new GregorianCalendar();
        mHour = cal.get(Calendar.HOUR_OF_DAY);
        mMinute = cal.get(Calendar.MINUTE);

        //달력개체 불러옴
        final GregorianCalendar startCalendar = new GregorianCalendar();
        concertStartYear = startCalendar.get(Calendar.YEAR);
        concertStartMonth = startCalendar.get(Calendar.MONTH);
        concertStartDay = startCalendar.get(Calendar.DAY_OF_MONTH);

        final GregorianCalendar endCalendar = new GregorianCalendar();
        concertEndYear = endCalendar.get(Calendar.YEAR);
        concertEndMonth = endCalendar.get(Calendar.MONTH);
        concertEndDay = endCalendar.get(Calendar.DAY_OF_MONTH);

        //날짜형태
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);

        //데이트 피커
        concertSD = (Button) findViewById(R.id.concertSD);
        concertSD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatePickerDialog.OnDateSetListener dateStartPicker = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        concertSD.setText(year + "년 " + (monthOfYear + 1) + "월 " + dayOfMonth + "일");
                        p_start_date = String.valueOf(simpleDateFormat.format(startCalendar.getTime()));
                    }
                };

                new SpinnerDatePickerDialogBuilder()
                        .callback(dateStartPicker)
                        .context(LocationLend.this)
                        .spinnerTheme(R.style.DatePickerSpinner)
                        .showTitle(true)
                        .defaultDate(concertStartYear,concertStartMonth,concertStartDay)
                        .build()
                        .show();
            }
        });


        concertED = (Button) findViewById(R.id.concertED);
        concertED.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog.OnDateSetListener dateEndPicker = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        concertED.setText(year + "년 " + (monthOfYear + 1) + "월 " + dayOfMonth + "일");
                        p_end_date = String.valueOf(simpleDateFormat.format(endCalendar.getTime()));
                    }
                };

                new SpinnerDatePickerDialogBuilder()
                        .callback(dateEndPicker)
                        .context(LocationLend.this)
                        .spinnerTheme(R.style.DatePickerSpinner)
                        .showTitle(true)
                        .defaultDate(concertEndYear,concertEndMonth,concertEndDay)
                        .build()
                        .show();
            }
        });

        //수경이 코드
        location_image = (ImageView) findViewById(R.id.imageChoice);
        location_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //갤러리 열기
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
                intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQ_CODE_SELECT_IMAGE);
            }
        });



     //   provider_phone, option_name, option_price;
        provider_phone = (EditText) findViewById(R.id.provider_phone);
        option_name = (EditText) findViewById(R.id.provide_option);
        option_price = (EditText) findViewById(R.id.option_price);

        provider_phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                p_phone = provider_phone.getText().toString();
            }
        });

        option_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                o_name = option_name.getText().toString();
            }
        });

        option_price.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                o_price = option_price.getText().toString();
            }
        });

        //location_info, provide_rule, refund_rule;
        location_info = (EditText) findViewById(R.id.locationInfo);
        provide_rule = (EditText) findViewById(R.id.privde_rule);
        refund_rule = (EditText) findViewById(R.id.refund_rule);
        location_info.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                p_info = location_info.getText().toString();
            }
        });

        provide_rule.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                p_rule = provide_rule.getText().toString();
            }
        });

        refund_rule.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                p_refund_rule = refund_rule.getText().toString();
            }
        });

        //옵션 추가시
        LinearLayout addOption = (LinearLayout) findViewById(R.id.add_option);
        addOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                optionContainer = (LinearLayout) findViewById(R.id.option_container);
                optionList = getLayoutInflater().inflate(R.layout.option_list,optionContainer,false);
                if(optionList.getParent()!= null)
                    ((ViewGroup)optionList.getParent()).removeView(optionList);
                optionContainer.addView(optionList);

            }

        });


        confirmBtn = (Button) findViewById(R.id.confirmBtn);

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final RadioGroup rg = (RadioGroup) findViewById(R.id.radioGroup);
                int radio_id = rg.getCheckedRadioButtonId();
                //getCheckedRadioButtonId() 의 리턴값은 선택된 RadioButton 의 id 값.
                rb = (RadioButton) findViewById(radio_id);
                practice_room = (RadioButton) findViewById(R.id.radioPracticeRoom);
                concert_room = (RadioButton) findViewById(R.id.radioConcert);


                if (rb.equals(practice_room)) {
                    p_type = "연습실";
                } else if (rb.equals(concert_room)) {
                    p_type = "콘서트";
                }

                if (optionContainer != null) {
                   for (int i = 0; i < optionContainer.getChildCount(); i++) {
                        optionListChild.add(optionContainer.getChildAt(i));

                        nameList.add(optionListChild.get(i).findViewById(R.id.provide_option));
                        priceList.add(optionListChild.get(i).findViewById(R.id.option_price));
                        option_name_list.add(nameList.get(i).getText().toString());
                        option_price_list.add(priceList.get(i).getText().toString());

                    }
                }


                Log.e("이름", String.valueOf(option_name_list));
                Log.e("가격", String.valueOf(option_price_list));

                if(option_name_list.size()==0&&option_price_list.size()==0){
                    rent(real_album_path,p_type,p_phone,p_start_date,p_end_date,p_start_time,p_end_time,null,p_info,p_rule,p_refund_rule,o_name,o_price,null,null);
                }else {
                    rent(real_album_path,p_type,p_phone,p_start_date,p_end_date,p_start_time,p_end_time,null,p_info,p_rule,p_refund_rule,o_name,o_price,option_name_list,option_price_list);
                }



            }
        });
    }

    public void sOnClick(View v){
        switch (v.getId()){
            //시간 대화상자 버튼이 눌리면 대화상자를 보여줌

            case R.id.timeSD:
                //여기서 리스너도 등록함
                new TimePickerDialog(LocationLend.this, R.style.TimePicker, startTimeSetListener, mHour, mMinute, false).show();

                break;

            case R.id.timeED:
                //여기서 리스너도 등록함
                new TimePickerDialog(LocationLend.this, R.style.TimePicker, endTimeSetListener, mHour, mMinute, false).show();

                break;
        }
    }

    TimePickerDialog.OnTimeSetListener startTimeSetListener =

            new TimePickerDialog.OnTimeSetListener() {

                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    //사용자가 입력한 값을 가져온뒤
                    mHour = hourOfDay;
                    mMinute = minute;

                    //텍스트뷰의 값을 업데이트함
                    Button startD = (Button)findViewById(R.id.timeSD);
                    startD.setText(String.format("%d:%d", mHour, mMinute));
                    p_start_time = String.format("%d:%d:00", mHour, mMinute);
                }
            };

    TimePickerDialog.OnTimeSetListener endTimeSetListener =

            new TimePickerDialog.OnTimeSetListener() {

                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    //사용자가 입력한 값을 가져온뒤
                    mHour = hourOfDay;
                    mMinute = minute;

                    //텍스트뷰의 값을 업데이트함
                    Button endD = (Button)findViewById(R.id.timeED);
                    endD.setText(String.format("%d:%d", mHour, mMinute));
                    p_end_time = String.format("%d:%d:00", mHour, mMinute);
                }

            };

    public void previousActivity(View v){
        onBackPressed();
    }

    // 선택된 이미지 가져오기
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQ_CODE_SELECT_IMAGE){
            if(resultCode== Activity.RESULT_OK) {
                try {
                    //이미지 데이터를 비트맵으로 받아온다.
                    Bitmap image_bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());

                    //배치해놓은 ImageView에 set
                    ImageView imageS = (ImageView)findViewById(R.id.imageChoice);
                    imageS.setImageBitmap(image_bitmap);

                    mImageCaptureUri = data.getData();
                    Log.e("SmartWheel", mImageCaptureUri.getPath().toString());
                    real_album_path= getPath(mImageCaptureUri);
                    Log.e("real_album_path",real_album_path);

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

    public void rent(String provide_image,
                     String provide_type,
                     String provider_phone,
                     String provide_start_date,
                     String provide_end_date,
                     String provide_start_time,
                     String provide_end_time,
                     String provide_location,
                     String provide_description,
                     String provide_rule,
                     String provide_refund_rule,
                     String o_name, String o_price,
                     ArrayList<String> o_name_list,
                     ArrayList<String> o_price_list){

        RequestBody user = RequestBody.create(MediaType.parse("multipart/form-data"),String.valueOf(user_id));
        RequestBody p_type = RequestBody.create(MediaType.parse("multipart/form-data"),String.valueOf(provide_type));
        RequestBody p_phone = RequestBody.create(MediaType.parse("multipart/form-data"),String.valueOf(provider_phone));
        RequestBody p_loc_name = RequestBody.create(MediaType.parse("multipart/form-data"),String.valueOf("임시 장소 이름"));
        RequestBody p_start_date = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(provide_start_date));
        RequestBody p_end_date = RequestBody.create(MediaType.parse("multipart/form-data"),String.valueOf(provide_end_date));
        RequestBody p_start_time = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(provide_start_time));
        RequestBody p_end_time = RequestBody.create(MediaType.parse("multipart/form-data"),String.valueOf(provide_end_time));
        RequestBody p_locaion = RequestBody.create(MediaType.parse("multipart/form-data"),String.valueOf("서울"));
        RequestBody p_desciption = RequestBody.create(MediaType.parse("multipart/form-data"),String.valueOf(provide_description));
        RequestBody p_rule = RequestBody.create(MediaType.parse("multipart/form-data"),String.valueOf(provide_rule));
        RequestBody p_refund_rule = RequestBody.create(MediaType.parse("multipart/form-data"),String.valueOf(provide_refund_rule));

        Log.e("내용",String.valueOf(user_id));
        Log.e("내용",String.valueOf(provide_type));
        Log.e("내용",String.valueOf(provider_phone));
        Log.e("내용",String.valueOf(provide_start_date));
        Log.e("내용",String.valueOf(provide_end_date));
        Log.e("내용",String.valueOf(provide_start_time));
        Log.e("내용",String.valueOf(provide_end_time));
        Log.e("내용",String.valueOf(provide_description));
        Log.e("내용",String.valueOf(provide_rule));
        Log.e("내용",String.valueOf(provide_refund_rule));

        //이미지 업로드
        File file = new File(provide_image);
        Log.e("파일경로",String.valueOf(provide_image));
        Log.e("파일이름",String.valueOf(file.getName()));
        RequestBody surveyBody = RequestBody.create(MediaType.parse("image/*"), file);
        Log.e("이미지",String.valueOf(surveyBody.contentType()));
        MultipartBody.Part filePart = MultipartBody.Part.createFormData("provide_image", file.getName(), surveyBody);

        Call<LendLocation> rentLoc = apiService.rentLocation(user_token,user,p_type,p_phone,p_loc_name,p_start_date,p_end_date,p_start_time,p_end_time,p_locaion,p_desciption,p_rule,p_refund_rule,filePart);
        rentLoc.enqueue(new Callback<LendLocation>() {
            @Override
            public void onResponse(Call<LendLocation> call, Response<LendLocation> response) {
                if(response.isSuccessful()){
                    Log.e("아이디받아오는지", String.valueOf(response.body().getProvide_id()));
                    Log.e("장소제공:", "성공");
                    if (o_name_list.size()==0&&o_price_list.size()==0){
                        rent_option(response.body().getProvide_id(),o_name,o_price);
                    }else {
                        for (int i=0;i < o_name_list.size(); i++){
                            rent_option(response.body().getProvide_id(),o_name_list.get(i),o_price_list.get(i));
                        }
                    }
                    Intent provide_list = new Intent(getApplicationContext(),LocationLendStart.class);
                    startActivity(provide_list);
                    finish();

                }else {
                    int StatusCode = response.code();
                    Log.i(ApplicationController.TAG, "상태 Code : " + StatusCode);
                    Log.e("메세지", String.valueOf(response.message()));
                    Log.e("리스폰스에러바디", String.valueOf(response.errorBody()));
                    Log.e("리스폰스바디", String.valueOf(response.body()));
                }
            }

            @Override
            public void onFailure(Call<LendLocation> call, Throwable t) {
                Log.i(ApplicationController.TAG, "장소제공 서버 연결 실패 Message : " + t.getMessage());
            }
        });
    }

    public void rent_option(int provide_id, String o_name, String o_price){
        LendLocationOption lendOption = new LendLocationOption();
        lendOption.setUser(user_id);
        lendOption.setProvide(provide_id);
        lendOption.setProvide_option_name(o_name);
        lendOption.setProvide_price(o_price);

        Call<LendLocationOption> optionCall = apiService.rentLocationOption(user_token,lendOption);
        optionCall.enqueue(new Callback<LendLocationOption>() {
            @Override
            public void onResponse(Call<LendLocationOption> call, Response<LendLocationOption> response) {
                if(response.isSuccessful()){
                    Log.e("옵션 제공:", "성공");
                }else {
                    int StatusCode = response.code();
                    Log.i(ApplicationController.TAG, "상태 Code : " + StatusCode);
                    Log.e("메세지", String.valueOf(response.message()));
                    Log.e("리스폰스에러바디", String.valueOf(response.errorBody()));
                    Log.e("리스폰스바디", String.valueOf(response.body()));
                }
            }

            @Override
            public void onFailure(Call<LendLocationOption> call, Throwable t) {
                Log.i(ApplicationController.TAG, "옵션 서버 연결 실패 Message : " + t.getMessage());
            }
        });
    }

    public void restApiBuilder() {
        ApplicationController application = ApplicationController.getInstance();
        application.buildNetworkService();
        apiService = ApplicationController.getInstance().getRestApiService();
    }

    public void getLocalData(){
        SharedPreferences pref = getSharedPreferences("User", Activity.MODE_PRIVATE);
        user_token = pref.getString("auth_token",null);
        user_id = pref.getInt("user_id",0);
    }

}

