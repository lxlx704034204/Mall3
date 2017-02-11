//package com.hxqc.mall.auto.dao;
//
//import com.raizlabs.android.dbflow.annotation.Database;
//
///**
// * Author:胡仲俊
// * Date: 2016 - 04 - 13
// * FIXME
// * Todo 省市与城市的数据库初始化操作
// * <p>
// * pacDao ，每张表对于一个
// * <p>
// * 单例获取该Helper
// *
// * @return 获得autoDao
// * @return
// */
//@Database(name = PACDatabaseHelper.NAME, version = PACDatabaseHelper.VERSION)
//public class PACDatabaseHelper {
//    //数据库名称
//    public static final String NAME = "area";
//    //数据库版本号
//    public static final int VERSION = 2;
//}
///*public class PACDatabaseHelper extends OrmLiteSqliteOpenHelper {
//
//    private static final String TABLE_NAME = "area.db";
//    private static final int DB_VERSION = 2;
//    *//**
// * pacDao ，每张表对于一个
// *//*
//    private Dao<PAC, Integer> pacDao;
//
//    private PACDatabaseHelper(Context context) {
//        super(context, TABLE_NAME, null, DB_VERSION);
//    }
//
//    @Override
//    public void onCreate(SQLiteDatabase database,
//                         ConnectionSource connectionSource) {
//        try {
//            //创建表
//            TableUtils.createTable(connectionSource, PAC.class);
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
//            TableUtils.dropTable(connectionSource, PAC.class, true);
//            onCreate(database, connectionSource);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private static PACDatabaseHelper instance;
//
//    *//**
// * 单例获取该Helper
// *
// * @param context
// * @return
// *//*
//    public static synchronized PACDatabaseHelper getHelper(Context context) {
//        if (instance == null) {
//            synchronized (PACDatabaseHelper.class) {
//                if (instance == null)
//                    instance = new PACDatabaseHelper(context);
//            }
//        }
//
//        return instance;
//    }
//
//    *//**
// * 获得autoDao
// *
// * @return
// * @throws SQLException
// *//*
//    public Dao<PAC, Integer> getPACDao() throws SQLException {
//        if (pacDao == null) {
//            pacDao = getDao(PAC.class);
//        }
//        return pacDao;
//    }
//
//    */
//
///**
// * 释放资源
// *//*
//    @Override
//    public void close() {
//
//        if (pacDao != null) {
//            pacDao = null;
//        }
//        super.close();
//        if (instance != null) {
//            instance = null;
//        }
//    }
//}*/
//
//
