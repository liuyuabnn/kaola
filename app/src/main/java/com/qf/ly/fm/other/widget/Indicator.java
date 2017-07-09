package com.qf.ly.fm.other.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.qf.ly.fm.R;

/**
 * Created by LY on 2016/10/9.18:49
 * 版权所有 盗版必究
 */

public class Indicator extends View {
    /**
     * 有多少个圆
     */
    private int circleCount;
    /**
     * 圆的半径
     */
    private float radius;
    /**
     * 两圆的间距
     */
    private float circlePadding;

    /**
     * 默认的颜色和选中的颜色
     */
    private int defaultColor;
    private int selectedColor;

    //两种颜色的画笔
    private Paint paint1 = new Paint();//画笔默认是黑色
    private Paint paint2 = new Paint();

    private int selectedIndex;//选中圆的下标
    private float offerSet;//偏移量

    public Indicator(Context context) {
        this(context, null);
    }

    public Indicator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Indicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.Indicator);
        circleCount = array.getInt(R.styleable.Indicator_circleCount, 4);//后面跟的都是默认值
        radius = array.getFloat(R.styleable.Indicator_radius, 10);
        circlePadding = array.getFloat(R.styleable.Indicator_circlePadding, 15);
        defaultColor = array.getColor(R.styleable.Indicator_defaultColor, Color.GREEN);
        selectedColor = array.getColor(R.styleable.Indicator_selectedColor, Color.RED);
        paint1.setColor(defaultColor);
        paint1.setAntiAlias(true);
        paint2.setColor(selectedColor);
        paint2.setAntiAlias(true);
        array.recycle();//用完之后回收
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        Canvas画布，所有的绘制操作都是在canvas上进行
        int width = getWidth();
        int height = getHeight();
        //圆心Y轴的坐标
        float cy = height / 2;
        //第一个圆心的X轴的位置
        float firstCx = radius + (width - radius * 2 * circleCount - circlePadding * (circleCount - 1)) / 2;
        for (int i = 0; i < circleCount; i++) {
            // 画四个圆 参数分别是圆心的x,y坐标,半径大小，画笔
            canvas.drawCircle(firstCx + i * (radius * 2 + circlePadding), cy, radius, paint1);
        }
        //画正在滑动的圆
        canvas.drawCircle(firstCx + selectedIndex * (radius * 2 + circlePadding) + offerSet * (radius * 2 + circlePadding), cy, radius, paint2);

    }

    public void setSmoothCircle(int selectedIndex, float offerSet) {

        this.selectedIndex = selectedIndex;
        this.offerSet = offerSet;
//        重绘,这个方法内部会自动调用onDraw
        invalidate();
    }
}
