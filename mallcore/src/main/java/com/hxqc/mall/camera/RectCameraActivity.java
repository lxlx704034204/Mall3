package com.hxqc.mall.camera;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hxqc.mall.core.R;
import com.hxqc.mall.core.util.ActivitySwitchBase;
import com.hxqc.mall.core.util.WidgetController;
import com.hxqc.util.DisplayTools;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * 说明:带有矩形框的相机
 *
 * @author: 吕飞
 * @since: 2016-10-31
 * Copyright:恒信汽车电子商务有限公司
 */
public class RectCameraActivity extends CameraActivity implements SurfaceHolder.Callback {
    RectCameraView mRectCameraView = null;
    SurfaceHolder mySurfaceHolder = null;
    TextView mDescriptionView;
    View mShadowBottomView;
    View mShadowRightView;
    View mShadowLeftView;
    public static final String TYPE = "type";
    public int mType;

    @Override
    protected void initView() {
        mType = getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA).getInt(TYPE);
        setContentView(R.layout.activity_rect_camera);
        //初始化SurfaceView
        mSurfaceView = (SurfaceView) findViewById(R.id.surface);
        mDescriptionView = (TextView) findViewById(R.id.description);
        mFlashView = (ImageView) findViewById(R.id.flash);
        mCancelView = (TextView) findViewById(R.id.cancel);
        mShadowBottomView = findViewById(R.id.shadow_bottom);
        mShadowRightView = findViewById(R.id.shadow_right);
        mShadowLeftView = findViewById(R.id.shadow_left);
        mSurfaceView.setZOrderOnTop(false);
        mySurfaceHolder = mSurfaceView.getHolder();
        mySurfaceHolder.setFormat(PixelFormat.TRANSPARENT);
        mySurfaceHolder.addCallback(this);
        mySurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        mRectCameraView = (RectCameraView) findViewById(R.id.rect_camera);
        mRectCameraView.draw(new Canvas());
        mDescriptionView.setPadding(0, (int) (DisplayTools.dip2px(this, 161) + mRectCameraView.mRectHeight), 0, 0);
        switch (mType) {
            case GetIDPhotoFragment.CAMERA_DRIVER:
                mDescriptionView.setText("将驾驶证正面置于此区域，并对准拍照");
                break;
            case GetIDPhotoFragment.CAMERA_DRIVING:
                mDescriptionView.setText("将行驶证正面置于此区域，并对准拍照");
                break;
            case GetIDPhotoFragment.CAMERA_ID:
                mDescriptionView.setText("将身份证正面置于此区域，并对准拍照");
                break;
            case GetIDPhotoFragment.CAMERA_DRIVER_COPY:
                mDescriptionView.setText("将驾驶证副本正面置于此区域，并对准拍照");
                break;
            case GetIDPhotoFragment.CAMERA_DRIVING_COPY:
                mDescriptionView.setText("将行驶证副本正面置于此区域，并对准拍照");
                break;
            case GetIDPhotoFragment.CAMERA_ID_BACK:
                mDescriptionView.setText("将身份证背面置于此区域，并对准拍照");
                break;
        }
        mTakePhotoView = (ImageView) findViewById(R.id.take_photo);
        mTakePhotoView.setOnClickListener(this);
        mCancelView.setOnClickListener(this);
        mFlashView.setOnClickListener(this);
        WidgetController.setLayoutHeight(mShadowBottomView, (int) (mRectCameraView.mScreenHeight - DisplayTools.dip2px(this, 36 + 109 + 96) - mRectCameraView.mRectHeight));
        WidgetController.setLayoutWidth(mShadowLeftView, (int) (mRectCameraView.mScreenWidth * 0.15 * 0.5));
        WidgetController.setLayoutWidth(mShadowRightView, (int) (mRectCameraView.mScreenWidth * 0.15 * 0.5));
    }

    /*下面三个是SurfaceHolder.Callback创建的回调函数*/
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
    // 当SurfaceView/预览界面的格式和大小发生改变时，该方法被调用
    {
        // TODO Auto-generated method stub
        initCamera();
    }


    public void surfaceCreated(SurfaceHolder holder)
    // SurfaceView启动时/初次实例化，预览界面被创建时，该方法被调用。
    {
        // TODO Auto-generated method stub

        mCamera = Camera.open();
        try {
            mCamera.setPreviewDisplay(mySurfaceHolder);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            if (null != mCamera) {
                mCamera.release();
                mCamera = null;
            }
            e.printStackTrace();
        }
    }


    public void surfaceDestroyed(SurfaceHolder holder)
    //销毁时被调用
    {
        if (null != mCamera) {
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            isPreview = false;
            mCamera.release();
            mCamera = null;
        }

    }

    //初始化相机
    @Override
    public void initCamera() {
        if (isPreview) {
            mCamera.stopPreview();
        }
        if (null != mCamera) {
            mParameters = mCamera.getParameters();
            List<Camera.Size> previewSizes = mParameters.getSupportedPreviewSizes();
            Collections.sort(previewSizes, mSizeComparator);
            int previewWidth = 1280;
            int previewHeight = 720;
            boolean findPreviewSize = false;
            if (previewSizes.size() > 1) {
                for (Camera.Size cur : previewSizes) {
                    if (cur.width >= previewWidth && cur.height >= previewHeight && cur.height * 1.7 < cur.width) {
                        findPreviewSize = true;
                        previewWidth = cur.width;
                        previewHeight = cur.height;
                        break;
                    }
                }
            }

            int pictureWidth = 1280;
            int pictureHeight = 720;
            boolean findPictureSize = false;
            List<Camera.Size> pictureSizes = mParameters.getSupportedPictureSizes();
            Collections.sort(pictureSizes, mSizeComparator);
            if (pictureSizes.size() > 1) {
                for (Camera.Size cur : pictureSizes) {
                    if (cur.width >= pictureWidth && cur.height >= pictureHeight && cur.width <= pictureWidth * 2 && cur.height <= pictureHeight * 2 && cur.height * 1.7 < cur.width) {
                        findPictureSize = true;
                        pictureWidth = cur.width;
                        pictureHeight = cur.height;
                        break;
                    }
                }
            }
            if (!findPreviewSize) {
                previewWidth = previewSizes.get(0).width;
                previewHeight = previewSizes.get(0).height;
            }
            if (!findPictureSize) {
                pictureWidth = pictureSizes.get(0).width;
                pictureHeight = pictureSizes.get(0).height;
            }
            mParameters.setPictureFormat(PixelFormat.JPEG);//设置拍照后存储的图片格式
            mParameters.setPreviewSize(previewWidth, previewHeight);
            mParameters.setPictureSize(pictureWidth, pictureHeight);
            mCamera.setDisplayOrientation(90);
            mParameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
            mParameters.setJpegQuality(100);
            mCamera.setParameters(mParameters);
            mCamera.startPreview();
            isPreview = true;
        }
    }

    PictureCallback rectCallback = new PictureCallback()
            //对jpeg图像数据的回调,最重要的一个回调
    {

        public void onPictureTaken(byte[] data, Camera camera) {
            // TODO Auto-generated method stub
            if (null != data) {
                mBitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                mCamera.stopPreview();
                isPreview = false;
            }
            Matrix matrix = new Matrix();
            matrix.postRotate((float) 90.0);
            Bitmap rotaBitmap = Bitmap.createBitmap(mBitmap, 0, 0, mBitmap.getWidth(), mBitmap.getHeight(), matrix, false);
            Bitmap rectBitmap;
            if (mType > 300) {
                rectBitmap = Bitmap.createBitmap(rotaBitmap, (int) (rotaBitmap.getWidth() * 0.15 * 0.5),
                        (int) (rotaBitmap.getHeight() * mRectCameraView.length1 / mRectCameraView.getHeight()),
                        (int) (rotaBitmap.getWidth() * 0.85), (int) (rotaBitmap.getWidth() * 0.85 * 54 / 85));
            } else {
                rectBitmap = Bitmap.createBitmap(rotaBitmap, (int) (rotaBitmap.getWidth() * 0.15 * 0.5),
                        (int) (rotaBitmap.getHeight() * mRectCameraView.length1 / mRectCameraView.getHeight()),
                        (int) (rotaBitmap.getWidth() * 0.85),
                        (int) (rotaBitmap.getWidth() * 0.85 * 66 / 95));
            }

            if (null != rectBitmap) {
                saveJpeg(rectBitmap);
            }
        }
    };

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.take_photo) {
            if (isPreview && mCamera != null) {
                mCamera.takePicture(myShutterCallback, null, rectCallback);
            }
        } else {
            super.onClick(view);
        }
    }

}
