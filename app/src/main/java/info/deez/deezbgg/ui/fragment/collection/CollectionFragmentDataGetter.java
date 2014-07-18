package info.deez.deezbgg.ui.fragment.collection;

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
import info.deez.deezbgg.repository.BoardGameRepository;
import info.deez.deezbgg.repository.CollectionItemRepository;
import info.deez.deezbgg.repository.DeezbggDbHelper;

/**
 * Created by sengelh on 7/18/2014.
 */
public class CollectionFragmentDataGetter {
    private static final String TAG = "CollectionFragmentDataGetter";
    private CollectionItemRepository mCollectionItemRepository;
    private BoardGameRepository mBoardGameRepository;

    public CollectionFragmentDataGetter(DeezbggDbHelper dbHelper) {
        mCollectionItemRepository = new CollectionItemRepository(dbHelper);
        mBoardGameRepository = new BoardGameRepository(dbHelper);
    }

    public List<CollectionFragmentRowData> getData() {
        Log.i(TAG, "Starting to get data");
        List<CollectionItem> collectionItems = mCollectionItemRepository.getAllCollectionItems();
        Set<Long> boardGameIds = new HashSet<Long>();
        for (CollectionItem collectionItem : collectionItems) {
            boardGameIds.add(collectionItem.boardGameId);
        }
        Dictionary<Long, BoardGame> boardGames = mBoardGameRepository.getBoardGamesByIds(boardGameIds);

        List<CollectionFragmentRowData> rows = new ArrayList<CollectionFragmentRowData>(collectionItems.size());
        for (CollectionItem collectionItem : collectionItems) {
            CollectionFragmentRowData row = new CollectionFragmentRowData();
            row.collectionItem = collectionItem;
            row.boardGame = boardGames.get(row.collectionItem.boardGameId);
            rows.add(row);
        }

        Collections.sort(rows, new Comparator<CollectionFragmentRowData>() {
            @Override
            public int compare(CollectionFragmentRowData lhs, CollectionFragmentRowData rhs) {
                String leftName = (lhs != null && lhs.boardGame != null ? lhs.boardGame.name : null);
                String rightName = (rhs != null && rhs.boardGame != null ? rhs.boardGame.name : null);
                if (leftName == null && rightName == null)
                    return 0;
                else if (leftName == null)
                    return 1;
                else if (rightName == null)
                    return -1;
                else
                    return leftName.compareTo(rightName);
            }
        });

        Log.i(TAG, "Finished load in background.  Loaded " + rows.size() + " rows");
        return rows;
    }
}
