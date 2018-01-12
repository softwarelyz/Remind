package com.gdnf.lazy.tips.dao;

import android.content.Context;

import com.gdnf.lazy.tips.entity.Tips;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by admin on 2017/12/15.
 */

public class TipsDao {

    private DBHelper dbHelper;
    private Dao dao;
    public TipsDao(Context context){
        dbHelper = new DBHelper(context);
        try {
            dao = dbHelper.getDao(Tips.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //添加一个提醒
    public boolean addTips(Tips tips) throws SQLException {
        dao.create(tips);
        return true;
    }

    //删除所有提醒
    public boolean deleteTips() throws SQLException {
        dao.delete(dao.queryForAll());
        return true;
    }

    //查询提醒
    public List<Tips> getTips() throws SQLException {

       return dbHelper.getDao(Tips.class).queryForAll();

    }

    //
}
