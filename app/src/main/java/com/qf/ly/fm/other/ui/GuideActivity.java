package com.qf.ly.fm.other.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.qf.ly.fm.R;
import com.qf.ly.fm.other.adapter.GuidePageAdapter;
import com.qf.ly.fm.other.widget.Indicator;

import java.util.ArrayList;
import java.util.List;

public class GuideActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private Indicator indicator;
    private int[] videoIds = {R.raw.splash_1, R.raw.splash_2, R.raw.splash_4, R.raw.splash_5};
    private int[] ivLeftIds = {R.drawable.guide_anim_1_2, R.drawable.guide_anim_2_2, R.drawable.guide_anim_4_2, R.drawable.guide_anim_5_2};
    private int[] ivRightIds = {R.drawable.guide_anim_1_1, R.drawable.guide_anim_2_1, R.drawable.guide_anim_4_1, R.drawable.guide_anim_5_1};
    private int[] ivCoverIds = {R.drawable.guide_video_static_1, R.drawable.guide_video_static_2, R.drawable.guide_video_static_4, R.drawable.guide_video_static_5};
    private int lastPage;//上一个页面

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        viewPager = (ViewPager) findViewById(R.id.guide_vp);
        indicator= (Indicator) findViewById(R.id.indicator);
        //循环加入数据
        final List<Fragment> fragmentList = new ArrayList<>();
        for (int i = 0; i < videoIds.length; i++) {
            Fragment fragment = new GuideFragment(i,ivLeftIds[i], ivRightIds[i], videoIds[i],ivCoverIds[i]);
            fragmentList.add(fragment);
        }
        GuidePageAdapter adapter = new GuidePageAdapter(getSupportFragmentManager(), fragmentList);
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                indicator.setSmoothCircle(position,positionOffset);
            }

            @Override
            public void onPageSelected(int position) {
//                //隐藏上一个页面的图片，停止播放的动画
//                GuideFragment lastFragment = (GuideFragment) fragmentList.get(lastPage);
//                lastFragment.hideAnimation();
//                //显示当前页面的图片，播放当前页面的动画
//                GuideFragment currentFrament = (GuideFragment) fragmentList.get(position);
//                currentFrament.showAnimation();
//
//                lastPage=position;

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }

    public void start(View view) {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        //修改使用状态
        SharedPreferences preferences = getSharedPreferences("firstUsed", MODE_PRIVATE);
        //获取editor
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("flag", false);
        editor.commit();
        //一旦进入主页面就不能返回欢迎页面
        this.finish();
    }
}
