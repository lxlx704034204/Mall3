package com.hxqc.mall.reactnative.model;

/**
 * Author:  wh
 * Date:  2016/8/18
 * FIXME
 * Todo
 */
public class BsdiffPatchResponseModel {
    public String code;
    public String message;
    public String patchMD5;//网络补丁包md5值
    public String bundleMD5;//网络完整包md5值
    public String patchFileURL;//增量更新地址
    public String wholeFileURL;//全量更新地址
}
