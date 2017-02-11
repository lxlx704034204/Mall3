package com.hxqc.mall.activity.me;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.hxqc.mall.activity.BackActivity;
import com.hxqc.mall.core.api.LoadingAnimResponseHandler;
import com.hxqc.mall.core.api.UserApiClient;
import com.hxqc.mall.core.controler.UserInfoHelper;
import com.hxqc.mall.core.model.FeedBackOption;
import com.hxqc.mall.core.util.ToastHelper;
import com.hxqc.mall.core.views.vedit.EditTextValidatorView;
import com.hxqc.mall.core.views.vedit.manager.VWholeEditManager;
import com.hxqc.mall.core.views.vedit.tech.VMallDivNotNull;
import com.hxqc.mall.core.views.vedit.tech.VMallPhoneNumber;
import com.hxqc.mall.fragment.me.FeedBackOptionListFragment;
import com.hxqc.mall.fragment.me.FilterShopListFragment;
import com.hxqc.mall.fragment.me.FilterSiteListFragment;
import com.hxqc.mall.interfaces.EditChanged;
import com.hxqc.mall.thirdshop.activity.auto.activity.ControllerConstruct;
import com.hxqc.mall.thirdshop.activity.auto.control.BaseFilterController;
import com.hxqc.mall.thirdshop.activity.auto.control.FilterControllerForSpecialCar;
import com.hxqc.mall.thirdshop.activity.auto.fragment.FilterBrandFragment;
import com.hxqc.mall.thirdshop.model.AreaCategory;
import com.hxqc.mall.thirdshop.model.Brand;
import com.hxqc.mall.thirdshop.model.ShopSearchShop;
import com.hxqc.mall.thirdshop.views.CallBar;
import com.hxqc.xiaoneng.ChatManager;

import hxqc.mall.R;

/**
 * Author:李烽
 * Date:2016-11-24
 * FIXME
 * Todo 客户投诉
 */
public class ComplaintsActivity2 extends BackActivity implements View.OnClickListener, ControllerConstruct, FilterBrandFragment.FilterBrandFragmentCallBack, View.OnFocusChangeListener, DrawerLayout.DrawerListener {
    private static final int ITEM_ID = 0x001;

    private TextView type;
    private EditTextValidatorView chooseType;
    private TextView phone;
    private EditTextValidatorView inputPhone;
    private EditTextValidatorView chooseAera;
    private EditTextValidatorView chooseBrand;
    private EditTextValidatorView chooseShop;
    private EditTextValidatorView inputPersonName;
    private TextView advice;
    private EditText inputAdvice;
    private TextView adviceWarning;
    private TextView markLabel;
    private Button commitBtn;
    private TextView watchOutInfo;

    //menu‘s fragments
    private FeedBackOptionListFragment feedBackOptionListFragment;
    private FilterSiteListFragment filterSiteListFragment;
    private FilterBrandFragment filterBrandFragment;
    private FilterShopListFragment filterShopListFragment;
    BaseFilterController baseFilterController;
    private DrawerLayout drawerLayout;
    private FeedBackOption currentFeedbackOption;

    private String currentSiteId = "";
    private String currentbrand = "";
    private String currentShopId = "";
    private VWholeEditManager vWholeEditManager;

