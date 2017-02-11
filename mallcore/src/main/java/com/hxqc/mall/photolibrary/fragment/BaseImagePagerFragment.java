package com.hxqc.mall.photolibrary.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.hxqc.mall.core.R;
import com.hxqc.mall.core.fragment.FunctionFragment;
import com.hxqc.mall.core.util.ImageUtil;
import com.hxqc.mall.core.util.ToastHelper;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;


/**
 * Author: wanghao
 * Date: 2015-03-24
 * FIXME
 * Todo
 */
public abstract class BaseImagePagerFragment extends FunctionFragment {

	protected ViewPager mViewPager;
	private TextView mPageNum;
	//    DisplayImageOptions options;
	ViewPager.OnPageChangeListener mChangeListener;
	ImageButton image_save;

	public final static int NORMAL_VIEW_PAGE = 0;
	public final static int NEWS_VIEW_PAGE = 1;

	public BaseImagePagerFragment() {
	}

	public abstract int getPageSize();

	public abstract String getShowPagePath(int position);

	public abstract int getCreateRootViewTag();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//        options = new DisplayImageOptions.Builder()
//                .resetViewBeforeLoading(true).cacheOnDisk(true)
//                .imageScaleType(ImageScaleType.EXACTLY)
//                .bitmapConfig(Bitmap.Config.RGB_565).considerExifParams(true)
//                .displayer(new FadeInBitmapDisplayer(300)).build();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View rootView = null;
		if (getCreateRootViewTag() == NORMAL_VIEW_PAGE) {
			rootView = inflater.inflate(R.layout.fragment_image_page, container, false);
		} else if (getCreateRootViewTag() == NEWS_VIEW_PAGE) {
			rootView = inflater.inflate(R.layout.fragment_news_image_page, container, false);
		}
		assert rootView != null;

		mViewPager = (ViewPager) rootView.findViewById(R.id.hvp_pager);
		mPageNum = (TextView) rootView.findViewById(R.id.tv_page_number);
		image_save = (ImageButton) rootView.findViewById(R.id.ib_save_image);
		return rootView;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mViewPager.setAdapter(new ImageAdapter());
		if (mChangeListener != null) {
			mViewPager.setOnPageChangeListener(mChangeListener);
		}
		mViewPager.setCurrentItem(8, true);
		mPageNum.setText(mViewPager.getCurrentItem() + 1 + "/" + mViewPager.getAdapter().getCount());
		mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

			}

			@Override
			public void onPageSelected(int position) {
				mPageNum.setText(position + 1 + "/" + mViewPager.getAdapter().getCount());
			}

			@Override
			public void onPageScrollStateChanged(int state) {

			}
		});

		image_save.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				int i = mViewPager.getCurrentItem();
//                Toast.makeText(getActivity(), getShowPagePath(i) + "--==" + i, Toast.LENGTH_SHORT).show();
				ToastHelper.showGreenToast(getActivity(), "保存成功！");
				//保存图片
				saveImage(getShowPagePath(i));
			}
		});

		image_save.setVisibility(View.INVISIBLE);
	}

	//保存图片
	private void saveImage(String url) {
		byte[] data = getImage(url);
		Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
		// 首先保存图片
		File appDir = new File(Environment.getExternalStorageDirectory(), "hxqc");
		if (!appDir.exists()) {
			appDir.mkdir();
		}
		String fileName = System.currentTimeMillis() + ".jpg";
		File file = new File(appDir, fileName);
		try {
			FileOutputStream fos = new FileOutputStream(file);
			bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
			fos.flush();
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// 其次把文件插入到系统图库
		try {
			MediaStore.Images.Media.insertImage(getActivity().getContentResolver(),
					file.getAbsolutePath(), fileName, null);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		// 最后通知图库更新
		getActivity().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + file.getAbsolutePath())));
	}

	//拿到网络图片
	public static byte[] getImage(String path) {
		byte[] data = null;
		try {
			//建立URL
			URL url = new URL(path);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setReadTimeout(5000);

			InputStream input = conn.getInputStream();
			data = readInputStream(input);
			input.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return data;
	}

	//转换流
	public static byte[] readInputStream(InputStream input) {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		try {
			byte[] buffer = new byte[1024];
			int len;
			while ((len = input.read(buffer)) != -1) {
				output.write(buffer, 0, len);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return output.toByteArray();
	}

	/**
	 * @param mChangeListener the mChangeListener to set
	 */
	public void setPageChangeListener(ViewPager.OnPageChangeListener mChangeListener) {
		this.mChangeListener = mChangeListener;
	}

	private class ImageAdapter extends PagerAdapter {

		private LayoutInflater inflater;

		ImageAdapter() {
			inflater = LayoutInflater.from(getActivity());
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		@Override
		public int getCount() {
			return getPageSize();
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {

			PhotoView photoView = new PhotoView(container.getContext());
			photoView.setBackgroundColor(Color.BLACK);
			container.addView(photoView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

//			ImageUtil.setImage(container.getContext(), photoView, getShowPagePath(position));
			ImageUtil.setImageNormalSize(container.getContext(), photoView, getShowPagePath(position));
			photoView.setOnViewTapListener(new PhotoViewAttacher.OnViewTapListener() {
				@Override
				public void onViewTap(View view, float x, float y) {
					getActivity().finish();
				}
			});

			return photoView;
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view.equals(object);
		}

		@Override
		public void restoreState(Parcelable state, ClassLoader loader) {
		}

		@Override
		public Parcelable saveState() {
			return null;
		}
	}

}
