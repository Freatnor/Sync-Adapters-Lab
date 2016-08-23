package com.example.freatnor.presenters;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.freatnor.R;
import com.example.freatnor.external_contracts.StockPortfolioContract;

/**
 * Created by Jonathan Taylor on 8/22/16.
 */
public class StocksCursorAdapter extends CursorAdapter {
    private static final String TAG = "StocksCursorAdapter";


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
        TextView name = (TextView) view.findViewById(R.id.stock_name);
        TextView price = (TextView) view.findViewById(R.id.stock_price);
        LinearLayout background = (LinearLayout) view.findViewById(R.id.background);

        String fullName = cursor.getString(cursor.getColumnIndex(StockPortfolioContract.Stocks.COLUMN_STOCKNAME))
                + "(" + cursor.getString(cursor.getColumnIndex(StockPortfolioContract.Stocks.COLUMN_STOCK_SYMBOL))
                + ")";
        name.setText(fullName);

        price.setText("$" + cursor.getString(cursor.getColumnIndex(StockPortfolioContract.Stocks.COLUMN_PRICE)));

        String exchange = cursor.getString(cursor.getColumnIndex(StockPortfolioContract.Stocks.COLUMN_EXCHANGE));
        Log.d(TAG, "bindView: exchange is " + exchange);
        if(exchange.equals("NASDAQ")){
            background.setBackgroundColor(ContextCompat.getColor(context, R.color.NASDAQ));
        }
    }
}
