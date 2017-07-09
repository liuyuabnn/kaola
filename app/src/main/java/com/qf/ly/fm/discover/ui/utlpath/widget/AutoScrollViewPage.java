package com.qf.ly.fm.discover.ui.utlpath.widget;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.qf.ly.fm.R;

/**
 * Created by LY on 2016/10/11.12:12
 * 版权所有 盗版必究
 * 组合控件
 */

public class AutoScrollViewPage extends LinearLayout {
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private int currentItem;
    private int pagecount;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            viewPager.setCurrentItem(++currentItem);
            autoScroll();
        }
    };

    public AutoScrollViewPage(Context context) {
        this(context,null);
    }

    public AutoScrollViewPage(final Context context, AttributeSet attrs) {
        super(context, attrs);
        //组合控件
        //加载指定的布局到当前的容器中
        inflate(context, R.layout.zhubo_header_scrollview, this);
        viewPager = (ViewPager) findViewById(R.id.widget_zhubo_vp);
        /**
         * 得到屏幕的宽度
         * layoutParams布局属性
         */
        ViewGroup.LayoutParams layoutParams = viewPager.getLayoutParams();
        int width = context.getResources().getDisplayMetrics().widthPixels;
        int height = width * 3 / 8;
        layoutParams.height = height;
        viewPager.setLayoutParams(layoutParams);
        tabLayout = (TabLayout) findViewById(R.id.widget_zhubo_tab);


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentItem = position;
                int index = currentItem % pagecount;
                tabLayout.getTabAt(index).select();

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /**
     * 显示数据
     *
     * @param adapter
     */
    public void setPageAdapter(PagerAdapter adapter, int count) {
        viewPager.setAdapter(adapter);
        this.pagecount = count;
        for (int i = 0; i < pagecount; i++) {
            tabLayout.addTab(tabLayout.newTab());
        }
        autoScroll();
    }

    /**
     * 自动滚动
     */
    public void autoScroll() {
        if (handler != null) {
            handler.sendEmptyMessageDelayed(0, 3000);
        }
    }

    /**
     * 当控件从窗口脱离的时候会回调的方法
     */
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        handler.removeMessages(0);
        handler = null;
    }
}
