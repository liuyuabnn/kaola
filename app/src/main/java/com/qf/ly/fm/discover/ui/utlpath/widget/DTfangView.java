package com.qf.ly.fm.discover.ui.utlpath.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.qf.ly.fm.R;
import com.qf.ly.fm.discover.ui.entiy.DianTai2;
import com.qf.ly.fm.other.utils.DeviceUtil;
import com.squareup.picasso.Picasso;

/**
 * Created by Administrator on 2016/10/15 0015.16:15
 * 版权所有 盗版必究
 */

public class DTfangView extends RelativeLayout {


    private ImageView imageView;
    private TextView tvRname, tvabl;
    private int size;

    public DTfangView(Context context) {
        this(context, null);
    }

    public DTfangView(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.diantai_item_single, this);
        imageView = (ImageView) findViewById(R.id.diantai_single_item_iv);
        tvRname = (TextView) findViewById(R.id.diantai_item_tv_1);
        tvabl = (TextView) findViewById(R.id.diantai_item_tv_2);
        // 先得到屏幕的宽度
        int width = DeviceUtil.getScreenWidth(context);
        //计算圆形的直径
        size = (int) DeviceUtil.getPxFromDp(context, 100);
//        size = (width - (int) DeviceUtil.getPxFromDp(context,20)) / 3;
    }

    public void setDianTai2(final DianTai2 dianTai2) {
        //给当前组合空间设置监听
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), dianTai2.getAlbumName(), Toast.LENGTH_SHORT).show();
            }
        });

        //设置图片
        Picasso.with(getContext())
                .load(dianTai2.getPic())
                .placeholder(R.drawable.ic_default_round_150_150)//还没加载出来的时候显示的默认的图片
                .error(R.drawable.no_net_error_chat_icon)//加载失败要显示的图片
                .resize(size,size)
                .centerCrop()
                .into(imageView);
        //设置第一个textview
        tvRname.setText(dianTai2.getRname());
        //设置第二个textview
        tvabl.setText(dianTai2.getAlbumName());
    }

}
