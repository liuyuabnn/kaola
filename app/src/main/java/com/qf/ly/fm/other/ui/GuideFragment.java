package com.qf.ly.fm.other.ui;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.VideoView;

import com.qf.ly.fm.R;

/**
 * Created by LY on 2016/10/9.14:29
 * 版权所有 盗版必究
 */

public class GuideFragment extends android.support.v4.app.Fragment {
    private VideoView videoView;
    private ImageView right_iv;
    private ImageView left_iv;
    private ImageView cover_iv;
    private int position;

    private int videoView_id, right_iv_id, left_iv_id,cover_id;

    public GuideFragment() {
    }

    @SuppressLint("ValidFragment")
    public GuideFragment(int position,int left_iv_id, int right_iv_id, int videoView_id,int cover_id) {
        //资源文件的ID
        this.left_iv_id = left_iv_id;
        this.right_iv_id = right_iv_id;
        this.videoView_id = videoView_id;
        this.cover_id=cover_id;
        this.position=position;//表示当前是哪个ViewPage或者Fragment
    }

    /**
     *
     * @param isVisibleToUser 当Fragment显示时最先调用的方法，true显示 false不显示
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (videoView == null)
        {
            return;
        }
        if (isVisibleToUser)
        {
            showAnimation();
        }
        else
        {
            hideAnimation();
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.guide_fragment_layout, null);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        videoView = (VideoView) view.findViewById(R.id.videoview);
        right_iv = (ImageView) view.findViewById(R.id.right_iv);
        left_iv = (ImageView) view.findViewById(R.id.left_iv);
        cover_iv= (ImageView) view.findViewById(R.id.guide_cover);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //显示对应的内容
        right_iv.setImageResource(right_iv_id);
        left_iv.setImageResource(left_iv_id);
        cover_iv.setImageResource(cover_id);
        //设置视频播放路径
        Uri uri = Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + videoView_id);
        videoView.setVideoURI(uri);
        //开始播放
        videoView.start();
        //设置循环播放
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                mediaPlayer.start();
            }
        });
    }
        //特殊处理默认页面
    @Override
    public void onResume() {
        super.onResume();
        if (getUserVisibleHint()){
            if (position == 0)
            {
                showAnimation();
            }
        }
    }

    public void hideAnimation(){
        //取消动画的时候把静态图片显示出来，不然Page切换的时候就会显示黑屏
        cover_iv.setVisibility(View.VISIBLE);
        //清除动画
        left_iv.clearAnimation();
        right_iv.clearAnimation();
        videoView.pause();
    }

    public void showAnimation(){
        //加载指定的动画
        final Animation rightAnimation= AnimationUtils.loadAnimation(getContext(),R.anim.right_image);
        final Animation leftAnimation=AnimationUtils.loadAnimation(getContext(),R.anim.left_image);
        //先把图片隐藏
        cover_iv.setVisibility(View.GONE);
        //先播放视频
        videoView.start();
        //先执行右边的动画，就算图片invisible，执行动画的时候也会显示（GONE例外）
        right_iv.startAnimation(rightAnimation);
        //右边图片动画结束通过动画的监听执行左边的图片动画
        rightAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                //在右边的动画执行完后，执行左边的动画
                left_iv.startAnimation(leftAnimation);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}
