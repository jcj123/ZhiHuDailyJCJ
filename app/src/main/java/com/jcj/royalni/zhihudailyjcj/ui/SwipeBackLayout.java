package com.jcj.royalni.zhihudailyjcj.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import com.jcj.royalni.zhihudailyjcj.R;

/**
 * Created by jcj on 2017/8/6.
 */

public class SwipeBackLayout extends FrameLayout {

    private ViewDragHelper mViewDragHelper;
    private View mContentView;
    private int mContentWidth;
    private int mMoveLeft;
    private boolean isClose = false;
    private boolean isEdgeDrag = false;
    private CallBack mCallBack;//自定义内部的回调函数，下面写
    private Drawable mShadowLeft;
    private static final int FULL_ALPHA = 255;
    private static final int DEFAULT_SCRIM_COLOR = 0x99000000;
    private int mScrimColor = DEFAULT_SCRIM_COLOR;
    private float mScrimOpacity;
    private float mScrollPercent;
    private Rect mTmpRect = new Rect();

    //自定义控件 必备俩个构造函数
    public SwipeBackLayout(Context context) {
        this(context, null);//引用俩个参数的构造方法，目的是将三个构造方法连接起来.
    }

    public SwipeBackLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0); //引用三个参数的构造方法
    }

    public SwipeBackLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();//写个初始化方法，等下在里面实现 viewDragHelper,已经一些初始化操作
    }

    private void init() {
//ViewDragHelper 需要用到的回调函数有点多哈，这里不介绍了。哈。那个啥大家可以多研究下，百度上资料也是千篇一律的。
        mViewDragHelper = ViewDragHelper.create(this, 1.0f, new ViewDragHelper.Callback() {
            //返回true表示可以拖动
            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                return child == mContentView;//如果child==mContentView，返回true，也就是说mContentView可以移动
            }

            //记录值的变化
            @Override
            public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
                //记录左边的值的变化，因为我们实现的是往右滑动，所以只记录左边的值就可以了
                mScrollPercent = Math.abs((float) left / (mContentView.getWidth() + mShadowLeft.getIntrinsicWidth()));
                mMoveLeft = left;
                if (isClose && (left == mContentWidth)) {
                    //如果当前状态是关闭状态且左边的值等于滑动的View的宽度，
                    //也就是说当前的界面已经滑出屏幕，就回调finish方法，通知activity可以finish了
                    mCallBack.onFinish();
                }
            }

            //手指松开会触发这个方法，做复位操作就在此方法中实现
            @Override
            public void onViewReleased(View releasedChild, float xvel, float yvel) {
                //一定得重写computeScroll()方法，不然没有效果
                //如果移动的距离大于或等于当前界面的1/10，则触发关闭
                if (mMoveLeft >= (mContentWidth / 10)) {
                    isClose = true;  //设置滑动的View移动位置，即然当前的界面滑出屏幕
                    mViewDragHelper.settleCapturedViewAt(mContentWidth, releasedChild.getTop());
                } else {
                    //设置滑动的View移动位置，即恢复原来的位置
                    mViewDragHelper.settleCapturedViewAt(0, releasedChild.getTop());
                }
                //通知重绘界面
                invalidate();
            }

            //重新定位水平移动的位置
            @Override
            public int clampViewPositionHorizontal(View child, int left, int dx) {
                //水平移动距离的范围是0~当前界面的宽度，如果left小于0直接返回0，
                // 如果大于当前界面的宽度直接返回当前界面宽度
                //也就是控制当前界面只能往右移动
                return Math.min(mContentWidth, Math.max(left, 0));
            }

            //设置水平拖动的距离
            @Override
            public int getViewHorizontalDragRange(View child) {
                //因为我们移动的是整个界面，所以直接返回整个界面的宽度就可以了
                return mContentWidth;
            }

            @Override
            public void onEdgeTouched(int edgeFlags, int pointerId) {
                super.onEdgeTouched(edgeFlags, pointerId);
                isEdgeDrag = true;
            }
        });
        mViewDragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_LEFT);
        setShadow();//设置侧滑的边框，这个方法的代码下面写哈

    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        mScrimOpacity = 1 - mScrollPercent;
        //一定要做这个操作，否则onViewReleased不起作用
        if (mViewDragHelper != null && mViewDragHelper.continueSettling(true)) {
            invalidate();
        }
    }

    public void setShadow() {
        mShadowLeft = getResources().getDrawable(R.drawable.shadow_left);
        invalidate();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        //SwipeBackFrameLayout的子View有且只有一个，否则抛异常
        if (getChildCount() != 1) {
            throw new IllegalStateException("SwipeBackFrameLayout must host one child.");
        }
        //取得当前布局的第一个子View，也是唯一一个子View
        //也就是activity的主要布局
        mContentView = getChildAt(0);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);    //获取当前界面宽度
        mContentWidth = mContentView.getWidth();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        //把事件传递给
        return mViewDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //把事件传递给ViewDragHelper
        mViewDragHelper.processTouchEvent(event);
        invalidate();
        return true;
    }

    //画一个子项 ，到时候把acitivity的主题设置下就可以看到下面的activity了
    @Override
    protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
        final boolean drawContent = child == mContentView;
        boolean ret = super.drawChild(canvas, child, drawingTime);
        if (drawContent && mViewDragHelper.getViewDragState() != ViewDragHelper.STATE_IDLE) {
            drawShadow(canvas, child);
            drawScrim(canvas, child);
        }
        return ret;
    }

    //这个是那个阴影线
    private void drawShadow(Canvas canvas, View child) {
        final Rect childRect = mTmpRect;
        child.getHitRect(childRect);
        mShadowLeft.setBounds(childRect.left - mShadowLeft.getIntrinsicWidth(), childRect.top, childRect.left, childRect.bottom);
        mShadowLeft.setAlpha((int) (mScrimOpacity * FULL_ALPHA));
        mShadowLeft.draw(canvas);
    }

    //这个就是画那个透明渐变出来的帷幕，还真™不知道怎么形容
    private void drawScrim(Canvas canvas, View child) {
        final int baseAlpha = (mScrimColor & 0xff000000) >>> 24;
        final int alpha = (int) (baseAlpha * mScrimOpacity);
        final int color = alpha << 24 | (mScrimColor & 0xffffff);
        canvas.clipRect(0, 0, child.getLeft(), getHeight());
        canvas.drawColor(color);
    }

    //界面移出屏幕时接口回调
    public interface CallBack {
        void onFinish();//这个就可以直接用了咯，然后在acitiviy中实例化接口

    }

    //设置回调接口，提供给activity实现接口
    public void setCallBack(CallBack callBack) {
        mCallBack = callBack;
    }

}