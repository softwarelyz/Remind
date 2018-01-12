package com.gdnf.lazy.tips.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.gdnf.lazy.tips.entity.Tips;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

/**
 * Created by admin on 2017/12/15.
 */

public class DBHelper extends OrmLiteSqliteOpenHelper {


    private static final String DATABASE_NAME="myTips.db";
    private static final int VERSION=39;
    public DBHelper(Context context) {
        super(context,DATABASE_NAME, null, VERSION);
    }

    //第一次执行数据库时
    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {

        try {
            TableUtils.createTableIfNotExists(connectionSource,Tips.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //加入测试数据
        try {
            Dao dao = getDao(Tips.class);
            //dao.create(new Tips(0,"haha","09:30"));
            //dao.create(new Tips(1,"haha","09:30"));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Log.i("create Table","创建表成功");

    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {

        try {
            TableUtils.dropTable(connectionSource,Tips.class,true);
            onCreate(database);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
