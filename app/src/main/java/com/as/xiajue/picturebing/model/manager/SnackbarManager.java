package com.as.xiajue.picturebing.model.manager;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.TextView;

import com.as.xiajue.picturebing.R;
import com.as.xiajue.picturebing.model.utils.SystemUtils;

/**
 * xiaJue 2017/9/5创建
 */
public class SnackbarManager {
    /**
     * 显示一个网络访问失败的snackbar
     */
    public static void showInternetFialure(View view,final Context context){
        Snackbar snackbar = Snackbar.make(view, context
                        .getString(R.string.internet_error_dialog_title),
                Snackbar.LENGTH_LONG);
        setSnackbarColor(snackbar, context.getResources().getColor(R
                .color.about_dialog_textColor));
        snackbar.setAction(R.string.to_set,
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //打开网络设置
                        SystemUtils.openWifiSettings(context);
                    }
                }).show();
    }

    /**
     * 修改snackbar样式
     *
     * @param snackbar
     * @param messageColor
     */
    private static void setSnackbarColor(Snackbar snackbar, int messageColor) {
        View view = snackbar.getView();//获取Snackbar的view
        if (view != null) {
            //获取Snackbar的message控件，修改字体颜色
            ((TextView) view.findViewById(R.id.snackbar_text)).setTextColor(messageColor);
            ((TextView) view.findViewById(R.id.snackbar_action)).setTextColor(messageColor);
        }
    }
}
