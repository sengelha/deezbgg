package info.deez.deezbgg.ui.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import info.deez.deezbgg.R;
import info.deez.deezbgg.ui.fragment.collection.CollectionFragment;
import info.deez.deezbgg.ui.fragment.plays.PlaysFragment;


public class MainActivity extends Activity {
    private ActionBar.Tab mTab1;
    private ActionBar.Tab mTab2;
    private Fragment mFragment1;
    private Fragment mFragment2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFragment1 = new CollectionFragment();
        mFragment2 = new PlaysFragment();

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Set Tab Icon and Titles
        mTab1 = actionBar.newTab().setText("Collection");
        mTab2 = actionBar.newTab().setText("Plays");

        // Set Tab Listeners
        mTab1.setTabListener(new TabListener(mFragment1));
        mTab2.setTabListener(new TabListener(mFragment2));

        // Add tabs to actionbar
        actionBar.addTab(mTab1);
        actionBar.addTab(mTab2);
    }
}
