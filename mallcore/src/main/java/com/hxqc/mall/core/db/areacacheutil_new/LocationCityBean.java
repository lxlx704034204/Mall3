package com.hxqc.mall.core.db.areacacheutil_new;

import com.hxqc.mall.core.db.CoreDatabase;
import com.hxqc.util.DebugLog;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Function: 地区缓存数据库中存放地区列表数据的表对应的转化类
 *
 * @author 袁秉勇
 * @since 2016年09月28日
 */
@Table(database = CoreDatabase.class, name = "location_city")
public class LocationCityBean extends BaseModel {
    @PrimaryKey(autoincrement = true)
    public Integer id;

    @Column
    public Integer dataType; // 当前数据类型 新车数据 or 4S店数据等等 （这个有由自己定义）

    @Column
    public String provinceInitial; // 省名称首大些字母

    @Column
    public String provinceName; // 省名称

    @Column
    public Integer provinceGroup; // 省组别编号（这个由自己定义）从0开始依次按组加1，例如0，1，2，3...

    @Column
    public String province; // 城市名称


    public LocationCityBean() {

    }


    public LocationCityBean(int dataType, String provinceInitial, String provinceName, int provinceGroup, String province) {
        super();
        this.dataType = dataType;
        this.provinceInitial = provinceInitial;
        this.provinceName = provinceName;
        this.provinceGroup = provinceGroup;
        this.province = province;
        DebugLog.e("----------", toString());
    }


    public Integer getDataType() {
        return dataType;
    }


    public String getProvince() {
        return province;
    }


    public Integer getProvinceGroup() {
        return provinceGroup;
    }


    public String getProvinceInitial() {
        return provinceInitial;
    }


    public String getProvinceName() {
        return provinceName;
    }


    public Integer getId() {
        return id;
    }


    @Override
    public String toString() {
        return "CityListBean{" +
                "dataType=" + dataType +
                ", id=" + id +
                ", provinceInitial='" + provinceInitial + '\'' +
                ", provinceName='" + provinceName + '\'' +
                ", provinceGroup=" + provinceGroup +
                ", province='" + province + '\'' +
                '}';
    }
}
