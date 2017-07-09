package com.qf.ly.fm.other.utils;

import android.graphics.Bitmap;
import android.os.AsyncTask;

/**
 * Created by LY on 2016/10/14.9:51
 * 版权所有 盗版必究
 */

public class KaolaTask extends AsyncTask<Object, Integer, Object> {

    private IRequest iRequest;
    private IRequestCallback iRequestCallback;
    private IDownIamge iDownIamge;
    private IDownFile iDownFile;

    /**
     * 如果下载文件使用这个构造方法
     */
    public KaolaTask(IDownFile iDownFile, IRequestCallback iRequestCallback) {
        if (iDownFile == null || iRequestCallback == null) {
            throw new NullPointerException("iDownFile and IRequestCallback can not be null ...");
        }
        this.iDownFile = iDownFile;
        this.iRequestCallback = iRequestCallback;
    }

    /**
     * 如果下载json使用这个构造方法
     *
     * @param iRequest
     * @param iRequestCallback
     */
    public KaolaTask(IRequest iRequest, IRequestCallback iRequestCallback) {
        if (iRequestCallback == null || iRequest == null) {
            throw new NullPointerException("IRequest and IRequestCallback can not be null ...");
        }
        this.iRequestCallback = iRequestCallback;
        this.iRequest = iRequest;
    }

    /**
     * 如果下载图片用这个构造方法
     *
     * @param iDownIamge
     * @param iRequestCallback
     */
    public KaolaTask(IDownIamge iDownIamge, IRequestCallback iRequestCallback) {
        if (iRequestCallback == null || iDownIamge == null) {
            throw new NullPointerException("iDownIamge and IRequestCallback can not be null ...");
        }
        this.iRequestCallback = iRequestCallback;
        this.iDownIamge = iDownIamge;
    }


    /**
     * 准备操作
     */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    /**
     * 执行耗时操作
     *
     * @param params
     * @return
     */
    @Override
    protected Object doInBackground(Object... params) {
        //表示下载文件
        if (iDownFile!=null){
            return iDownFile.downLoadFile();
        }
        //表示下载图片
        if (iDownIamge != null) {
            return iDownIamge.loadImage();
        }

        //如果是直接传进来的值，我们认为是本地的json,可以直接解析
        if (params != null && params.length > 0) {
            //返回解析后的结果 ,主播集合
            return iRequest.parseResult(params[0]);
        }

        //更新进度条，调用onProgressUpdate方法
//        publishProgress();
        //否则执行请求
        Object result = iRequest.doRequest();//json字符串
        //如果为空，表示请求失败，直接回调结果，不用解析了
        if (result != null) {
            return iRequest.parseResult(result);
        }
        return null;
    }

    /**
     * 更新进度
     *
     * @param values
     */
    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    /**
     * 把结果提交给主线程里去执行
     *
     * @param object
     */
    @Override
    protected void onPostExecute(Object object) {
        if (object == null) {
            //回调错误方法
            iRequestCallback.onError();
        } else {
            //请求成功并返回结果
            iRequestCallback.onSuccess(object);
        }
    }

    /**
     * 请求接口
     */
    public interface IRequest {
        /**
         * 具体的请求操作
         *
         * @return 请求结果
         */
        Object doRequest();

        /**
         * 解析结果
         *
         * @param obj json数据
         * @return
         */
        Object parseResult(Object obj);
    }

    /**
     * 请求结果回调接口
     */
    public interface IRequestCallback {
        /**
         * 请求成功
         *
         * @param object 请求结果
         */
        void onSuccess(Object object);

        /**
         * 请求失败
         */
        void onError();

    }

    /**
     * 下载图片的借口
     */
    public interface IDownIamge {
        /**
         * 具体的下载操作
         *
         * @return
         */
        Bitmap loadImage();
    }

    /**
     * 下载文件的接口
     */
    public interface IDownFile {
        Object downLoadFile();
    }
}
