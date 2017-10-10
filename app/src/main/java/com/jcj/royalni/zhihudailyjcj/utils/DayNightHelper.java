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

    public boolean isNight() {
        final boolean isNight = spUtil.getBoolean(Constants.IS_NIGHT, true);
        return isNight;
    }

    public void setIsNight(boolean isNight) {
        spUtil.putBoolean(Constants.IS_NIGHT,isNight);
    }
}
