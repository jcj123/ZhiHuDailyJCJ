package com.jcj.royalni.zhihudailyjcj.Obversable;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jcj.royalni.zhihudailyjcj.bean.News;
import com.jcj.royalni.zhihudailyjcj.bean.NewsList;
import com.jcj.royalni.zhihudailyjcj.net.Http;
import com.jcj.royalni.zhihudailyjcj.utils.Constants;

import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by Royal Ni on 2017/7/5.
 */

public class ObservableFromHttp {

    public static Observable<List<News>> getLatestDataObversable() {
        Observable<List<News>> observable = Observable.just(Constants.LATEST_NEWS_URL)
                .map(new Func1<String, List<News>>() {
                    @Override
                    public List<News> call(String s) {
                        String json = Http.getJsonFromHtml(s);
                        Gson gson = new Gson();
                        TypeToken<NewsList> newsListTypeToken = TypeToken.get(NewsList.class);
                        NewsList newsList = gson.fromJson(json, newsListTypeToken.getType());
                        List<News> newses = newsList.getStories();
                        return newses;
                    }
                });
        return observable;
    }
}
