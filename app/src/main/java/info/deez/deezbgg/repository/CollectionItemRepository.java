package info.deez.deezbgg.repository;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.List;
import java.util.ArrayList;
import info.deez.deezbgg.entity.CollectionItem;
import info.deez.deezbgg.entity.Play;

public class CollectionItemRepository {
    private DeezbggDbHelper mDbHelper;

    public CollectionItemRepository(DeezbggDbHelper dbHelper) {
        mDbHelper = dbHelper;
    }

    public List<CollectionItem> getAllCollectionItems() {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] projection = {
                DeezbggContract.CollectionItemEntry._ID,
                DeezbggContract.CollectionItemEntry.COLUMN_NAME_COLLECTION_ITEM_ID,
                DeezbggContract.CollectionItemEntry.COLUMN_NAME_BOARD_GAME_ID
        };

        Cursor c = db.query(DeezbggContract.CollectionItemEntry.TABLE_NAME, projection, null, null, null, null, null);

        List<CollectionItem> collectionItems = new ArrayList<CollectionItem>();
        while (c.moveToNext()) {
            CollectionItem collectionItem = new CollectionItem();
            collectionItem.id = c.getLong(c.getColumnIndex(DeezbggContract.CollectionItemEntry.COLUMN_NAME_COLLECTION_ITEM_ID));
            collectionItem.boardGameId = c.getLong(c.getColumnIndex(DeezbggContract.CollectionItemEntry.COLUMN_NAME_BOARD_GAME_ID));
            collectionItems.add(collectionItem);
        }
        return collectionItems;
    }
}