package com.qf.ly.fm.discover.ui.Adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.qf.ly.fm.discover.ui.utlpath.widget.PageView;

import java.util.List;

/**
 * Created by Administrator on 2016/10/24 0024.18:48
 * 版权所有 盗版必究
 */

public class PlayerAdapter extends PagerAdapter {

    private Context context;
    private List<PageView> pageViewList;

    public PlayerAdapter(Context context,List<PageView> pageViewList) {
        this.context = context;
        this.pageViewList = pageViewList;
    }

    @Override
    public int getCount() {
        return pageViewList == null ? 0 : pageViewList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        PageView pageView=pageViewList.get(position);
        container.addView(pageView);
        return pageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        PageView pageView=pageViewList.get(position);
        container.removeView(pageView);
    }
}
