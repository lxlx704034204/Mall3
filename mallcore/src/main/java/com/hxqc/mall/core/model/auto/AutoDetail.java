package com.hxqc.mall.core.model.auto;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.hxqc.mall.core.util.OtherUtil;
import com.hxqc.socialshare.pojo.ShareContent;

import java.util.ArrayList;
import java.util.List;


/**
 * Author: HuJunJie
 * Date: 2015-04-09
 * FIXME
 * Todo
 */
public class AutoDetail implements Parcelable {

    /**
     * 交易状态
     */
    public enum TransactionStatus {
        UNAVAILABLE,//商品下架
        PREPARE,//未开始
        NORMAL,//正常
        SELLOUT,//数量为0
        END//时间结束
    }

    int isCollect;//关注
    AutoBaseInformation baseInfo;//基本数据信息
    Promotion promotion;//特卖
    Fare fare;//包牌费用
    ShareContent share;//分享
    Subsidy subsidy;//新能源补贴
    ArrayList< AutoPackage > packages;//套餐
    ArrayList< PaymentType > paymentType;//付款方式
    ArrayList< PickupPointT > pickupPoint;//自提点

    public AutoDetail() {
    }

    public Subsidy getSubsidy() {
        return subsidy;
    }

    public ArrayList< AutoPackage > getAutoPackages() {
        return packages;
    }

    public AutoBaseInformation getAutoBaseInformation() {
        return baseInfo;
    }

    public Promotion getPromotion() {
        return promotion;
    }

    public Fare getFare() {
        return fare;
    }

    public ShareContent getShare() {
        return share;
    }

    public boolean getIsCollect() {
        return OtherUtil.int2Boolean(isCollect);
    }

    public ArrayList< AutoPackage > getPackages() {
        return packages;
    }

    public String getItemDescription() {
        if (isPromotion())
            return promotion.getTitle();
        return baseInfo.getItemDescription();
    }


    /**
     * 车身颜色
     */
    public String[] getItemColor() {
        return baseInfo.getItemColor();
    }

    /**
     * 内饰
     */
    public String[] getItemInterior() {
        return baseInfo.getItemInteriorArray();
    }


    public String getItemInteriorColor() {
        return baseInfo.getItemInterior();
    }

    public String getItemColorDescription() {
        return baseInfo.getItemColorDescription();
    }

    public String getItemInteriorDescription() {
        return baseInfo.getItemInteriorDescription();
    }

    /**
     * 车辆价格
     */
    public float getItemPrice() {
        if (isPromotion())
            return promotion.getPrice();
        return baseInfo.getItemPrice();
    }

    /**
     * 车辆价格  带单位(万)
     */
    public String getItemPriceU() {
        if (isPromotion())
            return promotion.getPriceU();
        return baseInfo.getItemPriceU();
    }

    /**
     * 降幅
     */
    public String getItemFallU() {
        if (isPromotion()) {
            return promotion.getFallU();
        } else {
            return baseInfo.getItemFallU();
        }
    }

    /**
     * 销量
     */
    public String getItemSales() {
        return baseInfo.getItemSales();
    }

    /**
     * 库存
     */
    public String getInventory() {
        if (isPromotion())
            return promotion.getStore();
        return baseInfo.getInventory();
    }

    /**
     * 交易状态
     */
    public TransactionStatus transactionStatus() {
        if (!baseInfo.isItemAvailable()) {
            return TransactionStatus.UNAVAILABLE;
        }
        if (isPromotion())
            return promotion.transactionStatus();
        return baseInfo.transactionStatus();
    }

    /**
     * 订金 特价订金
     */
    public String getSubscription() {
        if (isPromotion()) {
            return promotion.getSubscription();
        }
        return baseInfo.getSubscription();
    }

    /**
     * 图文详情
     */
    public String getIntroduce() {
        if (isPromotion()) {
            return promotion.getIntroduce();
        }
        return baseInfo.getIntroduce();
    }

    /**
     * 车辆原价
     */
    public String getItemOriginalPrice() {
        return baseInfo.getItemOriginalPrice();
    }

    /**
     * 车辆类型
     *
     * @return 车辆类别 (10.汽车，20.电动车)
     */
    public int getItemCategory() {
        return baseInfo.itemCategory;
    }

    public String getItemID() {
        return baseInfo.getItemID();
    }

    public String getPromotionID() {
        return baseInfo.getPromotionID();
    }

    public String getSeriesID() {
        return baseInfo.getSeriesID();
    }

    public String getExtID() {
        return baseInfo.getExtID();
    }

    public ArrayList< PaymentType > getPaymentType() {
        return paymentType;
    }

    public boolean isPromotion() {
        return (promotion != null && !TextUtils.isEmpty(promotion.promotionID));
    }

    public void setPackages(ArrayList< AutoPackage > packages) {
        this.packages = packages;
    }

    public ArrayList< PickupPointT > getPickupPoint() {
        return pickupPoint;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.isCollect);
        dest.writeParcelable(this.baseInfo, 0);
        dest.writeParcelable(this.promotion, 0);
        dest.writeParcelable(this.fare, 0);
        dest.writeParcelable(this.share, 0);
        dest.writeTypedList(packages);
        dest.writeList(this.paymentType);
        dest.writeTypedList(pickupPoint);
    }

    protected AutoDetail(Parcel in) {
        this.isCollect = in.readInt();
        this.baseInfo = in.readParcelable(AutoBaseInformation.class.getClassLoader());
        this.promotion = in.readParcelable(Promotion.class.getClassLoader());
        this.fare = in.readParcelable(Fare.class.getClassLoader());
        this.share = in.readParcelable(ShareContent.class.getClassLoader());
        this.packages = in.createTypedArrayList(AutoPackage.CREATOR);
        this.paymentType = new ArrayList< PaymentType >();
        in.readList(this.paymentType, List.class.getClassLoader());
        this.pickupPoint = in.createTypedArrayList(PickupPointT.CREATOR);
    }

    public static final Creator< AutoDetail > CREATOR = new Creator< AutoDetail >() {
        public AutoDetail createFromParcel(Parcel source) {
            return new AutoDetail(source);
        }

        public AutoDetail[] newArray(int size) {
            return new AutoDetail[size];
        }
    };
}
