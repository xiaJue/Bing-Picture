package com.as.xiajue.picturebing.model.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by xiaJue on 2017/8/4.
 */

public class HomeItemInfoSQLiteHelper extends SQLiteOpenHelper {
    private final static int VERSION = 1;
    /**
     * 数据库名称
     */
    private static final String NAME = "itemInfo.db";
    /**
     * 数据库表名
     */
    private String mTableName = "homeItem";

    /**
     * 获得数据库表名
     *
     * @return
     */
    public String getTableName() {
        return mTableName;
    }

    /**
     * 所有字段的名称
     */
    private String[] keyNames = {"_url", "_copyright", "_enddate"};

    /**
     * 获得所有字段的名称
     *
     * @return String[] {"_url", "_copyright", "_enddate"};
     */
    public String[] getKeyNames() {
        return keyNames;
    }

    /**
     * 创建数据库表的sql语句
     */
    private String sqlCreateTableString = "create table " + mTableName + " (" + keyNames[0] +
            " text," + keyNames[1] +
            " text," + keyNames[2] + " text)";
    /**
     * 实例
     */
    private static HomeItemInfoSQLiteHelper helper;

    /**
     * 获得一个SQLiteHelper 的实例
     *
     * @param context
     * @return SQLiteHelper 的实例
     */
    public static HomeItemInfoSQLiteHelper getInstance(Context context) {
        if (helper == null) {
            synchronized (HomeItemInfoSQLiteHelper.class) {
                if (helper == null) {
                    helper = new HomeItemInfoSQLiteHelper(context, NAME, null, VERSION);
                }
            }
        }
        return helper;
    }

    private HomeItemInfoSQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory
            factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(sqlCreateTableString);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
