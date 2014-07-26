package info.deez.deezbgg.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;

import info.deez.deezbgg.R;
import info.deez.deezbgg.sync.SyncUtils;

public class CollectionActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SyncUtils.createAccountAndEnableSync(this);

        setContentView(R.layout.activity_collection);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_collection, menu);
        return true;
    }
}
