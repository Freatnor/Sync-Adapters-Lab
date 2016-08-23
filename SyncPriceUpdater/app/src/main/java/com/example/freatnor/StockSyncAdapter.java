package com.example.freatnor;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SyncResult;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;

import com.example.freatnor.external_contracts.StockPortfolioContract;
import com.example.freatnor.models.StockQuote;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Jonathan Taylor on 8/22/16.
 */
public class StockSyncAdapter extends AbstractThreadedSyncAdapter {

    private static final String TAG = "StockSyncAdapter";

    private ContentResolver mContentResolver;
    public static final String CALLBACK_NAME = "result";
    public static final String QUOTE_URL = "http://dev.markitondemand.com/Api/v2/Quote/jsonp?callback="+CALLBACK_NAME+"&symbol=";

    public StockSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        mContentResolver = context.getContentResolver();
    }

    public StockSyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
        mContentResolver = context.getContentResolver();
    }

    @Override
    public void onPerformSync(Account account, Bundle bundle, String s, ContentProviderClient contentProviderClient, SyncResult syncResult) {
        Cursor cursor = mContentResolver.query(StockPortfolioContract.Stocks.CONTENT_URI, null, null, null, null);
        Log.d(TAG, "onPerformSync: got a cursor with rows - " + cursor.getCount());

        if(cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                //loop through and call the endpoint for each symbol in the database
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(QUOTE_URL + cursor.getString(cursor.getColumnIndex(StockPortfolioContract.Stocks.COLUMN_STOCK_SYMBOL)))
                        .build();

                try {
                    Response response = client.newCall(request).execute();
                    if (!response.isSuccessful()) {
                        throw new IOException("Response result not successful " + response);
                    }
                    //grab the price and insert it into the external db
                    mContentResolver.delete(StockPortfolioContract.Stocks.CONTENT_URI, null, null);
                    Gson gson = new Gson();
                    String responseBody = response.body().string();
                    String trimmedResponse = responseBody.substring(CALLBACK_NAME.length(), responseBody.length() - 2);
                    StockQuote result = gson.fromJson(trimmedResponse, StockQuote.class);

                    ContentValues values = new ContentValues();
                    values.put(StockPortfolioContract.Stocks.COLUMN_PRICE, result.getLastPrice());
                    mContentResolver.update(StockPortfolioContract.Stocks.CONTENT_URI, values,
                            StockPortfolioContract.Stocks._ID, new String[]{cursor.getString(cursor.getColumnIndex(StockPortfolioContract.Stocks._ID))});
                    Log.d(TAG, "onPerformSync: updated " + cursor.getColumnIndex(StockPortfolioContract.Stocks.COLUMN_STOCK_SYMBOL) +
                    " to have the price " + result.getLastPrice());


                } catch (IOException e) {
                    Log.e(TAG, "onPerformSync: Error after Markit Call", e);
                }
                cursor.moveToNext();
            }
        }
    }
}
