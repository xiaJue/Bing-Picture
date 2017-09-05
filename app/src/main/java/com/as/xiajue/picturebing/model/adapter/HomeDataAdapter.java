package com.as.xiajue.picturebing.model.adapter;

import android.content.Context;
import android.os.AsyncTask;

import com.as.xiajue.picturebing.Const;
import com.as.xiajue.picturebing.model.internet.RetrofitUtils;
import com.as.xiajue.picturebing.model.bean.HomeItemData;
import com.as.xiajue.picturebing.model.bean.HomeItemDataList;
import com.as.xiajue.picturebing.model.database.HomeItemInfoDao;
import com.as.xiajue.picturebing.utils.L;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * xiaJue 2017/9/5创建
 */
public class HomeDataAdapter {
    private Context mContext;
    private RetrofitUtils mRetrofitUtils;//网络访问操作类
    private HomeItemInfoDao mInfoDao;//数据库操作类

    private List<HomeItemData> mList;

    public void setList(List<HomeItemData> list) {
        this.mList = list;
    }

    public HomeDataAdapter(Context context) {
        this.mContext=context;
        mRetrofitUtils = new RetrofitUtils();
        mInfoDao = new HomeItemInfoDao(context);
    }
    /**
     * 当前的加载页数
     *
     * @return 获得当前加载的页数
     */
    public int getLoadingIdx() {
        L.e("size="+mList.size());
        return mList.size() / Const.LOAD_JSON_COUNT;
    }

    /**
     * 从网络加载数据并显示到列表中
     */
    public void load(int idx, final OnRefreshFinish finish){
        mRetrofitUtils.getDataListFromInternet(idx*Const.LOAD_JSON_COUNT, new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                //internet get json to javabean
                List<HomeItemData> list = ((HomeItemDataList) response.body()).getImages();
                //database try get json to javabean
                List<HomeItemData> tempList = mInfoDao.selectAll();
                for (HomeItemData data :
                        tempList) {
                    if (!HomeItemData.listContains(list, data)) {
                        //add to list
                        list.add(data);
                    }
                }
                finish.success(list);
                //insert to database
                 new InsertTask().execute();
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                List<HomeItemData> tempList = mInfoDao.selectAll();
                finish.fialure(tempList);
            }
        });
    }
    /**
     * 异步添加数据到数据库
     */
    class InsertTask extends AsyncTask {
        @Override
        protected Object doInBackground(Object[] params) {
            if (mList.size() == mInfoDao.selectAll().size()) {
                return null;
            }
            /**
             *倒序存入数据库
             */
            for (int i = 0; i < mList.size(); i++) {
                HomeItemData data = mList.get(i);
                if (!mInfoDao.isExist(data)) {//如果不存在则添加
                    mInfoDao.add(data);
                }
            }
            return null;
        }
    }
}
