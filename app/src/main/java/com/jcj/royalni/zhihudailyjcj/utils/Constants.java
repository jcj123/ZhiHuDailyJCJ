package com.jcj.royalni.zhihudailyjcj.utils;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by Royal Ni on 2017/7/5.
 */

public class Constants {
    public static final String BASIC_NEWS_URL = "https://news-at.zhihu.com/api/4/news/";
    public static final String BEFORE_NEWS_URL = BASIC_NEWS_URL+"before/";
    public static final String LATEST_NEWS_URL = BASIC_NEWS_URL+"latest";

    public static final String SP_NAME = "spName";
    public static final String IS_DAY = "isday";

    private Constants(){};

    public static final class Dates{
        public static final SimpleDateFormat FORMAT = new SimpleDateFormat("yyyyMMdd", Locale.US);
    }
}
