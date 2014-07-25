package info.deez.deezbgg.content;

import android.app.NotificationManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import java.util.Map;

import info.deez.deezbgg.db.DbContract;
import info.deez.deezbgg.db.DbHelper;

public class ContentProvider extends android.content.ContentProvider {
    private static final String TAG = "DeezbggContentProvider";
    private static final int ROUTE_COLLECTION_ITEMS = 1;
    private static final int ROUTE_COLLECTION_ITEM = 2;
    private static final int ROUTE_BOARD_GAMES = 3;
    private static final int ROUTE_BOARD_GAME = 4;
    private static final int ROUTE_INVALID = -1;
    private static final UriMatcher sUriMatcher;
    private DbHelper mDbHelper;

    static {
        sUriMatcher = new UriMatcher(0);
        sUriMatcher.addURI(
                ContentContract.CONTENT_AUTHORITY,
                ContentContract.CollectionItemEntry.TABLE_NAME,
                ROUTE_COLLECTION_ITEMS);
        sUriMatcher.addURI(
                ContentContract.CONTENT_AUTHORITY,
                ContentContract.CollectionItemEntry.TABLE_NAME + "/*",
                ROUTE_COLLECTION_ITEM);
        sUriMatcher.addURI(
                ContentContract.CONTENT_AUTHORITY,
                ContentContract.BoardGameEntry.TABLE_NAME,
                ROUTE_BOARD_GAMES);
        sUriMatcher.addURI(
                ContentContract.CONTENT_AUTHORITY,
                ContentContract.BoardGameEntry.TABLE_NAME + "/*",
                ROUTE_BOARD_GAME);
    }
    @Override
    public boolean onCreate() {
        Log.i(TAG, "In DeezbggContentProvider.onCreate()");
        mDbHelper = new DbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Log.i(TAG, "In DeezbggContentProvider.query()");

        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        switch (sUriMatcher.match(uri)) {
            case ROUTE_COLLECTION_ITEMS: {
                StringBuilder sb = new StringBuilder();
                sb.append("SELECT ");
                boolean first = true;
                for (String column : projection) {
                    if (!first)
                        sb.append(", ");
                    if (column.equals(ContentContract.CollectionItemEntry._ID)) {
                        sb.append("CI.");
                        sb.append(DbContract.CollectionItemEntry._ID);
                        sb.append(" AS ");
                        sb.append(ContentContract.CollectionItemEntry._ID);
                    } else if (column.equals(ContentContract.CollectionItemEntry.COLUMN_NAME_BOARD_GAME_ID)) {
                        sb.append("CI.");
                        sb.append(DbContract.CollectionItemEntry.COLUMN_NAME_BOARD_GAME_ID);
                        sb.append(" AS ");
                        sb.append(ContentContract.CollectionItemEntry.COLUMN_NAME_BOARD_GAME_ID);
                    } else if (column.equals(ContentContract.CollectionItemEntry.COLUMN_NAME_BOARD_GAME_NAME)) {
                        sb.append("BG.");
                        sb.append(DbContract.BoardGameEntry.COLUMN_NAME_NAME);
                        sb.append(" AS ");
                        sb.append(ContentContract.CollectionItemEntry.COLUMN_NAME_BOARD_GAME_NAME);
                    } else if (column.equals(ContentContract.CollectionItemEntry.COLUMN_NAME_BOARD_GAME_YEAR_PUBLISHED)) {
                        sb.append("BG.");
                        sb.append(DbContract.BoardGameEntry.COLUMN_NAME_YEAR_PUBLISHED);
                        sb.append(" AS ");
                        sb.append(ContentContract.CollectionItemEntry.COLUMN_NAME_BOARD_GAME_YEAR_PUBLISHED);
                    } else {
                        throw new UnsupportedOperationException("Unhandled column: " + column);
                    }
                    first = false;
                }
                sb.append(" FROM ");
                sb.append(DbContract.CollectionItemEntry.TABLE_NAME);
                sb.append(" CI LEFT OUTER JOIN ");
                sb.append(DbContract.BoardGameEntry.TABLE_NAME);
                sb.append(" BG ON BG.");
                sb.append(DbContract.BoardGameEntry._ID);
                sb.append("=CI.");
                sb.append(DbContract.CollectionItemEntry.COLUMN_NAME_BOARD_GAME_ID);
                if (sortOrder == null) {
                    // Do nothing
                } else if (sortOrder.equals(ContentContract.CollectionItemEntry.COLUMN_NAME_BOARD_GAME_NAME)) {
                    sb.append(" ORDER BY BG.");
                    sb.append(DbContract.BoardGameEntry.COLUMN_NAME_NAME);
                } else {
                    throw new UnsupportedOperationException("Unhandled sort order: " + sortOrder);
                }
                Log.i(TAG, "About to execute: " + sb.toString());
                return db.rawQuery(sb.toString(), null);
            }
            case ROUTE_BOARD_GAMES: {
                StringBuilder sb = new StringBuilder();
                sb.append("SELECT ");
                boolean first = true;
                for (String column : projection) {
                    if (!first)
                        sb.append(", ");
                    if (column.equals(ContentContract.BoardGameEntry._ID)) {
                        sb.append("BG.");
                        sb.append(DbContract.BoardGameEntry._ID);
                        sb.append(" AS ");
                        sb.append(ContentContract.BoardGameEntry._ID);
                    } else if (column.equals(ContentContract.BoardGameEntry.COLUMN_NAME_NAME)) {
                        sb.append("BG.");
                        sb.append(DbContract.BoardGameEntry.COLUMN_NAME_NAME);
                        sb.append(" AS ");
                        sb.append(ContentContract.BoardGameEntry.COLUMN_NAME_NAME);
                    } else if (column.equals(ContentContract.BoardGameEntry.COLUMN_NAME_YEAR_PUBLISHED)) {
                        sb.append("BG.");
                        sb.append(DbContract.BoardGameEntry.COLUMN_NAME_YEAR_PUBLISHED);
                        sb.append(" AS ");
                        sb.append(ContentContract.BoardGameEntry.COLUMN_NAME_YEAR_PUBLISHED);
                    } else {
                        throw new UnsupportedOperationException("Unhandled column: " + column);
                    }
                    first = false;
                }
                sb.append(" FROM ");
                sb.append(DbContract.BoardGameEntry.TABLE_NAME);
                sb.append(" BG");
                return db.rawQuery(sb.toString(), null);
            }
            default:
                throw new IllegalArgumentException("Query -- Invalid URI: " + uri);
        }
    }

