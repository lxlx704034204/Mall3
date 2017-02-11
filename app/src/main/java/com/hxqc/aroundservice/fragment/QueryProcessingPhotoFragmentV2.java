package com.hxqc.aroundservice.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hxqc.aroundservice.config.OrderDetailContants;
import com.hxqc.aroundservice.view.DialogImageView;
import com.hxqc.aroundservice.view.DialogTextView;
import com.hxqc.mall.auto.config.AutoInfoContants;
import com.hxqc.mall.camera.GetIDPhotoFragment;
import com.hxqc.mall.core.util.ImageUtil;
import com.hxqc.mall.core.views.HeightVSWidthImageView;
import com.hxqc.mall.core.views.dialog.ListDialog;

import hxqc.mall.R;


/**
 * Author:胡仲俊
 * Date: 2016 - 04 - 23
 * Des: 图片区域控件
 * FIXME
 * Todo
 */
public class QueryProcessingPhotoFragmentV2 extends GetIDPhotoFragment {

    private static final String TAG = AutoInfoContants.LOG_J;
    protected View rootView;
    private TextView mTitleView;
    private RelativeLayout mAreaView;
    private HeightVSWidthImageView mBGView;
    private HeightVSWidthImageView mBG2View;
    private Button mReuploadView;
    private String mFilePath;
    private DialogImageView mSampleView;
    private DialogTextView mSampleTitleView;
    private int mAactivityType;

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
        mBGView = (HeightVSWidthImageView) view.findViewById(R.id.photo_bg);
        mBG2View = (HeightVSWidthImageView) view.findViewById(R.id.photo_bg_2);
        mReuploadView = (Button) view.findViewById(R.id.photo_reupload);
        mSampleView = (DialogImageView) view.findViewById(R.id.photo_sample);
        mSampleTitleView = (DialogTextView) view.findViewById(R.id.photo_sample_title);

    }

    private boolean k = false;

    public void changeImageView(boolean k) {
        if (k) {
            this.k = k;
            mBGView.setVisibility(View.GONE);
            mBG2View.setVisibility(View.VISIBLE);
        }
    }

    public void setAactivityType(int type) {
        this.mAactivityType = type;
    }

    public void setClickAreaListener(final boolean isFront) {
        mAreaView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeUserImage(isFront);
            }
        });
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
        mSampleView.showSamplePhoto(context, resid);
    }

    public void setClickSampleTitleListener(Context context, int resid) {
        mSampleTitleView.showSamplePhoto(context, resid);
    }

    private void changeUserImage(final boolean isFront) {
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
//                            getPicFromCamera();
                            if (mAactivityType == OrderDetailContants.DRIVER) {
                                if (isFront) {
                                    toTakePhoto(CAMERA_DRIVER);
                                } else {
                                    toTakePhoto(CAMERA_DRIVER_COPY);
                                }
                            } else if (mAactivityType == OrderDetailContants.DRIVING) {
                                if (isFront) {
                                    toTakePhoto(CAMERA_DRIVING);
                                } else {
                                    toTakePhoto(CAMERA_DRIVING_COPY);
                                }
                            } else if (mAactivityType == OrderDetailContants.ID) {
                                if (isFront) {
                                    toTakePhoto(CAMERA_ID);
                                } else {
                                    toTakePhoto(CAMERA_ID_BACK);
                                }
                            }
                            break;
                        case 1:
                            // 图库
//                            getPicFromContent();
                            if (mAactivityType == OrderDetailContants.DRIVER) {
                                if (isFront) {
                                    getPicFromContent(GALLERY_DRIVER);
                                } else {
                                    getPicFromContent(GALLERY_DRIVER_COPY);
                                }
                            } else if (mAactivityType == OrderDetailContants.DRIVING) {
                                if (isFront) {
                                    getPicFromContent(GALLERY_DRIVING);
                                } else {
                                    getPicFromContent(GALLERY_DRIVING_COPY);
                                }
                            } else if (mAactivityType == OrderDetailContants.ID) {
                                if (isFront) {
                                    getPicFromContent(GALLERY_ID);
                                } else {
                                    getPicFromContent(GALLERY_ID_BACK);
                                }
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

    public String getmFilePath() {
        return mFilePath;
    }

    @Override
    protected void chooseSuccess(String photoPath, int requestCode) {
        mFilePath = "file://" + photoPath;
        switch (requestCode) {
            case CAMERA_DRIVER:
            case CAMERA_DRIVER_COPY:
            case GALLERY_DRIVER:
            case GALLERY_DRIVER_COPY:
            case CROP_DRIVER:
                if (k) {
                    ImageUtil.setImage(getActivity(), mBG2View, mFilePath, R.drawable.ic_pic_camera);
                } else {
                    ImageUtil.setImage(getActivity(), mBGView, mFilePath, R.drawable.ic_pic_camera);
                }
                break;
            case CAMERA_DRIVING:
            case CAMERA_DRIVING_COPY:
            case GALLERY_DRIVING:
            case GALLERY_DRIVING_COPY:
            case CROP_DRIVING:
                if (k) {
                    ImageUtil.setImage(getActivity(), mBG2View, mFilePath, R.drawable.ic_pic_camera);
                } else {
                    ImageUtil.setImage(getActivity(), mBGView, mFilePath, R.drawable.ic_pic_camera);
                }
                break;
            case CAMERA_ID:
            case CAMERA_ID_BACK:
            case GALLERY_ID:
            case GALLERY_ID_BACK:
            case CROP_ID:
                if (k) {
                    ImageUtil.setImage(getActivity(), mBG2View, mFilePath, R.drawable.ic_pic_camera);
                } else {
                    ImageUtil.setImage(getActivity(), mBGView, mFilePath, R.drawable.ic_pic_camera);
                }
                break;
        }
    }
}
