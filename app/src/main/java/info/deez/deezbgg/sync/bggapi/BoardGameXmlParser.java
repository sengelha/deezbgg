package info.deez.deezbgg.sync.bggapi;

import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import info.deez.deezbgg.utils.XmlParserUtils;

public class BoardGameXmlParser {
    private static final String TAG = "BoardGameXmlParser";
    // We don't use namespaces
    private static final String ns = null;

    public List<BoardGame> getBoardGames(InputStream stream) throws XmlPullParserException, IOException {
        List<BoardGame> boardGames = new ArrayList<BoardGame>();
        fillBoardGames(boardGames, stream);
        return boardGames;
    }

    public void fillBoardGames(List<BoardGame> boardGames, InputStream stream) throws XmlPullParserException, IOException {
        Log.i(TAG, "Parsing BGG board game XML feed...");
        XmlPullParser parser = Xml.newPullParser();
        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
        parser.setInput(stream, null);
        parser.nextTag();
        fillBoardGameFeed(boardGames, parser);
    }

    private void fillBoardGameFeed(List<BoardGame> boardGames, XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "items");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("item")) {
                boardGames.add(readBoardGameEntry(parser));
            } else {
                XmlParserUtils.skip(parser);
            }
        }
    }

    private BoardGame readBoardGameEntry(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "item");

        BoardGame boardGame = new BoardGame();
        boardGame.id = Long.parseLong(parser.getAttributeValue(null, "id"));

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("name")) {
                boardGame.nameEntries.add(readNameEntry(parser));
            } else if (name.equals("image")) {
                boardGame.imageUrl = XmlParserUtils.readInnerText(parser);
            } else if (name.equals("thumbnail")) {
                boardGame.thumbnailUrl = XmlParserUtils.readInnerText(parser);
            } else if (name.equals("yearpublished")) {
                boardGame.yearPublished = Integer.parseInt(parser.getAttributeValue(null, "value"));
                XmlParserUtils.skip(parser);
            } else {
                XmlParserUtils.skip(parser);
            }
        }
        parser.require(XmlPullParser.END_TAG, ns, "item");
        return boardGame;
    }

    private BoardGame.NameEntry readNameEntry(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "name");

        BoardGame.NameEntry nameEntry = new BoardGame.NameEntry();
        nameEntry.type = parser.getAttributeValue(null, "type");
        nameEntry.sortIndex = Integer.parseInt(parser.getAttributeValue(null, "sortindex"));
        nameEntry.name = parser.getAttributeValue(null, "value");

        XmlParserUtils.skip(parser);
        parser.require(XmlPullParser.END_TAG, ns, "name");

        return nameEntry;
    }
}