    @Override
    public String getType(Uri uri) {
        throw new RuntimeException("TODO");
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        Uri result;
        switch (sUriMatcher.match(uri)) {
            case ROUTE_COLLECTION_ITEMS: {
                ContentValues dbContentValues = new ContentValues();

                for (Map.Entry<String, Object> e : contentValues.valueSet()) {
                    if (e.getKey().equals(ContentContract.CollectionItemEntry._ID)) {
                        dbContentValues.put(DbContract.CollectionItemEntry._ID, (Long) e.getValue());
                    } else if (e.getKey().equals(ContentContract.CollectionItemEntry.COLUMN_NAME_BOARD_GAME_ID)) {
                        dbContentValues.put(DbContract.CollectionItemEntry.COLUMN_NAME_BOARD_GAME_ID, (Long) e.getValue());
                    } else {
                        throw new UnsupportedOperationException("Unhandled key: " + e.getKey());
                    }
                }

                long id = db.insertOrThrow(DbContract.CollectionItemEntry.TABLE_NAME, null, dbContentValues);
                result = Uri.withAppendedPath(ContentContract.CollectionItemEntry.CONTENT_URI, Long.toString(id));
                break;
            }
            case ROUTE_BOARD_GAMES: {
                ContentValues dbContentValues = new ContentValues();

                for (Map.Entry<String, Object> e : contentValues.valueSet()) {
                    if (e.getKey().equals(ContentContract.BoardGameEntry._ID)) {
                        dbContentValues.put(DbContract.BoardGameEntry._ID, (Long) e.getValue());
                    } else if (e.getKey().equals(ContentContract.BoardGameEntry.COLUMN_NAME_NAME)) {
                        dbContentValues.put(DbContract.BoardGameEntry.COLUMN_NAME_NAME, (String) e.getValue());
                    } else if (e.getKey().equals(ContentContract.BoardGameEntry.COLUMN_NAME_YEAR_PUBLISHED)) {
                        dbContentValues.put(DbContract.BoardGameEntry.COLUMN_NAME_YEAR_PUBLISHED, (Integer) e.getValue());
                    } else {
                        throw new UnsupportedOperationException("Unhandled key: " + e.getKey());
                    }
                }

                long id = db.insertOrThrow(DbContract.BoardGameEntry.TABLE_NAME, null, dbContentValues);
                result = Uri.withAppendedPath(ContentContract.BoardGameEntry.CONTENT_URI, Long.toString(id));
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        Context ctxt = getContext();
        ctxt.getContentResolver().notifyChange(uri, null, false);
        return result;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int count;
        switch (sUriMatcher.match(uri)) {
            case ROUTE_COLLECTION_ITEM: {
                String id = uri.getLastPathSegment();
                count = db.delete(
                        DbContract.CollectionItemEntry.TABLE_NAME,
                        DbContract.CollectionItemEntry._ID + "=?",
                        new String[]{id});
                break;
            }
            case ROUTE_BOARD_GAME: {
                String id = uri.getLastPathSegment();
                count = db.delete(
                        DbContract.BoardGameEntry.TABLE_NAME,
                        DbContract.BoardGameEntry._ID + "=?",
                        new String[]{id});
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        Context ctx = getContext();
        ctx.getContentResolver().notifyChange(uri, null, false);
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int count;
        switch (sUriMatcher.match(uri)) {
            case ROUTE_COLLECTION_ITEM: {
                ContentValues dbContentValues = new ContentValues();
                for (Map.Entry<String, Object> e : contentValues.valueSet()) {
                    if (e.getKey().equals(ContentContract.CollectionItemEntry._ID)) {
                        // Do nothing
                    } else if (e.getKey().equals(ContentContract.CollectionItemEntry.COLUMN_NAME_BOARD_GAME_ID)) {
                        dbContentValues.put(DbContract.CollectionItemEntry.COLUMN_NAME_BOARD_GAME_ID, (Long) e.getValue());
                    } else {
                        throw new UnsupportedOperationException("Unhandled key: " + e.getKey());
                    }
                }

                String id = uri.getLastPathSegment();
                count = db.update(DbContract.CollectionItemEntry.TABLE_NAME,
                        dbContentValues,
                        DbContract.CollectionItemEntry._ID + "=?",
                        new String[]{id});
                break;
            }
            case ROUTE_BOARD_GAME: {
                ContentValues dbContentValues = new ContentValues();

                for (Map.Entry<String, Object> e : contentValues.valueSet()) {
                    if (e.getKey().equals(ContentContract.BoardGameEntry._ID)) {
                        // Do nothing
                    } else if (e.getKey().equals(ContentContract.BoardGameEntry.COLUMN_NAME_NAME)) {
                        dbContentValues.put(DbContract.BoardGameEntry.COLUMN_NAME_NAME, (String) e.getValue());
                    } else if (e.getKey().equals(ContentContract.BoardGameEntry.COLUMN_NAME_YEAR_PUBLISHED)) {
                        dbContentValues.put(DbContract.BoardGameEntry.COLUMN_NAME_YEAR_PUBLISHED, (String) e.getValue());
                    } else {
                        throw new UnsupportedOperationException("Unhandled key: " + e.getKey());
                    }
                }

                String id = uri.getLastPathSegment();
                count = db.update(DbContract.BoardGameEntry.TABLE_NAME,
                        dbContentValues,
                        DbContract.BoardGameEntry._ID + "=?",
                        new String[]{id});
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        Context ctx = getContext();
        ctx.getContentResolver().notifyChange(uri, null, false);
        return count;
    }
}
