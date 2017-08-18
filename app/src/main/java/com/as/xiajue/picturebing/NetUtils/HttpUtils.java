package com.as.xiajue.picturebing.NetUtils;

import android.content.Context;
import android.graphics.Bitmap;

import com.as.xiajue.picturebing.cache.CacheUtils;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by XiaJue on 2017/7/31.
 * 网络操作的类,封装OKHttp的操作
 */

public class HttpUtils {
    private static HttpUtils mHttpUtils;
    private OkHttpClient client;
    private CacheUtils mCacheUtils;

    private HttpUtils(Context context) {
        client = new OkHttpClient();
        mCacheUtils = new CacheUtils(context);
        mCacheUtils.initialMemoryCache();
        mCacheUtils.initialSdCache();
    }

    public static HttpUtils getInstance(Context context) {
        if (mHttpUtils == null) {
            synchronized (HttpUtils.class) {
                if (mHttpUtils == null) {
                    mHttpUtils = new HttpUtils(context);
                }
            }
        }
        return mHttpUtils;
    }

    /**
     * 通过url获取json数据
     *
     * @param url
     * @param callback
     */
    public void getJsonFromUrl(final String url, final Callback callback) {
                Request request = new Request.Builder().url(url).build();
                Call call = client.newCall(request);
                call.enqueue(callback);
    }

    /**
     * 得到bitmap、如果不存在则从网络获取到本地
     *
     * @param url
     * @param imageWidth
     * @param loadCallback
     */
    public void getBitmap(final String url, final int imageWidth, final LoadCompressBitmapCallback
            loadCallback) {
        /**
         * 从内存中获得图片
         */
        Bitmap bitmap = mCacheUtils.getBitmapFromMemCache(url);
        if (bitmap != null) {
            loadCallback.finish(true, bitmap);
            return;
        }
        /**
         * 从本地缓存获得
         */
        mCacheUtils.getSdCache(url, imageWidth, new CacheUtils.LoadSdBitmapCallback() {
            @Override
            public void onLoadFinish(Bitmap imageBitmap) {
                if (imageBitmap != null) {
                    loadLocalBitmap(imageBitmap, loadCallback, url);
                    return;
                } else {
                    /**
                     * 从网络获取图片
                     */
                    loadBitmapFromNet(url, imageWidth, loadCallback);
                }
            }
        });
    }

    /**
     * 从网络获取图片
     *
     * @param url
     * @param imageWidth
     * @param loadCallback
     */
    private void loadBitmapFromNet(final String url, final int imageWidth, final

    LoadCompressBitmapCallback loadCallback) {
        Request request = new Request.Builder().url(url).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                loadCallback.finish(false, null);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //将图片缓存到本地
                mCacheUtils.addSdCache(url, response.body().byteStream());
                //从本地取出图片
                mCacheUtils.getSdCache(url, imageWidth, new CacheUtils.LoadSdBitmapCallback() {
                    @Override
                    public void onLoadFinish(Bitmap imageBitmap) {
                        loadLocalBitmap(imageBitmap, loadCallback, url);
                    }
                });
            }
        });
    }

    /**
     * 加载到本地缓存图片后的操作
     *
     * @param imageBitmap
     * @param loadCallback
     * @param url
     */
    private void loadLocalBitmap(Bitmap imageBitmap, LoadCompressBitmapCallback loadCallback,
                                 String url) {

        if (imageBitmap != null) {
            //从本地获得图片
            loadCallback.finish(true, imageBitmap);
            mCacheUtils.addBitmapToMemoryCache(url, imageBitmap);
            return;
        }
        loadCallback.finish(true, null);
    }

    /**
     * 对图片的本地缓存进行flush操作
     */
    public void cacheFlush() {
        mCacheUtils.flush();
    }

    /**
     * 加载网络图片的回调接口
     */
    public interface LoadCompressBitmapCallback {
        void finish(boolean isSuccess, Bitmap bitmap);
    }
}
