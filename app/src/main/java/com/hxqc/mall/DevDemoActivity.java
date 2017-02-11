package com.hxqc.mall;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;

import com.hxqc.mall.activity.LaunchActivity;
import com.hxqc.mall.activity.NoBackActivity;
import com.hxqc.mall.core.api.ApiUtil;
import com.hxqc.mall.reactnative.util.RNConfigUtil;
import com.hxqc.util.DebugLog;

import hxqc.mall.R;


public class DevDemoActivity extends NoBackActivity {
	CheckBox httpsView;
	String http = "https";

	protected int getContentViewLayout() {
		return R.layout.activity_dev_demo;
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(getContentViewLayout());

		httpsView = (CheckBox) findViewById(R.id.checkBox);
		httpsView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				boolean isHttpS = httpsView.isChecked();
				if (isHttpS) {
					http = "https";
				} else {
					http = "http";
				}
			}
		});

	}

	public void to203(View view) {
		RNConfigUtil.isRNDebug = false;
		DebugLog.setDebug(true);
		ApiUtil.isDebug = true;
		ApiUtil.setTestHostUrl("http://app-interface.t.hxqctest.com/");
		ApiUtil.AccessoryHostURL = "http://accessory-goods.tadmin.hxqctest.com/accessory/";
		ApiUtil.MaintainHostURL = "http://maintain-interface-goods.tadmin.hxqctest.com/";
		ApiUtil.UsedCarHostURL = "http://app-interface.t.hxqctest.com/";
		ApiUtil.Host_Insurance = "http://value-added.tm.hxqctest.com/";
		ApiUtil.APPConfigHostURL = "http://10.0.15.205:8361";
		ApiUtil.RNBsdiffURL = "http://10.0.15.205:8361";
		ApiUtil.ConfigAppKey = "4729e28f-ded5-4b71-8124-21d88af41817";
		ApiUtil.UsedCarAuctionHostURL = "http://usedcar.tm.hxqctest.com";
		DebugLog.w("bsdiff_rn", "to203: " + ApiUtil.getHomeRNURLHost());
		Intent intent = new Intent(this, LaunchActivity.class);
		startActivity(intent);
	}

	public void toReleaseTest(View view) {
		RNConfigUtil.isRNDebug = false;
		DebugLog.setDebug(true);
		ApiUtil.isDebug = false;
		ApiUtil.setTestHostUrl(http + "://app-interface.t.hxqc.com/");
		ApiUtil.AccessoryHostURL = http + "://accessory-goods.tadmin.hxqc.com/";
		ApiUtil.MaintainHostURL = http + "://maintain-interface-goods.tadmin.hxqc.com/";
		ApiUtil.UsedCarHostURL = http + "://usedcar-interface.t.hxqc.com/";
		ApiUtil.Host_Insurance = http + "://value-added.tm.hxqc.com";
		ApiUtil.APPConfigHostURL = http + "://appconf.t.hxqc.com";
		ApiUtil.RNBsdiffURL = http + "://appconf.t.hxqc.com";
		ApiUtil.ConfigAppKey = "f9faee90-3476-46f2-872d-86e3b2b45921";
		ApiUtil.UsedCarAuctionHostURL = "http://usedcar.tm.hxqc.com";
		DebugLog.w("bsdiff_rn", "toReleaseTest: " + ApiUtil.getHomeRNURLHost());
		Intent intent = new Intent(this, LaunchActivity.class);
		startActivity(intent);
	}

	/**
	 * 预上线不加密
	 *
	 * @param view
	 */
	public void toReleaseTest2(View view) {
		RNConfigUtil.isRNDebug = false;
		DebugLog.setDebug(true);
		ApiUtil.isDebug = true;
		ApiUtil.setTestHostUrl(http + "://app-interface.t.hxqc.com/");
		ApiUtil.AccessoryHostURL = http + "://accessory-goods.tadmin.hxqc.com/";
		ApiUtil.MaintainHostURL = http + "://maintain-interface-goods.tadmin.hxqc.com/";
		ApiUtil.UsedCarHostURL = http + "://usedcar-interface.t.hxqc.com/";
		ApiUtil.Host_Insurance = http + "://value-added.tm.hxqc.com";
		ApiUtil.APPConfigHostURL = http + "://appconf.t.hxqc.com";
		ApiUtil.RNBsdiffURL = http + "://appconf.t.hxqc.com";
		ApiUtil.ConfigAppKey = "f9faee90-3476-46f2-872d-86e3b2b45921";
		ApiUtil.UsedCarAuctionHostURL = "http://usedcar.tm.hxqc.com";
		DebugLog.w("bsdiff_rn", "toReleaseTest2: " + ApiUtil.getHomeRNURLHost());
		Intent intent = new Intent(this, LaunchActivity.class);
		startActivity(intent);

	}

	public void toRelease(View view) {
		DebugLog.setDebug(true);
		ApiUtil.isDebug = false;
		ApiUtil.setTestHostUrl(http + "://app-interface.hxqc.com/");
		ApiUtil.AccessoryHostURL = http + "://accessory-goods.admin.hxqc.com/";
		ApiUtil.MaintainHostURL = http + "://maintain-interface-goods.admin.hxqc.com/";
		ApiUtil.UsedCarHostURL = http + "://usedcar-interface.hxqc.com/";
		ApiUtil.Host_Insurance = http + "://value-added.m.hxqc.com";
		ApiUtil.APPConfigHostURL = http + "://appconf.hxqc.com";
		ApiUtil.RNBsdiffURL = http + "://appconf.hxqc.com";
		ApiUtil.ConfigAppKey = "6d91569d-173b-48a9-82bd-4e51c9abbc6b";
		ApiUtil.UsedCarAuctionHostURL = "http://usedcar.m.hxqc.com";
		Intent intent = new Intent(this, LaunchActivity.class);
		startActivity(intent);
	}

}
