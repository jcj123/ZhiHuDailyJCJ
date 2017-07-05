package com.jcj.royalni.zhihudailyjcj;

import android.app.Application;

/**
 * Created by Royal Ni on 2017/7/5.
 */

public final class NewsApp extends Application{
    private static NewsApp mGlobalContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mGlobalContext = this;
    }

    public static NewsApp getInstance() {
        return mGlobalContext;
    }
}
