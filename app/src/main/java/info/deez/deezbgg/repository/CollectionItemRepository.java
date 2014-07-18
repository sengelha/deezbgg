package info.deez.deezbgg.repository;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.Collection;
import java.util.List;
import java.util.ArrayList;

import info.deez.deezbgg.entity.BoardGame;
import info.deez.deezbgg.entity.CollectionItem;
import info.deez.deezbgg.entity.Play;

public class CollectionItemRepository {
    private static final String TAG = "CollectionItemRepository";
    private DeezbggDbHelper mDbHelper;

    public CollectionItemRepository(DeezbggDbHelper dbHelper) {
        mDbHelper = dbHelper;
    }

    public void addCollectionItem(CollectionItem collectionItem) {
        Log.i(TAG, "Adding collection item id=" + collectionItem.id + " boardGameId=" + collectionItem.boardGameId);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(DeezbggContract.CollectionItemEntry.COLUMN_NAME_COLLECTION_ITEM_ID, collectionItem.id);
        contentValues.put(DeezbggContract.CollectionItemEntry.COLUMN_NAME_BOARD_GAME_ID, collectionItem.boardGameId);
        db.insert(DeezbggContract.CollectionItemEntry.TABLE_NAME, null, contentValues);
    }

    public void addCollectionItems(Collection<CollectionItem> collectionItems) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        for (CollectionItem collectionItem : collectionItems) {
            addCollectionItem(collectionItem);
        }
    }

    public void deleteAllCollectionItems() {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        db.delete(DeezbggContract.CollectionItemEntry.TABLE_NAME, null, null);
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

    public CollectionItem getCollectionItemById(long id) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] projection = {
                DeezbggContract.CollectionItemEntry._ID,
                DeezbggContract.CollectionItemEntry.COLUMN_NAME_COLLECTION_ITEM_ID,
                DeezbggContract.CollectionItemEntry.COLUMN_NAME_BOARD_GAME_ID
        };

        Cursor c = db.query
            (
            DeezbggContract.CollectionItemEntry.TABLE_NAME,
            projection,
            DeezbggContract.CollectionItemEntry.COLUMN_NAME_COLLECTION_ITEM_ID + "=?",
            new String[] { Long.toString(id) },
            null,
            null,
            null
            );

        if (!c.moveToNext())
            return null;

        CollectionItem collectionItem = new CollectionItem();
        collectionItem.id = c.getLong(c.getColumnIndex(DeezbggContract.CollectionItemEntry.COLUMN_NAME_COLLECTION_ITEM_ID));
        collectionItem.boardGameId = c.getLong(c.getColumnIndex(DeezbggContract.CollectionItemEntry.COLUMN_NAME_BOARD_GAME_ID));
        return collectionItem;
    }

    public void upsertCollectionItem(CollectionItem collectionItem) {
        if (getCollectionItemById(collectionItem.id) == null)
            addCollectionItem(collectionItem);
    }
}