package info.deez.deezbgg.repository;

import java.util.List;
import java.util.ArrayList;
import info.deez.deezbgg.entity.CollectionItem;

/**
 * Created by sengelha on 7/15/2014.
 */
public class CollectionItemRepository {
    public static List<CollectionItem> getAllCollectionItems() {
        List<CollectionItem> collectionItems = new ArrayList<CollectionItem>();
        int id = 1;
        for (int boardGameId : new int[] { 31260, 2453, 822, 129622, 40692, 83330, 124708, 1258 }) {
            CollectionItem collectionItem = new CollectionItem();
            collectionItem.id = id++;
            collectionItem.boardGameId = boardGameId;
            collectionItems.add(collectionItem);
        }
        return collectionItems;
    }
}
