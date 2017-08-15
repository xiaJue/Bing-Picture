package com.as.xiajue.picturebing.dialog;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.as.xiajue.picturebing.R;

/**
 * Created by Moing_Admin on 2017/8/7.
 */

public class DialogManager {
    /**
     * 显示一个”关于界面“的dialog
     */
//    private AlertDialog mAlertDialog;
    public static void showAboutDialog(Context context) {
        Dialog mDialog = new Dialog(context, R.style.about_dialog_style);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_about_layout, null);
        mDialog.setContentView(view);
        mDialog.setCancelable(true);
        mDialog.show();
    }

    public static void showInternetErrorDialog(final Context context) {
        Dialog mDialog = new Dialog(context, R.style.about_dialog_style);
        View view = LayoutInflater.from(context).inflate(R.layout.internet_error_dialog, null);
        View textView=view.findViewById(R.id.dialog_connect_internet_tv);
        /**
         * 打开设置界面
         */
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //打开系统网络连接设置
                //判断手机系统的版本  即API大于10 就是3.0或以上版本
                Intent intent;
                if(android.os.Build.VERSION.SDK_INT>10){
                    intent = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
                }else{
                    intent = new Intent();
                    ComponentName component = new ComponentName("com.android.settings","com.android.settings.WirelessSettings");
                    intent.setComponent(component);
                    intent.setAction("android.intent.action.VIEW");
                }
                context.startActivity(intent);
            }
        });
        /**
         * 水代码
         */
        View tv1 = view.findViewById(R.id.dialog_connect_internet_rsj);
        View tv2 = view.findViewById(R.id.dialog_connect_internet_zsj);
        View.OnClickListener onClick=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId()==R.id.dialog_connect_internet_rsj) {
                    Toast.makeText(context, R.string.lihai, Toast.LENGTH_SHORT).show();
                }else if(v.getId()==R.id.dialog_connect_internet_zsj){
                    Toast.makeText(context, R.string.ccfd, Toast.LENGTH_SHORT).show();
                }
            }
        };
        tv1.setOnClickListener(onClick);
        tv2.setOnClickListener(onClick);
        /**
         * 只是要提醒你.水代码结束
         */
        mDialog.setContentView(view);
        mDialog.show();
    }
}
