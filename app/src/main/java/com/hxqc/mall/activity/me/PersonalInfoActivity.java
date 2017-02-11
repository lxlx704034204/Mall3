package com.hxqc.mall.activity.me;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.hxqc.mall.activity.AppBackActivity;
import com.hxqc.mall.core.api.DialogResponseHandler;
import com.hxqc.mall.core.api.UserApiClient;
import com.hxqc.mall.core.controler.UserInfoHelper;
import com.hxqc.mall.core.fragment.CityChooseFragment;
import com.hxqc.mall.core.model.User;
import com.hxqc.mall.core.util.FormatCheck;
import com.hxqc.mall.core.util.ToastHelper;
import com.hxqc.mall.core.views.materialedittext.MaterialEditText;
import com.hxqc.util.DisplayTools;

import java.util.Calendar;

import hxqc.mall.R;

/**
 * 说明:编辑个人资料
 * <p>
 * author: 吕飞
 * since: 2015-03-04
 * Copyright:恒信汽车电子商务有限公司
 */
public class PersonalInfoActivity extends AppBackActivity implements View.OnClickListener, CityChooseFragment.OnAreaChooseInteractionListener {
	private static final String MALE = "1";
	private static final String FEMALE = "2";
	private static final String SECRET = "0";
	final int DATE_DIALOG_ID = 0;//DatePicker的Dialog的ID
	MaterialEditText mNicknameView;//昵称
	MaterialEditText mRealNameView;//真名
	MaterialEditText mBirthdayView;//生日
	RadioGroup mSetSexView;//选择性别
	Button mSaveView;//保存
	User mUser;//个人资料
	RadioButton mMaleView;
	RadioButton mFemaleView;
	RadioButton mSecretView;
	FrameLayout mRightView;//右边浮层
	CityChooseFragment mCityChooseFragment;
	DrawerLayout mDrawerLayoutView;
	MaterialEditText mAreaView;//选择区域
	MaterialEditText mDetailAddressView;
	int mYear;
	int mMonth;
	int mDay;
	private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
			mYear = year;
			mMonth = monthOfYear;
			mDay = dayOfMonth;
			//设置文本的内容：
			StringBuilder mBirthdayViewText = new StringBuilder()
					.append(mYear).append("-")
					.append(mMonth + 1).append("-")
					.append(mDay);
			mBirthdayView.setText(mBirthdayViewText);
			mUser.birthday = mBirthdayViewText.toString();
			mBirthdayView.setTextColor(getResources().getColor(R.color.title_and_main_text));
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_personal_info);
		//手势关闭
