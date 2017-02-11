package com.hxqc.mall.photolibrary.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.widget.CheckBox;
import android.widget.RelativeLayout;

import com.hxqc.mall.core.R;
import com.hxqc.mall.photolibrary.control.GalleryControl;
import com.hxqc.mall.photolibrary.fragment.ImagePagerFragment;
import com.hxqc.mall.photolibrary.model.MediaImgEntity;


/**
 * 图集  评论   图片预览
 */
public class PreviewImageActivity extends FragmentActivity implements ViewPager.OnPageChangeListener, View.OnClickListener {

	private static final String PAGERFRAGMENTTAG_STRING = "pagerfragment";
	CheckBox mCheckView;
	ImagePagerFragment imagePagerFragment;
	GalleryControl mControl;
	int position;
	RelativeLayout bg;
	Bundle bundle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_preview_image);
		bg = (RelativeLayout) findViewById(R.id.rl_bg_pl_image);
		mControl = GalleryControl.getDefault(this);
		mCheckView = (CheckBox) findViewById(R.id.preview_checkbox);
		mCheckView.setOnClickListener(this);
		if (savedInstanceState == null) {
			bundle = getIntent().getBundleExtra("bundle");
			position = bundle.getInt("position");
			imagePagerFragment = (ImagePagerFragment) ImagePagerFragment
					.instantiate(this, ImagePagerFragment.class.getName(),
							bundle);
			imagePagerFragment.setPageChangeListener(this);
			getSupportFragmentManager()
					.beginTransaction()
					.add(R.id.container_pre, imagePagerFragment,
							PAGERFRAGMENTTAG_STRING).commit();
			initView();
		}


	}

	public void initView() {
		final int left = bundle.getInt("locationX", 0);
		final int top = bundle.getInt("locationY", 0);
		final int width = bundle.getInt("width", 0);
		final int height = bundle.getInt("height", 0);
		bg.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
			@Override
			public boolean onPreDraw() {
				bg.getViewTreeObserver().removeOnPreDrawListener(this);
				int location[] = new int[2];
				bg.getLocationOnScreen(location);
				mLeft = left - location[0];
				mTop = top - location[1];
				mScaleX = width * 1.0f / bg.getWidth();
				mScaleY = height * 1.0f / bg.getHeight();
				activityEnterAnim();
				return true;
			}
		});

	}

	@Override
	public void onClick(View v) {
		mControl.checkChange(mCheckView, (MediaImgEntity) mCheckView.getTag());
	}

	@Override
	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

	}

	@Override
	public void onPageSelected(int position) {
		changeCheckBoxState(imagePagerFragment.getCurrentItem());
	}

	private void changeCheckBoxState(MediaImgEntity entity) {
		mCheckView.setTag(entity);
		if (mControl.getSelectEntitySet().contains(entity)) {
			mCheckView.setChecked(true);
		} else {
			mCheckView.setChecked(false);
		}
	}

	@Override
	public void onPageScrollStateChanged(int state) {

	}

	private int mLeft;
	private int mTop;
	private float mScaleX;
	private float mScaleY;

	private void activityEnterAnim() {

		bg.setPivotX(0);
		bg.setPivotY(0);
		bg.setScaleX(mScaleX);
		bg.setScaleY(mScaleY);
		bg.setTranslationX(mLeft);
		bg.setTranslationY(mTop);
		bg.animate().scaleX(1).scaleY(1).translationX(0).translationY(0).alphaBy(0f).
				setDuration(400).setInterpolator(new DecelerateInterpolator()).start();
	}
}
