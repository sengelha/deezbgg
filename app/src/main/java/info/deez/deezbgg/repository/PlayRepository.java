package info.deez.deezbgg.repository;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.List;
import java.util.ArrayList;

import info.deez.deezbgg.entity.BoardGame;
import info.deez.deezbgg.entity.Play;

public class PlayRepository {
    private DeezbggDbHelper mDbHelper;

    public PlayRepository(DeezbggDbHelper dbHelper) {
        mDbHelper = dbHelper;
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
