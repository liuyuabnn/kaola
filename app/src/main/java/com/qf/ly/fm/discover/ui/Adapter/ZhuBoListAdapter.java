package com.qf.ly.fm.discover.ui.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.qf.ly.fm.R;
import com.qf.ly.fm.discover.ui.utlpath.ZhuBo;
import com.qf.ly.fm.discover.ui.utlpath.widget.ImageTextItem;
import com.qf.ly.fm.other.utils.DeviceUtil;

import java.util.List;

/**
 * Created by LY on 2016/10/11.15:22
 * 版权所有 盗版必究
 */

public class ZhuBoListAdapter extends BaseAdapter {
    private List<ZhuBo> zhuBoList;
    private Context context;
    /**
     * 圆形图片外切正方形的边长
     */
    private int size;

    public ZhuBoListAdapter(List<ZhuBo> zhuBoList, Context context) {
        this.zhuBoList = zhuBoList;
        this.context = context;
//        先得到屏幕的宽度
        int width = DeviceUtil.getScreenWidth(context);

        //计算圆形的直径
        size = (int) DeviceUtil.getPxFromDp(context, 100);
//        size = (width - (int) DeviceUtil.getPxFromDp(context, 20)) / 3;
    }

    @Override
    public int getCount() {
        return zhuBoList == null ? 0 : zhuBoList.size();
    }

    @Override
    public Object getItem(int position) {
        return zhuBoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //解决视图复用
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.zhubo_listview_item, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        ZhuBo zhuBo = zhuBoList.get(position);
        //设置tittle
        viewHolder.tvTitle.setText(zhuBo.getName());
        //分别显示三个item
        viewHolder.imageTextItem1.setZhuBo2(zhuBo.getDataList().get(0));
        viewHolder.imageTextItem2.setZhuBo2(zhuBo.getDataList().get(1));
        viewHolder.imageTextItem3.setZhuBo2(zhuBo.getDataList().get(2));

        return convertView;
    }


    class ViewHolder {
        TextView tvTitle, tvMore;
        ImageTextItem imageTextItem1;
        ImageTextItem imageTextItem2;
        ImageTextItem imageTextItem3;

        public ViewHolder(View view) {
            tvTitle = (TextView) view.findViewById(R.id.item_zhubo_title_tv);
            tvMore = (TextView) view.findViewById(R.id.item_zhubo_more_tv);
            imageTextItem1 = (ImageTextItem) view.findViewById(R.id.ImageTextItem1);
            imageTextItem2= (ImageTextItem) view.findViewById(R.id.ImageTextItem2);
            imageTextItem3 = (ImageTextItem) view.findViewById(R.id.ImageTextItem3);
        }
    }
}
