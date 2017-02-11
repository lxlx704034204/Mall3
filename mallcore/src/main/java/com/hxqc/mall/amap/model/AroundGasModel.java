package com.hxqc.mall.amap.model;

/**
 * Author: wanghao
 * Date: 2016-04-08
 * FIXME
 * Todo   加油站查询 返回结果
 */
public class AroundGasModel {

    //全国性
    final public String countryURL = "http://apis.haoservice.com/oil/region?city=%E6%AD%A6%E6%B1%89&page=1&key=e65d5652da974ef7a3702d753598b6b4";
    //周边
    final public String aroundURL = "http://apis.haoservice.com/oil/local?lon=116.403119&lat=39.916042&r=3000&page=1&key=e65d5652da974ef7a3702d753598b6b4";


    public int error_code;
    public String reason;
    public GasResultModel result;
}
