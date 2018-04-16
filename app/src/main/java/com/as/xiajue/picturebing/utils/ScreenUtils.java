package com.as.xiajue.picturebing.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
public class ScreenUtils
{  
    private ScreenUtils()  
    {  
        /* cannot be instantiated */  
        throw new UnsupportedOperationException("cannot be instantiated");  
    }  
  
    public static int getScreenWidth(Context context)
    {  
        WindowManager wm = (WindowManager) context  
                .getSystemService(Context.WINDOW_SERVICE);  
        DisplayMetrics outMetrics = new DisplayMetrics();  
        wm.getDefaultDisplay().getMetrics(outMetrics);  
        return outMetrics.widthPixels;  
    }  
  
    public static int getScreenHeight(Context context)
    {  
        WindowManager wm = (WindowManager) context  
                .getSystemService(Context.WINDOW_SERVICE);  
        DisplayMetrics outMetrics = new DisplayMetrics();  
        wm.getDefaultDisplay().getMetrics(outMetrics);  
        return outMetrics.heightPixels;  
    }  
    public static int getStatusHeight(Context context)
    {  
  
        int statusHeight = -1;  
        try  
        {  
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");  
            Object object = clazz.newInstance();  
            int height = Integer.parseInt(clazz.getField("status_bar_height")  
                    .get(object).toString());  
            statusHeight = context.getResources().getDimensionPixelSize(height);  
        } catch (Exception e)  
        {  
            e.printStackTrace();  
        }  
        return statusHeight;  
    }

    public static Bitmap snapShotWithStatusBar(Activity activity)
    {  
        View view = activity.getWindow().getDecorView();  
        view.setDrawingCacheEnabled(true);  
        view.buildDrawingCache();  
        Bitmap bmp = view.getDrawingCache();  
        int width = getScreenWidth(activity);  
        int height = getScreenHeight(activity);  
        Bitmap bp = null;  
        bp = Bitmap.createBitmap(bmp, 0, 0, width, height);  
        view.destroyDrawingCache();  
        return bp;  
  
    }  
  
    public static Bitmap snapShotWithoutStatusBar(Activity activity)
    {  
        View view = activity.getWindow().getDecorView();  
        view.setDrawingCacheEnabled(true);  
        view.buildDrawingCache();  
        Bitmap bmp = view.getDrawingCache();  
        Rect frame = new Rect();  
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);  
        int statusBarHeight = frame.top;  
  
        int width = getScreenWidth(activity);  
        int height = getScreenHeight(activity);  
        Bitmap bp = null;  
        bp = Bitmap.createBitmap(bmp, 0, statusBarHeight, width, height  
                - statusBarHeight);  
        view.destroyDrawingCache();  
        return bp;  
  
    }  

    public static void setStatusColor(Activity activity, Window win, int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(win, true);
        }
        SystemBarTintManager tintManager = new SystemBarTintManager(activity);
        tintManager.setStatusBarTintEnabled(true);
//        tintManager.setStatusBarTintResource(color);
        tintManager.setStatusBarTintColor(color);
    }


    private static void setTranslucentStatus(Window win, boolean on) {
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;        // a|=b����˼���ǰ�a��b��λ��Ȼ��ֵ��a   ��λ�����˼�����Ȱ�a��b������2���ƣ�Ȼ���û�������൱��a=a|b
        } else {
            winParams.flags &= ~bits;        //&��λ�������棬������  a&=b�൱�� a = a&b  ~�������
        }
        win.setAttributes(winParams);
    }

  
}  