package info.deez.deezbgg.repository;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DeezbggDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "deezbgg.db";

    public DeezbggDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DeezbggContract.DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DeezbggContract.SQL_CREATE_BOARD_GAMES);
        db.execSQL(DeezbggContract.SQL_CREATE_COLLECTION_ITEMS);
        db.execSQL(DeezbggContract.SQL_CREATE_PLAYS);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(DeezbggContract.SQL_DROP_BOARD_GAMES);
        db.execSQL(DeezbggContract.SQL_DROP_COLLECTION_ITEMS);
        db.execSQL(DeezbggContract.SQL_DROP_PLAYS);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}