package com.as.xiajue.picturebing.net;

import com.as.xiajue.picturebing.Const;
import com.as.xiajue.picturebing.bean.HomeItemDataList;

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
     * 获取并解析json数据
     */
    public void getDataListFromInternet(int idx,Callback callback){
        Call<HomeItemDataList> call = mService.getHomeItemData(idx, Const.LOAD_JSON_COUNT);
        call.enqueue(callback);
    }

    /**
     *获得github上的数据文件解析
     */
    public void getImportDataListFromInternet(Callback callback){
        //
        Retrofit retrofit =new Retrofit.Builder().baseUrl(Const.GIT_JSON_BASE_URL).addConverterFactory(GsonConverterFactory.create()).
                client(client).build();
        RetrofitService service = retrofit.create(RetrofitService.class);
        Call<HomeItemDataList> call = service.getImportHomeItemData();
        call.enqueue(callback);
    }
}
