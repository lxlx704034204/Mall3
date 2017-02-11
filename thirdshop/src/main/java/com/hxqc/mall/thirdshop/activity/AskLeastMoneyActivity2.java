package com.hxqc.mall.thirdshop.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.hxqc.mall.activity.BackActivity;
import com.hxqc.mall.core.api.LoadingAnimResponseHandler;
import com.hxqc.mall.core.controler.UserInfoHelper;
import com.hxqc.mall.core.db.area.AreaDBManager;
import com.hxqc.mall.core.fragment.CityChooseFragment;
import com.hxqc.mall.core.model.DeliveryAddress;
import com.hxqc.mall.core.model.User;
import com.hxqc.mall.core.util.ActivitySwitchBase;
import com.hxqc.mall.core.util.FormatCheck;
import com.hxqc.mall.core.util.ToastHelper;
import com.hxqc.mall.core.views.materialedittext.MaterialEditText;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.api.ThirdPartShopClient;
import com.hxqc.mall.thirdshop.fragment.ChooseIntentCarFragment;
import com.hxqc.mall.thirdshop.fragment.NewCarModelChooseFragment;
import com.hxqc.mall.thirdshop.model.Series;
import com.hxqc.mall.thirdshop.model.newcar.ModelInfo;
import com.hxqc.mall.thirdshop.model.newcar.ShopModelPrice;
import com.hxqc.mall.thirdshop.model.promotion.SalesItem;
import com.hxqc.mall.thirdshop.utils.ActivitySwitcherThirdPartShop;
import com.hxqc.mall.thirdshop.utils.SharedPreferencesHelper;
import com.hxqc.mall.thirdshop.views.InfoSubmitSuccessConfirmDialog;
import com.hxqc.mall.thirdshop.views.adpter.AskPriceShopAdapter;
import com.hxqc.util.DebugLog;
import com.hxqc.util.DisplayTools;
import com.hxqc.util.JSONUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * 询价
 * Created by 赵帆
 */

public class AskLeastMoneyActivity2 extends BackActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener, CityChooseFragment.OnAreaChooseInteractionListener, ChooseIntentCarFragment.ChooseIntentCarListener {
    DrawerLayout mDrawerLayoutView;
    FrameLayout mRightView;
    TextView mCustomerIntentCarTypeView;
    MaterialEditText mCustomerCityView;
    EditText mCustomerNameView;
    EditText mCustomerPhoneView;
    RadioGroup mSexRadioGroupView;
    CheckBox mApplyPalacmentView;
    Button mSumbmitView;
    CheckBox mClauseCheckboxView;
    TextView mGoToClauseDeatailView;
    TextView mTipTextView;
    ThirdPartShopClient mThirdPartShopClient;
    SharedPreferencesHelper mSharedPreferencesHelper;
    int sex = 10;//10为男， 20为女
    int applyPlacement = 0;//0为不申请置换， 1为申请置换
    boolean clickable = false;
    CityChooseFragment cityChooseFragment;
    DeliveryAddress mDeliveryAddress;
    ChooseIntentCarFragment chooseIntentCarFragment;
    String carTypeName;
    String brandStr;
    Series series;
    String extID;
    String shopSiteFrom;
    ArrayList<SalesItem> salesItems = new ArrayList<>();
    AreaDBManager areaDBManager;

