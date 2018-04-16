package com.as.xiajue.picturebing.ui.home;

import android.content.Context;

import com.as.xiajue.picturebing.Const;
import com.as.xiajue.picturebing.base.BaseObserver;
import com.as.xiajue.picturebing.base.BasePresenter;
import com.as.xiajue.picturebing.bean.HomeData2ImageBean;
import com.as.xiajue.picturebing.bean.HomeItemData;
import com.as.xiajue.picturebing.bean.HomeItemDataList;
import com.as.xiajue.picturebing.bean.ImageBean;
import com.as.xiajue.picturebing.database.ImageDao;
import com.as.xiajue.picturebing.net.RetrofitFactory;
import com.as.xiajue.picturebing.net.home.HomeApiServer;
import com.as.xiajue.picturebing.ui.custom.RxSchedulers;

import java.util.List;

import retrofit2.Response;

/**
 * xiaJue 2018/4/8创建
 */
public class HomePresenter extends BasePresenter<HomeContract.View> implements HomeContract
        .Presenter {
    private ImageDao mImageDao;

    public HomePresenter(Context context) {
        mImageDao = new ImageDao(context);
    }

    @Override
    public void loadData(int idx) {
        RetrofitFactory.create(HomeApiServer.class).getData(idx * Const.LOAD_JSON_COUNT, Const
                .LOAD_JSON_COUNT).compose(mView.<Response<HomeItemDataList>>bindToLife()).compose
                (RxSchedulers.<Response<HomeItemDataList>>applySchedulers()).subscribe(new BaseObserver<HomeItemDataList>() {
            @Override
            public void onFailure(Throwable e, boolean isNetWorkError) {
                loadFlag = false;
                mView.onLoadError(e);
                mView.setUiData(put2db(null));
            }

            @Override
            public void onSuccess(Response<HomeItemDataList> result) {
                //将数据存入数据库
                List<HomeItemData> datas = put2db(result);
                loadFlag = true;
                mView.setUiData(datas);
            }
        });
    }

    private List put2db(Response<HomeItemDataList> result) {
        if (result != null && result.body() != null && result.body()
                .getImages().size() > 0) {
            //成功获取了数据则存入
            for (HomeItemData data :
                    result.body().getImages()) {
                ImageBean imageBean = HomeData2ImageBean.home2bean(data);
                if (!mImageDao.isExist(imageBean)) {
                    mImageDao.add(imageBean);
                }
            }
        }
        //数据取出
        return mImageDao.selectAll();
    }

    @Override
    public void refresh() {
        loadData(0);
    }

    private boolean loadFlag = false;

    @Override
    public boolean isLoad() {
        return loadFlag;
    }
}
