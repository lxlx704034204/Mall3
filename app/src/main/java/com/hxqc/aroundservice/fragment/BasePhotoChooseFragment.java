//package com.hxqc.aroundservice.fragment;
//
//import android.content.Context;
//import android.graphics.Bitmap;
//import android.view.View;
//
//import com.hxqc.mall.core.views.dialog.ListDialog;
//import com.hxqc.mall.config.application.SampleApplicationContext;
//import com.hxqc.widget.PhotoChooseFragment;
//import com.squareup.picasso.Transformation;
//
//import java.io.File;
//
//import hxqc.mall.R;
//
///**
// * Author:胡仲俊
// * Date: 2016 - 05 - 17
// * FIXME
// * Todo 详情页图片选择父类
// */
//public abstract class BasePhotoChooseFragment extends PhotoChooseFragment {
//
//    /**
//     * 选择图片获取方式
//     */
//    protected void changeUserImage() {
//        try {
//            new ListDialog(getActivity(), getActivity().getResources().getString(R.string.me_upload_photo),
//                    new String[]{getResources().getString(R.string.me_take_picture_upload), getResources().getString
//                            (R.string.me_local_upload)},
//                    new int[]{R.drawable.ic_uploadpictures, R.drawable.ic_detail_image1}) {
//                @Override
//                protected void doNext(int position) {
//                    switch (position) {
//                        case 0:
//                            // 相机
//                            getPicFromCamera();
//                            break;
//                        case 1:
//                            // 图库
//                            getPicFromContent();
//                            break;
//                        default:
//                            break;
//                    }
//                }
//            }.show();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    protected Transformation transformationOriginal = null;
//
//    /**
//     * 图片大小等比例缩放
//     * @param v
//     * @return
//     */
//    protected Transformation getTransformation(final View v) {
//        transformationOriginal = new Transformation() {
//
//            @Override
//            public Bitmap transform(Bitmap source) {
//                int targetWidth = v.getWidth();
//                double aspectRatio = (double) source.getHeight() / (double) source.getWidth();
//                int targetHeight = (int) (targetWidth * aspectRatio);
//                Bitmap result = Bitmap.createScaledBitmap(source, targetWidth, targetHeight, false);
//                if (result != source) {
//                    // Same bitmap is returned if sizes are the same
//                    source.recycle();
//                }
//                return result;
//            }
//
//            @Override
//            public String key() {
//                return "transformation" + " desiredWidth";
//            }
//        };
//
//        return transformationOriginal;
//    }
//
//    @Override
//    public boolean toCropPhoto() {
//        return false;
//    }
//
//    @Override
//    public String getCameraPath() {
//        return SampleApplicationContext.application.getExternalCacheDir().toString()
//                + "/ps" + System.currentTimeMillis() + ".png";
//    }
//
//    @Override
//    protected String getCropCacheFilePath() {
//        Context context = getActivity();
//        cropCachePath = context.getExternalCacheDir() + File.separator + "crop_"
//                + System.currentTimeMillis() + ".png";
//        return cropCachePath;
//    }
//}
