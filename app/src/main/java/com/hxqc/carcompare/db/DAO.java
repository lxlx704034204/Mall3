package com.hxqc.carcompare.db;

import com.hxqc.mall.core.db.DbHelper;
import com.hxqc.mall.core.db.carcomparedb.ChooseCarModel;
import com.hxqc.mall.core.db.carcomparedb.ChooseCarModel_Table;
import com.raizlabs.android.dbflow.sql.language.Condition;

import java.util.List;

/**
 * Created by zhaofan on 2016/11/18.
 */
public class DAO {

    /**
     * 添加对比车辆
     *
     * @param brand
     * @param series
     * @param extID
     * @param callback
     */
    public static void addCompareCarDb(String brand, String series, String extID, onSuccessCallBack callback) {
        ChooseCarModel data = null;
        int check = 0;
        for (ChooseCarModel i : DbHelper.query(ChooseCarModel.class)) {
            if (i.getIsCheck() == 1) {
                if (check == 0) {
                    data = i;
                }
                check++;
            }
        }
        ChooseCarModel entity = new ChooseCarModel(brand, series, extID, 1);//check < 10 ? 1 : 0);
        //  if (DbHelper.query(ChooseCarModel.class, ChooseCarModel_Table.extId.eq(extID)).isEmpty()) {
        DbHelper.delete(ChooseCarModel.class, ChooseCarModel_Table.extId.eq(extID));
        entity.save();
        if (check >= 10) {
            if (data != null) {
                data.setIsCheck(0);
                data.update();
            }
        }
        if (callback != null)
            callback.onSaveSuccess();
    }

    public interface onSuccessCallBack {
        void onSaveSuccess();
    }

    /**
     * 删除对比车辆
     *
     * @param extID
     */
    public static void deleteCarDb(String extID) {
        DbHelper.delete(ChooseCarModel.class, ChooseCarModel_Table.extId.eq(extID));
    }


    /**
     * 查询对比车型list
     *
     * @param whereCondition
     * @return
     */
    public static List<ChooseCarModel> queryCarCompareList(Condition... whereCondition) {
        return DbHelper.query(ChooseCarModel.class, ChooseCarModel_Table.id, false, whereCondition);
    }


}
