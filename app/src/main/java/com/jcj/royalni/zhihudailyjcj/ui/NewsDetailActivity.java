package com.jcj.royalni.zhihudailyjcj.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.widget.SlidingPaneLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jcj.royalni.zhihudailyjcj.R;
import com.jcj.royalni.zhihudailyjcj.bean.NewsDetail;
import com.jcj.royalni.zhihudailyjcj.bean.Story;
import com.jcj.royalni.zhihudailyjcj.model.DetailPagePresenter;
import com.jcj.royalni.zhihudailyjcj.utils.HtmlUtil;
import com.jcj.royalni.zhihudailyjcj.view.IDetailPageView;

import butterknife.Bind;

/**
 * Created by Royal Ni on 2017/7/7.
 */

public class NewsDetailActivity extends BaseActivity implements IDetailPageView, View.OnClickListener {
    public static final String NEWS_KEY = "newsKey";
    private Story mStory;
    @Bind(R.id.iv_detail)
    ImageView mDetailIv;
    @Bind(R.id.detail_tv)
    TextView mTvDetail;
    @Bind(R.id.tv_image_from)
    TextView mTvImageFrom;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.collapsing_toolbar_layout)
    CollapsingToolbarLayout mCollapsingToolbarLayout;
    @Bind(R.id.detail_webview)
    WebView mDetailWebview;

    private DetailPagePresenter presenter;

    //通过这种方式启动activit十分科学，可以让其他意图启动这个activity的人知道该传入什么参数进来
    public static void startNewsDetailActivity(Context context, Story story) {
        Intent intent = new Intent(context, NewsDetailActivity.class);
        intent.putExtra(NEWS_KEY, story);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        mStory = intent.getParcelableExtra(NEWS_KEY);
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        mToolbar.setNavigationOnClickListener(this);
        presenter = new DetailPagePresenter(this, mStory);
        presenter.showData();
    }

    @Override
    protected void onDestroy() {
        if (mDetailWebview != null) {
            mDetailWebview.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            mDetailWebview.clearHistory();
            ((ViewGroup) mDetailWebview.getParent()).removeView(mDetailWebview);
            mDetailWebview.destroy();
            mDetailWebview = null;
        }
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_detail_news;
    }

    @Override
    public void showSucc(NewsDetail newsDetail) {
        mTvDetail.setText(newsDetail.getTitle());
        mTvImageFrom.setText(newsDetail.getImage_source());
        Glide.with(NewsDetailActivity.this).load(newsDetail.getImage()).into(mDetailIv);

        mDetailWebview.setDrawingCacheEnabled(true);
        String htmlSource = HtmlUtil.createHtmlData(newsDetail, false);
        //这样显示会在格式上出现一点问题
//      mDetailWebview.loadDataWithBaseURL(null, htmlSource,"text/html", "utf-8", null);
        mDetailWebview.loadData(htmlSource, HtmlUtil.MIME_TYPE, HtmlUtil.ENCODING);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.share:

                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showFail() {

    }

    @Override
    public void onClick(View v) {
        finish();
    }
}
