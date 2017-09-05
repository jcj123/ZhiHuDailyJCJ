package com.jcj.royalni.zhihudailyjcj.utils;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

/**
 * Created by Royal Ni on 2017/7/5.
 */

public abstract class NewsAutoRefreshScollListener extends RecyclerView.OnScrollListener{
    private LinearLayoutManager layoutManager;
    private int totalItemCount;
    private int lastVisibleItemPosition;
    private boolean loading = false;

    public NewsAutoRefreshScollListener(LinearLayoutManager layoutManager) {
        this.layoutManager = layoutManager;
    }

    //当recyclerview滑动时调用此方法
    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        totalItemCount = layoutManager.getItemCount();
        lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();

        Log.d("NewsScollListener","totalItemCount="+totalItemCount+" lastVisibleItemPosition="+lastVisibleItemPosition);
        //在这里加loading的目的在于，如果不加的话，在lastVisibleItemPosition > totalItemCount -2的那一瞬间
        //很可能这个条件会满足若干次，loadMoreData()方法执行若干次导致数据错误
        if (!loading && lastVisibleItemPosition > totalItemCount -2 && dy>0) {
            loadMoreData();
            loading = true;
        }
    }

    public abstract void loadMoreData();

    public void setLoading(boolean loading) {
        this.loading = loading;
    }
}
