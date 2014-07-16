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
        Play play4 = new Play();
        play4.id = 12155552;
        play4.date = "2014-07-03";
        play4.boardGameId = 124708;
        plays.add(play4);
        Play play5 = new Play();
        play5.id = 12137692;
        play5.date = "2014-07-02";
        play5.boardGameId = 72287;
        plays.add(play5);
        Play play6 = new Play();
        play6.id = 12137750;
        play6.date = "2014-07-02";
        play6.boardGameId = 24480;
        plays.add(play6);
        Play play7 = new Play();
        play7.id = 12137754;
        play7.date = "2014-07-02";
        play7.boardGameId = 143519;
        plays.add(play7);
        Play play8 = new Play();
        play8.id = 12111597;
        play8.date = "2014-06-28";
        play8.boardGameId = 124708;
        plays.add(play8);
        Play play9 = new Play();
        play9.id = 12110954;
        play9.date = "2014-06-28";
        play9.boardGameId = 1258;
        plays.add(play9);
        Play play10 = new Play();
        play10.id = 12111599;
        play10.date = "2014-06-27";
        play10.boardGameId = 2453;
        plays.add(play10);
        return plays;
    }
}
