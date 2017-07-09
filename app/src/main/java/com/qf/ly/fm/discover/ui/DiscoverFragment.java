package com.qf.ly.fm.discover.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qf.ly.fm.R;
import com.qf.ly.fm.discover.ui.entiy.HideLoadingView;
import com.qf.ly.fm.discover.ui.entiy.ShowLoadlingView;
import com.qf.ly.fm.discover.ui.utlpath.widget.LoadingView;
import com.qf.ly.fm.other.adapter.GuidePageAdapter;
import com.qf.ly.fm.other.ui.HomeActivity;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

/**
 * Created by LY on 2016/10/10.15:51
 * 版权所有 盗版必究
 */

public class DiscoverFragment extends Fragment {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private LoadingView loadingView;
    public static final String LOADING_ACTION_SHOW = "com.ly.loading.show";
    public static final String LOADING_ACTION_HIDE = "com.ly.loading.hide";

//    private LoadingReceiver receiver = new LoadingReceiver();
//
//    /**
//     * 广播接受者
//     */
//    private class LoadingReceiver extends BroadcastReceiver {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            String action = intent.getAction();
//            if (LOADING_ACTION_SHOW.equals(action)) {
//                loadingView.setVisibility(View.VISIBLE);
//            } else if (LOADING_ACTION_HIDE.equals(action)) {
//                loadingView.setVisibility(View.GONE);
//            }
//        }
//    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.discover_fragment, null);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        tabLayout = (TabLayout) view.findViewById(R.id.discover_tab);
        viewPager = (ViewPager) view.findViewById(R.id.discover_vp);

        tabLayout.addTab(tabLayout.newTab().setText("推荐"));
        tabLayout.addTab(tabLayout.newTab().setText("分类"));
        tabLayout.addTab(tabLayout.newTab().setText("电台"));
        tabLayout.addTab(tabLayout.newTab().setText("主播"));

        loadingView = (LoadingView) view.findViewById(R.id.loadingview);


    }

    //无论发布者在哪个线程发布，这个方法都在UI线程调用
    @Subscribe(sticky = false, threadMode = ThreadMode.MainThread)
    public void onEvent(ShowLoadlingView showLoadlingView) {
        loadingView.setVisibility(View.VISIBLE);
    }

    @Subscribe(sticky = false, threadMode = ThreadMode.MainThread)
    public void onEvent(HideLoadingView hideLoadingView) {
        loadingView.setVisibility(View.GONE);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

//        //动态注册广播
//        IntentFilter filter = new IntentFilter();
//        filter.addAction(LOADING_ACTION_SHOW);
//        filter.addAction(LOADING_ACTION_HIDE);
//        getActivity().registerReceiver(receiver, filter);


        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new TuiJianFragment());
        fragments.add(new FenleiFragment());
        fragments.add(new DianTaiFragment());
        fragments.add(new ZhuBoFragment());

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
                HomeActivity activity = (HomeActivity) getActivity();
                activity.setDiscoverPagerIndex(position);
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

    @Override
    public void onStart() {
        super.onStart();
        //通常在onStart注册EventBus，让当前的Fragment成为订阅者
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        //通常在注销订阅者
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}
