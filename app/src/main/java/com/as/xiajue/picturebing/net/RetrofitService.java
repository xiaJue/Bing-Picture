package com.as.xiajue.picturebing.net;

import com.as.xiajue.picturebing.Const;
import com.as.xiajue.picturebing.bean.HomeItemDataList;

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
    @GET(Const.BING_IMAGE_JSON_URL)
    Call<HomeItemDataList> getHomeItemData(@Query("idx") int idx, @Query("n") int n);
    /**
     * 发起一个get请求获取网络数据并解析成javabean
     */
    @GET(Const.GIT_JSON_URL)
    Call<HomeItemDataList> getImportHomeItemData();
}
