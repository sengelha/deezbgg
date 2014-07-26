package info.deez.deezbgg.sync;

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
import info.deez.deezbgg.entity.Play;

public class BoardGameGeekXmlParser {
    private static final String TAG = "BoardGameGeekXmlParser";
    // We don't use namespaces
    private static final String ns = null;

    public List<Pair<CollectionItem, BoardGame>> parseCollection(InputStream stream) throws XmlPullParserException, IOException {
        Log.i(TAG, "Parsing BGG collection XML feed...");
        XmlPullParser parser = Xml.newPullParser();
        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
        parser.setInput(stream, null);
        parser.nextTag();
        return readCollectionFeed(parser);
    }

    public List<Pair<Play, BoardGame>> parsePlays(InputStream stream) throws XmlPullParserException, IOException {
        Log.i(TAG, "Parsing BGG plays XML feed...");
        XmlPullParser parser = Xml.newPullParser();
        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
        parser.setInput(stream, null);
        parser.nextTag();
        return readPlayFeed(parser);
    }

    private List<Pair<CollectionItem, BoardGame>> readCollectionFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
        List<Pair<CollectionItem, BoardGame>> entries = new ArrayList<Pair<CollectionItem, BoardGame>>();

        parser.require(XmlPullParser.START_TAG, ns, "items");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("item")) {
                entries.add(readCollectionEntry(parser));
            } else {
                skip(parser);
            }
        }
        return entries;
    }

    private List<Pair<Play, BoardGame>> readPlayFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
        List<Pair<Play, BoardGame>> entries = new ArrayList<Pair<Play, BoardGame>>();

        parser.require(XmlPullParser.START_TAG, ns, "plays");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("play")) {
                entries.add(readPlayEntry(parser));
            } else {
                skip(parser);
            }
        }
        return entries;
    }

    private Pair<CollectionItem, BoardGame> readCollectionEntry(XmlPullParser parser) throws XmlPullParserException, IOException {
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
                boardGame.thumbnailUrl = readThumbnailUrl(parser);
            } else if (name.equals("yearpublished")) {
                boardGame.yearPublished = readYearPublished(parser);
            } else {
                skip(parser);
            }
        }
        parser.require(XmlPullParser.END_TAG, ns, "item");
        return Pair.create(collectionItem, boardGame);
    }

    private Pair<Play, BoardGame> readPlayEntry(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "play");

        Play play = new Play();
        play.id = Long.parseLong(parser.getAttributeValue(null, "id"));
        play.date = parser.getAttributeValue(null, "date");
        BoardGame boardGame = null;

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("item")) {
                boardGame = readBoardGame(parser);
            } else {
                skip(parser);
            }
        }
        play.boardGameId = boardGame.id;

        parser.require(XmlPullParser.END_TAG, ns, "play");
        return Pair.create(play, boardGame);
    }

    private BoardGame readBoardGame(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "item");
        BoardGame boardGame = new BoardGame();
        boardGame.name = parser.getAttributeValue(null, "name");
        boardGame.id = Long.parseLong(parser.getAttributeValue(null, "objectid"));
        skip(parser);
        parser.require(XmlPullParser.END_TAG, ns, "item");
        return boardGame;
    }

    private String readName(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "name");
        String name = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "name");
        return name;
    }

    private String readThumbnailUrl(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "thumbnail");
        String thumbnailUrl = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "thumbnail");
        Log.i(TAG, "Read thumbnail url: " + thumbnailUrl);
        return thumbnailUrl;
    }

    private int readYearPublished(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "yearpublished");
        String yearPublishedStr = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "yearpublished");
        return Integer.parseInt(yearPublishedStr);
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