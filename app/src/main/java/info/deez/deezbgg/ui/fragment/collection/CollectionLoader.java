package info.deez.deezbgg.ui.fragment.collection;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Dictionary;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

import info.deez.deezbgg.entity.BoardGame;
import info.deez.deezbgg.entity.CollectionItem;
import info.deez.deezbgg.loader.BaseLoader;
import info.deez.deezbgg.repository.BoardGameRepository;
import info.deez.deezbgg.repository.CollectionItemRepository;

/**
 * Asynchronously loads the collection stored in local storage.
 * Monitors changes to local storage and notifies the client(s).
 *
 * Some pieces of information (e.g. bitmaps) are loaded on-demand
 * by the adapter.
 */
class CollectionLoader extends BaseLoader<List<CollectionFragmentRowData>> {
    private static final String TAG = "CollectionLoader";

    public CollectionLoader(Context context) {
        super(context);
    }

    @Override
    public List<CollectionFragmentRowData> loadInBackground() {
        Log.i(TAG, "Starting load in background");
        List<CollectionItem> collectionItems = CollectionItemRepository.getAllCollectionItems();
        Set<Long> boardGameIds = new HashSet<Long>();
        for (CollectionItem collectionItem : collectionItems) {
            boardGameIds.add(collectionItem.boardGameId);
        }
        Dictionary<Long, BoardGame> boardGames = BoardGameRepository.getBoardGamesByIds(boardGameIds);

        Dictionary<Long, Bitmap> boardGameBitmaps = new Hashtable<Long, Bitmap>();
        for (Long boardGameId : boardGameIds) {
            try {
                URL url = new URL(boardGames.get(boardGameId).thumbnailUrl);
                URLConnection conn = url.openConnection();
                InputStream inputStream = conn.getInputStream();
                try {
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    boardGameBitmaps.put(boardGameId, bitmap);
                } finally {
                    inputStream.close();
                }
            } catch (Exception e) {
                Log.e(TAG, "Error loading board game bitmap", e);
            }
        }

        List<CollectionFragmentRowData> rows = new ArrayList<CollectionFragmentRowData>(collectionItems.size());
        for (CollectionItem collectionItem : collectionItems) {
            CollectionFragmentRowData row = new CollectionFragmentRowData();
            row.collectionItem = collectionItem;
            row.boardGame = boardGames.get(row.collectionItem.boardGameId);
            row.boardGameBitmap = boardGameBitmaps.get(row.collectionItem.boardGameId);
            rows.add(row);
        }

        Collections.sort(rows, new Comparator<CollectionFragmentRowData>() {
            @Override
            public int compare(CollectionFragmentRowData lhs, CollectionFragmentRowData rhs) {
                return lhs.boardGame.name.compareTo(rhs.boardGame.name);
            }
        });

        Log.i(TAG, "Finished load in background.  Loaded " + rows.size() + " rows");
        return rows;
    }
}
