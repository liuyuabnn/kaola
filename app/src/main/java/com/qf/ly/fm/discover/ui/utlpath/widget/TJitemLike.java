package com.qf.ly.fm.discover.ui.utlpath.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.qf.ly.fm.R;
import com.qf.ly.fm.discover.ui.entiy.TuiJian2;
import com.qf.ly.fm.other.utils.DeviceUtil;
import com.squareup.picasso.Picasso;

/**
 * Created by Administrator on 2016/10/16 0016.14:21
 * 版权所有 盗版必究
 */

public class TJitemLike extends LinearLayout {

    private ImageView imageView;
    private TextView tvRname, tvalbum;
    private int size;

    public TJitemLike(Context context) {
        this(context,null);
    }

    public TJitemLike(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.tuijian_single_like, this);
        imageView = (ImageView) findViewById(R.id.tuijian_like_iv);
        tvRname = (TextView) findViewById(R.id.tuijian_like_tv1);
        tvalbum = (TextView) findViewById(R.id.tuijian_like_tv2);
//        // 先得到屏幕的宽度
//        int width = DeviceUtil.getScreenWidth(context);
        //计算圆形的直径
        size = (int) DeviceUtil.getPxFromDp(context, 100);
//        size = (width - (int) DeviceUtil.getPxFromDp(context,20)) / 3;
    }
    public void setTuiJian2(final TuiJian2 tuiJian2) {
        //给当前组合空间设置监听
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), tuiJian2.getAlbumName(), Toast.LENGTH_SHORT).show();
            }
        });
        //设置图片
        Picasso.with(getContext())
                .load(tuiJian2.getPic())
                .placeholder(R.drawable.ic_default_round_150_150)//还没加载出来的时候显示的默认的图片
                .error(R.drawable.no_net_error_chat_icon)//加载失败要显示的图片
                .resize(size,size)
                .centerCrop()
                .into(imageView);

        //设置第一个textview
        tvalbum.setText(tuiJian2.getAlbumName());
        //设置第二个textview
        tvRname.setText(tuiJian2.getRname());
    }
}
