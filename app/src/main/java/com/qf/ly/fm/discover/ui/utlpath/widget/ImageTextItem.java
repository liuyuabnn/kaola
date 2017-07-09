package com.qf.ly.fm.discover.ui.utlpath.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.qf.ly.fm.R;
import com.qf.ly.fm.discover.ui.utlpath.ZhuBo2;
import com.qf.ly.fm.other.utils.DeviceUtil;
import com.qf.ly.fm.other.utils.TransformatSquare;
import com.squareup.picasso.Picasso;

/**
 * Created by LY on 2016/10/13.10:08
 * 版权所有 盗版必究
 */

public class ImageTextItem extends RelativeLayout {

    private ImageView imageView;
    private TextView tvName, tvRecomend;
    private int size;

    public ImageTextItem(Context context) {
        this(context, null);
    }

    public ImageTextItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        //<merge/>
        inflate(context, R.layout.zhubo_item, this);
        imageView = (ImageView) findViewById(R.id.item_zhubo_head_iv);
        tvName = (TextView) findViewById(R.id.item_zhubo_nickname_tv);
        tvRecomend = (TextView) findViewById(R.id.item_zhubo_recomend_tv);
        // 先得到屏幕的宽度
        int width = DeviceUtil.getScreenWidth(context);
        //计算圆形的直径
//        size = (int) DeviceUtil.getPxFromDp(context, 100);
        size = (width - (int) DeviceUtil.getPxFromDp(context, 60)) / 3;
    }


    public void setZhuBo2(final ZhuBo2 zhuBo2) {
        //给当前组合空间设置监听
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),zhuBo2.getNickName(),Toast.LENGTH_SHORT).show();
            }
        });

//设置圆形图片
        Picasso.with(getContext())
                .load(zhuBo2.getAvatar())
                .placeholder(R.drawable.ic_default_round_150_150)//还没加载出来的时候显示的默认的图片
                .error(R.drawable.no_net_error_chat_icon)//加载失败要显示的图片
                .transform(new TransformatSquare(zhuBo2.getAvatar(), size, "ImageTextItem", true))//修改图片的接口
                .into(imageView);


        //设置第一个textview
        tvName.setText(zhuBo2.getNickName());
        int gender = zhuBo2.getGender();
        if (gender == 0) {
            tvName.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.man, 0);
        } else if (gender == 1) {
            tvName.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.woman, 0);
        }else {
            tvName.setCompoundDrawablesWithIntrinsicBounds(0, 0,0, 0);
        }

        //设置第二个textview
        String text = zhuBo2.getRecommendReson();
        if (text =="") {
            tvRecomend.setText(zhuBo2.getFansCount()+"");
            tvRecomend.setCompoundDrawablesWithIntrinsicBounds(R.drawable.heart_grey, 0, 0, 0);
        } else {
            tvRecomend.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            tvRecomend.setText(text);
        }


    }
}
