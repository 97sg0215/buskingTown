package graduationwork.buskingtown;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Map extends Fragment {

    //호출될프래그먼트 변수들
    private final int locationFRAGMENT = 1;
    private final int dateFRAGMENT = 2;

    //탭바 아이콘 변수들
    private RelativeLayout map, date;
    private TextView map_text, date_text;
    private View map_bar, date_bar;

    public Map() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.activity_map, container, false);

        //로딩코드
        CheckTypesTask task = new CheckTypesTask();
        task.execute();

        callFragment(locationFRAGMENT);

        map_text = (TextView) v.findViewById(R.id.mapsearch);
        date_text = (TextView) v.findViewById(R.id.datesearch);
        map_bar = (View) v.findViewById(R.id.mapsearchBar);
        date_bar = (View) v.findViewById(R.id.datesearchBar);
        map = (RelativeLayout) v.findViewById(R.id.mapsearchTab);
        date = (RelativeLayout) v.findViewById(R.id.datesearchTab);

        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callFragment(locationFRAGMENT);

                map_text.setTextColor(getResources().getColor(R.color.mainYellow));
                map_bar.setVisibility(View.VISIBLE);
                date_text.setTextColor(getResources().getColor(R.color.fontBlack));
                date_bar.setVisibility(View.GONE);
            }
        });

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callFragment(dateFRAGMENT);
                map_text.setTextColor(getResources().getColor(R.color.fontBlack));
                map_bar.setVisibility(View.GONE);

                date_text.setTextColor(getResources().getColor(R.color.mainYellow));
                date_bar.setVisibility(View.VISIBLE);
            }
        });

        return v;
    }


    @SuppressLint("ResourceAsColor")
    private void callFragment(int frament_no) {
        // 프래그먼트 사용을 위해
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();

        switch (frament_no) {
            case 1:
                // '지도' 호출
                RealtimeBuskingMap realtimeBuskingMap = new RealtimeBuskingMap();
                transaction.replace(R.id.realtimeFragmentContainer, realtimeBuskingMap);
                transaction.commit();

                break;
            case 2:
                // '일정페이지' 호출
                CalendarView calendarFragment = new CalendarView();
                transaction.replace(R.id.realtimeFragmentContainer, calendarFragment);
                transaction.commit();

                break;

        }

    }

    private class CheckTypesTask extends AsyncTask<Void, Void, Void> {

        ProgressDialog asyncDialog = new ProgressDialog(
                getContext());

        @Override
        protected void onPreExecute() {
            asyncDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            asyncDialog.setMessage("로딩중입니다..");

            // show dialog
            asyncDialog.show();
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            try {
                for (int i = 0; i < 5; i++) {
                    //asyncDialog.setProgress(i * 30);
                    Thread.sleep(300);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            asyncDialog.dismiss();
            super.onPostExecute(result);
        }
    }


}