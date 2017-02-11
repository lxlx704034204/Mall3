package com.hxqc.mall.core.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestListener;
import com.hxqc.mall.core.R;
import com.hxqc.util.DebugLog;

import java.io.File;

/**
 * Created 胡俊杰
 * 2016/10/21.
 * Todo:
 */

public class ImageUtil {



    private static <T> DrawableRequestBuilder<T> initDefaultBuild(Context mC, T URL, int def_img_res_id, int err_img_res_id) {
        return Glide.with(mC)
                .load(URL)
                .placeholder(def_img_res_id)
                .error(err_img_res_id)
                .diskCacheStrategy(DiskCacheStrategy.ALL);
    }

//    private static <T> DrawableRequestBuilder<T> initDefaultBuildWithCache(Context mC, T URL, int def_img_res_id, int err_img_res_id) {
//        return Glide.with(mC)
//                .load(URL)
//                .placeholder(def_img_res_id)
//                .error(err_img_res_id)
//                .diskCacheStrategy(DiskCacheStrategy.ALL);
//    }

    /**
     * 读图
     *
     * @param context
     * @param imageView 图片控件
     * @param url       图片地址
     * @return R.drawable.pic_normal
     */
    public static <T> void setImage(Context context, ImageView imageView, T url) {
        DebugLog.i("img_T","setImage1 url: "+url.toString());
        initDefaultBuild(context, url, R.drawable.pic_normal, R.drawable.pic_normal).fitCenter().dontAnimate().into(imageView);
    }

    /**
     * 读图
     *
     * @param context
     * @param imageView 图片控件
     * @param url       图片地址
     * @param resId     占位图
     * @return
     */
    public static <T> void setImage(Context context, ImageView imageView, T url, int resId) {
        DebugLog.i("img_T","setImage2 url: "+url.toString());
        initDefaultBuild(context, url, resId, resId).fitCenter().dontAnimate().into(imageView);
    }

    public static <T> void setImageCenterCrop(Context context, ImageView imageView, T url) {
        DebugLog.i("img_T","setImageCenterCrop url: "+url.toString());
        initDefaultBuild(context, url, R.drawable.pic_normal, R.drawable.pic_normal).centerCrop().dontAnimate().into(imageView);
    }

    public static <T> void setImageFitCrop(Context context, ImageView imageView, T url) {
        initDefaultBuild(context, url, R.drawable.pic_normal, R.drawable.pic_normal).fitCenter().dontAnimate().into(imageView);
    }

    /**
     * 读图
     *
     * @param context
     * @param imageView 图片控件
     * @param url       图片地址
     * @return R.drawable.pic_normal_square
     */
    public static <T> void setImageSquare(Context context, ImageView imageView, T url) {
        initDefaultBuild(context, url, R.drawable.pic_normal_square, R.drawable.pic_normal_square).crossFade().into(imageView);
    }

    /**
     * 读图
     *
     * @param context
     * @param imageView 图片控件
     * @param url       图片地址
     * @return R.drawable.ic_question_mark
     */
    public static <T> void setImageSquareOfAutoInfo(Context context, ImageView imageView, T url) {
        initDefaultBuild(context, url, R.drawable.ic_question_mark, R.drawable.ic_question_mark).crossFade().into(imageView);
    }

    @SuppressWarnings("unchecked")
    public static <T> void setImageCallBack(T url, ImageView img, RequestListener listener) {
        initDefaultBuild(img.getContext(), url, R.drawable.pic_normal, R.drawable.pic_normal).listener(listener).into(img);
    }

    public static <T> void setImageResize(Context context, ImageView imageView, T url, int width, int height) {
        initDefaultBuild(context, url, R.drawable.pic_normal, R.drawable.pic_normal)
                .override(width, height)
                .centerCrop()
                .into(imageView);
    }


    public static <T> void setImageNormalSize(Context context, ImageView imageView, T url) {
        initDefaultBuild(context, url, R.drawable.pic_normal, R.drawable.pic_normal).dontAnimate().into(imageView);
    }

    /**
     * @param context
     * @param imageView
     * @param url       R.drawable.pic_normal_logo
     */
    public static <T> void setImageSmall(Context context, ImageView imageView, T url) {
        initDefaultBuild(context, url, R.drawable.pic_normal_logo, R.drawable.pic_normal_logo).dontAnimate().into(imageView);
    }

    /**
     * 违章查缴详情读图
     *
     * @param context
     * @param imageView 图片控件
     * @param url       图片地址
     * @param resId     占位图
     */
    public static <T>  void setLargeImage(Context context, ImageView imageView, T url, int resId, int errorPic) {
        initDefaultBuild(context, url, resId, errorPic).crossFade().into(imageView);
    }

    /**
     * 剪裁图片
     *
     * @return RequestCreator
     */
    public static void startPhotoZoom(Uri uri, int outputX, int outputY, int aspectX, int aspectY, int requestCode, String photoPath, Activity activity) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("scale", true);
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", aspectX);
        intent.putExtra("aspectY", aspectY);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", outputX);
        intent.putExtra("outputY", outputY);
        if (TextUtils.isEmpty(photoPath)) {
            throw new NullPointerException("crop path is null");
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(photoPath)));
        intent.putExtra("return-data", false);
        activity.startActivityForResult(intent, requestCode);
    }

}
