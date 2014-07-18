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
import info.deez.deezbgg.repository.DeezbggDbHelper;

/**
 * Asynchronously loads the collection stored in local storage.
 * Monitors changes to local storage and notifies the client(s).
 *
 * Some pieces of information (e.g. bitmaps) are loaded on-demand
 * by the adapter.
 */
public class CollectionLoader extends BaseLoader<List<CollectionFragmentRowData>> {
    private static final String TAG = "CollectionLoader";
    private CollectionFragmentDataGetter mDataGetter;

    public CollectionLoader(Context context, DeezbggDbHelper dbHelper) {
        super(context);
        mDataGetter = new CollectionFragmentDataGetter(dbHelper);
    }

    @Override
    public List<CollectionFragmentRowData> loadInBackground() {
        return mDataGetter.getData();
    }
}
