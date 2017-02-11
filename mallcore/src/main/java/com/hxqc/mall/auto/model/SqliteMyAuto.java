//package com.hxqc.mall.auto.model;
//
//
///**
// * Author:胡仲俊
// * Date: 2016 - 04- 13
// * FIXME
// * Todo 车辆信息
// */
//@Deprecated
//@DatabaseTable(tableName = "db_auto")
//public class SqliteMyAuto {
//
//    @DatabaseField(generatedId = true)
//    private int id;
//    @DatabaseField(columnName = "myAutoID")
//    private String myAutoID;//    我的车辆ID string
//    @DatabaseField(columnName = "brand")
//    private String brand;//    品牌名称 string
//    @DatabaseField(columnName = "brandID")
//    private String brandID;//    品牌ID string
//    @DatabaseField(columnName = "brandThumb")
//    private String brandThumb;//    品牌图标 string
//    @DatabaseField(columnName = "series")
//    private String series;//    车系名称 string
//    @DatabaseField(columnName = "seriesID")
//    private String seriesID;//    车系ID string
//    @DatabaseField(columnName = "autoModel")
//    private String autoModel;//    车型名称 string
//    @DatabaseField(columnName = "autoModelID")
//    private String autoModelID;//    车型ID string
//    @DatabaseField(columnName = "plateNumber")
//    private String plateNumber;//    车牌号 (鄂A58945) string
//    @DatabaseField(columnName = "VIN")
//    private String VIN;//    VIN码 string
//    @DatabaseField(columnName = "drivingDistance")
//    private String drivingDistance;//    行驶里程 单位(千米) number
//    @DatabaseField(columnName = "ownName")
//    private String ownName;//    车主姓名 string
//    @DatabaseField(columnName = "ownPhone")
//    private String ownPhone;//    车主手机号 string
//    @DatabaseField(columnName = "lastMaintenancDate")
//    private String lastMaintenancDate;//    上次维修时间 string
//    @DatabaseField(columnName = "isDefault")
//    private int isDefault;//    是否默认 20是 10不是 number
//    @DatabaseField(columnName = "guaranteePeriod")
//    private String guaranteePeriod;//    质保期 y-m-n string
//    @DatabaseField(columnName = "nextMaintenanceDistance")
//    private long nextMaintenanceDistance;
//    @DatabaseField(columnName = "nextInsurance")
//    private long nextInsurance;
//    @DatabaseField(columnName = "lastMaintenanceDistance")
//    private long lastMaintenanceDistance;
//
//    public SqliteMyAuto() {
//    }
//
//    public SqliteMyAuto(int id, String myAutoID, String brand, String brandID, String brandThumb, String series, String seriesID, String autoModel, String autoModelID, String plateNumber, String VIN, String drivingDistance, String ownName, String ownPhone, String lastMaintenancDate, int isDefault, String guaranteePeriod, long nextMaintenanceDistance, long nextInsurance, long lastMaintenanceDistance) {
//        this.id = id;
//        this.myAutoID = myAutoID;
//        this.brand = brand;
//        this.brandID = brandID;
//        this.brandThumb = brandThumb;
//        this.series = series;
//        this.seriesID = seriesID;
//        this.autoModel = autoModel;
//        this.autoModelID = autoModelID;
//        this.plateNumber = plateNumber;
//        this.VIN = VIN;
//        this.drivingDistance = drivingDistance;
//        this.ownName = ownName;
//        this.ownPhone = ownPhone;
//        this.lastMaintenancDate = lastMaintenancDate;
//        this.isDefault = isDefault;
//        this.guaranteePeriod = guaranteePeriod;
//        this.nextMaintenanceDistance = nextMaintenanceDistance;
//        this.nextInsurance = nextInsurance;
//        this.lastMaintenanceDistance = lastMaintenanceDistance;
//    }
//
//    public int getId() {
//        return id;
//    }
//
//    public void setId(int id) {
//        this.id = id;
//    }
//
//    public String getMyAutoID() {
//        return myAutoID;
//    }
//
//    public void setMyAutoID(String myAutoID) {
//        this.myAutoID = myAutoID;
//    }
//
//    public String getBrand() {
//        return brand;
//    }
//
//    public void setBrand(String brand) {
//        this.brand = brand;
//    }
//
//    public String getBrandID() {
//        return brandID;
//    }
//
//    public void setBrandID(String brandID) {
//        this.brandID = brandID;
//    }
//
//    public String getBrandThumb() {
//        return brandThumb;
//    }
//
//    public void setBrandThumb(String brandThumb) {
//        this.brandThumb = brandThumb;
//    }
//
//    public String getSeries() {
//        return series;
//    }
//
//    public void setSeries(String series) {
//        this.series = series;
//    }
//
//    public String getSeriesID() {
//        return seriesID;
//    }
//
//    public void setSeriesID(String seriesID) {
//        this.seriesID = seriesID;
//    }
//
//    public String getAutoModel() {
//        return autoModel;
//    }
//
//    public void setAutoModel(String autoModel) {
//        this.autoModel = autoModel;
//    }
//
//    public String getAutoModelID() {
//        return autoModelID;
//    }
//
//    public void setAutoModelID(String autoModelID) {
//        this.autoModelID = autoModelID;
//    }
//
//    public String getPlateNumber() {
//        return plateNumber;
//    }
//
//    public void setPlateNumber(String plateNumber) {
//        this.plateNumber = plateNumber;
//    }
//
//    public String getVIN() {
//        return VIN;
//    }
//
//    public void setVIN(String VIN) {
//        this.VIN = VIN;
//    }
//
//    public String getDrivingDistance() {
//        return drivingDistance;
//    }
//
//    public void setDrivingDistance(String drivingDistance) {
//        this.drivingDistance = drivingDistance;
//    }
//
//    public String getOwnName() {
//        return ownName;
//    }
//
//    public void setOwnName(String ownName) {
//        this.ownName = ownName;
//    }
//
//    public String getOwnPhone() {
//        return ownPhone;
//    }
//
//    public void setOwnPhone(String ownPhone) {
//        this.ownPhone = ownPhone;
//    }
//
//    public String getLastMaintenancDate() {
//        return lastMaintenancDate;
//    }
//
//    public void setLastMaintenancDate(String lastMaintenancDate) {
//        this.lastMaintenancDate = lastMaintenancDate;
//    }
//
//    public int getIsDefault() {
//        return isDefault;
//    }
//
//    public void setIsDefault(int isDefault) {
//        this.isDefault = isDefault;
//    }
//
//    public String getGuaranteePeriod() {
//        return guaranteePeriod;
//    }
//
//    public void setGuaranteePeriod(String guaranteePeriod) {
//        this.guaranteePeriod = guaranteePeriod;
//    }
//
//    public long getNextMaintenanceDistance() {
//        return nextMaintenanceDistance;
//    }
//
//    public void setNextMaintenanceDistance(long nextMaintenanceDistance) {
//        this.nextMaintenanceDistance = nextMaintenanceDistance;
//    }
//
//    public long getNextInsurance() {
//        return nextInsurance;
//    }
//
//    public void setNextInsurance(long nextInsurance) {
//        this.nextInsurance = nextInsurance;
//    }
//
//    public long getLastMaintenanceDistance() {
//        return lastMaintenanceDistance;
//    }
//
//    public void setLastMaintenanceDistance(long lastMaintenanceDistance) {
//        this.lastMaintenanceDistance = lastMaintenanceDistance;
//    }
//
//    @Override
//    public String toString() {
//        return "SqliteMyAuto{" +
//                "id=" + id +
//                ", myAutoID='" + myAutoID + '\'' +
//                ", brand='" + brand + '\'' +
//                ", brandID='" + brandID + '\'' +
//                ", brandThumb='" + brandThumb + '\'' +
//                ", series='" + series + '\'' +
//                ", seriesID='" + seriesID + '\'' +
//                ", autoModel='" + autoModel + '\'' +
//                ", autoModelID='" + autoModelID + '\'' +
//                ", plateNumber='" + plateNumber + '\'' +
//                ", VIN='" + VIN + '\'' +
//                ", drivingDistance='" + drivingDistance + '\'' +
//                ", ownName='" + ownName + '\'' +
//                ", ownPhone='" + ownPhone + '\'' +
//                ", lastMaintenancDate='" + lastMaintenancDate + '\'' +
//                ", isDefault=" + isDefault +
//                ", guaranteePeriod='" + guaranteePeriod + '\'' +
//                ", nextMaintenanceDistance=" + nextMaintenanceDistance +
//                ", nextInsurance=" + nextInsurance +
//                ", lastMaintenanceDistance=" + lastMaintenanceDistance +
//                '}';
//    }
//}
