package com.qf.ly.fm.discover.ui.utlpath.widget;

import android.content.Context;
import android.support.v4.widget.SlidingPaneLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Administrator on 2016/10/29 0029.12:44
 * 版权所有 盗版必究
 */

public class MySlidingPaneView extends SlidingPaneLayout {

    public MySlidingPaneView(Context context) {
        super(context);
    }

    public MySlidingPaneView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 事件拦截
     *
     * @param ev
     * @return
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (interceptListener == null) {
            return super.onInterceptTouchEvent(ev);
        }
        if (interceptListener.isIntercept()) {
            return super.onInterceptTouchEvent(ev);
        } else {
            return false;
        }

    }


    private IOnInterceptListener interceptListener;

    public void setIOnInterceptListener(IOnInterceptListener interceptListener) {
        this.interceptListener = interceptListener;
    }

    public interface IOnInterceptListener {
        boolean isIntercept();
    }
}
