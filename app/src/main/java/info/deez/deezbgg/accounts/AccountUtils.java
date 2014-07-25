package info.deez.deezbgg.accounts;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.util.Log;

public class AccountUtils {
    private static final String TAG = "AccountUtils";
    public static final String ACCOUNT_NAME = "sengelha";
    public static final String ACCOUNT_TYPE = "info.deez.deezbgg.account";

    public static boolean createSyncAccount(Context context) {
        Account account = getSyncAccount();
        AccountManager accountManager = (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);
        if (accountManager.addAccountExplicitly(account, null, null)) {
            Log.i(TAG, "Account created");
            return true;
        } else {
            Log.i(TAG, "Account already exists");
            return false;
        }
    }

    public static Account getSyncAccount() {
        return new Account(ACCOUNT_NAME, ACCOUNT_TYPE);
    }
}
