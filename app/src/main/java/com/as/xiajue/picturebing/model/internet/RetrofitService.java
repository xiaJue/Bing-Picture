package com.as.xiajue.picturebing.model.internet;

import com.as.xiajue.picturebing.model.bean.HomeItemDataList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * xiaJue 2017/9/5创建
 */
public interface RetrofitService {
    /**
     * 发起一个get请求获取网络数据并解析成javabean
     */
    @GET("HPImageArchive.aspx?format=js")
    Call<HomeItemDataList> getHomeItemData(@Query("idx") int idx, @Query("n") int n);
}
