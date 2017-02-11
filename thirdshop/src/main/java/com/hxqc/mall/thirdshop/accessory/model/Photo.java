package com.hxqc.mall.thirdshop.accessory.model;

/**
 * 照片
 * Created by huangyi on 16/2/25.
 */
public class Photo {

    public String thumbnailPath; //缩略图
    public String sourcePath; //大图

    public Photo(String thumbnailPath, String sourcePath) {
        this.thumbnailPath = thumbnailPath;
        this.sourcePath = sourcePath;
    }
}
