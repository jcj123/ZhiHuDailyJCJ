package com.jcj.royalni.zhihudailyjcj.model;

import com.jcj.royalni.zhihudailyjcj.bean.NewsDetail;

/**
 * Created by jcj on 2017/8/6.
 */

public interface IShowDataListener {
    void showSucc(NewsDetail newsDetail);
    void showFail();
}
