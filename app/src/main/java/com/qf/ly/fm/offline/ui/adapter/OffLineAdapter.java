package com.qf.ly.fm.offline.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.qf.ly.fm.R;
import com.qf.ly.fm.offline.ui.entiy.DownLoadEntiy;
import com.qf.ly.fm.other.utils.DeviceUtil;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Administrator on 2016/10/22 0022.16:39
 * 版权所有 盗版必究
 */

public class OffLineAdapter extends BaseAdapter {
    private List<DownLoadEntiy> entiyList;
    private Context context;
    private int size;

    public OffLineAdapter(Context context, List<DownLoadEntiy> entiyList) {
        this.context = context;
        this.entiyList = entiyList;
        //计算图片显示的大小
        size = (int) DeviceUtil.getPxFromDp(context, 100);
    }

    @Override
    public int getCount() {
        return entiyList == null ? 0 : entiyList.size();
    }

    @Override
    public Object getItem(int position) {
        return entiyList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //解决视图复用
        OffLineAdapter.ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.offline_list_item, null);
            viewHolder = new OffLineAdapter.ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (OffLineAdapter.ViewHolder) convertView.getTag();
        }
        DownLoadEntiy loadEntiy = entiyList.get(position);
        viewHolder.tv1.setText(loadEntiy.getAlbumName());
        viewHolder.tv2.setText("共离线" + loadEntiy.getCount() + "期节目");
        viewHolder.tv3.setText(loadEntiy.getSize());

        Picasso.with(context)
                .load(loadEntiy.getImage())
                .placeholder(R.drawable.player_default_bg)
                .error(R.drawable.ic_default)
                .resize(size, size)
                .centerCrop()
                .into(viewHolder.imageView);

        return convertView;
    }

    class ViewHolder {
        TextView tv1, tv2, tv3;
        ImageView imageView;

        public ViewHolder(View view) {
            tv1 = (TextView) view.findViewById(R.id.offline_list_tv1);
            tv2 = (TextView) view.findViewById(R.id.offline_list_tv2);
            tv3 = (TextView) view.findViewById(R.id.offline_list_tv3);
            imageView = (ImageView) view.findViewById(R.id.offline_list_iv1);
        }
    }
}
