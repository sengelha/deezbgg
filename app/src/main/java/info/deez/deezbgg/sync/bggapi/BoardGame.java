package info.deez.deezbgg.sync.bggapi;

import java.util.ArrayList;
import java.util.List;

public class BoardGame {
    public static class NameEntry {
        public String type;
        public int sortIndex;
        public String name;

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("{");
            sb.append("type:\"");
            sb.append(type);
            sb.append("\"");
            sb.append(", sortIndex:");
            sb.append(sortIndex);
            sb.append(", name:\"");
            sb.append(name);
            sb.append("\"");
            sb.append("}");
            return sb.toString();
        }
    }
    public long id;
    public List<NameEntry> nameEntries = new ArrayList<NameEntry>();
    public String imageUrl;
    public String thumbnailUrl;
    public int yearPublished;

    public String getPrimaryName() {
        for (NameEntry nameEntry : nameEntries) {
            if ("primary".equals(nameEntry.type))
                return nameEntry.name;
        }
        return null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("id:");
        sb.append(id);
        sb.append(", names:[");
        boolean first = true;
        for (NameEntry nameEntry : nameEntries) {
            if (!first)
                sb.append(", ");
            sb.append(nameEntry.toString());
            first = false;
        }
        sb.append("]");
        sb.append(", imageUrl:\"");
        sb.append(imageUrl);
        sb.append("\"");
        sb.append(", thumbnailUrl:\"");
        sb.append(thumbnailUrl);
        sb.append("\"");
        sb.append(", yearPublished:");
        sb.append(yearPublished);
        sb.append("}");
        return sb.toString();
    }
}