    ListView shop_lv; //经销商listview
    AskPriceShopAdapter mAskPriceShopAdapter;  //经销商adapter
    //List<ShopInfo> shopList;
    List<ShopModelPrice.ShopInfoPriceBean> shopInfoPrice; //经销商列表


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.t_activity_ask_price2);

        EventBus.getDefault().register(this);
        initView();
        initListener();
        initResource();
        getShopPriceData();
        shop_lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

    }

    @Override
    public void onResume() {
        super.onResume();

        if (!TextUtils.isEmpty(mDeliveryAddress.city)) {
            areaDBManager = AreaDBManager.getInstance(this.getApplicationContext());
            int[] id = areaDBManager.searchPIDAndCIDByTitle(mDeliveryAddress.city);
            mDeliveryAddress.provinceID = id[0] + "";
            mDeliveryAddress.cityID = id[1] + "";
        }
    }


    private void initResource() {
        setAddress();
        mAskPriceShopAdapter = new AskPriceShopAdapter(AskLeastMoneyActivity2.this, shop_lv);
        shop_lv.setAdapter(mAskPriceShopAdapter);

        carTypeName = TextUtils.isEmpty(getIntent().getStringExtra("carTypeName")) ? "未知车型" : getIntent().getStringExtra("carTypeName");
        extID = TextUtils.isEmpty(getIntent().getStringExtra("extID")) ? "" : getIntent().getStringExtra("extID");
        salesItems = getIntent().getParcelableArrayListExtra(ActivitySwitcherThirdPartShop.INTENT_TYPE);
        brandStr = getIntent().getStringExtra("brand");
        series = getIntent().getParcelableExtra("series");
        shopSiteFrom = getIntent().getStringExtra("shopSiteFrom");
        boolean hasCarTypeNameData = getIntent().getBooleanExtra(ActivitySwitcherThirdPartShop.FROMCERTAINCAR, true);

        clickable = !hasCarTypeNameData;
        if (!hasCarTypeNameData && salesItems == null) {
            clickable = false;
        }

        mThirdPartShopClient = new ThirdPartShopClient();
        mSharedPreferencesHelper = new SharedPreferencesHelper(this);

        mCustomerIntentCarTypeView.setText(carTypeName);

        UserInfoHelper.getInstance().getUserInfo(this, new UserInfoHelper.UserInfoAction() {
            @Override
            public void showUserInfo(User meData) {
                if (TextUtils.isEmpty(mCustomerPhoneView.getText()))
                    mCustomerPhoneView.setText(meData.phoneNumber);
                if (TextUtils.isEmpty(mCustomerNameView.getText()))
                    mCustomerNameView.setText(meData.fullname);
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
        }, true);


        SpannableString spannableString = new SpannableString(mTipTextView.getText());
        spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.third_tip_gray)), 17, spannableString.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        mTipTextView.setText(spannableString);

        if (TextUtils.isEmpty(extID)) {
            ToastHelper.showRedToast(this, "该店铺数据存在异常,数据暂时无法提交");
            mSumbmitView.setEnabled(false);
        } else if ("未知车型".equals(carTypeName)) {
            ToastHelper.showRedToast(this, "活动车辆数据存在异常,数据暂时无法提交");
            mSumbmitView.setEnabled(false);
        }
        initChooseCityFragment();
    }

    private void setAddress() {
        SharedPreferencesHelper sharedPreferencesHelper = new SharedPreferencesHelper(this);
        String province = sharedPreferencesHelper.getHistoryProvinceForSpecialCar(this);
        String city = sharedPreferencesHelper.getCityForSpecialCar(this);

        mCustomerCityView.setText(String.format("%s %s", province, city));

        mDeliveryAddress = new DeliveryAddress();
        mDeliveryAddress.province = province;
        mDeliveryAddress.city = city;
    }


    public void initView() {
        mDrawerLayoutView = (DrawerLayout) findViewById(R.id.drawer_layout);

        mRightView = (FrameLayout) findViewById(R.id.right);
        mRightView.setLayoutParams(new DrawerLayout.LayoutParams(DisplayTools.getScreenWidth(this) * 8 / 9, DrawerLayout.LayoutParams.MATCH_PARENT, Gravity.RIGHT));

        mTipTextView = (TextView) findViewById(R.id.tip_textview);
        mCustomerIntentCarTypeView = (TextView) findViewById(R.id.customer_intent_car_type);
        mCustomerCityView = (MaterialEditText) findViewById(R.id.customer_city);
        mCustomerNameView = (EditText) findViewById(R.id.customer_name);
        mCustomerPhoneView = (EditText) findViewById(R.id.customer_phone);
        mSexRadioGroupView = (RadioGroup) findViewById(R.id.radiogroup_sex);
        mApplyPalacmentView = (CheckBox) findViewById(R.id.apply_replacement_checkBox);
        mSumbmitView = (Button) findViewById(R.id.submit);
        mClauseCheckboxView = (CheckBox) findViewById(R.id.clause_checkbox);
        mGoToClauseDeatailView = (TextView) findViewById(R.id.goto_clause_detail);
        shop_lv = (ListView) findViewById(R.id.shop_lv);
    }


    public void initListener() {
        //    mCustomerIntentCarTypeView.setOnClickListener(this);
        mCustomerCityView.setOnClickListener(this);
        mSumbmitView.setOnClickListener(this);
        findViewById(R.id.model_name_lay).setOnClickListener(this);
        mSexRadioGroupView.setOnCheckedChangeListener(this);

        if (clickable) {
            mCustomerIntentCarTypeView.setEnabled(true);
        } else {
            // mCustomerIntentCarTypeView.setEnabled(false);
            mCustomerIntentCarTypeView.setCompoundDrawables(null, null, null, null);
        }
    }


    /**
     * 获取经销商列表
     */
    private void getShopPriceData() {
        mThirdPartShopClient.ShopModelPrice(getIntent().getStringExtra("siteId"), shopSiteFrom, extID, brandStr, series.getSeriesName(), carTypeName,
                new LoadingAnimResponseHandler(this, true, false) {
                    @Override
                    public void onSuccess(String response) {
                        //     forTest(response);
                        findViewById(R.id.submit_lay).setVisibility(View.VISIBLE);
                        ShopModelPrice mShopModelPrice
                                = JSONUtils.fromJson(response, ShopModelPrice.class);

                        if (mShopModelPrice == null || mShopModelPrice.shopInfoPrice == null) {
                            return;
                        }
                        shopInfoPrice = mShopModelPrice.shopInfoPrice;
                        mAskPriceShopAdapter.setData(shopInfoPrice);
                        for (int i = 0; i < shopInfoPrice.size(); i++) {
                            shop_lv.setItemChecked(i, true);
                        }

                    }
                });
    }


    /**
     * 从车型列表选择车型
     * {@link com.hxqc.mall.thirdshop.views.adpter.CarModelChooseAdapter}  }
     */
    @Subscribe
    public void getFourSModel(ModelInfo data) {
        carTypeName = data.getModelName();
        mCustomerIntentCarTypeView.setText(carTypeName);
        mDrawerLayoutView.closeDrawers();
        getShopPriceData();

    }


    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.model_name_lay) {
	        Series data = getIntent().getParcelableExtra("series");
	        NewCarModelChooseFragment newCarModelListFragment = NewCarModelChooseFragment.newInstance(
                    getIntent().getStringExtra("siteId"), shopSiteFrom, extID, brandStr, data.getSeriesName(), carTypeName);

            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.right, newCarModelListFragment);
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

            if (!TextUtils.isEmpty(mCustomerIntentCarTypeView.getText()) && !TextUtils.isEmpty(mCustomerCityView.getText()) && !TextUtils.isEmpty(mCustomerNameView.getText()) && !TextUtils.isEmpty(mCustomerPhoneView.getText())) {
                if (FormatCheck.checkPhoneNumber(mCustomerPhoneView.getText().toString().trim(), this) != 0) {
                    return;
                }

                String nicknameRegex = "^[A-Za-z0-9\\-\\u4e00-\\u9fa5]{2,20}$";
                if (!mCustomerNameView.getText().toString().matches(nicknameRegex)) {
                    ToastHelper.showYellowToast(this, "请检查姓名格式：2~20个字,可由中英文,字母,数字组成");
                    return;
                }

                if (mApplyPalacmentView.isChecked()) {
                    applyPlacement = 1;
                }

                if (TextUtils.isEmpty(mDeliveryAddress.city) || TextUtils.isEmpty(mDeliveryAddress.province)) {
                    ToastHelper.showRedToast(this, "请选择具体省市");
                    return;
                }

                if ("-1".equals(mDeliveryAddress.cityID) || "-1".equals(mDeliveryAddress.provinceID)) {
                    ToastHelper.showRedToast(this, "当前城市数据不正确，请手动选择城市");
                    return;
                }

                if (shop_lv.getCheckedItemCount() == 0) {
                    ToastHelper.showRedToast(this, "请至少选择一家为您服务的经销商");
                    return;
                }
                StringBuilder sb = new StringBuilder();
                for (int j = 0; j < shop_lv.getCount(); j++) {
                    if (shop_lv.isItemChecked(j)) {
                        sb.append(shopInfoPrice.get(j).itemID).append(",");
                    }
                }

                submitData(sb.substring(0, sb.length() - 1));
            } else {
                ToastHelper.showRedToast(this, "请检查必填项是否填写");
            }

        }
    }

    private void submitData(String itemIdArray) {
        mThirdPartShopClient.submitMessage("", itemIdArray, mDeliveryAddress.city, mDeliveryAddress.cityID,
                mDeliveryAddress.province, mDeliveryAddress.provinceID, mCustomerPhoneView.getText() + "",
                mCustomerNameView.getText() + "", sex + "", "10", applyPlacement + "",
                new LoadingAnimResponseHandler(AskLeastMoneyActivity2.this) {
                    @Override
                    public void onSuccess(String response) {
                        InfoSubmitSuccessConfirmDialog infoSubmitSuccessConfirmDialog = new InfoSubmitSuccessConfirmDialog(mContext, "询价提交成功", "您的询价信息已提交，我们将及时联系您进行报价", new InfoSubmitSuccessConfirmDialog.ConfirmListener() {
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
                        infoSubmitSuccessConfirmDialog.show();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);
                        ToastHelper.showRedToast(AskLeastMoneyActivity2.this, "信息提交失败，请再试一次，如有问题请反馈，我们会及时处理，谢谢您的配合");
                    }
                });
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
    public void OnAreaChooseInteraction(String provinces, String city, String district, String pID, String cID, String dID) {
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
        extID = salesItem.baseInfo.itemID;
        mDrawerLayoutView.closeDrawers();
    }


    /**
     * 初始化地区选择Fragment
     */
    public void initChooseCityFragment() {
        if (cityChooseFragment == null) {
            cityChooseFragment = new CityChooseFragment();
            Bundle bundle = new Bundle();
            bundle.putInt(CityChooseFragment.ShowAreaLevelKey, 1);
            bundle.putParcelable(CityChooseFragment.AddressKey, mDeliveryAddress);
            cityChooseFragment.setArguments(bundle);
            cityChooseFragment.setAreaChooseListener(this);
            areaDBManager = cityChooseFragment.areaDBManager;
        }
        FragmentTransaction fragmentTransaction1 = getSupportFragmentManager().beginTransaction();
        fragmentTransaction1.replace(R.id.right, cityChooseFragment);
        fragmentTransaction1.commit();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        int i = item.getItemId();
        if (i == R.id.action_to_home) {
            ActivitySwitchBase.toMain(AskLeastMoneyActivity2.this, 0);
        }

        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayoutView.isDrawerOpen(Gravity.RIGHT))
            mDrawerLayoutView.closeDrawers();
        else
            finish();
    }


    private void forTest(String response) {
        try {
            JSONObject jb = new JSONObject(response);
            //      String a = jb.getJSONArray("modelList").toString();
            String b = jb.getJSONArray("shopInfoPrice").toString();
            List<ShopModelPrice.ShopInfoPriceBean> list = JSONUtils.fromJson(b, new TypeToken<ArrayList<ShopModelPrice.ShopInfoPriceBean>>() {
            });
            DebugLog.e("", list + "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
