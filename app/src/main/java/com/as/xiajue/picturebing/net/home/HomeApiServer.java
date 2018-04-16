package com.as.xiajue.picturebing.net.home;

import com.as.xiajue.picturebing.Const;
import com.as.xiajue.picturebing.bean.HomeItemDataList;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * xiaJue 2018/4/14创建
 */
public interface HomeApiServer {
    /**
     * 发起一个get请求获取网络数据并解析成javabean
     */
    @GET(Const.BING_IMAGE_JSON_URL)
    Observable<Response<HomeItemDataList>> getData(@Query("idx") int idx, @Query("n") int
            n);

    /**
     * 发起一个get请求获取网络数据并解析成javabean
     */
    @GET(Const.GIT_JSON_URL)
    Observable<Response<HomeItemDataList>> importData();
}
