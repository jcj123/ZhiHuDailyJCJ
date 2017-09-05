package com.jcj.royalni.zhihudailyjcj.view;

import com.jcj.royalni.zhihudailyjcj.bean.NewsDetail;
import com.jcj.royalni.zhihudailyjcj.bean.NewsList;
import com.jcj.royalni.zhihudailyjcj.bean.Story;
import com.jcj.royalni.zhihudailyjcj.bean.TopNews;

import java.util.List;

/**
 * Created by jcj on 2017/8/5.
 */

public interface IHomePageView {
    void loadLatestDataSucc(NewsList newsList);
    void loadLatestDataFail();

    void loadBeforeDataSucc(List<Story> newsList, String curDate);
    void loadBeforeDataFail();

    void loadTopNewsSucc(List<TopNews> topNewses);
}
