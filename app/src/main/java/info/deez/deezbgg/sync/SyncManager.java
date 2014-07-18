package info.deez.deezbgg.sync;

import android.app.Fragment;
import android.content.Context;
import android.util.Log;

import info.deez.deezbgg.repository.CollectionItemRepository;
import info.deez.deezbgg.repository.DeezbggDbHelper;
import info.deez.deezbgg.ui.fragment.collection.CollectionFragment;
import info.deez.deezbgg.ui.fragment.collection.CollectionFragmentAdapter;
import info.deez.deezbgg.ui.fragment.collection.CollectionLoader;
import info.deez.deezbgg.ui.fragment.plays.PlaysFragmentAdapter;

public class SyncManager {
    private static final String TAG = "SyncManager";
    private Context mContext;

    public SyncManager(Context context) {
        mContext = context;
    }

    public void syncCollectionAsync(String username, DeezbggDbHelper dbHelper, CollectionFragmentAdapter adapter) {
        Log.i(TAG, "Starting sync of collection in background...");
        CollectionSyncTask collectionSyncTask = new CollectionSyncTask(dbHelper, adapter);
        collectionSyncTask.execute(username);
    }

    public void syncPlaysAsync(String username, DeezbggDbHelper dbHelper, PlaysFragmentAdapter adapter) {
        Log.i(TAG, "Starting sync of plays in background...");
        PlaySyncTask playSyncTask = new PlaySyncTask(dbHelper, adapter);
        playSyncTask.execute(username);
    }
}
