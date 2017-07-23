package com.jcj.royalni.zhihudailyjcj.adapter;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import com.jcj.royalni.zhihudailyjcj.bean.Story;
import com.jcj.royalni.zhihudailyjcj.utils.DateUtil;

import java.util.List;

/**
 * Created by Royal Ni on 2017/7/6.
 * 通过添加ItemDecoration的方式来进行头文件的编写
 */

public class NewsDateItemDecoration extends RecyclerView.ItemDecoration {
    private static final String TAG = "NewsDateItemDecoration";

    private List<Story> stories;
    private Context mContext;
    private Paint mPaint;
    private int mTitleHeight;
    private Rect mBounds;
    private int mTextSize;

    private Toolbar toolbar;

    public NewsDateItemDecoration(Context context, List<Story> stories, Toolbar toolbar) {
        mContext = context;
        this.stories = stories;
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 16,
                context.getResources().getDisplayMetrics());
        mPaint.setTextSize(mTextSize);

        mTitleHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30,
                context.getResources().getDisplayMetrics());
        mBounds = new Rect();
        this.toolbar = toolbar;
    }


    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        RecyclerView.LayoutParams lm = (RecyclerView.LayoutParams) view.getLayoutParams();
        int position = lm.getViewLayoutPosition();
        if (position > -1) {
            if (position == 0) {
                outRect.set(0, mTitleHeight, 0, 0);
            } else {
                Log.d(TAG,"storiesSize"+stories.size());
                Log.d(TAG,"position"+position);
                if (null != stories.get(position).getDate() & !stories.get(position - 1).getDate().equals(stories.get(position).getDate())) {
                    outRect.set(0, mTitleHeight, 0, 0);
                }
            }
        }
    }

    @Override
    public void onDraw(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(canvas, parent, state);
        final int left;
        final int right;
        int firstVisibleItemPosition=0;
        left = parent.getPaddingLeft();
        right = parent.getWidth() - parent.getPaddingRight();

        final int childCount = parent.getChildCount();
        final RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof LinearLayoutManager) {
            LinearLayoutManager linearManager = (LinearLayoutManager) layoutManager;
            firstVisibleItemPosition = linearManager.findFirstVisibleItemPosition();
        }
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) child.getLayoutParams();
            int position = lp.getViewLayoutPosition();
            if (stories.size() > 0) {
                if (position == 0) {
                    drawTitle(canvas, left, right, child, firstVisibleItemPosition, position);
                } else {
                    if (null != stories.get(position).getDate() && !stories.get(position - 1).getDate()
                            .equals(stories.get(position).getDate())) {
                        drawTitle(canvas, left, right, child, firstVisibleItemPosition, position);
                    }
                }
            }

        }
    }

    private void drawTitle(Canvas c, int left, int right, View child, int firstVisibleItemPosition, int position) {
        mPaint.setColor(Color.BLACK);
        String date = DateUtil.formatDate(stories.get(position).getDate());
        String firstVisibleItemPositionDate = DateUtil.formatDate(stories.get(firstVisibleItemPosition).getDate());
        if (position == 0) {
            date = "今日热点";
            toolbar.setTitle(date);
        }else if(date.equals(firstVisibleItemPositionDate)){
            toolbar.setTitle(date);

        }

        mPaint.getTextBounds(date, 0, date.length(), mBounds);
        c.drawText(date, child.getPaddingLeft() + mTitleHeight / 2, child.getTop() - mTitleHeight / 2, mPaint);
    }

    public List<Story> getStories() {
        return stories;
    }

    public void setStories(List<Story> stories) {
        this.stories = stories;
    }
}
