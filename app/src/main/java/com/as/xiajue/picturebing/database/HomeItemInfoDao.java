package com.as.xiajue.picturebing.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.as.xiajue.picturebing.model.HomeItemData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiaJue on 2017/8/4.
 * 实现数据库的增删改查
 */

public class HomeItemInfoDao {
    private HomeItemInfoSQLiteHelper mHelper;

    public HomeItemInfoDao(Context context) {
        mHelper = HomeItemInfoSQLiteHelper.getInstance(context);
    }

    /**
     * 增加一条数据
     *
     * @param data HomeItemData数据
     */
    public void add(HomeItemData data) {
        SQLiteDatabase db = mHelper.getWritableDatabase();//得到一个写操作的数据库
        if (db.isOpen()) {
            ContentValues values = new ContentValues();
            values.put(mHelper.getKeyNames()[0], data.getUrl());//url
            values.put(mHelper.getKeyNames()[1], data.getCopyright());//copyright
            values.put(mHelper.getKeyNames()[2], data.getEnddate());//endDate
            db.insert(mHelper.getTableName(), null, values);
            db.close();
        }
    }

    /**
     * 删除一条数据
     *
     * @param data HomeItemData数据
     */
    public void delete(HomeItemData data) {
        SQLiteDatabase db = mHelper.getWritableDatabase();//得到一个写操作的数据库
        if (db.isOpen()) {
            db.delete(mHelper.getTableName(), mHelper.getKeyNames()[0] + "=?", new String[]{data
                    .getUrl()});
            db.close();
        }
    }

    /**
     * 修改一条数据
     *
     * @param data HomeItemData数据
     */
    public void update(HomeItemData data) {
        SQLiteDatabase db = mHelper.getWritableDatabase();//得到一个写操作的数据库
        if (db.isOpen()) {
            ContentValues values = new ContentValues();
            values.put(mHelper.getKeyNames()[0], data.getUrl());//url
            values.put(mHelper.getKeyNames()[1], data.getCopyright());//copyright
            values.put(mHelper.getKeyNames()[2], data.getEnddate());//endDate
            db.update(mHelper.getTableName(), values, mHelper.getKeyNames()[0] + "=?", new
                    String[]{data.getUrl()});
        }
    }

    /**
     * 查询所有数据
     *
     * @return List<HomeItemData>
     */
    public List selectAll() {
        List list = new ArrayList();
        SQLiteDatabase db = mHelper.getReadableDatabase();//得到一个写操作的数据库
        if (db.isOpen()) {
            Cursor query = db.query(mHelper.getTableName(), null, null, null,
                    null, null, null);
            while (query.moveToNext()) {
                HomeItemData data = new HomeItemData();
                data.setUrl(query.getString(query.getColumnIndex(mHelper.getKeyNames()[0])));
                data.setCopyright(query.getString(query.getColumnIndex(mHelper.getKeyNames()[1])));
                data.setEnddate(query.getString(query.getColumnIndex(mHelper.getKeyNames()[2])));
                list.add(data);
            }
        }
        return list;
    }

    /**
     * 查询某条数据是否存在
     *
     * @param data
     * @return
     */
    public boolean isExist(HomeItemData data) {
        SQLiteDatabase db = mHelper.getReadableDatabase();//得到一个写操作的数据库
        if (db.isOpen()) {
            Cursor query = db.query(mHelper.getTableName(), null, mHelper.getKeyNames()[0] +
                    "=?", new String[]{data.getUrl()}, null, null, null);
            return query.moveToFirst();
        }
        return false;
    }
}
