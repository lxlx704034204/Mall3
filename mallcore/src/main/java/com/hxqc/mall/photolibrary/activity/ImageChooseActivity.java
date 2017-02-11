package com.hxqc.mall.photolibrary.activity;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;

import com.hxqc.mall.activity.BackActivity;
import com.hxqc.mall.core.util.ToastHelper;
import com.hxqc.mall.core.R;
import com.hxqc.mall.photolibrary.adapter.ImageGridAdapter;
import com.hxqc.mall.photolibrary.model.EventImages;
import com.hxqc.mall.photolibrary.model.ImageItem;
import com.hxqc.mall.photolibrary.util.CustomConstants;
import com.hxqc.mall.photolibrary.util.IntentConstants;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 相册中  照片选择
 */
public class ImageChooseActivity extends BackActivity {

	private List<ImageItem> mDataList = new ArrayList<>();
	private int availableSize;

	private GridView mGridView;

	private ImageGridAdapter mAdapter;
	private Button mFinishBtn;
	private HashMap<String, ImageItem> selectedImgs = new HashMap<>();

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_choose);

		getSupportActionBar().setTitle("选择照片");

		mDataList = (List<ImageItem>) getIntent().getSerializableExtra(
				IntentConstants.EXTRA_IMAGE_LIST);
		if (mDataList == null) mDataList = new ArrayList<>();
		String mBucketName = getIntent().getStringExtra(
				IntentConstants.EXTRA_BUCKET_NAME);

		if (TextUtils.isEmpty(mBucketName)) {
			mBucketName = "请选择";
		}
		int i = getAvailableSize();
		int j = getIntent().getIntExtra(
				IntentConstants.EXTRA_CAN_ADD_IMAGE_SIZE,
				CustomConstants.MAX_IMAGE_SIZE);
		if (i < j) {
			availableSize = i;
		} else {
			availableSize = j;
		}
		initView();
		initListener();
	}

	private void initView() {

		mGridView = (GridView) findViewById(R.id.gridview);
		mGridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		mAdapter = new ImageGridAdapter(ImageChooseActivity.this, mDataList);
		mGridView.setAdapter(mAdapter);
		mFinishBtn = (Button) findViewById(R.id.finish_btn);

		mFinishBtn.setText(String.format("完成(%d/%d)", selectedImgs.size(), availableSize));
		mAdapter.notifyDataSetChanged();
	}

	private void saveAvailableSize(int dSize) {
		SharedPreferences sp = getSharedPreferences(
				CustomConstants.APPLICATION_NAME, MODE_PRIVATE);
		sp.edit().putInt(CustomConstants.AVAILABLE_IMAGE_SIZE, dSize).apply();
	}

	private int getAvailableSize() {
		SharedPreferences sp = getSharedPreferences(
				CustomConstants.APPLICATION_NAME, MODE_PRIVATE);
		return sp.getInt(CustomConstants.AVAILABLE_IMAGE_SIZE, CustomConstants.MAX_IMAGE_SIZE);
	}

	private void initListener() {
		mFinishBtn.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {

				List<ImageItem> imageItems = new ArrayList<>(selectedImgs.values());
				saveAvailableSize(availableSize - imageItems.size());
				EventBus.getDefault().post(new EventImages(imageItems, IntentConstants.SELECT_TYPE));
				finish();
			}

		});

		mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
			                        int position, long id) {

				ImageItem item = mDataList.get(position);
				if (item.isSelected) {
					item.isSelected = false;
					selectedImgs.remove(item.imageId);
				} else {
					if (selectedImgs.size() >= availableSize) {
//                        Toast.makeText(ImageChooseActivity.this,
//                                "最多选择" + availableSize + "张图片",
//                                Toast.LENGTH_SHORT).show();
						ToastHelper.showRedToast(ImageChooseActivity.this,
								"最多选择" + availableSize + "张图片");

						return;
					}
					item.isSelected = true;
					selectedImgs.put(item.imageId, item);
				}

				mFinishBtn.setText("完成" + "(" + selectedImgs.size() + "/"
						+ availableSize + ")");
				mAdapter.notifyDataSetChanged();
			}

		});
	}
}
