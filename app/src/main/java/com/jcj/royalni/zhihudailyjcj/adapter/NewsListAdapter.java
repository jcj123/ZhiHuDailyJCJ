package com.jcj.royalni.zhihudailyjcj.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jcj.royalni.zhihudailyjcj.R;
import com.jcj.royalni.zhihudailyjcj.bean.News;
import com.jcj.royalni.zhihudailyjcj.bean.NewsList;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Royal Ni on 2017/7/5.
 */

public class NewsListAdapter extends RecyclerView.Adapter<NewsListAdapter.NewsListViewHolder>{
    private List<News> newsLists = new ArrayList<>();

    public NewsListAdapter(List<News> newsLists) {
        this.newsLists = newsLists;
    }

    public void setNewsLists(List<News> newsLists) {
        this.newsLists = newsLists;
    }

    public List<News> getNewsLists() {
        return newsLists;
    }

    public void updateData(List<News> newsLists) {
        setNewsLists(newsLists);
        notifyDataSetChanged();
    }

    @Override
    public NewsListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news_list,parent,false);
        NewsListViewHolder holder = new NewsListViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(NewsListViewHolder holder, int position) {
        News news = newsLists.get(position);
        holder.tvTitle.setText(news.getTitle());
    }

    @Override
    public int getItemCount() {
        return newsLists == null ? 0:newsLists.size();
    }

    class NewsListViewHolder extends RecyclerView.ViewHolder{
        @Bind(R.id.iv_news)
        ImageView ivNews;
        @Bind(R.id.tv_title)
        TextView tvTitle;
        public NewsListViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
