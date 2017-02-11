package com.hxqc.aroundservice.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hxqc.mall.core.views.HeightVSWidthImageView;

import hxqc.mall.R;

/**
 * Author:胡仲俊
 * Date: 2016 - 12 - 05
 * FIXME
 * Todo 选择照片并区域显示控件
 */

public class ChoosePictureLayout extends LinearLayout {

    private RelativeLayout mAreaView;
    private HeightVSWidthImageView mBGView;
    private HeightVSWidthImageView mBG2View;
    private Button mReuploadView;
    private DialogImageView mSampleView;
    private TextView mTitleView;
    private DialogTextView mSampleTitleView;

    public ChoosePictureLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater.from(context).inflate(R.layout.fragment_illegal_processing_photo, this);

        mTitleView = (TextView) findViewById(R.id.photo_title);
        mAreaView = (RelativeLayout) findViewById(R.id.photo_area);
        mBGView = (HeightVSWidthImageView) findViewById(R.id.photo_bg);
        mBG2View = (HeightVSWidthImageView) findViewById(R.id.photo_bg_2);
        mReuploadView = (Button) findViewById(R.id.photo_reupload);
        mSampleView = (DialogImageView) findViewById(R.id.photo_sample);
        mSampleTitleView = (DialogTextView) findViewById(R.id.photo_sample_title);
    }

    public void setTitle(CharSequence title) {
        mTitleView.setText(title);
    }

    public RelativeLayout getAreaView() {
        return mAreaView;
    }

    public HeightVSWidthImageView getBGView() {
        return mBGView;
    }

    public HeightVSWidthImageView getBG2View() {
        return mBG2View;
    }

    public Button getReuploadView() {
        return mReuploadView;
    }

    public DialogImageView getSampleView() {
        return mSampleView;
    }

    public TextView getTitleView() {
        return mTitleView;
    }

    public DialogTextView getSampleTitleView() {
        return mSampleTitleView;
    }
}