    private Fragment tempFragment;//记录菜单里面显示的fragment

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaints2);
        initController();
        initView();
        initEvent();
    }

    private void initView() {
        type = (TextView) findViewById(R.id.type);
        type.setText(titleAddStar(type.getText().toString()));
        chooseType = (EditTextValidatorView) findViewById(R.id.choose_type);
        chooseType.addValidator(new VMallDivNotNull(this.getString(R.string.waring_choose_feedback_option), ""));
        phone = (TextView) findViewById(R.id.phone);
        phone.setText(titleAddStar(phone.getText().toString()));
        inputPhone = (EditTextValidatorView) findViewById(R.id.input_phone);
        inputPhone.setText(UserInfoHelper.getInstance().getPhoneNumber(this));
        inputPhone.addValidator(new VMallPhoneNumber("请输入正确的手机号"));
        vWholeEditManager = new VWholeEditManager(this);
        vWholeEditManager.autoAddVViews();
        inputPhone.setOnFocusChangeListener(this);
        chooseAera = (EditTextValidatorView) findViewById(R.id.choose_aera);
        chooseBrand = (EditTextValidatorView) findViewById(R.id.choose_brand);
        chooseShop = (EditTextValidatorView) findViewById(R.id.choose_shop);
        inputPersonName = (EditTextValidatorView) findViewById(R.id.input_person_name);
        advice = (TextView) findViewById(R.id.advice);
        advice.setText(titleAddStar(advice.getText().toString()));
        inputAdvice = (EditText) findViewById(R.id.input_advice);
//        inputAdvice.addValidator(new VMallDivNotNull("请输入投诉意见", ""));
        inputAdvice.addTextChangedListener(new EditChanged() {
            @Override
            public void onEditChange(CharSequence s, int start, int before, int count) {
                int length = s.length();
                markLabel.setText(String.format("%d/200", length));
            }
        });
        adviceWarning = (TextView) findViewById(R.id.advice_warning);
        markLabel = (TextView) findViewById(R.id.mark_label);
        commitBtn = (Button) findViewById(R.id.commit_btn);
        watchOutInfo = (TextView) findViewById(R.id.watch_out_info);
        watchOutInfo.setText(Html.fromHtml("<p>我们收到您的投诉会在2小时内与您联系<br/>如有疑问请拨打：" +
                "<font color=\"#e02c36\">400-1868-555</font></p>"));
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        drawerLayout.addDrawerListener(this);
    }

    private Spanned titleAddStar(String title) {
        return Html.fromHtml("<font color=\"#e02c36\">*</font>" + title);
    }

    private void initEvent() {
        chooseType.setOnClickListener(this);
        chooseAera.setOnClickListener(this);
        chooseShop.setOnClickListener(this);
        chooseBrand.setOnClickListener(this);
        inputPhone.addTextChangedListener(new EditChanged() {
            @Override
            public void onEditChange(CharSequence s, int start, int before, int count) {
                String trim = s.toString().trim();
                if (trim.isEmpty())
                    adviceWarning.setVisibility(View.VISIBLE);
                else adviceWarning.setVisibility(View.GONE);
            }
        });
    }

    public void onfeedbackOptionSelected(FeedBackOption feedBackOption) {
        chooseType.setText(feedBackOption.adviceTitle);
        this.currentFeedbackOption = feedBackOption;
        drawerLayout.closeDrawers();
    }

    public void onSiteSelected(AreaCategory areaCategory) {
        chooseAera.setText(areaCategory.areaName);
        chooseBrand.setText("");
        currentbrand = "";
        this.currentSiteId = areaCategory.siteID;
        baseFilterController.mFilterMap.put("siteID", areaCategory.siteID);
        drawerLayout.closeDrawers();
    }

    public void onShopSelected(ShopSearchShop shop) {
        chooseShop.setText(shop.shopTitle);
        this.currentShopId = shop.shopID;
        drawerLayout.closeDrawers();
    }

    public void callPhone(View view) {
        //打客服
        new CallBar.CallPhoneDialog(this, "4001868555").show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem item = menu.add(0, ITEM_ID, 0, "在线客服");
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case ITEM_ID:
                ChatManager.getInstance().startChatWithNothing();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.choose_type:
                showFeebackOptionList();
                break;
            case R.id.choose_aera:
                showSiteList();
                break;
            case R.id.choose_brand:
                showBrandList();
                break;
            case R.id.choose_shop:
                showShopList();
                break;
        }
    }

    private void showBrandList() {
        if (filterBrandFragment == null) {
            filterBrandFragment = new FilterBrandFragment();
            filterBrandFragment.setFilterBrandFragmentCallBack(this);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, filterBrandFragment)
                    .show(filterBrandFragment)
                    .commit();
        } else {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, filterBrandFragment)
                    .commit();
        }
        filterBrandFragment.getData();
        drawerLayout.openDrawer(Gravity.RIGHT);
        tempFragment = filterBrandFragment;
    }

    private void showSiteList() {
        if (filterSiteListFragment == null) {
            filterSiteListFragment = FilterSiteListFragment.newInstance();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, filterSiteListFragment)
                    .show(filterSiteListFragment)
                    .commit();
        } else {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, filterSiteListFragment)
                    .commit();
        }
        drawerLayout.openDrawer(Gravity.RIGHT);
        tempFragment = filterSiteListFragment;
    }

    private void showFeebackOptionList() {
        if (feedBackOptionListFragment == null) {
            feedBackOptionListFragment = FeedBackOptionListFragment.newInstance();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, feedBackOptionListFragment)
                    .show(feedBackOptionListFragment)
                    .commit();
        } else {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, feedBackOptionListFragment)
                    .commit();
        }
        drawerLayout.openDrawer(Gravity.RIGHT);
        tempFragment = feedBackOptionListFragment;
    }

    private void showShopList() {
        if (filterShopListFragment == null) {
            filterShopListFragment = FilterShopListFragment
                    .newInstance(currentbrand, currentSiteId);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, filterShopListFragment)
                    .show(filterShopListFragment)
                    .commit();
        } else {
            filterShopListFragment.setBrand(currentbrand);
            filterShopListFragment.setSiteID(currentSiteId);
            filterShopListFragment.reloadData();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, filterShopListFragment)
                    .commit();
        }
        drawerLayout.openDrawer(Gravity.RIGHT);
        tempFragment = filterShopListFragment;
    }

    public void commit(View view) {
        //隐藏键盘
        if (getCurrentFocus() != null) {
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(getCurrentFocus()
                                    .getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
        }
        if (!vWholeEditManager.validate()) {
            return;
        }
        String phoneNumber = inputPhone.getText().toString().trim();
        String advice = inputAdvice.getText().toString().trim();
        if (TextUtils.isEmpty(advice)) {
            adviceWarning.setVisibility(View.VISIBLE);
            ToastHelper.showRedToast(this, adviceWarning.getText().toString());
            return;
        }
        new UserApiClient().sendFeedback(phoneNumber, advice, currentFeedbackOption.adviceID,
                currentSiteId, currentbrand, currentShopId, inputPersonName.getText().toString()
                , new LoadingAnimResponseHandler(this, true) {
                    @Override
                    public void onSuccess(String response) {
                        ToastHelper.showGreenToast(ComplaintsActivity2.this, "提交成功！");
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                ComplaintsActivity2.this.finish();
                            }
                        }, 500);

                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        commitBtn.setEnabled(true);
                    }
                });
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(Gravity.RIGHT))
            drawerLayout.closeDrawer(Gravity.RIGHT);
        else
            finish();
    }

    @Override
    public void initController() {
        baseFilterController = FilterControllerForSpecialCar.getInstance();
    }

    @Override
    public BaseFilterController getController() {
        return baseFilterController;
    }

    @Override
    public void destroyController() {
        if (baseFilterController != null) baseFilterController.destroy();
    }

    @Override
    public void onFilterBrandCallback(Brand brand) {
        //当品牌选择之后
        if (brand != null) {
            chooseBrand.setText(brand.brandName);
            currentbrand = brand.brandName;
        } else {
            chooseBrand.setText("不限");
            currentbrand = "不限";
        }
        drawerLayout.closeDrawers();
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (!hasFocus) {
            VWholeEditManager manager = new VWholeEditManager(this);
            manager.addEditView(inputPhone);
            manager.validate();
        }
    }

    @Override
    public void onDrawerSlide(View drawerView, float slideOffset) {

    }

    @Override
    public void onDrawerOpened(View drawerView) {

    }

    @Override
    public void onDrawerClosed(View drawerView) {
        if (tempFragment instanceof FeedBackOptionListFragment) {
            checkFeedBackOption();
        }
    }

    private void checkFeedBackOption() {
        VWholeEditManager manager = new VWholeEditManager(this);
        manager.addEditView(chooseType);
        manager.validate();
    }

    @Override
    public void onDrawerStateChanged(int newState) {

    }
}
