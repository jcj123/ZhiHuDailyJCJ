package com.jcj.royalni.zhihudailyjcj.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jcj.royalni.zhihudailyjcj.NewsApp;
import com.jcj.royalni.zhihudailyjcj.Obversable.ObservableFromHttp;
import com.jcj.royalni.zhihudailyjcj.adapter.NewsDateItemDecoration;
import com.jcj.royalni.zhihudailyjcj.bean.NewsDetail;
import com.jcj.royalni.zhihudailyjcj.bean.NewsList;
import com.jcj.royalni.zhihudailyjcj.bean.Story;
import com.jcj.royalni.zhihudailyjcj.bean.TopNews;
import com.jcj.royalni.zhihudailyjcj.db.NewsDatabase;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by jcj on 2017/8/5.
 */

public class HomePageModel implements IHomePageModel {
    private NewsDatabase newsDatabase;
    private String curDate;

    @Override
    public void loadLatestData(final ILoadDataListener listener) {
        ObservableFromHttp.getLatestDataObversable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<NewsList>() {
                    @Override
                    public void call(NewsList newsList) {
                        if (newsList != null) {
                            listener.loadLatestDataSucc(newsList);
                        } else {
                            listener.loadLatestDataFail();
                        }
                    }
                });
    }

    @Override
    public void loadBeforeData(final String date, final ILoadBeforeDataListener listener) {
        newsDatabase = NewsApp.getDatabase();
        ObservableFromHttp.getBeforeDataObversable(date)
                .map(new Func1<NewsList, List<Story>>() {
                    @Override
                    public List<Story> call(NewsList newsList) {
                        List<Story> newsHasRead = newsDatabase.queryIsRead();
                        List<Story> stories = newsList.getStories();
                        curDate = newsList.getDate();
                        for (Story story : stories) {
                            story.setDate(curDate);
                            if (newsHasRead.contains(story.getId())) {
                                story.setRead(true);
                            }
                        }
                        return stories;
                    }
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Story>>() {
                    @Override
                    public void call(List<Story> newsList) {
                        if (newsList != null) {
                            listener.loadBeforeDataSucc(newsList, curDate);
                        } else {
                            listener.loadBeforeDataFail();
                        }
                    }
                });

//        Observable.just(Constants.BEFORE_NEWS_URL)
//                .map(new Func1<String, List<Story>>() {
//                    @Override
//                    public List<Story> call(String s) {
//                        List<Story> newsHasRead = newsDatabase.queryIsRead();
//                        String json = Http.getJsonFromHtml(s, date);
//                        Gson gson = new Gson();
//                        TypeToken<NewsList> newsListTypeToken = TypeToken.get(NewsList.class);
//                        NewsList newsList = gson.fromJson(json, newsListTypeToken.getType());
//                        List<Story> stories = newsList.getStories();
//                        curDate = newsList.getDate();
//                        for (Story story : stories) {
//                            story.setDate(curDate);
//                            if (newsHasRead.contains(story.getId())) {
//                                story.setRead(true);
//                            }
//                        }
//                        return stories;
//                    }
//                })
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Action1<List<Story>>() {
//                    @Override
//                    public void call(List<Story> newsList) {
//                        if (newsList != null) {
//                            listener.loadBeforeDataSucc(newsList, curDate);
//                        } else {
//                            listener.loadBeforeDataFail();
//                        }
//                    }
//                });
    }

    @Override
    public void loadTopNews(final List<Story> stories, final ILoadTopNewsListener loadTopNewsListener) {
//        final List<NewsDetail> newsDetails = new ArrayList<NewsDetail>();
//        for (int i = 0; i < 5; i++) {
//            Observable<NewsDetail> observable = ObservableFromHttp.getDetailNewsObservable(stories.get(i).getId());
//            observable.subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(new Action1<NewsDetail>() {
//                        @Override
//                        public void call(NewsDetail newsDetail) {
//                            newsDetails.add(newsDetail);
//                            if (newsDetails.size() == 5) {
//                                loadTopNewsListener.loadTopNewsSucc(newsDetails);
//                            }
//                        }
//                    });
//        }

        Observable<NewsList> observable = ObservableFromHttp.getLatestDataObversable();
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<NewsList>() {
                    @Override
                    public void call(NewsList newsList) {
                        List<TopNews> top_stories = newsList.getTop_stories();
                        loadTopNewsListener.loadTopNewsSucc(top_stories);
                    }
                });
    }


}
