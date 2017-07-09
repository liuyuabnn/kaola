package com.qf.ly.fm.discover.ui.utlpath.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.qf.ly.fm.discover.ui.entiy.FenLeiTop;
import com.qf.ly.fm.other.utils.DeviceUtil;
import com.qf.ly.fm.other.utils.FileUitl;
import com.qf.ly.fm.other.utils.HttpUtil;
import com.qf.ly.fm.other.utils.KaolaTask;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 分类页面
 * 由7个正方形组成的图形
 * Created by Administrator on 2016/10/17 0017.18:36
 * 版权所有 盗版必究
 */

public class FenLeiView extends View {

    private List<FenLeiTop> list;
    private int size;//最小的正方形的边长
    private final int paddingWidth = 5;//白色间距的宽度
    private Map<String, Bitmap> map = new HashMap<>();

    private TypeRect[] RectArray;

    private Paint paint = new Paint();
    /**
     * 三步定义一个接口
     */
    private FenLeiOnClickListener clickListener;

    public interface FenLeiOnClickListener {
        void onClick(int position);
    }

    public void setFenLeiOnClickListener(FenLeiOnClickListener listener) {
        this.clickListener = listener;
    }

    public FenLeiView(Context context) {
        this(context, null);
    }

    public FenLeiView(Context context, AttributeSet attrs) {
        super(context, attrs);
        int width = getResources().getDisplayMetrics().widthPixels;
        size = (width - (int) DeviceUtil.getPxFromDp(context, 20) - paddingWidth * 3) / 4;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = size * 4 + paddingWidth * 3;
        int height = size * 3 + paddingWidth * 2;
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //在setTypeTopList显示数据之前会默认调用一次
        if (RectArray == null) {
            return;
        }
        for (int i = 0; i < RectArray.length; i++) {
            TypeRect typeRect = RectArray[i];
            String pic = typeRect.getPic();
            Bitmap bitmap = map.get(pic);
            if (bitmap == null) {
                return;
            }
            canvas.drawBitmap(bitmap, typeRect.getFromX(), typeRect.getFromY(), paint);
        }

    }

    public void setFenLeiList(List<FenLeiTop> list) {
        this.list = list;
        RectArray = new TypeRect[]{
                new TypeRect(size * 0 + paddingWidth * 0, size * 0 + paddingWidth * 0, size * 2 + paddingWidth * 1, size * 2 + paddingWidth * 1, list.get(0).getPic()),
                new TypeRect(size * 2 + paddingWidth * 2, size * 0 + paddingWidth * 0, size * 2 + paddingWidth * 1, size * 1 + paddingWidth * 0, list.get(1).getPic()),
                new TypeRect(size * 2 + paddingWidth * 2, size * 1 + paddingWidth * 1, size * 2 + paddingWidth * 1, size * 1 + paddingWidth * 0, list.get(2).getPic()),
                new TypeRect(size * 0 + paddingWidth * 0, size * 2 + paddingWidth * 2, size * 1 + paddingWidth * 0, size * 1 + paddingWidth * 0, list.get(3).getPic()),
                new TypeRect(size * 1 + paddingWidth * 1, size * 2 + paddingWidth * 2, size * 1 + paddingWidth * 0, size * 1 + paddingWidth * 0, list.get(4).getPic()),
                new TypeRect(size * 2 + paddingWidth * 2, size * 2 + paddingWidth * 2, size * 1 + paddingWidth * 0, size * 1 + paddingWidth * 0, list.get(5).getPic()),
                new TypeRect(size * 3 + paddingWidth * 3, size * 2 + paddingWidth * 2, size * 1 + paddingWidth * 0, size * 1 + paddingWidth * 0, list.get(6).getPic()),
        };

        for (int i = 0; i < list.size(); i++) {
            FenLeiTop fenLeiTop = list.get(i);
            final String pic = fenLeiTop.getPic();
            final int position = i;
            KaolaTask task = new KaolaTask(new KaolaTask.IDownIamge() {
                @Override
                public Bitmap loadImage() {
                    Bitmap bitmap = HttpUtil.getBitmap(pic);
                    return getTransformat(bitmap, pic, position);
                }
            }, new KaolaTask.IRequestCallback() {
                @Override
                public void onSuccess(Object object) {
                    Bitmap bitmap = (Bitmap) object;
                    map.put(pic, bitmap);
                    invalidate();
                }

                @Override
                public void onError() {
                    Toast.makeText(getContext(), "图片下载失败", Toast.LENGTH_SHORT).show();
                }
            });

            task.execute();
        }


    }

    private Bitmap getTransformat(Bitmap source, String pic, int position) {

        if (source == null) {
            return source;
        }
        File image = new File(FileUitl.DIR_PICASSO_IMAGE, "" + pic.hashCode());
        if (image.exists()) {
            source.recycle();
            return BitmapFactory.decodeFile(image.getAbsolutePath());
        }
        int width = source.getWidth();
        int height = source.getHeight();
        TypeRect type = RectArray[position];
        int targetWidth = type.getWidth();
        int targetHeight = type.getHeight();
        Bitmap target = null;
        switch (position) {
            //只进行缩放
            case 0:
            case 3:
            case 4:
            case 5:
            case 6:
                Matrix matrix1 = new Matrix();
                float scale = 1.0f * targetWidth / width;
                matrix1.setScale(scale, scale);
                target = Bitmap.createBitmap(source, 0, 0, width, height, matrix1, false);
                break;
            //裁剪缩放
            case 1:
            case 2:
                Bitmap bitmap = Bitmap.createBitmap(source, 0, height / 4, width, height / 2);
                Matrix matrix2 = new Matrix();
                float xscale = 1.0f * targetWidth / bitmap.getWidth();
                float yscale = 1.0f * targetHeight / bitmap.getHeight();
                matrix2.setScale(xscale, yscale);
                target = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix2, false);
                bitmap.recycle();
                break;
        }
        source.recycle();
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(image);
            target.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
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

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        //获取触摸在当前控件中的坐标
        float x = event.getX();
        float y = event.getY();
        switch (action) {
            //按下事件,所有事件都是从按下开始
            case MotionEvent.ACTION_DOWN:
                return true;
            //当手指抬起的时候就相当于执行一次点击事件
            case MotionEvent.ACTION_UP:
                int touchposition = getTouchPosition(x, y);
                if (touchposition == -1) {
                    Toast.makeText(getContext(), "未知区域", Toast.LENGTH_SHORT).show();
                } else {
                    if (clickListener != null) {
                        clickListener.onClick(touchposition);
                    }
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    private int getTouchPosition(float x, float y) {
        for (int i = 0; i < RectArray.length; i++) {
            TypeRect typeRect = RectArray[i];
            if (typeRect.isTouched(x, y)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 该类用于封装图片显示位置以及大小的信息
     */
    private class TypeRect {
        int fromX, fromY, width, height;
        String pic;

        public TypeRect(int fromX, int fromY, int width, int height, String pic) {
            this.fromX = fromX;
            this.fromY = fromY;
            this.width = width;
            this.height = height;
            this.pic = pic;
        }

        /**
         * 是否点中该区域
         *
         * @param x
         * @param y
         * @return
         */
        public boolean isTouched(float x, float y) {
            if (x > fromX && x < fromX + width
                    && y > fromY && y < fromY + height) {
                return true;
            }

            return false;
        }

        public int getFromX() {
            return fromX;
        }

        public void setFromX(int fromX) {
            this.fromX = fromX;
        }

        public int getFromY() {
            return fromY;
        }

        public void setFromY(int fromY) {
            this.fromY = fromY;
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }
    }


}
