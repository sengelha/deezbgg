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
        CollectionItem collectionItem1 = new CollectionItem();
        collectionItem1.id = 1;
        collectionItem1.boardGameId = 1;
        collectionItems.add(collectionItem1);
        CollectionItem collectionItem2 = new CollectionItem();
        collectionItem2.id = 2;
        collectionItem2.boardGameId = 2;
        collectionItems.add(collectionItem2);
        CollectionItem collectionItem3 = new CollectionItem();
        collectionItem3.id = 3;
        collectionItem3.boardGameId = 3;
        collectionItems.add(collectionItem3);
        return collectionItems;
    }
}
