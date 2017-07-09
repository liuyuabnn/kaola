package com.qf.ly.fm.other.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.VideoView;

/**
 * Created by LY on 2016/10/9.14:59
 * 版权所有 盗版必究
 */

public class FullScreenVideoView extends VideoView {


    public FullScreenVideoView(Context context) {
        this(context, null);
    }

    public FullScreenVideoView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FullScreenVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        setMeasuredDimension(width, height);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}
