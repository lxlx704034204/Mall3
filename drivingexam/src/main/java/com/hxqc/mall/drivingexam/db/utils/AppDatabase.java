package com.hxqc.mall.drivingexam.db.utils;

import com.raizlabs.android.dbflow.annotation.Database;

/**
 * 数据库定义类
 * Created zhaofan
 */
@Database(name = AppDatabase.NAME, version = AppDatabase.VERSION)
public class AppDatabase {
    //数据库名称
    public static final String NAME = "ExamDateBase";
    //数据库版本号
    public static final int VERSION = 1;

}
