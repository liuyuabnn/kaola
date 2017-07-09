package com.qf.ly.fm.discover.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.qf.ly.fm.R;
import com.qf.ly.fm.discover.ui.entiy.EventLogin;
import com.qf.ly.fm.other.utils.LogUtil;
import com.tencent.connect.UserInfo;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.PlatformDb;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import de.greenrobot.event.EventBus;

public class QQActivity extends AppCompatActivity {
    private ImageView qq_iv, weibo_iv;
    private Tencent tencent;
    public final static String APP_ID = "1105793412";

    /**
     * 登录授权监听
     */
    private IUiListener listener = new IUiListener() {
        @Override
        public void onComplete(Object o) {
            LogUtil.d("listener onComplete=========== " + o.toString());
            try {
                JSONObject root = new JSONObject(o.toString());
                String openid = root.getString("openid");
                String access_token = root.getString("access_token");
                long expires_in = root.getLong("expires_in");
                tencent.setOpenId(openid);
                tencent.setAccessToken(access_token, "" + expires_in);

                UserInfo userInfo = new UserInfo(QQActivity.this, tencent.getQQToken());
                //请求用户信息
                userInfo.getUserInfo(new IUiListener() {
                    @Override
                    public void onComplete(Object o) {
                        LogUtil.d("UserInfo onComplete ==============" + o.toString());
                    }

                    @Override
                    public void onError(UiError uiError) {
                        LogUtil.e("UserInfo onError " + uiError.errorMessage + " , " + uiError.errorDetail);
                    }

                    @Override
                    public void onCancel() {
                        LogUtil.w("UserInfo onCancel ");
                    }
                });

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }

        @Override
        public void onError(UiError uiError) {
            LogUtil.d("listener onError " + uiError.errorMessage + " , " + uiError.errorDetail);
        }

        @Override
        public void onCancel() {
            LogUtil.w("listener onCancel ");

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qq);

        qq_iv = (ImageView) findViewById(R.id.qq_iv);
        weibo_iv = (ImageView) findViewById(R.id.weibo_iv);

        // 其中APP_ID是分配给第三方应用的appid，类型为String。
        tencent = Tencent.createInstance(APP_ID, getApplicationContext());
        qq_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                loginByQQ();
                loginQQBySdkShare();
            }
        });

        //微博登陆
        weibo_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //创建一个平台实例
                Platform platform = ShareSDK.getPlatform(SinaWeibo.NAME);
                //移除授权
                platform.removeAccount();
                //设置请求回调
                platform.setPlatformActionListener(new PlatformActionListener() {
                    @Override
                    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                        LogUtil.d("ShareSDK onComplete " + platform.toString());
                        PlatformDb db = platform.getDb();
                        final String userIcon = db.getUserIcon();//头像
                        final String userName = db.getUserName();//用户名
                        EventBus.getDefault().post(new EventLogin(userName, userIcon));
                        finish();
                    }

                    @Override
                    public void onError(Platform platform, int i, Throwable throwable) {

                    }

                    @Override
                    public void onCancel(Platform platform, int i) {

                    }
                });

                //请求授权并获取用户信息
                platform.showUser(null);
            }
        });

    }

    /**
     * 通过SdkShare登录qq
     */
    private void loginQQBySdkShare() {
        //创建一个平台实例
        Platform platform = ShareSDK.getPlatform(QQ.NAME);

        platform.removeAccount();
        //设置请求回调
        platform.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                LogUtil.d("ShareSDK onComplete " + platform.toString());
                PlatformDb db = platform.getDb();
                final String userIcon = db.getUserIcon();//头像
                final String userName = db.getUserName();//用户名
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        EventBus.getDefault().post(new EventLogin(userName, userIcon));
//                        finish();
//                    }
//                });
                EventBus.getDefault().post(new EventLogin(userName, userIcon));
                finish();

            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {

            }

            @Override
            public void onCancel(Platform platform, int i) {

            }
        });

        //请求授权并获取用户信息
        platform.showUser(null);


    }

    private void loginByQQ() {
        //是否已经登录
        if (tencent.isSessionValid()) {
            //注销登录
            // tencent.logout(this);
            Toast.makeText(QQActivity.this, "已经登陆，不可重复登陆", Toast.LENGTH_SHORT).show();
        } else {
            //调用登录操作
            //第二个参数：登录时需要请求的权限，默认我们使用"all"
            tencent.login(QQActivity.this, "all", listener);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //从登录页面返回当前页时，需要的回调操作
        Tencent.onActivityResultData(requestCode, resultCode, data, listener);
    }
}




