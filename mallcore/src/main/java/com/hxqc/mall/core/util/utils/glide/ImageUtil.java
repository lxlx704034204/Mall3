//package com.hxqc.mall.core.util.utils.glide;
//
//import android.content.Context;
//import android.widget.ImageView;
//
//import com.bumptech.glide.DrawableRequestBuilder;
//import com.bumptech.glide.Glide;
//import com.bumptech.glide.Priority;
//import com.bumptech.glide.load.engine.DiskCacheStrategy;
//import com.bumptech.glide.request.RequestListener;
//import com.hxqc.mall.core.R;
//
///**
// * 图片加载工具类
// * Created by zf
// */
//public class ImageUtil {
//    public static <T> DrawableRequestBuilder<T> build(Context context, T url, int error_resid) {
//
//        return Glide.with(context)
//                .load(url)
//                .placeholder(error_resid)
//                .error(error_resid)
//                .diskCacheStrategy(DiskCacheStrategy.ALL);
//    }
//
//
//    public static <T> DrawableRequestBuilder<T> build(Context context, T url) {
//        return build(context, url, R.drawable.pic_normal);
//    }
//
//
//    public static void load(Object url, ImageView img, RequestListener listener) {
//        Glide.with(img.getContext())
//                .load(url)
//                .placeholder(R.drawable.pic_normal)
//                .error(R.drawable.pic_normal)
//                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .listener(listener)
//                .into(img);
//    }
//
//
//    public static void loadImg(Object url, ImageView v, int error_resid) {
//        Glide.with(v.getContext())
//                .load(url)
//                .placeholder(error_resid)
//                .error(error_resid)
//                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .into(v);
//
//    }
//
//    public static void loadImg(Object url, ImageView v) {
//        loadImg(url, v, R.drawable.pic_normal);
//    }
//
//
//    public static void loadCircleImg(Object url, ImageView v) {
//        Glide.with(v.getContext())
//                .load(url)
//                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .transform(new GlideCircleTransform(v.getContext()))
//                .error(R.drawable.pic_normal)
//                .into(v);
//    }
//
//
//    public static void loadRoundImg(Object url, ImageView v, int angle) {
//        Glide.with(v.getContext())
//                .load(url)
//                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .transform(new GlideRoundTransform(v.getContext(), angle))
//                .error(R.drawable.pic_normal)
//                .into(v);
//    }
//
//
//    public static void loadPriority(Object highUrl, Object lowUrl, ImageView v) {
//        //优先加载
//        Glide
//                .with(v.getContext())
//                .load(highUrl)
//                .priority(Priority.HIGH)
//                .into(v);
//        //后加载
//        Glide
//                .with(v.getContext())
//                .load(lowUrl)
//                .priority(Priority.LOW)
//                .into(v);
//    }
//
//    //用原图的2/10作为缩略图
//    public static void loadThumbnail(Object url, ImageView v) {
//        Glide.with(v.getContext())
//                .load(url)
//                .thumbnail(0.2f)
//                .into(v);
//    }
//
//    //其他图作为缩略图
//    public static void loadThumbnail(Object mainUrl, Object thumbnailUrl, ImageView v) {
//        //优先加载
//        DrawableRequestBuilder<Object> thumbnailRequest = Glide
//                .with(v.getContext())
//                .load(thumbnailUrl);
//
//        Glide.with(v.getContext())
//                .load(mainUrl)
//                .thumbnail(thumbnailRequest)
//                .dontAnimate()
//                .into(v);
//    }
//
//
//}
