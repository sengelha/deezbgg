package info.deez.deezbgg.sync.bggapi;

public class Play {
    public long id;
    public long boardGameId;
    public String boardGameName;
    public String date;
    public int quantity;
    public int length;
    public int incomplete;

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
        sb.append(", date:\"");
        sb.append(date);
        sb.append("\"");
        sb.append(", quantity:");
        sb.append(quantity);
        sb.append(", length:");
        sb.append(length);
        sb.append(", incomplete:");
        sb.append(incomplete);
        sb.append("}");
        return sb.toString();
    }}
