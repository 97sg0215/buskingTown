package graduationwork.buskingtown;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import graduationwork.buskingtown.model.Login;

public class Mypage extends Fragment {

    RelativeLayout go_Busker;

    public Mypage(){
        // Required empty public constructor
    }

    //유저 정보 변수들
    String user_id;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.activity_my_page, container, false);

//        user_id = getArguments().getString("userId");
  //      Log.e("아이디",user_id);

        //프로필 이미지 변수
        ImageView profile = (ImageView) v.findViewById(R.id.profileImg);
        //임시 이미지
        int imageresource = getResources().getIdentifier("@drawable/test_img", "drawable", getActivity().getPackageName());
        //이미지 원형 처리
        Picasso.with(getActivity()).load(imageresource).transform(new CircleTransForm()).into(profile);

        //프로필 설정
        //로그인 액티비티에서 얻어옵니다
        SharedPreferences pref = getActivity().getSharedPreferences("User", Activity.MODE_PRIVATE);
        String username = pref.getString("username",null);
        Log.e("사용자아이디",username);

        TextView userID = (TextView) v.findViewById(R.id.userId);
        userID.setText(username);

        //버스커되기 채널 가기
        go_Busker = (RelativeLayout) v.findViewById(R.id.goBusker);
        go_Busker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(),BuskerCertification.class);
                startActivity(i);
            }
        });


        return v;
    }
}