package info.deez.deezbgg.bggapi;

import android.util.Log;
import android.util.Pair;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import info.deez.deezbgg.entity.BoardGame;
import info.deez.deezbgg.entity.CollectionItem;

public class BoardGameGeekXmlParser {
    private static final String TAG = "BoardGameGeekXmlParser";
    // We don't use namespaces
    private static final String ns = null;

    public List<Pair<CollectionItem, BoardGame>> parse(InputStream stream) throws XmlPullParserException, IOException {
        Log.i(TAG, "Parsing BGG XML feed...");
        XmlPullParser parser = Xml.newPullParser();
        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
        parser.setInput(stream, null);
        parser.nextTag();
        return readFeed(parser);
    }

    private List<Pair<CollectionItem, BoardGame>> readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
        List<Pair<CollectionItem, BoardGame>> entries = new ArrayList<Pair<CollectionItem, BoardGame>>();

        parser.require(XmlPullParser.START_TAG, ns, "items");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("item")) {
                entries.add(readEntry(parser));
            } else {
                skip(parser);
            }
        }
        return entries;
    }

    private Pair<CollectionItem, BoardGame> readEntry(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "item");

        CollectionItem collectionItem = new CollectionItem();
        collectionItem.id = Long.parseLong(parser.getAttributeValue(null, "collid"));
        collectionItem.boardGameId = Long.parseLong(parser.getAttributeValue(null, "objectid"));
        BoardGame boardGame = new BoardGame();
        boardGame.id = collectionItem.boardGameId;

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("name")) {
                boardGame.name = readName(parser);
            } else if (name.equals("thumbnail")) {
                boardGame.thumbnailUrl = readThumbnail(parser);
            } else {
                skip(parser);
            }
        }
        parser.require(XmlPullParser.END_TAG, ns, "item");
        return Pair.create(collectionItem, boardGame);
    }

    private String readName(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "name");
        String name = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "name");
        return name;
    }

    private URL readThumbnail(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "thumbnail");
        String thumbnail = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "thumbnail");
        return new URL(thumbnail);
    }

    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }

    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }
}