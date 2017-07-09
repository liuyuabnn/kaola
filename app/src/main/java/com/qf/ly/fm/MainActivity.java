package com.qf.ly.fm;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.qf.ly.fm.other.ui.BannerActivity;
import com.qf.ly.fm.other.ui.GuideActivity;
import com.qf.ly.fm.other.utils.KaolaTask;
import com.qf.ly.fm.other.widget.SdkDialog;

public class MainActivity extends AppCompatActivity {
    private KaolaTask task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        //进入应用的第一件事，请求版本
//        //把本地的版本和服务端的版本进行对比，如果不一样，表示需要更新
//        //先获取本地的版本
//        PackageManager packageManager = getPackageManager();
//        //本应用的信息
//        PackageInfo packageInfo = null;
//        try {
//            packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
//            //本地的版本
//            //如果清单文件和build.gradle文件里都有版本信息，并且他们都不一样，那么会以build.gradle的描述为准
//            final String localVersion = packageInfo.versionName;
////            int code = packageInfo.versionCode;
//            task = new KaolaTask(new KaolaTask.IRequest() {
//                @Override
//                public Object doRequest() {
//                    return HttpUtil.getJson(Path.URL_UPGRADE + localVersion);
//                }
//
//                @Override
//                public Object parseResult(Object obj) {
//                    try {
//                        JSONObject root = new JSONObject(obj.toString());
//                        String code = root.getString("code");
//                        String message = root.getString("message");
//                        if ("10000".equals(code) && "success".equals(message)) {
//                            JSONObject result = root.getJSONObject("result");
//                            int updateType = result.getInt("updateType");
//                            if (updateType == 0) {
//                                String msg = result.getString("updateInfo");//更新的信息描述
//                                String netVersion = result.getString("updateVersion");//更新的版本号
//                                String apkUrl = result.getString("updateUrl");//apk下载路径
//                                //如果本地版本和网络版本不一样，那么需要下载apk
//                                if (!localVersion.equals(netVersion)) {
//                                    return result;
//                                }
//                            } else {
//                                return "已经是最新版本了";
//                            }
//                        }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                    return null;
//                }
//            }, new KaolaTask.IRequestCallback() {
//                @Override
//                public void onSuccess(Object object) {
//                    //显示更新对话框
//                    JSONObject result = null;
//                    try {
//                        result = (JSONObject) object;
//                    } catch (Exception e) {
//                        Toast.makeText(MainActivity.this, result.toString(), Toast.LENGTH_SHORT).show();
//                        return;
//                    }
//                    try {
//                        String netVersion = result.getString("updateVersion");//更新的版本号
//                        String msg = result.getString("updateInfo");
//                        String apkUrl = result.getString("updateUrl");
//                        //显示更新信息
//                        showUpgradeDialog(msg, netVersion, apkUrl);
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//
//                @Override
//                public void onError() {
//                    Toast.makeText(MainActivity.this, "请求版本失败", Toast.LENGTH_SHORT).show();
//                }
//            });
//
//            task.execute();
//
//        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
//        }

        Intent intent = null;
        if (isFirstUsed()) {
            //如果是第一次使用就跳转到欢迎页面
            intent = new Intent(this, GuideActivity.class);
        } else {
            //否则跳到BannerActivity
            intent = new Intent(this, BannerActivity.class);

        }
        startActivity(intent);
        //进入其他页面后不能再回到MainActivity
        finish();

    }

    private void showUpgradeDialog(String msg, String netVersion, String apkUrl) {

        //显示一个对话框如果点击确定下载，下载到本地文件夹中，自动进入安装页面installApk
        SdkDialog dialog = new SdkDialog(this, msg, netVersion, apkUrl);
        dialog.show();
        /**
         * 进入安装页面
         * @param context
         * @param apk apk的存放目录
         */
//        public static void installApk(Context context, File apk)
//        {
//            Uri uri = Uri.fromFile(apk);
//            // 核心是下面几句代码
//            Intent intent = new Intent(Intent.ACTION_VIEW);
//            intent.setDataAndType(uri, "application/vnd.android.package-archive");
//            context.startActivity(intent);
//        }


    }

    private boolean isFirstUsed() {
        SharedPreferences preferences = getSharedPreferences("firstUsed", MODE_PRIVATE);

        boolean firstUsed = preferences.getBoolean("flag", true);
        return firstUsed;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (task != null) {
            task.cancel(false);
            task = null;

        }
    }
}
