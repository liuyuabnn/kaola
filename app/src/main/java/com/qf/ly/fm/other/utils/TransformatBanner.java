package com.qf.ly.fm.other.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

import com.squareup.picasso.Transformation;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by LY on 2016/10/14.19:37
 * 版权所有 盗版必究
 */

public class TransformatBanner implements Transformation {

    private String key;
    private String url;
    private Context context;
    private int width, height;

    public TransformatBanner(String url, String key, Context context) {
        this.url = url;
        this.key = key;
        this.context = context;
        width = context.getResources().getDisplayMetrics().widthPixels;
        height = width * 3 / 8;
    }

    /**
     * 修改图片的方法
     *
     * @param source 要加工的图片
     * @return 加工后的图片
     */
    @Override
    public Bitmap transform(Bitmap source) {
        //根据图片地址从压缩目录查找有没有对应的处理图片
        String fileName = url.hashCode() + "_banner";
        File targetImage = new File(FileUitl.DIR_PICASSO_IMAGE, fileName);
        //如果文件(图片)已经存在
        if (targetImage.exists()) {
            source.recycle();
            //直接解析路径获得图片
            return BitmapFactory.decodeFile(targetImage.getAbsolutePath());
        }
//        如果文件(图片)不存在，就要对原图进行裁剪
        int sWidth = source.getWidth();
        int sHeight = source.getHeight();
        //先剪出一个8/3的长方形
        Bitmap bitmap = Bitmap.createBitmap(source, 0, sHeight * 2 / 5, sWidth, sHeight * 3 / 5);

        Matrix matrix = new Matrix();//矩阵
        float xscale = 1.0f * width / sWidth;//计算缩放比例,目标大小/原图大小
        float yscale = 1.0f * height /(sHeight * 3 / 5);//计算缩放比例
        //设置X,Y轴方向绽放比例
        matrix.setScale(xscale, yscale);
        //根据缩放比例生成一张新的图片
        Bitmap Bitmap2 = Bitmap.createBitmap(bitmap, 0, 0, sWidth, sHeight*3/5, matrix, false);


        //保存图片操作
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(targetImage);
            Bitmap2.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        source.recycle();
        bitmap.recycle();
        return Bitmap2;
    }

    /**
     * 唯一标识修改的是哪个接口的图片
     *
     * @return
     */
    @Override
    public String key() {
        return key;
    }
}
