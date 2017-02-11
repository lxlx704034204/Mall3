package com.hxqc.mall.auto.model;

import com.hxqc.mall.core.db.CoreDatabase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Author:胡仲俊
 * Date: 2016 - 04 - 13
 * FIXME
 * Todo 省市城市的数据库MODEL
 */
//@DatabaseTable(tableName = "db_pac")

@Table(database = CoreDatabase.class)
public class PAC extends BaseModel{

    //    @DatabaseField(generatedId = true)
    @PrimaryKey(autoincrement = true)
    public int id;
//    @DatabaseField(columnName = "plateNumber")
    @Column
    public String plateNumber;
//    @DatabaseField(columnName = "city")
    @Column
    public String city;
//    @DatabaseField(columnName = "province")
    @Column
    public String province;

    public PAC() {
    }

    public PAC(String plateNumber, String city, String province) {
        this.plateNumber = plateNumber;
        this.city = city;
        this.province = province;
    }

    @Override
    public String toString() {
        return "PAC{" +
                "id=" + id +
                ", plateNumber='" + plateNumber + '\'' +
                ", city='" + city + '\'' +
                ", province='" + province + '\'' +
                '}';
    }
}
