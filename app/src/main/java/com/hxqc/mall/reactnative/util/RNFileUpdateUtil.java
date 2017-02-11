package com.hxqc.mall.reactnative.util;

import android.content.Context;

import com.hxqc.mall.core.bsdiff.BSFileUtil;
import com.hxqc.mall.core.bzinga.DES3;
import com.hxqc.util.DebugLog;
import com.hxqc.util.FileUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * Author: wanghao
 * Date: 2016-03-14
 * FIXME
 * Todo
 */
public class RNFileUpdateUtil extends BSFileUtil{


    /**
     * 保存文件
     */
    public static void saveFileZip(File file,String savePath){
        try {
            FileOutputStream fos = new FileOutputStream(savePath);
            FileInputStream fis = new FileInputStream(file);
            byte[] buf = new byte[1024];
            int len = 0;
            while ((len = fis.read(buf)) > 0) {
                fos.write(buf, 0, len);
            }
            if (fis != null)
                fis.close();
            if (fos != null)
                fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 保存文件
     * @param newsRootPath  新建文件地址
     * @param filename 存储文件名
     * @param jsFile
     */
    public static void saveFile(String newsRootPath, String filename, File jsFile) {
        try {
            File newsFileRoot = new File(newsRootPath);
            if (!newsFileRoot.exists()) {
                newsFileRoot.mkdirs();
            }

            FileOutputStream fos = new FileOutputStream(newsRootPath + filename);
            FileInputStream fis = new FileInputStream(jsFile);
            byte[] buf = new byte[1024];
            int len = 0;
            while ((len = fis.read(buf)) > 0) {
                fos.write(buf, 0, len);
            }
            if (fis != null)
                fis.close();
            if (fos != null)
                fos.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 从本地文件中解密并保存-----------------------------------------------------------------------------------------------
     */
    public static void localLoadDeCodeAndSave(Context ctx,String fileWholePath, String saveWholePath) {
        String fileStr = FileUtil.convertCodeAndGetText(fileWholePath);
        try {
            DebugLog.i("rn_test", "deCode");
            FileUtil.writerFile(ctx, saveWholePath, DES3.decode(fileStr));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
