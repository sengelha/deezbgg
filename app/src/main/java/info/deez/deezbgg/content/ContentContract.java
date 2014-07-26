package info.deez.deezbgg.content;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by sengelh on 7/25/14.
 */
public class ContentContract {
    public static final String SCHEME = "content";
    public static final String CONTENT_AUTHORITY = "info.deez.deezbgg.content";
    public static final Uri CONTENT_URI = Uri.parse(SCHEME + "://" + CONTENT_AUTHORITY);

    public static abstract class BoardGameEntry implements BaseColumns {
        public static final String TABLE_NAME = "boardGames";
        public static final Uri CONTENT_URI = Uri.withAppendedPath(ContentContract.CONTENT_URI, TABLE_NAME);
        // Use _ID for the board game id
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_YEAR_PUBLISHED = "yearPublished";
        public static final String COLUMN_NAME_THUMBNAIL_URL = "thumbnailUrl";
    }

    public static abstract class CollectionItemEntry implements BaseColumns {
        public static final String TABLE_NAME = "collectionItems";
        public static final Uri CONTENT_URI = Uri.withAppendedPath(ContentContract.CONTENT_URI, TABLE_NAME);
        // Use _ID for the collection item id
        public static final String COLUMN_NAME_BOARD_GAME_ID = "boardGameId";
        public static final String COLUMN_NAME_BOARD_GAME_NAME = "boardGameName";
        public static final String COLUMN_NAME_BOARD_GAME_YEAR_PUBLISHED = "boardGameYearPublished";
        public static final String COLUMN_NAME_BOARD_GAME_THUMBNAIL_URL = "boardGameThumbnailUrl";
    }

    public static abstract class PlayEntry implements BaseColumns {
        public static final String TABLE_NAME = "plays";
        public static final Uri CONTENT_URI = Uri.withAppendedPath(ContentContract.CONTENT_URI, TABLE_NAME);
        // Use _ID for the collection item id
        public static final String COLUMN_NAME_BOARD_GAME_ID = "boardGameId";
        public static final String COLUMN_NAME_BOARD_GAME_NAME = "boardGameName";
        public static final String COLUMN_NAME_BOARD_GAME_THUMBNAIL_URL = "boardGameThumbnailUrl";
        public static final String COLUMN_NAME_PLAY_DATE = "playDate";
    }
}
