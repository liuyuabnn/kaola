package com.qf.ly.fm.discover.ui.utlpath.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.qf.ly.fm.R;

/**
 * Created by Administrator on 2016/10/26 0026.15:18
 * 版权所有 盗版必究
 */

public class LoadingView extends RelativeLayout {
    private ImageView ivbg;

    public LoadingView(Context context) {
        super(context);
    }

    public LoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setBackgroundColor(getResources().getColor(R.color.colorback));
        inflate(context, R.layout.loading_layout, this);
        ivbg = (ImageView) findViewById(R.id.widget_loading_bg_iv);
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.loading_image);
        ivbg.startAnimation(animation);
    }
}
