package com.hxqc.mall.core.model;

import android.text.TextUtils;

import com.google.gson.annotations.Expose;

/**
 * 说明:用户信息
 * <p/>
 * author: 吕飞
 * since: 2015-03-19
 * Copyright:恒信汽车电子商务有限公司
 */
public class User {
    @Expose
    public String avatar;//头像Url
//    @Expose
//    public String avatarLocalCache;//头像本地缓存
    @Expose
    public String birthday;//生日
    @Expose
    public String fullname;//真实姓名
    @Expose
    public boolean isRealNameAuthentication;//是否实名认证
    @Expose
    public String gender;//性别 1 男 | 2 女 | 0 保密
    @Expose
    public String nickName = "";//昵称
    @Expose
    public String city;//市
    @Expose
    public String cityID;//市id
    @Expose
    public String detailedAddress;//详细地址
    @Expose
    public String district;//区
    @Expose
    public String districtID;//区id
    @Expose
    public String province;//省
    @Expose
    public String provinceID;//省id
    @Expose
    public String phoneNumber;
    @Expose
    public String levelID;
    @Expose
    public String level;
    @Expose
    public String levelIcon;

    public String getFullName(boolean encryption) {
        if (TextUtils.isEmpty(fullname)) {
//            String mPhoneNumber = UserInfoHelper.getInstance().getPhoneNumber(context);
            if (encryption) {
                return String.format("%s****%s", phoneNumber.substring(0, 3), phoneNumber.substring(7));
            }
            return phoneNumber;
        }
        return fullname;
    }

    public String getNickName(boolean encryption) {
//        if (TextUtils.isEmpty(nickName)) {
////            String mPhoneNumber = UserInfoHelper.getInstance().getPhoneNumber(context);
//            if (encryption) {
//                return String.format("%s****%s", phoneNumber.substring(0, 3), phoneNumber.substring(7));
//            }
//            return phoneNumber;
//        } else {
        return nickName;
//        }
    }


    @Override
    public String toString() {
        return "User{" +
                "avatar='" + avatar + '\'' +
                ", birthday='" + birthday + '\'' +
                ", fullname='" + fullname + '\'' +
                ", isRealNameAuthentication=" + isRealNameAuthentication +
                ", gender='" + gender + '\'' +
                ", nickName='" + nickName + '\'' +
                ", city='" + city + '\'' +
                ", cityID='" + cityID + '\'' +
                ", detailedAddress='" + detailedAddress + '\'' +
                ", district='" + district + '\'' +
                ", districtID='" + districtID + '\'' +
                ", province='" + province + '\'' +
                ", provinceID='" + provinceID + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", levelID='" + levelID + '\'' +
                ", level='" + level + '\'' +
                ", levelIcon='" + levelIcon + '\'' +
                '}';
    }
}
