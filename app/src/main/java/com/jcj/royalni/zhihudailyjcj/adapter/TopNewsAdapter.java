package com.jcj.royalni.zhihudailyjcj.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.jcj.royalni.zhihudailyjcj.NewsApp;
import com.jcj.royalni.zhihudailyjcj.bean.NewsDetail;

import java.util.List;

/**
 * Created by jcj on 2017/8/19.
 */

public class TopNewsAdapter extends PagerAdapter{
    private List<NewsDetail> datas;
    public  TopNewsAdapter(List<NewsDetail> datas ){
        this.datas=datas;
    }
    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
    @Override
    public Object instantiateItem(ViewGroup container, int position) {//初始化一个页卡
        ImageView image = new ImageView(NewsApp.getInstance());
        image.setScaleType(ImageView.ScaleType.FIT_XY);// 基于控件大小填充图片

        NewsDetail newsDetail = datas.get(position);
//        utils.display(image, topNewsData.topimage);// 传递imagView对象和图片地址
        Glide.with(NewsApp.getInstance()).load(newsDetail.getImage()).into(image);
        container.addView(image);

//        image.setOnTouchListener(new TopNewsTouchListener());// 设置触摸监听

        return image;
    }
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {//销毁一个页卡
        container.removeView((View) object);
    }
}
