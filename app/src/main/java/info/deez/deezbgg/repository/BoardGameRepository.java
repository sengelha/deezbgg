package info.deez.deezbgg.repository;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;
import info.deez.deezbgg.entity.BoardGame;

public class BoardGameRepository {
    private DeezbggDbHelper mDbHelper;

    public BoardGameRepository(DeezbggDbHelper dbHelper) {
        mDbHelper = dbHelper;
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
}
