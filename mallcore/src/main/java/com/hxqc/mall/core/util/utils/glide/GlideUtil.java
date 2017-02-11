//package com.hxqc.mall.core.util.utils.glide;
//
//import android.content.Context;
//import android.widget.ImageView;
//
//import com.bumptech.glide.DrawableRequestBuilder;
//import com.bumptech.glide.Glide;
//import com.bumptech.glide.load.engine.DiskCacheStrategy;
//import com.bumptech.glide.request.RequestListener;
//
///**
// * Created by zhaofan on 2016/12/13.
// */
//public class GlideUtil {
//    public DrawableRequestBuilder mBuilder;
//    public static volatile GlideUtil instance;
//
//    private GlideUtil() {
//    }
//
//    private static class SingletonHolder {
//        private static final GlideUtil INSTANCE = new GlideUtil();
//    }
//
//    public static GlideUtil getInstance() {
//        return SingletonHolder.INSTANCE;
//    }
//
//    public static <T> DrawableRequestBuilder<T> init(Context context, T url, int error_resid) {
//        return Glide.with(context)
//                .load(url)
//                .placeholder(error_resid)
//                .error(error_resid)
//                .diskCacheStrategy(DiskCacheStrategy.ALL);
//    }
//
//    public <T> GlideUtil build(Context context, T url, int error_resid) {
//        mBuilder = init(context, url, error_resid);
//        return this;
//    }
//
//    public <T> GlideUtil build(Context context, T url) {
//        mBuilder = init(context, url, com.common.R.drawable.pic_normal);
//        return this;
//    }
//
//    public void into(ImageView view) {
//        mBuilder.into(view);
//    }
//
//    public GlideUtil setListener(RequestListener requestListener) {
//        mBuilder.listener(requestListener);
//        return this;
//    }
//
// /*   public static <T> DrawableRequestBuilder<T> scaleType(DrawableRequestBuilder<T> build) {
//        return build.fitCenter();
//    }*/
//
//    public GlideUtil scaleType(ScaleType type) {
//        if (type == ScaleType.fitCenter)
//            mBuilder.fitCenter();
//        else if (type == ScaleType.centerCrop)
//            mBuilder.centerCrop();
//        return this;
//    }
//
//
//    public enum ScaleType {
//        fitCenter,
//        centerCrop,
//    }
//
//
//}
