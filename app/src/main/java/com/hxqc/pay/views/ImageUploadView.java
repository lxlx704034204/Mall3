package com.hxqc.pay.views;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.hxqc.mall.core.util.ImageUtil;
import com.hxqc.pay.fragment.PhotoPreviewFragment;
import com.hxqc.util.DebugLog;

import hxqc.mall.R;

/**
 * Author :liukechong
 * Date : 2015-11-09
 * FIXME
 * Todo
 */
public class ImageUploadView extends RelativeLayout implements View.OnClickListener {
    private static final String TAG = "ImageUploadView";

    private boolean photoPreview = false;

    public ImageUploadView(Context context) {
        super(context);
    }

    public ImageUploadView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ImageUploadView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private View view;

    private ImageView image;
    private ImageCoverProgress progress;
    private ImageView image_edit;

    private String infoLargeURL = "";
    private String infoURL = "";
    private boolean isUploading = false;

    public boolean getIsUploading() {
        return isUploading;
    }

    private void setIsUploading(boolean isUploading) {
        this.isUploading = isUploading;
    }

    public void setImageInfoURL(String infoURL, String infoLargeURL) {
        this.infoURL = infoURL;
        this.infoLargeURL = infoLargeURL;
        setImage(infoURL);
    }

    public String getInfoLargeURL() {
        return infoLargeURL;
    }

    public String getInfoURL() {
        return infoURL;
    }


    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        LayoutInflater inflater = LayoutInflater.from(getContext());
        view = inflater.inflate(R.layout.item_image_upload, this, true);
        initView();
    }

    private void initView() {
        image = (ImageView) view.findViewById(R.id.image);
        progress = (ImageCoverProgress) view.findViewById(R.id.progress);
        image_edit = (ImageView) view.findViewById(R.id.image_edit);
        image_edit.setVisibility(GONE);
        image.setOnClickListener(this);
        image_edit.setOnClickListener(this);

    }

    public void setImage(String imageURL) {
        loadImage(imageURL, image, image_edit);
    }

    public boolean getPhotoPreviewPrepared() {
        return photoPreview;
    }

    /**
     * 给iamgeView赋值
     */
    private void loadImage(String imageURL, ImageView imageView, ImageView targetImageEdit) {
        if (!TextUtils.isEmpty(imageURL)) {
            ImageUtil.setImage(getContext(), imageView, imageURL, R.drawable.ic_pic_camera);
            targetImageEdit.setVisibility(View.VISIBLE);
            imageView.setBackgroundColor(Color.WHITE);
            photoPreview = true;
        }
    }

    public void openPhotoView(FragmentActivity activity) {
        if (TextUtils.isEmpty(infoLargeURL)) {
            return;
        }
        PhotoPreviewFragment photoPreviewFragment = new PhotoPreviewFragment();
        Bundle bundle = new Bundle();
        bundle.putString("InfoLarge", infoLargeURL);
        photoPreviewFragment.setArguments(bundle);
        photoPreviewFragment.show(activity.getSupportFragmentManager(), "");
    }

    /**
     * 上传图片
     */
//    public void uploadImage(String filePath) {
//        try {
//            PayApiClient mPayApiClient = new PayApiClient();
//            Message message = Message.obtain();
//            message.what = 101;
//            Bundle bundle = new Bundle();
//            bundle.putFloat("RADIO", 0);
//            message.setData(bundle);
//            mHandler.sendMessage(message);
//
//            File file = new File(filePath);
//            DebugLog.d(TAG, "uploadImage() returned: " + file);
//            mPayApiClient.uploadImage(file, new TextHttpResponseHandler() {
//                @Override
//                public void onStart() {
//                    super.onStart();
//                    if (image != null) {
//                        image.setClickable(false);
//                    }
//                    setIsUploading(true);
//                }
//
//                @Override
//                public void onCancel() {
//                    super.onCancel();
//                    if (image != null) {
//                        image.setClickable(true);
//                    }
//                    setIsUploading(false);
//                }
//
//                @Override
//                public void onFinish() {
//                    super.onFinish();
//                    if (image != null) {
//                        image.setClickable(true);
//                    }
//                    setIsUploading(false);
//                }
//
//                @Override
//                public void onFailure(int statusCode, Header[] headers, String responseBody, Throwable error) {
//                    if (image != null) {
//                        Context context = getContext();
//                        if (context != null) {
//                            image.setImageResource(R.drawable.ic_warning2);
//                            ToastHelper.showRedToast(context, "无法获取图片地址");
//                            photoPreview = false;
//                        }
//                    }
//                }
//
//                @Override
//                public void onSuccess(int statusCode, Header[] headers, String responseString) {
//                    HashMap<String, String> stringStringHashMap = JSONUtils.fromJson(responseString, new TypeToken<HashMap<String, String>>() {
//                    });
//                    image_edit.setVisibility(View.VISIBLE);
//                    photoPreview = true;
//                    infoLargeURL = stringStringHashMap.get("image");
//                    Message message = Message.obtain();
//                    message.what = 101;
//                    Bundle bundle = new Bundle();
//                    bundle.putFloat("RADIO", 1.0f);
//                    message.setData(bundle);
//                    mHandler.sendMessage(message);
//                    DebugLog.d(TAG, "onSuccess() returned: " + infoLargeURL);
//                }
//
//
//                @Override
//                public synchronized void onProgress(int bytesWritten, int totalSize) {
//                    super.onProgress(bytesWritten, totalSize);
//                    DebugLog.d(TAG, "onProgress() returned: " + bytesWritten + "-----" + totalSize);
//                    Message message = Message.obtain();
//                    message.what = 101;
//                    Bundle bundle = new Bundle();
//
//
//                    if (bytesWritten * 1.0f / totalSize > 0.9) {
//                        bundle.putFloat("RADIO", 0.9f);
//                        message.setData(bundle);
//                        mHandler.sendMessage(message);
//                        return;
//                    }
//                    bundle.putFloat("RADIO", bytesWritten * 1.0f / totalSize);
//                    message.setData(bundle);
//                    mHandler.sendMessage(message);
//
//                }
//            });
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//            image.setImageResource(R.drawable.ic_warning2);
//            ToastHelper.showRedToast(getContext(), "无法获取图片地址");
//        }
//    }

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == 101) {
                DebugLog.d(TAG, "handleMessage() returned: " + msg.getData().getFloat("RADIO"));
                progress.setRadio(msg.getData().getFloat("RADIO"));
            }
            return true;
        }
    });

    @Override
    public void setEnabled(boolean enabled) {
        image_edit.setVisibility(GONE);
        photoPreview = true;
        super.setEnabled(enabled);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.image_edit) {
            if (mCallBack != null) {
                mCallBack.clickImageEdit(this);
            }
        } else if (v.getId() == R.id.image) {
            if (mCallBack != null) {
                mCallBack.clickImage(this);
            }
        }
    }

    private ImageUploadCallBack mCallBack;

    public void setCallBack(ImageUploadCallBack callBack) {
        this.mCallBack = callBack;
    }

    public interface ImageUploadCallBack {
        /**
         * 点击图片旁边的编辑按钮
         * @param view
         */
        void clickImageEdit(ImageUploadView view);

        /**
         * 点击图片
         * @param view
         */
        void clickImage(ImageUploadView view);

    }
}
