package com.as.xiajue.picturebing.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import static android.graphics.BitmapFactory.decodeByteArray;

/**
 * Created by xiaJue on 2017/8/3.
 * 创建Bitmap的一些相关操作
 */

public class BitmapUtils {
    /**
     * 计算压缩图片的SampleSize
     *
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int
            reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >=
                    reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    /**
     * 通过byte数组拿到压缩后的图片
     *
     * @param bytes
     * @param imageWidth
     * @return
     */
    public static Bitmap decodeScaleBitmap(byte[] bytes, int imageWidth) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        decodeByteArray(bytes, 0, bytes.length, options);
        //插---图片的精确最终高度((float) getItemWidth() / bitWidth) ps: *1.0和转换成float是一样的
        int imageHeight = (int) (options.outHeight * (imageWidth * 1.0 / options
                .outWidth));
        //尔等继续
        options.inSampleSize = calculateInSampleSize(options, imageWidth, imageWidth);
        options.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
        Bitmap scaleBitmap = Bitmap.createScaledBitmap(bitmap, imageWidth, imageHeight,
                false);
        return scaleBitmap;
    }
}
