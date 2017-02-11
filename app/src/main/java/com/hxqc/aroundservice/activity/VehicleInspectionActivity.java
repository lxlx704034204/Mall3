package com.hxqc.aroundservice.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.hxqc.aroundservice.config.OrderDetailContants;
import com.hxqc.aroundservice.control.AnnualVehicleControl;
import com.hxqc.aroundservice.fragment.QueryProcessingPhotoFragmentV2;
import com.hxqc.aroundservice.util.ActivitySwitchAround;
import com.hxqc.mall.activity.BackActivity;
import com.hxqc.mall.auto.inter.CallBackControl;
import com.hxqc.mall.auto.model.MyAuto;
import com.hxqc.mall.auto.view.PlateNumberTextView;
import com.hxqc.mall.core.api.ApiUtil;
import com.hxqc.mall.core.controler.UserInfoHelper;
import com.hxqc.mall.core.fragment.CityChooseFragment;
import com.hxqc.mall.core.model.User;
import com.hxqc.mall.core.util.ActivitySwitchBase;
import com.hxqc.mall.core.util.FormatCheck;
import com.hxqc.mall.core.util.ToastHelper;
import com.hxqc.mall.core.views.materialedittext.MaterialEditText;
import com.hxqc.mall.usedcar.utils.OtherUtil;
import com.hxqc.util.DisplayTools;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.Calendar;

import hxqc.mall.R;

/**
 * 车辆年审
 * Created by huangyi on 16/4/29.
 */
public class VehicleInspectionActivity extends BackActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener, CityChooseFragment.OnAreaChooseInteractionListener {

