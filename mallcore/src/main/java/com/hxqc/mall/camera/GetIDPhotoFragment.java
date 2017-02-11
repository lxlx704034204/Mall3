package com.hxqc.mall.camera;


import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.hxqc.mall.core.fragment.FunctionFragment;
import com.hxqc.mall.core.util.ActivitySwitchBase;
import com.hxqc.mall.core.util.ToastHelper;

import java.util.Collection;

import me.nereo.multi_image_selector.MultiImageSelector;


/**
 * 说明:
 *
 * @author: 吕飞
 * @since: 2016-11-04
 * Copyright:恒信汽车电子商务有限公司
 */

public abstract class GetIDPhotoFragment extends FunctionFragment implements ActivityCompat.OnRequestPermissionsResultCallback {
    protected final static int CAMERA_DRIVER = 101;    // 驾驶证拍照
    protected final static int GALLERY_DRIVER = 102;    // 驾驶证选图
    protected final static int CROP_DRIVER = 103;    // 驾驶证裁切
    protected final static int CAMERA_DRIVER_COPY = 104;    // 驾驶证副本拍照
    protected final static int GALLERY_DRIVER_COPY = 105;    // 驾驶证副本选图
    protected final static int CAMERA_DRIVING = 201;    // 行驶证拍照
    protected final static int GALLERY_DRIVING = 202;    // 行驶证选图
    protected final static int CROP_DRIVING = 203;    // 驾驶证裁切
    protected final static int CAMERA_DRIVING_COPY = 204;    // 行驶证副本拍照
    protected final static int GALLERY_DRIVING_COPY = 205;    // 行驶证副本选图
    protected final static int CAMERA_ID = 301;    // 身份证拍照
    protected final static int GALLERY_ID = 302;    // 身份证选图
    protected final static int CROP_ID = 303;    // 驾驶证裁切
    protected final static int CAMERA_ID_BACK = 304;    // 身份证背面拍照
    protected final static int GALLERY_ID_BACK = 305;    // 身份证背面选图
    protected int aspectX;
    protected int aspectY;
    protected int outputX;
    protected int outputY;
    protected String photoPath;
    int requestCode;

    protected void toTakePhoto(int requestCode) {
        this.requestCode = requestCode;
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            //申请WRITE_EXTERNAL_STORAGE权限
            requestPermissions(new String[]{Manifest.permission.CAMERA}, 1);
        } else {
            takePhoto();
        }
    }

    private void takePhoto() {
        Intent intent = new Intent(getActivity(), RectCameraActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(RectCameraActivity.TYPE, this.requestCode);
        intent.putExtra(ActivitySwitchBase.KEY_DATA, bundle);
        startActivityForResult(intent, requestCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        doNext(requestCode, grantResults);
    }

    private void doNext(int requestCode, int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                takePhoto();
            } else {
                ToastHelper.showYellowToast(getActivity(), "请在应用管理中打开“相机”访问权限！");
            }
        }
    }

    /**
     * 打开相册
     */
    protected void getPicFromContent(final int requestCode) {
        MultiImageSelector selector = new MultiImageSelector(getActivity());
        selector.showCamera(false);
        selector.cropPhoto(true);
        if (requestCode < 300) {
            selector.cropWithAspectRatio(95 * 6, 66 * 6);
        } else {
            selector.cropWithAspectRatio(85 * 6, 54 * 6);
        }
        selector.start(getActivity(), new MultiImageSelector.MultiImageCallBack() {
            @Override
            public void multiSelectorImages(Collection<String> collection) {
                if (collection != null && collection.size() > 0) {
                    String[] paths = new String[collection.size()];
                    String imgPath = collection.toArray(paths)[0];
                    chooseSuccess(imgPath, requestCode);
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case CAMERA_DRIVER:
            case CAMERA_DRIVING:
            case CAMERA_ID:
            case CAMERA_DRIVER_COPY:
            case CAMERA_DRIVING_COPY:
            case CAMERA_ID_BACK:
                photoPath = data.getStringExtra(RectCameraActivity.PHOTO_PATH);
                chooseSuccess(photoPath, requestCode);
                break;
            case GALLERY_ID:// 打开相册
            case CROP_DRIVER:
            case CROP_DRIVING:
            case CROP_ID:
                chooseSuccess(photoPath, requestCode);
                break;
        }
    }

    protected abstract void chooseSuccess(String photoPath, int requestCode);

    @Override
    public String fragmentDescription() {
        return "取照片";
    }

}
