package com.jcj.royalni.zhihudailyjcj.ui;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jcj.royalni.zhihudailyjcj.Obversable.ObservableFromHttp;
import com.jcj.royalni.zhihudailyjcj.R;
import com.jcj.royalni.zhihudailyjcj.adapter.NewsListAdapter;
import com.jcj.royalni.zhihudailyjcj.bean.News;
import com.jcj.royalni.zhihudailyjcj.bean.NewsList;
import com.jcj.royalni.zhihudailyjcj.net.Http;
import com.jcj.royalni.zhihudailyjcj.utils.Constants;
import com.jcj.royalni.zhihudailyjcj.utils.NewsScollListener;

import java.util.ArrayList;
import java.util.List;

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
    private List<News> newses = new ArrayList<>();
    private NewsListAdapter mAdapter;
    private NewsScollListener newsScollListener;

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        mSwipeRefreshLayout.setOnRefreshListener(this);

        setSupportActionBar(mToolbar);

        RecyclerView.LayoutManager ll = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        mAdapter = new NewsListAdapter(newses);
        mRecyclerView.setLayoutManager(ll);
        mRecyclerView.setAdapter(mAdapter);
        newsScollListener = new NewsScollListener((LinearLayoutManager)ll) {
            @Override
            public void loadMoreData() {
                loadBeforeData(curDate);
            }
        };
        mRecyclerView.addOnScrollListener(newsScollListener);
    }

    private void loadBeforeData(final String date) {
        Observable.just(Constants.BEFORE_NEWS_URL)
                .map(new Func1<String, List<News>>() {
                    @Override
                    public List<News> call(String s) {
                        String json = Http.getJsonFromHtml(s,date);
                        Gson gson = new Gson();
                        TypeToken<NewsList> newsListTypeToken = TypeToken.get(NewsList.class);
                        NewsList newsList = gson.fromJson(json, newsListTypeToken.getType());
                        List<News> newses = newsList.getStories();
                        curDate = newsList.getDate();
                        return newses;
                    }
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<News>>() {
                    @Override
                    public void call(List<News> newsList) {
                        newsScollListener.setLoading(false);
                        System.out.println("Action1");
                        newses.addAll(newsList);
                        mAdapter.updateData(newses);
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mAdapter.getNewsLists().size() == 0) {
            loadLatestData();
        }
    }

    private void loadLatestData() {
        Observable.just(Constants.LATEST_NEWS_URL)
                .map(new Func1<String, List<News>>() {
                    @Override
                    public List<News> call(String s) {
                        String json = Http.getJsonFromHtml(s);
                        Gson gson = new Gson();
                        TypeToken<NewsList> newsListTypeToken = TypeToken.get(NewsList.class);
                        NewsList newsList = gson.fromJson(json, newsListTypeToken.getType());
                        List<News> newses = newsList.getStories();
                        curDate = newsList.getDate();
                        System.out.println("curDate="+curDate);
                        return newses;
                    }
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<News>>() {
                    @Override
                    public void onCompleted() {
                        mAdapter.updateData(newses);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<News> newsList) {
                        newses = newsList;

                        System.out.println(newses);
                    }
                });
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_main;
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

    @Override
    public void onRefresh() {

    }
}
