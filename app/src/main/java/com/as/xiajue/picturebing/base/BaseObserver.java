package com.as.xiajue.picturebing.base;

import android.accounts.NetworkErrorException;

import com.as.xiajue.picturebing.utils.L;

import java.net.ConnectException;
import java.net.UnknownHostException;
import java.util.concurrent.TimeoutException;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import retrofit2.Response;


/**
 * xiaJue 2018/3/6创建
 */
public abstract class BaseObserver<T> implements Observer<Response<T>> {
    @Override
    public void onSubscribe(Disposable d) {
        onRequestStart();
    }

    @Override
    public void onNext(Response<T> result) {
        onRequestEnd();
        L.e("next>>>>>>>>>>>>>>>>>" + result.code());

        if (result.code() == 200) {
            T body = result.body();
            L.e(body + ">>>>>>>>>>>");
            try {
                onSuccess(result);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                onCodeError(result);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onError(Throwable e) {
        onRequestEnd();
        try {
            if (e instanceof ConnectException
                    || e instanceof TimeoutException
                    || e instanceof NetworkErrorException
                    || e instanceof UnknownHostException) {
                onFailure(e, true);
            } else {
                onFailure(e, false);
            }
        } catch (Exception e1) {
            e1.printStackTrace();
            onFailure(e1, false);
        }
    }

    @Override
    public void onComplete() {

    }

    protected void onRequestStart() {
    }

    protected void onRequestEnd() {
    }

    public abstract void onFailure(Throwable e, boolean isNetWorkError);


    public abstract void onSuccess(Response<T> result);

    protected void onCodeError(Response<T> result) {
    }

}
