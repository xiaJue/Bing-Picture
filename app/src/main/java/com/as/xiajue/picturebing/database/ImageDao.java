package com.as.xiajue.picturebing.database;

import android.content.Context;

import com.as.xiajue.picturebing.bean.HomeData2ImageBean;
import com.as.xiajue.picturebing.bean.ImageBean;
import com.as.xiajue.picturebing.database.genUtils.DaoMaster;
import com.as.xiajue.picturebing.database.genUtils.DaoSession;
import com.as.xiajue.picturebing.database.genUtils.ImageBeanDao;

import java.util.ArrayList;
import java.util.List;

/**
 * xiaJue 2017/9/12创建
 */
public class ImageDao {

    private ImageBeanDao mImageBeanDao;

    public ImageDao(Context context) {
        //初始化 GreenDao
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, "m_images.db", null);
        DaoMaster master = new DaoMaster(helper.getWritableDb());
        DaoSession session = master.newSession();
        mImageBeanDao = session.getImageBeanDao();
    }

    /**
     * 增加一条数据
     *
     * @param bean ImageBean
     */
    public void add(ImageBean bean) {
        mImageBeanDao.insert(bean);
    }

    /**
     * 删除一条数据
     *
     * @param bean ImageBean
     */
    public void delete(ImageBean bean) {
        mImageBeanDao.delete(bean);
    }

    /**
     * 删除所有数据
     */
    public void delete() {
        mImageBeanDao.deleteAll();
    }

    /**
     * 修改一条数据
     *
     * @param bean ImageBean
     */
    public void update(ImageBean bean) {
        mImageBeanDao.update(bean);
    }

    /**
     * 查询所有数据
     *
     * @return List<ImageBean>
     */
    public List selectAll() {
        List list = new ArrayList();
        List<ImageBean> tempList = mImageBeanDao.queryBuilder()./*orderDesc(ImageBeanDao.Properties.Enddate).*/
        build().list();
        for (ImageBean bean :
                tempList) {
            list.add(HomeData2ImageBean.bean2home(bean));
        }
        return list;
    }

    /**
     * 查询某条数据是否存在
     *
     * @param bean ImageBean
     * @return true or false
     */
    public boolean isExist(ImageBean bean) {
        List<ImageBean> list = mImageBeanDao.queryBuilder().where(ImageBeanDao.Properties.Enddate.
                eq(bean.getEnddate())).build().list();
        return list.size() > 0;
    }
}
