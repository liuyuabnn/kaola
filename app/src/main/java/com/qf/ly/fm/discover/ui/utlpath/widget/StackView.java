package com.qf.ly.fm.discover.ui.utlpath.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Administrator on 2016/10/23 0023.10:32
 * 版权所有 盗版必究
 */

public class StackView extends View {

    private int verticalPadding;//px
    private int horizontalPadding = 20;

    //圆角矩形的高度
    private int rectHeight = 60;//只要大于该控件的高度即可
    private int radius = 10;
    private int color = 0xaaFFFFFF;//#aaffffff
    private int width, height;
    //从下往上数，第一个矩形的位置
    private int FromX, fromY;

    //动画结束的终点,控件的左下角的位置
    private int endX, endY;
    //每次的变化值
    private int offerSet = 1;

    private Paint paint = new Paint();

    public StackView(Context context) {
        super(context);
    }

    public StackView(Context context, AttributeSet attrs) {
        super(context, attrs);

        paint.setColor(color);
        //在构造方法里直接获取宽高是0,因为还没有调用测量方法
        post(new Runnable() {
            @Override
            public void run() {
                width = getWidth();
                height = getHeight();
                verticalPadding = height / 3;

                FromX = horizontalPadding;
                fromY = height - verticalPadding;

                endX = 0;
                endY = height;

                //
                showAnim();
            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        RectF rectf3 = new RectF(FromX + horizontalPadding * 2, fromY - verticalPadding * 2,
                width - (FromX + horizontalPadding * 2), rectHeight + fromY - verticalPadding * 2);

        RectF rectf2 = new RectF(FromX + horizontalPadding * 1, fromY - verticalPadding * 1,
                width - (FromX + horizontalPadding * 1), rectHeight + fromY - verticalPadding * 1);

        RectF rectf1 = new RectF(FromX + horizontalPadding * 0, fromY - verticalPadding * 0,
                width - FromX, rectHeight + fromY - verticalPadding * 0);

        //画圆 角矩形
        canvas.drawRoundRect(rectf3, radius, radius, paint);
        canvas.drawRoundRect(rectf2, radius, radius, paint);
        canvas.drawRoundRect(rectf1, radius, radius, paint);
    }

    /**
     * 执行动画
     */
    public void showAnim() {
        FromX -= offerSet;
        fromY += offerSet;

        invalidate();

        postDelayed(new Runnable() {
            @Override
            public void run() {
                if (fromY == endY) {
                    FromX = horizontalPadding;
                    fromY = height - verticalPadding;
                } else {
                    showAnim();
                }
            }
        }, 10);
    }

}
