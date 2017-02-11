package com.hxqc.mall.activity.me;

import android.app.DatePickerDialog;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.hxqc.mall.activity.BackActivity;
import com.hxqc.mall.core.api.DialogResponseHandler;
import com.hxqc.mall.core.api.UserApiClient;
import com.hxqc.mall.core.controler.UserInfoHelper;
import com.hxqc.mall.core.fragment.CityChooseFragment;
import com.hxqc.mall.core.model.Error;
import com.hxqc.mall.core.model.User;
import com.hxqc.mall.core.util.FormatCheck;
import com.hxqc.mall.core.util.ImageUtil;
import com.hxqc.mall.core.util.OtherUtil;
import com.hxqc.mall.core.util.ToastHelper;
import com.hxqc.mall.core.util.activityutil.ActivitySwitcher;
import com.hxqc.mall.views.bill.NormalMapView;
import com.hxqc.util.DebugLog;
import com.hxqc.util.DisplayTools;
import com.hxqc.util.JSONUtils;

import cz.msebera.android.httpclient.Header;

import java.io.File;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import hxqc.mall.R;
import me.nereo.multi_image_selector.MultiImageSelector;


/**
 * Author:李烽
 * Date:2016-03-18
 * FIXME
 * Todo 修改个人资料
 */
public class ResetUserInfoActivity extends BackActivity implements View.OnClickListener,
		CityChooseFragment.OnAreaChooseInteractionListener, com.wdullaer.materialdatetimepicker.date.DatePickerDialog.OnDateSetListener {
	private static final String MALE = "1";
	private static final String FEMALE = "2";
	private static final String SECRET = "0";
	private static final int REQUEST_IMAGE_CODE = 0x001;
	private static final int CODE_CROP_PHOTO = 0x002;
	final int DATE_DIALOG_ID = 0;//DatePicker的Dialog的ID
	FrameLayout mRightView;//右边浮层
	//    private ResetAvatarFragment resetAvatarFragment;
	int mYear;
	int mMonth;
	int mDay;
	private NormalMapView nick_name_item,
			full_name_item,
			level_item,
			birthday_item,
			area_item,
	//            address_item,
	authenticate_item;
	private EditText input_address, input_full_name;
	private RadioGroup gender_group;
	private RadioButton gender_man, gender_woman, gender_secret;
	private CityChooseFragment mCityChooseFragment;
	private DrawerLayout mDrawerLayoutView;
	private User mUser;//个人资料
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
			mUser.birthday = mBirthdayViewText.toString();
			birthday_item.setValue(mBirthdayViewText.toString());
		}
	};
	private boolean toRealName = false;

	private CircleImageView user_avatar;//头像
	private String cropCachePath;
	protected int outputX = 400;
	protected int outputY = 400;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		DebugLog.e("Tag", "onCreate  " + this.getClass().toString());
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		setContentView(R.layout.activity_reset_user_info);
		initView();
		initData();
		initEvent();
		initRight();
	}

	//    浮层初始化
	private void initRight() {
		mRightView.setLayoutParams(new DrawerLayout.LayoutParams(DisplayTools.getScreenWidth(this)
				* 8 / 9, DrawerLayout.LayoutParams.MATCH_PARENT, Gravity.RIGHT));
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		mCityChooseFragment = new CityChooseFragment();
		mCityChooseFragment.setAreaChooseListener(this);
		fragmentTransaction.replace(R.id.right, mCityChooseFragment);
		fragmentTransaction.commit();
	}


	private void initData() {
//        mUser = UserInfoHelper.getInstance().getUser(this);
		UserInfoHelper.getInstance().getUserInfo(this, new UserInfoHelper.UserInfoAction() {
			@Override
			public void showUserInfo(User meData) {
				mUser = meData;
				fillData();
			}

			@Override
			public void onFinish() {

			}
		}, false);

	}

	private void fillData() {
		if (mUser == null) return;
		if (!TextUtils.isEmpty(mUser.nickName)) {
			nick_name_item.setValue(mUser.nickName);
		}
		if (!TextUtils.isEmpty(mUser.fullname)) {
			input_full_name.setText(mUser.fullname);
			full_name_item.setValue(mUser.fullname);
		}
		if (!TextUtils.isEmpty(mUser.birthday)) {
			birthday_item.setValue(mUser.birthday);
		}
		if (!TextUtils.isEmpty(mUser.district)) {
			area_item.setValue(mUser.province + " " + mUser.city + " " + mUser.district);
		}
		if (!TextUtils.isEmpty(mUser.detailedAddress)) {
//            address_item.setValue(mUser.detailedAddress);
			input_address.setText(mUser.detailedAddress);
		}
		if (!TextUtils.isEmpty(mUser.level))
			level_item.setValue(mUser.level);
		else level_item.setVisibility(View.GONE);
		if (!TextUtils.isEmpty(mUser.levelIcon))
			level_item.setValueIcon(mUser.levelIcon);
		authenticate_item.setValue(mUser.isRealNameAuthentication ? "已设置" : "去设置");
		if (!TextUtils.isEmpty(mUser.avatar)) {
//            ImageUtil.setImage(ResetUserInfoActivity.this, user_avatar,
//                    mUser.avatar, R.drawable.me_userphoto);
			ImageUtil.setImage(ResetUserInfoActivity.this, user_avatar, mUser.avatar,
					R.drawable.me_userphoto);
		}
		showSex();
	}

	private void initView() {
		nick_name_item = (NormalMapView) findViewById(R.id.nick_name_item);
		full_name_item = (NormalMapView) findViewById(R.id.full_name_item);
		nick_name_item.getInputEditText().setSingleLine(true);
		full_name_item.getInputEditText().setSingleLine(true);
		level_item = (NormalMapView) findViewById(R.id.level_item);
		birthday_item = (NormalMapView) findViewById(R.id.birthday_item);
		area_item = (NormalMapView) findViewById(R.id.area_item);
//        address_item = (NormalMapView) findViewById(R.id.address_item);
		input_address = (EditText) findViewById(R.id.input_address);
		input_full_name = (EditText) findViewById(R.id.input_full_name);
		authenticate_item = (NormalMapView) findViewById(R.id.authenticate_item);
		gender_group = (RadioGroup) findViewById(R.id.gender_group);
		gender_man = (RadioButton) findViewById(R.id.gender_man);
		gender_woman = (RadioButton) findViewById(R.id.gender_woman);
		gender_secret = (RadioButton) findViewById(R.id.gender_secret);
		mDrawerLayoutView = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerLayoutView.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
		mRightView = (FrameLayout) findViewById(R.id.right);

//        resetAvatarFragment = (ResetAvatarFragment) getSupportFragmentManager().findFragmentById(R.id.reset_avatar_fragment);

		user_avatar = (CircleImageView) findViewById(R.id.user_avatar);
	}

	private void initEvent() {
		user_avatar.setOnClickListener(this);
		findViewById(R.id.root_layout).setOnClickListener(this);

		birthday_item.setOnClickListener(this);
		area_item.setOnClickListener(this);
		authenticate_item.setOnClickListener(this);
		findViewById(R.id.save).setOnClickListener(this);
//        resetAvatarFragment.setOnAvatarChangeListener(this);
		input_full_name.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				OtherUtil.closeSoftKeyBoard(ResetUserInfoActivity.this, input_full_name);
				input_full_name.clearFocus();
				return event.getKeyCode() == KeyEvent.KEYCODE_ENTER;
			}
		});
	}

	private void showSex() {
		if (!TextUtils.isEmpty(mUser.gender))
			switch (mUser.gender) {
				case MALE:
					gender_man.setChecked(true);
					break;
				case FEMALE:
					gender_woman.setChecked(true);
					break;
				case SECRET:
					gender_secret.setChecked(true);
					break;
			}
	}

	@Override
	public void onResume() {
		super.onResume();
		if (toRealName)
			updateData();
	}

	private void updateData() {
		toRealName = false;
//        mUser = UserInfoHelper.getInstance().getUser(this);
//        fillData();
		UserInfoHelper.getInstance().getUserInfo(this, new UserInfoHelper.UserInfoAction() {
			@Override
			public void showUserInfo(User meData) {
				mUser = meData;
				fillData();
			}

			@Override
			public void onFinish() {

			}
		}, false);
	}

