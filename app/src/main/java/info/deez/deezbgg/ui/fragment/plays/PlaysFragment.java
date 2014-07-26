package info.deez.deezbgg.ui.fragment.plays;

import android.accounts.Account;
import android.app.Activity;
import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.ContentResolver;
import android.content.CursorLoader;
import android.content.Loader;
import android.content.SyncStatusObserver;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import info.deez.deezbgg.R;
import info.deez.deezbgg.accounts.AccountUtils;
import info.deez.deezbgg.bitmap.BitmapMemoryCache;
import info.deez.deezbgg.content.ContentContract;
import info.deez.deezbgg.sync.SyncUtils;
import info.deez.deezbgg.ui.fragment.plays.PlaysFragmentAdapter;

public class PlaysFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final String TAG = "PlaysFragment";
    private PlaysFragmentAdapter mAdapter;
    private Object mSyncObserverHandle;
    private SyncStatusObserver mSyncStatusObserver = new SyncStatusObserver() {
        @Override
        public void onStatusChanged(int which) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Account account = AccountUtils.getSyncAccount();
                    if (account == null) {
                        setRefreshActionButtonState(false);
                        return;
                    }

                    boolean syncActive = ContentResolver.isSyncActive(account, ContentContract.CONTENT_AUTHORITY);
                    boolean syncPending = ContentResolver.isSyncPending(account, ContentContract.CONTENT_AUTHORITY);
                    setRefreshActionButtonState(syncActive || syncPending);
                }
            });
        }
    };
    private Menu mOptionsMenu;
    private BitmapMemoryCache mBitmapCache;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        final int cacheSize = maxMemory / 8;
        mBitmapCache = new BitmapMemoryCache(cacheSize);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        SyncUtils.createAccountAndEnableSync(activity);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAdapter = new PlaysFragmentAdapter(getActivity(), null, 0, mBitmapCache);
        setListAdapter(mAdapter);
        // TODO: setEmptyText()
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public void onResume() {
        super.onResume();
        mSyncStatusObserver.onStatusChanged(0);

        int mask = ContentResolver.SYNC_OBSERVER_TYPE_PENDING | ContentResolver.SYNC_OBSERVER_TYPE_ACTIVE;
        mSyncObserverHandle = ContentResolver.addStatusChangeListener(mask, mSyncStatusObserver);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mSyncObserverHandle != null) {
            ContentResolver.removeStatusChangeListener(mSyncObserverHandle);
            mSyncObserverHandle = null;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                SyncUtils.triggerRefresh();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(
                getActivity(),
                ContentContract.PlayEntry.CONTENT_URI,
                PlaysFragmentAdapter.COLUMNS,
                null,
                null,
                ContentContract.PlayEntry.COLUMN_NAME_PLAY_DATE
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        mAdapter.swapCursor(cursor);

        if (isResumed()) {
            setListShown(true);
        } else {
            setListShownNoAnimation(true);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        mAdapter.swapCursor(null);
    }

    public void setRefreshActionButtonState(boolean refreshing) {
        if (mOptionsMenu == null) {
            return;
        }

        MenuItem refreshItem = mOptionsMenu.findItem(R.id.action_refresh);
        if (refreshItem != null) {
            if (refreshing) {
                refreshItem.setActionView(R.layout.actionbar_indeterminate_progress);
            } else {
                refreshItem.setActionView(null);
            }
        }
    }
}
