//package com.hxqc.mall.photolibrary.activity;
//
//import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
//import android.view.View;
//import android.widget.TextView;
//
//import com.hxqc.mall.core.R;
//import com.hxqc.mall.photolibrary.control.GalleryControl;
//import com.hxqc.mall.photolibrary.fragment.GridPhotoFragment;
//import com.umeng.analytics.MobclickAgent;
//
//
//public class GPhotoActivity extends AppCompatActivity {
//
//	GalleryControl mGalleyControl;
//
//	TextView mSizeView;
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_gphoto);
//		mGalleyControl = GalleryControl.getDefault(this);
//		getSupportFragmentManager().beginTransaction().
//				add(R.id.rl_content_layout, new GridPhotoFragment(), "grid").commit();
//		mSizeView = (TextView) findViewById(R.id.send_image_size_textview);
//		mSizeView.setText("");
//	}
//
//	public void preview(View view) {
//		((GridPhotoFragment) getSupportFragmentManager().findFragmentByTag("grid")).toPreview();
//	}
//
//	public void commit(View view) {
//		finish();
//	}
//
//	@Override
//	protected void onDestroy() {
//		super.onDestroy();
//		mGalleyControl.unRegister();
//	}
//
//	public void onResume() {
//		super.onResume();
//		MobclickAgent.onResume(this);
//	}
//
//	public void onPause() {
//		super.onPause();
//		MobclickAgent.onPause(this);
//	}
//}
