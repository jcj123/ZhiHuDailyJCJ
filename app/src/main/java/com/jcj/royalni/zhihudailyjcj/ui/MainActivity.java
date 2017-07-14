package com.jcj.royalni.zhihudailyjcj.ui;

import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jcj.royalni.zhihudailyjcj.Obversable.ObservableFromHttp;
import com.jcj.royalni.zhihudailyjcj.R;
import com.jcj.royalni.zhihudailyjcj.adapter.NewsDateItemDecoration;
import com.jcj.royalni.zhihudailyjcj.adapter.NewsListAdapter;
import com.jcj.royalni.zhihudailyjcj.bean.Story;
import com.jcj.royalni.zhihudailyjcj.bean.NewsList;
import com.jcj.royalni.zhihudailyjcj.net.Http;
import com.jcj.royalni.zhihudailyjcj.utils.Constants;
import com.jcj.royalni.zhihudailyjcj.utils.NewsAutoRefreshScollListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import butterknife.Bind;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class MainActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {
    private static final String Tag = "MainActivity";

    @Bind(R.id.swipe_refreshlayout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @Bind(R.id.rv_news)
    RecyclerView mRecyclerView;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    private String curDate;
    private List<Story> stories = new ArrayList<>();
    private NewsListAdapter mAdapter;
    private NewsAutoRefreshScollListener mNewsAutoRefreshScollListener;
    private boolean onlyAddItemDecorationOnce = true;
    private NewsDateItemDecoration newsDateItemDecoration;

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        mSwipeRefreshLayout.setOnRefreshListener(this);

        mToolbar.setTitle("首页");
        mToolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(mToolbar);

        RecyclerView.LayoutManager ll = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        mAdapter = new NewsListAdapter(stories);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(ll);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);
        mNewsAutoRefreshScollListener = new NewsAutoRefreshScollListener((LinearLayoutManager)ll) {
            @Override
            public void loadMoreData() {
                loadBeforeData(curDate);
            }
        };
        mRecyclerView.addOnScrollListener(mNewsAutoRefreshScollListener);
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (mAdapter.getStories().size() == 0) {
            loadLatestData();
        }
    }

    /**
     * 按日期加载旧信息
     * @param date
     */
    private void loadBeforeData(final String date) {
        Observable.just(Constants.BEFORE_NEWS_URL)
                .map(new Func1<String, List<Story>>() {
                    @Override
                    public List<Story> call(String s) {
                        String json = Http.getJsonFromHtml(s,date);
                        Gson gson = new Gson();
                        TypeToken<NewsList> newsListTypeToken = TypeToken.get(NewsList.class);
                        NewsList newsList = gson.fromJson(json, newsListTypeToken.getType());
                        List<Story> stories = newsList.getStories();
                        curDate = newsList.getDate();
                        for(Story story : stories) {
                            story.setDate(curDate);
                        }
                        return stories;
                    }
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Story>>() {
                    @Override
                    public void call(List<Story> newsList) {
                        mNewsAutoRefreshScollListener.setLoading(false);
                        stories.addAll(newsList);
                        mAdapter.updateData(stories);
                        //不知道为什么不加这一句，在用完刷新最新新闻以后总是会报错，
                        // 读取到的信息总是不能赋值给newsDateItemDecoration中
                        newsDateItemDecoration.setStories(stories);
                    }
                });
    }
    /**
     * 加载最新消息
     */
    private void loadLatestData() {
        ObservableFromHttp.getLatestDataObversable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<NewsList>() {
                    @Override
                    public void call(NewsList newsList) {
                        stories = newsList.getStories();
                        if (stories != null) {
                            curDate = newsList.getDate();
                            for(Story story : stories) {
                                story.setDate(curDate);
                            }
                            if (onlyAddItemDecorationOnce) {
                                onlyAddItemDecorationOnce = false;
                                newsDateItemDecoration = new NewsDateItemDecoration(MainActivity.this, stories);
                                mRecyclerView.addItemDecoration(newsDateItemDecoration);
                            }
                            mAdapter.updateData(stories);
                            mSwipeRefreshLayout.setRefreshing(false);
                        }
                    }
                });
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_main;
    }

    @Override
    public void onRefresh() {
        loadLatestData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.news_main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

        }
        return super.onOptionsItemSelected(item);
    }

}
