package com.jcj.royalni.zhihudailyjcj.utils;

import android.widget.Toast;

import com.jcj.royalni.zhihudailyjcj.NewsApp;

/**
 * @data 2017/7/25 15:59
 */

public class ToastUtil {

        /**
     * @param oldMsg 旧内容
     * @param oneTime 第一次时间
     * @param twoTime 第二次时间
     * 判断是否存在Toast，没有则创建
     * 有则判断内容是否一样，一样判断之前Toast是否消失，是则产生新的Toast，否则不显示
     * 内容不一样则直接显示新的Toast信息
     */
    private static Toast toast;

    public static void show(String message) {
        if (toast == null) {
            toast = Toast.makeText(NewsApp.getInstance(), message, Toast.LENGTH_SHORT);
        } else {
            toast.setText(message);
        }
        toast.show();
    }
}