package com.as.xiajue.picturebing.cache;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.LruCache;

import com.as.xiajue.picturebing.utils.BitmapUtils;
import com.as.xiajue.picturebing.utils.L;
import com.jakewharton.disklrucache.DiskLruCache;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by xiaJue on 2017/8/2.
 */

public class CacheUtils {
    private LruCache<String, Bitmap> mMemoryCache;
    private Context mContext;

    public CacheUtils(Context mContext) {
        this.mContext = mContext;
    }

    /**
     * 初始化内存缓存
     */
    public void initialMemoryCache() {
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        final int cacheSize = maxMemory / 8;//缓存的最大内存
        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getByteCount() / 1024;
            }
        };
    }

    /**
     * 添加图片到内存缓存中
     *
     * @param key
     * @param bitmap
     */
    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        String findKey = hashKeyForDisk(key);
        if (getBitmapFromMemCache(findKey) == null && null != bitmap) {
            mMemoryCache.put(findKey, bitmap);
        }
    }

    /**
     * 从内存缓存中取得图片
     *
     * @param key
     * @return
     */
    public Bitmap getBitmapFromMemCache(String key) {
        return mMemoryCache.get(hashKeyForDisk(key));
    }

    private DiskLruCache mDiskLruCache;
    private static final int DISK_CACHE_MAX_SIZE = 1024 * 1024 * 10; // 10MB
    private static final String DISK_CACHE_SUB_DIR = "imageCache";

    public void initialSdCache() {
        File cacheDir = getDiskCacheDir(mContext, DISK_CACHE_SUB_DIR);
        //初始化sd卡缓存
        try {
            mDiskLruCache = DiskLruCache.open(cacheDir, getAppVersion(mContext), 1,
                    DISK_CACHE_MAX_SIZE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 通过url获得缓存中的对应文件
     *
     * @param url 完整的url路径
     * @return 缓存文件对应的file
     */
    public File getCacheFileFromUrl(String url) {
        String findKey = hashKeyForDisk(url);
        String s = getDiskCacheDir(mContext, DISK_CACHE_SUB_DIR) +
                File.separator + findKey + ".0";
        return new File(s);
    }

    /**
     * 获得缓存目录
     *
     * @param context
     * @param uniqueName
     * @return
     */
    private File getDiskCacheDir(Context context, String uniqueName) {
        String cachePath;
        //如果sd卡存在并且没有被移除
        if (Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return new File(cachePath + File.separator + uniqueName);
    }

    /**
     * 获得应用版本号
     *
     * @param context
     * @return
     */
    private int getAppVersion(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0);
            return info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 1;
    }

    /**
     * 获得MD5当作key
     *
     * @param key
     * @return
     */
    public String hashKeyForDisk(String key) {
        String cacheKey;
        try {
            final MessageDigest mDigest = MessageDigest.getInstance("MD5");
            mDigest.update(key.getBytes());
            cacheKey = bytesToHexString(mDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            cacheKey = String.valueOf(key.hashCode());
        }
        return cacheKey;
    }

    @NonNull
    private String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    public interface LoadSdBitmapCallback {
        void onLoadFinish(Bitmap imageBitmap);
    }

    /**
     * 获取储存卡的缓存图片
     *
     * @param key
     * @return
     */
    public void getSdCache(String key, final int width, final LoadSdBitmapCallback callback) {
        final String findKey = hashKeyForDisk(key);
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                /**
                 * 异步加载本地图片
                 */
                try {
                    DiskLruCache.Snapshot snapshot = mDiskLruCache.get(findKey);
                    if (snapshot != null) {
                        InputStream is = snapshot.getInputStream(0);
                        Bitmap bitmap = BitmapUtils.decodeScaleBitmap(inputStreamToByteArray(is),
                                width);
                        callback.onLoadFinish(bitmap);
                        return null;
                    }
                } catch (IOException e) {
                    L.e("加载异常");
                    e.printStackTrace();
                }
                //如果没有获取到或获取失败
                callback.onLoadFinish(null);
                return null;
            }
        }.execute();
    }

    /**
     * 将inputStream的转换成byte数组
     *
     * @param is
     * @return
     */
    private byte[] inputStreamToByteArray(InputStream is) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] bt = new byte[1021];
        try {
            while ((is.read(bt)) != -1) {
                bos.write(bt, 0, bt.length);
            }
            is.close();
            bos.close();
            return bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 添加SD卡缓存
     *
     * @param key
     * @param is
     * @return
     */
    public void addSdCache(String key, InputStream is) {
        String findKey = hashKeyForDisk(key);
        DiskLruCache.Editor editor;
        try {
            //如果已经存在则不添加
            if (mDiskLruCache.get(findKey) != null) {
                return;
            }
            editor = mDiskLruCache.edit(findKey);
            BufferedInputStream bis = new BufferedInputStream(is, 8 * 1024);
            BufferedOutputStream bos = new BufferedOutputStream(editor.newOutputStream(0), 8 *
                    1024);
            int len;
            while ((len = bis.read()) != -1) {
                bos.write(len);
            }
            bos.flush();
            bis.close();
            bos.close();
            editor.commit();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * flush
     */
    public void flush() {
        try {
            mDiskLruCache.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
