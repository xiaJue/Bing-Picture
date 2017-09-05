package com.as.xiajue.picturebing.model.internet;

import com.as.xiajue.picturebing.Const;
import com.as.xiajue.picturebing.model.bean.HomeItemDataList;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * xiaJue 2017/9/5创建
 */
public class RetrofitUtils {
    private RetrofitService mService;
    private Retrofit mRetrofit;
    private OkHttpClient client;

    public RetrofitUtils() {
        client = new OkHttpClient();
        mRetrofit =new Retrofit.Builder().baseUrl(Const.BASE_URL).addConverterFactory(GsonConverterFactory.create()).
                client(client).build();
        mService = mRetrofit.create(RetrofitService.class);
    }

    /**
     * 使用Retrofit网络框架完成网络的访问
     */

    /**
     * 获取并解析json数据
     */
    public void getDataListFromInternet(int idx,Callback callback){
        Call<HomeItemDataList> call = mService.getHomeItemData(idx, Const.LOAD_JSON_COUNT);
        call.enqueue(callback);
    }
}
