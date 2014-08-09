package info.deez.deezbgg.sync.bggapi;

import android.util.Pair;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

public class BoardGameGeekApi {
    public static List<CollectionItem> getCollectionForUser(String username) throws IOException, XmlPullParserException {
        URL url = new URL("http://boardgamegeek.com/xmlapi2/collection?username=" + username);
        URLConnection conn = url.openConnection();
        CollectionXmlParser parser = new CollectionXmlParser();

        List<CollectionItem> apiResults;
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
        InputStream stream = conn.getInputStream();
        try {
            apiResults = parser.parsePlays(stream);
        } finally {
            stream.close();
        }

        return apiResults;
    }
}
