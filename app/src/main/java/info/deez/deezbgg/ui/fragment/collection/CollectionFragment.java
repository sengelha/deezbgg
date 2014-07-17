package info.deez.deezbgg.ui.fragment.collection;

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

public class CollectionFragment extends ListFragment implements LoaderManager.LoaderCallbacks<List<CollectionFragmentRowData>> {
    private static final String TAG = "CollectionFragment";
    private CollectionFragmentAdapter mAdapter;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mAdapter = new CollectionFragmentAdapter(getActivity());
        setListAdapter(mAdapter);
        setListShown(false);
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        // do something with the data
    }

    @Override
    public Loader<List<CollectionFragmentRowData>> onCreateLoader(int id, Bundle args) {
        Log.i(TAG, "Creating loader");
        return new CollectionLoader(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<List<CollectionFragmentRowData>> l, List<CollectionFragmentRowData> data) {
        Log.i(TAG, "Load finished.  Swapping collection data...");
        mAdapter.swapCollectionData(data);
        if (isResumed()) {
            setListShown(true);
        } else {
            setListShownNoAnimation(true);
        }
    }

    @Override
    public void onLoaderReset(Loader l) {
        Log.i(TAG, "Loader reset.  Clearing collection data...");
        mAdapter.swapCollectionData(null);
    }
}
