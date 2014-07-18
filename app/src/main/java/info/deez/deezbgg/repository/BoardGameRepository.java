package info.deez.deezbgg.repository;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;
import info.deez.deezbgg.entity.BoardGame;
import info.deez.deezbgg.entity.CollectionItem;

public class BoardGameRepository {
    private static final String TAG = "BoardGameRepository";
    private DeezbggDbHelper mDbHelper;

    public BoardGameRepository(DeezbggDbHelper dbHelper) {
        mDbHelper = dbHelper;
    }

    public void addBoardGame(BoardGame boardGame) {
        Log.i(TAG, "Adding board game id=" + boardGame.id + " name=" + boardGame.name);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(DeezbggContract.BoardGameEntry.COLUMN_NAME_BOARD_GAME_ID, boardGame.id);
        contentValues.put(DeezbggContract.BoardGameEntry.COLUMN_NAME_NAME, boardGame.name);
        if (boardGame.thumbnailUrl != null)
            contentValues.put(DeezbggContract.BoardGameEntry.COLUMN_NAME_THUMBNAIL_URL, boardGame.thumbnailUrl.toString());
        db.insert(DeezbggContract.BoardGameEntry.TABLE_NAME, null, contentValues);
    }

    public void addBoardGames(Collection<BoardGame> boardGames) {
        for (BoardGame boardGame : boardGames) {
            addBoardGame(boardGame);
        }
    }

    public void deleteAllBoardGames() {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        db.delete(DeezbggContract.BoardGameEntry.TABLE_NAME, null, null);
    }

    public List<BoardGame> getAllBoardGames() {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] projection = {
            DeezbggContract.BoardGameEntry._ID,
            DeezbggContract.BoardGameEntry.COLUMN_NAME_BOARD_GAME_ID,
            DeezbggContract.BoardGameEntry.COLUMN_NAME_NAME,
            DeezbggContract.BoardGameEntry.COLUMN_NAME_THUMBNAIL_URL,
            DeezbggContract.BoardGameEntry.COLUMN_NAME_IMAGE_URL
        };

        Cursor c = db.query(DeezbggContract.BoardGameEntry.TABLE_NAME, projection, null, null, null, null, null);

        List<BoardGame> boardGames = new ArrayList<BoardGame>();
        while (c.moveToNext()) {
            BoardGame boardGame = new BoardGame();
            boardGame.id = c.getLong(c.getColumnIndex(DeezbggContract.BoardGameEntry.COLUMN_NAME_BOARD_GAME_ID));
            boardGame.name = c.getString(c.getColumnIndex(DeezbggContract.BoardGameEntry.COLUMN_NAME_NAME));
            try {
                boardGame.thumbnailUrl = new URL(c.getString(c.getColumnIndex(DeezbggContract.BoardGameEntry.COLUMN_NAME_THUMBNAIL_URL)));
            } catch (MalformedURLException e) {
                Log.e(TAG, "Error parsing URL", e);
            }
            boardGames.add(boardGame);
        }
        return boardGames;
    }

    public BoardGame getBoardGameById(long id) {
        for (BoardGame boardGame : getAllBoardGames()) {
            if (boardGame.id == id)
                return boardGame;
        }
        return null;
    }

    public Dictionary<Long, BoardGame> getBoardGamesByIds(Collection<Long> ids) {
        Dictionary<Long, BoardGame> dict = new Hashtable<Long, BoardGame>();
        for (BoardGame boardGame : getAllBoardGames()) {
            if (ids.contains(boardGame.id)) {
                dict.put(boardGame.id, boardGame);
            }
        }
        return dict;
    }

    public void upsertBoardGame(BoardGame boardGame) {
        if (getBoardGameById(boardGame.id) == null)
            addBoardGame(boardGame);
    }
}
