package com.hxqc.aroundservice.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hxqc.aroundservice.view.DialogImageView;
import com.hxqc.aroundservice.view.DialogTextView;
import com.hxqc.mall.camera.GetIDPhotoFragment;
import com.hxqc.mall.core.util.ImageUtil;
import com.hxqc.mall.core.views.dialog.ListDialog;

import hxqc.mall.R;

/**
 * Author:胡仲俊
 * Date: 2016 - 04 - 23
 * FIXME
 * Todo 图片区域控件
 */
@Deprecated
public class QueryProcessingPhotoFragment extends GetIDPhotoFragment {

    protected View rootView;
    private TextView mTitleView;
    private RelativeLayout mAreaView;
    private ImageView mBGView;
    private Button mReuploadView;
    private String mFilePath;
    private DialogImageView mSampleView;
    private DialogTextView mSampleTitleView;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_illegal_processing_photo, container, false);
        }
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mTitleView = (TextView) view.findViewById(R.id.photo_title);
        mAreaView = (RelativeLayout) view.findViewById(R.id.photo_area);
        mBGView = (ImageView) view.findViewById(R.id.photo_bg);
        mReuploadView = (Button) view.findViewById(R.id.photo_reupload);
        mSampleView = (DialogImageView) view.findViewById(R.id.photo_sample);
        mSampleTitleView = (DialogTextView) view.findViewById(R.id.photo_sample_title);

    }

    public void setClickAreaListener() {
        mAreaView.setOnClickListener(clickAreaListener);
    }

    public void setBackground(int resId) {
        mBGView.setImageResource(resId);
    }

    public void setTitle(CharSequence text) {
        mTitleView.setText(text);
    }

    public void setSampleTitleViewVisibility(boolean visibility) {
        if (visibility) {
            mSampleTitleView.setVisibility(View.VISIBLE);
        } else {
            mSampleTitleView.setVisibility(View.GONE);
        }
    }

    public void setClickSampleListener(Context context, int resid) {
        /*mSampleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView imageView = new ImageView(getActivity());
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(10,0,10,0);
                imageView.setLayoutParams(layoutParams);
                imageView.setBackgroundResource(resid);
                new AlertDialog.Builder(getActivity())
                        .setView(imageView)
                        .show();
            }
        });*/
        mSampleView.showSamplePhoto(context, resid);
    }

    public void setClickSampleTitleListener(Context context, int resid) {
        /*mSampleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView imageView = new ImageView(getActivity());
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(10,0,10,0);
                imageView.setLayoutParams(layoutParams);
                imageView.setBackgroundResource(resid);
                new AlertDialog.Builder(getActivity())
                        .setView(imageView)
                        .show();
            }
        });*/
        mSampleTitleView.showSamplePhoto(context, resid);
    }


//    @Override
//    public void chooseSuccess(String filePath) {
//        mFilePath = "file://" + filePath;
//        DebugLog.i(TAG, "mFilePath:" + mFilePath);
//        ImageUtil.setImage2(getActivity(), mBGView, mFilePath, R.drawable.ic_uploadpictures);
//    }

    public String getmFilePath() {
        return mFilePath;
    }
//
//    @Override
//    public boolean toCropPhoto() {
//        return false;
//    }
//
//    @Override
//    public String getCameraPath() {
////        return SampleApplicationContext.application.getExternalCacheDir().toString()
////                + "/ps" + System.currentTimeMillis() + ".png";
//        return String.valueOf(getActivity().getExternalCacheDir()) + File.separator +
//                "ps" + System.currentTimeMillis() + ".png";
//    }

//    @Override
//    protected String getCropCacheFilePath() {
//        Context context = getActivity();
//        cropCachePath = context.getExternalCacheDir() + File.separator + "crop_"
//                + System.currentTimeMillis() + ".png";
//        return cropCachePath;
//    }

    private void changeUserImage() {
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
                            toTakePhoto(CAMERA_DRIVER);
                            break;
                        case 1:
                            // 图库
                            getPicFromContent(GALLERY_DRIVER);
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
     * 照片区域点击事件
     */
    private View.OnClickListener clickAreaListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            changeUserImage();
        }
    };

//    /**
//     * 图片大小等比例缩放
//     */
//    private Transformation transformation = new Transformation() {
//
//        @Override
//        public Bitmap transform(Bitmap source) {
//            int targetWidth = mAreaView.getWidth();
//
//            double aspectRatio = (double) source.getHeight() / (double) source.getWidth();
//            int targetHeight = (int) (targetWidth * aspectRatio);
//            Bitmap result = Bitmap.createScaledBitmap(source, targetWidth, targetHeight, false);
//            if (result != source) {
//                // Same bitmap is returned if sizes are the same
//                source.recycle();
//            }
//            return result;
//        }
//
//        @Override
//        public String key() {
//            return "transformation" + " desiredWidth";
//        }
//    };

    @Override
    protected void chooseSuccess(String photoPath, int requestCode) {
        mFilePath = "file://" + photoPath;
        ImageUtil.setImage(getActivity(), mBGView, mFilePath, R.drawable.ic_pic_camera);
    }
}
