package info.deez.deezbgg.sync.bggapi;

import android.content.ContentResolver;
import android.util.Log;
import android.util.Pair;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import info.deez.deezbgg.utils.XmlParserUtils;

public class PlayXmlParser {
    private static final String TAG = "PlayXmlParser";
    // We don't use namespaces
    private static final String ns = null;

    public List<Play> parsePlays(InputStream stream) throws XmlPullParserException, IOException {
        Log.i(TAG, "Parsing BGG plays XML feed...");
        XmlPullParser parser = Xml.newPullParser();
        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
        parser.setInput(stream, null);
        parser.nextTag();
        return readPlayFeed(parser);
    }

    private List<Play> readPlayFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
        List<Play> plays = new ArrayList<Play>();

        parser.require(XmlPullParser.START_TAG, ns, "plays");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("play")) {
                plays.add(readPlayEntry(parser));
            } else {
                XmlParserUtils.skip(parser);
            }
        }
        return plays;
    }

    private Play readPlayEntry(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "play");

        Play play = new Play();
        play.id = Long.parseLong(parser.getAttributeValue(null, "id"));
        play.date = parser.getAttributeValue(null, "date");
        play.quantity = Integer.parseInt(parser.getAttributeValue(null, "quantity"));
        play.length = Integer.parseInt(parser.getAttributeValue(null, "length"));
        play.incomplete = Integer.parseInt(parser.getAttributeValue(null, "incomplete"));

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("item")) {
                play.boardGame.id = Long.parseLong(parser.getAttributeValue(null, "objectid"));
                play.boardGame.name = parser.getAttributeValue(null, "name");
                XmlParserUtils.skip(parser);
                parser.require(XmlPullParser.END_TAG, ns, "item");
            } else {
                XmlParserUtils.skip(parser);
            }
        }

        parser.require(XmlPullParser.END_TAG, ns, "play");
        return play;
    }
}
