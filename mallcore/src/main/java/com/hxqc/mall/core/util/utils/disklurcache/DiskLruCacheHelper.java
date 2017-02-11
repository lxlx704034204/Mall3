package com.hxqc.mall.core.util.utils.disklurcache;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Log;

import com.jakewharton.disklrucache.DiskLruCache;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;


/**
 * DiskLruCache接口类
 * Created by zhaofan.
 */
public class DiskLruCacheHelper {
    public static final String DIR_NAME = "diskCache";
    public static final int MAX_COUNT = 30 * 1024 * 1024;
    public static final int DEFAULT_APP_VERSION = 1;
    public static final int DEFAULT_VALUE_COUNT = 1;
    private static final String TAG = "DiskLruCacheHelper";

    private DiskLruCache mDiskLruCache;
    private static volatile DiskLruCacheHelper mInstance = null;

    public static DiskLruCacheHelper builder(Context context, Object dir) {
        if (mInstance == null)
            synchronized (DiskLruCacheHelper.class) {
                if (mInstance == null) {
                    try {
                        if (dir instanceof String)
                            mInstance = new DiskLruCacheHelper(context, (String) dir);
                        else if (dir instanceof File)
                            mInstance = new DiskLruCacheHelper(context, (File) dir);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        return mInstance;
    }

    /**
     * 默认单例
     *
     * @param context
     * @return
     */
    public static DiskLruCacheHelper builder(Context context) {
        return builder(context, DIR_NAME);
    }

    public DiskLruCacheHelper(Context context) throws IOException {
        mDiskLruCache = defDirCache(context, DIR_NAME, MAX_COUNT);
    }

    public DiskLruCacheHelper(Context context, String dirName) throws IOException {
        dirName = TextUtils.isEmpty(dirName) ? DIR_NAME : dirName;
        mDiskLruCache = defDirCache(context, dirName, MAX_COUNT);
    }

    public DiskLruCacheHelper(Context context, String dirName, int maxCount) throws IOException {
        mDiskLruCache = defDirCache(context, dirName, maxCount);
    }

    public DiskLruCacheHelper(File dir) throws IOException {
        mDiskLruCache = customDirCache(null, dir, MAX_COUNT);
    }

    public DiskLruCacheHelper(Context context, File dir) throws IOException {
        mDiskLruCache = customDirCache(context, dir, MAX_COUNT);
    }

    public DiskLruCacheHelper(Context context, File dir, int maxCount) throws IOException {
        mDiskLruCache = customDirCache(context, dir, maxCount);
    }

    //自定义文件路径
    private DiskLruCache customDirCache(Context context, File dir, int maxCount) throws IOException {
        if (!dir.exists() || !dir.isDirectory()) {
            throw new IllegalArgumentException(
                    dir + " is not a directory or does not exists. ");
        }
        // int appVersion = context == null ? DEFAULT_APP_VERSION : Util.getAppVersion(context);
        DiskLruCache diskLruCache = DiskLruCache.open(
                dir,
                DEFAULT_APP_VERSION,
                DEFAULT_VALUE_COUNT,
                maxCount);
        return diskLruCache;
    }

    //默认文件路径 CacheDir
    private DiskLruCache defDirCache(Context context, String dirName, int maxCount) throws IOException {
        DiskLruCache diskLruCache = DiskLruCache.open(
                Util.getDiskCacheDir(context, dirName),
                DEFAULT_APP_VERSION,
                DEFAULT_VALUE_COUNT,
                maxCount);
        return diskLruCache;
    }


    // =======================================
    // ============== String 数据 读写 =============
    // =======================================

    public void put(String key, String value) {
        DiskLruCache.Editor edit = null;
        BufferedWriter bw = null;
        try {
            edit = editor(key);
            if (edit == null) return;
            OutputStream os = edit.newOutputStream(0);
            bw = new BufferedWriter(new OutputStreamWriter(os));
            bw.write(value);
            edit.commit();//write CLEAN
        } catch (IOException e) {
            e.printStackTrace();
            try {
                //s
                edit.abort();//write REMOVE
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } finally {
            try {
                if (bw != null)
                    bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @param timeLimit 过期时间  秒
     */
    public void put(String key, String value, int timeLimit) {
        put(key, CacheValidManager.StringWithTimeInfo(timeLimit, value));
    }

    public String getAsString(String key) {
        String str = "";
        InputStream inputStream = null;
        inputStream = get(key);
        if (inputStream == null)
            return str;
        try {
            str = Util.readFully(new InputStreamReader(inputStream, Util.UTF_8));
            String value = CacheValidManager.isDue(str);
            if (TextUtils.isEmpty(value))
                remove(key);
            return value;
        } catch (IOException e) {
            e.printStackTrace();
            try {
                inputStream.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        return "";
    }


    // =======================================
    // ============ JSONObject 数据 读写 =============
    // =======================================

    public void put(String key, JSONObject jsonObject) {
        put(key, jsonObject.toString());
    }

    /**
     * @param timeLimit 过期时间  秒
     */
    public void put(String key, JSONObject jsonObject, int timeLimit) {
        put(key, CacheValidManager.StringWithTimeInfo(timeLimit, jsonObject.toString()));
    }

    public JSONObject getAsJson(String key) {
        String val = getAsString(key);
        try {
            if (val != null)
                return new JSONObject(val);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    // =======================================
    // ============ JSONArray 数据 读写 =============
    // =======================================

    public void put(String key, JSONArray jsonArray) {
        put(key, jsonArray.toString());
    }

    /**
     * @param timeLimit 过期时间  秒
     */
    public void put(String key, JSONArray jsonArray, int timeLimit) {
        put(key, CacheValidManager.StringWithTimeInfo(timeLimit, jsonArray.toString()));
    }


    public JSONArray getAsJSONArray(String key) {
        String JSONString = getAsString(key);
        try {
            JSONArray obj = new JSONArray(JSONString);
            return obj;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // =======================================
    // ============== byte 数据 读写 =============
    // =======================================

    /**
     * 保存 byte数据 到 缓存中
     *
     * @param key   保存的key
     * @param value 保存的数据
     */
    public void put(String key, byte[] value) {
        OutputStream out = null;
        DiskLruCache.Editor editor = null;
        try {
            editor = editor(key);
            if (editor == null) {
                return;
            }
            out = editor.newOutputStream(0);
            out.write(value);
            out.flush();
            editor.commit();//write CLEAN
        } catch (Exception e) {
            e.printStackTrace();
            try {
                editor.abort();//write REMOVE
            } catch (IOException e1) {
                e1.printStackTrace();
            }

        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * @param timeLimit 过期时间  秒
     */
    public void put(String key, byte[] value, int timeLimit) {
        put(key, CacheValidManager.BytesWithTimeInfo(timeLimit, value));
    }


    public byte[] getAsBytes(String key) {
        byte[] res = null;
        InputStream is = get(key);
        if (is == null) return null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            byte[] buf = new byte[2048];
            int len = 0;
            while ((len = is.read(buf)) != -1) {
                baos.write(buf, 0, len);
            }
            res = CacheValidManager.isDue(baos.toByteArray());

            return res;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }


    // =======================================
    // ============== Serializable 序列化 数据 读写 =============
    // =======================================
    public void put(String key, Serializable value) {
        put(key, value, -1);
    }


    public void put(String key, Serializable value, int saveTime) {
        ByteArrayOutputStream baos = null;
        ObjectOutputStream oos = null;
        try {
            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            oos.writeObject(value);
            byte[] data = baos.toByteArray();
            if (saveTime != -1) {
                put(key, data, saveTime);
            } else {
                put(key, data);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                oos.close();
            } catch (IOException e) {
            }
        }
    }


    /**
     * 读取 Serializable数据
     *
     * @param key
     * @return Serializable 数据
     */
    public <T> T getAsSerializable(String key) {
        byte[] data = getAsBytes(key);
        if (data != null) {
            ByteArrayInputStream bais = null;
            ObjectInputStream ois = null;
            try {
                bais = new ByteArrayInputStream(data);
                ois = new ObjectInputStream(bais);
                T reObject = (T) ois.readObject();
                return reObject;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            } finally {
                try {
                    if (bais != null)
                        bais.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    if (ois != null)
                        ois.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;

    }


    // =======================================
    // ============== bitmap 数据 读写 =============
    // =======================================
    public void put(String key, Bitmap bitmap) {
        put(key, Util.bitmap2Bytes(bitmap));
    }

    /**
     * @param timeLimit 过期时间  秒
     */
    public void put(String key, Bitmap bitmap, int timeLimit) {
        put(key, CacheValidManager.BytesWithTimeInfo(timeLimit, Util.bitmap2Bytes(bitmap)));
    }


    public Bitmap getAsBitmap(String key) {
        byte[] bytes = getAsBytes(key);
        if (bytes == null) return null;
        return Util.bytes2Bitmap(bytes);
    }

    // =======================================
    // ============= drawable 数据 读写 =============
    // =======================================
    public void put(String key, Drawable value) {
        put(key, Util.drawable2Bitmap(value));
    }

    public Drawable getAsDrawable(String key) {
        byte[] bytes = getAsBytes(key);
        if (bytes == null) {
            return null;
        }
        return Util.bitmap2Drawable(Util.bytes2Bitmap(bytes));
    }

    // =======================================
    // ============= other methods =============
    // =======================================
    public boolean remove(String key) {
        try {
            key = Util.hashKeyForDisk(key);
            return mDiskLruCache.remove(key);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void close() throws IOException {
        mDiskLruCache.close();
    }

    public void delete() throws IOException {
        mDiskLruCache.delete();
    }

    public void flush() throws IOException {
        mDiskLruCache.flush();
    }

    public boolean isClosed() {
        return mDiskLruCache.isClosed();
    }

    public long size() {
        return mDiskLruCache.size();
    }

    public void setMaxSize(long maxSize) {
        mDiskLruCache.setMaxSize(maxSize);
    }

    public File getDirectory() {
        return mDiskLruCache.getDirectory();
    }

    public long getMaxSize() {
        return mDiskLruCache.getMaxSize();
    }


    //basic editor
    public DiskLruCache.Editor editor(String key) {
        try {
            key = Util.hashKeyForDisk(key);
            //wirte DIRTY
            DiskLruCache.Editor edit = mDiskLruCache.edit(key);
            //edit maybe null :the entry is editing
            if (edit == null) {
                Log.w(TAG, "the entry spcified key:" + key + " is editing by other . ");
            }
            return edit;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }


    //basic get
    public InputStream get(String key) {
        try {
            DiskLruCache.Snapshot snapshot = mDiskLruCache.get(Util.hashKeyForDisk(key));
            if (snapshot == null) //not find entry , or entry.readable = false
            {
                Log.e(TAG, "not find entry , or entry.readable = false");
                return null;
            }
            //write READ
            return snapshot.getInputStream(0);

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }


}



