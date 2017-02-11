package com.hxqc.mall.thirdshop.model;

import java.util.ArrayList;

/**
 * Function: 分站地区Bean
 *
 * @author 袁秉勇
 * @since 2016年05月09日
 *
 * 区域分组 {
 *              areaName:
 *                  分站名称 string
 *              siteID:
 *                  分站ID string
 *              areaGroup:
 *                  []
 *          }
 */
public class AreaCategory {

    public String areaName;

    public String siteID;

    public String pinyin;

    public ArrayList< AreaCategoryCity > areaGroup;
}
