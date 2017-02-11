package com.hxqc.mall.thirdshop.model;

import java.util.List;

/**
 * Author:liaoguilong
 * Date 2015-12-01
 * FIXME 
 * 店铺经营车系
 * Todo  车型报价
 */
public class ModelsQuote {
    public String brandName; //品牌名称
    public List<Series> series; //车系
    

    /**
     * 车系
     */
    public static class Series{
        public String  seriesID; //车系ID
        public String seriesThumb="http://10.0.12.223:8082/images/1d/f1/1df1d2422795bde5336455f55d5cbb27_295_165.jpg"; //图片
        public String seriesName="奥迪A3系列"; //车系名称
        public String priceRange="19.00万-29.00万"; //价格区间

        public String getSeriesName() {
            return seriesName+"系列车型";
        }
    }
}
