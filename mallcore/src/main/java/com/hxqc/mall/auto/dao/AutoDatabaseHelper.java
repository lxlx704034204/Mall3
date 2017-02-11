//package com.hxqc.mall.auto.dao;
//
//import android.content.Context;
//import android.database.sqlite.SQLiteDatabase;
//
//import com.hxqc.mall.auto.model.MyAuto;
//import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
//import com.j256.ormlite.dao.Dao;
//import com.j256.ormlite.support.ConnectionSource;
//import com.j256.ormlite.table.TableUtils;
//
//import java.sql.SQLException;
//
///**
// * Author:胡仲俊
// * Date: 2016 - 04 - 13
// * FIXME
// * Todo 我的车辆的数据库初始化
// */
//@Deprecated
//public class AutoDatabaseHelper extends OrmLiteSqliteOpenHelper {
//    private static final String TABLE_NAME = "my_auto.db";
//    /**
//     * autoDao ，每张表对于一个
//     */
//    private Dao<MyAuto, Integer> autoDao;
//
//    private AutoDatabaseHelper(Context context) {
//        super(context, TABLE_NAME, null, 1);
//    }
//
//    @Override
//    public void onCreate(SQLiteDatabase database,
//                         ConnectionSource connectionSource) {
//        try {
//            //创建表
//            TableUtils.createTable(connectionSource, MyAuto.class);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//
//    //更新表
//    @Override
//    public void onUpgrade(SQLiteDatabase database,
//                          ConnectionSource connectionSource, int oldVersion, int newVersion) {
//        try {
//            //删除表
//            TableUtils.dropTable(connectionSource, MyAuto.class, true);
//            onCreate(database, connectionSource);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private static AutoDatabaseHelper instance;
//
//    /**
//     * 单例获取该Helper
//     *
//     * @param context
//     * @return
//     */
//    public static synchronized AutoDatabaseHelper getHelper(Context context) {
//        if (instance == null) {
//            synchronized (AutoDatabaseHelper.class) {
//                if (instance == null)
//                    instance = new AutoDatabaseHelper(context.getApplicationContext());
//            }
//        }
//
//        return instance;
//    }
//
//    /**
//     * 获得autoDao
//     *
//     * @return
//     * @throws SQLException
//     */
//    public Dao<MyAuto, Integer> getAutoDao() throws SQLException {
//        if (autoDao == null) {
//            autoDao = getDao(MyAuto.class);
//        }
//        return autoDao;
//    }
//
//    /**
//     * 释放资源
//     */
//    @Override
//    public void close() {
//        super.close();
//        if(autoDao != null) {
//            autoDao = null;
//        }
//    }
//
//}
