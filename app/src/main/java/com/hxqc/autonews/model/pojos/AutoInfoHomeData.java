package com.hxqc.autonews.model.pojos;

import java.util.ArrayList;

/**
 * Author:李烽
 * Date:2016-09-01
 * FIXME
 * Todo AutoInformationApiClient.requestAutoInfomation()返回数据
 */
public class AutoInfoHomeData {
    public ArrayList<AutoInformation>banner;
    public ArrayList<AutoInformation>infoList;
    public AutoInfoHomeData(){
        banner=new ArrayList<>();
        infoList=new ArrayList<>();
    }
}
