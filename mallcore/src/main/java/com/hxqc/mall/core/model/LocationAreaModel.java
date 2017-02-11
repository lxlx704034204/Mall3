package com.hxqc.mall.core.model;

import java.util.ArrayList;

/**
 * Function:第三方店铺省model
 *
 * @author 袁秉勇
 * @since 2015年12月07日
 */
public class LocationAreaModel {
    public String provinceName;//省全名
    public String provinceInitial;//省首字母
    public ArrayList<LocationArea > areaGroup;

    public String title;
    public String areaID;

    @Override
    public String toString() {
        return "LocationAreaModel{" +
                "provinceName='" + provinceName + '\'' +
                ", provinceInitial='" + provinceInitial + '\'' +
                ", areaGroup=" + areaGroup +
                ", title='" + title + '\'' +
                ", areaID='" + areaID + '\'' +
                '}';
    }
}
