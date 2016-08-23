package com.example.freatnor.presenters;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.freatnor.R;

/**
 * Created by Jonathan Taylor on 8/22/16.
 */
public class StocksCursorAdapter extends CursorAdapter {


    public StocksCursorAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View rootView = LayoutInflater.from(context).inflate(R.layout.stock_recycler_content, parent, false);
        return rootView;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

    }
}
