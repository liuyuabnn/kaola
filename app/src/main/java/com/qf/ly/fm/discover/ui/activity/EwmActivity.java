package com.qf.ly.fm.discover.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.WriterException;
import com.qf.ly.fm.R;
import com.zxing.activity.CaptureActivity;
import com.zxing.encoding.EncodingHandler;

public class EwmActivity extends AppCompatActivity {
    private EditText editText;
    private TextView textView;
    private Button sao_bt;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ewm);
        editText = (EditText) findViewById(R.id.EditText);
        textView = (TextView) findViewById(R.id.TextView);
        sao_bt = (Button) findViewById(R.id.saoysao_bt);
        imageView = (ImageView) findViewById(R.id.ImageView);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = s.toString();
                try {
                    Bitmap bitmap = EncodingHandler.createQRCode(text, 500);
                    imageView.setImageBitmap(bitmap);
                } catch (WriterException e) {
                    e.printStackTrace();
                }
            }
        });
        sao_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EwmActivity.this, CaptureActivity.class);

                startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            //zxing把扫描的信息用result当键
            String result = data.getStringExtra("result");
            textView.setText(result);
        }
    }
}
