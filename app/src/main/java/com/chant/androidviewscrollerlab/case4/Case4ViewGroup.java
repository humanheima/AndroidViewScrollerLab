package com.chant.androidviewscrollerlab.case4;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Scroller;
import android.widget.TextView;

/**
 * 示例：自定义一个 ViewGroup，包含几个一字排开的子 View，
 * 每个子 View 都与该 ViewGroup 一样大。
 * 通过 VelocityTracker 监控手指滑动速度。
 */
public class Case4ViewGroup extends ViewGroup {

    private static final String TAG = "Case4ViewGroup";

    public static final int CHILD_NUMBER = 6;

    // 滚动器
    protected Scroller mScroller;
    // 速度监控器
    private VelocityTracker mVelocityTracker;

    // 上一次事件的位置
    private float mLastMotionX;
    private int mMaximumVelocity = ViewConfiguration.get(getContext()).getScaledMaximumFlingVelocity();
    private int mMinimumVelocity = ViewConfiguration.get(getContext()).getScaledMinimumFlingVelocity();

    public Case4ViewGroup(Context context) {
        this(context, null);
    }

    public Case4ViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        initScroller();
        initChildView();
        Log.d(TAG, "Case4ViewGroup: mMaximumVelocity = " + mMaximumVelocity);
        Log.d(TAG, "Case4ViewGroup: mMinimumVelocity = " + mMinimumVelocity);
    }

    private void initChildView() {
        // 添加几个子 View
        for (int i = 0; i < CHILD_NUMBER; i++) {
            TextView child = new TextView(getContext());
            int color;
            switch (i % 3) {
                case 0:
                    color = 0xffcc6666;
                    break;
                case 1:
                    color = 0xffcccc66;
                    break;
                case 2:
                default:
                    color = 0xff6666cc;
                    break;
            }
            child.setBackgroundColor(color);
            child.setGravity(Gravity.CENTER);
            child.setTextSize(TypedValue.COMPLEX_UNIT_SP, 46);
            child.setTextColor(0x80ffffff);
            child.setText(String.valueOf(i));
            addView(child);
        }
    }

    private void initScroller() {
        mScroller = new Scroller(getContext());
    }

    int maxX;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        // 每个子 View 都与自己一样大
        int totalWidth = 0;
        for (int i = 0, childCount = getChildCount(); i < childCount; i++) {
            View childView = getChildAt(i);
            childView.measure(
                    MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY),
                    MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY));
            totalWidth += childView.getMeasuredWidth();

        }
        setMeasuredDimension(width, height);
        Log.d(TAG, "onMeasure: 内容宽度 = " + totalWidth + "，viewGroup的测量宽度" + getMeasuredWidth());
        maxX = getMeasuredWidth() * (CHILD_NUMBER - 1);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        // 子 View 一字排开
        for (int i = 0, childCount = getChildCount(); i < childCount; i++) {
            View childView = getChildAt(i);
            childView.layout(getWidth() * i, 0, getWidth() * (i + 1), b - t);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 速度监控器，监控每一个 event
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);

        // 触摸点
        final float eventX = event.getX();

        int maxX = getWidth() * (CHILD_NUMBER - 1);
        int scrollX = getScrollX();
        Log.d(TAG, "onTouchEvent: maxX = " + maxX);
        Log.d(TAG, "onTouchEvent: scrollX = " + scrollX);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                // 如果滚动未结束时按下，则停止滚动
                if (!mScroller.isFinished()) {
                    mScroller.abortAnimation();
                }
                // 记录按下位置
                mLastMotionX = eventX;
                break;
            case MotionEvent.ACTION_MOVE:
                // 手指移动的位移
                int deltaX = (int) (eventX - mLastMotionX);
                // 滚动内容，前提是不超出边界
                int targetScrollX = scrollX - deltaX;
                if (targetScrollX >= 0 &&
                        targetScrollX <= maxX) {
                    scrollTo(targetScrollX, 0);
                }
                // 记下手指的新位置
                mLastMotionX = eventX;
                break;
            case MotionEvent.ACTION_UP:
                // 计算当前速度， 1000表示每秒像素数等
                mVelocityTracker.computeCurrentVelocity(1000);
                int velocityX = (int) mVelocityTracker.getXVelocity();

                Log.d(TAG, "onTouchEvent: velocityX = " + velocityX);

                // 速度要大于最小的速度值，才开始滑动
                if (Math.abs(velocityX) > mMinimumVelocity) {
                    mScroller.fling(
                            getScrollX(),
                            0,
                            -velocityX,
                            0,
                            0,
                            maxX,
                            0,
                            0
                    );
                    invalidate();
                }
                // 回收速度监控器
                if (mVelocityTracker != null) {
                    mVelocityTracker.recycle();
                    mVelocityTracker = null;
                }
                break;
        }

        return true;
    }

    /**
     * 在 ViewGroup.dispatchDraw() -> ViewGroup.drawChild() -> View.draw(Canvas,ViewGroup,long) 时被调用
     * 任务：计算 mScrollX & mScrollY 应有的值，然后调用scrollTo/scrollBy
     */
    @Override
    public void computeScroll() {
        boolean isNotFinished = mScroller.computeScrollOffset();
        if (isNotFinished) {
            int currX = mScroller.getCurrX();
            Log.d(TAG, "computeScroll:  = " + "currX = " + currX);
            scrollTo(currX, 0);
            postInvalidate();

        }
    }

}
