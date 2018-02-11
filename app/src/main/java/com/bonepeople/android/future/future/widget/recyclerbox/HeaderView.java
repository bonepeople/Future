package com.bonepeople.android.future.future.widget.recyclerbox;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.bonepeople.android.future.future.R;

/**
 * RecyclerBox列表的头部容器
 * <p>
 * Created by bonepeople on 2018/2/9.
 */

public class HeaderView extends ViewGroup {
    private View view;
    private int showHeight = 210, maxHeight = 300;
    private int circleRadius = 10, circleOffset = 30;
    private static final float CIRCLE_PERCENT = 0.6f;
    private Paint paint;
    private boolean shownTop = false;


    public HeaderView(Context context) {
        this(context, null);
    }

    public HeaderView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setBackgroundColor(0xFFE5E5E5);
        inflate(getContext(), R.layout.recyclerbox_header, this);
        view = getChildAt(0);
        paint = new Paint();
        paint.setARGB(255, 183, 183, 183);
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        maxHeight = view.getMeasuredHeight();
        showHeight = (int) (maxHeight * 0.7);
        setMeasuredDimension(view.getMeasuredWidth(), view.getMeasuredHeight());
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int top, bottom;
        if (shownTop) {
            if (getHeight() <= maxHeight) {
                top = b - maxHeight;
                bottom = b;
            } else {
                top = (getHeight() - maxHeight) / 2;
                bottom = top + maxHeight;
            }
        } else {
            if (getHeight() <= showHeight) {
                top = getHeight() / 2 - circleRadius - maxHeight;
                bottom = top + maxHeight;
            } else {
                bottom = (getHeight() - showHeight) * (maxHeight - showHeight / 2 + circleRadius) / (maxHeight - showHeight) + showHeight / 2 - circleRadius;
                top = bottom - maxHeight;
            }
        }
        view.layout(l, top, r, bottom);
        if (top >= 0)
            shownTop = true;
        else if (top <= -maxHeight) {
            shownTop = false;
            paint.setAlpha(255);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (shownTop)
            return;
        float radius, offset = circleOffset;
        int alpha;
        if (getHeight() <= showHeight / 2) {
            radius = getHeight() / (showHeight / 2f) * circleRadius;
        } else if (getHeight() <= showHeight) {
            radius = circleRadius - (getHeight() - showHeight / 2f) / (showHeight / 2f) * (circleRadius - circleRadius * CIRCLE_PERCENT);
            offset = (getHeight() - showHeight / 2f) / (showHeight / 2f) * circleOffset;
        } else {
            radius = circleRadius * CIRCLE_PERCENT;
            alpha = 255 * (maxHeight - (maxHeight - showHeight) / 2 - getHeight()) / ((maxHeight - showHeight) / 2);
            paint.setAlpha(Math.max(alpha, 0));
        }
        canvas.drawCircle(getWidth() / 2, view.getBottom() + circleRadius, radius, paint);
        if (getHeight() > showHeight / 2) {
            canvas.drawCircle(getWidth() / 2 - offset, view.getBottom() + circleRadius, circleRadius * CIRCLE_PERCENT, paint);
            canvas.drawCircle(getWidth() / 2 + offset, view.getBottom() + circleRadius, circleRadius * CIRCLE_PERCENT, paint);
        }
    }

    public int getShowHeight() {
        return showHeight;
    }

    public int getMaxHeight() {
        return maxHeight;
    }
}
