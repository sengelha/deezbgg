package info.deez.deezbgg.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CollectionUtils {
    public static List<List<Long>> divideIntoBatches(Collection<Long> collection, int batchSize) {
        List<List<Long>> batches = new ArrayList<List<Long>>();
        List<Long> currentList = new ArrayList<Long>(batchSize);
        for (Long item : collection) {
            currentList.add(item);
            if (currentList.size() >= batchSize) {
                batches.add(currentList);
                currentList = new ArrayList<Long>();
            }
        }
        if (!currentList.isEmpty())
            batches.add(currentList);
        return batches;
    }
}
