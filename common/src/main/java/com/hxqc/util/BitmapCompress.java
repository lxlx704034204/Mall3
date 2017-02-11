package com.hxqc.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.view.View;
import android.view.animation.Transformation;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 图片压缩
 * <p>
 * author Administrator
 */

public class BitmapCompress {
    public static Bitmap compressImageQuality(Bitmap image, int maxByteCount, int qualityReduceDegree,
                                              Bitmap.CompressFormat formate, boolean filter) {
        if (qualityReduceDegree > 10) {
            qualityReduceDegree = 10;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int quality = 100;
        image.compress(formate, quality, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中

        // 循环判断是否已经压缩到指定的大小
        while (baos.toByteArray().length > maxByteCount) {
            quality -= qualityReduceDegree;
            baos.reset();// 重置baos即清空baos
            image.compress(formate, quality, baos);// 这里压缩options%，把压缩后的数据存放到baos中
        }
        if (filter) {
            image.recycle();
        }
        return BitmapFactory.decodeByteArray(baos.toByteArray(), 0, baos.toByteArray().length);
    }

    public static Bitmap compressJPEGImageQuality(Bitmap image, int maxByteCount, int qualityReduceDegree) {
        return BitmapCompress.compressImageQuality(image, maxByteCount, qualityReduceDegree,
                Bitmap.CompressFormat.JPEG, true);
    }

    /**
     * 修改图片尺寸
     *
     * @param image
     * @param specifyWidth
     * @param specifyHeight
     * @return
     */
    public static Bitmap compressImageToSpecifySize(Bitmap image, int specifyWidth, int specifyHeight) {
        float imageWidth = image.getWidth();
        float imageHeight = image.getHeight();
        float scale = 1;
        if (imageWidth > specifyWidth || imageHeight > specifyHeight) {
            float scaleWidth = specifyWidth / imageWidth;
            float scaleHeight = specifyHeight / imageHeight;
            scale = scaleWidth < scaleHeight ? scaleWidth : scaleHeight;
        }

        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);
        return Bitmap.createBitmap(image, 0, 0, (int) imageWidth, (int) imageHeight, matrix, true);
    }

    /**
     * 压缩并保存图片
     *
     * @param bitmap
     * @param specifyWidth
     * @param specifyHeight
     * @param tempBitmapPath
     * @param quality
     */
    public synchronized static void compressImageToSpecifySizeAndSaveFile(Bitmap bitmap, int specifyWidth, int specifyHeight, String tempBitmapPath, int quality) {
        Bitmap bitmap1 = compressImageToSpecifySize(bitmap, specifyWidth, specifyHeight);
        FileUtil.saveQualityBitmap(tempBitmapPath, bitmap1, quality);
    }

    public static Bitmap compressImageQualityAndSize(Bitmap image, int maxBytes, int qualityReduceDegree,
                                                     Bitmap.CompressFormat formate, int specifyWidth, int specifyHeight) {
        Bitmap compressQualityImage = BitmapCompress.compressImageToSpecifySize(image, specifyWidth, specifyHeight);
        return BitmapCompress.compressImageQuality(compressQualityImage, maxBytes, qualityReduceDegree, formate, true);
    }

    /**
     * 压缩并保存图片
     *
     * @param bitmap
     * @param specifyWidth
     * @param specifyHeight
     * @param tempBitmapPath
     * @param quality
     */
    public synchronized static void compressImageToSpecifySizeAndSaveFile(Bitmap bitmap, int specifyWidth, int specifyHeight, String tempBitmapPath, int quality, int sourceDegree) {
        Bitmap bitmap1 = compressImageToSpecifySize(bitmap, specifyWidth, specifyHeight);
        if (sourceDegree == 90) {
            bitmap1 = rotateBitmap(bitmap1, 90);
        } else if (sourceDegree == 270) {
            bitmap1 = rotateBitmap(bitmap1, 90);
        }
        FileUtil.saveQualityBitmap(tempBitmapPath, bitmap1, quality);
        int degree = readPictureDegree(tempBitmapPath);
    }

    /**
     * 压缩并保存图片
     *
     * @param sourcePath
     * @param tempBitmapPath
     * @param quality
     * @param sourceDegree
     */
    public synchronized static void compressImageToSpecifySizeAndSaveFile(String sourcePath, String tempBitmapPath, int quality, int sourceDegree) {
        Bitmap smallBitmap = getSmallBitmap(sourcePath);
        if (sourceDegree == 90) {
            smallBitmap = rotateBitmap(smallBitmap, 90);
        } else if (sourceDegree == 270) {
            smallBitmap = rotateBitmap(smallBitmap, 90);
        }
        FileUtil.saveQualityBitmap(tempBitmapPath, smallBitmap, quality);
        int degree = readPictureDegree(tempBitmapPath);
    }

    /**
     * 压缩并保存图片
     *
     * @param filePath
     * @param quality
     */
    public synchronized static String compressImageToSpecifySizeAndSaveFile(final String tempBitmapPath, final String filePath, final int quality) {
        Bitmap smallBitmap = getSmallBitmap(filePath);
        FileUtil.saveQualityBitmap(tempBitmapPath, smallBitmap, quality);

        int degree = readPictureDegree(tempBitmapPath);
        Bitmap bitmap = BitmapFactory.decodeFile(tempBitmapPath);
        Bitmap rotateBitmap = null;
        if (degree != 0) {//旋转照片角度
            rotateBitmap = rotateBitmap(bitmap, degree);
        }
        if (rotateBitmap == null) {
            FileUtil.saveQualityBitmap(tempBitmapPath, bitmap, 100);
        } else {
            FileUtil.saveQualityBitmap(tempBitmapPath, rotateBitmap, 100);
        }
        return tempBitmapPath;
    }

    /**
     * 计算图片的缩放值
     *
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    /**
     * 根据路径获得图片并无损压缩，返回bitmap用于显示
     *
     * @param filePath
     * @return
     */
    public static Bitmap getSmallBitmap(String filePath) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, 480, 800);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(filePath, options);
    }

    /**
     * 判断照片角度
     *
     * @param path
     * @return
     */
    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * 旋转照片
     *
     * @param bitmap
     * @param degress
     * @return
     */
    public static Bitmap rotateBitmap(Bitmap bitmap, int degress) {
        if (bitmap != null) {
            Matrix m = new Matrix();
            m.postRotate(degress);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                    bitmap.getHeight(), m, true);
            return bitmap;
        }
        return bitmap;
    }

    /**
     * 获得图片
     *
     * @param view
     * @return
     */
    public static Bitmap getBitmapFromView(View view) {
        view.destroyDrawingCache();
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
//        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.setDrawingCacheEnabled(true);
        return view.getDrawingCache(true);
    }

    private static Transformation transformationOriginal = null;


    /**
     * @param path
     * @param mScreenWidth
     * @param mScreenHeight
     * @return
     * @throws IOException
     */
    public static Bitmap compressAndGetImgBitmap(String path, int mScreenWidth, int mScreenHeight)
            throws IOException {
        BitmapFactory.Options options = new BitmapFactory.Options();

        options.inTempStorage = new byte[100 * 1024];
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inSampleSize = 8;
        options.inJustDecodeBounds = false;
        options.inPurgeable = true;// 允许可清除
        options.inInputShareable = true;// 以上options的两个属性必须联合使用才会有效果

        InputStream is = new BufferedInputStream(new FileInputStream(
                new File(path)));

        return  BitmapFactory.decodeStream(is, null, options);
    }
}
