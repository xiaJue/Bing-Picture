package com.as.xiajue.picturebing.ui.details;

import android.content.Context;

import com.as.xiajue.picturebing.base.BaseContract;

/**
 * xiaJue 2018/4/14创建
 */
public class DetailsContract {
    interface View<T> extends BaseContract.View {
        void saveSuccess();

        void saveError();

        void saveExist();

        void shareError();

        void shareSuccess();
    }

    interface Presenter extends BaseContract.Presenter<DetailsContract.View> {
        void saveImage(Context context, String url);

        void shareImage(Context context, String url);

        void clearTempFile();

    }
}
