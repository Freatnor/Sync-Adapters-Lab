package com.example.freatnor;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;
import android.util.Log;

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
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://api.nytimes.com/svc/news/v3/content/all/all/all.json?limit=20&api-key=05af8d5d9e5a4decafd7a8bc32b6b076")
                .build();

        try {
            Response response = client.newCall(request).execute();
            if(!response.isSuccessful()){
                throw new IOException("Response result not successful " + response);
            }
            //clear the DB to remove old data (could try and keep newer ones)
            mContentResolver.delete(Content_URI, null, null);
            Gson gson = new Gson();
            String responseBody = response.body().string();
            String trimmedResponse = responseBody.substring(CALLBACK_NAME.length(), responseBody.length() - 2);
            StockQuote result = gson.fromJson(trimmedResponse, StockQuote.class);

            ContentValues values = new ContentValues();
            values.put()

            for (NewsItem item: result.getResults()) {
                ContentValues values = new ContentValues();
                values.put(NewsDBHelper.COLUMN_TITLE, item.getTitle());
                mContentResolver.insert(NewsContentProvider.CONTENT_URI, values);

                Log.d(TAG, "onPerformSync: Latest story - " + item.getTitle());
            }


        } catch (IOException e) {
            Log.e(TAG, "onPerformSync: Error after NYT API call", e);
        }
    }
}
