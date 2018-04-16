package com.as.xiajue.picturebing.net;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * xiaJue 2018/3/6创建
 */
public class RetrofitFactory {
    private static OkHttpClient mOkHttpClient;

    public static OkHttpClient getOkHttpClient() {
        if (mOkHttpClient == null) {
            synchronized (RetrofitFactory.class) {
                if (mOkHttpClient == null) {
                    mOkHttpClient = new OkHttpClient.Builder()
                            .connectTimeout(HttpConfig.HTTP_TIME, TimeUnit.SECONDS)//设置连接超时时间
                            .readTimeout(HttpConfig.HTTP_TIME, TimeUnit.SECONDS)//设置读取超时时间
                            .writeTimeout(HttpConfig.HTTP_TIME, TimeUnit.SECONDS)//设置写入超时时间
                            .addInterceptor(InterceptorUtil.HeaderInterceptor())//添加其他拦截器
                            .addInterceptor(InterceptorUtil.LogInterceptor())//添加日志拦截器
                            .build();
                }
            }
        }
        return mOkHttpClient;
    }

    private static Retrofit mRetrofit;

    public static <T> T create(Class<T> clazz) {
        if (mRetrofit == null) {
            synchronized (RetrofitFactory.class) {
                if (mRetrofit == null) {
                    mRetrofit = new Retrofit.Builder()
                            .baseUrl(HttpConfig.BASE_URL)
                            .addConverterFactory(GsonConverterFactory.create())//添加gson转换器
                            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())//添加rxjava转换器
                            .client(getOkHttpClient())//传入上面创建的OKHttpClient
                            .build();
                }
            }
        }
        return mRetrofit.create(clazz);
    }
}
