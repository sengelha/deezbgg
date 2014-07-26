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
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import info.deez.deezbgg.content.ContentContract;
import info.deez.deezbgg.entity.BoardGame;
import info.deez.deezbgg.entity.CollectionItem;
import info.deez.deezbgg.entity.Play;
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
            List<Pair<CollectionItem, BoardGame>> collection = BoardGameGeekApi.getCollectionForUser("sengelha");
            List<Pair<Play, BoardGame>> plays = BoardGameGeekApi.getPlaysForUser("sengelha");

            ArrayList<ContentProviderOperation> batch = new ArrayList<ContentProviderOperation>();
            Set<BoardGame> boardGames = new HashSet<BoardGame>();
            for (Pair<CollectionItem, BoardGame> e : collection) {
                boardGames.add(e.second);
            }
            for (Pair<Play, BoardGame> e : plays) {
                boardGames.add(e.second);
            }
            batch.addAll(getBoardGameUpdates(boardGames, syncResult));
            batch.addAll(getCollectionUpdates(collection, syncResult));
            batch.addAll(getPlayUpdates(plays, syncResult));

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

    private List<ContentProviderOperation> getBoardGameUpdates(Collection<BoardGame> boardGames, SyncResult syncResult) throws RemoteException, OperationApplicationException {
        List<ContentProviderOperation> batch = new ArrayList<ContentProviderOperation>();

        HashMap<Long, BoardGame> boardGameMap = new HashMap<Long, BoardGame>();
        for (BoardGame boardGame : boardGames) {
            boardGameMap.put(boardGame.id, boardGame);
        }

        String[] projection = new String[] {
                ContentContract.BoardGameEntry._ID,
                ContentContract.BoardGameEntry.COLUMN_NAME_NAME,
                ContentContract.BoardGameEntry.COLUMN_NAME_YEAR_PUBLISHED,
                ContentContract.BoardGameEntry.COLUMN_NAME_THUMBNAIL_URL,
        };

        ContentResolver contentResolver = getContext().getContentResolver();
        Cursor c = contentResolver.query(
                ContentContract.BoardGameEntry.CONTENT_URI,
                projection,
                null,
                null,
                null);
        while (c.moveToNext()) {
            syncResult.stats.numEntries++;

            long localId = c.getLong(0);
            String localName = c.getString(1);
            int localYearPublished = c.getInt(2);
            String localThumbnailUrl = c.getString(3);

            BoardGame remoteBoardGame = boardGameMap.get(localId);
            if (remoteBoardGame != null) {
                boardGameMap.remove(localId);

                Uri existingUri = ContentContract.BoardGameEntry.CONTENT_URI
                        .buildUpon()
                        .appendPath(Long.toString(localId))
                        .build();
                if (!StringUtils.areEqualOrBothNull(remoteBoardGame.name, localName) ||
                    (remoteBoardGame.isYearPublishedSet() && remoteBoardGame.getYearPublished() != localYearPublished) ||
                    (remoteBoardGame.isThumbnailUrlSet() && !StringUtils.areEqualOrBothNull(remoteBoardGame.getThumbnailUrl(), localThumbnailUrl))) {
                    Log.i(TAG, "Scheduling update: " + existingUri);

                    ContentProviderOperation.Builder builder = ContentProviderOperation.newUpdate(existingUri)
                            .withValue(ContentContract.BoardGameEntry.COLUMN_NAME_NAME, remoteBoardGame.name);
                    if (remoteBoardGame.isThumbnailUrlSet())
                        builder = builder.withValue(ContentContract.BoardGameEntry.COLUMN_NAME_THUMBNAIL_URL, remoteBoardGame.getThumbnailUrl());
                    if (remoteBoardGame.isYearPublishedSet())
                        builder = builder.withValue(ContentContract.BoardGameEntry.COLUMN_NAME_YEAR_PUBLISHED, remoteBoardGame.getYearPublished());
                    batch.add(builder.build());
                    syncResult.stats.numUpdates++;
                } else {
                    Log.i(TAG, "No action: " + existingUri);
                }
            } else {
                Uri deleteUri = ContentContract.BoardGameEntry.CONTENT_URI
                        .buildUpon()
                        .appendPath(Long.toString(localId))
                        .build();
                Log.i(TAG, "Scheduling delete: " + deleteUri);
                batch.add(ContentProviderOperation.newDelete(deleteUri).build());
                syncResult.stats.numDeletes++;
            }
        }
        c.close();

        for (BoardGame boardGame : boardGameMap.values()) {
            Log.i(TAG, "Scheduling insert: id=" + boardGame.id);

            ContentProviderOperation.Builder builder = ContentProviderOperation.newInsert(ContentContract.BoardGameEntry.CONTENT_URI)
                    .withValue(ContentContract.BoardGameEntry._ID, boardGame.id)
                    .withValue(ContentContract.BoardGameEntry.COLUMN_NAME_NAME, boardGame.name);
            if (boardGame.isThumbnailUrlSet())
                builder = builder.withValue(ContentContract.BoardGameEntry.COLUMN_NAME_THUMBNAIL_URL, boardGame.getThumbnailUrl());
            if (boardGame.isYearPublishedSet())
                builder = builder.withValue(ContentContract.BoardGameEntry.COLUMN_NAME_YEAR_PUBLISHED, boardGame.getYearPublished());
            batch.add(builder.build());

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

        String[] projection = new String[] {
            ContentContract.CollectionItemEntry._ID,
            ContentContract.CollectionItemEntry.COLUMN_NAME_BOARD_GAME_ID
        };

        ContentResolver contentResolver = getContext().getContentResolver();
        Cursor c = contentResolver.query(
                ContentContract.CollectionItemEntry.CONTENT_URI,
                projection,
                null,
                null,
                null);
        while (c.moveToNext()) {
            syncResult.stats.numEntries++;

            long localId = c.getLong(0);
            long localBoardGameId = c.getLong(1);

            CollectionItem remoteCollectionItem = collectionItemMap.get(localId);
            if (remoteCollectionItem != null) {
                collectionItemMap.remove(localId);

                Uri existingUri = ContentContract.CollectionItemEntry.CONTENT_URI
                        .buildUpon()
                        .appendPath(Long.toString(localId))
                        .build();
                if (remoteCollectionItem.boardGameId != localBoardGameId) {
                    Log.i(TAG, "Scheduling update: " + existingUri);
                    batch.add(ContentProviderOperation.newUpdate(existingUri)
                        .withValue(ContentContract.CollectionItemEntry.COLUMN_NAME_BOARD_GAME_ID, remoteCollectionItem.boardGameId)
                        .build());
                    syncResult.stats.numUpdates++;
                } else {
                    Log.i(TAG, "No action: " + existingUri);
                }
            } else {
                Uri deleteUri = ContentContract.CollectionItemEntry.CONTENT_URI
                        .buildUpon()
                        .appendPath(Long.toString(localId))
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

    private List<ContentProviderOperation> getPlayUpdates(List<Pair<Play, BoardGame>> plays, SyncResult syncResult) throws RemoteException, OperationApplicationException {
        List<ContentProviderOperation> batch = new ArrayList<ContentProviderOperation>();

        HashMap<Long, Play> playMap = new HashMap<Long, Play>();
        for (Pair<Play, BoardGame> e : plays) {
            playMap.put(e.first.id, e.first);
        }

        String[] projection = new String[] {
                ContentContract.PlayEntry._ID,
                ContentContract.PlayEntry.COLUMN_NAME_BOARD_GAME_ID,
                ContentContract.PlayEntry.COLUMN_NAME_PLAY_DATE
        };

        ContentResolver contentResolver = getContext().getContentResolver();
        Cursor c = contentResolver.query(
                ContentContract.PlayEntry.CONTENT_URI,
                projection,
                null,
                null,
                null);
        while (c.moveToNext()) {
            syncResult.stats.numEntries++;

            long localId = c.getLong(0);
            long localBoardGameId = c.getLong(1);
            String localPlayDate = c.getString(2);

            Play remotePlay = playMap.get(localId);
            if (remotePlay != null) {
                playMap.remove(localId);

                Uri existingUri = ContentContract.PlayEntry.CONTENT_URI
                        .buildUpon()
                        .appendPath(Long.toString(localId))
                        .build();
                if (remotePlay.boardGameId != localBoardGameId ||
                    !StringUtils.areEqualOrBothNull(remotePlay.date, localPlayDate)) {
                    Log.i(TAG, "Scheduling update: " + existingUri);
                    batch.add(ContentProviderOperation.newUpdate(existingUri)
                            .withValue(ContentContract.PlayEntry.COLUMN_NAME_BOARD_GAME_ID, remotePlay.boardGameId)
                            .withValue(ContentContract.PlayEntry.COLUMN_NAME_PLAY_DATE, remotePlay.date)
                            .build());
                    syncResult.stats.numUpdates++;
                } else {
                    Log.i(TAG, "No action: " + existingUri);
                }
            } else {
                Uri deleteUri = ContentContract.PlayEntry.CONTENT_URI
                        .buildUpon()
                        .appendPath(Long.toString(localId))
                        .build();
                Log.i(TAG, "Scheduling delete: " + deleteUri);
                batch.add(ContentProviderOperation.newDelete(deleteUri).build());
                syncResult.stats.numDeletes++;
            }
        }
        c.close();

        for (Play play : playMap.values()) {
            Log.i(TAG, "Scheduling insert: id=" + play.id);
            batch.add(ContentProviderOperation.newInsert(ContentContract.PlayEntry.CONTENT_URI)
                    .withValue(ContentContract.PlayEntry._ID, play.id)
                    .withValue(ContentContract.PlayEntry.COLUMN_NAME_BOARD_GAME_ID, play.boardGameId)
                    .withValue(ContentContract.PlayEntry.COLUMN_NAME_PLAY_DATE, play.date)
                    .build());
            syncResult.stats.numInserts++;
        }

        return batch;
    }
}
