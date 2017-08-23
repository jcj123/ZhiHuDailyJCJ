package com.jcj.royalni.zhihudailyjcj.ui;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jcj.royalni.zhihudailyjcj.NewsApp;
import com.jcj.royalni.zhihudailyjcj.Obversable.ObservableFromHttp;
import com.jcj.royalni.zhihudailyjcj.R;
import com.jcj.royalni.zhihudailyjcj.adapter.NewsDateItemDecoration;
import com.jcj.royalni.zhihudailyjcj.adapter.NewsListAdapter;
import com.jcj.royalni.zhihudailyjcj.adapter.NewsListAdapterWrapper;
import com.jcj.royalni.zhihudailyjcj.adapter.TopNewsAdapter;
import com.jcj.royalni.zhihudailyjcj.bean.NewsDetail;
import com.jcj.royalni.zhihudailyjcj.bean.NewsList;
import com.jcj.royalni.zhihudailyjcj.bean.Story;
import com.jcj.royalni.zhihudailyjcj.db.NewsDatabase;
import com.jcj.royalni.zhihudailyjcj.presenter.HomePagePresenter;
import com.jcj.royalni.zhihudailyjcj.utils.DensityUtils;
import com.jcj.royalni.zhihudailyjcj.utils.NewsAutoRefreshScollListener;
import com.jcj.royalni.zhihudailyjcj.utils.ToastUtil;
import com.jcj.royalni.zhihudailyjcj.view.IHomePageView;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import static android.os.Build.VERSION_CODES.N;

public class MainActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, IHomePageView, ViewPager.OnPageChangeListener {
    @Bind(R.id.swipe_refreshlayout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @Bind(R.id.rv_news)
    RecyclerView mRecyclerView;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.iv_empty)
    ImageView ivTip;
    @Bind(R.id.tv_empty_tip)
    TextView tvTip;
    @Bind(R.id.bt_empty_button)
    Button btEmptyButton;
    @Bind(R.id.empty_view)
    View emptyView;
    private String curDate;
    private List<Story> stories = new ArrayList<>();
    private NewsListAdapter mAdapter;
    private NewsListAdapterWrapper mAdapterWrapper;

    private NewsAutoRefreshScollListener mNewsAutoRefreshScollListener;
    private boolean onlyAddItemDecorationOnce = true;
    private NewsDateItemDecoration newsDateItemDecoration;
    private NewsDatabase newsDatabase;
    private List newsHasRead;

    private HomePagePresenter presenter;
    private RelativeLayout mRlTopNews;
    private CirclePageIndicator mIndicator;
    private TextView mTopNewsTitle;
    private ViewPager viewPager;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        mSwipeRefreshLayout.setOnRefreshListener(this);

        mToolbar.setTitle("首页");
        mToolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(mToolbar);
        Window window = getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        }
        presenter = new HomePagePresenter(this);
        newsDatabase = NewsApp.getDatabase();

        RecyclerView.LayoutManager ll = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mAdapter = new NewsListAdapter(stories);
        mAdapterWrapper = new NewsListAdapterWrapper(mAdapter);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(ll);

        mRlTopNews = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.header_viewpager, null);
        viewPager = (ViewPager) mRlTopNews.findViewById(R.id.vp);
        initHeaderDatas();
        viewPager.addOnPageChangeListener(this);
        mIndicator = (CirclePageIndicator) mRlTopNews.findViewById(R.id.indicator);
        mTopNewsTitle = (TextView) mRlTopNews.findViewById(R.id.tv_title);
        mAdapterWrapper.addHeaderView(mRlTopNews);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapterWrapper);

        mNewsAutoRefreshScollListener = new NewsAutoRefreshScollListener((LinearLayoutManager) ll) {
            @Override
            public void loadMoreData() {
                presenter.loadBeforeData(curDate);
            }
        };
        mRecyclerView.addOnScrollListener(mNewsAutoRefreshScollListener);
    }

    private void initHeaderDatas() {
        if (stories.size() !=0 ) {
           presenter.loadTopNews(stories);
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (mAdapter.getStories().size() == 0) {
            presenter.loadLatestData();
        }
    }

    @Override
    public void onRefresh() {
        presenter.loadLatestData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.news_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void loadBeforeDataSucc(List<Story> newsList, String curDate) {
        this.curDate = curDate;
        mNewsAutoRefreshScollListener.setLoading(false);
        stories.addAll(newsList);
        mAdapter.updateData(stories);
        initHeaderDatas();
        mAdapterWrapper.notifyDataSetChanged();
        //不知道为什么不加这一句，在用完刷新最新新闻以后总是会报错，
        // 读取到的信息总是不能赋值给newsDateItemDecoration中
        newsDateItemDecoration.setStories(stories);
    }

    @Override
    public void loadLatestDataSucc(NewsList newsList) {
        mSwipeRefreshLayout.setRefreshing(true);
        stories = newsList.getStories();
        newsHasRead = newsDatabase.queryIsRead();
        if (stories != null) {
            curDate = newsList.getDate();
            for (Story story : stories) {
                story.setDate(curDate);
                if (newsHasRead.contains(story.getId())) {
                    story.setRead(true);
                }
            }
            if (onlyAddItemDecorationOnce) {
                onlyAddItemDecorationOnce = false;
                newsDateItemDecoration = new NewsDateItemDecoration(MainActivity.this, stories, mToolbar);
                mRecyclerView.addItemDecoration(newsDateItemDecoration);
            }

            mAdapter.updateData(stories);
            initHeaderDatas();
            mAdapterWrapper.notifyDataSetChanged();
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    private void initErrorView() {
        ivTip.setImageDrawable(getResources().getDrawable(R.drawable.failed_tip_no_wifi));
        tvTip.setText("当前网络出现了问题，请检查网络");
        btEmptyButton.setText("点击刷新");
        btEmptyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showErrorView(false);
                presenter.loadLatestData();
            }
        });
    }

    private void showErrorView(boolean showEmptyView) {
        if (showEmptyView) {
            ToastUtil.show("网络错误");
            mSwipeRefreshLayout.setVisibility(View.GONE);
            mSwipeRefreshLayout.setRefreshing(false);
            emptyView.setVisibility(View.VISIBLE);
            mAdapter.updateData(Collections.EMPTY_LIST);
        } else {
            mSwipeRefreshLayout.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);

        }
    }

    @Override
    public void loadLatestDataFail() {
        initErrorView();
        showErrorView(true);
    }

    @Override
    public void loadBeforeDataFail() {
        initErrorView();
        showErrorView(true);
    }
    List<NewsDetail> newsDetails;
    @Override
    public void loadTopNewsSucc(List<NewsDetail> newsDetails) {
        this.newsDetails = newsDetails;
        TopNewsAdapter topNewsAdapter = new TopNewsAdapter(newsDetails,stories);
        viewPager.setAdapter(topNewsAdapter);

        mIndicator.setViewPager(viewPager);
        mIndicator.setSnap(true);// 支持快照显示
        mIndicator.setOnPageChangeListener(this);
        mIndicator.onPageSelected(0);

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        NewsDetail newsDetail = newsDetails.get(position);
        mTopNewsTitle.setText(newsDetail.getTitle());
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
