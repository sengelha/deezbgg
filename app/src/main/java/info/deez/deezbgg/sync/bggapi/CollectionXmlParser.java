package info.deez.deezbgg.sync.bggapi;

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

public class CollectionXmlParser {
    private static final String TAG = "CollectionXmlParser";
    // We don't use namespaces
    private static final String ns = null;

    public List<CollectionItem> parseCollection(InputStream stream) throws XmlPullParserException, IOException {
        Log.i(TAG, "Parsing BGG collection XML feed...");
        XmlPullParser parser = Xml.newPullParser();
        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
        parser.setInput(stream, null);
        parser.nextTag();
        return readCollectionFeed(parser);
    }

    private List<CollectionItem> readCollectionFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
        List<CollectionItem> entries = new ArrayList<CollectionItem>();

        parser.require(XmlPullParser.START_TAG, ns, "items");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("item")) {
                entries.add(readCollectionEntry(parser));
            } else {
                XmlParserUtils.skip(parser);
            }
        }
        return entries;
    }

    private CollectionItem readCollectionEntry(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "item");

        CollectionItem collectionItem = new CollectionItem();
        collectionItem.id = Long.parseLong(parser.getAttributeValue(null, "collid"));
        collectionItem.boardGameId = Long.parseLong(parser.getAttributeValue(null, "objectid"));

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("name")) {
                collectionItem.boardGameName = XmlParserUtils.readInnerText(parser);
            } else if (name.equals("yearpublished")) {
                collectionItem.boardGameYearPublished = new Integer(XmlParserUtils.readInnerText(parser));
            } else if (name.equals("image")) {
                collectionItem.boardGameImageUrl = XmlParserUtils.readInnerText(parser);
            } else if (name.equals("thumbnail")) {
                collectionItem.boardGameThumbnailUrl = XmlParserUtils.readInnerText(parser);
            } else if (name.equals("status")) {
                collectionItem.owned = "1".equals(parser.getAttributeValue(null, "own"));
                XmlParserUtils.skip(parser);
            } else if (name.equals("numplays")) {
                collectionItem.numberOfPlays = Integer.parseInt(XmlParserUtils.readInnerText(parser));
            } else {
                XmlParserUtils.skip(parser);
            }
        }
        parser.require(XmlPullParser.END_TAG, ns, "item");
        return collectionItem;
    }
}
