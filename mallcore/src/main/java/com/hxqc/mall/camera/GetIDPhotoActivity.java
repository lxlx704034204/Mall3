package com.hxqc.mall.camera;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;

import com.hxqc.mall.activity.NoBackActivity;
import com.hxqc.mall.core.util.ActivitySwitchBase;

import java.io.File;

/**
 * 说明:取照片或截图
 *
 * @author: 吕飞
 * @since: 2016-11-02
 * Copyright:恒信汽车电子商务有限公司
 */

public abstract class GetIDPhotoActivity extends NoBackActivity {
    protected final static int CAMERA_DRIVER = 101;    // 驾驶证拍照
    protected final static int GALLERY_DRIVER = 102;    // 驾驶证选图
    protected final static int CROP_DRIVER = 103;    // 驾驶证裁切
    protected final static int CAMERA_DRIVING = 201;    // 行驶证拍照
    protected final static int GALLERY_DRIVING = 202;    // 行驶证选图
    protected final static int CROP_DRIVING = 203;    // 驾驶证裁切
    protected final static int CAMERA_ID = 301;    // 身份证拍照
    protected final static int GALLERY_ID = 302;    // 身份证选图
    protected final static int CROP_ID = 303;    // 驾驶证裁切
    protected int aspectX;
    protected int aspectY;
    protected int outputX;
    protected int outputY;
    protected String photoPath;

    protected void toTakePhoto(int requestCode) {
        Intent intent = new Intent(this, RectCameraActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(RectCameraActivity.TYPE, requestCode);
        intent.putExtra(ActivitySwitchBase.KEY_DATA, bundle);
        startActivityForResult(intent, requestCode);
    }

    /**
     * 裁剪图片方法实现
     *
     * @param uri
     */
    protected void startPhotoZoom(Uri uri, int requestCode) {
        if (requestCode < 300) {
            aspectX = 95;
            aspectY = 66;
            outputX = 95 * 6;
            outputY = 66 * 6;
        } else {
            aspectX = 85;
            aspectY = 54;
            outputX = 85 * 6;
            outputY = 54 * 6;
        }
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
        photoPath = Environment.getExternalStorageDirectory() + "/myimage/" + System.currentTimeMillis() + ".png";
        if (TextUtils.isEmpty(photoPath)) {
            throw new NullPointerException("crop path is null");
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT,
                Uri.fromFile(new File(photoPath)));
        intent.putExtra("return-data", false);
        this.startActivityForResult(intent, requestCode);
    }

    /**
     * 打开相册
     */
    protected void getPicFromContent(int requestCode) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
        startActivityForResult(intent, requestCode);
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
                photoPath = data.getStringExtra(RectCameraActivity.PHOTO_PATH);
                chooseSuccess(photoPath, requestCode);
                break;
            case GALLERY_DRIVER:// 打开相册
                startPhotoZoom(data.getData(), CROP_DRIVER);
                break;
            case GALLERY_DRIVING:// 打开相册
                startPhotoZoom(data.getData(), CROP_DRIVING);
                break;
            case GALLERY_ID:// 打开相册
                startPhotoZoom(data.getData(), CROP_ID);
                break;
            case CROP_DRIVER:
            case CROP_DRIVING:
            case CROP_ID:
                chooseSuccess(photoPath, requestCode);
                break;
        }
    }

    protected abstract void chooseSuccess(String photoPath, int requestCode);
}
