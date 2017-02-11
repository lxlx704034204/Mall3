package com.hxqc.util;

import android.content.Context;
import android.graphics.Bitmap;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class FileUtil {

    /**
     * 创建新文件,如果存在就删除文件,如果不存在就创建文件
     *
     * @param filepath
     * @return
     */
    public static boolean makeFileOver(String filepath) {
        File file = new File(filepath);
        if (file.exists()) {
            boolean newFile = file.delete();
        } else {
            File fileParent = new File(file.getParent());
            try {
                boolean newFile = fileParent.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file.exists();
    }

    /**
     * 判断文件是否存在
     *
     * @param filepath
     * @return
     */
    public static boolean isFileExist(String filepath) {
        File file = new File(filepath);
        return file.exists();
    }

    /**
     * 根据URL得到输入流
     *
     * @param urlStr
     * @return
     * @throws java.net.MalformedURLException
     * @throws java.io.IOException
     */

    public static InputStream getInputStreamFromUrl(String urlStr)
            throws IOException {
        URL url = new URL(urlStr);
        HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
        return urlConn.getInputStream();
    }

    /**
     * 按照给出的路径读取文件内容
     *
     * @param str_filepath
     * @return
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static synchronized String convertCodeAndGetText(String str_filepath) {
        File file = new File(str_filepath);
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

    /**
     * 简单写入
     * @param path
     * @param content
     */
    public static void fileWriteout(String path,String content) {
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

    /**
     * 写文件
     *
     * @param context
     * @param dir
     * @param string
     */
    public static synchronized void writerFile(Context context, String dir,
                                               String string) {
        File file = new File(dir);
        File path1 = file.getParentFile();

        if (!android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED)) {
            return;
        }
        if (!path1.exists()) {
            // 路径不存在? Just 创建
            path1.mkdirs();
        }
        if (!file.exists()) {
            // 文件不存在、 Just创建
            try {
                file.createNewFile();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        OutputStreamWriter osw;
        try {
            osw = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
            osw.write("\n " + string);
            osw.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 保存图片
     *
     * @param strPath
     * @param patamBitmap
     * @return
     */
    public static boolean savaQualityBitmap(String strPath, Bitmap patamBitmap,
                                            int quality) {

        makeFileDirs(strPath);
        File localFile = new File(strPath);
        try {
            FileOutputStream localFileOutputStream = new FileOutputStream(
                    localFile);
            patamBitmap.compress(Bitmap.CompressFormat.JPEG, quality,
                    localFileOutputStream);
            localFileOutputStream.flush();
            localFileOutputStream.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return true;
    }

    /**
     * 创建新文件,如果存在就不创建
     *
     * @param filepath
     * @return
     */
    public static boolean makeFileDirs(String filepath) {
        File file = new File(filepath);
        File fileparent = new File(file.getParent());
        if (!fileparent.exists()) {
            fileparent.mkdirs();
        }

        return fileparent.exists();
    }

    public static void saveQualityBitmap(String strPath, Bitmap bitmap, int quality) {
        File localFile = new File(strPath);
        FileOutputStream localFileOutputStream;
        try {
            localFileOutputStream = new FileOutputStream(
                    localFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality,
                    localFileOutputStream);
            localFileOutputStream.flush();
            localFileOutputStream.close();
            bitmap.recycle();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
