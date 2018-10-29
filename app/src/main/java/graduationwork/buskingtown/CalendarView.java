package graduationwork.buskingtown;


import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.TabListener;


import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class CalendarView extends Fragment  {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.activity_calendar, container, false);

       // final ActionBar actionBar = getActionBar();
        //actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        //actionBar.setDisplayUseLogoEnabled(false);
        //actionBar.setDisplayShowHomeEnabled(false);
        //actionBar.setDisplayShowTitleEnabled(false);
        //ActionBar.Tab monthTab = actionBar.newTab().setText("DAY");

        DayFragment dayFragment = new DayFragment();

        //monthTab.setTabListener(new TabsListener(dayFragment));
        //actionBar.addTab(monthTab, 0, true);


        return v;
    }

}