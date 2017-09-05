package com.jcj.royalni.zhihudailyjcj.model;

import com.jcj.royalni.zhihudailyjcj.bean.NewsDetail;
import com.jcj.royalni.zhihudailyjcj.bean.Story;
import com.jcj.royalni.zhihudailyjcj.view.IDetailPageView;

/**
 * Created by jcj on 2017/8/6.
 */

public class DetailPagePresenter {
    private IDetailPageModel model;
    private IDetailPageView view;
    private Story story;

    public DetailPagePresenter(IDetailPageView view,Story story) {
        this.view = view;
        model = new DetailPageModel();
        this.story = story;
    }


    public void showData() {
        model.showData(story, new IShowDataListener() {
            @Override
            public void showSucc(NewsDetail newsDetail) {
                view.showSucc(newsDetail);
            }

            @Override
            public void showFail() {
                view.showFail();
            }
        });
    }
}
