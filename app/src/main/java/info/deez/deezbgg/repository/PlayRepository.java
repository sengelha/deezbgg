package info.deez.deezbgg.repository;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.Collection;
import java.util.List;
import java.util.ArrayList;

import info.deez.deezbgg.entity.BoardGame;
import info.deez.deezbgg.entity.Play;

public class PlayRepository {
    private static final String TAG = "PlayRepository";
    private DeezbggDbHelper mDbHelper;

    public PlayRepository(DeezbggDbHelper dbHelper) {
        mDbHelper = dbHelper;
    }

    public void addPlay(Play play) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        addPlayHelper(db, play);
    }

    public void addPlays(Collection<Play> plays) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        for (Play play : plays) {
            addPlayHelper(db, play);
        }
    }

    private void addPlayHelper(SQLiteDatabase db, Play play) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DeezbggContract.PlayEntry.COLUMN_NAME_PLAY_ID, play.id);
        contentValues.put(DeezbggContract.PlayEntry.COLUMN_NAME_PLAY_DATE, play.date);
        contentValues.put(DeezbggContract.PlayEntry.COLUMN_NAME_BOARD_GAME_ID, play.boardGameId);
        db.insert(DeezbggContract.PlayEntry.TABLE_NAME, null, contentValues);
    }

    public void deleteAllPlays() {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        db.delete(DeezbggContract.PlayEntry.TABLE_NAME, null, null);
    }

    public List<Play> getAllPlays() {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] projection = {
                DeezbggContract.PlayEntry._ID,
                DeezbggContract.PlayEntry.COLUMN_NAME_PLAY_ID,
                DeezbggContract.PlayEntry.COLUMN_NAME_PLAY_DATE,
                DeezbggContract.PlayEntry.COLUMN_NAME_BOARD_GAME_ID
        };

        Cursor c = db.query(DeezbggContract.PlayEntry.TABLE_NAME, projection, null, null, null, null, null);

        List<Play> plays = new ArrayList<Play>();
        while (c.moveToNext()) {
            Play play = new Play();
            play.id = c.getLong(c.getColumnIndex(DeezbggContract.PlayEntry.COLUMN_NAME_PLAY_ID));
            play.date = c.getString(c.getColumnIndex(DeezbggContract.PlayEntry.COLUMN_NAME_PLAY_DATE));
            play.boardGameId = c.getLong(c.getColumnIndex(DeezbggContract.PlayEntry.COLUMN_NAME_BOARD_GAME_ID));
            plays.add(play);
        }
        return plays;
    }
}
