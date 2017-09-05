package com.jcj.royalni.zhihudailyjcj.model;

import com.jcj.royalni.zhihudailyjcj.bean.NewsList;
import com.jcj.royalni.zhihudailyjcj.bean.Story;

import java.util.List;

/**
 * Created by jcj on 2017/8/5.
 */

public interface ILoadDataListener {
    void loadLatestDataSucc(NewsList newsList);
    void loadLatestDataFail();

}
