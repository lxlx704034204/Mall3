package com.hxqc.mall.photolibrary.control;

import android.content.Context;
import android.widget.CheckBox;

import com.hxqc.mall.config.application.SampleApplicationContext;
import com.hxqc.mall.core.util.ToastHelper;
import com.hxqc.mall.photolibrary.model.MediaImgEntity;
import com.hxqc.mall.photolibrary.util.MediaImgUtil;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Author: wanghao
 * Date: 2015-03-24
 * FIXME
 * Todo
 */
public class GalleryControl {

	static GalleryControl mControl;
	public int maxCount = 8;
	public List<MediaImgEntity> queryGallery;
	private Set<MediaImgEntity> mSelectEntitySet;

	public GalleryControl(final Context context) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				queryGallery = MediaImgUtil.getThumbnailsEntities(context);
			}
		}).start();
		mSelectEntitySet = new LinkedHashSet<>();
	}

	public static GalleryControl getDefault(Context context) {
		if (mControl == null) {
			synchronized ((GalleryControl.class)) {
				if (mControl == null) {
					mControl = new GalleryControl(context);
				}
			}
		}
		return mControl;
	}

	public void unRegister() {
		if (mControl != null) {
			mControl = null;
		}
	}

	public boolean checkChange(CheckBox view, MediaImgEntity value) {
		if (!checkChange(view.isChecked(), value)) {
			view.setChecked(false);
			return false;
		}
		return true;
	}

	public boolean checkChange(boolean check, MediaImgEntity value) {
		if (mSelectEntitySet.size() >= maxCount && check) {
			ToastHelper.showYellowToast(SampleApplicationContext.application, "最多选择 " + maxCount + " 张图");
//            Toast.makeText(SampleApplicationContext.application, "最多选择 "+maxCount+" 张图", Toast.LENGTH_SHORT).show();
			return false;
		}
		if (check) {
			mSelectEntitySet.add(value);
		} else {
			mSelectEntitySet.remove(value);
		}
		return true;
	}

	public MediaImgEntity[] getPreviewEntity(boolean isPreview) {
		MediaImgEntity[] mediaImgEntities;
		if (isPreview) {
			mediaImgEntities = new MediaImgEntity[mSelectEntitySet.size()];
			return mSelectEntitySet.toArray(mediaImgEntities);
		} else {
			mediaImgEntities = new MediaImgEntity[queryGallery.size()];
			return queryGallery.toArray(mediaImgEntities);
		}
	}

	/**
	 * 获取已选择图片
	 *
	 * @return
	 */
	public Set<MediaImgEntity> getSelectEntitySet() {
		return mSelectEntitySet;
	}

}
