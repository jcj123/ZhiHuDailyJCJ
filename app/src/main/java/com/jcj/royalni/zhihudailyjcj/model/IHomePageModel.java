package com.jcj.royalni.zhihudailyjcj.model;

/**
 * Created by jcj on 2017/8/5.
 */

public interface IHomePageModel {

    void loadLatestData(ILoadDataListener listener);

    void loadBeforeData(final String date,ILoadBeforeDataListener listener);
}
