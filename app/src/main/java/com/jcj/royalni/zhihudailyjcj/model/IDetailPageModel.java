package com.jcj.royalni.zhihudailyjcj.model;

import com.jcj.royalni.zhihudailyjcj.bean.Story;

/**
 * Created by jcj on 2017/8/6.
 */

public interface IDetailPageModel {
    void showData(Story story,IShowDataListener listener);
}
