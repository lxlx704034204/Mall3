package com.hxqc.mall.photolibrary.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.hxqc.mall.core.R;
import com.hxqc.mall.core.util.ImageUtil;
import com.hxqc.mall.photolibrary.control.GalleryControl;
import com.hxqc.mall.photolibrary.model.MediaImgEntity;

/**
 * Author: wanghao
 * Date: 2015-03-24
 * FIXME
 * Todo
 */
public class GridPhotoAdapter extends BaseAdapter implements View.OnClickListener {

	GalleryControl mControl;
	boolean hasCamera = false;
	//    DisplayImageOptions options;
	int itemLayout = R.layout.item_grid_image;
	private LayoutInflater inflater;
	Context context;

	public GridPhotoAdapter(Context context, boolean hasCamera) {
		init(context, hasCamera);
	}

	public GridPhotoAdapter(Context context, boolean hasCamera, int itemLayoutResources) {
		init(context, hasCamera);
		this.itemLayout = itemLayoutResources;
	}

	private void init(Context context, boolean hasCamera) {
		inflater = LayoutInflater.from(context);
		mControl = GalleryControl.getDefault(context);
		this.context = context;
		this.hasCamera = hasCamera;
//        options = new DisplayImageOptions.Builder().delayBeforeLoading(0)
//                .resetViewBeforeLoading(true).bitmapConfig(Bitmap.Config.ARGB_4444)
//                .showImageOnLoading(R.drawable.bg_imgloading).displayer(new FadeInBitmapDisplayer(300))
//                .cacheInMemory(true).cacheOnDisk(true).build();
	}

	@Override
	public int getCount() {
		if (hasCamera) {
			return mControl.queryGallery == null ? 1 : mControl.queryGallery.size() + 1;
		}
		return mControl.queryGallery == null ? 0 : mControl.queryGallery.size();
	}

	@Override
	public MediaImgEntity getItem(int position) {
		return mControl.queryGallery.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if (convertView == null) {
			convertView = inflater.inflate(itemLayout, parent, false);
			holder = new ViewHolder();
			assert convertView != null;
			holder.imageView = (ImageView) convertView.findViewById(R.id.siv_image);
			holder.mCheckBox = (CheckBox) convertView.findViewById(R.id.cb_checkbox);
			holder.mCheckBox.setOnClickListener(this);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if (position == 0) {
			holder.mCheckBox.setVisibility(View.INVISIBLE);
			holder.imageView.setImageResource(R.drawable.icon_camera);
		} else {
			position = position - 1;
			holder.mCheckBox.setVisibility(View.VISIBLE);
			String path = getItem(position).getThumbnails_path();
			ImageUtil.setImage(context, holder.imageView, path);
			MediaImgEntity entity = getItem(position);
			holder.mCheckBox.setTag(getItem(position));
			if (mControl.getSelectEntitySet().contains(entity)) {
				holder.mCheckBox.setChecked(true);
			} else {
				holder.mCheckBox.setChecked(false);
			}
		}
		return convertView;
	}

	@Override
	public void onClick(View v) {
		mControl.checkChange((CheckBox) v, (MediaImgEntity) v.getTag());
	}

	static class ViewHolder {
		ImageView imageView;
		CheckBox mCheckBox;
	}
}
