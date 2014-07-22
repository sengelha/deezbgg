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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
