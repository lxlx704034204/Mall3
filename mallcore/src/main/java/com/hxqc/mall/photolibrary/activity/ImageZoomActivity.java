package com.hxqc.mall.photolibrary.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.hxqc.mall.core.R;
import com.hxqc.mall.activity.BackActivity;
import com.hxqc.mall.photolibrary.model.EventImages;
import com.hxqc.mall.photolibrary.model.ImageItem;
import com.hxqc.mall.photolibrary.util.ImageDisplayer;
import com.hxqc.mall.photolibrary.util.IntentConstants;

import java.util.ArrayList;
import java.util.List;

import org.greenrobot.eventbus.EventBus;

import uk.co.senab.photoview.PhotoView;


/**
 * 照片选择中的 预览
 */
public class ImageZoomActivity extends BackActivity {

	private ViewPager pager;
	private MyPageAdapter adapter;
	private int currentPosition;
	private List<ImageItem> mDataList = new ArrayList<ImageItem>();
	private TextView mPageNum;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_zoom);
		getSupportActionBar().setTitle("照片预览");

		mPageNum = (TextView) findViewById(R.id.tv_page_number_sc);
		initData();

		Button photo_bt_del = (Button) findViewById(R.id.photo_bt_del);
		photo_bt_del.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (mDataList.size() < 1 || mDataList == null) return;
				removeImg(currentPosition);
			}
		});

		adapter = new MyPageAdapter(mDataList);

		pager = (ViewPager) findViewById(R.id.viewpager);
		pager.setOnPageChangeListener(pageChangeListener);
		pager.setAdapter(adapter);
		pager.setCurrentItem(currentPosition);

		mPageNum.setText(pager.getCurrentItem() + 1 + "/" + pager.getAdapter().getCount());
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mDataList != null) {
			mDataList.clear();
			mDataList = null;
		}
	}

	/**
	 * 初始化所有已选图片数据
	 */
	@SuppressWarnings("unchecked")
	private void initData() {
		currentPosition = getIntent().getIntExtra(
				IntentConstants.EXTRA_CURRENT_IMG_POSITION, 0);
		mDataList = (List<ImageItem>) getIntent().getSerializableExtra(IntentConstants.EXTRA_IMAGE_LIST);
	}

	//删除当前图片
	private void removeImg(int location) {
		if (location + 1 <= mDataList.size()) {
			mDataList.remove(location);
			EventBus.getDefault().post(new EventImages(mDataList, IntentConstants.PREVIEW_TYPE));
			adapter.notifyDataSetChanged();
			pager.postInvalidate();
			if (mDataList.size() == 0)
				finish();
		}
	}

	private OnPageChangeListener pageChangeListener = new OnPageChangeListener() {

		public void onPageSelected(int arg0) {
			mPageNum.setText(arg0 + 1 + "/" + pager.getAdapter().getCount());
			currentPosition = arg0;
		}

		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		public void onPageScrollStateChanged(int arg0) {

		}
	};

	class MyPageAdapter extends PagerAdapter {
		private List<ImageItem> dataList = new ArrayList<>();

		public MyPageAdapter(List<ImageItem> dataList) {
			this.dataList = dataList;
		}

		public int getItemPosition(Object object) {
			return POSITION_NONE;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			PhotoView photoView = new PhotoView(container.getContext());
			photoView.setBackgroundColor(Color.BLACK);

			container.addView(photoView, ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.MATCH_PARENT);

			ImageDisplayer.getInstance(ImageZoomActivity.this).displayBmp(
					photoView, null, dataList.get(position).sourcePath, false);

			return photoView;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public int getCount() {
			return dataList.size();
		}

		@Override
		public void notifyDataSetChanged() {
			super.notifyDataSetChanged();
			if (currentPosition != mDataList.size())
				mPageNum.setText(currentPosition + 1 + "/" + pager.getAdapter().getCount());
		}
	}
}
