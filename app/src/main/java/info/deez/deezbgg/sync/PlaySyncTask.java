package info.deez.deezbgg.sync;

import android.os.AsyncTask;
import android.util.Log;
import android.util.Pair;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import info.deez.deezbgg.entity.BoardGame;
import info.deez.deezbgg.entity.Play;
import info.deez.deezbgg.repository.BoardGameRepository;
import info.deez.deezbgg.repository.DeezbggDbHelper;
import info.deez.deezbgg.repository.PlayRepository;
import info.deez.deezbgg.ui.fragment.plays.PlaysFragmentAdapter;
import info.deez.deezbgg.ui.fragment.plays.PlaysFragmentDataGetter;
import info.deez.deezbgg.ui.fragment.plays.PlaysFragmentRowData;

public class PlaySyncTask extends AsyncTask<String, Void, List<PlaysFragmentRowData>> {
    private static final String TAG = "PlaySyncTask";
    private DeezbggDbHelper mDbHelper;
    private PlaysFragmentAdapter mAdapter;

    public PlaySyncTask(DeezbggDbHelper dbHelper, PlaysFragmentAdapter adapter) {
        mDbHelper = dbHelper;
        mAdapter = adapter;
    }

    @Override
    protected List<PlaysFragmentRowData> doInBackground(String... params) {
        try {
            String username = params[0];
            URL url = new URL("http://boardgamegeek.com/xmlapi2/plays?username=" + username);
            URLConnection conn = url.openConnection();
            BoardGameGeekXmlParser parser = new BoardGameGeekXmlParser();

            List<Pair<Play, BoardGame>> results;
            InputStream stream = conn.getInputStream();
            try {
                results = parser.parsePlays(stream);
            } finally {
                stream.close();
            }

            // Put data in database
            PlayRepository playRepository = new PlayRepository(mDbHelper);
            BoardGameRepository boardGameRepository = new BoardGameRepository(mDbHelper);
            playRepository.deleteAllPlays();
            for (Pair<Play, BoardGame> result : results) {
                playRepository.addPlay(result.first);
                boardGameRepository.upsertBoardGame(result.second);
            }

            // Get new UI-ready data
            PlaysFragmentDataGetter dataGetter = new PlaysFragmentDataGetter(mDbHelper);
            return dataGetter.getData();
        }
        catch (Exception e) {
            Log.e(TAG, "Error syncing collection", e);
        }

        return null;
    }

    @Override
    protected void onPostExecute(List<PlaysFragmentRowData> result) {
        mAdapter.swapPlayData(result);
    }
}
