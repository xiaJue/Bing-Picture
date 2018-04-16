package com.as.xiajue.picturebing.ui.home;

import com.as.xiajue.picturebing.base.BaseContract;

/**
 * xiaJue 2018/4/8创建
 */
public class HomeContract {
    interface View<T> extends BaseContract.View {
        void setUiData(T data);

        void onLoadError(Throwable e);
    }

    interface Presenter extends BaseContract.Presenter<HomeContract.View> {
        void loadData(int idx);

        void refresh();

        /**
         * 数据是否加载成功过
         * @return
         */
        boolean isLoad();
    }
}
