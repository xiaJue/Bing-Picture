package com.as.xiajue.picturebing.base;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * xiaJue 2018/4/13创建
 */
public class BasePresenter<V extends BaseContract.View> implements BaseContract.Presenter<V> {

    protected Reference<V> mViewRef;
    protected V mView;

    @Override
    public void attachView(V view) {
        //presenter 持有view的弱引用
        this.mView = new WeakReference<>(view).get();
    }

    @Override
    public void detachView() {
        if (mViewRef != null) {
            if (mView != null) {
                mView = null;
            }
            mViewRef.clear();
            mViewRef = null;
        }
    }
}
