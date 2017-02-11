//package com.hxqc.mall.core.dao;
//
//import android.content.Context;
//import android.database.sqlite.SQLiteDatabase;
//import android.util.Log;
//
//import com.hxqc.mall.core.areadb.CoreDatabaseHelper;
//import com.hxqc.mall.core.model.auto.AutoBaseInformation;
//import com.hxqc.util.DebugLog;
//import com.j256.ormlite.dao.RuntimeExceptionDao;
//import com.j256.ormlite.support.ConnectionSource;
//import com.j256.ormlite.table.TableUtils;
//
//import java.sql.SQLException;
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;
//
///**
// * Created by CPR011 on 2015/2/2.
// */
//public class AutoSQLDataHelper extends CoreDatabaseHelper {
//
//    public AutoSQLDataHelper(Context context) {
//        super(context);
//    }
//
//    /**
//     * 创建SQLite数据库
//     */
//    @Override
//    public void onCreate(SQLiteDatabase sqliteDatabase, ConnectionSource connectionSource) {
//        try {
//            TableUtils.createTable(connectionSource, AutoBaseInformation.class);
//        } catch (SQLException e) {
//            DebugLog.e("SQL", "Unable to create databases" + e.toString());
//        }
//    }
//
//    /**
//     * 更新SQLite数据库
//     */
//    @Override
//    public void onUpgrade(SQLiteDatabase sqliteDatabase, ConnectionSource connectionSource,
//                          int oldVer, int newVer) {
//        try {
//            TableUtils.dropTable(connectionSource, AutoBaseInformation.class, true);
//            onCreate(sqliteDatabase, connectionSource);
//        } catch (SQLException e) {
//            DebugLog.e(AutoSQLDataHelper.class.getName(),
//                    "Unable to upgrade database from version " + oldVer + " to new "
//                            + newVer + e);
//        }
//    }
//
//    private RuntimeExceptionDao< AutoBaseInformation, String > mAutoBaseInformationDao = null;
//
//    public RuntimeExceptionDao< AutoBaseInformation, String > getAutoBaseInformationDao() {
//        if (mAutoBaseInformationDao == null) {
//            mAutoBaseInformationDao = getRuntimeExceptionDao(AutoBaseInformation.class);
//        }
//        return mAutoBaseInformationDao;
//    }
//
//    /**
//     * 保存历史浏览记录
//     *
//     * @param autoBaseInformation
//     */
//    public void insertAutoHistory(AutoBaseInformation autoBaseInformation) {
//        if (autoBaseInformation == null) return;
//        RuntimeExceptionDao< AutoBaseInformation, String > userDao = getAutoBaseInformationDao();
//        userDao.createIfNotExists(autoBaseInformation);
//    }
//
//    public ArrayList< AutoBaseInformation > findAutoHistory() {
//        RuntimeExceptionDao< AutoBaseInformation, String > dao = getAutoBaseInformationDao();
//        List< AutoBaseInformation > mList = dao.queryForAll();
//        Collections.reverse(mList);
//        return (ArrayList< AutoBaseInformation >) mList;
//    }
//
//    public void clearAutoHistory() {
//        try {
//            TableUtils.clearTable(connectionSource, AutoBaseInformation.class);
//        } catch (SQLException e) {
//            Log.e(AutoSQLDataHelper.class.getName(),
//                    e.toString());
//        }
//    }
//}
