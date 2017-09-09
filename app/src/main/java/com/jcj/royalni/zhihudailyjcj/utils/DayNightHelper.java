package com.jcj.royalni.zhihudailyjcj.utils;

import android.content.Context;

/**
 * Created by jcj on 2017/9/9.
 */

public class DayNightHelper {
    private Context context;
    private  SPUtil spUtil;

    public DayNightHelper(Context context) {
        this.context = context;
        spUtil = new SPUtil(context, Constants.SP_NAME);

    }

    public boolean isDay() {
        final boolean isDay = spUtil.getBoolean(Constants.IS_DAY, true);
        return isDay;
    }

    public void setIsDay(boolean isDay) {
        spUtil.putBoolean(Constants.IS_DAY,isDay);
    }
}
