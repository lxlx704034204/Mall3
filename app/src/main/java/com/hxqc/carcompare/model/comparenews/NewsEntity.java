package com.hxqc.carcompare.model.comparenews;

import java.util.List;

/**
 * 资讯对比
 * Created by zhaofan on 2016/10/24.
 */
public class NewsEntity {

    public String infoID;  //资讯ID string

    public String form_source;  // 来源 10资讯 20 4s店 number

    public String pageType;//页面样式form_source为10的时候判断 10图文混排 20图集 string

    public String newsType;//  新闻类型form_source为20的时候判断 10优惠促销 30新闻资讯 number


    public String title;  //  标题 string

    public String date; //时间 string

    public String introduction;  //简介 string

    public List<Tags> tags;

    public List<String> thumbImage;  // 缩略图url

    public NewsEntity(String infoID, String title) {
        this.infoID = infoID;
        this.title = title;
    }

    private class Tags {

        public String tagName;  //  标签名称 string

        public String tagCode;  //   标签Code string


    }


    public NewsEntity(String infoID, String form_source, String pageType, String newsType, String title, String date, String introduction, List<Tags> tags, List<String> thumbImage) {
        this.infoID = infoID;
        this.form_source = form_source;
        this.pageType = pageType;
        this.newsType = newsType;
        this.title = title;
        this.date = date;
        this.introduction = introduction;
        this.tags = tags;
        this.thumbImage = thumbImage;
    }


    @Override
    public String toString() {
        return "NewsEntity{" +
                "infoID='" + infoID + '\'' +
                ", form_source='" + form_source + '\'' +
                ", pageType='" + pageType + '\'' +
                ", newsType='" + newsType + '\'' +
                ", title='" + title + '\'' +
                ", date='" + date + '\'' +
                ", introduction='" + introduction + '\'' +
                ", tags=" + tags +
                ", thumbImage=" + thumbImage +
                '}';
    }
}
