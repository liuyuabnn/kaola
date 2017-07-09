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
 * Created by LY on 2016/10/13.10:08
 * 版权所有 盗版必究
 */

public class DianTaiZhuBo extends RelativeLayout {

    private ImageView imageView;
    private TextView tvName, tvRecomend;
    private int size;

    public DianTaiZhuBo(Context context) {
        this(context, null);
    }

    public DianTaiZhuBo(Context context, AttributeSet attrs) {
        super(context, attrs);
        //<merge/>
        inflate(context, R.layout.diantai_zhubo_item, this);
        imageView = (ImageView) findViewById(R.id.diantai_zhubo_iv);
        tvName = (TextView) findViewById(R.id.diantai_zhubo_nickmame_tv);
        tvRecomend = (TextView) findViewById(R.id.diantai_zhubo_recomend_tv);
        // 先得到屏幕的宽度
        int width = DeviceUtil.getScreenWidth(context);
        //计算圆形的直径
        size = (int) DeviceUtil.getPxFromDp(context, 100);
//        size = (width - (int) DeviceUtil.getPxFromDp(context, 60)) / 3;
    }


    public void setDianTai2(final DianTai2 dianTai2) {
        //给当前组合空间设置监听
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),dianTai2.getNickName(),Toast.LENGTH_SHORT).show();
            }
        });

//设置圆形图片
        Picasso.with(getContext())
                .load(dianTai2.getAvatar())
                .placeholder(R.drawable.ic_default_round_150_150)//还没加载出来的时候显示的默认的图片
                .error(R.drawable.no_net_error_chat_icon)//加载失败要显示的图片
                .resize(size,size)
                .centerCrop()
                .into(imageView);


        //设置第一个textview
        tvName.setText(dianTai2.getNickName());
        int gender = dianTai2.getGender();
        if (gender == 0) {
            tvName.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.man, 0);
        } else if (gender == 1) {
            tvName.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.woman, 0);
        }else {
            tvName.setCompoundDrawablesWithIntrinsicBounds(0, 0,0, 0);
        }

        //设置第二个textview
        String text = dianTai2.getRecommendReson();
        if (text =="") {
            tvRecomend.setText(dianTai2.getFansCount()+"");
            tvRecomend.setCompoundDrawablesWithIntrinsicBounds(R.drawable.heart_grey, 0, 0, 0);
        } else {
            tvRecomend.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            tvRecomend.setText(text);
        }
    }
}
