package com.jcj.royalni.zhihudailyjcj.adapter;

import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.jcj.royalni.zhihudailyjcj.NewsApp;
import com.jcj.royalni.zhihudailyjcj.R;
import com.jcj.royalni.zhihudailyjcj.bean.NewsDetail;
import com.jcj.royalni.zhihudailyjcj.bean.Story;
import com.jcj.royalni.zhihudailyjcj.bean.TopNews;
import com.jcj.royalni.zhihudailyjcj.ui.NewsDetailActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jcj on 2017/8/19.
 */

public class TopNewsAdapter extends PagerAdapter{
    private List<TopNews>  datas;
    private List<Story> stories;
    public  TopNewsAdapter(List<TopNews>  datas, List<Story> stories){
        this.datas=datas;
        this.stories = stories;
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
    public Object instantiateItem(ViewGroup container, final int position) {//初始化一个页卡
        ImageView image = new ImageView(NewsApp.getInstance());
        image.setScaleType(ImageView.ScaleType.FIT_XY);// 基于控件大小填充图片
        image.setBackgroundResource(R.drawable.bg_shadow_mask);
        final TopNews topNews = datas.get(position);
        Glide.with(NewsApp.getInstance()).load(topNews.getImage()).into(image);//加载图片
        container.addView(image);

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Story s = null;
                for (Story story:stories) {
                    if (story.getId() == topNews.getId()){
                        s = story;
                    }
                }
                if (s != null) {
                    NewsDetailActivity.startNewsDetailActivity(NewsApp.getInstance(),s);
                }else {
                    if (listener != null) {
                        listener.goToNewsDetailPage(topNews);
                    }
                }
            }
        });// 设置触摸监听

        return image;
    }
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {//销毁一个页卡
        container.removeView((View) object);
    }

    private loadTopNewsNotTodayListener listener;

    public void setListener(loadTopNewsNotTodayListener listener) {
        this.listener = listener;
    }

    public interface loadTopNewsNotTodayListener{
        void goToNewsDetailPage(TopNews topNews);
    }
}
