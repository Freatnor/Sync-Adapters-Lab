package com.example.freatnor;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CursorAdapter;
import android.widget.ListView;

import com.example.freatnor.external_contracts.StockPortfolioContract;
import com.example.freatnor.presenters.StocksCursorAdapter;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = "MainActivity";
    public static final int STOCKS_LOADER = 25;

    private ListView mArticlesListView;
    private StocksCursorAdapter mCursorAdapter;

    // Constants
    // Account type
    public static final String ACCOUNT_TYPE = "example.com";
    // Account
    public static final String ACCOUNT = "default_account";

    private Account mAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportLoaderManager().initLoader(STOCKS_LOADER, null, this);

        Cursor cursor = getContentResolver().query(CONTENT_URI, null, null, null, null);

        mCursorAdapter = new StocksCursorAdapter(this, cursor, true);

//        Bundle settingsBundle = new Bundle();
//        settingsBundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
//        settingsBundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
//
//        ContentResolver.requestSync(mAccount, NewsContentProvider.AUTHORITY, settingsBundle);

        ContentResolver.setSyncAutomatically(mAccount,NewsContentProvider.AUTHORITY,true);
        ContentResolver.addPeriodicSync(
                mAccount,
                NewsContentProvider.AUTHORITY,
                Bundle.EMPTY,
                60);
    }

    /**
     * Create a new dummy account for the sync adapter
     *
     * @param context The application context
     */
    public static Account createSyncAccount(Context context) {
        // Create the account type and default account
        Account newAccount = new Account(
                ACCOUNT, ACCOUNT_TYPE);
        // Get an instance of the Android account manager
        AccountManager accountManager =
                (AccountManager) context.getSystemService(
                        ACCOUNT_SERVICE);
        /*
         * Add the account and account type, no password or user data
         * If successful, return the Account object, otherwise report an error.
         */
        if (accountManager.addAccountExplicitly(newAccount, null, null)) {
            /*
             * If you don't set android:syncable="true" in
             * in your <provider> element in the manifest,
             * then call context.setIsSyncable(account, AUTHORITY, 1)
             * here.
             */
        } else {
            /*
             * The account exists or some other error occurred. Log this, report it,
             * or handle it internally.
             */
        }
        return newAccount;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if(id == STOCKS_LOADER){
            return new CursorLoader(this, StockPortfolioContract.Stocks.CONTENT_URI, null, null, null, null);
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mCursorAdapter.changeCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursorAdapter.changeCursor(null);
    }
}
