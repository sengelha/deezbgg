package info.deez.deezbgg.sync.bggapi;

public class Play {
    public class BoardGame {
        public long id;
        public String name;
    }

    public long id;
    public BoardGame boardGame = new BoardGame();
    public String date;
    public int quantity;
    public int length;
    public int incomplete;
}
