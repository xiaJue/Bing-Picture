package com.as.xiajue.picturebing.model.manager;

import android.content.Context;
import android.os.AsyncTask;

import com.as.xiajue.picturebing.Const;
import com.as.xiajue.picturebing.model.bean.HomeData2ImageBean;
import com.as.xiajue.picturebing.model.bean.HomeItemData;
import com.as.xiajue.picturebing.model.bean.HomeItemDataList;
import com.as.xiajue.picturebing.model.database.ImageDao;
import com.as.xiajue.picturebing.model.database.bean.ImageBean;
import com.as.xiajue.picturebing.model.internet.RetrofitUtils;
import com.as.xiajue.picturebing.presenter.presenterInterfece.OnGetImportFinish;
import com.as.xiajue.picturebing.presenter.presenterInterfece.OnRefreshFinish;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * xiaJue 2017/9/5创建
 */
public class HomeDataManager {
    private RetrofitUtils mRetrofitUtils;//网络访问操作类
//    private HomeItemInfoDao mInfoDao;//数据库操作类
    private ImageDao mImageDao;//数据库操作类

    private List<HomeItemData> mList;

    public void setList(List<HomeItemData> list) {
        this.mList = list;
    }

    public HomeDataManager(Context context) {
        mRetrofitUtils = new RetrofitUtils();
        mImageDao = new ImageDao(context);
    }
    /**
     * 当前的加载页数
     *
     * @return 获得当前加载的页数
     */
    public int getLoadingIdx() {
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
                List<HomeItemData> tempList = mImageDao.selectAll();

                for (HomeItemData data :
                        tempList) {
                    if (!HomeItemData.listContains(list, data)) {
                        //add to list
                        list.add(data);
                    }
                }
                finish.success(list);
                //insert to database
                 new InsertTask().execute(mList);
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                List<HomeItemData> tempList = mImageDao.selectAll();
                finish.failure(tempList);
            }
        });
    }

    public void loadImport(final OnGetImportFinish finish){
        mRetrofitUtils.getImportDataListFromInternet(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                List<HomeItemData> list = ((HomeItemDataList) response.body()).getImages();
                finish.success(list);
                //if data file format error list is null!
                if (list != null) {
                    new InsertTask().execute(list);
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                finish.failure();
            }
        });
    }

    /**
     * 异步添加数据到数据库
     */
    class InsertTask extends AsyncTask<List,Void,Void> {
        @Override
        protected Void doInBackground(List[] params) {
            List<HomeItemData> list=params[0];
            if (list.size() == mImageDao.selectAll().size()) {
                return null;
            }
            /**
             *存入数据库
             */
            for (int i = 0; i < list.size(); i++) {
                HomeItemData data = list.get(i);
                ImageBean bean=HomeData2ImageBean.home2bean(data);
                if (!mImageDao.isExist(bean)) {//如果不存在则添加
                    mImageDao.add(bean);
                }
            }
            return null;
        }
    }

}
