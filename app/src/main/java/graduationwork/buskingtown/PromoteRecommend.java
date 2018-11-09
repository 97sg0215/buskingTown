package graduationwork.buskingtown;

import android.Manifest;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.ArrayList;

import javax.mail.MessagingException;
import javax.mail.SendFailedException;

public class PromoteRecommend extends Fragment {

    private String email = "buskingtown2018@gmail.com";
    private EditText message , phone_hint, email_hint, date_hint;
    private Button paymentBtn = null;

    String team_name, phone, user_email, date, total_message;

    public PromoteRecommend(){
        // Required empty public constructor
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.activity_promote_recommend, container, false);

        //이메일 인터넷 사용 권한 허용
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .permitDiskReads()
                .permitDiskWrites()
                .permitNetwork().build());

        //email = (EditText) v.findViewById(R.id.emailHint); //받는 사람의 이메일
        message = (EditText) v.findViewById(R.id.teamNameHint); //본문 내용
        phone_hint = (EditText) v.findViewById(R.id.phoneNumberHint);
        email_hint = (EditText) v.findViewById(R.id.emailHint);

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


        total_message = "팀 이름: "+ team_name+"\n"
        +"신청인 휴대폰: " + phone+"\n"
        +"신청인 이메일: " + user_email+"\n"
        +"신청 날짜: " ;

        paymentBtn = (Button) v.findViewById(R.id.paymentBtn);
        paymentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new senmailAsync().execute();
            }

        });

        return v;
    }

    private class senmailAsync extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                GMailSender gMailSender = new GMailSender("buskingtown2018@gmail.com", "khphTown123");
                //GMailSender.sendMail(제목, 본문내용, 받는사람);
                gMailSender.sendMail("추천순위 노출 신청입니다.", total_message, email);
                Log.e("메시지",String.valueOf(message));
                Log.e("이메일",String.valueOf(email));
                Log.e("이메일","이메일을 성공적으로 보냈습니다.");
              //  Toast.makeText(getActivity().getApplicationContext(), "이메일을 성공적으로 보냈습니다.", Toast.LENGTH_SHORT).show();
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
