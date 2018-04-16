package com.as.xiajue.picturebing.base;

import com.trello.rxlifecycle2.LifecycleTransformer;

/**
 * xiaJue 2018/4/8创建
 */
public class BaseContract {
    public interface View {
        <T> LifecycleTransformer<T> bindToLife();
    }

    public interface Presenter<V extends View> {
        void attachView(V view);

        void detachView();
    }
}
