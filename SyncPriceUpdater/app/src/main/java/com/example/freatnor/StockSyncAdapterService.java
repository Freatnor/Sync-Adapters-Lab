package com.example.freatnor;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by Jonathan Taylor on 8/22/16.
 */
public class StockSyncAdapterService extends Service {

    private static final String TAG = "StockSyncAdapterService";
    private static StockSyncAdapter sSyncAdapter = null;
    private static final Object sSyncAdapterLock = new Object();

    @Override
    public void onCreate() {
        synchronized (sSyncAdapterLock){
            if(sSyncAdapter == null){
                Log.d(TAG, "onCreate: making new stock sync adapter");
                sSyncAdapter = new StockSyncAdapter(getApplicationContext(), true, false);
            }
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return sSyncAdapter.getSyncAdapterBinder();
    }
}
