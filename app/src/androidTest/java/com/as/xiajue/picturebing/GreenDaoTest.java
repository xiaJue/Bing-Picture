package com.as.xiajue.picturebing;

import android.test.AndroidTestCase;

import com.as.xiajue.picturebing.model.database.ImageDao;
import com.as.xiajue.picturebing.model.database.bean.ImageBean;
import com.as.xiajue.picturebing.model.utils.L;
import com.as.xiajue.picturebing.model.utils.NetUtils;

import java.util.List;

/**
 * xiaJue 2017/9/12创建
 */
public class GreenDaoTest extends AndroidTestCase {

    public void testNet(){
        boolean b = NetUtils.isNetworkConnected(getContext());
        L.e("net state= "+b);
    }

    public void testAddGreenDao(){
        ImageDao dao = new ImageDao(getContext());
        ImageBean bean = new ImageBean("url","copyight-","enddata");
        dao.add(bean);//insert
    }
    public void testSelectGreenDao(){
        ImageDao dao = new ImageDao(getContext());

        List<ImageBean> imageBeanQueryBuilder =dao.selectAll();
        L.e("select all");
        for (ImageBean bean :
                imageBeanQueryBuilder) {
            L.e(bean.getUrl());
            L.e(bean.getEnddate()+"");
            L.e(bean.getCopyright());
            L.e("-------------------------------");
        }
    }
    public void testDelete(){
        ImageDao dao = new ImageDao(getContext());
        ImageBean bean = new ImageBean("url","copyight-","enddata");
        dao.delete();
    }
//
//    public void testUpdate(){
//        ImageDao dao = new ImageDao(getContext());
//        ImageBean bean = new ImageBean("url","copyight-",2017);
//        bean.setUrl("url--aaaaa");
//        dao.update(bean);
//    }
}
