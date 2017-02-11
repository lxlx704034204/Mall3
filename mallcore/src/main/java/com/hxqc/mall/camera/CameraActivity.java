package com.hxqc.mall.camera;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hxqc.mall.core.R;
import com.hxqc.mall.core.util.ToastHelper;
import com.hxqc.mall.core.util.WidgetController;
import com.hxqc.util.DisplayTools;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 说明:相机
 *
 * @author: 吕飞
 * @since: 2016-10-31
 * Copyright:恒信汽车电子商务有限公司
 */
public class CameraActivity extends Activity implements SurfaceHolder.Callback, OnClickListener {
    boolean isPreview = false;
    boolean isFlash = false;
    SurfaceView mSurfaceView = null; //预览SurfaceView
    SurfaceHolder mySurfaceHolder = null;
    ImageView mTakePhotoView = null;
    Camera mCamera = null;
    Bitmap mBitmap = null;
    TextView mCancelView;
    ImageView mFlashView;
    Camera.Parameters mParameters;
    String mPhotoPath;
    int mScreenWidth;
    int mScreenHeight;
    RelativeLayout mBottomView;
    public static final String PHOTO_PATH = "photo_path";
    Comparator mSizeComparator = new Comparator<Camera.Size>() {
        @Override
        public int compare(Camera.Size size, Camera.Size t1) {
            if (size.height * size.width > t1.height * t1.width) {
                return 0;
            } else {
                return 1;
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置全屏无标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        int flag = WindowManager.LayoutParams.FLAG_FULLSCREEN;
        Window window = this.getWindow();
        window.setFlags(flag, flag);
        mScreenWidth = DisplayTools.getScreenWidth(this);
        mScreenHeight = DisplayTools.getScreenHeight(this);
        initView();
    }

    protected void initView() {
        setContentView(R.layout.activity_camera);
        //初始化SurfaceView
        mSurfaceView = (SurfaceView) findViewById(R.id.surface);
        mFlashView = (ImageView) findViewById(R.id.flash);
        mCancelView = (TextView) findViewById(R.id.cancel);
        mBottomView = (RelativeLayout) findViewById(R.id.bottom);
        mSurfaceView.setZOrderOnTop(false);
        mySurfaceHolder = mSurfaceView.getHolder();
        mySurfaceHolder.setFormat(PixelFormat.TRANSPARENT);
        mySurfaceHolder.addCallback(this);
        mySurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        mTakePhotoView = (ImageView) findViewById(R.id.take_photo);
        mTakePhotoView.setOnClickListener(this);
        mCancelView.setOnClickListener(this);
        mFlashView.setOnClickListener(this);
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
    public void initCamera() {
        if (isPreview) {
            mCamera.stopPreview();
        }
        if (null != mCamera) {
            mParameters = mCamera.getParameters();
            List<Camera.Size> previewSizes = mParameters.getSupportedPreviewSizes();
            Collections.sort(previewSizes, mSizeComparator);
            int previewWidth = 1200;
            int previewHeight = 900;
            boolean findPreviewSize = false;
            if (previewSizes.size() > 1) {
                for (Camera.Size cur : previewSizes) {
                    if (cur.width >= previewWidth && cur.height >= previewHeight && cur.height * 1.7 > cur.width && cur.height * 1.3 < cur.width) {
                        findPreviewSize = true;
                        previewWidth = cur.width;
                        previewHeight = cur.height;
                        break;
                    }
                }
            }

            int pictureWidth = 1200;
            int pictureHeight = 900;
            boolean findPictureSize = false;
            List<Camera.Size> pictureSizes = mParameters.getSupportedPictureSizes();
            Collections.sort(pictureSizes, mSizeComparator);
            if (pictureSizes.size() > 1) {
                for (Camera.Size cur : pictureSizes) {
                    if (cur.width >= pictureWidth && cur.height >= pictureHeight && cur.height * 1.7 > cur.width && cur.height * 1.3 < cur.width) {
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
            WidgetController.setLayoutHeight(mBottomView, mScreenHeight - mScreenHeight * previewHeight / previewWidth-DisplayTools.dip2px(this,32));
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

    /*为了实现拍照的快门声音及拍照保存照片需要下面三个回调变量*/
    ShutterCallback myShutterCallback = new ShutterCallback()
            //快门按下的回调，在这里我们可以设置类似播放“咔嚓”声之类的操作。默认的就是咔嚓。
    {

        public void onShutter() {
            // TODO Auto-generated method stub
        }
    };
    PictureCallback myJpegCallback = new PictureCallback()
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
            if (null != rotaBitmap) {
                saveJpeg(rotaBitmap);
            }
        }
    };

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.take_photo) {
            if (isPreview && mCamera != null) {
                mCamera.takePicture(myShutterCallback, null, myJpegCallback);
            }
        } else if (i == R.id.cancel) {
            finish();
        } else if (i == R.id.flash) {
            if (isFlash) {
                try {
                    mParameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                    mCamera.setParameters(mParameters);
                    isFlash = false;
                    mFlashView.setImageResource(R.drawable.flash);
                } catch (Exception e) {
                    ToastHelper.showRedToast(this, "闪光灯关闭失败");
                }
            } else {
                try {
                    mParameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                    mCamera.setParameters(mParameters);
                    isFlash = true;
                    mFlashView.setImageResource(R.drawable.flash_lamp);
                } catch (Exception e) {
                    ToastHelper.showRedToast(this, "闪光灯开启失败");
                }
            }
        }
    }

    /*给定一个Bitmap，进行保存*/
    public void saveJpeg(Bitmap bm) {
        String savePath = Environment.getExternalStorageDirectory() + "/myimage/";
        File folder = new File(savePath);
        if (!folder.exists()) {
            folder.mkdir();
        }
        long dataTake = System.currentTimeMillis();
        mPhotoPath = savePath + dataTake + ".jpg";
        try {
            FileOutputStream flout = new FileOutputStream(mPhotoPath);
            BufferedOutputStream bos = new BufferedOutputStream(flout);
            bm.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
            Log.i("camera", "saveJpeg：存储完毕！");
            Intent intent = new Intent();
            intent.putExtra(PHOTO_PATH, mPhotoPath);
            setResult(RESULT_OK, intent);
            finish();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            Log.i("camera", "saveJpeg:存储失败！");
            e.printStackTrace();
        }
    }

}
