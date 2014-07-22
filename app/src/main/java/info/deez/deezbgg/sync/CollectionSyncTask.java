package info.deez.deezbgg.sync;

import android.os.AsyncTask;
import android.util.Log;
import android.util.Pair;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import info.deez.deezbgg.entity.BoardGame;
import info.deez.deezbgg.entity.CollectionItem;
import info.deez.deezbgg.repository.BoardGameRepository;
import info.deez.deezbgg.repository.CollectionItemRepository;
import info.deez.deezbgg.repository.DeezbggDbHelper;
import info.deez.deezbgg.ui.fragment.collection.CollectionFragmentAdapter;
import info.deez.deezbgg.ui.fragment.collection.CollectionFragmentDataGetter;
import info.deez.deezbgg.ui.fragment.collection.CollectionFragmentRowData;

public class CollectionSyncTask extends AsyncTask<String, Void, List<CollectionFragmentRowData>> {
    private static final String TAG = "CollectionSyncTask";
    private DeezbggDbHelper mDbHelper;
    private CollectionFragmentAdapter mAdapter;

    public CollectionSyncTask(DeezbggDbHelper dbHelper, CollectionFragmentAdapter adapter) {
        mDbHelper = dbHelper;
        mAdapter = adapter;
    }

    @Override
    protected List<CollectionFragmentRowData> doInBackground(String... params) {
        try {
            Log.i(TAG, "Starting sync of collection...");
            String username = params[0];

            CollectionItemRepository collectionItemRepository = new CollectionItemRepository(mDbHelper);

            List<CollectionItem> currentCollection = collectionItemRepository.getAllCollectionItems();
            List<CollectionItem> targetCollection = BoardGameGeekApi.getCollectionForUser(username);

            // Figure out what to add
            List<CollectionItem> itemsToAdd = new ArrayList<CollectionItem>();
            List<CollectionItem> itemsToRemove = new ArrayList<CollectionItem>();
            SyncUtils.determineChanges(currentCollection, targetCollection, itemsToAdd, itemsToRemove);

            collectionItemRepository.addCollectionItems(itemsToAdd);
            collectionItemRepository.deleteCollectionItems(itemsToRemove);

            // Get new UI-ready data
            CollectionFragmentDataGetter dataGetter = new CollectionFragmentDataGetter(mDbHelper);
            Log.i(TAG, "Finished sync of collection.");
            return dataGetter.getData();
        }
        catch (Exception e) {
            Log.e(TAG, "Error syncing collection", e);
        }

        return null;
    }

    @Override
    protected void onPostExecute(List<CollectionFragmentRowData> result) {
        mAdapter.swapCollectionData(result);
    }
}
