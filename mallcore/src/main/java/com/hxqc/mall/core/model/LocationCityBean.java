//package com.hxqc.mall.core.model;
//
//import com.hxqc.util.DebugLog;
//import com.j256.ormlite.field.DatabaseField;
//import com.j256.ormlite.table.DatabaseTable;
//
///**
// * Function:
// *
// * @author 袁秉勇
// * @since 2016年03月09日
// */
//@DatabaseTable(tableName = "location_city")
//public class LocationCityBean {
//
//    @DatabaseField()
//    public int dataType;//当前数据类型 新车数据 or 4S店数据等等。。。 （这个有由自己定义）
//    @DatabaseField()
//    public String provinceInitial;//省名称首大些字母
//    @DatabaseField()
//    public String provinceName;//省名称
//    @DatabaseField()
//    public int provinceGroup;//省组别编号（这个由自己定义）从0开始依次按组加1，例如0，1，2，3...
//    @DatabaseField()
//    public String province;//城市名称
//
//
//    public LocationCityBean() {
//
//    }
//
//
//    public LocationCityBean(int dataType, String provinceInitial, String provinceName, int provinceGroup, String province) {
//        super();
//        this.dataType = dataType;
//        this.provinceInitial = provinceInitial;
//        this.provinceName = provinceName;
//        this.provinceGroup = provinceGroup;
//        this.province = province;
//        DebugLog.e("----------", toString());
//    }
//
//
//    public int getDataType() {
//        return dataType;
//    }
//
//
//    public void setDataType(int dataType) {
//        this.dataType = dataType;
//    }
//
//
//    public String getProvinceInitial() {
//        return provinceInitial;
//    }
//
//
//    public void setProvinceInitial(String provinceInitial) {
//        this.provinceInitial = provinceInitial;
//    }
//
//
//    public int getProvinceGroup() {
//        return provinceGroup;
//    }
//
//
//    public void setProvinceGroup(int provinceGroup) {
//        this.provinceGroup = provinceGroup;
//    }
//
//
//    public String getProvinceName() {
//        return provinceName;
//    }
//
//
//    public void setProvinceName(String provinceName) {
//        this.provinceName = provinceName;
//    }
//
//
//    public String getProvince() {
//        return province;
//    }
//
//
//    public void setProvince(String province) {
//        this.province = province;
//    }
//
//
//    @Override
//    public String toString() {
//        return "LocationCityBean{" +
//                "dataType=" + dataType +
//                ", provinceInitial='" + provinceInitial + '\'' +
//                ", provinceName='" + provinceName + '\'' +
//                ", provinceGroup=" + provinceGroup +
//                ", province='" + province + '\'' +
//                '}';
//    }
//}
