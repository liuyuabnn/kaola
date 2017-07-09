package com.qf.ly.fm.other.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;

import com.squareup.picasso.Transformation;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by LY on 2016/10/12.12:38
 * 版权所有 盗版必究
 */

public class TransformatSquare implements Transformation {

    private int size;
    private String key;
    private String url;
    private boolean isNeedCircle;

    /**
     * 裁剪正方形
     *
     * @param size 目标正方形边长,也jiushiImageView的宽高
     */
    public TransformatSquare(String url, int size, String key, boolean isNeedCircle) {
        this.url = url;
        this.size = size;
        this.key = key;
        this.isNeedCircle = isNeedCircle;
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
        String fileName = url.hashCode() + "_circle";
        File targetImage = new File(FileUitl.DIR_PICASSO_IMAGE, fileName);
        if (isNeedCircle) {
            //如果文件(图片)已经存在
            if (targetImage.exists()) {
                source.recycle();
                //直接解析路径获得图片
                return BitmapFactory.decodeFile(targetImage.getAbsolutePath());
            }
        }
        //如果不需要圆形图片或者文件(图片)不存在，就要对原图进行裁剪
        int sWidth = source.getWidth();
        int sHeight = source.getHeight();
        Bitmap target = null;
        //如果刚好是正方形
        Matrix matrix = new Matrix();//矩阵
        if (sWidth == sHeight) {
            float scale = 1.0f * size / sHeight;//计算缩放比例
            //设置X,Y轴方向绽放比例
            matrix.setScale(scale, scale);
            //根据缩放比例生成一张新的图片
            target = Bitmap.createBitmap(source, 0, 0, sWidth, sHeight, matrix, false);
        } else {
            //另一种计算方式
            int fSize = Math.min(sWidth, sHeight);
            int fromX = (sWidth - fSize) / 2;
            int fromY = (sHeight - fSize) / 2;
            //先剪出一个正方形
            Bitmap bitmap = Bitmap.createBitmap(source, fromX, fromY, fSize, fSize);
            float scale = 1.0f * size / fSize;//缩放比例
            //设置X,Y轴方向绽放比例
            matrix.setScale(scale, scale);
            //缩放生成一张新的图片
            target = Bitmap.createBitmap(bitmap, 0, 0, fSize, fSize, matrix, false);
            //吧不用的图片回收掉
            bitmap.recycle();
        }

        //在返回加工后的图片之前要释放源图片
        source.recycle();
        //把裁剪好的方形图片边成圆形图片
        if (isNeedCircle) {
            target = transformatCircleBitmap(target);
        }

        //保存图片操作
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(targetImage);
            target.compress(Bitmap.CompressFormat.PNG, 100, fos);
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


        return target;
    }

    /**
     * 加工圆形图片
     *
     * @param source
     * @return
     */
    public Bitmap transformatCircleBitmap(Bitmap source) {

        int sWidth = source.getWidth();
        int size = sWidth;
        Bitmap background = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
        //创建一个画布，把方形的background放里面
        Canvas canvas = new Canvas(background);
        Paint paint = new Paint();
        //设置画笔的重叠模式，取两个图形的交集
        canvas.drawCircle(size / 2, size / 2, size / 2, paint);//必须先画一个圆
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        //取两个图形的交集
        canvas.drawBitmap(source, 0, 0, paint);
        source.recycle();
        return background;
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