    DrawerLayout mDrawerView;
    PlateNumberTextView mNumberView;
    MaterialEditText mDateView, mNameView, mPhoneView, mAddressView, mAddressDetailView;
    String mProvince, mCity, mRegion;
    CityChooseFragment mAddressFragment;
    QueryProcessingPhotoFragmentV2 mOriginalFragment, mCopyFragment;
    int year = -1, monthOfYear = -1, dayOfMonth = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_inspection);

        mDrawerView = (DrawerLayout) findViewById(R.id.inspection_drawer);
        OtherUtil.setDrawerMode(mDrawerView);
        mNumberView = (PlateNumberTextView) findViewById(R.id.inspection_number);
        mDateView = (MaterialEditText) findViewById(R.id.inspection_date);
        mNameView = (MaterialEditText) findViewById(R.id.inspection_name);
        mPhoneView = (MaterialEditText) findViewById(R.id.inspection_phone);
        mAddressView = (MaterialEditText) findViewById(R.id.inspection_address);
        mAddressView.setOnClickListener(this);
        mAddressDetailView = (MaterialEditText) findViewById(R.id.inspection_address_detail);
        findViewById(R.id.inspection_right).setLayoutParams(new DrawerLayout.LayoutParams(DisplayTools.getScreenWidth(this) / 5 * 4, DrawerLayout.LayoutParams.MATCH_PARENT, Gravity.RIGHT));
        mAddressFragment = new CityChooseFragment();
        mAddressFragment.setAreaChooseListener(this);
        getSupportFragmentManager().beginTransaction().replace(R.id.inspection_right, mAddressFragment).commit();

        mOriginalFragment = (QueryProcessingPhotoFragmentV2) getSupportFragmentManager().findFragmentById(R.id.inspection_original);
        mCopyFragment = (QueryProcessingPhotoFragmentV2) getSupportFragmentManager().findFragmentById(R.id.inspection_copy);

        mDateView.setOnClickListener(this);
        mDateView.setInputType(InputType.TYPE_NULL);
        mDateView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(mDateView.getWindowToken(), 0);
                    showDialog();
                }
            }
        });

        mOriginalFragment.setTitle("行驶证正本正面照");
        mOriginalFragment.setAactivityType(OrderDetailContants.DRIVING);
        mOriginalFragment.setClickAreaListener(true);
        mOriginalFragment.setSampleTitleViewVisibility(true);
        mOriginalFragment.setClickSampleTitleListener(this, R.drawable.ic_sample_licence_origianl);
        mCopyFragment.setTitle("行驶证副本正面照");
        mCopyFragment.setAactivityType(OrderDetailContants.DRIVING);
        mCopyFragment.setClickAreaListener(false);
        mCopyFragment.setSampleTitleViewVisibility(true);
        mCopyFragment.setClickSampleTitleListener(this, R.drawable.ic_sample_licence_copy);
        findViewById(R.id.inspection_submit).setOnClickListener(this);

        //个人信息回显
        UserInfoHelper.getInstance().getUserInfo(this, new UserInfoHelper.UserInfoAction() {
            @Override
            public void showUserInfo(User meData) {
                mNameView.setText(meData.fullname);
                mPhoneView.setText(meData.phoneNumber);
            }

            @Override
            public void onFinish() {

            }
        }, false);

        //上级页面数据回显
        if (null != getIntent() && null != getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA)) {
            MyAuto auto = getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA).getParcelable("myAuto");
            if (null != auto) {
                mNumberView.setText(auto.plateNumber.substring(2));

                if (auto.registerTime.length() > 9)
                    auto.registerTime = auto.registerTime.substring(0, 10);
                mDateView.setText(auto.registerTime);
                String[] split = auto.registerTime.split(" ");
                if (split.length == 2) {
                    String[] s = split[0].split("-");
                    if (s.length == 3) {
                        this.year = Integer.parseInt(s[0]);
                        this.monthOfYear = Integer.parseInt(s[1]) - 1;
                        this.dayOfMonth = Integer.parseInt(s[2]);
                    }
                }
            }
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.inspection_address) {
            mDrawerView.openDrawer(Gravity.RIGHT);

        } else if (id == R.id.inspection_date) {
            showDialog();

        } else if (id == R.id.inspection_submit) {
            String number = mNumberView.getText().toString().trim();
            if (TextUtils.isEmpty(number) || number.length() != 5) {
                ToastHelper.showYellowToast(this, "请输入正确的车牌号");
                return;
            }

            String date = mDateView.getText().toString().trim();
            if (TextUtils.isEmpty(date)) {
                ToastHelper.showYellowToast(this, "请输入正确的车辆注册日期");
                return;
            }

            String name = mNameView.getText().toString().trim();
            if (TextUtils.isEmpty(name)) {
                ToastHelper.showYellowToast(this, "请输入正确的姓名");
                return;
            }

            String phone = mPhoneView.getText().toString().trim();
            if (FormatCheck.checkPhoneNumber(phone, this) != FormatCheck.CHECK_SUCCESS) return;

            if (TextUtils.isEmpty(mProvince) || TextUtils.isEmpty(mCity) || TextUtils.isEmpty(mRegion)) {
                ToastHelper.showYellowToast(this, "请选择省市区");
                return;
            }

            String detail = mAddressDetailView.getText().toString().trim();
            if (TextUtils.isEmpty(detail)) {
                ToastHelper.showYellowToast(this, "请输入详细地址");
                return;
            }

            String original = mOriginalFragment.getmFilePath();
            if (TextUtils.isEmpty(original)) {
                ToastHelper.showYellowToast(this, "请上传行驶证正本正面照");
                return;
            } else {
                original = original.substring(7);
            }

            String copy = mCopyFragment.getmFilePath();
            if (TextUtils.isEmpty(copy)) {
                ToastHelper.showYellowToast(this, "请上传行驶证副本正面照");
                return;
            } else {
                copy = copy.substring(7);
            }

            Calendar now = Calendar.getInstance();
            int i = now.get(Calendar.YEAR);
            int i1 = now.get(Calendar.MONTH);
            //验证注册日期
            int nowM = i * 12 + i1;
            int oldM = year * 12 + monthOfYear;
            int disM = nowM - oldM; //月差
            int remainderM = disM % 12; //余月

            if ((remainderM == 0 && ((disM - remainderM) / 12) > 10) || (remainderM != 0 && ((disM - remainderM) / 12) >= 10)) {
                //超过10年
                showHint("根据交管局相关规定，您的爱车不在年审时间范围内，无法提交订单，如有疑问，请致电：13476195625。");
                return;
            }
            if (remainderM == 0) {
                //同月
                int y = disM / 12; //年
                if (y == 2 || y == 4 || y == 6 || y == 7 || y == 8 || y == 9 || y == 10) {
                    submit(number, date, name, phone, detail, original, copy, view);
                } else {
                    showHint("根据交管局相关规定，您的爱车不在年审时间范围内，无法提交订单，如有疑问，请致电：13476195625。");
                }

            } else {
                //异月
                if ((12 - remainderM) < 3) {
                    int y = (disM - remainderM) / 12; //年
                    if (y == 1 || y == 3 || y == 5 || y == 6 || y == 7 || y == 8 || y == 9) {
                        submit(number, date, name, phone, detail, original, copy, view);
                    } else {
                        showHint("根据交管局相关规定，您的爱车不在年审时间范围内，无法提交订单，如有疑问，请致电：13476195625。");
                    }
                } else {
                    showHint("根据交管局相关规定，您的爱车不在年审时间范围内，无法提交订单，如有疑问，请致电：13476195625。");
                }
            }
        }
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        this.year = year;
        this.monthOfYear = monthOfYear;
        this.dayOfMonth = dayOfMonth;
        if (monthOfYear < 9) {
            if (dayOfMonth < 10) {
                mDateView.setText(year + "-0" + (monthOfYear + 1) + "-0" + dayOfMonth);
            } else {
                mDateView.setText(year + "-0" + (monthOfYear + 1) + "-" + dayOfMonth);
            }
        } else {
            if (dayOfMonth < 10) {
                mDateView.setText(year + "-" + (monthOfYear + 1) + "-0" + dayOfMonth);
            } else {
                mDateView.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
            }
        }
    }


    /**
     * @param number
     * @param date
     * @param name
     * @param phone
     * @param address
     * @param path1
     * @param path2
     * @param view
     */
    private void submit(final String number, final String date, final String name, final String phone, final String address, final String path1, final String path2, final View view) {
        UserInfoHelper.getInstance().loginAction(this, new UserInfoHelper.OnLoginListener() {
            @Override
            public void onLoginSuccess() {
                /*new AroundServiceApiClient().postAnnualnspectionSubmit(VehicleInspectionActivity.this, "鄂A" + number, date, name, phone,
                        mProvince, mCity, mRegion, address, path1, path2, new LoadingAnimResponseHandler(VehicleInspectionActivity.this, true) {

                    @Override
                    public void onStart() {
                        super.onStart();
                        view.setEnabled(false);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        view.setEnabled(true);
                    }

                    @Override
                    public void onSuccess(String response) {
                        ActivitySwitchAround.toIllegalProcessingSuccessActivity(VehicleInspectionActivity.this);
                    }
                });*/
                AnnualVehicleControl.getInstance().commitAnnualVehicle(VehicleInspectionActivity.this, "鄂A" + number, date, name, phone,
                        mProvince, mCity, mRegion, address, path1, path2, new CallBackControl.CallBack<String>() {
                            @Override
                            public void onSuccess(String response) {
                                ActivitySwitchAround.toIllegalProcessingSuccessActivity(VehicleInspectionActivity.this);
                            }

                            @Override
                            public void onFailed(boolean offLine) {

                            }
                        });
            }
        });
    }

    private void showDialog() {
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                VehicleInspectionActivity.this,
                this.year != -1 ? this.year : now.get(Calendar.YEAR),
                this.monthOfYear != -1 ? this.monthOfYear : now.get(Calendar.MONTH),
                this.dayOfMonth != -1 ? this.dayOfMonth : now.get(Calendar.DAY_OF_MONTH)
        );
        dpd.setThemeDark(true);
        dpd.vibrate(true);
        dpd.dismissOnPause(false);
        dpd.showYearPickerFirst(false);
        dpd.setAccentColor(ContextCompat.getColor(VehicleInspectionActivity.this, R.color.cursor_orange));
        dpd.show(getFragmentManager(), "DatePickerDialog");
    }

    private void showHint(String content) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MaterialDialog);
        builder.setTitle("提示").setMessage(content).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).create().show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_introduce, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_introduce) {
            ActivitySwitchBase.toH5Activity(this, "服务说明", ApiUtil.getAroundURL("/Servicedeclar/annualnspection.html"));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void OnAreaChooseInteraction(String provinces, String city, String district, String pID, String cID, String dID) {
        this.mProvince = provinces;
        this.mCity = city;
        this.mRegion = district;
        mAddressView.setText(mProvince + mCity + mRegion);
        mDrawerView.closeDrawer(Gravity.RIGHT);
    }
}
