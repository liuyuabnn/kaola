package com.qf.ly.fm.discover.ui.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.qf.ly.fm.R;
import com.qf.ly.fm.discover.ui.entiy.TuiJian2;
import com.qf.ly.fm.other.utils.DeviceUtil;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by LY on 2016/10/14.21:29
 * 版权所有 盗版必究
 */

public class EntryAdapter extends RecyclerView.Adapter<EntryAdapter.EntryHolder>{

    private LayoutInflater inflater;
    private Context context;
    private List<TuiJian2> tuiJian2List;
    private int width,size;

    public EntryAdapter(Context context, List<TuiJian2> tuiJian2List) {
        this.context = context;
        this.tuiJian2List = tuiJian2List;
        //计算ViewPager应该显示的高度
        //获取屏幕的宽度
        width = DeviceUtil.getScreenWidth(context);
        size = width / 5;
//        inflater=LayoutInflater.from(context);
        //通过获得系统服务的方式获得inflater
        inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public EntryHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView= inflater.inflate(R.layout.tuijian_entry_item,null);
        EntryHolder holder=new EntryHolder(itemView);
        return holder;
    }

    /**
     * 显示每一个Item的内容
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(EntryHolder holder, int position) {

        TuiJian2 tuiJian2 = tuiJian2List.get(position);
        Picasso.with(context)
                .load(tuiJian2.getPic())
                .resize(size,size)
                .into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return tuiJian2List==null? 0:tuiJian2List.size();
    }

    class EntryHolder extends RecyclerView.ViewHolder {

        ImageView imageView;

        /**
         * @param itemView RecyclerView item的根布局
         */
        public EntryHolder(View itemView) {
            super(itemView);
            imageView= (ImageView) itemView.findViewById(R.id.tuijian_entry_iv);
        }
    }
}
