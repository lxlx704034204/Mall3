package com.hxqc.tinker.model;

/**
 * Author:  wh
 * Date:  2016/12/29
 * FIXME
 * Todo
 */

public class AppPatchItemModel {
    public String version;
    public String url;
    public String md5;

    @Override
    public String toString() {
        return "AppPatchItemModel{" +
                "version='" + version + '\'' +
                ", url='" + url + '\'' +
                ", md5='" + md5 + '\'' +
                '}';
    }
}
