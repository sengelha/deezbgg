package info.deez.deezbgg.sync;

import android.accounts.Account;
import android.content.ContentResolver;
import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.Collection;

import info.deez.deezbgg.accounts.AccountUtils;
import info.deez.deezbgg.content.ContentContract;

public class SyncUtils {
    private static final String TAG = "SyncUtils";
    private static final long SYNC_FREQUENCY = 60 * 60; // 1 hour (in seconds)
    private static final String PREF_SETUP_COMPLETE = "setup_complete";

    public static void createAccountAndEnableSync(Context context) {
        boolean newAccount = AccountUtils.createSyncAccount(context);
        boolean setupComplete = PreferenceManager
                .getDefaultSharedPreferences(context)
                .getBoolean(PREF_SETUP_COMPLETE, false);
        if (newAccount || !setupComplete) {
            Account account = AccountUtils.getSyncAccount();
            ContentResolver.setIsSyncable(account, ContentContract.CONTENT_AUTHORITY, 1);
            ContentResolver.setSyncAutomatically(account, ContentContract.CONTENT_AUTHORITY, true);
            ContentResolver.addPeriodicSync(account, ContentContract.CONTENT_AUTHORITY, new Bundle(), SYNC_FREQUENCY);
            triggerRefresh();
            PreferenceManager
                    .getDefaultSharedPreferences(context)
                    .edit()
                    .putBoolean(PREF_SETUP_COMPLETE, true)
                    .commit();
        }
    }

    public static void triggerRefresh() {
        Log.i(TAG, "Requesting sync refresh...");
        Bundle b = new Bundle();
        b.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        b.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        ContentResolver.requestSync(
                AccountUtils.getSyncAccount(),
                ContentContract.CONTENT_AUTHORITY,
                b);
    }
}
