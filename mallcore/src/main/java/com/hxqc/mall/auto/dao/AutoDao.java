//package com.hxqc.mall.auto.dao;
//
//import android.content.Context;
//
//import com.hxqc.mall.auto.model.MyAuto;
//import com.hxqc.util.DebugLog;
//import com.j256.ormlite.stmt.DeleteBuilder;
//import com.j256.ormlite.stmt.QueryBuilder;
//
//import java.sql.SQLException;
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * Author:胡仲俊
// * Date: 2016 - 04 - 13
// * FIXME
// * Todo 我的数据库操作
// */
//@Deprecated
//public class AutoDao {
//
//    private static final String TAG = "AutoDao";
//
//    private static AutoDao imInstance;
//
//    public AutoDao() {
//    }
//
//    /**
//     * 单例获取该Helper
//     *
//     * @return
//     */
//    public static synchronized AutoDao getInstance() {
//        if (imInstance == null) {
//            synchronized (AutoDatabaseHelper.class) {
//                if (imInstance == null)
//                    imInstance = new AutoDao();
//            }
//        }
//
//        return imInstance;
//    }
//
//    /**
//     * 添加车辆
//     *
//     * @param myAuto
//     */
//    public void add(Context context, MyAuto myAuto) {
//        try {
//            DebugLog.i(TAG, myAuto.toString());
//            AutoDatabaseHelper.getHelper(context).getAutoDao().create(myAuto);
//        } catch (SQLException e) {
//            DebugLog.e(TAG, e.toString());
//        }
//    }
//
//    /**
//     * 删除车辆
//     *
//     * @param myAuto
//     */
//    public void delete(Context context, MyAuto myAuto) {
//        try {
//            AutoDatabaseHelper.getHelper(context).getAutoDao().delete(myAuto);
//        } catch (SQLException e) {
//            DebugLog.e(TAG, e.toString());
//        }
//    }
//
//    /**
//     * 删除车辆
//     *
//     * @param plateNumber
//     */
//    public void deleteByPlateNumber(Context context, String plateNumber) {
//        try {
//            List<MyAuto> myAutos = queryByPlateNumber(context, plateNumber);
//            if (myAutos != null && !myAutos.isEmpty()) {
//                for (int i = 0; i < myAutos.size(); i++) {
//                    DeleteBuilder<MyAuto, Integer> myAutoIntegerDeleteBuilder = AutoDatabaseHelper.getHelper(context).getAutoDao().deleteBuilder();
//                    myAutoIntegerDeleteBuilder.where().eq("plateNumber", plateNumber);
//                    myAutoIntegerDeleteBuilder.delete();
//                }
//            }
//        } catch (SQLException e) {
//            DebugLog.e(TAG, e.toString());
//        }
//    }
//
//    /**
//     * 更新车辆
//     *
//     * @param myAuto
//     */
//    public void update(Context context, MyAuto myAuto) {
//        try {
//            AutoDatabaseHelper.getHelper(context).getAutoDao().createOrUpdate(myAuto);
//        } catch (SQLException e) {
//            DebugLog.e(TAG, e.toString());
//        }
//    }
//
//    /**
//     * 清空数据库车辆
//     */
//    public void cleanAll(Context context) {
//        try {
//            List<MyAuto> myAutos = queryForAll(context);
//            for (int i = 0; i < myAutos.size(); i++) {
//                AutoDatabaseHelper.getHelper(context).getAutoDao().update(myAutos.get(i));
//            }
//        } catch (SQLException e) {
//            DebugLog.e(TAG, e.toString());
//        }
//    }
//
//    /**
//     * @param myAuto
//     * @return
//     */
//    public List<MyAuto> queryMyAuto(Context context, MyAuto myAuto) {
//        List<MyAuto> myAutos = null;
//        try {
//            myAutos = AutoDatabaseHelper.getHelper(context).getAutoDao().queryForMatching(myAuto);
//            return myAutos;
//        } catch (SQLException e) {
//            e.printStackTrace();
//            DebugLog.e(TAG, e.toString());
//        }
//        return myAutos;
//    }
//
//    /**
//     * 获得数据库所有车辆
//     *
//     * @return List<MyAuto>
//     */
//    public List<MyAuto> queryForAll(Context context) {
//        try {
//            List<MyAuto> sqliteMyAutos = AutoDatabaseHelper.getHelper(context).getAutoDao().queryForAll();
//            return sqliteMyAutos;
//        } catch (SQLException e) {
//            DebugLog.e(TAG, e.toString());
//        }
//        return null;
//    }
//
//    /**
//     * @return ArrayList<MyAuto>
//     */
//    public ArrayList<MyAuto> queryForAllA(Context context) {
//        try {
//            ArrayList<MyAuto> myAutos = new ArrayList<MyAuto>();
//            List<MyAuto> sqliteMyAutos = AutoDatabaseHelper.getHelper(context).getAutoDao().queryForAll();
//            for (int i = 0; i < sqliteMyAutos.size(); i++) {
//                myAutos.add(sqliteMyAutos.get(i));
//            }
//            return myAutos;
//        } catch (SQLException e) {
//            DebugLog.e(TAG, e.toString());
//        }
//        return null;
//    }
//
//    public List<MyAuto> queryByPlateNumber(Context context, String plateNumber) {
//        List<MyAuto> myAutos = null;
//        try {
//            myAutos = AutoDatabaseHelper.getHelper(context).getAutoDao().queryForEq("plateNumber", plateNumber);
//            return myAutos;
//        } catch (SQLException e) {
//            DebugLog.e(TAG, e.toString());
//            e.printStackTrace();
//        }
//        return myAutos;
//    }
//
//    /**
//     * 通过brandID查询
//     *
//     * @param brandID
//     * @return
//     */
//    public MyAuto queryByBrandIDForFirst(Context context, String brandID) {
//        MyAuto myAuto = null;
//        try {
//            QueryBuilder<MyAuto, Integer> myAutoIntegerQueryBuilder = AutoDatabaseHelper.getHelper(context).getAutoDao().queryBuilder();
//            myAutoIntegerQueryBuilder.where().eq("brandID", brandID);
//            myAuto = myAutoIntegerQueryBuilder.queryForFirst();
//            if (myAuto != null) {
//                DebugLog.i(TAG, "sqlite:" + myAuto.toString());
//                return myAuto;
//            } else {
//                DebugLog.i(TAG, "无数据");
//                return myAuto;
//            }
//        } catch (SQLException e) {
//            DebugLog.e(TAG, e.toString());
//        }
//        return myAuto;
//    }
//
//
//    /**
//     * @param brandID
//     * @return
//     */
//    public boolean queryByBrandID(Context context, String brandID) {
//        List<MyAuto> myAutos = queryForAll(context);
//        for (int i = 0; i < myAutos.size(); i++) {
//            if (brandID.equals(myAutos.get(i).brandID)) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//    /**
//     * 清空
//     */
//    public void killInstance(Context context) {
//        if (imInstance != null) {
//            AutoDatabaseHelper.getHelper(context).close();
//            imInstance = null;
//        }
//    }
//}
