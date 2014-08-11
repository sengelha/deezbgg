package info.deez.deezbgg.sync.bggapi;

/**
 * Created by sengelha on 8/9/2014.
 */
public class CollectionItem {
    public long id;
    public long boardGameId;
    public String boardGameName;
    public Integer boardGameYearPublished;
    public String boardGameImageUrl;
    public String boardGameThumbnailUrl;
    public boolean owned;
    public int numberOfPlays;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("id:");
        sb.append(id);
        sb.append(", boardGameId:");
        sb.append(boardGameId);
        sb.append(", boardGameName:\"");
        sb.append(boardGameName);
        sb.append("\"");
        sb.append(", boardGameYearPublished:");
        sb.append(boardGameYearPublished);
        sb.append(", boardGameImageUrl:\"");
        sb.append(boardGameImageUrl);
        sb.append("\"");
        sb.append(", boardGameThumbnailUrl:\"");
        sb.append(boardGameThumbnailUrl);
        sb.append("\"");
        sb.append(", owned:");
        sb.append(owned);
        sb.append(", numberOfPlays:");
        sb.append(numberOfPlays);
        sb.append("}");
        return sb.toString();
    }
}
