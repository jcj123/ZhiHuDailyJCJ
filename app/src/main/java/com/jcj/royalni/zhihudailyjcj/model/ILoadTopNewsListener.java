package com.jcj.royalni.zhihudailyjcj.model;

import com.jcj.royalni.zhihudailyjcj.bean.NewsDetail;
import com.jcj.royalni.zhihudailyjcj.bean.TopNews;

import java.util.List;

/**
 * Created by jcj on 2017/8/19.
 */

public interface ILoadTopNewsListener {
    void loadTopNewsSucc(List<TopNews> topNewses);
}