//        SlidrConfig config = new SlidrConfig.Builder()
//                .primaryColor(getResources().getColor(R.color.primary))
//                .sensitivity(1f).build();
//        Slidr.attach(this, config);
		mAreaView = (MaterialEditText) findViewById(R.id.area);
		mDetailAddressView = (MaterialEditText) findViewById(R.id.detail_address);
		mNicknameView = (MaterialEditText) findViewById(R.id.nickname);
		mRealNameView = (MaterialEditText) findViewById(R.id.real_name);
		mBirthdayView = (MaterialEditText) findViewById(R.id.birthday);
		mSetSexView = (RadioGroup) findViewById(R.id.set_sex);
		mSaveView = (Button) findViewById(R.id.save);
		mFemaleView = (RadioButton) findViewById(R.id.female);
		mMaleView = (RadioButton) findViewById(R.id.male);
		mSecretView = (RadioButton) findViewById(R.id.secret);
		UserInfoHelper.getInstance().getUserInfo(this, new UserInfoHelper.UserInfoAction() {
			@Override
			public void showUserInfo(User meData) {
				mUser = meData;
			}

			@Override
			public void onFinish() {

			}
		}, false);
		fillData();
		mDrawerLayoutView = (DrawerLayout) findViewById(R.id.drawer_layout);
		mRightView = (FrameLayout) findViewById(R.id.right);
		mBirthdayView.setOnClickListener(this);
		mSaveView.setOnClickListener(this);
		mAreaView.setOnClickListener(this);
		initRight();
	}

	//    浮层初始化
	private void initRight() {
		mRightView.setLayoutParams(new DrawerLayout.LayoutParams(DisplayTools.getScreenWidth(this) * 8 / 9, DrawerLayout.LayoutParams.MATCH_PARENT, Gravity.RIGHT));
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		mCityChooseFragment = new CityChooseFragment();
		mCityChooseFragment.setAreaChooseListener(this);
		fragmentTransaction.replace(R.id.right, mCityChooseFragment);
		fragmentTransaction.commit();
	}

	private void fillData() {
		if (mUser == null) return;
		if (!TextUtils.isEmpty(mUser.nickName)) {
			mNicknameView.setText(mUser.nickName);
		}
		if (!TextUtils.isEmpty(mUser.fullname)) {
			mRealNameView.setText(mUser.fullname);
		}
		if (!TextUtils.isEmpty(mUser.birthday)) {
			mBirthdayView.setText(mUser.birthday);
			mBirthdayView.setTextColor(getResources().getColor(R.color.title_and_main_text));
		}
		if (!TextUtils.isEmpty(mUser.district)) {
			mAreaView.setText(mUser.province + " " + mUser.city + " " + mUser.district);
			mAreaView.setTextColor(getResources().getColor(R.color.title_and_main_text));
		}
		if (!TextUtils.isEmpty(mUser.detailedAddress)) {
			mDetailAddressView.setText(mUser.detailedAddress);
		}
		showSex();
	}

	private void showSex() {
		switch (mUser.gender) {
			case MALE:
				mMaleView.setChecked(true);
				break;
			case FEMALE:
				mFemaleView.setChecked(true);
				break;
			case SECRET:
				mSecretView.setChecked(true);
				break;
		}
	}

	/**
	 * 当Activity调用showDialog函数时会触发该函数的调用：
	 */
	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
			case DATE_DIALOG_ID:
				return new DatePickerDialog(this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT, mDateSetListener, mYear, mMonth, mDay);
		}
		return null;
	}

	@Override
	public void onClick(View v) {
		int i = v.getId();
		switch (i) {
			case R.id.birthday:
				setCurrentDate();
				showDialog(DATE_DIALOG_ID);
				break;
			case R.id.area:
				mCityChooseFragment.setDefaultAreaData(mUser.province, mUser.city, mUser.district);
				mDrawerLayoutView.openDrawer(Gravity.RIGHT);
				break;
			case R.id.save:
				mUser.detailedAddress = mDetailAddressView.getText().toString().trim();
				if (FormatCheck.checkNickname(mNicknameView.getText().toString(), this) && FormatCheck.checkRealName(mRealNameView.getText().toString(), this) && checkAddress()) {
					getSex();
					mUser.nickName = mNicknameView.getText().toString();
					mUser.fullname = mRealNameView.getText().toString();
					UserInfoHelper.getInstance().saveUser(PersonalInfoActivity.this, mUser);
					new UserApiClient().putUser(mUser, new DialogResponseHandler(PersonalInfoActivity.this, getResources().getString(R.string.me_submitting)) {
						@Override
						public void onSuccess(String response) {
							finish();
						}
					});
				}
				break;
		}
	}

	//    设置默认显示时间
	private void setCurrentDate() {
		if (TextUtils.isEmpty(mUser.birthday)) {
			Calendar currentDate = Calendar.getInstance();
			mYear = currentDate.get(Calendar.YEAR);
			mMonth = currentDate.get(Calendar.MONTH);
			mDay = currentDate.get(Calendar.DAY_OF_MONTH);
		} else {
			String[] mStrings = mUser.birthday.split("-");
			mYear = Integer.parseInt(mStrings[0]);
			mMonth = Integer.parseInt(mStrings[1]) - 1;
			mDay = Integer.parseInt(mStrings[2]);
		}
	}

	//    获取性别
	private void getSex() {
		if (mMaleView.isChecked()) {
			mUser.gender = MALE;
		} else if (mFemaleView.isChecked()) {
			mUser.gender = FEMALE;
		} else {
			mUser.gender = SECRET;
		}
	}

	@Override
	public void OnAreaChooseInteraction(String provinces, String city, String district, String pID, String cID, String dID) {
		mUser.province = provinces;
		mUser.city = city;
		mUser.district = district;
		mUser.provinceID = pID;
		mUser.cityID = cID;
		mUser.districtID = dID;
		if (!TextUtils.isEmpty(provinces)) {
			mAreaView.setText(provinces + " " + city + " " + district);
			mAreaView.setTextColor(getResources().getColor(R.color.title_and_main_text));
		}
		if ("-1".equals(pID)) {
			ToastHelper.showYellowToast(this, "请选择省份");
			return;
		}
		if ("-1".equals(cID)) {
			ToastHelper.showYellowToast(this, "请选择城市");
			return;
		}
		if ("-1".equals(dID)) {
			ToastHelper.showYellowToast(this, "请选择区域");
			return;
		}
		mDrawerLayoutView.closeDrawers();
	}

	//验证区域选择
	private boolean checkArea() {
		if (!TextUtils.isEmpty(mUser.district)) {
			return true;
		} else {
			ToastHelper.showYellowToast(this, R.string.me_area_hint);
			return false;
		}
	}

	//验证详细地址
	private boolean checkDetailAddress() {
		String address = mUser.detailedAddress;
		if (!TextUtils.isEmpty(address) && length(address) >= 5 && length(address) <= 60) {
			return true;
		} else {
			ToastHelper.showYellowToast(this, R.string.me_detail_address_null);
			return false;
		}
	}

	/**
	 * 获取字符串的长度，如果有中文，则每个中文字符计为2位
	 *
	 * @param value 指定的字符串
	 * @return 字符串的长度
	 */
	public int length(String value) {
		int valueLength = 0;
		String chinese = "[\u0391-\uFFE5]";
		for (int i = 0; i < value.length(); i++) {
			String temp = value.substring(i, i + 1);
			if (temp.matches(chinese)) {
				valueLength += 2;
			} else {
				valueLength += 1;
			}
		}
		return valueLength;
	}

	private boolean checkAddress() {
		return TextUtils.isEmpty(mUser.district) && TextUtils.isEmpty(mUser.detailedAddress) || checkArea() && checkDetailAddress();
	}
}
