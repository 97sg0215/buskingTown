package graduationwork.buskingtown;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.SendFailedException;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;

public class PromoteMainbanner extends Fragment {

    private String email = "buskingtown2018@gmail.com";
    private EditText message , phone_hint, email_hint, date_hint, content_hint, requriement_hint;
    private Button paymentBtn = null;

    String team_name, phone, user_email, date, total_message, content, requriement;

    TextView publicDateHint,imageaddHint;
    SimpleDateFormat simpleDateFormat;
    int promoteYear, promoteMonth, promoteDay, mHour, mMinute;
    RelativeLayout dateImgLayout, fileAdd;
    String p_date;

    private Uri mImageCaptureUri;
    String real_album_path;

    final int REQ_CODE_SELECT_IMAGE=100;

    public PromoteMainbanner(){
        // Required empty public constructor
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.activity_promote_mainbanner, container, false);

        //달력개체 불러옴
        final GregorianCalendar startCalendar = new GregorianCalendar();
        promoteYear = startCalendar.get(Calendar.YEAR);
        promoteMonth = startCalendar.get(Calendar.MONTH);
        promoteDay = startCalendar.get(Calendar.DAY_OF_MONTH);

        //시간을 가져오기위한 Calendar 인스턴스 선언
        Calendar cal = new GregorianCalendar();
        mHour = cal.get(Calendar.HOUR_OF_DAY);
        mMinute = cal.get(Calendar.MINUTE);

        //날짜형태
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);

        Calendar minDate = Calendar.getInstance();

        dateImgLayout = (RelativeLayout) v.findViewById(R.id.dateImgLayout);
        publicDateHint = (TextView) v.findViewById(R.id.publicDateHint);
        dateImgLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog dialog = new DatePickerDialog(getContext(),R.style.MyDatePickerDialogTheme, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int date) {
                        publicDateHint.setText(year + "년 " + (month + 1) + "월 " + date + "일");
                        p_date = String.valueOf(year+"-"+(month+1)+"-"+date);
                    }
                }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));
                dialog.getDatePicker().setMinDate(new Date().getTime());    //입력한 날짜 이후로 클릭 안되게 옵션
                dialog.show();
            }
        });

        //이메일 인터넷 사용 권한 허용
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .permitDiskReads()
                .permitDiskWrites()
                .permitNetwork().build());

        message = (EditText) v.findViewById(R.id.teamNameHint); //본문 내용
        phone_hint = (EditText) v.findViewById(R.id.phoneNumberHint);
        email_hint = (EditText) v.findViewById(R.id.emailHint);
        content_hint = (EditText)v.findViewById(R.id.publicContentHint);
        requriement_hint = (EditText)v.findViewById(R.id.requriementHint);
        fileAdd = (RelativeLayout) v.findViewById(R.id.fileAdd);

        fileAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //갤러리 열기
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
                intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQ_CODE_SELECT_IMAGE);
            }
        });

        imageaddHint = (TextView) v.findViewById(R.id.imageaddHint);

        message.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                team_name = message.getText().toString();
            }
        });

        phone_hint.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

        phone_hint.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                phone = phone_hint.getText().toString();
            }
        });

        email_hint.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                user_email = email_hint.getText().toString();
            }
        });

        content_hint.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                content = content_hint.getText().toString();
            }
        });

        requriement_hint.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                requriement = requriement_hint.getText().toString();
            }
        });

        paymentBtn = (Button) v.findViewById(R.id.paymentBtn);
        paymentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new senmailAsync().execute();
            }
        });

        return v;
    }

    // 선택된 이미지 가져오기
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQ_CODE_SELECT_IMAGE){
            if(resultCode== Activity.RESULT_OK) {
                try {
                    mImageCaptureUri = data.getData();
                    Log.e("SmartWheel", mImageCaptureUri.getPath().toString());
                    real_album_path= getPath(mImageCaptureUri);
                    Log.e("real_album_path",real_album_path);
                    imageaddHint.setText(real_album_path);
                } catch (Exception e) { e.printStackTrace();	}
            }
        }
    }

    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().getContentResolver().query(uri, projection, null, null, null);
        getActivity().startManagingCursor(cursor);
        int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(columnIndex);
    }

    private class senmailAsync extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            total_message = "팀 이름: "+ team_name+"\n"
                    +"신청인 휴대폰: " + phone+"\n"
                    +"신청인 이메일: " + user_email+"\n"
                    +"신청 날짜: "+ p_date+"\n"
                    +"신청 내용: "+ content+"\n"
                    +"요구 사항:" + requriement;
            try {
                GMailSender gMailSender = new GMailSender("buskingtown2018@gmail.com", "khphTown123");
                //GMailSender.sendMail(제목, 본문내용, 받는사람);
                gMailSender.sendMailWithFile("추천순위 노출 신청입니다.", total_message, email, real_album_path, team_name);
                Log.e("이메일",String.valueOf(email));
                Log.e("이메일","이메일을 성공적으로 보냈습니다.");
//                Toast.makeText(getActivity().getApplicationContext(), "메인배너 홍보 신청이 완료 되었습니다!", Toast.LENGTH_SHORT).show();
            } catch (SendFailedException e) {
                Log.e("이메일","이메일 형식이 잘못되었습니다.");
                //   Toast.makeText(getActivity().getApplicationContext(), "이메일 형식이 잘못되었습니다.", Toast.LENGTH_SHORT).show();
            } catch (MessagingException e) {
                Log.e("이메일","인터넷 연결을 확인해주십시오");
                // Toast.makeText(getActivity().getApplicationContext(), "인터넷 연결을 확인해주십시오", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
