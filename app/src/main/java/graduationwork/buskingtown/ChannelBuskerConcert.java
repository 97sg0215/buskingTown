package graduationwork.buskingtown;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ChannelBuskerConcert extends Fragment {

    int test__concert=3;

    public ChannelBuskerConcert(){
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.activity_channel_busker_concert, container, false);

        //로딩코드
        CheckTypesTask task = new CheckTypesTask();
        task.execute();

        for (int concertCount=0; concertCount<test__concert; concertCount++) {
            LinearLayout concertBox = (LinearLayout) v.findViewById(R.id.concertContainer);

            TextView concertText = (TextView)v.findViewById(R.id.concertText);
            if (test__concert > 1 ){
                concertText.setVisibility(View.GONE);
                View concertlist = inflater.inflate(R.layout.concertbox,concertBox,false);
                if(concertlist.getParent()!= null)
                    ((ViewGroup)concertlist.getParent()).removeView(concertlist);
               concertBox.addView(concertlist);
            }
        }

        return v;
    }

    //로딩코드, fragment일때는 getContext()를 씀
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
