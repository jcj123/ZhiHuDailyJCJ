package com.jcj.royalni.zhihudailyjcj.presenter;

import com.jcj.royalni.zhihudailyjcj.bean.NewsList;
import com.jcj.royalni.zhihudailyjcj.bean.Story;
import com.jcj.royalni.zhihudailyjcj.model.HomePageModel;
import com.jcj.royalni.zhihudailyjcj.model.IHomePageModel;
import com.jcj.royalni.zhihudailyjcj.model.ILoadBeforeDataListener;
import com.jcj.royalni.zhihudailyjcj.model.ILoadDataListener;
import com.jcj.royalni.zhihudailyjcj.view.IHomePageView;

import java.util.List;

/**
 * Created by jcj on 2017/8/5.
 */

public class HomePagePresenter {
    private IHomePageView view;
    private IHomePageModel model;

    public HomePagePresenter(IHomePageView view) {
        model = new HomePageModel();
        this.view = view;
    }

    public void loadLatestData() {
        model.loadLatestData(new ILoadDataListener() {
            @Override
            public void loadLatestDataSucc(NewsList newsList) {
                view.loadLatestDataSucc(newsList);
            }

            @Override
            public void loadLatestDataFail() {
                view.loadLatestDataFail();
            }
        });
    }

    public void loadBeforeData(final String date) {
        model.loadBeforeData(date, new ILoadBeforeDataListener() {
            @Override
            public void loadBeforeDataSucc(List<Story> newsList,String curDate) {
                view.loadBeforeDataSucc(newsList,curDate);
            }

            @Override
            public void loadBeforeDataFail() {

            }
        });

    }
}
