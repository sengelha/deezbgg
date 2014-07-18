package info.deez.deezbgg.repository;

import android.provider.BaseColumns;

public final class DeezbggContract {
    private DeezbggContract() {}

    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 5;

    /* Inner class that defines the table contents */
    public static abstract class BoardGameEntry implements BaseColumns {
        public static final String TABLE_NAME = "boardGame";
        public static final String COLUMN_NAME_BOARD_GAME_ID = "boardGameId";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_THUMBNAIL_URL = "thumbnailUrl";
        public static final String COLUMN_NAME_IMAGE_URL = "imageUrl";
    }

    /* Inner class that defines the table contents */
    public static abstract class CollectionItemEntry implements BaseColumns {
        public static final String TABLE_NAME = "collectionItem";
        public static final String COLUMN_NAME_COLLECTION_ITEM_ID = "collectionItemId";
        public static final String COLUMN_NAME_BOARD_GAME_ID = "boardGameId";
    }

    /* Inner class that defines the table contents */
    public static abstract class PlayEntry implements BaseColumns {
        public static final String TABLE_NAME = "plays";
        public static final String COLUMN_NAME_PLAY_ID = "playId";
        public static final String COLUMN_NAME_PLAY_DATE = "playDate";
        public static final String COLUMN_NAME_BOARD_GAME_ID = "boardGameId";
    }

    public static final String SQL_CREATE_BOARD_GAMES =
            "CREATE TABLE " + BoardGameEntry.TABLE_NAME + "("
                    + BoardGameEntry._ID + " INTEGER PRIMARY KEY"
                    + "," + BoardGameEntry.COLUMN_NAME_BOARD_GAME_ID + " INTEGER"
                    + "," + BoardGameEntry.COLUMN_NAME_NAME + " TEXT"
                    + "," + BoardGameEntry.COLUMN_NAME_THUMBNAIL_URL + " TEXT"
                    + "," + BoardGameEntry.COLUMN_NAME_IMAGE_URL + " TEXT"
                    + ")";
    public static final String SQL_DROP_BOARD_GAMES =
            "DROP TABLE IF EXISTS " + BoardGameEntry.TABLE_NAME;

    public static final String SQL_CREATE_COLLECTION_ITEMS =
            "CREATE TABLE " + CollectionItemEntry.TABLE_NAME + "("
                    + CollectionItemEntry._ID + " INTEGER PRIMARY KEY"
                    + "," + CollectionItemEntry.COLUMN_NAME_COLLECTION_ITEM_ID + " INTEGER"
                    + "," + CollectionItemEntry.COLUMN_NAME_BOARD_GAME_ID + " INTEGER"
                    + ")";
    public static final String SQL_DROP_COLLECTION_ITEMS =
            "DROP TABLE IF EXISTS " + CollectionItemEntry.TABLE_NAME;

    public static final String SQL_CREATE_PLAYS =
            "CREATE TABLE " + PlayEntry.TABLE_NAME + "("
                    + PlayEntry._ID + " INTEGER PRIMARY KEY"
                    + "," + PlayEntry.COLUMN_NAME_PLAY_ID + " INTEGER"
                    + "," + PlayEntry.COLUMN_NAME_PLAY_DATE + " TEXT"
                    + "," + PlayEntry.COLUMN_NAME_BOARD_GAME_ID + " INTEGER"
                    + ")";
    public static final String SQL_DROP_PLAYS =
            "DROP TABLE IF EXISTS " + PlayEntry.TABLE_NAME;
}
