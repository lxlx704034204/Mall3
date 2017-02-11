package com.hxqc.mall.reactnative.model;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.hxqc.mall.config.router.RouteOpenActivityUtil;

/**
 * Author: wanghao
 * Date: 2015-04-22
 * FIXME
 * 主页上半部分 切换的  活动
 */
public class HomeSlideADModel_RN implements Parcelable, Cloneable {

    public static final Creator<HomeSlideADModel_RN> CREATOR = new Creator<HomeSlideADModel_RN>() {
        public HomeSlideADModel_RN createFromParcel(Parcel source) {
            return new HomeSlideADModel_RN(source);
        }

        public HomeSlideADModel_RN[] newArray(int size) {
            return new HomeSlideADModel_RN[size];
        }
    };
    /**
     * 首页Banner跳转
     * <p/>
     * hxmall://ProductDetail/{itemID}/{title}		自营车辆详情
     * <p/>
     * hxmall://Seckill/{itemID}/{title}			特卖详情
     * <p/>
     * hxmall://Shop/{shopID}/{page}/{title}		网上4S店对应页面
     * page:
     * home ＝ 首页
     * auto ＝ 车型报价
     * main = 保养
     * repair = 修车预约
     * acce = 用品
     * prom = 促销
     * <p/>
     * hxmall://ShopPromotion/{promotionID}/{type}/{title}	促销详情
     * <p/>
     * hxmall://EV/{page}	新能源
     * page:
     * home ＝ 首页
     * seckill ＝ 特卖列表
     * wiki = 百科
     */

    final private String Seckill = "Seckill";//特卖详情
    final private String ProductDetail = "ProductDetail";//自营车辆详情
    final private String Shop = "Shop";//网上4S店对应页面
    final private String ShopPromotion = "ShopPromotion";//促销详情
    final private String EV = "EV";//新能源
    public String slide;
    public String type;
    public String url;
    public String routerUrl;

    public HomeSlideADModel_RN() {
    }

    private HomeSlideADModel_RN(Parcel in) {
        this.slide = in.readString();
        this.type = in.readString();
        this.url = in.readString();
        this.routerUrl = in.readString();
    }

    //是否活动
    private boolean isPromotion() {
        return type.equals("1");
    }

    //是否特卖
    private String getTagActivityType() {
        return url.split("/")[2];
    }

    //店铺page
    private String getPage() {
        return url.split("/")[4];
    }

    private String getPromotionType() {
        return url.split("/")[4];
    }

    //获取ID
    private String getID() {
        return url.split("/")[3];
    }

    //获取新能源  page
    private String getEVPage() {
        return url.split("/")[3];
    }

    //获取商品名字
    private String getProductName() {

        if (getTagActivityType().equals(Shop) || getTagActivityType().equals(ShopPromotion)) {
            return url.split("/")[5];
        } else {
            return url.split("/")[4];
        }
    }

    //跳转
    public void activitySwitch(Context context) {

//        跳转路由
        if (TextUtils.isEmpty(routerUrl)) {
            RouteOpenActivityUtil.linkToActivity(context, url);
        } else {
            RouteOpenActivityUtil.linkToActivity(context, routerUrl);
        }

//        if (isPromotion()) {
//            ActivitySwitcher.toEventDetail(context, url);
//        } else {
//            switch (getTagActivityType()) {
//                case Seckill:
//                    ActivitySwitcher.toAutoItemDetail(context, AutoItem.AUTO_PROMOTION, getID(), "");
//                    break;
//                case ProductDetail:
//                    ActivitySwitcher.toAutoItemDetail(context, AutoItem.AUTO_COMMON, getID(), "");
//                    break;
//                case Shop:
//
//                    switch (getPage()) {
//                        case "home":
//                            ActivitySwitcherThirdPartShop.toShopHome(getID(), context);
//                            break;
//                        case "auto":
//                            ActivitySwitcherThirdPartShop.toModelsOffer(getID(), context);
//                            break;
//                        case "main":
//                            ActivitySwitcherThirdPartShop.toMaintenanHome(getID(), context);
//                            break;
//                        case "repair":
//                            ActivitySwitcherMaintenance.toReserveMaintainAndHeadActivity(context, getID());
//                            break;
//                        case "acce":
//                            ActivitySwitcherAccessory.toAccessorySaleActivity(context);
//                            break;
//                        case "prom":
//                            ActivitySwitcherThirdPartShop.toShopPromotionList(getID(), context);
//                            break;
//                    }
//
//                    break;
//                case ShopPromotion:
//
//                    if (getPromotionType().equals("10")) {
//                        ActivitySwitcherThirdPartShop.toSalesItemDetail(getID(), context);
//                    } else if (getPromotionType().equals("60")) {
//                        ActivitySwitchMaintenance.toMaintenancePromotion(getID(), context);
//                    }
//
//                    break;
//                case EV:
//                    switch (getEVPage()) {
//                        case "home":
//                            ActivitySwitcher.toWhere(context, "com.hxqc.newenergy.activity.Ev_NewEnergyActivity");
//                            break;
//                        case "seckill":
//                            context.startActivity(new Intent(context, Ev_NewEnergyCarTemaiActivity.class));
//                            break;
//                        case "wiki":
//                            ActivitySwitcherEV.toWikiActivity(context);
//                            break;
//                    }
//                    break;
//            }
//
//        }

    }

    @Override
    public String toString() {
        return "HomeSlideADModel{" +
                "slide='" + slide + '\'' +
                ", type='" + type + '\'' +
                ", url='" + url + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.slide);
        dest.writeString(this.type);
        dest.writeString(this.url);
        dest.writeString(this.routerUrl);
    }
}
