package info.deez.deezbgg.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "deezbgg.db";

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DbContract.DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DbContract.SQL_CREATE_BOARD_GAMES);
        db.execSQL(DbContract.SQL_CREATE_COLLECTION_ITEMS);
        db.execSQL(DbContract.SQL_CREATE_PLAYS);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(DbContract.SQL_DROP_BOARD_GAMES);
        db.execSQL(DbContract.SQL_DROP_COLLECTION_ITEMS);
        db.execSQL(DbContract.SQL_DROP_PLAYS);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}