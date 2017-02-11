package com.hxqc.aroundservice.fragment;

import com.hxqc.mall.camera.GetIDPhotoFragment;
import com.hxqc.mall.core.views.dialog.ListDialog;

import hxqc.mall.R;

/**
 * Author:胡仲俊
 * Date: 2016 - 05 - 17
 * FIXME
 * Todo 详情页图片选择父类
 */
public abstract class BasePhotoChooseFragmentV2 extends GetIDPhotoFragment {

    /**
     * 选择驾驶证获取方式
     */
    protected void changeDriverImage(final boolean isFront) {
        try {
            new ListDialog(getActivity(), getActivity().getResources().getString(R.string.me_upload_photo),
                    new String[]{getResources().getString(R.string.me_take_picture_upload), getResources().getString
                            (R.string.me_local_upload)},
                    new int[]{R.drawable.ic_pic_camera, R.drawable.ic_detail_image1}) {
                @Override
                protected void doNext(int position) {
                    switch (position) {
                        case 0:
                            // 相机
                            if (isFront) {
                                toTakePhoto(CAMERA_DRIVER);
                            } else {
                                toTakePhoto(CAMERA_DRIVER_COPY);
                            }
                            break;
                        case 1:
                            // 图库
                            if (isFront) {
                                getPicFromContent(GALLERY_DRIVER);
                            } else {
                                getPicFromContent(GALLERY_DRIVER_COPY);
                            }
                            break;
                        default:
                            break;
                    }
                }
            }.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 选择行驶证获取方式
     */
    protected void changeDriveringImage(final boolean isFront) {
        try {
            new ListDialog(getActivity(), getActivity().getResources().getString(R.string.me_upload_photo),
                    new String[]{getResources().getString(R.string.me_take_picture_upload), getResources().getString
                            (R.string.me_local_upload)},
                    new int[]{R.drawable.ic_pic_camera, R.drawable.ic_detail_image1}) {
                @Override
                protected void doNext(int position) {
                    switch (position) {
                        case 0:
                            // 相机
                            if (isFront) {
                                toTakePhoto(CAMERA_DRIVING);
                            } else {
                                toTakePhoto(CAMERA_DRIVING_COPY);
                            }
                            break;
                        case 1:
                            // 图库
                            if (isFront) {
                                getPicFromContent(GALLERY_DRIVING);
                            } else {
                                getPicFromContent(GALLERY_DRIVING_COPY);
                            }
                            break;
                        default:
                            break;
                    }
                }
            }.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 选择身份证获取方式
     */
    protected void changeIDImage(final boolean isFront) {
        try {
            new ListDialog(getActivity(), getActivity().getResources().getString(R.string.me_upload_photo),
                    new String[]{getResources().getString(R.string.me_take_picture_upload), getResources().getString
                            (R.string.me_local_upload)},
                    new int[]{R.drawable.ic_pic_camera, R.drawable.ic_detail_image1}) {
                @Override
                protected void doNext(int position) {
                    switch (position) {
                        case 0:
                            // 相机
                            if (isFront) {
                                toTakePhoto(CAMERA_ID);
                            } else {
                                toTakePhoto(CAMERA_ID_BACK);
                            }
                            break;
                        case 1:
                            // 图库
                            if (isFront) {
                                getPicFromContent(GALLERY_ID);
                            } else {
                                getPicFromContent(GALLERY_ID_BACK);
                            }
                            break;
                        default:
                            break;
                    }
                }
            }.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
