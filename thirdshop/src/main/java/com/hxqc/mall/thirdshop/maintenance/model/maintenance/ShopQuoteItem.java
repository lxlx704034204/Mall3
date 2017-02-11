package com.hxqc.mall.thirdshop.maintenance.model.maintenance;

/**
 * @Author : 钟学东
 * @Since : 2016-06-22
 * FIXME
 * Todo
 */
public class ShopQuoteItem  {
    public String name;
    public String itemGroupID;
    public String descriptionUrl;
    public int choose; // 1 选中  0 非选中
    public String itemPic; //项目示例图 string
    public String mutualExclusionGroup; //"互斥组ID，多互斥以逗号分隔。 例如情况1:项目AA和项目BB互斥，则项目AA和项目BB均返回“AABB”，
    // 情况2:项目AA和项目BB互斥，AA和CC互斥,BB和CC不互斥，项目AA返回“AABB,AACC”，项目BB返回\"AABB\",项目CC返回\"AACC\" " string
    public boolean isPlatformRecommend = false; //是否为平台推荐
    public int  mustSelect4S;
    public String bothGroup ; //同时选中组ID,多同组选中以逗号分隔，逻辑类似互斥组ID string
}
