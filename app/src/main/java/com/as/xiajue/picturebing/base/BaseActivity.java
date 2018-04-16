package com.as.xiajue.picturebing.base;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.trello.rxlifecycle2.LifecycleTransformer;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

/**
 * xiaJue 2018/4/8创建
 */
public abstract class BaseActivity<P extends BaseContract.Presenter> extends RxAppCompatActivity
        implements BaseContract.View {
    protected P mPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        bindView();
        initialize();
    }

    protected abstract void initialize();

    protected abstract void bindView();

    protected abstract int getLayoutId();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //un bind
        if (mPresenter != null) {
            mPresenter.detachView();
        }
    }

    @Override
    public <T> LifecycleTransformer<T> bindToLife() {
        return this.bindToLifecycle();
    }

}
