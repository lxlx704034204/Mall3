package com.hxqc.mall.core.bsdiff;

import android.content.Context;

import com.hxqc.util.DebugLog;

import org.apache.http.util.EncodingUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Author:  wh
 * Date:  2016/8/15
 * FIXME
 * Todo
 */
public class BSFileUtil {

    /**
     * 判断文件是否存在
     */
    public static boolean isFileExist(String fileWholePath) {
        boolean iiii = false;
        File file = new File(fileWholePath);
        if (file.exists()) {
            iiii = true;
        }
        return iiii;
    }

    /**
     * 删除文件
     */
    public static void deleteExistsFile(String wholePath) {
        File file = new File(wholePath);
        // 路径为文件且不为空则进行删除
        if (file.isFile() && file.exists()) {
            file.delete();
        }
    }

    /**
     * 吧asset中的文件存到file下
     */
    public static void saveFileFromAsset(Context context, String assetName, String savePath) {
        DebugLog.i("rn_test", "saveFileFromAsset " + savePath);
        fileWriteout(savePath, readFileFromAssets(context, assetName));
    }

    public static void saveFileFAsset(Context context, String assetName, String savePath) {
        try {
            InputStream myInput;
            OutputStream myOutput = new FileOutputStream(savePath);
            myInput = context.getAssets().open(assetName);
            byte[] buffer = new byte[1024];
            int length = myInput.read(buffer);
            while (length > 0) {
                myOutput.write(buffer, 0, length);
                length = myInput.read(buffer);
            }
            myOutput.flush();
            myInput.close();
            myOutput.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 简单写入
     *
     * @param path
     * @param content
     */
    public static void fileWriteout(String path, String content) {
        File f = new File(path);// 新建一个文件对象
        FileWriter fw;
        try {
            fw = new FileWriter(f);// 新建一个FileWriter
            fw.write(content);// 将字符串写入到指定的路径下的文件中
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String readFileFromAssets(Context ctx, String assetName) {
        String res = "";
        try {
            //得到资源中的asset数据流
            InputStream in = ctx.getResources().getAssets().open(assetName);
            int length = in.available();
            byte[] buffer = new byte[length];

            in.read(buffer);
            in.close();
            res = EncodingUtils.getString(buffer, "UTF-8");
            return res;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }
}
