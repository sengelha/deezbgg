package info.deez.deezbgg.ui.activity;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentTransaction;

import info.deez.deezbgg.R;

public class TabListener implements ActionBar.TabListener {
    Fragment mFragment;

    public TabListener(Fragment fragment) {
        mFragment = fragment;
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        ft.replace(R.id.fragment_container, mFragment);
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
        ft.remove(mFragment);
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
    }
}