package com.bonepeople.android.future.future.widget.recyclerbox;

import android.animation.ValueAnimator;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;

/**
 * 可下拉的RecyclerView容器
 * <p>
 * Created by bonepeople on 2018/1/29.
 */

public class RecyclerBox extends ViewGroup implements ValueAnimator.AnimatorUpdateListener {
    private HeaderView headerView;
    private RecyclerView recyclerView;
    private ValueAnimator animator;
    private int showHeight, maxHeight;
    private boolean shownTop = false;
    private boolean needShowTop = false;
    private int oldOffset, topOffset = 0;
    private float lastY = -1;

    public RecyclerBox(Context context) {
        this(context, null);
    }

    public RecyclerBox(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RecyclerBox(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        if (getChildCount() != 0) {
            throw new IllegalArgumentException("该控件默认包含一个RecyclerView，其他控件请使用函数添加");
        }
        headerView = new HeaderView(getContext());
        addView(headerView);
        recyclerView = new RecyclerView(getContext());
        addView(recyclerView);
        animator = ValueAnimator.ofInt(topOffset, 0);
        animator.addUpdateListener(this);
        animator.setDuration(200);
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int parentWidth = MeasureSpec.getSize(widthMeasureSpec);
        int parentHeight = MeasureSpec.getSize(heightMeasureSpec);

        measureChildren(widthMeasureSpec, heightMeasureSpec);

        showHeight = headerView.getShowHeight();
        maxHeight = headerView.getMaxHeight();
        int width = widthMode == MeasureSpec.EXACTLY ? parentWidth : recyclerView.getMeasuredWidth();
        int height = heightMode == MeasureSpec.EXACTLY ? parentHeight : recyclerView.getMeasuredHeight();

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        headerView.layout(0, 0, getWidth(), topOffset);
        recyclerView.layout(0, topOffset, getWidth(), getHeight());
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (animator.isRunning())
            animator.cancel();
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        MotionEvent recyclerEvent;
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                lastY = event.getY();
                oldOffset = topOffset;
//                recyclerView.dispatchTouchEvent(event);
                recyclerEvent = MotionEvent.obtain(event);
                recyclerEvent.offsetLocation(0, -oldOffset);
                recyclerView.dispatchTouchEvent(recyclerEvent);
                recyclerEvent.recycle();
                break;
            case MotionEvent.ACTION_MOVE:
                final float dy = event.getY() - lastY;
                if (dy < 0) {
                    setTopOffset((int) dy);
                    if (topOffset == 0) {
                        recyclerEvent = MotionEvent.obtain(event);
                        recyclerEvent.offsetLocation(0, oldOffset);
                        recyclerView.dispatchTouchEvent(recyclerEvent);
                        recyclerEvent.recycle();
                    }
                } else {
                    LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                    int position = layoutManager.findFirstCompletelyVisibleItemPosition();
                    if (position <= 0) {
                        setTopOffset((int) dy);
                        recyclerEvent = MotionEvent.obtain(event);
                        recyclerEvent.setAction(MotionEvent.ACTION_CANCEL);
                        recyclerView.dispatchTouchEvent(recyclerEvent);
                        recyclerEvent.recycle();
                    } else {
                        lastY = event.getY();
                        recyclerView.dispatchTouchEvent(event);
                    }
                }
                break;
            case MotionEvent.ACTION_UP: {
                if (topOffset == 0 || topOffset == maxHeight) {
//                    recyclerView.dispatchTouchEvent(event);
                    recyclerEvent = MotionEvent.obtain(event);
                    recyclerEvent.offsetLocation(0, -oldOffset);
                    recyclerView.dispatchTouchEvent(recyclerEvent);
                    recyclerEvent.recycle();
                    performClick();
                } else {
                    recyclerEvent = MotionEvent.obtain(event);
                    recyclerEvent.setAction(MotionEvent.ACTION_CANCEL);
                    recyclerView.dispatchTouchEvent(recyclerEvent);
                    recyclerEvent.recycle();
                    if (shownTop) {
                        needShowTop = topOffset >= maxHeight;
                    } else {
                        needShowTop = topOffset >= showHeight;
                    }
                    startAnimation();
                }
                //计算速度，滑动速度比较大的情况计算出下一个抬起的点位 赋值给抬起事件再传递给RecyclerView，这样可以使滑动更加顺滑，避免实际移动距离过短导致列表静止不动的情况
            }
            break;
        }
        return true;
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    private void setTopOffset(int offset) {
        topOffset = oldOffset + offset;
        if (offset > 0) {
            if (topOffset > maxHeight)
                if (oldOffset > maxHeight)
                    topOffset = oldOffset + (topOffset - oldOffset) / 2;
                else
                    topOffset = maxHeight + (topOffset - maxHeight) / 2;
        } else {
            if (topOffset <= 0) {
                topOffset = 0;
                shownTop = false;
            }
        }
        requestLayout();
    }

    private void startAnimation() {
        if (needShowTop) {
            animator.setIntValues(topOffset, maxHeight);
            animator.start();
        } else {
            animator.setIntValues(topOffset, 0);
            animator.start();
        }
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        topOffset = (int) animation.getAnimatedValue();
        shownTop = topOffset >= maxHeight;
        requestLayout();
    }
}
