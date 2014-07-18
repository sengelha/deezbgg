package info.deez.deezbgg.ui.fragment.plays;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Dictionary;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import info.deez.deezbgg.entity.BoardGame;
import info.deez.deezbgg.entity.CollectionItem;
import info.deez.deezbgg.entity.Play;
import info.deez.deezbgg.repository.BoardGameRepository;
import info.deez.deezbgg.repository.CollectionItemRepository;
import info.deez.deezbgg.repository.DeezbggDbHelper;
import info.deez.deezbgg.repository.PlayRepository;
import info.deez.deezbgg.ui.fragment.collection.CollectionFragmentRowData;

public class PlaysFragmentDataGetter {
    private static final String TAG = "CollectionFragmentDataGetter";
    private PlayRepository mPlayRepository;
    private BoardGameRepository mBoardGameRepository;

    public PlaysFragmentDataGetter(DeezbggDbHelper dbHelper) {
        mPlayRepository = new PlayRepository(dbHelper);
        mBoardGameRepository = new BoardGameRepository(dbHelper);
    }

    public List<PlaysFragmentRowData> getData() {
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