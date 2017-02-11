package com.hxqc.mall.thirdshop.maintenance.model.maintenance;

import java.util.ArrayList;

/**
 * @Author : 钟学东
 * @Since : 2016-04-22
 * FIXME
 * Todo
 */
public class MaintenanceItemN  {

    public String name; //项目名称
    public String itemGroupID; //项目类型ID string
    public String itemID; //项目ID
    public String amount; //项目总计
    public String workCost; //工时费
    public float discount; //优惠价(项目自身优惠价) number (float)
    public String deduction; //可使用优惠卷或者积分抵扣金额 number
    public String descriptionUrl; //项目介绍URL string
    public String summary; //保养项目介绍摘要 string
    public String mutualExclusionGroup; //互斥组ID，用于判断是否有项目互斥 相同ID表示互斥 string
    public long revisability;//	是否可以修改 默认 1可修改
    public ArrayList<MaintenanceGoods> goods = new ArrayList<>();
    public int choose; //用于4s点首页  是否选中 boolean
    public boolean maintainStatus; //该项目是否维修过 boolean
    public int deleteable ; //是否可以删除 默认1为可以删除 ，0为不可删除
    public String bothGroup;  //同时选中组ID,多同组选中以逗号分隔，逻辑类似互斥组ID string

    public boolean isPackage = false;
    public boolean isCheck = false;

    //用于判断onActivityResult 返回的item是不是套餐
    public boolean isInPackage = false;
    //用于判断是不是在返回的list中
    public boolean isInReturn = false;
    public boolean isPlatformRecommend = false; //是否为平台推荐

    public float selectGoodPrice ; // 用于第四版保养4s店流程 方便计算价格 这个价格是所有的商品价格之和（第四版之后4s店一个项目就是一个商品 然而后台有时候还是会以项目的形式返回 所以才说所有之和）
    public ArrayList<String> selectGoodID = new ArrayList<>(); //用于第四版保养4s店流程 方便得到上传参数 (同价格)
    public int selectCount; //用于第四版保养4s店流程 方便得到商品的数量
}
