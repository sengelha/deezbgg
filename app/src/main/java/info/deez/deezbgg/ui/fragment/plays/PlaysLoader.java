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

class PlaysLoader extends BaseLoader<List<PlaysFragmentRowData>> {
    private static final String TAG = "PlaysLoader";
    private BoardGameRepository mBoardGameRepository;
    private PlayRepository mPlayRepository;

    public PlaysLoader(Context context, DeezbggDbHelper dbHelper) {
        super(context);
        mBoardGameRepository = new BoardGameRepository(dbHelper);
        mPlayRepository = new PlayRepository(dbHelper);
    }

    @Override
    public List<PlaysFragmentRowData> loadInBackground() {
        Log.i(TAG, "Starting load in background");
        List<Play> plays = mPlayRepository.getAllPlays();

        Set<Long> boardGameIds = new HashSet<Long>();
        for (Play play : plays) {
            boardGameIds.add(play.boardGameId);
        }
        Dictionary<Long, BoardGame> boardGames = mBoardGameRepository.getBoardGamesByIds(boardGameIds);

        List<PlaysFragmentRowData> rows = new ArrayList<PlaysFragmentRowData>(plays.size());
        for (Play play : plays) {
            PlaysFragmentRowData row = new PlaysFragmentRowData();
            row.play = play;
            row.boardGame = boardGames.get(row.play.boardGameId);
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
}
