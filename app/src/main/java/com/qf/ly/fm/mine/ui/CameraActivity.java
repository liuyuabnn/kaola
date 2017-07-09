package com.qf.ly.fm.mine.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.qf.ly.fm.R;
import com.qf.ly.fm.other.utils.FileUitl;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CameraActivity extends AppCompatActivity {
    private ImageView camera_iv, show_iv;
    private File imageFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        camera_iv = (ImageView) findViewById(R.id.camera_iv);
        show_iv = (ImageView) findViewById(R.id.show_image);
        camera_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //用当前时间作为文件名
                Date date = new Date();
                SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");//获取当前时间，进一步转化为字符串
                String fileName = format.format(date) + ".jpg";
                imageFile = new File(FileUitl.DIR_IMAGE, fileName);

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                Uri uri = Uri.fromFile(imageFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                startActivityForResult(intent, 1);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {

            Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());

            show_iv.setImageBitmap(bitmap);
            camera_iv.setImageBitmap(bitmap);
        }
    }
}
