package com.hxqc.mall.core.model;



import java.util.ArrayList;

/**
 * 说明:保险（首页）
 * author: liaoguilong
 * since: 2015年10月18日10:42:50
 * Copyright:恒信汽车电子商务有限公司
 */
public class InsuranceForHome {
    public ArrayList<cooperatorMode> cooperator;

    public ArrayList<insuranceMode> insurance;

    /**
     * 合作的金融机构
     */
    public  class cooperatorMode {
        public String cooperationID; //机构ID
        public String logo; //机构logo
    }

    /**
     * 合作的金融机构
     */
    public  class insuranceMode {
        public String insuranceID; //保险公司ID
        public String logo; //保险公司logo
        public String text1; //保险公司介绍
        public String text2; //保险特点
        public String url; //对应HTML5页面的URL
    }
}
