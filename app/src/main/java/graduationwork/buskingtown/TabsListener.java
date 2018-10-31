package graduationwork.buskingtown;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Fragment;
import android.app.FragmentTransaction;


/**
 * @author Srikanth G.R
 *
 */
public class TabsListener implements ActionBar.TabListener, android.support.v7.app.ActionBar.TabListener {
    public Fragment fragment;

    public TabsListener(Fragment fragment) {
        this.fragment = fragment;
    }

    public void onTabReselected(Tab tab, FragmentTransaction ft) {
    }

    public void onTabSelected(Tab tab, FragmentTransaction ft) {
        if (fragment != null) {
            ft.replace(R.id.mainLayout, fragment);
            // isSettingClicked = false;
        } else {
            ft.attach(fragment);
        }
    }

    public void onTabUnselected(Tab tab, FragmentTransaction ft) {
        if (fragment != null) {
            try {
                ft.remove(fragment);
            } catch (Exception e) {
                // TODO: handle exception
            }
        }
    }

    @Override
    public void onTabSelected(android.support.v7.app.ActionBar.Tab tab, android.support.v4.app.FragmentTransaction ft) {

    }

    @Override
    public void onTabUnselected(android.support.v7.app.ActionBar.Tab tab, android.support.v4.app.FragmentTransaction ft) {

    }

    @Override
    public void onTabReselected(android.support.v7.app.ActionBar.Tab tab, android.support.v4.app.FragmentTransaction ft) {

    }
}
