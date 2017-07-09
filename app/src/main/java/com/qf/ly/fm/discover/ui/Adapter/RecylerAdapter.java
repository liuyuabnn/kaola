package com.qf.ly.fm.discover.ui.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.qf.ly.fm.R;
import com.qf.ly.fm.discover.ui.entiy.DianTai3;

import java.util.List;

/**
 * Created by Administrator on 2016/10/19 0019.10:54
 * 版权所有 盗版必究
 */

public class RecylerAdapter extends RecyclerView.Adapter<RecylerAdapter.MyViewHolder> {

    private List<DianTai3> dianTai3List;
    private Context context;

    public RecylerAdapter(Context context, List<DianTai3> dianTai3List) {
        this.context = context;
        this.dianTai3List = dianTai3List;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.textview, parent,false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        DianTai3 dianTai3 = dianTai3List.get(position);
        if (position == dianTai3List.size() - 1) {
            if (dianTai3List.size() == 8) {
//                holder.textView.setBackgroundResource(R.drawable.category_down);
                holder.textView.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.category_down,0, 0, 0);
                holder.textView.setText(null);
            } else if (dianTai3List.size() == 16) {
                holder.textView.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.category_up, 0, 0, 0);
                holder.textView.setText(null);
            }
        } else {
            holder.textView.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, 0, 0);
            holder.textView.setText(dianTai3.getName());
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onClick(position);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return dianTai3List == null ? 0 : dianTai3List.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textView;
        View itemView;

        public MyViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.diantai_textview);
            this.itemView = itemView;
        }
    }

    private IonItemClickListener onItemClickListener;

    public void setIonItemClickListener(IonItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface IonItemClickListener {

        void onClick(int position);
    }
}
