package com.hxqc.mall.fragment.comment;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.hxqc.mall.core.model.ImageModel;
import com.hxqc.mall.core.util.activityutil.ActivitySwitcher;
import com.hxqc.mall.photolibrary.fragment.BaseImagePagerFragment;

import java.util.ArrayList;

import hxqc.mall.R;


/**
 * Author: wanghao
 * Date: 2015-04-01
 * FIXME
 * 大图查看
 */
public class ImageInfoFragment extends BaseImagePagerFragment {


    ArrayList< ImageModel > imageModels;
    int position;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        imageModels = bundle.getParcelableArrayList(ActivitySwitcher.viewLargePics);
        position = bundle.getInt(ActivitySwitcher.viewLargePosition);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewPager.setCurrentItem(position);
    }

    @Override
    public int getPageSize() {
        return imageModels.size();
    }

    @Override
    public String getShowPagePath(int position) {
        return imageModels.get(position).largeImage;
    }

    @Override
    public int getCreateRootViewTag() {
        return BaseImagePagerFragment.NORMAL_VIEW_PAGE;
    }


    @Override
    public String fragmentDescription() {
        return getResources().getString(R.string.fragment_description_image_info);
    }
}
