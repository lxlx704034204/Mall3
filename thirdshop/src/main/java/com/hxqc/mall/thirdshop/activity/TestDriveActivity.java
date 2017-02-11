package com.hxqc.mall.thirdshop.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.hxqc.mall.activity.BackActivity;
import com.hxqc.mall.core.api.LoadingAnimResponseHandler;
import com.hxqc.mall.core.controler.UserInfoHelper;
import com.hxqc.mall.core.db.area.AreaDBManager;
import com.hxqc.mall.core.fragment.CityChooseFragment;
import com.hxqc.mall.core.model.DeliveryAddress;
import com.hxqc.mall.core.model.User;
import com.hxqc.mall.core.util.ActivitySwitchBase;
import com.hxqc.mall.core.util.BaseSharedPreferencesHelper;
import com.hxqc.mall.core.util.FormatCheck;
import com.hxqc.mall.core.util.ToastHelper;
import com.hxqc.mall.core.views.materialedittext.MaterialEditText;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.api.ThirdPartShopClient;
import com.hxqc.mall.thirdshop.fragment.ChooseIntentCarFragment;
import com.hxqc.mall.thirdshop.model.promotion.SalesItem;
import com.hxqc.mall.thirdshop.utils.ActivitySwitcherThirdPartShop;
import com.hxqc.mall.thirdshop.utils.SharedPreferencesHelper;
import com.hxqc.mall.thirdshop.views.CallBar;
import com.hxqc.mall.thirdshop.views.InfoSubmitSuccessConfirmDialog;
import com.hxqc.util.DisplayTools;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * Function:试乘试驾页面
 *
 * @author 袁秉勇
 * @since 2015年12月01日
 */
