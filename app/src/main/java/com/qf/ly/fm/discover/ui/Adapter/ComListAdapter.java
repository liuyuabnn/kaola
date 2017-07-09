package com.qf.ly.fm.discover.ui.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.CircleBitmapDisplayer;
import com.qf.ly.fm.R;
import com.qf.ly.fm.discover.ui.entiy.Comment;

import java.util.List;

/**
 * Created by Administrator on 2016/10/23 0023.17:47
 * 版权所有 盗版必究
 */

public class ComListAdapter extends BaseAdapter {
    private List<Comment> commentList;
    private Context context;
    //构造者模式
    private DisplayImageOptions options = new DisplayImageOptions.Builder()
            .cacheOnDisk(true)//磁盘缓存
            .cacheInMemory(true)//内存缓存
            .bitmapConfig(Bitmap.Config.RGB_565)//图片质量
            .displayer(new CircleBitmapDisplayer())//显示圆形图片
//            .displayer(new RoundedBitmapDisplayer(20))//显示圆角图片
            .showImageOnFail(R.drawable.no_net_error_chat_icon)//显示错误图片
            .showImageForEmptyUri(R.drawable.ic_user_photo_default)//url为""时候显示图片
            .showImageOnLoading(R.drawable.ic_default_round_150_150)//默认图片
            .build();

    public ComListAdapter(Context context, List<Comment> commentList) {
        this.context = context;
        this.commentList = commentList;
    }

    @Override
    public int getCount() {
        return commentList == null ? 0 : commentList.size();
    }

    @Override
    public Object getItem(int position) {
        return commentList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.comment_listview, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Comment comment = commentList.get(position);
        //ImageLoader
        ImageLoader.getInstance().displayImage(comment.getUserImg(), viewHolder.iv, options);
        viewHolder.tvName.setText(comment.getUserName());
        viewHolder.tvTime.setText(comment.getUtime());
        viewHolder.tvContext.setText(comment.getContent());

        return convertView;
    }

    class ViewHolder {
        TextView tvName, tvTime, tvContext;
        ImageView iv;

        public ViewHolder(View view) {
            iv = (ImageView) view.findViewById(R.id.comment_list_head_iv);
            tvName = (TextView) view.findViewById(R.id.comment_list_nickname_tv);
            tvTime = (TextView) view.findViewById(R.id.comment_list_time_tv);
            tvContext = (TextView) view.findViewById(R.id.comment_list_content_tv);
        }
    }
}
