/**
 * author 胡俊杰
 * Todo
 */
package com.hxqc.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * author Administrator 从assets文件夹获取数据 // 初始化，只需要调用一次
 *         AssetsDatabaseManager.initManager(getApplication()); //
 *         获取管理对象，因为数据库需要通过管理对象才能够获取 AssetsDatabaseManager mg =
 *         AssetsDatabaseManager.getManager(); // 通过管理对象获取数据库 return
 *         mg.getDatabase(DBName);
 */
public class AssetsDatabaseManager {
    private static String tag = "AssetsDatabase";
    // %s    // is    // packageName    // Singleton Pattern
    private static AssetsDatabaseManager mInstance = null;
    // A mapping from assets database file to SQLiteDatabase object
    private Map< String, SQLiteDatabase > databases = new HashMap<>();
    // Context of application
    private Context context = null;

    public AssetsDatabaseManager(Context context) {
        this.context = context;
    }

    /**
     * Initialize AssetsDatabaseManager
     *
     * @param context
     *         , context of application
     */
    public static AssetsDatabaseManager initManager(Context context) {
        if (mInstance == null) {
            mInstance = new AssetsDatabaseManager(context);
        }

        return mInstance;
    }

    /**
     * Get a AssetsDatabaseManager object
     *
     * @return, if success return a AssetsDatabaseManager object, else
     * return null
     */
    public static AssetsDatabaseManager getManager() {
        return mInstance;
    }

    /**
     * Close all assets database
     */
    static public void closeAllDatabase() {
        if (mInstance != null) {
            for (int i = 0; i < mInstance.databases.size(); ++i) {
                if (mInstance.databases.get(i) != null) {
                    mInstance.databases.get(i).close();
                }
            }
            mInstance.databases.clear();
        }
    }

    /**
     * Get a assets database, if this database is opened this method
     * is only return a copy of the opened database
     *
     * @param dbfile
     *         , the assets file which will be opened for a
     *         database
     * @return, if success it return a SQLiteDatabase object else
     * return null
     */
    public SQLiteDatabase getDatabase(String dbfile) {
        if (databases.get(dbfile) != null) {
            return databases.get(dbfile);
        }
        if (context == null) {
            return null;
        }

        String spath = getDatabaseFilepath();
        String sfile = getDatabaseFile(dbfile);

        File file = new File(sfile);
        SharedPreferences dbs = context.getSharedPreferences(AssetsDatabaseManager.class.toString(), 0);
        boolean flag = dbs.getBoolean(dbfile, false);
        // Get Database
        // file flag,
        // if true
        // means this
        // database
        // file was
        // copied and
        // valid
        if (!flag || !file.exists()) {
            file = new File(spath);
            if (!file.exists() && !file.mkdirs()) {
                return null;
            }
            if (!copyAssetsToFilesystem(dbfile, sfile)) {
                return null;
            }

            dbs.edit().putBoolean(dbfile, true).apply();
        }

        // SQLiteDatabase db = SQLiteDatabase.openDatabase(sfile,
        // null,
        // SQLiteDatabase.NO_LOCALIZED_COLLATORS);
        SQLiteDatabase db = context.openOrCreateDatabase(sfile, Context.MODE_PRIVATE, null);
        if (db != null) {
            databases.put(dbfile, db);
        }
        return db;
    }

    public boolean copyDatabase(String dbfile) {
//        if (databases.get(dbfile) != null) {
//            return false;
//        }
        if (context == null) {
            return false;
        }

        String spath = getDatabaseFilepath();
        String sfile = getDatabaseFile(dbfile);

        File file = new File(sfile);
        SharedPreferences dbs = context.getSharedPreferences(AssetsDatabaseManager.class.toString(), 0);
        boolean flag = dbs.getBoolean(dbfile, false);

        if (!flag || !file.exists()) {
            file = new File(spath);
            if (!file.exists() && !file.mkdirs()) {
                return false;
            }
            if (!copyAssetsToFilesystem(dbfile, sfile)) {
                return false;
            }

            dbs.edit().putBoolean(dbfile, true).apply();
        }

        return true;
    }

    private String getDatabaseFilepath() {
        @SuppressLint("SdCardPath") String databasepath = "/data/data/%s/databases";
        return String.format(databasepath, context.getApplicationInfo().packageName);
    }

    private String getDatabaseFile(String dbfile) {
        return getDatabaseFilepath() + "/" + dbfile;
    }

    private boolean copyAssetsToFilesystem(String assetsSrc, String des) {
        InputStream istream = null;
        OutputStream ostream = null;
        try {
            AssetManager am = context.getAssets();
            istream = am.open(assetsSrc);
            ostream = new FileOutputStream(des);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = istream.read(buffer)) > 0) {
                ostream.write(buffer, 0, length);
            }
            istream.close();
            ostream.close();
        } catch (Exception e) {
            e.printStackTrace();
            try {
                if (istream != null) {
                    istream.close();
                }
                if (ostream != null) {
                    ostream.close();
                }
            } catch (Exception ee) {
                ee.printStackTrace();
            }
            return false;
        }
        return true;
    }

    /**
     * Close assets database
     *
     * @param dbfile
     *         , the assets file which will be closed soon
     * @return, the status of this operating
     */
    public boolean closeDatabase(String dbfile) {
        if (databases.get(dbfile) != null) {
            SQLiteDatabase db = databases.get(dbfile);
            db.close();
            databases.remove(dbfile);
            return true;
        }
        return false;
    }
}
