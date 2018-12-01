package graduationwork.buskingtown;

import android.app.Activity;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import graduationwork.buskingtown.api.RestApiService;

public class RankingListView extends Fragment {

    RestApiService apiService;

    String user_token, user_name;
    int user_id;

    public RankingListView() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.activity_ranking_list_view, container, false);

        return v;
    }


    //user데이터 얻어오기
    private void getLocalData() {
        SharedPreferences pref = getActivity().getSharedPreferences("User", Activity.MODE_PRIVATE);
        user_token = pref.getString("auth_token", null);
        user_name = pref.getString("username", null);
        user_id = pref.getInt("user_id", 0);

    }

    //api연결
    private void restApiBuilder() {
        ApplicationController application = ApplicationController.getInstance();
        application.buildNetworkService();
        apiService = ApplicationController.getInstance().getRestApiService();
    }


}
