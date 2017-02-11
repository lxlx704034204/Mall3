package com.hxqc.mall.photolibrary.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;

import com.hxqc.mall.core.R;
import com.hxqc.mall.photolibrary.control.GalleryControl;
import com.hxqc.mall.photolibrary.model.MediaImgEntity;

/**
 * Author: wanghao
 * Date: 2015-03-24
 * FIXME
 * Todo
 */
public class ImagePagerFragment extends BaseImagePagerFragment {

	GalleryControl mControl;
	boolean isPreview;
	int position;
	MediaImgEntity[] mediaImgEntities;

	public ImagePagerFragment() {
	}

	@Override
	public int getPageSize() {
		return mediaImgEntities == null ? 0 : mediaImgEntities.length;
	}

	@Override
	public String getShowPagePath(int position) {
		return "file://" + mediaImgEntities[position].img_path;
	}

	@Override
	public int getCreateRootViewTag() {
		return NORMAL_VIEW_PAGE;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mControl = GalleryControl.getDefault(getActivity());
		position = getArguments().getInt("position");
		isPreview = getArguments().getBoolean("isPreview");
		mediaImgEntities = mControl.getPreviewEntity(isPreview);
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mViewPager.setCurrentItem(position, true);

	}

	public MediaImgEntity getCurrentItem() {
		return mediaImgEntities[mViewPager.getCurrentItem()];
	}


	@Override
	public String fragmentDescription() {
		return getResources().getString(R.string.fragment_description_image);
	}
}
