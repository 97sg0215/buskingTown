package graduationwork.buskingtown;


import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ChannelBuskerPracticeroom extends Fragment {
    public ChannelBuskerPracticeroom(){
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.activity_channel_busker_practiceroom, container, false);

        return v;
    }


}
