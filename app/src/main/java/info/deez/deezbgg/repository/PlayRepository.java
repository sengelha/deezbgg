package info.deez.deezbgg.repository;

import java.util.List;
import java.util.ArrayList;
import info.deez.deezbgg.entity.Play;

/**
 * Created by sengelha on 7/15/2014.
 */
public class PlayRepository {
    public static List<Play> getAllPlays() {
        List<Play> plays = new ArrayList<Play>();
        Play play1 = new Play();
        play1.id = 12197106;
        play1.date = "2014-07-10";
        play1.boardGameId = 129622;
        plays.add(play1);
        Play play2 = new Play();
        play2.id = 12197104;
        play2.date = "2014-07-10";
        play2.boardGameId = 40692;
        plays.add(play2);
        Play play3 = new Play();
        play3.id = 12190763;
        play3.date = "2014-07-09";
        play3.boardGameId = 83330;
        plays.add(play3);
        return plays;
    }
}
