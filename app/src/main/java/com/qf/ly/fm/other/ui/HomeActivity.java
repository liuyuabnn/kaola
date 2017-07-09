package com.qf.ly.fm.other.ui;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.qf.ly.fm.R;
import com.qf.ly.fm.discover.ui.DiscoverFragment;
import com.qf.ly.fm.discover.ui.activity.EwmActivity;
import com.qf.ly.fm.discover.ui.activity.QQActivity;
import com.qf.ly.fm.discover.ui.entiy.EventLogin;
import com.qf.ly.fm.discover.ui.utlpath.widget.CircleImageView;
import com.qf.ly.fm.discover.ui.utlpath.widget.ExitDialog;
import com.qf.ly.fm.mine.ui.MineFragment;
import com.qf.ly.fm.offline.ui.OfflineFragment;
import com.squareup.picasso.Picasso;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

public class HomeActivity extends AppCompatActivity {
    private Fragment[] fragments;
    private RadioGroup radioGroup;
    private int lastFragment;
    private int currFragment;
    private DrawerLayout drawerLayout;
    private CircleImageView head_iv;
    private TextView user_tv;

    //    private MySlidingPaneView slidingPaneView;
    private NavigationView navigationView;

    private ServiceConnection connect = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MyService.MyBind myBind = (MyService.MyBind) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case "com.kaola.play":
                    Log.d("BroadcastReceiver", "play点击了");
                    break;
                case "com.kaola.close":
                    NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    //清理指定id的通知
                    manager.cancel(0x001);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //注册EventBus
        EventBus.getDefault().register(this);

        //动态注册广播
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.kaola.play");
        filter.addAction("com.kaola.close");
        registerReceiver(broadcastReceiver, filter);

        //启动服务
//        bindService(new Intent(this, MyService.class), connect, 0);
        startService(new Intent(this, MyService.class));


        radioGroup = (RadioGroup) findViewById(R.id.home_rg);
        if (fragments == null)
            fragments = new Fragment[]{
                    new DiscoverFragment(),
                    new MineFragment(),
                    new OfflineFragment()
            };

        FragmentManager fragmentManager = getSupportFragmentManager();
        //开启事物
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        for (int i = 0; i < fragments.length; i++) {
            fragmentTransaction.add(R.id.fragContainer, fragments[i]);
            fragmentTransaction.hide(fragments[i]);
        }
        //默认第一个显示
        fragmentTransaction.show(fragments[0]);
        fragmentTransaction.commit();

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (checkedId == R.id.home_discover_rb) {
                    //显示发现
                    currFragment = 0;
                } else if (checkedId == R.id.home_mine_rb) {
                    //显示我的
                    currFragment = 1;
                } else if (checkedId == R.id.home_offline_rb) {
                    //显示离线
                    currFragment = 2;
                }

                FragmentTransaction beginTransaction = getSupportFragmentManager().beginTransaction();
                //隐藏上一次显示的页面
                beginTransaction.hide(fragments[lastFragment]);
                beginTransaction.show(fragments[currFragment]);
                beginTransaction.commit();
                //重新记录下标
                lastFragment = currFragment;
            }
        });


        drawerLayout = (DrawerLayout) findViewById(R.id.DrawerLayout);

//        slidingPaneView = (MySlidingPaneView) findViewById(R.id.MySlidingPaneView);
//        //侧滑事件监听
//        slidingPaneView.setPanelSlideListener(new SlidingPaneLayout.PanelSlideListener() {
//            /**
//             * 正在滑动
//             * @param panel
//             * @param slideOffset
//             */
//            @Override
//            public void onPanelSlide(View panel, float slideOffset) {
//                //offset往右滑动慢慢变小,往左滑动慢慢变大
//                float offset = 1 - slideOffset;
//                if (offset < 0.5f) {
//                    offset = 0.5f;
//                }
////                panel.setScaleX(offset);
////                panel.setScaleY(offset);
//            }
//
//            /**
//             * 打开状态回调方法
//             * @param panel
//             */
//            @Override
//            public void onPanelOpened(View panel) {
//                Toast.makeText(HomeActivity.this, "onPanelOpened", Toast.LENGTH_SHORT).show();
//            }
//
//            /**
//             * 关闭状态回调方法
//             * @param panel
//             */
//            @Override
//            public void onPanelClosed(View panel) {
//                Toast.makeText(HomeActivity.this, "onPanelClosed", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        slidingPaneView.setIOnInterceptListener(new MySlidingPaneView.IOnInterceptListener() {
//            @Override
//            public boolean isIntercept() {
//                //在第一个页面需要拦截，往右滑动是可以显示侧滑菜单
//                return discoverPagerIndex == 0;
//            }
//        });

        navigationView = (NavigationView) findViewById(R.id.NavigationView);
        //然后通过调用headerView中的findViewById方法来查找到头部的控件，设置点击事件即可。
        View headerView = navigationView.getHeaderView(0);
        head_iv = (CircleImageView) headerView.findViewById(R.id.avatar);
        user_tv = (TextView) headerView.findViewById(R.id.Username);

        //item点击事件
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.drawer_home:
                        Intent intent = new Intent(HomeActivity.this, ShakeActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.drawer_favourite:
                        Intent i = new Intent(HomeActivity.this, EwmActivity.class);
                        startActivity(i);
                        break;
                    case R.id.drawer_downloaded:
                        Intent in = new Intent(HomeActivity.this, QQActivity.class);
                        startActivity(in);
                        break;
                }

                return false;
            }
        });
    }


    @Subscribe(sticky = false, threadMode = ThreadMode.MainThread)
    public void onEvent(EventLogin eventLogin) {

        user_tv.setText(eventLogin.getUserName());
        Picasso.with(this)
                .load(eventLogin.getUserIcon())
                .into(head_iv);
        Toast.makeText(this, eventLogin.getUserName() + "===" + eventLogin.getUserIcon(), Toast.LENGTH_SHORT).show();
    }

    private int discoverPagerIndex;

    public void setDiscoverPagerIndex(int index) {
        discoverPagerIndex = index;
    }

    @Override
    public void onBackPressed() {
        ExitDialog dialog = new ExitDialog(this) {
            @Override
            public void dialogFinish() {
                finish();
            }
        };
        dialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
        //通常在注销订阅者
        EventBus.getDefault().unregister(this);
    }
}
