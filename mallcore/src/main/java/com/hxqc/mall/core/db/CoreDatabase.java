package com.hxqc.mall.core.db;

import com.raizlabs.android.dbflow.annotation.Database;

/**
 * 核心数据库
 * Created 胡俊杰
 */
@Database(name = CoreDatabase.NAME, version = CoreDatabase.VERSION)
public class CoreDatabase {
    //数据库名称
    public static final String NAME = "Core";
    //数据库版本号
    public static final int VERSION = 2;
//
//    @Migration(version = 2, database = CoreDatabase.class)
//    public static class Migration extends BaseMigration {
//
//        @Override
//        public void migrate(DatabaseWrapper database) {
//            Log.i("Tag", "mirgrate------");
//            // run some code here
//            SQLite.update(RouteModule.class); // required inside a migration to pass the wrapper
//            SQLite.update(ChooseCarModel.class);
//        }
//    }
}
