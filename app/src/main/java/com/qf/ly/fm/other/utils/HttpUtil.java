package com.qf.ly.fm.other.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by ly on 2016/9/19.14:35
 */
public class HttpUtil {

    /**
     * Post请求
     *
     * @param httpUrl
     * @param params
     * @return
     */
    public static String doPost(String httpUrl, Map<String, String> params) {

        //content=wwwww&revieweruid=2754846&resourcetype=1&reviewername=QianFengLaoLiu&commenttype=0&resourceid=1000002787562&
//在连接之前先把map转化为字符串
        if (httpUrl == null || params == null) {
            throw new NullPointerException("httpUrl or params can not be null.");
        }
        Set<Map.Entry<String, String>> entries = params.entrySet();
        //获取迭代器
        Iterator<Map.Entry<String, String>> iterator = entries.iterator();
        StringBuffer buffer = new StringBuffer();
        //开始迭代
        while (iterator.hasNext()) {
            Map.Entry<String, String> next = iterator.next();
            String key = next.getKey();
            buffer.append(key);
            buffer.append("=");
            String value = next.getValue();
            buffer.append(value);
            buffer.append("&");
        }
        //我们要写入到服务端的参数字符串
        String paramsString = buffer.toString();
        HttpURLConnection conn = null;
        OutputStream os = null;
        InputStream is = null;
        InputStreamReader reader = null;
        BufferedReader bufferedReader = null;
        try {
            URL url = new URL(httpUrl);
            conn = (HttpURLConnection) url.openConnection();
            //设置POST请求方式
            conn.setRequestMethod("POST");
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
            //POST请求需要写入数据
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.connect();
            //在获取返回码之前先写入参数
            os = conn.getOutputStream();
            //把字符串转化成字节数组写到服务端
            os.write(paramsString.getBytes());//将字符串装换为字节数组
            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                //获取输入流
                is = conn.getInputStream();
                //字节流转换成字符流
                reader = new InputStreamReader(is);
                //转换缓冲流
                bufferedReader = new BufferedReader(reader);

                String read = null;
                //用来接收读取的结果
                StringBuffer resultBuffer = new StringBuffer();

                while ((read = bufferedReader.readLine()) != null) {
                    //每读取一行，拼接到结果集里
                    resultBuffer.append(read);
                }

                String result = resultBuffer.toString();
                LogUtil.w("请求成功 result = " + result);

                return result;
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //如果is!=null表示请求成功了，需要关闭流
            if (is != null) {
                try {
                    is.close();
                    reader.close();
                    bufferedReader.close();
                    conn.disconnect();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        LogUtil.e("请求失败了");

        return null;
    }


    //下载JSON数据
    public static String getJson(String path) {
        StringBuffer sb = new StringBuffer();
        InputStream is = null;
        BufferedReader br = null;
        HttpURLConnection huc = null;
        try {
            URL url = new URL(path);
            huc = (HttpURLConnection) url.openConnection();
            int code = huc.getResponseCode();
            if (code == HttpURLConnection.HTTP_OK) {
                is = huc.getInputStream();
                br = new BufferedReader(new InputStreamReader(is));
                String line = null;
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
                if (is != null) {
                    is.close();
                }
                huc.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();

    }

    //下载图片
    public static Bitmap getBitmap(String path) {
        //根据路径先去二级缓存里面去找
        Bitmap bm = null;
        //文件输出流用来保存图片
        FileOutputStream fos = null;
        String fileName = path.hashCode() + "";
        File file = new File(FileUitl.DIR_IMAGE, fileName);
        //如果图片存在
        if (file.exists()) {
            //返回文件所在路径解析成Bitmap
            bm = BitmapFactory.decodeFile(file.getAbsolutePath());
            return bm;
        } else {
            InputStream is = null;
            HttpURLConnection huc = null;
            try {
                URL url = new URL(path);
                huc = (HttpURLConnection) url.openConnection();
                int code = huc.getResponseCode();
                if (code == HttpURLConnection.HTTP_OK) {
                    is = huc.getInputStream();
                    bm = BitmapFactory.decodeStream(is);
                    //下载好存到本地
                    fos = new FileOutputStream(file);
                    //保存图片(图片类型，图片质量，输出流)
                    bm.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                    return bm;
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    is.close();
                    huc.disconnect();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        return null;
    }

    /**
     * 下载文件
     */
    public static File getFile(String path, File dir) {
        String fname = path.hashCode() + ".MP3";
        File file = new File(dir, fname);
        if (file.exists()) {
            return file;
        }
        HttpURLConnection huc = null;
        InputStream is = null;
        FileOutputStream fos = null;
        try {
            URL url = new URL(path);
            //打开连接
            huc = (HttpURLConnection) url.openConnection();
            //GET请求方式
            huc.setRequestMethod("GET");//默认就是GET,也可以不用设置
            //连接超时时长
            huc.setConnectTimeout(5000);
            //读取超时时长
            huc.setReadTimeout(5000);
            //开始连接
            huc.connect();
            int code = huc.getResponseCode();
            if (code == HttpURLConnection.HTTP_OK) {
                is = huc.getInputStream();
                fos = new FileOutputStream(file);
                //获取文件的大小
                long contentLength = huc.getContentLength();
                long down = 0;
                byte[] buffer = new byte[1024 * 1024];
                int len = -1;
                while ((len = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, len);
                    fos.flush();

                    down += len;
                    int progress = (int) (100 * down / contentLength);
                    LogUtil.d("下载进度" + progress);
                }
                return file;
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    is.close();
                    fos.close();
                    huc.disconnect();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }

        return null;
    }
}
