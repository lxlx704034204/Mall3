package com.hxqc.mall.usedcar.model;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * 车辆详情
 * @author huangyi
 * @since 2015年10月20日
 */
public class CarDetail implements Serializable {

    public InstalmentAndQA qa;
    public String state; //0上架 1已售 2已下架 3待上架

    public String cover; //汽车大图
    public String car_source_no; //车源号

    public String contacts; //联系人
    public String phone_num; //联系人手机号
    public String customer_head_img; //车主头像
    public String owners; //自述
    public String province; //所在省
    public String city; //所在城市
    public String look_address; //看车地址

    public String car_mileage; //里程
    public String first_on_card; //上牌时间

    public String car_name; //车名
    public String new_car_price; //新车价
    public String estimate_price; //估价
    public String purchase; //购置税
    public String engine; //发动机
    public String transmission; //变速箱

    public String detail_url; //车辆详情Url
    public String outer_inner_img; //参数配置Url

    public String warranty_date; //质保有效期
    public String insurance_date; //商业险到期时间
    public String inspection_date; //年检有效期
    public String sali_date; //交强险有效期

    public ArrayList<Image> image; //图集
    public ArrayList<UsedCarBase> recommend_car_source; //同价位车源推荐

    /** 车龄 **/
    public String getCarAgeStr() {
        int startYear = Integer.valueOf(first_on_card.substring(0, 4));
        int startMonth = Integer.valueOf(first_on_card.substring(5, 7));
        int startDay = Integer.valueOf(first_on_card.substring(8));
        Calendar calendar = Calendar.getInstance();
        int endYear = calendar.get(Calendar.YEAR);
        int endMonth = calendar.get(Calendar.MONTH) + 1;
        int endDay = calendar.get(Calendar.DAY_OF_MONTH);

        if (endDay >= startDay) ++endMonth;
        if (endMonth > startMonth) {
            int disYear = endYear - startYear;
            int disMonth = endMonth - startMonth;
            if (disYear > 0) {
                if (disMonth == 12) {
                    ++disYear;
                    return disYear + "年";
                } else {
                    return disYear + "年" + disMonth + "月";
                }
            } else {
                if (disMonth == 12) {
                    return "1年";
                } else {
                    return disMonth + "月";
                }
            }
        } else if (endMonth == startMonth) {
            int disYear = endYear - startYear;
            if (disYear > 0) {
                return disYear + "年";
            } else {
                return "1月";
            }
        } else {
            int disYear = endYear - startYear - 1;
            int disMonth = endMonth + 12 - startMonth;
            if (disYear > 0) {
                if (disMonth == 12) {
                    ++disYear;
                    return disYear + "年";
                } else {
                    return disYear + "年" + disMonth + "月";
                }
            } else {
                if (disMonth == 12) {
                    return "1年";
                } else {
                    return disMonth + "月";
                }
            }
        }
    }

    /**
     * 销售价
     **/
    public String getCarPrice() {
        String string = null;
        try {
            string = new DecimalFormat("0.00").format(Float.valueOf(estimate_price));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return "销售价: ￥" + string + "万";
    }

    /**
     * 新车价
     **/
    public String getNewCarPrice() {
        String string = null;
        try {
            string = new DecimalFormat("0.00").format(Float.valueOf(new_car_price));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return "新    车    价: " + string + "万";
    }

    /**
     * 购置税
     **/
    public String getPurchasePrice() {
        String string = null;
        try {
            string = new DecimalFormat("0.00").format(Float.valueOf(purchase));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return "购  置  税: " + string + "万";
    }

    /**
     * 新车总价
     **/
    public String getAllPrice() {
        String string = null;
        try {
            float price = Float.valueOf(new_car_price) + Float.valueOf(purchase);
            DecimalFormat decimalFormat = new DecimalFormat("0.00"); //构造方法的字符格式这里如果小数不足2位 会以0补足
            string = decimalFormat.format(price);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return "新车含税价: " + string + "万";
    }

    /**
     * 为您节省
     **/
    public String getSavePrice() {
        String string = null;
        try {
            float price = Float.valueOf(new_car_price) + Float.valueOf(purchase) - Float.valueOf(estimate_price);
            DecimalFormat decimalFormat = new DecimalFormat("0.00"); //构造方法的字符格式这里如果小数不足2位 会以0补足
            string = decimalFormat.format(price);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return "为您节省: " + string + "万";
    }

}
