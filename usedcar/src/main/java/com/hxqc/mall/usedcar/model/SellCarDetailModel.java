package com.hxqc.mall.usedcar.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @Author : 钟学东
 * @Since : 2015-10-13
 * FIXME
 * Todo
 */
public class SellCarDetailModel implements Serializable {
    /**
     * model : string,车型
     * sali_date : string,交强险到期时间
     * purchase : string,购置税
     * addserie : string,自定义车系
     * license2 : string,购车发票缩略图地址
     * car_source_no : string,车源号
     * license1 : string,登记证照片缩略图地址
     * car_mileage : string,里程
     * serie_id : string,车系id
     * city : string,所在市
     * contacts : string,联系人
     * warranty_date : string,质保到期时间
     * cover : string,封面（文件名）
     * addmodel : string,自定义车型
     * province : string,所在省
     * license : string,行驶证照片缩略图地址
     * location_province : string,看车省
     * insurance_date : string,商业保险到期时间
     * first_on_card : string,上牌时间
     * brand_id : string,品牌id
     * image : [{"name":"string,图片名","small_path":"string,缩略图地址"}]
     * location_region : string,看车区
     * location_city : string,看车市
     * serie : string,车系
     * inspection_date : string,年检有效期
     * estimate_price : string,价格
     * phone_num : string,联系人电话
     * car_color : string,颜色：汉字
     * model_id : string,车型id
     * addbrand : string,自定义品牌
     * brand : string,品牌
     * owners : string,自述
     */

    public String model;
    public String level;
    public String level_editable;
    public String gearbox;
    public String gearbox_editable;
    public String displacement;
    public String displacement_editable;
    public String sali_date;
    public String purchase;
    public String addserie;
    public String car_source_no;
    public String car_mileage;
    public String serie_id;
    public String city;
    public String city_id;
    public String contacts;
    public String warranty_date;
    public String cover;
    public String addmodel;
    public String province;
    public String province_id;
    public String license;
    public String license1;
    public String license2;
    public String location_province;
    public String location_province_id;
    public String insurance_date;
    public String first_on_card;
    public String brand_id;
    public String location_region;
    public String location_city;
    public String location_region_id;
    public String location_city_id;
    public String serie;
    public String inspection_date;
    public String estimate_price;
    public String phone_num;
    public String car_color;
    public String model_id;
    public String addbrand;
    public String brand;
    public String owners;
    public String new_car_price;
    public String new_car_price_editable;
    public String car_license_no;
    public ArrayList<ImageEntity> image;

    public static class ImageEntity implements Serializable {
        public String getPath() {
            return path;
        }

        /**
         * name : string,图片名
         * small_path : string,缩略图地址
         */
        private String path;

        public void setPath(String path) {
            this.path = path;
        }

        private String name;
        private String small_path;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSmall_path() {
            return small_path;
        }

        public void setSmall_path(String small_path) {
            this.small_path = small_path;
        }
    }


}