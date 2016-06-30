package rishabhbanga.nanodegree.tnimdb.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

/**
 * Created by erishba on 6/29/2016.
 */
public class MovieSyncService extends Service {

    private static final Object sSyncAdapterLock = new Object();
    private static MovieSyncAdapter sMovieSyncAdapter = null;

    @Override
    public void onCreate() {
        Log.d("MovieSyncService", "onCreate - MovieSyncService");
        synchronized (sSyncAdapterLock) {
            if (sMovieSyncAdapter == null) {
                sMovieSyncAdapter = new MovieSyncAdapter(getApplicationContext(), true);
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return sMovieSyncAdapter.getSyncAdapterBinder();
    }
}
