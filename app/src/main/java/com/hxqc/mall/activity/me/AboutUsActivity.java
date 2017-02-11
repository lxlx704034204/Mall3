package com.hxqc.mall.activity.me;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.hxqc.mall.activity.AppBackActivity;
import com.hxqc.mall.core.util.activityutil.ActivitySwitcher;

import hxqc.mall.BuildConfig;
import hxqc.mall.R;

/**
 * 说明:关于我们
 * <p>
 * author: 吕飞
 * since: 2015-03-20
 * Copyright:恒信汽车电子商务有限公司
 */
public class AboutUsActivity extends AppBackActivity implements View.OnClickListener {
	private static final String WEIBO = "http://weibo.com/CHINAHXQC";


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about_us);
		//手势关闭
//        SlidrConfig config = new SlidrConfig.Builder()
//                .primaryColor(getResources().getColor(R.color.primary))
//                .sensitivity(1f).build();
//        Slidr.attach(this, config);
		TextView mVersionNameView = (TextView) findViewById(R.id.version);
		TextView mSloganNameView = (TextView) findViewById(R.id.slogan);
		TextView mOfficialWebsiteView = (TextView) findViewById(R.id.official_website);
		TextView mWeiboView = (TextView) findViewById(R.id.weibo);

		mVersionNameView.setText(getResources().getString(R.string.me_version) + " " + BuildConfig.VersionTime);
		mSloganNameView.setText(getString(R.string.scan_qr_code));
		mOfficialWebsiteView.setOnClickListener(this);
		mWeiboView.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		int i = v.getId();
		switch (i) {
			case R.id.official_website:
//                ActivitySwitcher.toWebBrowser(OFFICIAL_WEBSITE, this);
//                ActivitySwitcher.toAdvantage(this);
				ActivitySwitcher.toOfficialWebsite(this);
				break;

			case R.id.weibo:
				ActivitySwitcher.toWebBrowser(WEIBO, this);
				break;
		}

	}
}
