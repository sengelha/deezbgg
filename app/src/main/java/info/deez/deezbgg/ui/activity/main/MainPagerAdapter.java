package info.deez.deezbgg.ui.activity.main;

import android.app.Fragment;
import android.app.FragmentManager;

import info.deez.deezbgg.ui.fragment.collection.CollectionFragment;
import info.deez.deezbgg.ui.fragment.plays.PlaysFragment;
import info.deez.deezbgg.utils.FragmentPagerAdapter;

public class MainPagerAdapter extends FragmentPagerAdapter {
    public MainPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: return new CollectionFragment();
            case 1: return new PlaysFragment();
            default: return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0: return "Collection";
            case 1: return "Plays";
            default: return null;
        }
    }
}