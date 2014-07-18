package info.deez.deezbgg.repository;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;
import info.deez.deezbgg.entity.BoardGame;

/**
 * Created by sengelha on 7/15/2014.
 */
public class BoardGameRepository {
    public static List<BoardGame> getAllBoardGames() {
        try {
            List<BoardGame> boardGames = new ArrayList<BoardGame>();
            BoardGame agricola = new BoardGame();
            agricola.id = 31260;
            agricola.name = "Agricola";
            agricola.imageUrl = new URL("http://cf.geekdo-images.com/images/pic259085.jpg");
            agricola.thumbnailUrl = new URL("http://cf.geekdo-images.com/images/pic259085_t.jpg");
            boardGames.add(agricola);
            BoardGame blokus = new BoardGame();
            blokus.id = 2453;
            blokus.name = "Blokus";
            blokus.imageUrl = new URL("http://cf.geekdo-images.com/images/pic153979.jpg");
            blokus.thumbnailUrl = new URL("http://cf.geekdo-images.com/images/pic153979_t.jpg");
            boardGames.add(blokus);
            BoardGame carcassonne = new BoardGame();
            carcassonne.id = 822;
            carcassonne.name = "Carcassonne";
            carcassonne.imageUrl = new URL("http://cf.geekdo-images.com/images/pic166867.jpg");
            carcassonne.thumbnailUrl = new URL("http://cf.geekdo-images.com/images/pic166867_t.jpg");
            boardGames.add(carcassonne);
            BoardGame loveLetter = new BoardGame();
            loveLetter.id = 129622;
            loveLetter.name = "Love Letter";
            loveLetter.imageUrl = new URL("http://cf.geekdo-images.com/images/pic1401448.jpg");
            loveLetter.thumbnailUrl = new URL("http://cf.geekdo-images.com/images/pic1401448_t.jpg");
            boardGames.add(loveLetter);
            BoardGame smallWorld = new BoardGame();
            smallWorld.id = 40692;
            smallWorld.name = "Small World";
            smallWorld.imageUrl = new URL("http://cf.geekdo-images.com/images/pic428828.jpg");
            smallWorld.thumbnailUrl = new URL("http://cf.geekdo-images.com/images/pic428828_t.jpg");
            boardGames.add(smallWorld);
            BoardGame mansionsOfMadness = new BoardGame();
            mansionsOfMadness.id = 83330;
            mansionsOfMadness.name = "Mansions of Madness";
            mansionsOfMadness.imageUrl = new URL("http://cf.geekdo-images.com/images/pic814011.jpg");
            mansionsOfMadness.thumbnailUrl = new URL("http://cf.geekdo-images.com/images/pic814011_t.jpg");
            boardGames.add(mansionsOfMadness);
            BoardGame miceAndMystics = new BoardGame();
            miceAndMystics.id = 124708;
            miceAndMystics.name = "Mice and Mystics";
            miceAndMystics.imageUrl = new URL("http://cf.geekdo-images.com/images/pic1312072.jpg");
            miceAndMystics.thumbnailUrl = new URL("http://cf.geekdo-images.com/images/pic1312072_t.jpg");
            boardGames.add(miceAndMystics);
            BoardGame mrJackPocket = new BoardGame();
            mrJackPocket.id = 72287;
            mrJackPocket.name = "Mr. Jack Pocket";
            mrJackPocket.imageUrl = new URL("http://cf.geekdo-images.com/images/pic1519530.jpg");
            mrJackPocket.thumbnailUrl = new URL("http://cf.geekdo-images.com/images/pic1519530_t.jpg");
            boardGames.add(mrJackPocket);
            BoardGame quantum = new BoardGame();
            quantum.id = 143519;
            quantum.name = "Quantum";
            quantum.imageUrl = new URL("http://cf.geekdo-images.com/images/pic1727619.jpg");
            quantum.thumbnailUrl = new URL("http://cf.geekdo-images.com/images/pic1727619_t.jpg");
            boardGames.add(quantum);
            BoardGame phase10 = new BoardGame();
            phase10.id = 1258;
            phase10.name = "Phase 10";
            phase10.imageUrl = new URL("http://cf.geekdo-images.com/images/pic226562.jpg");
            phase10.thumbnailUrl = new URL("http://cf.geekdo-images.com/images/pic226562_t.jpg");
            boardGames.add(phase10);
            BoardGame pillarsOfTheEarth = new BoardGame();
            pillarsOfTheEarth.id = 24480;
            pillarsOfTheEarth.name = "The Pillars of the Earth";
            pillarsOfTheEarth.imageUrl = new URL("http://cf.geekdo-images.com/images/pic212815.jpg");
            pillarsOfTheEarth.thumbnailUrl = new URL("http://cf.geekdo-images.com/images/pic212815_t.jpg");
            boardGames.add(pillarsOfTheEarth);
            return boardGames;
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error parsing URL", e);
        }
    }

    public static BoardGame getBoardGameById(long id) {
        for (BoardGame boardGame : getAllBoardGames()) {
            if (boardGame.id == id)
                return boardGame;
        }
        return null;
    }

    public static Dictionary<Long, BoardGame> getBoardGamesByIds(Collection<Long> ids) {
        Dictionary<Long, BoardGame> dict = new Hashtable<Long, BoardGame>();
        for (BoardGame boardGame : getAllBoardGames()) {
            if (ids.contains(boardGame.id)) {
                dict.put(boardGame.id, boardGame);
            }
        }
        return dict;
    }
}
