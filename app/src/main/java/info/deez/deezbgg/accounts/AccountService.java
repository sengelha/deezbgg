package info.deez.deezbgg.accounts;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class AccountService extends Service {
    private final String TAG = "DeezbggAccountService";
    private Authenticator mAuthenticator;

    @Override
    public void onCreate() {
        Log.i(TAG, "DeezbggAccountService created");
        mAuthenticator = new Authenticator(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mAuthenticator.getIBinder();
    }
}
