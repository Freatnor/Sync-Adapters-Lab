package com.example.freatnor.presenters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Jonathan Taylor on 8/22/16.
 */
public class StockRecyclerViewAdapter extends RecyclerView.Adapter {


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}

class StockViewHolder extends RecyclerView.ViewHolder{

    public StockViewHolder(View itemView) {
        super(itemView);
    }
}
