package com.hxqc.mall.core.util;


import com.hxqc.util.FileUtil;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * Author: wanghao
 * Date: 2015-03-25
 * FIXME
 * 存储 读取 省市区
 */
public class MyFileUtil extends FileUtil {


    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void saveText(File file, String text) {
        File path1 = file.getParentFile();

        if (!path1.exists()) {
            // 路径不存在? Just 创建
            path1.mkdirs();
        }
        if (!file.exists()) {
            // 文件不存在、 Just创建
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        OutputStreamWriter osw;
        try {
            osw = new OutputStreamWriter(new FileOutputStream(file), "GBK");
            osw.write("\n " + text);
            osw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static String getText(File file) {
        BufferedReader reader;
        String text = "";
        try {
            FileInputStream fis = new FileInputStream(file);
            BufferedInputStream in = new BufferedInputStream(fis);
            in.mark(4);
            byte[] first3bytes = new byte[3];
            in.read(first3bytes);
            in.reset();
            if (first3bytes[0] == (byte) 0xEF && first3bytes[1] == (byte) 0xBB
                    && first3bytes[2] == (byte) 0xBF) {
                reader = new BufferedReader(new InputStreamReader(in, "utf-8"));

            } else if (first3bytes[0] == (byte) 0xFF
                    && first3bytes[1] == (byte) 0xFE) {

                reader = new BufferedReader(
                        new InputStreamReader(in, "unicode"));
            } else if (first3bytes[0] == (byte) 0xFE
                    && first3bytes[1] == (byte) 0xFF) {

                reader = new BufferedReader(new InputStreamReader(in,
                        "utf-16be"));
            } else if (first3bytes[0] == (byte) 0xFF
                    && first3bytes[1] == (byte) 0xFF) {

                reader = new BufferedReader(new InputStreamReader(in,
                        "utf-16le"));
            } else {

                reader = new BufferedReader(new InputStreamReader(in, "GBK"));
            }
            String str = reader.readLine();

            while (str != null) {
                text = text + str;
                str = reader.readLine();
            }
            reader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }


        return text;
    }
}
