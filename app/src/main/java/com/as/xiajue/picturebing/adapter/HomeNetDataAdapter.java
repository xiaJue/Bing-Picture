package com.as.xiajue.picturebing.adapter;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;

import com.as.xiajue.picturebing.Const;
import com.as.xiajue.picturebing.NetUtils.HttpUtils;
import com.as.xiajue.picturebing.NetUtils.JsonUtils;
import com.as.xiajue.picturebing.database.HomeItemInfoDao;
import com.as.xiajue.picturebing.dialog.DialogManager;
import com.as.xiajue.picturebing.model.HomeItemData;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.as.xiajue.picturebing.model.HomeItemData.listContains;

/**
 * Created by xiaJue on 2017/7/31.
 * 控制器：http和view的一个适配
 * 涵盖了网络操作，列表刷新，数据持久化等操作
 */

public class HomeNetDataAdapter {
    private HttpUtils mHttpUtils;//okHttp封装的操作类
    private int mLength = Const.LOAD_JSON_COUNT;//一次获取的json数据长度、最大为8
    private HomeAdapter mAdapter;
    private Context mContext;
    private HomeItemInfoDao mInfoDao;
    private boolean mIsSqlLoad = false;

    public HomeNetDataAdapter(Context context, List list, HomeAdapter
            adapter) {
        this.mHttpUtils = HttpUtils.getInstance(mContext);
        this.mContext = context;
        this.mList = list;
        this.mAdapter = adapter;
        mInfoDao = new HomeItemInfoDao(context);
    }

    private static final int MSG_INVALI = 65;
    private static final int MSG_NET_FAILURE = 66;
    /**
     * Handler消息机制更新内容
     */
    private Handler handler;

    {
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case MSG_INVALI:
                        //更新recycleView adapter
                        mIsSqlLoad = false;
                        invalidate();
                        break;
                    case MSG_NET_FAILURE:
                        //如果从数据库中获取的数据长度大于1则更新界面
//                        Toast.makeText(mContext, "啊哦...连接网络失败了", Toast.LENGTH_SHORT).show();
                        //显示一个网络连接错误的dialog
                        DialogManager.showInternetErrorDialog(mContext);
                        mIsSqlLoad = true;
                        invalidate();
                        break;
                }
            }
        };
    }


    /**
     * 获得需要的数据的url‘即根据服务器端的JSON获取格式拼装出url
     *
     * @param idx 页数
     * @param n   长度
     * @return Url String
     */
    private String getUrl(int idx, int n) {
        String mUrl = Const.URL_ADDRESS;
        return mUrl + idx + "&n=" + n;
    }

    /**
     * 维护一个recycleView的adapter的数据列表
     */
    private List<HomeItemData> mList;//数据列表

    /**
     * 将获取的json数据解析为HomeItemData模型数据
     *
     * @param idx 获取第几页、从0开始
     */
    public void requestJsonData(int idx) {
        mHttpUtils.getJsonFromUrl(getUrl(idx * mLength, mLength), new Callback() {
            /**
             * 当获取失败
             * @param call
             * @param e
             */
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                //访问网络失败时，尝试从数据库中获取数据
                fromDatabaseLoad();
                //通知handler更新数据
                handler.sendEmptyMessage(MSG_NET_FAILURE);
            }

            /**
             * 当获取成功
             * @param call
             * @param response
             * @throws IOException
             */
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws
                    IOException {
                String jsonStr = response.body().string();//json数据
                JsonUtils.getJsonData(mList, jsonStr);//解析json数据为HomeItemData模型数据
                //尝试从数据库获取数据
                fromDatabaseLoad();
                handler.sendEmptyMessage(MSG_INVALI);//通知handler更新
            }
        });
    }

    /**
     * 从数据库获取数据
     */
    private void fromDatabaseLoad() {
        List<HomeItemData> tempList = mInfoDao.selectAll();
        //如果不存在再添加
        for (HomeItemData data :
                tempList) {
            if (!listContains(mList, data)) {
                mList.add(data);
            }
        }
    }

    private void sortList(List<HomeItemData> tempList) {
        /**
         * 对元素进行升序排序
         */
        Collections.sort(tempList, new Comparator<HomeItemData>() {
            @Override
            public int compare(HomeItemData o1, HomeItemData o2) {
                return Integer.valueOf(o2.getEnddate()).compareTo(Integer.valueOf(o1.getEnddate()));
            }
        });
    }

    /**
     * 上次获取的list item个数，用于判断是否发生改变
     */
    private int mOldListSize;

    /**
     * 添加条目到recycleView
     */
    private void invalidate() {
        //如果list发生改变再更新
        if (mOldListSize != mList.size()) {
            sortList(mList);//排序
            mAdapter.notifyDataSetChanged();
            mOldListSize = mList.size();
            addToDatabase();//添加到数据库
        }
        refreshLayout.finishRefreshing();
        refreshLayout.finishLoadmore();
    }

    public void addToDatabase() {
        if (!mIsSqlLoad) {
            new PutSqlInfoTask().execute();//开始存储数据
        }
    }

    /**
     * 添加数据到数据库
     */
    class PutSqlInfoTask extends AsyncTask {

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

    /**
     * 当前的加载页数
     *
     * @return 获得当前加载的页数
     */
    public int getLoadingIdx() {
        return mList.size() / Const.LOAD_JSON_COUNT;
    }

    private TwinklingRefreshLayout refreshLayout;

    /**
     * 绑定刷新组件
     *
     * @param refreshLayout 刷新组件
     */
    public void setRefreshLayout(TwinklingRefreshLayout refreshLayout) {
        this.refreshLayout = refreshLayout;
    }
}
