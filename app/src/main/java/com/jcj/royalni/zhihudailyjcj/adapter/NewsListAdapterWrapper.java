package com.jcj.royalni.zhihudailyjcj.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by jcj on 2017/8/19.
 */

public class NewsListAdapterWrapper extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private NewsListAdapter mAdapter;
    private View mHeaderView;

    private static int HEADER = 0;
    private static int NORMAL = 1;

    public NewsListAdapterWrapper(NewsListAdapter mAdapter) {
        this.mAdapter = mAdapter;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return HEADER;
        }else {
            return NORMAL;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == HEADER) {
            return new RecyclerView.ViewHolder(mHeaderView) {};
        }else {
            return mAdapter.onCreateViewHolder(parent,viewType);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (position == 0) {

            return ;
        }else {
            mAdapter.onBindViewHolder((NewsListAdapter.NewsListViewHolder) holder,position-1);
        }
    }

    @Override
    public int getItemCount() {
        return mAdapter.getItemCount()+1;
    }

    public void addHeaderView(View mHeaderView){
        this.mHeaderView = mHeaderView;
    }
}
