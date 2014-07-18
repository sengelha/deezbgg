package info.deez.deezbgg.ui.fragment.plays;

import java.util.List;
import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import info.deez.deezbgg.repository.DeezbggDbHelper;
import info.deez.deezbgg.sync.SyncManager;

public class PlaysFragment extends ListFragment implements LoaderManager.LoaderCallbacks<List<PlaysFragmentRowData>> {
    private static final String TAG = "PlaysFragment";
    private DeezbggDbHelper mDbHelper;
    private PlaysFragmentAdapter mAdapter;
    private SyncManager mSyncManager;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mDbHelper = new DeezbggDbHelper(getActivity());
        mAdapter = new PlaysFragmentAdapter(getActivity());
        mSyncManager = new SyncManager(getActivity());

        setEmptyText("No plays found");
        setListAdapter(mAdapter);
        setListShown(false);
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        // do something with the data
    }

    @Override
    public Loader<List<PlaysFragmentRowData>> onCreateLoader(int id, Bundle args) {
        Log.i(TAG, "Creating loader");
        return new PlaysLoader(getActivity(), mDbHelper);
    }

    @Override
    public void onLoadFinished(Loader<List<PlaysFragmentRowData>> l, List<PlaysFragmentRowData> data) {
        Log.i(TAG, "Load finished.  Swapping play data...");
        mAdapter.swapPlayData(data);
        if (isResumed()) {
            setListShown(true);
        } else {
            setListShownNoAnimation(true);
        }

        mSyncManager.syncPlaysAsync("sengelha", mDbHelper, mAdapter);
    }

    @Override
    public void onLoaderReset(Loader l) {
        Log.i(TAG, "Loader reset.  Clearing play data...");
        mAdapter.swapPlayData(null);
    }
}
