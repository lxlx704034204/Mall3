package com.hxqc.mall.auto.dao;

import android.content.Context;

import com.hxqc.mall.auto.model.PAC;
import com.hxqc.mall.core.db.DbHelper;

import java.util.List;

/**
 * Author:胡仲俊
 * Date: 2016 - 04 - 13
 * FIXME
 * Todo
 */
public class PACDao {

    private static final String TAG = "PACDao";

    private static PACDao instance;

    /**
     * 单例获取该Helper
     * @return
     */
    public static PACDao getInstance() {
        if (instance == null) {
            synchronized (PACDao.class) {
                if (instance == null)
                    instance = new PACDao();
            }
        }
        return instance;
    }

    /**
     * @param pac
     */
    public void add(Context context,PAC pac) {
        /*try {
            PACDatabaseHelper.getHelper(context.getApplicationContext()).getPACDao().create(pac);
        } catch (SQLException e) {
        }*/
    }

    /**
     * @param pac
     */
    public void delete(Context context,PAC pac) {
        /*try {
            PACDatabaseHelper.getHelper(context.getApplicationContext()).getPACDao().delete(pac);
        } catch (SQLException e) {
        }*/
    }

    /**
     * @param pac
     */
    public void update(Context context,PAC pac) {
        /*try {
            PACDatabaseHelper.getHelper(context.getApplicationContext()).getPACDao().update(pac);
        } catch (SQLException e) {
        }*/
    }

    /**
     * @return
     */
    public List<PAC> queryForAll() {
        /*try {
            List<PAC> pacs = PACDatabaseHelper.getHelper(context.getApplicationContext()).getPACDao().queryForAll();
            return pacs;
        } catch (SQLException e) {
            DebugLog.i(TAG,e.toString());
        }*/
        try {
            List<PAC> pacs = DbHelper.query(PAC.class);
            return pacs;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param plateNumber
     * @return
     */
    public PAC query(String plateNumber) {
        List<PAC> pacs = queryForAll();
        if (pacs != null) {
            for (int i = 0; i < pacs.size(); i++) {
                if (pacs.get(i).plateNumber.equals(plateNumber)) {
                    PAC pac = pacs.get(i);
                    return pac;
                }
            }
        }

        return null;
    }

    /**
     * @param province
     * @return
     */
    public PAC checkProvince(String province) {
        List<PAC> pacs = queryForAll();
        if (pacs != null) {
            for (int i = 0; i < pacs.size(); i++) {
                if (pacs.get(i).province.equals(province)) {
                    PAC pac = pacs.get(i);
                    return pac;
                }
            }
        }

        return null;
    }

    /**
     *
     */
    public void killInstance(Context context) {
//        PACDatabaseHelper.getHelper(context).close();
        if (instance != null) {
            instance = null;
        }

    }
}
