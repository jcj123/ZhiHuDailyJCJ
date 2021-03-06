package com.jcj.royalni.zhihudailyjcj.model;

import com.jcj.royalni.zhihudailyjcj.bean.Story;

import java.util.List;

/**
 * Created by jcj on 2017/8/5.
 */

public interface IHomePageModel {

    void loadLatestData(ILoadDataListener listener);

    void loadBeforeData(final String date,ILoadBeforeDataListener listener);

    void loadTopNews(List<Story> stories, ILoadTopNewsListener loadTopNewsListener);
}
