package com.qf.ly.fm.other.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.qf.ly.fm.R;
import com.qf.ly.fm.other.utils.FileUitl;
import com.qf.ly.fm.other.utils.HttpUtil;

import java.io.File;

/**
 * Created by Administrator on 2016/11/1 0001.10:35
 * 版权所有 盗版必究
 */

public class SdkDialog extends Dialog {
    private Button btn1, btn2;
    private TextView textView;
    private String text, netVersion, url;

    public SdkDialog(Context context, String text, String netVersion, String url) {
        this(context, R.style.sdk_dialog);
        this.text = text;
        this.netVersion = netVersion;
        this.url = url;
    }

    public SdkDialog(Context context, int themeResId) {
        super(context, themeResId);
        //加载布局
        setContentView(R.layout.sdkdialog);
        btn1 = (Button) findViewById(R.id.sdk_btn1);
        btn2 = (Button) findViewById(R.id.sdk_btn2);
        textView = (TextView) findViewById(R.id.sdk_textview);
        textView.setText(text);
        //下载
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                File file = HttpUtil.getFile(url, FileUitl.DIR_SDK);
                Uri uri = Uri.fromFile(file);
                // 核心是下面几句代码,跳转到安装页面
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(uri, "application/vnd.android.package-archive");
                getContext().startActivity(intent);


            }
        });
        //取消
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    }

}
