package com.hxqc.aroundservice.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hxqc.mall.auto.config.AutoInfoContants;
import com.hxqc.mall.core.model.ImageModel;
import com.hxqc.mall.core.util.ImageUtil;
import com.hxqc.mall.core.util.activityutil.ActivitySwitcher;
import com.hxqc.util.DebugLog;

import hxqc.mall.R;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Author:胡仲俊
 * Date: 2016 - 05 -
 * Des: 大图页面
 * FIXME
 * Todo
 */
public class LargeImageFragment extends Fragment {

    private static final String TAG = AutoInfoContants.LOG_J;
    private ImageModel imImageModel;
    private View rootView;
    private PhotoView mLargeView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        imImageModel = bundle.getParcelable(ActivitySwitcher.viewLargePics);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_large_image, container, false);
        }
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mLargeView = (PhotoView) rootView.findViewById(R.id.large_image);
        mLargeView.setBackgroundColor(Color.BLACK);
        DebugLog.i(TAG,imImageModel.largeImage);
        ImageUtil.setLargeImage(getContext(), mLargeView, imImageModel.largeImage, R.drawable.pic_normal_square, R.drawable.ic_illegal_no_img);
        mLargeView.setOnViewTapListener(new PhotoViewAttacher.OnViewTapListener() {
            @Override
            public void onViewTap(View view, float x, float y) {
                getActivity().finish();
            }
        });
    }

//    private Transformation getTransformation(final View v) {
//        /**
//         * 图片大小等比例缩放
//         */
//        Transformation transformationOriginal = new Transformation() {
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
}
