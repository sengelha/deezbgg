package info.deez.deezbgg.sync;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.Context;
import android.content.OperationApplicationException;
import android.content.SyncResult;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.util.Pair;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import info.deez.deezbgg.content.ContentContract;
import info.deez.deezbgg.entity.BoardGame;
import info.deez.deezbgg.entity.CollectionItem;
import info.deez.deezbgg.utils.StringUtils;

public class SyncAdapter extends AbstractThreadedSyncAdapter {
    private static final String TAG = "SyncAdapter";
    private ContentResolver mContentResolver;

    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        mContentResolver = context.getContentResolver();
    }

    @Override
    public void onPerformSync(
            Account account,
            Bundle bundle,
            String s,
            ContentProviderClient contentProviderClient,
            SyncResult syncResult) {
        Log.i(TAG, "Beginning network synchronization");

        try {
            List<Pair<CollectionItem, BoardGame>> collection = BoardGameGeekApi.getCollectionForUser("aldie");

            ArrayList<ContentProviderOperation> batch = new ArrayList<ContentProviderOperation>();
            batch.addAll(getBoardGameUpdates(collection, syncResult));
            batch.addAll(getCollectionUpdates(collection, syncResult));

            Log.i(TAG, "Merge solution ready.  Applying batch update");
            mContentResolver.applyBatch(ContentContract.CONTENT_AUTHORITY, batch);
            mContentResolver.notifyChange(
                    ContentContract.BoardGameEntry.CONTENT_URI,
                    null,
                    false);
            mContentResolver.notifyChange(
                    ContentContract.CollectionItemEntry.CONTENT_URI,
                    null,
                    false);
        } catch (MalformedURLException e) {
            Log.wtf(TAG, "Feed URL is malformed", e);
            syncResult.stats.numParseExceptions++;
            return;
        } catch (IOException e) {
            Log.e(TAG, "Error reading from network: " + e.toString());
            syncResult.stats.numIoExceptions++;
            return;
        } catch (XmlPullParserException e) {
            Log.e(TAG, "Error parsing feed: " + e.toString());
            syncResult.stats.numParseExceptions++;
            return;
        } catch (RemoteException e) {
            Log.e(TAG, "Error updating database: " + e.toString());
            syncResult.databaseError = true;
            return;
        } catch (OperationApplicationException e) {
            Log.e(TAG, "Error updating database: " + e.toString());
            syncResult.databaseError = true;
            return;
        }

        Log.i(TAG, "Network synchronization complete");
    }

    private List<ContentProviderOperation> getBoardGameUpdates(List<Pair<CollectionItem, BoardGame>> collectionItems, SyncResult syncResult) throws RemoteException, OperationApplicationException {
        List<ContentProviderOperation> batch = new ArrayList<ContentProviderOperation>();

        HashMap<Long, BoardGame> boardGameMap = new HashMap<Long, BoardGame>();
        for (Pair<CollectionItem, BoardGame> e : collectionItems) {
            boardGameMap.put(e.second.id, e.second);
        }

        ContentResolver contentResolver = getContext().getContentResolver();
        Cursor c = contentResolver.query(
                ContentContract.BoardGameEntry.CONTENT_URI,
                new String[] { ContentContract.BoardGameEntry._ID, ContentContract.BoardGameEntry.COLUMN_NAME_NAME, ContentContract.BoardGameEntry.COLUMN_NAME_YEAR_PUBLISHED },
                null,
                null,
                null);
        while (c.moveToNext()) {
            syncResult.stats.numEntries++;

            long id = c.getLong(0);
            String name = c.getString(1);
            int yearPublished = c.getInt(2);

            BoardGame match = boardGameMap.get(id);
            if (match != null) {
                boardGameMap.remove(id);

                Uri existingUri = ContentContract.BoardGameEntry.CONTENT_URI
                        .buildUpon()
                        .appendPath(Long.toString(id))
                        .build();
                if (!StringUtils.areEqualOrBothNull(match.name, name) ||
                    match.yearPublished != yearPublished) {
                    Log.i(TAG, "Scheduling update: " + existingUri);
                    batch.add(ContentProviderOperation.newUpdate(existingUri)
                            .withValue(ContentContract.BoardGameEntry.COLUMN_NAME_NAME, name)
                            .withValue(ContentContract.BoardGameEntry.COLUMN_NAME_YEAR_PUBLISHED, yearPublished)
                            .build());
                    syncResult.stats.numUpdates++;
                } else {
                    Log.i(TAG, "No action: " + existingUri);
                }
            } else {
                Uri deleteUri = ContentContract.BoardGameEntry.CONTENT_URI
                        .buildUpon()
                        .appendPath(Long.toString(id))
                        .build();
                Log.i(TAG, "Scheduling delete: " + deleteUri);
                batch.add(ContentProviderOperation.newDelete(deleteUri).build());
                syncResult.stats.numDeletes++;
            }
        }
        c.close();

        for (BoardGame boardGame : boardGameMap.values()) {
            Log.i(TAG, "Scheduling insert: id=" + boardGame.id);
            batch.add(ContentProviderOperation.newInsert(ContentContract.BoardGameEntry.CONTENT_URI)
                    .withValue(ContentContract.BoardGameEntry._ID, boardGame.id)
                    .withValue(ContentContract.BoardGameEntry.COLUMN_NAME_NAME, boardGame.name)
                    .withValue(ContentContract.BoardGameEntry.COLUMN_NAME_YEAR_PUBLISHED, boardGame.yearPublished)
                    .build());
            syncResult.stats.numInserts++;
        }

        return batch;
    }

    private List<ContentProviderOperation> getCollectionUpdates(List<Pair<CollectionItem, BoardGame>> collectionItems, SyncResult syncResult) throws RemoteException, OperationApplicationException {
        List<ContentProviderOperation> batch = new ArrayList<ContentProviderOperation>();

        HashMap<Long, CollectionItem> collectionItemMap = new HashMap<Long, CollectionItem>();
        for (Pair<CollectionItem, BoardGame> e : collectionItems) {
            collectionItemMap.put(e.first.id, e.first);
        }

        ContentResolver contentResolver = getContext().getContentResolver();
        Cursor c = contentResolver.query(
                ContentContract.CollectionItemEntry.CONTENT_URI,
                new String[] { ContentContract.CollectionItemEntry._ID, ContentContract.CollectionItemEntry.COLUMN_NAME_BOARD_GAME_ID },
                null,
                null,
                null);
        while (c.moveToNext()) {
            syncResult.stats.numEntries++;

            long id = c.getLong(0);
            long boardGameId = c.getLong(1);

            CollectionItem match = collectionItemMap.get(id);
            if (match != null) {
                collectionItemMap.remove(id);

                Uri existingUri = ContentContract.CollectionItemEntry.CONTENT_URI
                        .buildUpon()
                        .appendPath(Long.toString(id))
                        .build();
                if (match.boardGameId != boardGameId) {
                    Log.i(TAG, "Scheduling update: " + existingUri);
                    batch.add(ContentProviderOperation.newUpdate(existingUri)
                        .withValue(ContentContract.CollectionItemEntry.COLUMN_NAME_BOARD_GAME_ID, boardGameId)
                        .build());
                    syncResult.stats.numUpdates++;
                } else {
                    Log.i(TAG, "No action: " + existingUri);
                }
            } else {
                Uri deleteUri = ContentContract.CollectionItemEntry.CONTENT_URI
                        .buildUpon()
                        .appendPath(Long.toString(id))
                        .build();
                Log.i(TAG, "Scheduling delete: " + deleteUri);
                batch.add(ContentProviderOperation.newDelete(deleteUri).build());
                syncResult.stats.numDeletes++;
            }
        }
        c.close();

        for (CollectionItem collectionItem : collectionItemMap.values()) {
            Log.i(TAG, "Scheduling insert: id=" + collectionItem.id);
            batch.add(ContentProviderOperation.newInsert(ContentContract.CollectionItemEntry.CONTENT_URI)
                    .withValue(ContentContract.CollectionItemEntry._ID, collectionItem.id)
                    .withValue(ContentContract.CollectionItemEntry.COLUMN_NAME_BOARD_GAME_ID, collectionItem.boardGameId)
                    .build());
            syncResult.stats.numInserts++;
        }

        return batch;
    }
}
