package com.jcj.royalni.zhihudailyjcj.ui;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jcj.royalni.zhihudailyjcj.NewsApp;
import com.jcj.royalni.zhihudailyjcj.R;
import com.jcj.royalni.zhihudailyjcj.adapter.NewsDateItemDecoration;
import com.jcj.royalni.zhihudailyjcj.adapter.NewsListAdapter;
import com.jcj.royalni.zhihudailyjcj.adapter.NewsListAdapterWrapper;
import com.jcj.royalni.zhihudailyjcj.adapter.TopNewsAdapter;
import com.jcj.royalni.zhihudailyjcj.bean.NewsDetail;
import com.jcj.royalni.zhihudailyjcj.bean.NewsList;
import com.jcj.royalni.zhihudailyjcj.bean.Story;
import com.jcj.royalni.zhihudailyjcj.bean.TopNews;
import com.jcj.royalni.zhihudailyjcj.db.NewsDatabase;
import com.jcj.royalni.zhihudailyjcj.presenter.HomePagePresenter;
import com.jcj.royalni.zhihudailyjcj.utils.DiffCallbackUtil;
import com.jcj.royalni.zhihudailyjcj.utils.HtmlUtil;
import com.jcj.royalni.zhihudailyjcj.utils.NewsAutoRefreshScollListener;
import com.jcj.royalni.zhihudailyjcj.utils.ToastUtil;
import com.jcj.royalni.zhihudailyjcj.view.IHomePageView;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.Bind;

public class MainActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, IHomePageView, ViewPager.OnPageChangeListener, TopNewsAdapter.loadTopNewsNotTodayListener {
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
    List<TopNews> topNewses = new ArrayList<>();
    private List<Story> mOldStories = new ArrayList<>();
    private List<Story> mNewStories = new ArrayList<>();
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
        if (HtmlUtil.isNetworkAvailable(this)) {
            presenter.loadLatestData();
        }else {
            loadLatestDataFail();
        }
    }

    @Override
    public void onRefresh() {
        if (HtmlUtil.isNetworkAvailable(this)) {
            presenter.loadLatestData();
        }else {
            loadLatestDataFail();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.news_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_action_daynight:
                boolean isNight = dayNightHelper.isNight();
                if (isNight) {
                    dayNightHelper.setIsNight(false);
                    getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    recreate();
                }else {
                    dayNightHelper.setIsNight(true);
                    getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    recreate();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void loadBeforeDataSucc(List<Story> newsList, String curDate) {
        this.curDate = curDate;
        mNewsAutoRefreshScollListener.setLoading(false);
        for (Story story:stories) {
            if (!mOldStories.contains(story)) {
                mOldStories.add(story);
            }
        }
        stories.addAll(newsList);
        mNewStories = stories;
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffCallbackUtil(mOldStories, mNewStories), true);
        diffResult.dispatchUpdatesTo(mAdapterWrapper);
        mAdapter.setStories(stories);
//        initHeaderDatas();
        //不知道为什么不加这一句，在用完刷新最新新闻以后总是会报错，
        // 读取到的信息总是不能赋值给newsDateItemDecoration中
        newsDateItemDecoration.setStories(stories);
    }

    @Override
    public void loadLatestDataSucc(NewsList newsList) {
        mSwipeRefreshLayout.setRefreshing(true);
        newsHasRead = newsDatabase.queryIsRead();
        stories = newsList.getStories();
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
            initHeaderDatas();
            mAdapter.setStories(stories);
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
                if (HtmlUtil.isNetworkAvailable(NewsApp.getInstance())) {
                    showErrorView(false);
                    presenter.loadLatestData();
                }
            }
        });
    }

    private void showErrorView(boolean showEmptyView) {
        if (showEmptyView) {
            ToastUtil.show("网络错误");
            mSwipeRefreshLayout.setVisibility(View.GONE);
            mSwipeRefreshLayout.setRefreshing(false);
            emptyView.setVisibility(View.VISIBLE);
            mNewStories = Collections.EMPTY_LIST;
            mRecyclerView.setVisibility(View.GONE);
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
    @Override
    public void loadTopNewsSucc(List<TopNews> topNewses) {

        this.topNewses = topNewses;
        TopNewsAdapter topNewsAdapter = new TopNewsAdapter(topNewses,stories);
        topNewsAdapter.setListener(this);
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
        TopNews topNews = topNewses.get(position);
        mTopNewsTitle.setText(topNews.getTitle());
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    /**
     * 当新闻头条中出现非今日消息时，由于没有对应的story的详情页信息 调用此接口获取信息并跳转至相应详情页面
     * @param topNews
     */
    @Override
    public void goToNewsDetailPage(TopNews topNews) {
        presenter.loadBeforeData(stories.get(0).getDate());
        for (Story story:stories) {
            if (story.getId() == topNews.getId()){
                NewsDetailActivity.startNewsDetailActivity(NewsApp.getInstance(),story);
            }
        }
    }
}
