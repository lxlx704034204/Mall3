package com.hxqc.mall.core.model.auto;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.google.gson.annotations.Expose;
import com.hxqc.util.JSONUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * 说明:套餐
 * <p/>
 * author: 吕飞
 * since: 2015-10-09
 * Copyright:恒信汽车电子商务有限公司
 */
public class AutoPackage implements Parcelable, Cloneable {
    public static final String PACKAGE_CUSTOM = "custom";//特卖

    public enum PackageTypeEnum {
        custom(1), combo(2);
        private int value = 0;

        PackageTypeEnum(int value) {    //    必须是private的，否则编译错误
            this.value = value;
        }

        public static PackageTypeEnum valueOf(int value) {
            switch (value) {
                case 1:
                    return custom;
                case 2:
                    return combo;
                default:
                    return combo;
            }
        }

        public int value() {
            return this.value;
        }
    }

    @Expose
    public ArrayList< Accessory > accessory;//用品数组
    @Expose
    private String amount;//套餐价格
    @Expose
    public String packageID;//套餐ID
    @Expose
    public String title;//套餐名称
    @Expose
    public String status;//状态
    @Expose
    private static Set< Accessory > mCustomChooseAccessory;//选择的用品数组
    private static Set< Accessory > mTempChooseAccessory;//临时选择用品
    private float mTotalAmount;//已选择商品价格

    public int position;

    /**
     * 是否自定义套餐
     */
    public PackageTypeEnum isCustomPackage() {
        if (!TextUtils.isEmpty(packageID))
            switch (packageID) {
                case PACKAGE_CUSTOM:
                    return PackageTypeEnum.custom;
                default:
                    return PackageTypeEnum.combo;
            }
        else return PackageTypeEnum.custom;
    }

    /**
     * 增加选择的商品
     */
    public synchronized int setChooseAccessor(Accessory accessory, boolean isChoose) {
        if (mTempChooseAccessory == null) {
            mTempChooseAccessory = new HashSet<>();//选择的用品数组
        }

        if (isChoose) {
            mTempChooseAccessory.add(accessory);
        } else {
            mTempChooseAccessory.remove(accessory);
        }
        return mTempChooseAccessory.size();
    }

    public static void setCustomChooseAccessory() {
        if (mCustomChooseAccessory == null) mCustomChooseAccessory = new HashSet<>();
        mCustomChooseAccessory.clear();
        if (mTempChooseAccessory == null) return;
        mCustomChooseAccessory.addAll(mTempChooseAccessory);
        mTempChooseAccessory.clear();
    }

    public void seTempChooseAccessory() {
        if (mTempChooseAccessory == null) mTempChooseAccessory = new HashSet<>();
        if (mCustomChooseAccessory == null) mCustomChooseAccessory = new HashSet<>();
        mTempChooseAccessory.addAll(mCustomChooseAccessory);
    }

    public static synchronized Set< Accessory > getTempChooseAccessory() {
        return mTempChooseAccessory;
    }

    /**
     * 获取选择商品
     */
    public Iterator< Accessory > getChooseAccessory() {
        switch (isCustomPackage()) {
            case combo:
                return accessory.iterator();
            case custom:
                return mCustomChooseAccessory.iterator();
            default:
                return null;
        }

    }

    /**
     * 商品总价
     */
    public float getTotalAmount() {
        switch (isCustomPackage()) {
            case combo:
                //普通
                if (mTotalAmount == 0) {
                    if (accessory != null) {
                        for (Accessory ac : accessory) {
                            mTotalAmount += ac.getSubtotal();
                        }
                    }
                }
                break;
            case custom:
                //自定义
                mTotalAmount = 0f;
                if (mCustomChooseAccessory != null) {
                    for (Accessory ac : mCustomChooseAccessory) {
                        mTotalAmount += ac.getSubtotal();
                    }
                }
                break;
            default:
                return 0f;
        }
        return mTotalAmount;
    }

    /**
     * 获取临时总价
     *
     * @return
     */
    public float getCustomTempToalAmount() {
        switch (isCustomPackage()) {
            case custom:
                //自定义
                mTotalAmount = 0f;
                if (mTempChooseAccessory != null) {
                    for (Accessory ac : mTempChooseAccessory) {
                        mTotalAmount += ac.getSubtotal();
                    }
                }
                break;
            default:
                return 0f;
        }
        return mTotalAmount;
    }

    /**
     * 活动总价
     */
    public float getAmount() {
        switch (isCustomPackage()) {
            case combo:
                return Float.parseFloat(amount);
            case custom:
                if (Float.parseFloat(amount) != 0)
                    return Float.parseFloat(amount);
                return getTotalAmount();
            default:
                return 0f;
        }
    }

    /**
     * 获取选中的用品
     */
    public String getAccessoryString() {
        switch (isCustomPackage()) {
            case custom:
//                //自定义
//                StringBuilder sb = new StringBuilder();
//                for (Accessory ac : mCustomChooseAccessory) {
//                    sb.append(ac.accessoryID).append(",");
//                }
//                if (sb.lastIndexOf(",") == sb.length()-1) {
//                    sb.deleteCharAt(sb.length() - 1);
//                }
//                return sb.toString();
                return JSONUtils.toJson(mCustomChooseAccessory);
            case combo:
                return null;
            default:
                return null;
        }
    }

    public AutoPackage(String title) {
        this.title = title;
    }

    public AutoPackage() {
    }

    /**
     * 设置是否选择
     * false  移除所有选择的单品
     */
    public static void clearTempChooseAccessory() {
        if (mTempChooseAccessory != null) {
            mTempChooseAccessory.clear();
        }
    }

    public static void clearCustomChooseAccessory() {
        if (mCustomChooseAccessory != null) {
            mCustomChooseAccessory.clear();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof AutoPackage) {
            return packageID.equals(((AutoPackage) o).packageID);
        }
        return super.equals(o);
    }


    @Override
    protected Object clone() throws CloneNotSupportedException {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public AutoPackage clonePackage() {
        try {
            return (AutoPackage) clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }

    }


    public Set< Accessory > getCustomChooseAccessory() {
        if (mCustomChooseAccessory == null) mCustomChooseAccessory = new HashSet<>();
        return mCustomChooseAccessory;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(accessory);
        dest.writeString(this.amount);
        dest.writeString(this.packageID);
        dest.writeString(this.title);
        dest.writeString(this.status);
    }


    protected AutoPackage(Parcel in) {
        this.accessory = in.createTypedArrayList(Accessory.CREATOR);
        this.amount = in.readString();
        this.packageID = in.readString();
        this.title = in.readString();
        this.status = in.readString();
    }

    public static final Parcelable.Creator< AutoPackage > CREATOR = new Parcelable.Creator< AutoPackage >() {
        public AutoPackage createFromParcel(Parcel source) {
            return new AutoPackage(source);
        }

        public AutoPackage[] newArray(int size) {
            return new AutoPackage[size];
        }
    };
}