//    /**
//     * 当Activity调用showDialog函数时会触发该函数的调用：
//     */
//    @Override
//    protected Dialog onCreateDialog(int id) {
//        switch (id) {
//            case DATE_DIALOG_ID:
//                return new DatePickerDialog(this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT,
//                        mDateSetListener, mYear, mMonth, mDay);
//        }
//        return null;
//    }

	//    获取性别
	private void getSex() {
		if (gender_man.isChecked()) {
			mUser.gender = MALE;
		} else if (gender_woman.isChecked()) {
			mUser.gender = FEMALE;
		} else {
			mUser.gender = SECRET;
		}
	}

	public void fullNameClick(View v) {
		OtherUtil.openSoftKeyBoard(this, input_full_name);
	}

	public void addressClick(View v) {
		OtherUtil.openSoftKeyBoard(this, input_address);
	}

	public void save() {
		//保存
//        mUser.detailedAddress = address_item.getValue().trim();
		mUser.detailedAddress = input_address.getText().toString().trim();
		if (FormatCheck.checkNickname(nick_name_item.getValue(), this)
				&& FormatCheck.checkRealName(full_name_item.getValue().toString().trim(), this)
				&& checkAddress()) {
			getSex();
			mUser.nickName = nick_name_item.getValue();
//            mUser.fullname = input_full_name.getText().toString();
			mUser.fullname = full_name_item.getValue();

			new UserApiClient().putUser(
					mUser, new DialogResponseHandler(this, getResources().getString(R.string.me_submitting)) {
						@Override
						public void onSuccess(String response) {
							finish();
							UserInfoHelper.getInstance().saveUser(ResetUserInfoActivity.this, mUser);
						}

						@Override
						public void onFailure(int statusCode, Header[] headers,
						                      String responseString, Throwable throwable) {
							super.onFailure(statusCode, headers, responseString, throwable);
							Error error = JSONUtils.fromJson(responseString, Error.class);
							if (error != null)
								ToastHelper.showRedToast(ResetUserInfoActivity.this, error.message);
							else
								ToastHelper.showRedToast(ResetUserInfoActivity.this, "保存失败");
						}
					});
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.user_avatar:
			case R.id.root_layout:
				chooseImage();
				break;
			case R.id.save:
				save();
				break;
			case R.id.authenticate_item:
				if (!mUser.isRealNameAuthentication) {
					//实名认证
					ActivitySwitcher.toRealNameAuthentication(this);
					toRealName = true;
				}
				break;
			case R.id.birthday_item:
				OtherUtil.closeSoftKeyBoard(this, input_full_name);
				OtherUtil.closeSoftKeyBoard(this, full_name_item);
				OtherUtil.closeSoftKeyBoard(this, nick_name_item);
				OtherUtil.closeSoftKeyBoard(this, input_address);
				setCurrentDate();
//                showDialog(DATE_DIALOG_ID);
				OtherUtil.showDialog(ResetUserInfoActivity.this, mYear, mMonth, mDay, this);
				break;
			case R.id.area_item:
				OtherUtil.closeSoftKeyBoard(this, input_full_name);
				OtherUtil.closeSoftKeyBoard(this, full_name_item);
				OtherUtil.closeSoftKeyBoard(this, nick_name_item);
				OtherUtil.closeSoftKeyBoard(this, input_address);
				if (mUser != null) {
					mCityChooseFragment.setDefaultAreaData(mUser.province, mUser.city, mUser.district);
					mDrawerLayoutView.openDrawer(Gravity.RIGHT);
				}
				break;
		}
	}


	private void uploadUserAvatar(final String path, final String mPhoto) {
		new UserApiClient().updateAvatar(UserInfoHelper.getInstance().getToken(ResetUserInfoActivity.this),
				path, new DialogResponseHandler(ResetUserInfoActivity.this,
						getResources().getString(R.string.me_submitting)) {
					@Override
					public void onSuccess(String response) {
						HashMap<String, Object> stringObjectHashMap = JSONUtils.fromJson(response);
						mUser.avatar = stringObjectHashMap.get("url").toString();
//                        mUser.avatarLocalCache = mPhoto;
						ImageUtil.setImage(ResetUserInfoActivity.this, user_avatar, mUser.avatar, R.drawable.me_userphoto);
						//头像上传完后更新内存中的user
						UserInfoHelper.getInstance().refreshUserInfo(mContext, new UserInfoHelper.UserInfoAction() {
							@Override
							public void showUserInfo(User meData) {
							}

							@Override
							public void onFinish() {

							}
						}, true);
					}
				});
	}

	private void chooseImage() {
		new MultiImageSelector(this)
				.showCamera(true)
				.count(1).cropPhoto(true).cropWithAspectRatio(1, 1)
				.start(ResetUserInfoActivity.this, new MultiImageSelector.MultiImageCallBack() {
					@Override
					public void multiSelectorImages(Collection<String> result) {
						if (result != null && result.size() > 0) {
							String[] paths = new String[result.size()];
							final String imgPath = result.toArray(paths)[0];
							final String mPhoto = "file://" + imgPath;
							runOnUiThread(new Runnable() {
								@Override
								public void run() {
									ImageUtil.setImage(ResetUserInfoActivity.this, user_avatar,
											mPhoto, R.drawable.me_userphoto);
								}
							});
							uploadUserAvatar(imgPath, mPhoto);
						}
					}
				});
	}

	//    设置默认显示时间
	private void setCurrentDate() {
		if (mUser == null)
			return;
		if (TextUtils.isEmpty(mUser.birthday)) {
			Calendar currentDate = Calendar.getInstance();
			mYear = currentDate.get(Calendar.YEAR);
			mMonth = currentDate.get(Calendar.MONTH);
			mDay = currentDate.get(Calendar.DAY_OF_MONTH);
		} else {
			if (mUser.birthday.length() > 9)
				mUser.birthday = mUser.birthday.substring(0, 9);
			String[] mStrings = mUser.birthday.split("-");
			mYear = Integer.parseInt(mStrings[0]);
			mMonth = Integer.parseInt(mStrings[1]) - 1;
			mDay = Integer.parseInt(mStrings[2]);
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
			area_item.setValue(provinces + " " + city + " " + district);
		}
		if ("-1".equals(pID)) {
			ToastHelper.showRedToast(this, "请选择省份");
			return;
		}
		if ("-1".equals(cID)) {
			ToastHelper.showRedToast(this, "请选择城市");
			return;
		}
		if ("-1".equals(dID)) {
			ToastHelper.showRedToast(this, "请选择区域");
			return;
		}
		mDrawerLayoutView.closeDrawers();
	}

	private boolean checkAddress() {
		if (mUser.detailedAddress.isEmpty())
			return true;
		return TextUtils.isEmpty(mUser.district) && TextUtils.isEmpty(mUser.detailedAddress)
				|| checkArea() && checkDetailAddress();
	}

	//验证区域选择
	private boolean checkArea() {
		if (!TextUtils.isEmpty(mUser.district)) {
			return true;
		} else {
			ToastHelper.showRedToast(this, R.string.me_area_hint);
			return false;
		}
	}

	//验证详细地址
	private boolean checkDetailAddress() {
		String address = mUser.detailedAddress;
		if (!TextUtils.isEmpty(address) && length(address) >= 2 && length(address) <= 60) {
			return true;
		} else {
			ToastHelper.showRedToast(this, "请检查详细地址格式：" + getString(R.string.me_detail_address_error));
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

	@Override
	public void onBackPressed() {
		boolean drawerOpen = mDrawerLayoutView.isDrawerOpen(Gravity.RIGHT);
		if (drawerOpen)
			mDrawerLayoutView.closeDrawers();
		else finish();
	}

	@Override
	public void onDateSet(com.wdullaer.materialdatetimepicker.date.DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
		this.mYear = year;
		this.mMonth = monthOfYear;
		this.mDay = dayOfMonth;
		//设置文本的内容：
		StringBuilder mBirthdayViewText = new StringBuilder()
				.append(mYear).append("-")
				.append(mMonth + 1).append("-")
				.append(mDay);
		mUser.birthday = mBirthdayViewText.toString();
		birthday_item.setValue(mBirthdayViewText.toString());
	}


	@Override
	public void onStart() {
		super.onStart();
		DebugLog.i("Tag", "3");
	}

	@Override
	public void onStop() {
		super.onStop();
		DebugLog.i("Tag", "4");
	}

	@Override
	public void onPause() {
		super.onPause();
		DebugLog.i("Tag", "5");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		DebugLog.e("Tag", "6onDestroy");
	}


	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		DebugLog.i("Tag", "9");
	}


	/**
	 * 裁剪图片方法实现
	 *
	 * @param uri
	 */
	public void startPhotoZoom(Uri uri, int outputX, int outputY) {
		String cropPath = getCropCacheFilePath();
		ImageUtil.startPhotoZoom(uri, outputX, outputY, 1, 1, CODE_CROP_PHOTO, cropPath, this);
	}

	private String getCropCacheFilePath() {
		cropCachePath = this.getExternalCacheDir() + File.separator + "crop_" + System.currentTimeMillis() + ".png";
		return cropCachePath;
	}

}
