package com.qf.ly.fm.discover.ui.utlpath.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.qf.ly.fm.R;
import com.qf.ly.fm.discover.ui.entiy.Player;
import com.qf.ly.fm.other.utils.DeviceUtil;
import com.qf.ly.fm.other.utils.ExecutorUtil;
import com.squareup.picasso.Picasso;

/**
 * Created by Administrator on 2016/10/24 0024.18:53
 * 版权所有 盗版必究
 */

public class PageView extends LinearLayout {
    private ImageView ivbg, ivplay;
    private int size;

    public PageView(Context context) {
        this(context, null);
    }

    public PageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(VERTICAL);
        inflate(context, R.layout.play2_viewpager_item, this);

        ivbg = (ImageView) findViewById(R.id.viewpager_play2_iv);
        ivplay = (ImageView) findViewById(R.id.viewpager_play2_edit);
        //计算图片的边长
        size = (int) DeviceUtil.getPxFromDp(context, 350);
    }

    public void setPlayer(final Player player) {

        //设置图片
        Picasso.with(getContext())
                .load(player.getPic())
                .placeholder(R.drawable.player_default_bg)//还没加载出来的时候显示的默认的图片
                .error(R.drawable.no_net_error_chat_icon)//加载失败要显示的图片
                .resize(size, size)
                .centerCrop()
                .into(ivbg);

        ivplay.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ExecutorUtil.getInstance().work(player.getPlayUrl());

            }
        });

    }
}
