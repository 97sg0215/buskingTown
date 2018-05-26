package graduationwork.buskingtown;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class Mypage extends Fragment {
    public Mypage(){
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.activity_my_page, container, false);

        //프로필 이미지 변수
        ImageView profile = (ImageView) v.findViewById(R.id.profileImg);
        //임시 이미지
        int imageresource = getResources().getIdentifier("@drawable/test_img", "drawable", getActivity().getPackageName());

        Picasso.with(getActivity()).load(imageresource).transform(new CircleTransForm()).into(profile);


        return v;
    }

}
