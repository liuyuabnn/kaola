package com.qf.ly.fm.discover.ui.utlpath.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.qf.ly.fm.R;

import java.util.List;

/**
 * Created by LY on 2016/10/10.20:03
 * 版权所有 盗版必究
 * 主播
 */
public class VerticalScrollTextView extends View {
    /**
     * 滑动的两个字符串
     */
    private String text1, text2;
    /**
     * 开始绘制textview的Y轴起点
     */
    private float fromY;

    /**
     * text1,text2滑动的时候的Y坐标
     */
    private float y1, y2;

    /**
     * 每次向上滑动 1px
     */
    private int offsetY = 1;


    /**
     * 一次滚动完成后，暂停2秒钟
     */
    private final int pauseTime = 2000;

    /**
     * 每一次小的滑动间隔时间
     */
    private int scrollDelayTime;

    private int height;

    private List<String> list;

    /**
     * text1在list中的索引
     */
    private int currIndex;
    /**
     * 是否需要边框
     */
    private boolean isBorder;

    private Paint paint, redPaint;

    private float textsize;

    public VerticalScrollTextView(Context context) {
        super(context);
    }

    public VerticalScrollTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.VerticalScrollTextView);
        textsize=array.getDimensionPixelSize(R.styleable.VerticalScrollTextView_textsize,16);
        array.recycle();


        paint = new Paint();
        paint.setTextSize(textsize);
        paint.setColor(Color.GRAY);
        paint.setAntiAlias(true);

        redPaint = new Paint();
        redPaint.setTextSize(textsize);
        redPaint.setAntiAlias(true);
        redPaint.setColor(Color.RED);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (text1 == null || text2 == null) {
            return;
        }
        if (isBorder) {
            //画字符串,显示在空间x轴中间位置
            redPaint.setStyle(Paint.Style.FILL);
            canvas.drawText(text1, getWidth() / 2 - paint.measureText(text1) / 2, y1, redPaint);
            canvas.drawText(text2, getWidth() / 2 - paint.measureText(text2) / 2, y2, redPaint);

            redPaint.setStyle(Paint.Style.STROKE);
            redPaint.setStrokeWidth(2);
            RectF rectF = new RectF(1, 1, getWidth() - 1, getHeight() - 1);
            //设置画笔是空心的
            canvas.drawRoundRect(rectF, 15, 15, redPaint);

        } else {
            //画字符串,显示在空间x轴中间位置
            canvas.drawText(text1,0, y1, paint);
            canvas.drawText(text2,0, y2, paint);
//            canvas.drawText(text1, getWidth() / 2 - paint.measureText(text1) / 2, y1, paint);
//            canvas.drawText(text2, getWidth() / 2 - paint.measureText(text2) / 2, y2, paint);
        }

    }

    public void setList(List<String> list, Boolean isBorder) {
        this.list = list;
        this.isBorder = isBorder;
        start();
    }

    /**
     * 第一次操作
     */
    private void start() {
        post(new Runnable() {
            @Override
            public void run() {
                height = getHeight();
                fromY = getHeight() / 2 + (paint.descent() - paint.ascent()) / 2 - paint.descent();
                //间隔时间=总时间/绘制次数
                scrollDelayTime = 1000 / (height / offsetY);
                next();
            }
        });
    }

    /**
     * 下一次大的滚动操作
     */
    private void next() {
        int index1 = currIndex % list.size();
        text1 = list.get(index1);
        currIndex++;
        int index2 = currIndex % list.size();
        text2 = list.get(index2);

        //text1的Y坐标和超始点一样
        y1 = fromY;
        //text的Y坐标比text1的Y坐标大一个控件的高度
        y2 = y1 + height;

        scroll();
    }

    /**
     * 执行一次小的滑动
     */
    private void scroll() {
        y1 = y1 - offsetY;
        y2 = y2 - offsetY;
        //重新绘制
        invalidate();

        //延时执行下一次小的滑动
        postDelayed(new Runnable() {
            @Override
            public void run() {
                //判断是否需要暂停
                if (y2 <= fromY) {
                    pause();
                } else {
                    scroll();
                }
            }
        }, scrollDelayTime);
    }

    /**
     * 暂停
     */
    private void pause() {
        //延时执行下一次大的滑动
        postDelayed(new Runnable() {
            @Override
            public void run() {
                next();
            }
        }, pauseTime);

    }

    /**
     * 得到下标
     */
    public int  getCurrentIndex(){

        return currIndex % list.size();
    }
}
