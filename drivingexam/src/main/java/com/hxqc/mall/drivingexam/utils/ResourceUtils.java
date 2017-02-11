package com.hxqc.mall.drivingexam.utils;


import android.content.Context;

/**
 * Created by zhaofan on 2016/8/24.
 */
public class ResourceUtils {

    /**
     * @param fileName
     * @param fileType "raw" "drawable"
     * @return
     */
    public static int getResourceId(Context context,String fileName, String fileType) {
        return context.getResources().getIdentifier(fileName, fileType,
                context.getPackageName());
    }
}
