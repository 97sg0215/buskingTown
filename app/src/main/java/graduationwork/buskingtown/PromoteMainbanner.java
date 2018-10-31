package graduationwork.buskingtown;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;

public class PromoteMainbanner extends Fragment {
    public PromoteMainbanner(){
        // Required empty public constructor
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.activity_promote_mainbanner, container, false);

        return v;
    }
}
