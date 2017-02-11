package com.hxqc.mall.thirdshop.model;

/**
 * Function: 用来存储站点数据的实体类
 *
 * @author 袁秉勇
 * @since 2016年11月01日
 */
public class SiteBean {
    public String siteGroupName;

    public String siteGroupID;

    public String siteAreaName;

    public String siteProvinceName;


    public SiteBean(String siteAreaName, String siteGroupID, String siteGroupName, String siteProvinceName) {
        this.siteAreaName = siteAreaName;
        this.siteGroupID = siteGroupID;
        this.siteGroupName = siteGroupName;
        this.siteProvinceName = siteProvinceName;
    }


    @Override
    public String toString() {
        return "SiteBean{" +
                "siteAreaName='" + siteAreaName + '\'' +
                ", siteGroupName='" + siteGroupName + '\'' +
                ", siteGroupID='" + siteGroupID + '\'' +
                ", siteProvinceName='" + siteProvinceName + '\'' +
                '}';
    }
}
