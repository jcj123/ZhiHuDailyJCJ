package com.jcj.royalni.zhihudailyjcj.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.widget.ImageView;
import android.widget.TextView;

import com.jcj.royalni.zhihudailyjcj.Obversable.ObservableFromHttp;
import com.jcj.royalni.zhihudailyjcj.R;
import com.jcj.royalni.zhihudailyjcj.bean.NewsDetail;
import com.jcj.royalni.zhihudailyjcj.bean.Story;

import butterknife.Bind;
import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by Royal Ni on 2017/7/7.
 */

public class NewsDetailActivity extends BaseActivity {
    public static final String NEWS_KEY = "newsKey";
    private Story mStory;
    @Bind(R.id.iv_detail)
    ImageView mDetailIv;
    @Bind(R.id.detail_tv)
    TextView mTvDetail;
    @Bind(R.id.tv_image_from)
    TextView mTvImageFrom;
    public static void startNewsDetailActivity(Context context, Story story) {
        Intent intent = new Intent(context,NewsDetailActivity.class);
        intent.putExtra(NEWS_KEY,story);
        context.startActivity(intent);
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        mStory = intent.getParcelableExtra(NEWS_KEY);
        showData(mStory);
    }

    private void showData(Story mStory) {
        int id = mStory.getId();
        ObservableFromHttp.getDetailNewsObservable(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<NewsDetail>() {
                    @Override
                    public void call(NewsDetail newsDetail) {
                        mTvDetail.setText(newsDetail.getTitle());
                    }
                });
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_detail_news;
    }
}
