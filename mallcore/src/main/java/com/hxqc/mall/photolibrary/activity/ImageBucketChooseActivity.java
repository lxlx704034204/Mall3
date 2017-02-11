package com.hxqc.mall.photolibrary.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.hxqc.mall.core.R;
import com.hxqc.mall.activity.BackActivity;
import com.hxqc.mall.core.util.ToastHelper;
import com.hxqc.mall.photolibrary.adapter.ImageBucketAdapter;
import com.hxqc.mall.photolibrary.model.ImageBucket;
import com.hxqc.mall.photolibrary.util.CustomConstants;
import com.hxqc.mall.photolibrary.util.ImageFetcher;
import com.hxqc.mall.photolibrary.util.IntentConstants;
import com.umeng.analytics.MobclickAgent;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 显示相册列表
 */

public class ImageBucketChooseActivity extends BackActivity {

	private ImageFetcher mHelper;
	private List<ImageBucket> mDataList = new ArrayList<>();
	private ImageBucketAdapter mAdapter;
	private int availableSize;
	private static final int REQUEST_STORAGE_PERMISSION = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_bucket_choose);
		getSupportActionBar().setTitle("相册");
		//        6.0以上手动拿权限
		int checkSelfPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
		if (checkSelfPermission != PackageManager.PERMISSION_GRANTED) {
			ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_STORAGE_PERMISSION);
		} else {
			init();
		}


	}

	private void init() {
		mHelper = ImageFetcher.getInstance(getApplicationContext());
		initData();
		initView();
	}

	private void initData() {
		mDataList = mHelper.getImagesBucketList(false);
		availableSize = getIntent().getIntExtra(
				IntentConstants.EXTRA_CAN_ADD_IMAGE_SIZE,
				CustomConstants.MAX_IMAGE_SIZE);
	}

	private void initView() {
		ListView mListView = (ListView) findViewById(R.id.listview);
		mAdapter = new ImageBucketAdapter(this, mDataList);
		mListView.setAdapter(mAdapter);

		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
			                        int position, long id) {

				selectOne(position);

				Intent intent = new Intent(ImageBucketChooseActivity.this,
						ImageChooseActivity.class);
				intent.putExtra(IntentConstants.EXTRA_IMAGE_LIST,
						(Serializable) mDataList.get(position).imageList);
				intent.putExtra(IntentConstants.EXTRA_BUCKET_NAME,
						mDataList.get(position).bucketName);
				intent.putExtra(IntentConstants.EXTRA_CAN_ADD_IMAGE_SIZE,
						availableSize);

				startActivity(intent);
			}
		});
	}

	private void selectOne(int position) {
		int size = mDataList.size();
		for (int i = 0; i != size; i++) {
			mDataList.get(i).selected = i == position;
		}
		mAdapter.notifyDataSetChanged();
	}

	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		switch (requestCode) {
			case REQUEST_STORAGE_PERMISSION:
				if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					init();
				} else {
					ToastHelper.showRedToast(this, "获取权限失败");
				}
				break;
		}
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
	}
}
