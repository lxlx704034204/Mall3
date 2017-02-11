package com.hxqc.mall.reactnative.util;

import com.hxqc.mall.config.application.SampleApplicationContext;

/**
 * Author:  wh
 * Date:  2016/8/16
 * FIXME
 * Todo   rn 相关配置信息
 */
public class RNConfigUtil {
    public static boolean isRNDebug = false;

    //MD5验证    URL
//    public static  String md5VerdifyURL = RNBsdiffURL +"/apis/RNVersion";
    //全量更新下载  URL
//    public static final String fullUpdateURL = "";


    //资源文件里面的 加密的bundle文件名
    public static final String assetPathName = "rn/home.android.js";
    //文件存储地址
    public static final String path = SampleApplicationContext.application.getFilesDir().getAbsolutePath();
    //文件存储地址下 保存的本地加密的bundle文件名
    public static final String localEncodeFileName = "home.android.js";
    //解密后可引用的 bundle文件名
    public static final String saveName = "home.js";
    //加载bundle对应的项目key
    public static final String RNConnectJSKey = "MallHome1";
    //下载zip文件
    public static final String saveNameZip = "mallHomeJS.zip";
    //zip中更新补丁文件夹名字
    public static final String innerZipPatchFileName = "jsUpdatePatch";
    //最终加载bundle的地址路径
    public static final String  finalLoadBundleFilePath = path+"/"+saveName;

    //test  String

    public static final String updatePatchFileName = "updatePatch.js";
    public static final String newLocalFileName = "newHome.android.js";
}
