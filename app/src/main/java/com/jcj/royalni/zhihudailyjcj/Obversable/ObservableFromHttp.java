package com.jcj.royalni.zhihudailyjcj.Obversable;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jcj.royalni.zhihudailyjcj.bean.NewsDetail;
import com.jcj.royalni.zhihudailyjcj.bean.NewsList;
import com.jcj.royalni.zhihudailyjcj.bean.Story;
import com.jcj.royalni.zhihudailyjcj.net.Http;
import com.jcj.royalni.zhihudailyjcj.utils.Constants;

import java.lang.reflect.Type;
import java.util.List;

import rx.Observable;
import rx.functions.Action0;
import rx.functions.Func1;

/**
 * Created by Royal Ni on 2017/7/5.
 */

public class ObservableFromHttp {
    private static final String TAG = "ObservableFromHttp";

    public static Observable<NewsList> getLatestDataObversable() {
        Observable<NewsList> observable = Observable.just(Constants.LATEST_NEWS_URL)
                .map(new Func1<String, NewsList>() {
                    @Override
                    public NewsList call(String s) {
                        String json = Http.getJsonFromHtml(s);
                        Gson gson = new Gson();
                        Type type = TypeToken.get(NewsList.class).getType();
                        NewsList newsList = gson.fromJson(json, type);
                        return newsList;
                    }
                }).map(new Func1<NewsList, NewsList>() {
                    @Override
                    public NewsList call(NewsList newsList) {
                        saveNewsesIntoDB(newsList);
                        return newsList;
                    }
                });
        return observable;
    }

    public static Observable<NewsDetail> getDetailNewsObservable(int id) {
        String url = Constants.BASIC_NEWS_URL + id;
        Observable<NewsDetail> observable = Observable
                .just(url)
                .map(new Func1<String, NewsDetail>() {
                    @Override
                    public NewsDetail call(String s) {
                        String json = Http.getJsonFromHtml(s);
                        Gson gson = new Gson();
                        Type type = TypeToken.get(NewsDetail.class).getType();
                        NewsDetail detail = gson.fromJson(json,type);
                        Log.d(TAG,detail.toString());
                        return detail;
                    }
                });
        return observable;
    }


    public static Observable<NewsList> getBeforeDataObversable(final String date) {
        Observable<NewsList> observable = Observable.just(Constants.BEFORE_NEWS_URL)
                .map(new Func1<String, NewsList>() {
                    @Override
                    public NewsList call(String s) {
                        String json = Http.getJsonFromHtml(s,date);
                        Gson gson = new Gson();
                        Type type = TypeToken.get(NewsList.class).getType();
                        NewsList newsList = gson.fromJson(json, type);
                        return newsList;
                    }
                }).map(new Func1<NewsList, NewsList>() {
                    @Override
                    public NewsList call(NewsList newsList) {
                        saveNewsesIntoDB(newsList);
                        return newsList;
                    }
                });
        return observable;
    }

    private static void saveNewsesIntoDB(NewsList newsList) {
        System.out.println("将信息存入到数据库中");
    }
}
