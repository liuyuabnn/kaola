package com.qf.ly.fm.mine.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.qf.ly.fm.R;
import com.qf.ly.fm.other.adapter.GuidePageAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LY on 2016/10/10.15:51
 * 版权所有 盗版必究
 */

public class MineFragment extends Fragment {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ImageView camera_iv;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mine_fragment, null);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        camera_iv = (ImageView) view.findViewById(R.id.mine_camera_iv);
        tabLayout = (TabLayout) view.findViewById(R.id.mine_tablayout);
        viewPager = (ViewPager) view.findViewById(R.id.mine_viewpager);
        tabLayout.addTab(tabLayout.newTab().setText("收听历史"));
        tabLayout.addTab(tabLayout.newTab().setText("订阅"));
        tabLayout.addTab(tabLayout.newTab().setText("收藏"));

        camera_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CameraActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new LiShiFragment());
        fragments.add(new DingYueFragment());
        fragments.add(new ShouCangFragment());
        //Fragment嵌套Fragment用getChildFragmentManager
        GuidePageAdapter pageAdapter = new GuidePageAdapter(getChildFragmentManager(), fragments);
        viewPager.setAdapter(pageAdapter);
        //互相监听
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tabLayout.getTabAt(position).select();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


    }

}
