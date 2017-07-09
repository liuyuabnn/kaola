package com.qf.ly.fm.discover.ui.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.qf.ly.fm.R;
import com.qf.ly.fm.discover.ui.entiy.FenLeiBottom2;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Administrator on 2016/10/18 0018.9:32
 * 版权所有 盗版必究
 */

public class GridViewAdapter extends BaseAdapter {
    private Context context;
    private List<FenLeiBottom2> list;

    public GridViewAdapter(Context context, List<FenLeiBottom2> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view=LayoutInflater.from(context).inflate(R.layout.gridviewitem,null);
        ImageView imageView = (ImageView) view.findViewById(R.id.gridviewitem_iv);
        TextView textView = (TextView) view.findViewById(R.id.gridviewitem_tv);

        FenLeiBottom2 fenLeiBottom2 = list.get(position);
        textView.setText(fenLeiBottom2.getTitle());
        String icon = fenLeiBottom2.getIcon();
        Picasso.with(context)
                .load(icon)
                .into(imageView);
        return view;
    }

}
