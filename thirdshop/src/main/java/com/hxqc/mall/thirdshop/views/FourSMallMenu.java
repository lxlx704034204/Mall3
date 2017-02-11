package com.hxqc.mall.thirdshop.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hxqc.mall.auto.config.AutoInfoContants;
import com.hxqc.mall.core.util.ActivitySwitchBase;
import com.hxqc.mall.core.util.BaseSharedPreferencesHelper;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.accessory4s.utils.ActivitySwitcherAccessory4S;
import com.hxqc.mall.thirdshop.activity.FourSMallActivity;
import com.hxqc.mall.thirdshop.activity.GroupBuyMergeActivity;
import com.hxqc.mall.thirdshop.utils.ActivitySwitcherThirdPartShop;
import com.hxqc.util.DebugLog;

/**
 * 说明:4s店商城的目录
 *
 * @author: 吕飞
 * @since: 2016-05-05
 * Copyright:恒信汽车电子商务有限公司
 */
public class FourSMallMenu extends RelativeLayout implements View.OnClickListener {
	ImageView mMenuIconView;
	TextView mMenuTextView;
	int mMenuIcon;
	String mMenuText;
	BaseSharedPreferencesHelper mBaseSharedPreferencesHelper;

	public FourSMallMenu(Context context) {
		super(context);
		initView();
	}

	public FourSMallMenu(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
		TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.FourSMallMenu);
		mMenuIcon = typedArray.getResourceId(R.styleable.FourSMallMenu_menuIcon, 0);
		mMenuText = typedArray.getString(R.styleable.FourSMallMenu_menuText);
		typedArray.recycle();
		mMenuIconView.setImageResource(mMenuIcon);
		mMenuTextView.setText(mMenuText);
		setOnClickListener(this);
	}

	private void initView() {
		LayoutInflater.from(getContext()).inflate(R.layout.view_4s_mall_menu, this);
		mMenuIconView = (ImageView) findViewById(R.id.menu_icon);
		mMenuTextView = (TextView) findViewById(R.id.menu_text);
		mBaseSharedPreferencesHelper = new BaseSharedPreferencesHelper(getContext());
	}

	@Override
	public void onClick(View v) {
		String siteID = ((FourSMallActivity) getContext()).getCityGroupID();

		switch (mMenuText) {
			case "新车销售":
				ActivitySwitcherThirdPartShop.toFilterAllShopBrand(getContext(),false);
				break;
			case "推荐店铺":
				ActivitySwitcherThirdPartShop.toFilterThirdSpecialActivity(getContext(), "", null, "");
				break;
			case "限时特价车":
				ActivitySwitcherThirdPartShop.toFlashSaleList(getContext(), siteID, false);
				break;
			case "团购汇":
//				String site = mBaseSharedPreferencesHelper.getSpecialCarAreaHistoryPinYing();
//				String url = new ThirdPartShopClient().getGrouponURL(site);
//				ActivitySwitchBase.toH5Activity(getContext(), "团购汇", url);
				Bundle bundle = new Bundle();
				bundle.putBoolean(GroupBuyMergeActivity.SHOWTOHOME, true);
				ActivitySwitcherThirdPartShop.toGroupBuyMerge(getContext(), bundle);
				break;
			case "分期购车":
				DebugLog.i(AutoInfoContants.LOG_J,"分期购车");
				ActivitySwitcherAccessory4S.toInstallmentBuyingSeries(getContext());
				break;
			case "新车上市":
				ActivitySwitcherThirdPartShop.toNewAutoCalendar(getContext());
				break;
		}
	}
}
