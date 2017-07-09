package com.qf.ly.fm.discover.ui.Adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by LY on 2016/10/11.12:30
 * 版权所有 盗版必究
 */

public abstract class CommenImagePageAdapter extends PagerAdapter {

    /**
     * 数据源
     */
    private List<ImageView> viewList;

    public CommenImagePageAdapter(List<ImageView> viewList) {
        this.viewList = viewList;

    }

    @Override
    public int getCount() {

        return Integer.MAX_VALUE;
//        return viewList == null ? 0 : viewList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        int index = position % viewList.size();
        ImageView imageView = viewList.get(index);
        container.addView(imageView);
        showImage(index,imageView);
        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        int index = position % viewList.size();
        ImageView imageView = viewList.get(index);
        container.removeView(imageView);
    }

    public abstract void showImage(int position, ImageView imageView);
}
