package com.jcj.royalni.zhihudailyjcj.adapter;

import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jcj.royalni.zhihudailyjcj.NewsApp;
import com.jcj.royalni.zhihudailyjcj.R;
import com.jcj.royalni.zhihudailyjcj.bean.Story;
import com.jcj.royalni.zhihudailyjcj.db.NewsDatabase;
import com.jcj.royalni.zhihudailyjcj.ui.NewsDetailActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Royal Ni on 2017/7/5.
 */

public class NewsListAdapter extends RecyclerView.Adapter<NewsListAdapter.NewsListViewHolder> {
    //    private static final int ITEM_NEWS = 0;
//    private static final int ITEM_NEWS_HEADER = 1;
    private List<Story> stories = new ArrayList<>();

    private NewsDatabase database;
    private List newsHasRead;
    private boolean anim = true;
    private long lastPos = -1;

    public void setNewsHasRead(List newsHasRead) {
        this.newsHasRead = newsHasRead;
    }

    public NewsListAdapter(List<Story> stories) {
        this.stories = stories;
        database = NewsApp.getDatabase();
    }

    public void setStories(List<Story> stories) {
        this.stories = stories;
    }

    public List<Story> getStories() {
        return stories;
    }

//    public void updateData(List<Story> stories) {
//        setStories(stories);
////        notifyDataSetChanged();
//    }


    @Override
    public NewsListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        switch (viewType) {
//            case ITEM_NEWS:
//                View viewNews = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news_list, parent, false);
//                NewsListViewHolder holder = new NewsListViewHolder(viewNews);
//                return holder;
//            case ITEM_NEWS_HEADER:
//                View  viewNewsDate = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news_header, parent, false);
//                NewsListViewHolder holder1 = new NewsDateViewHolder(viewNewsDate);
//                return holder1;
//
//        }
        View viewNews = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news_list, parent, false);
        NewsListViewHolder holder = new NewsListViewHolder(viewNews);
        return holder;
    }

    @Override
    public void onBindViewHolder(final NewsListViewHolder holder, int position) {
        final Story story = stories.get(position);
//        if (holder instanceof NewsDateViewHolder) {
//            NewsDateViewHolder  dateHolder = (NewsDateViewHolder)holder;
//            String date = DateUtil.formatDate(story.getDate());
//            dateHolder.tv_date.setText(date);
//        }else {

        holder.tvTitle.setText(story.getTitle());
        if (story.isRead()) {
            holder.tvTitle.setTextColor(Color.GRAY);
        }else {
            holder.tvTitle.setTextColor(Color.BLACK);
        }
        String imageUrl = story.getImages().get(0);
        Glide.with(NewsApp.getInstance()).load(imageUrl).into(holder.ivNews);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!story.isRead()) {
                    holder.tvTitle.setTextColor(Color.GRAY);
                    //将信息更新至数据库中
                    story.setRead(true);
                    database.insert(story);
                }else {
                    holder.tvTitle.setTextColor(Color.GRAY);
                }
                NewsDetailActivity.startNewsDetailActivity(NewsApp.getInstance(), story);
            }
        });
        if (anim) {
            startAnimation(holder.cardView,position);
        }
    }

    private void startAnimation(View view, int position) {
        if (position > lastPos) {
            view.startAnimation(AnimationUtils.loadAnimation(NewsApp.getInstance(), R.anim.item_bottom_in));
            lastPos = position;
        }
    }

    @Override
    public int getItemCount() {
        return stories == null ? 0 : stories.size();
    }



    class NewsListViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.iv_news)
        ImageView ivNews;
        @Bind(R.id.tv_title)
        TextView tvTitle;
        @Bind(R.id.cv_item)
        CardView cardView;

        public NewsListViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class NewsDateViewHolder extends NewsListViewHolder {
        @Bind(R.id.news_header)
        TextView tv_date;

        public NewsDateViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


    //    @Override
//    public int getItemViewType(int position) {
//
//        if (position == 0) {
//            return ITEM_NEWS;
//        }
//        String curDate = stories.get(position).getDate();
//        int preIndex = position - 1;
//        boolean isDifferent = !curDate.equals(stories.get(preIndex).getDate());
//        return isDifferent ? ITEM_NEWS_HEADER : ITEM_NEWS;
//    }
}
