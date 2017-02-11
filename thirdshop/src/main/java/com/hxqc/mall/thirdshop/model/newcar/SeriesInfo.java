package com.hxqc.mall.thirdshop.model.newcar;

import com.hxqc.mall.thirdshop.model.ColorInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhaofan
 */
public class SeriesInfo {

    /**
     * brand : 奥迪(进口)
     * parameter : [{"value":[""],"label":"发动机"},{"value":[""],"label":"车身结构"},{"value":[""],"label":"变速箱"}]
     * appearance : [{"color":"#341D0D","colorDescription":"柚木棕"},{"color":"#FFFFFF","colorDescription":"冰川白"},{"color":"#003362","colorDescription":"思科巴蓝"},{"color":"#CD002B","colorDescription":"米萨诺红"},{"color":"#000000","colorDescription":"旋风黑"},{"color":"#B3C6CC","colorDescription":"积云蓝"},{"color":"#55272A","colorDescription":"西拉红"},{"color":"#2F3032","colorDescription":"天云灰"},{"color":"#070A01","colorDescription":"幻影黑"},{"color":"#D37201","colorDescription":"萨摩亚橙"},{"color":"#455B69","colorDescription":"星球蓝"},{"color":"#FFF0F0","colorDescription":"阿玛菲白"},{"color":"#D7D8DA","colorDescription":"水晶银"}]
     * priceRange : 158000.00-299800.00
     * seriesName : 奥迪A1
     * itemPic : {"url":""}
     * itemOrigPrice : 199800.00-299800.00
     */

    private String brand;
    private String priceRange;
    private String seriesName;
    /**
     * url :
     */

    private String itemPic;
    private String itemOrigPrice;
    /**
     * value : [""]
     * label : 发动机
     */

    private List<ParameterInfo> parameter;
    /**
     * color : #341D0D
     * colorDescription : 柚木棕
     */

    private ArrayList<ColorInfo> appearance;

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getPriceRange() {
        return priceRange;
    }

    public void setPriceRange(String priceRange) {
        this.priceRange = priceRange;
    }

    public String getSeriesName() {
        return seriesName;
    }

    public void setSeriesName(String seriesName) {
        this.seriesName = seriesName;
    }

    public String getItemPic() {
        return itemPic;
    }

    public void setItemPic(String itemPic) {
        this.itemPic = itemPic;
    }

    public String getItemOrigPrice() {
        return itemOrigPrice;
    }

    public void setItemOrigPrice(String itemOrigPrice) {
        this.itemOrigPrice = itemOrigPrice;
    }

    public List<ParameterInfo> getParameter() {
        return parameter;
    }

    public void setParameter(List<ParameterInfo> parameter) {
        this.parameter = parameter;
    }

    public ArrayList<ColorInfo> getAppearance() {
        return appearance;
    }

    public void setAppearance(ArrayList<ColorInfo> appearance) {
        this.appearance = appearance;
    }

    public static class ItemPicBean {
        private String url;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }


}
