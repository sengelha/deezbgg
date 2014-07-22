package info.deez.deezbgg.sync;

import java.util.Collection;

/**
 * Created by sengelha on 7/22/2014.
 */
public class SyncUtils {
    public static <T> void determineChanges
        (
        Collection<T> originalCollection,
        Collection<T> targetCollection,
        Collection<T> itemsToAdd,
        Collection<T> itemsToRemove
        )
    {
        for (T t : originalCollection) {
            if (!targetCollection.contains(t))
                itemsToRemove.add(t);
        }
        for (T t : targetCollection) {
            if (!originalCollection.contains(t))
                itemsToAdd.add(t);
        }
    }
}
