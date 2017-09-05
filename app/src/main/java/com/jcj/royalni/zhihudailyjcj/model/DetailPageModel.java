package com.jcj.royalni.zhihudailyjcj.model;

import com.bumptech.glide.Glide;
import com.jcj.royalni.zhihudailyjcj.Obversable.ObservableFromHttp;
import com.jcj.royalni.zhihudailyjcj.bean.NewsDetail;
import com.jcj.royalni.zhihudailyjcj.bean.Story;
import com.jcj.royalni.zhihudailyjcj.ui.NewsDetailActivity;
import com.jcj.royalni.zhihudailyjcj.utils.HtmlUtil;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by jcj on 2017/8/6.
 */

public class DetailPageModel implements IDetailPageModel {
    @Override
    public void showData(Story story, final IShowDataListener listener) {
        int id = story.getId();
        ObservableFromHttp.getDetailNewsObservable(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<NewsDetail>() {
                    @Override
                    public void call(NewsDetail newsDetail) {
                        if (newsDetail!=null) {
                            listener.showSucc(newsDetail);
                        }else {
                            listener.showFail();
                        }
                    }
                });
    }
}
