package com.as.xiajue.picturebing.model.manager;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

/**
 * xiaJue 2017/9/6创建
 */
public class MemoryCacheManager {
    private LruCache<String, Bitmap> mLruCache;//----ps:imageLoader默认的内存缓存管理会导致一个bug...所以自定义一个内存缓存管理
    public static MemoryCacheManager memoryCacheManager;

    public MemoryCacheManager(Context context) {
        //初始化内存缓存
        int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        int cacheSize = maxMemory / 8;//缓存的最大内存
        mLruCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getByteCount() / 1024;
            }
        };
    }

    public static MemoryCacheManager getInstance(Context context) {
        if (memoryCacheManager == null) {
            synchronized (MemoryCacheManager.class) {
                if (memoryCacheManager == null) {
                    memoryCacheManager = new MemoryCacheManager(context);
                }
            }
        }
        return memoryCacheManager;
    }

    /**
     * add to memory
     */
    public void addCache(String key, Bitmap bitmap){
        if (getCache(key) == null) {
            mLruCache.put(key, bitmap);
        }
    }

    /**
     * get from memory
     */
    public Bitmap getCache(String key) {
        return mLruCache.get(key);
    }

}
