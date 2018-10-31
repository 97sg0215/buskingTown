package graduationwork.buskingtown;

import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import javax.mail.MessagingException;
import javax.mail.SendFailedException;

public class PromoteRecommend extends Fragment {

    private String email = "buskingtown123@gmail.com";
    private EditText message = null;
    private Button paymentBtn = null;

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

        paymentBtn = (Button) v.findViewById(R.id.paymentBtn);
        paymentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    GMailSender gMailSender = new GMailSender("buskingtown2018@gmail.com", "khphTown123");
                    //GMailSender.sendMail(제목, 본문내용, 받는사람);
                    gMailSender.sendMail("추천순위 노출 신청입니다.", message.getText().toString(), email);
                    Log.e("메시지",String.valueOf(message));
                    Log.e("이메일",String.valueOf(email));
                    Toast.makeText(getActivity().getApplicationContext(), "이메일을 성공적으로 보냈습니다.", Toast.LENGTH_SHORT).show();
                } catch (SendFailedException e) {
                    Toast.makeText(getActivity().getApplicationContext(), "이메일 형식이 잘못되었습니다.", Toast.LENGTH_SHORT).show();
                } catch (MessagingException e) {
                    Toast.makeText(getActivity().getApplicationContext(), "인터넷 연결을 확인해주십시오", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        });

        return v;
    }
}
