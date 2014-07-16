package info.deez.deezbgg.repository;

import java.util.ArrayList;
import java.util.List;
import info.deez.deezbgg.entity.BoardGame;

/**
 * Created by sengelha on 7/15/2014.
 */
public class BoardGameRepository {
    public static List<BoardGame> getAllBoardGames() {
        List<BoardGame> boardGames = new ArrayList<BoardGame>();
        BoardGame agricola = new BoardGame();
        agricola.id = 31260;
        agricola.name = "Agricola";
        agricola.imageUrl = "http://cf.geekdo-images.com/images/pic259085.jpg";
        agricola.thumbnailUrl = "http://cf.geekdo-images.com/images/pic259085_t.jpg";
        boardGames.add(agricola);
        BoardGame blokus = new BoardGame();
        blokus.id = 2453;
        blokus.name = "Blokus";
        blokus.imageUrl = "http://cf.geekdo-images.com/images/pic153979.jpg";
        blokus.thumbnailUrl = "http://cf.geekdo-images.com/images/pic153979_t.jpg";
        boardGames.add(blokus);
        BoardGame carcassonne = new BoardGame();
        carcassonne.id = 822;
        carcassonne.name = "Carcassonne";
        carcassonne.imageUrl = "http://cf.geekdo-images.com/images/pic166867.jpg";
        carcassonne.thumbnailUrl = "http://cf.geekdo-images.com/images/pic166867_t.jpg";
        boardGames.add(carcassonne);
        BoardGame loveLetter = new BoardGame();
        loveLetter.id = 129622;
        loveLetter.name = "Love Letter";
        loveLetter.imageUrl = "http://cf.geekdo-images.com/images/pic1401448.jpg";
        loveLetter.thumbnailUrl = "http://cf.geekdo-images.com/images/pic1401448_t.jpg";
        boardGames.add(loveLetter);
        BoardGame smallWorld = new BoardGame();
        smallWorld.id = 40692;
        smallWorld.name = "Small World";
        smallWorld.imageUrl = "http://cf.geekdo-images.com/images/pic428828.jpg";
        smallWorld.thumbnailUrl = "http://cf.geekdo-images.com/images/pic428828_t.jpg";
        boardGames.add(smallWorld);
        BoardGame mansionsOfMadness = new BoardGame();
        mansionsOfMadness.id = 83330;
        mansionsOfMadness.name = "Mansions of Madness";
        mansionsOfMadness.imageUrl = "http://cf.geekdo-images.com/images/pic814011.jpg";
        mansionsOfMadness.thumbnailUrl = "http://cf.geekdo-images.com/images/pic814011_t.jpg";
        boardGames.add(mansionsOfMadness);
        return boardGames;
    }

    public static BoardGame getBoardGameById(long id) {
        for (BoardGame boardGame : getAllBoardGames()) {
            if (boardGame.id == id)
                return boardGame;
        }
        return null;
    }

}
