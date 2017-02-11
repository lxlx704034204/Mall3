package com.hxqc.mall.core.db.carcomparedb;

import com.hxqc.mall.core.db.CoreDatabase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by zhaofan on 2016/10/9.
 */

@Table(database = CoreDatabase.class)
public class ChooseCarModel extends BaseModel {
    //自增ID
    @PrimaryKey(autoincrement = true)
    public Long id;
    @Column
    public String brand;
    @Column
    public String modelName;
    @Column
    public String extId;
    @Column
    public int isCheck;

    public ChooseCarModel() {
    }

    public ChooseCarModel(String brand, String modelName, String extId, int isCheck) {
        this.brand = brand;
        this.modelName = modelName;
        this.extId = extId;
        this.isCheck = isCheck;
    }



    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getExtId() {
        return extId;
    }

    public void setExtId(String extId) {
        this.extId = extId;
    }

    public int getIsCheck() {
        return isCheck;
    }

    public void setIsCheck(int isCheck) {
        this.isCheck = isCheck;
    }

    @Override
    public String toString() {
        return "ChooseCarModel{" +
                "id=" + id +
                ", modelName='" + modelName + '\'' +
                ", extId='" + extId + '\'' +
                ", isCheck=" + isCheck +
                '}';
    }
}
