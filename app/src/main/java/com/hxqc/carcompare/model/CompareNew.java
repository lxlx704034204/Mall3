package com.hxqc.carcompare.model;

/**
 * Created by zhaofan on 2016/10/9.
 */
public class CompareNew {
    public String groupTag;
    public String label;
    public String value;
    public int def;
    //资讯
    public String newsID;
    //评论
    public String userName;
    public String publishDate;

    public CompareNew(String groupTag, String label, String value, int def) {
        this.groupTag = groupTag;
        this.label = label;
        this.value = value;
        this.def = def;
    }

    public CompareNew(String groupTag, String label, String value, String newsID) {
        this.groupTag = groupTag;
        this.label = label;
        this.value = value;
        this.newsID = newsID;
    }

    public CompareNew(String groupTag, String label, String value, String userName, String publishDate) {
        this.groupTag = groupTag;
        this.label = label;
        this.value = value;
        this.userName = userName;
        this.publishDate = publishDate;
    }

    public String getGroupTag() {
        return groupTag;
    }

    public void setGroupTag(String groupTag) {
        this.groupTag = groupTag;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "CompareNew{" +
                "groupTag='" + groupTag + '\'' +
                ", label='" + label + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
