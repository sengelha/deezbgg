package info.deez.deezbgg.sync.bggapi;

import android.util.Log;
import android.util.Pair;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import info.deez.deezbgg.utils.CollectionUtils;

public class BoardGameGeekApi {
    private static final String TAG = "BoardGameGeekApi";
    private static final int BOARD_GAME_BATCH_SIZE = 10;

    public static List<CollectionItem> getCollectionForUser(String username) throws IOException, XmlPullParserException {
        URL url = new URL("http://boardgamegeek.com/xmlapi2/collection?username=" + username);
        URLConnection conn = url.openConnection();
        CollectionXmlParser parser = new CollectionXmlParser();

        List<CollectionItem> apiResults;
        Log.i(TAG, "Calling " + url);
        InputStream stream = conn.getInputStream();
        try {
            apiResults = parser.parseCollection(stream);
        } finally {
            stream.close();
        }

        return apiResults;
    }

    public static List<Play> getPlaysForUser(String username) throws IOException, XmlPullParserException {
        URL url = new URL("http://boardgamegeek.com/xmlapi2/plays?username=" + username);
        URLConnection conn = url.openConnection();
        PlayXmlParser parser = new PlayXmlParser();

        List<Play> apiResults;
        Log.i(TAG, "Calling " + url);
        InputStream stream = conn.getInputStream();
        try {
            apiResults = parser.parsePlays(stream);
        } finally {
            stream.close();
        }

        return apiResults;
    }

    public static List<BoardGame> getBoardGamesByIds(Collection<Long> boardGameIds) throws IOException, XmlPullParserException {
        List<BoardGame> boardGames = new ArrayList<BoardGame>();
        for (Collection<Long> batch : CollectionUtils.divideIntoBatches(boardGameIds, BOARD_GAME_BATCH_SIZE)) {
            fillBoardGamesByIds(boardGames, batch);
        }
        return boardGames;
    }

    private static void fillBoardGamesByIds(List<BoardGame> boardGames, Collection<Long> boardGameIds) throws IOException, XmlPullParserException {
        StringBuilder sb = new StringBuilder();
        sb.append("http://boardgamegeek.com/xmlapi2/thing?id=");
        boolean first = true;
        for (Long boardGameId : boardGameIds) {
            if (!first)
                sb.append(",");
            sb.append(boardGameId);
            first = false;
        }

        URL url = new URL(sb.toString());
        URLConnection conn = url.openConnection();
        BoardGameXmlParser parser = new BoardGameXmlParser();

        Log.i(TAG, "Calling " + url);
        InputStream stream = conn.getInputStream();
        try {
            parser.fillBoardGames(boardGames, stream);
        } finally {
            stream.close();
        }
    }
}
