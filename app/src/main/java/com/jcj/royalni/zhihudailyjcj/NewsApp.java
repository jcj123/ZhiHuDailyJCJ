package com.jcj.royalni.zhihudailyjcj;

import android.app.Application;
import android.support.v7.app.AppCompatDelegate;

import com.jcj.royalni.zhihudailyjcj.db.NewsDatabase;
import com.jcj.royalni.zhihudailyjcj.utils.Constants;
import com.jcj.royalni.zhihudailyjcj.utils.DayNightHelper;
import com.jcj.royalni.zhihudailyjcj.utils.SPUtil;

/**
 * Created by Royal Ni on 2017/7/5.
 */

public final class NewsApp extends Application{
    private static NewsApp mGlobalContext;
    private static NewsDatabase database;

    @Override
    public void onCreate() {
        super.onCreate();
        mGlobalContext = this;
        database = new NewsDatabase(getApplicationContext());
    }

    public static NewsApp getInstance() {
        return mGlobalContext;
    }

    public static NewsDatabase getDatabase() {
        return database;
    }
}
