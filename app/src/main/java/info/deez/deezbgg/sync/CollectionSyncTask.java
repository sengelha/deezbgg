package info.deez.deezbgg.sync;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.util.Pair;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import info.deez.deezbgg.bggapi.BoardGameGeekXmlParser;
import info.deez.deezbgg.entity.BoardGame;
import info.deez.deezbgg.entity.CollectionItem;
import info.deez.deezbgg.repository.BoardGameRepository;
import info.deez.deezbgg.repository.CollectionItemRepository;
import info.deez.deezbgg.repository.DeezbggDbHelper;
import info.deez.deezbgg.ui.fragment.collection.CollectionFragment;
import info.deez.deezbgg.ui.fragment.collection.CollectionFragmentAdapter;
import info.deez.deezbgg.ui.fragment.collection.CollectionFragmentDataGetter;
import info.deez.deezbgg.ui.fragment.collection.CollectionFragmentRowData;
import info.deez.deezbgg.ui.fragment.collection.CollectionLoader;

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
            String username = params[0];
            URL url = new URL("http://boardgamegeek.com/xmlapi2/collection?username=" + username);
            URLConnection conn = url.openConnection();
            BoardGameGeekXmlParser parser = new BoardGameGeekXmlParser();

            List<Pair<CollectionItem, BoardGame>> results;
            InputStream stream = conn.getInputStream();
            try {
                results = parser.parse(stream);
            } finally {
                stream.close();
            }

            // Put data in database
            CollectionItemRepository collectionItemRepository = new CollectionItemRepository(mDbHelper);
            BoardGameRepository boardGameRepository = new BoardGameRepository(mDbHelper);
            collectionItemRepository.deleteAllCollectionItems();
            boardGameRepository.deleteAllBoardGames();
            for (Pair<CollectionItem, BoardGame> result : results) {
                collectionItemRepository.addCollectionItem(result.first);
                boardGameRepository.addBoardGame(result.second);
            }

            // Get new UI-ready data
            CollectionFragmentDataGetter dataGetter = new CollectionFragmentDataGetter(mDbHelper);
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
