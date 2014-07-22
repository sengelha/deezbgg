package info.deez.deezbgg.entity;

/**
 * Created by sengelha on 7/15/2014.
 */
public class CollectionItem {
    public long id;
    public long boardGameId;

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof CollectionItem))
            return false;

        CollectionItem rhs = (CollectionItem)obj;
        return id == rhs.id && boardGameId == rhs.boardGameId;
    }
}
