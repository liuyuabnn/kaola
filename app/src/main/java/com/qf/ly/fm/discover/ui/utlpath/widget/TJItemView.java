package com.qf.ly.fm.discover.ui.utlpath.widget;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.qf.ly.fm.R;
import com.qf.ly.fm.discover.ui.activity.Play2Activity;
import com.qf.ly.fm.discover.ui.entiy.TuiJian2;
import com.qf.ly.fm.other.ui.PlayActivity;
import com.qf.ly.fm.other.utils.DeviceUtil;
import com.squareup.picasso.Picasso;

/**
 * Created by Administrator on 2016/10/15 0015.16:15
 * 版权所有 盗版必究
 */

public class TJItemView extends RelativeLayout {


    private ImageView imageView, ivplay;
    private TextView tvRname, tvdes;
    private int size;

    public TJItemView(Context context) {
        this(context, null);
    }

    public TJItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.tuijian_item_single, this);
        imageView = (ImageView) findViewById(R.id.tuijian_item_iv);
        ivplay = (ImageView) findViewById(R.id.tuijian_item_iv1);
        tvRname = (TextView) findViewById(R.id.tuijian_item_tv_rname);
        tvdes = (TextView) findViewById(R.id.tuijian_item_tv_des);
        // 先得到屏幕的宽度
        int width = DeviceUtil.getScreenWidth(context);
        //计算圆形的直径
        size = (int) DeviceUtil.getPxFromDp(context, 100);
//        size = (width - (int) DeviceUtil.getPxFromDp(context,20)) / 3;
    }

    public void setTuiJian2(final TuiJian2 tuiJian2) {
        //给当前组合空间设置监听
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tuiJian2.getRtype() == 1) {
                    Intent intent = new Intent(getContext(), PlayActivity.class);
                    intent.putExtra("tuiJian2", tuiJian2);
                    getContext().startActivity(intent);
                } else if (tuiJian2.getRtype() == 3 || tuiJian2.getRtype() == 11) {
                    Intent intent = new Intent(getContext(), Play2Activity.class);
                    intent.putExtra("tuiJian2", tuiJian2);
                    getContext().startActivity(intent);
                } else {
                    Toast.makeText(getContext(), tuiJian2.getDes(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        //设置图片
        Picasso.with(getContext())
                .load(tuiJian2.getPic())
                .placeholder(R.drawable.ic_default_round_150_150)//还没加载出来的时候显示的默认的图片
                .error(R.drawable.no_net_error_chat_icon)//加载失败要显示的图片
                .resize(size, size)
                .centerCrop()
                .into(imageView);
        //设置play按钮
        String url = tuiJian2.getMp3PlayUrl();
        if (url == "") {
            ivplay.setVisibility(View.GONE);
        }
        //设置第一个textview
        tvdes.setText(tuiJian2.getAlbumName());
        //设置第二个textview
        tvRname.setText(tuiJian2.getRname());
    }

}
