package info.deez.deezbgg.ui.fragment.plays;

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
import info.deez.deezbgg.entity.Play;
import info.deez.deezbgg.loader.BaseLoader;
import info.deez.deezbgg.repository.BoardGameRepository;
import info.deez.deezbgg.repository.DeezbggDbHelper;
import info.deez.deezbgg.repository.PlayRepository;
import info.deez.deezbgg.ui.fragment.collection.CollectionFragmentDataGetter;

class PlaysLoader extends BaseLoader<List<PlaysFragmentRowData>> {
    private static final String TAG = "PlaysLoader";
    private PlaysFragmentDataGetter mDataGetter;

    public PlaysLoader(Context context, DeezbggDbHelper dbHelper) {
        super(context);
        mDataGetter = new PlaysFragmentDataGetter(dbHelper);
    }

    @Override
    public List<PlaysFragmentRowData> loadInBackground() {
        return mDataGetter.getData();
    }
}
