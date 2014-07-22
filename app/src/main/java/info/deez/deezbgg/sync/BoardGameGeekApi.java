package info.deez.deezbgg.sync;

import android.util.Pair;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import info.deez.deezbgg.entity.BoardGame;
import info.deez.deezbgg.entity.CollectionItem;

/**
 * Created by sengelha on 7/22/2014.
 */
public class BoardGameGeekApi {
    public static List<CollectionItem> getCollectionForUser(String username) throws IOException, XmlPullParserException {
        URL url = new URL("http://boardgamegeek.com/xmlapi2/collection?username=" + username);
        URLConnection conn = url.openConnection();
        BoardGameGeekXmlParser parser = new BoardGameGeekXmlParser();

        List<Pair<CollectionItem, BoardGame>> apiResults;
        InputStream stream = conn.getInputStream();
        try {
            apiResults = parser.parseCollection(stream);
        } finally {
            stream.close();
        }

        List<CollectionItem> collectionItems = new ArrayList<CollectionItem>();
        for (Pair<CollectionItem, BoardGame> apiResult : apiResults)
            collectionItems.add(apiResult.first);
        return collectionItems;
    }
}
