package com.hxqc.autonews.model.pojos;

import com.google.gson.annotations.Expose;
import com.hxqc.socialshare.pojo.ShareContent;

import java.util.ArrayList;

/**
 * Author:李烽
 * Date:2016-09-01
 * FIXME
 * Todo 汽车资讯详情数据
 */
public class AutoInfoDetail {
    @Expose
    public ShareContent share;
    @Expose
    public String pageType = "";//10图文混排 20图集
    @Expose
    public String title;
    @Expose
    public String body;
    @Expose
    public String sourceForm;
    @Expose
    public String date;
    @Expose
    public ArrayList<AutoInfoData> data;
    @Expose
    public ArrayList<AutoImage> images;
    @Expose
    public ArrayList<String> bodyImg;
    @Expose
    public ArrayList<AutoInformation> relevant;

    @Expose
    public long commentCount;//评论条数

    public PageType getPageType() {
        if (pageType.equals("10"))
            return PageType.textAndImage;
        else if (pageType.equals("20"))
            return PageType.Images;
        else return null;
    }

    public enum PageType {
        Images, textAndImage
    }
}
