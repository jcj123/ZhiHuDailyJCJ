package com.jcj.royalni.zhihudailyjcj.model;

import com.jcj.royalni.zhihudailyjcj.bean.NewsDetail;

import java.util.List;

/**
 * Created by jcj on 2017/8/19.
 */

public interface ILoadTopNewsListener {
    void loadTopNewsSucc(List<NewsDetail> newsDetails);
}
