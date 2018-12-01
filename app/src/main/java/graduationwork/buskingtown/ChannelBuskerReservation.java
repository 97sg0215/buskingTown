package graduationwork.buskingtown;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import graduationwork.buskingtown.api.RestApiService;
import graduationwork.buskingtown.model.RoadConcert;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChannelBuskerReservation extends Fragment {

    String user_token;
    int busker_id;
    RestApiService apiService;
    RelativeLayout addBtn;

    public ChannelBuskerReservation() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.activity_channel_busker_reservation, container, false);

        restApiBuilder();

        getLocalData();

        addBtn = (RelativeLayout) v.findViewById(R.id.addBtn);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent add = new Intent(getActivity(), BuskingOpen.class);
                startActivity(add);
            }
        });

        LinearLayout roadBox = (LinearLayout) v.findViewById(R.id.roadContainer);

        Call<List<RoadConcert>> listCall = apiService.getPreviousReservationRoadConcert(user_token, busker_id);
        listCall.enqueue(new Callback<List<RoadConcert>>() {
            @Override
            public void onResponse(Call<List<RoadConcert>> call, Response<List<RoadConcert>> response) {
                if (response.isSuccessful()) {
                    List<RoadConcert> roadConcerts = response.body();
                    if (roadConcerts.size() != 0) {
                        for (int i = 0; i < roadConcerts.size(); i++) {
                            View roadlist = inflater.inflate(R.layout.roadconcert_reservation, roadBox, false);
                            TextView dateTime = roadlist.findViewById(R.id.dateTime);
                            TextView location = roadlist.findViewById(R.id.location);
                            TextView datailAddress = roadlist.findViewById(R.id.datailAddress);
                            Button next = roadlist.findViewById(R.id.next_location);

                            String[] start_time_words = roadConcerts.get(i).getRoad_concert_start_time().split(":");
                            String[] end_time_words = roadConcerts.get(i).getRoad_concert_end_time().split(":");
                            String start_time = start_time_words[0] + ":" + start_time_words[1];
                            String end_time = end_time_words[0] + ":" + end_time_words[1];
                            String getDate = roadConcerts.get(i).getRoad_concert_date() + " " + start_time + "~" + end_time;

                            dateTime.setText(getDate);
                            location.setText(roadConcerts.get(i).getRoad_name());
                            datailAddress.setText(roadConcerts.get(i).getRoad_address());

                            int finalI = i;
                            next.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent same_reservation = new Intent(getActivity(), BuskingOpen.class);
                                    same_reservation.putExtra("location_name", String.valueOf(roadConcerts.get(finalI).getRoad_name()));
                                    same_reservation.putExtra("location_detail", String.valueOf(roadConcerts.get(finalI).getRoad_address()));
                                    startActivity(same_reservation);
                                }
                            });

                            if (roadlist.getParent() != null)
                                ((ViewGroup) roadlist.getParent()).removeView(roadlist);
                            roadBox.addView(roadlist);

                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<List<RoadConcert>> call, Throwable t) {

            }
        });

        return v;
    }

    public void getLocalData() {
        SharedPreferences pref = this.getActivity().getSharedPreferences("User", Activity.MODE_PRIVATE);
        SharedPreferences buskerPref = this.getActivity().getSharedPreferences("BuskerUser", Activity.MODE_PRIVATE);
        user_token = pref.getString("auth_token", null);
        busker_id = buskerPref.getInt("busker_id", 0);
    }

    public void restApiBuilder() {
        ApplicationController application = ApplicationController.getInstance();
        application.buildNetworkService();
        apiService = ApplicationController.getInstance().getRestApiService();
    }

}