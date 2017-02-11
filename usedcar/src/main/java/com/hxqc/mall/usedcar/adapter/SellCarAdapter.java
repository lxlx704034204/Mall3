package com.hxqc.mall.usedcar.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hxqc.mall.core.util.ImageUtil;
import com.hxqc.mall.core.util.OtherUtil;
import com.hxqc.mall.photolibrary.model.ImageItem;
import com.hxqc.mall.usedcar.R;
import com.hxqc.mall.usedcar.activity.SellCarActivity;
import com.hxqc.util.DisplayTools;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
 * @Author : 钟学东
 * @Since : 2015-08-12
 * FIXME
 * Todo
 */
public class SellCarAdapter extends BaseAdapter {

	public String delete = "";
	Context context;
	List<ImageItem> mDataList = new ArrayList<>();
	int[] pictures = new int[]{R.mipmap.sellthecar_bg_1, R.mipmap.sellthecar_bg_7, R.mipmap.sellthecar_bg_4, R.mipmap.sellthecar_bg_5, R.mipmap.sellthecar_bg_6};
	String[] titles = new String[]{"正45度", "背面", "中控", "发动机", "后备箱"};

	public void initAdapter(Context context, List<ImageItem> mDataList) {
		this.context = context;
		this.mDataList = mDataList;
	}

	public String getDelete() {
		return delete;
	}

	@Override
	public int getCount() {
		return 5;
	}

	@Override
	public Object getItem(int position) {
		if (mDataList.size() != 0 && mDataList.size() > position) {
			return mDataList.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, final ViewGroup parent) {
		final ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = View.inflate(context, R.layout.item_sell_car_upload_photo, null);
			viewHolder.mCameraView = (ImageView) convertView.findViewById(R.id.camera);
			viewHolder.mCarPhotoView = (ImageView) convertView.findViewById(R.id.car_photo);
			viewHolder.mDeleteView = (ImageView) convertView.findViewById(R.id.delete);
			viewHolder.mTagView = (ImageView) convertView.findViewById(R.id.tag);
			viewHolder.mTitleView = (TextView) convertView.findViewById(R.id.title);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		setCover();
		viewHolder.mTitleView.setText(titles[position]);
		if (mDataList.size() != 0 && mDataList.get(position) != null && !TextUtils.isEmpty(mDataList.get(position).sourcePath)) {
			final ImageItem item = mDataList.get(position);
			viewHolder.mCameraView.setVisibility(View.GONE);
			viewHolder.mDeleteView.setVisibility(View.VISIBLE);
			OtherUtil.setVisible(viewHolder.mTagView, item.isFigure);
			if (item.isFigure) {
				((SellCarActivity) context).cover = position;
			}
			if (!TextUtils.isEmpty(item.sourcePath)) {
				if (item.getTitle() != null) {
					ImageUtil.setImageResize(context, viewHolder.mCarPhotoView, item.sourcePath, DisplayTools.dip2px(context, 78), DisplayTools.dip2px(context, 65));
				} else {
					ImageUtil.setImageResize(context, viewHolder.mCarPhotoView, new File(item.sourcePath), DisplayTools.dip2px(context, 78), DisplayTools.dip2px(context, 65));

				}
			}
			viewHolder.mDeleteView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if (mDataList.get(position).isFigure) {
						for (int i = 0; i < 5; i++) {
							if (!TextUtils.isEmpty(mDataList.get(position).sourcePath) && position != i) {
								mDataList.get(i).isFigure = true;
								((SellCarActivity) context).cover = i;
								break;
							}
						}
					}
					String temple = "";
					if (!TextUtils.isEmpty(item.mTitle)) {
						temple = temple + item.getTitle() + ",";
						delete = delete + temple;
					}
					mDataList.remove(position);
					mDataList.add(position, new ImageItem(""));
					notifyDataSetChanged();
				}
			});
		} else {
			viewHolder.mTagView.setVisibility(View.GONE);
			viewHolder.mCameraView.setVisibility(View.VISIBLE);
			viewHolder.mDeleteView.setVisibility(View.GONE);
			ImageUtil.setImageResize(context, viewHolder.mCarPhotoView, pictures[position], DisplayTools.dip2px(context, 78), DisplayTools.dip2px(context, 65));
		}

		return convertView;
	}

	private void setCover() {
		if (!((SellCarActivity) context).chosenCover) {
			for (int i = 0; i < 5; i++) {
				if (!TextUtils.isEmpty(mDataList.get(i).sourcePath)) {
					mDataList.get(i).isFigure = true;
					for (int j = i + 1; j < 5; j++) {
						mDataList.get(j).isFigure = false;
					}
					break;
				}
			}
		}
	}


	private class ViewHolder {
		ImageView mCarPhotoView;
		ImageView mCameraView;
		ImageView mDeleteView;
		ImageView mTagView;
		TextView mTitleView;
	}
}
