package com.qf.ly.fm.discover.ui.utlpath.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;

import com.qf.ly.fm.R;

/**
 * Created by Administrator on 2016/10/27 0027.10:04
 * 版权所有 盗版必究
 */

public class ExitDialog extends Dialog {
    private Button exitbtn, minbtn, canclebtn;

    public ExitDialog(Context context) {
        this(context, R.style.exit_dialog);
    }

    public ExitDialog(Context context, int themeResId) {
        super(context, themeResId);
        //加载布局
        setContentView(R.layout.exitdialog);
        exitbtn = (Button) findViewById(R.id.exit_btn);
        minbtn = (Button) findViewById(R.id.min_btn);
        canclebtn = (Button) findViewById(R.id.cancle_btn);

        exitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogFinish();
            }
        });
        minbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                Intent intent = new Intent();
                //跳到主页面
                intent.setAction(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
//                //在新的桟里打开
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getContext().startActivity(intent);
            }
        });
        canclebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    public void dialogFinish() {
    }
}
