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
import info.deez.deezbgg.repository.BoardGameRepository;
import info.deez.deezbgg.repository.PlayRepository;

class PlaysLoader extends AsyncTaskLoader<List<PlaysFragmentRowData>> {
    private static final String TAG = "PlaysLoader";
    private List<PlaysFragmentRowData> mData;

    public PlaysLoader(Context context) {
        super(context);
    }

    @Override
    public List<PlaysFragmentRowData> loadInBackground() {
        Log.i(TAG, "Starting load in background");
        List<Play> plays = PlayRepository.getAllPlays();

        Set<Long> boardGameIds = new HashSet<Long>();
        for (Play play : plays) {
            boardGameIds.add(play.boardGameId);
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

        List<PlaysFragmentRowData> rows = new ArrayList<PlaysFragmentRowData>(plays.size());
        for (Play play : plays) {
            PlaysFragmentRowData row = new PlaysFragmentRowData();
            row.play = play;
            row.boardGame = boardGames.get(row.play.boardGameId);
            row.boardGameBitmap = boardGameBitmaps.get(row.play.boardGameId);
            rows.add(row);
        }

        Collections.sort(rows, new Comparator<PlaysFragmentRowData>() {
            @Override
            public int compare(PlaysFragmentRowData lhs, PlaysFragmentRowData rhs) {
                return -lhs.play.date.compareTo(rhs.play.date);
            }
        });

        Log.i(TAG, "Finished load in background.  Loaded " + rows.size() + " rows");
        return rows;
    }


    /**
     * Called when there is new data to deliver to the client.  The
     * super class will take care of delivering it; the implementation
     * here just adds a little more logic.
     */
    @Override
    public void deliverResult(List<PlaysFragmentRowData> data) {
        if (isReset()) {
            // An async query came in while the loader is stopped.  We
            // don't need the result.
            if (data != null) {
                onReleaseResources(data);
            }
        }
        List<PlaysFragmentRowData> oldData = mData;
        mData = data;

        if (isStarted()) {
            // If the Loader is currently started, we can immediately
            // deliver its results.
            super.deliverResult(data);
        }

        // At this point we can release the resources associated with
        // 'oldApps' if needed; now that the new result is delivered we
        // know that it is no longer in use.
        if (oldData != null) {
            onReleaseResources(oldData);
        }
    }

    /**
     * Handles a request to start the Loader.
     */
    @Override
    protected void onStartLoading() {
        if (mData != null) {
            // If we currently have a result available, deliver it
            // immediately.
            deliverResult(mData);
        }

        if (mData == null) {
            // If the data has changed since the last time it was loaded
            // or is not currently available, start a load.
            forceLoad();
        }
    }

    /**
     * Handles a request to stop the Loader.
     */
    @Override
    protected void onStopLoading() {
        // Attempt to cancel the current load task if possible.
        cancelLoad();
    }

    /**
     * Handles a request to cancel a load.
     */
    @Override
    public void onCanceled(List<PlaysFragmentRowData> data) {
        super.onCanceled(data);

        // At this point we can release the resources associated with 'apps'
        // if needed.
        onReleaseResources(data);
    }

    /**
     * Handles a request to completely reset the Loader.
     */
    @Override
    protected void onReset() {
        super.onReset();

        // Ensure the loader is stopped
        onStopLoading();

        // At this point we can release the resources associated with 'apps'
        // if needed.
        if (mData != null) {
            onReleaseResources(mData);
            mData = null;
        }
    }

    /**
     * Helper function to take care of releasing resources associated
     * with an actively loaded data set.
     */
    protected void onReleaseResources(List<PlaysFragmentRowData> data) {
        // For a simple List<> there is nothing to do.  For something
        // like a Cursor, we would close it here.
    }
}