public class TestDriveActivity extends BackActivity implements View.OnClickListener, RadioGroup
        .OnCheckedChangeListener, CityChooseFragment.OnAreaChooseInteractionListener, ChooseIntentCarFragment
        .ChooseIntentCarListener {
    DrawerLayout mDrawerLayoutView;
    FrameLayout mRightView;
    MaterialEditText mCustomerIntentCarTypeView;
    MaterialEditText mCustomerCityView;
    EditText mCustomerNameView;
    EditText mCustomerPhoneView;
    RadioGroup mSexRadioGroupView;
    Button mSumbmitView;
    CheckBox mClauseCheckboxView;
    TextView mGoToClauseDeatailView;
    TextView mTipTextView;
    ThirdPartShopClient mThirdPartShopClient;
    CallBar mCallBarView;
    SharedPreferencesHelper mSharedPreferencesHelper;
    int sex = 10;
    boolean clickable = false;
    CityChooseFragment cityChooseFragment;
    DeliveryAddress mDeliveryAddress;
    ChooseIntentCarFragment chooseIntentCarFragment;
    String shopID;
    String carTypeName;
    String itemID;
    ArrayList< SalesItem > salesItems = new ArrayList<>();
    AreaDBManager areaDBManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.e(getClass().getName(), "-------- onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.t_activity_test_drive);

        BaseSharedPreferencesHelper sharedPreferencesHelper = new BaseSharedPreferencesHelper(this);

        shopID = TextUtils.isEmpty(getIntent().getStringExtra("shopID")) ? "" : getIntent().getStringExtra("shopID");
        carTypeName = TextUtils.isEmpty(getIntent().getStringExtra("carTypeName")) ? "未知车型" : getIntent()
                .getStringExtra("carTypeName");
        itemID = TextUtils.isEmpty(getIntent().getStringExtra("itemID")) ? "" : getIntent().getStringExtra("itemID");
        salesItems = getIntent().getParcelableArrayListExtra(ActivitySwitcherThirdPartShop.INTENT_TYPE);
        boolean hasCarTypeNameData = getIntent().getBooleanExtra(ActivitySwitcherThirdPartShop.FROMCERTAINCAR, true);

        clickable = !hasCarTypeNameData;

        if (!hasCarTypeNameData && salesItems == null) {
            clickable = false;
        }

        mThirdPartShopClient = new ThirdPartShopClient();
        mSharedPreferencesHelper = new SharedPreferencesHelper(this);
        mDeliveryAddress = new DeliveryAddress();

        initView();
        initListener();

        mCustomerIntentCarTypeView.setText(carTypeName);
        mCustomerCityView.setText(sharedPreferencesHelper.getProvince() + " " + sharedPreferencesHelper.getCity());

        mDeliveryAddress = new DeliveryAddress();
        mDeliveryAddress.province = sharedPreferencesHelper.getProvince();
        mDeliveryAddress.city = sharedPreferencesHelper.getCity();

        UserInfoHelper.getInstance().getUserInfo(this, new UserInfoHelper.UserInfoAction() {
            @Override
            public void showUserInfo(User meData) {
                if (TextUtils.isEmpty(mCustomerPhoneView.getText())) mCustomerPhoneView.setText(meData.phoneNumber);
                if (TextUtils.isEmpty(mCustomerNameView.getText())) mCustomerNameView.setText(meData.fullname);
                //1为男 2为女
                if ("1".equals(meData.gender)) {
                    mSexRadioGroupView.check(R.id.man);
                } else if ("2".equals(meData.gender)) {
                    mSexRadioGroupView.check(R.id.woman);
                }
            }

            @Override
            public void onFinish() {

            }
        },true);

        initChooseCityFragment();
    }

    @Override
    protected void onStart() {
        Log.e(getClass().getName(), " -------- onStart");
        super.onStart();
    }

    @Override
    public void onResume() {
        Log.e(getClass().getName(), " ------- onResume");
        super.onResume();

        if (!TextUtils.isEmpty(mDeliveryAddress.city)) {
            areaDBManager = AreaDBManager.getInstance(this.getApplicationContext());
            int[] id = areaDBManager.searchPIDAndCIDByTitle(mDeliveryAddress.city);
            mDeliveryAddress.provinceID = id[0] + "";
            mDeliveryAddress.cityID = id[1] + "";
        }
    }

    public void initView() {
        mDrawerLayoutView = (DrawerLayout) findViewById(R.id.drawer_layout);

        mRightView = (FrameLayout) findViewById(R.id.right);
        mRightView.setLayoutParams(new DrawerLayout.LayoutParams(DisplayTools.getScreenWidth(this) * 8 / 9,
                DrawerLayout.LayoutParams.MATCH_PARENT, Gravity.RIGHT));
        salesItems = getIntent().getParcelableArrayListExtra(ActivitySwitcherThirdPartShop.INTENT_TYPE);

        mTipTextView = (TextView) findViewById(R.id.tip_textview);
        mCustomerIntentCarTypeView = (MaterialEditText) findViewById(R.id.customer_intent_car_type);
        mCustomerCityView = (MaterialEditText) findViewById(R.id.customer_city);
        mCustomerNameView = (EditText) findViewById(R.id.customer_name);
        mCustomerPhoneView = (EditText) findViewById(R.id.customer_phone);
        mSexRadioGroupView = (RadioGroup) findViewById(R.id.radiogroup_sex);
        mSumbmitView = (Button) findViewById(R.id.submit);
        mClauseCheckboxView = (CheckBox) findViewById(R.id.clause_checkbox);
        mGoToClauseDeatailView = (TextView) findViewById(R.id.goto_clause_detail);

        mCallBarView = (CallBar) findViewById(R.id.call_bar);
        mCallBarView.setNumber(getIntent().getStringExtra("shopTel"));
        mCallBarView.setTitle("咨询电话");

        SpannableString spannableString = new SpannableString(mTipTextView.getText());
        spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.third_tip_gray)), 17,
                spannableString.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        mTipTextView.setText(spannableString);

        if (TextUtils.isEmpty(shopID) || TextUtils.isEmpty(itemID)) {
            ToastHelper.showRedToast(this, "该店铺数据存在异常,数据暂时无法提交");
            mSumbmitView.setEnabled(false);
        } else if ("未知车型".equals(carTypeName)) {
            ToastHelper.showRedToast(this, "活动车辆数据存在异常,数据暂时无法提交");
            mSumbmitView.setEnabled(false);
        }
    }

    public void initListener() {
        mCustomerIntentCarTypeView.setOnClickListener(this);
        mCustomerCityView.setOnClickListener(this);
        mSumbmitView.setOnClickListener(this);
        mSexRadioGroupView.setOnCheckedChangeListener(this);

        if (clickable) {
            mCustomerIntentCarTypeView.setEnabled(true);
        } else {
            mCustomerIntentCarTypeView.setEnabled(false);
            mCustomerIntentCarTypeView.setCompoundDrawables(null, null, null, null);
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.customer_intent_car_type) {
            if (chooseIntentCarFragment == null) {
                chooseIntentCarFragment = new ChooseIntentCarFragment();
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList(ActivitySwitcherThirdPartShop.INTENT_TYPE, salesItems);
                chooseIntentCarFragment.setArguments(bundle);
                chooseIntentCarFragment.setChooseIntentCarListener(this);
            }
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.right, chooseIntentCarFragment);
            fragmentTransaction.commit();
            mDrawerLayoutView.openDrawer(Gravity.RIGHT);

        } else if (i == R.id.customer_city) {
            initChooseCityFragment();
            mDrawerLayoutView.openDrawer(Gravity.RIGHT);

        } else if (i == R.id.submit) {
            if (!mClauseCheckboxView.isChecked()) {
                ToastHelper.showRedToast(this, "请勾选个人信息保护声明");
                return;
            }
            if (!TextUtils.isEmpty(mCustomerIntentCarTypeView.getText()) && !TextUtils.isEmpty(mCustomerCityView
                    .getText()) && !TextUtils.isEmpty(mCustomerNameView.getText()) && !TextUtils.isEmpty
                    (mCustomerPhoneView.getText())) {
                if (FormatCheck.checkPhoneNumber(mCustomerPhoneView.getText().toString().trim(), this) != 0) {
                    ToastHelper.showRedToast(this, "请检查手机号码是否正确");
                    return;
                }

                String nicknameRegex = "^[A-Za-z0-9\\-\\u4e00-\\u9fa5]{2,20}$";
                if (!mCustomerNameView.getText().toString().matches(nicknameRegex)) {
                    ToastHelper.showYellowToast(this, "请检查姓名格式：2~20个字,可由中英文,字母,数字组成");
                    return;
                }

                if (TextUtils.isEmpty(mDeliveryAddress.city) || TextUtils.isEmpty(mDeliveryAddress.province)) {
                    ToastHelper.showRedToast(this, "请选择具体省市");
                    return;
                }

                if ("-1".equals(mDeliveryAddress.cityID) || "-1".equals(mDeliveryAddress.provinceID)) {
                    ToastHelper.showRedToast(this, "当前城市数据不正确，请手动选择城市");
                    return;
                }

                mThirdPartShopClient.submitMessage(shopID, itemID, mDeliveryAddress.city, mDeliveryAddress
                        .cityID, mDeliveryAddress.province, mDeliveryAddress.provinceID, mCustomerPhoneView
                        .getText() + "", mCustomerNameView.getText() + "", sex + "", "20", "", new
                        LoadingAnimResponseHandler(TestDriveActivity.this, true) {
                            @Override
                            public void onSuccess(String response) {
                                InfoSubmitSuccessConfirmDialog infoSubmitSuccessConfirmDialog = new
                                        InfoSubmitSuccessConfirmDialog(mContext, "试驾提交成功", "您的试驾申请已提交，我们将及时安排您试驾", new
                                        InfoSubmitSuccessConfirmDialog.ConfirmListener() {
                                            @Override
                                            public void confirm() {
                                                new Handler().postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        finish();
                                                    }
                                                }, 180);
                                            }
                                        });
                                infoSubmitSuccessConfirmDialog.setCancelable(false);
                                infoSubmitSuccessConfirmDialog.setCanceledOnTouchOutside(false);
                                infoSubmitSuccessConfirmDialog.show();
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable
                                    throwable) {
                                super.onFailure(statusCode, headers, responseString, throwable);
                                ToastHelper.showRedToast(TestDriveActivity.this,
                                        "信息提交失败，请再试一次，或将问题反馈给我们，我们会及时处理，谢谢您的配合");
                            }
                        });
            } else {
                ToastHelper.showRedToast(this, "请检查必填项是否填写");
            }

        } else {
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (checkedId == R.id.man) {
            sex = 10;

        } else if (checkedId == R.id.woman) {
            sex = 20;

        }
    }

    /**
     * 跳转到协议详情页面
     *
     * @param view
     */
    public void GotoClauseDetail(View view) {
        ActivitySwitcherThirdPartShop.toDeclares(this);
    }

    @Override
    public void OnAreaChooseInteraction(String provinces, String city, String district, String pID, String cID,
                                        String dID) {
        mDeliveryAddress.province = provinces;
        mDeliveryAddress.city = city;
        mDeliveryAddress.district = district;
        mDeliveryAddress.provinceID = pID;
        mDeliveryAddress.cityID = cID;
        mDeliveryAddress.districtID = dID;
        if (TextUtils.isEmpty(provinces) || TextUtils.isEmpty(city)) {
            ToastHelper.showRedToast(this, "请选择具体省市");
            return;
        } else {
            mCustomerCityView.setText(provinces + " " + city + " ");
        }
        mDrawerLayoutView.closeDrawers();
    }

    @Override
    public void onChooseIntentCar(SalesItem salesItem) {
        mCustomerIntentCarTypeView.setText(salesItem.baseInfo.itemName);
        itemID = salesItem.baseInfo.itemID;
        mDrawerLayoutView.closeDrawers();
    }

    public void initChooseCityFragment() {
        if (cityChooseFragment == null) {
            cityChooseFragment = new CityChooseFragment();
            Bundle bundle = new Bundle();
            bundle.putInt(CityChooseFragment.ShowAreaLevelKey, 1);
            cityChooseFragment.setArguments(bundle);
            cityChooseFragment.setAreaChooseListener(this);
        }
        FragmentTransaction fragmentTransaction1 = getSupportFragmentManager().beginTransaction();
        fragmentTransaction1.replace(R.id.right, cityChooseFragment);
        fragmentTransaction1.commit();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_tohome, menu);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == R.id.action_to_home) {
            ActivitySwitchBase.toMain(this, 0);
            return true;
        }
        return false;
    }
}
