package com.hxqc.mall.thirdshop.accessory.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.WindowCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListPopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hxqc.mall.core.api.LoadingAnimResponseHandler;
import com.hxqc.mall.core.api.RequestFailView;
import com.hxqc.mall.core.controler.UserInfoHelper;
import com.hxqc.mall.core.model.ImageModel;
import com.hxqc.mall.core.util.ActivitySwitchBase;
import com.hxqc.mall.core.util.ImageUtil;
import com.hxqc.mall.core.util.OtherUtil;
import com.hxqc.mall.core.util.ToastHelper;
import com.hxqc.mall.core.util.WidgetController;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.accessory.adapter.ConditionListAdapter;
import com.hxqc.mall.thirdshop.accessory.adapter.SingleConditionAdapter;
import com.hxqc.mall.thirdshop.accessory.fragment.SelectCarModelFragment;
import com.hxqc.mall.thirdshop.accessory.model.ChoseCondition;
import com.hxqc.mall.thirdshop.accessory.model.Condition;
import com.hxqc.mall.thirdshop.accessory.model.Photo;
import com.hxqc.mall.thirdshop.accessory.model.ProductDetail;
import com.hxqc.mall.thirdshop.accessory.model.ProductsForProperty;
import com.hxqc.mall.thirdshop.accessory.utils.ActivitySwitcherAccessory;
import com.hxqc.mall.thirdshop.accessory.views.ItemView;
import com.hxqc.mall.thirdshop.accessory.views.ScrollViewBottom;
import com.hxqc.mall.thirdshop.accessory.views.ScrollViewContainer;
import com.hxqc.socialshare.manager.ShareController;
import com.hxqc.util.DisplayTools;
import com.hxqc.util.JSONUtils;
import com.hxqc.util.ScreenUtil;
import com.hxqc.widget.ListViewNoSlide;
import com.umeng.socialize.UMShareAPI;

import net.simonvt.menudrawer.MenuDrawer;
import net.simonvt.menudrawer.OverlayDrawer;

import cz.msebera.android.httpclient.Header;

import java.util.ArrayList;

/**
 * 商品详情 旧版本
 * Created by huangyi on 16/2/22.
 */
public class ProductDetailActivity extends AccessoryNoBackActivity implements View.OnClickListener {

    public OverlayDrawer drawerView; //自定义侧滑 android DrawerLayout侧滑此界面有Bug
    String mProductID;
    ProductDetail mProductDetail; //数据源
    ShareController mShareController; //分享
    Toolbar mToolbar;
    ScrollViewContainer mContainerView;
    ImageView mPhotoView;
    TextView mNameView, mPriceView, mAddView;
    ItemView mCarView, mPackageView, mColorView;

    ListViewNoSlide mConditionView; //条件(颜色 尺寸...)
    ArrayList<ChoseCondition> mChoseCondition;
    ConditionListAdapter mAdapter;
    ArrayList<ProductsForProperty> mProductsForProperty;
    ScrollViewBottom mIntroduceWebView; //商品介绍
    LinearLayout mBottomView; //底部固定View parent
    RequestFailView mFailView; //请求错误View
    Dialog mDialog; //加入购物车弹窗
    TextView mNumberView1, mNumberView2;

    SelectCarModelFragment mSelectCarModelFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(WindowCompat.FEATURE_ACTION_MODE_OVERLAY); //解决 WebView使用剪切板 回弹到第一页Bug
        setContentView(R.layout.activity_product_detail);

        mProductID = getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA).getString(ActivitySwitcherAccessory.PRODUCT_ID);
        initView();
        initData(true, true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mShareController != null) {
            mShareController.onActivityResult(this, requestCode, resultCode, data);
        }
    }

    private void initView() {
        mToolbar = (Toolbar) findViewById(R.id.product_detail_toolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_back);
        mToolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(mToolbar);

        drawerView = (OverlayDrawer) findViewById(R.id.product_detail_drawer);
        drawerView.setTouchMode(MenuDrawer.TOUCH_MODE_FULLSCREEN_MENUOPEN);
        drawerView.setSidewardCloseMenu(true);
        drawerView.setMenuSize(DisplayTools.getScreenWidth(this) / 8 * 7);

        mContainerView = (ScrollViewContainer) findViewById(R.id.product_detail_container);
        mContainerView.initActionBarHeight(56);
        mContainerView.initBottomViewHeight(50);

        mPhotoView = (ImageView) findViewById(R.id.product_detail_photo);
        mPhotoView.setOnClickListener(this);
        mPhotoView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, ScreenUtil.getScreenWidth(this)));
        mNameView = (TextView) findViewById(R.id.product_detail_name);
        mPriceView = (TextView) findViewById(R.id.product_detail_price);

        mCarView = (ItemView) findViewById(R.id.product_detail_car);
        mPackageView = (ItemView) findViewById(R.id.product_detail_package);
        mColorView = (ItemView) findViewById(R.id.product_detail_color);

        mCarView.setOnClickListener(this);
        mPackageView.setOnClickListener(this);

        mConditionView = (ListViewNoSlide) findViewById(R.id.package_detail_condition);
        mChoseCondition = new ArrayList<>();
        mChoseCondition.add(new ChoseCondition("", "", "")); //防止 ListViewNoSlide 获取焦点
        mAdapter = new ConditionListAdapter(this, mChoseCondition);
        mConditionView.setAdapter(mAdapter);
        mConditionView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showPopupWindow(view, position);
            }
        });

        mIntroduceWebView = (ScrollViewBottom) findViewById(R.id.product_detail_wv_introduce);
        mIntroduceWebView.getSettings().setJavaScriptEnabled(true);
        mBottomView = (LinearLayout) findViewById(R.id.product_detail_bottom);

        findViewById(R.id.product_detail_service).setOnClickListener(this);
        findViewById(R.id.product_detail_share).setOnClickListener(this);
        findViewById(R.id.product_detail_shopping_car).setOnClickListener(this);
        mAddView = (TextView) findViewById(R.id.product_detail_add_car);
        mAddView.setOnClickListener(this);

        mFailView = (RequestFailView) findViewById(R.id.product_detail_fail);
        mFailView.setEmptyDescription("暂无数据");
        mFailView.setEmptyButtonClick("刷新", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initData(true, true);
            }
        });
        mFailView.setFailButtonClick("刷新", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initData(false, true);
            }
        });

        mSelectCarModelFragment = new SelectCarModelFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.mdMenu, mSelectCarModelFragment).commit();
    }

    private void initData(boolean showAnim, boolean cancelOutside) {
        mAccessoryApiClient.getProductDetail(mProductID, new LoadingAnimResponseHandler(this, showAnim, cancelOutside) {
            @Override
            public void onSuccess(String response) {
                mProductDetail = JSONUtils.fromJson(response, ProductDetail.class);
                if (mProductDetail == null) {
                    mContainerView.setVisibility(View.GONE);
                    mBottomView.setVisibility(View.GONE);
                    mFailView.setVisibility(View.VISIBLE);
                    mFailView.setRequestViewType(RequestFailView.RequestViewType.empty);
                } else {
                    mContainerView.setVisibility(View.VISIBLE);
                    mBottomView.setVisibility(View.VISIBLE);
                    mFailView.setVisibility(View.GONE);
                    bindData();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                mProductDetail = null;
                mContainerView.setVisibility(View.GONE);
                mBottomView.setVisibility(View.GONE);
                mFailView.setVisibility(View.VISIBLE);
                mFailView.setRequestViewType(RequestFailView.RequestViewType.fail);
            }
        });
    }

    private void bindData() {
        ImageUtil.setImageSquare(this, mPhotoView, mProductDetail.cover);
        mNameView.setText(mProductDetail.productInfo.name);
        mPriceView.setText(OtherUtil.amountFormat(mProductDetail.productInfo.price, true));
        OtherUtil.setVisible(mPackageView, mProductDetail.packages != null && mProductDetail.packages.size() > 0);
        mChoseCondition.clear();
        mChoseCondition.addAll(mProductDetail.choseCondition);
        mAdapter.notifyDataSetChanged();
        mProductsForProperty = mProductDetail.productsForProperty;
        if (mChoseCondition.size() == 0) {
            mColorView.setVisibility(View.INVISIBLE);
            mConditionView.setVisibility(View.GONE);
        } else {
            mColorView.setVisibility(View.VISIBLE);
            mConditionView.setVisibility(View.VISIBLE);

            StringBuilder sb = new StringBuilder();
            for (ChoseCondition c : mProductDetail.choseCondition) {
                sb.append(c.conditionName);
            }
            mColorView.setName(sb.toString());
        }
        if (mProductDetail.inventory == -1) { //已下架
            mAddView.setEnabled(false);
            mAddView.setText("已下架");
            mAddView.setTextColor(ContextCompat.getColor(this, R.color.divider));
            mAddView.setBackgroundColor(Color.parseColor("#999999"));
        } else {
            mAddView.setEnabled(true);
            mAddView.setText("加入购物车");
            mAddView.setTextColor(ContextCompat.getColor(this, R.color.white));
            mAddView.setBackgroundColor(Color.parseColor("#ffE02C37"));
        }

        mIntroduceWebView.loadUrl(mProductDetail.productIntroduce);
    }

    private void showPopupWindow(View view, final int position) {
        //数据处理
        final ArrayList<Condition> conditions = new ArrayList<>();
        switch (position) {
            case 0:
                for (ProductsForProperty p : mProductsForProperty) {
                    //剔除第一选项已选条件
                    if (!mChoseCondition.get(position).conditionValue.trim().equals(p.conditions.get(position).conditionValue.trim())) {
                        boolean flag = true;
                        for (Condition c : conditions) {
                            //剔除重复条件
                            if (c.conditionValue.trim().equals(p.conditions.get(position).conditionValue.trim())) {
                                flag = false;
                                break;
                            }
                        }
                        if (flag) conditions.add(p.conditions.get(position));
                    }
                }
                break;
            case 1:
                for (ProductsForProperty p : mProductsForProperty) {
                    if (mChoseCondition.get(position - 1).conditionValue.trim().equals(p.conditions.get(position - 1).conditionValue.trim()) && //考虑第一选项已选条件
                            !mChoseCondition.get(position).conditionValue.trim().equals(p.conditions.get(position).conditionValue.trim())) { //剔除第二选项已选条件
                        boolean flag = true;
                        for (Condition c : conditions) {
                            //剔除重复条件
                            if (c.conditionValue.trim().equals(p.conditions.get(position).conditionValue.trim())) {
                                flag = false;
                                break;
                            }
                        }
                        if (flag) conditions.add(p.conditions.get(position));
                    }
                }
                break;
            case 2:
                for (ProductsForProperty p : mProductsForProperty) {
                    if (mChoseCondition.get(position - 2).conditionValue.trim().equals(p.conditions.get(position - 2).conditionValue.trim()) && //考虑第一选项已选条件
                            mChoseCondition.get(position - 1).conditionValue.trim().equals(p.conditions.get(position - 1).conditionValue.trim()) && //考虑第二选项已选条件
                            !mChoseCondition.get(position).conditionValue.trim().equals(p.conditions.get(position).conditionValue.trim())) { //剔除第三选项已选条件
                        boolean flag = true;
                        for (Condition c : conditions) {
                            //剔除重复条件
                            if (c.conditionValue.trim().equals(p.conditions.get(position).conditionValue.trim())) {
                                flag = false;
                                break;
                            }
                        }
                        if (flag) conditions.add(p.conditions.get(position));
                    }
                }
                break;
        }

        if (conditions.size() == 0) return;
        final ListPopupWindow mPopupWindow = new ListPopupWindow(this);
        SingleConditionAdapter adapter = new SingleConditionAdapter(this, conditions);
        mPopupWindow.setAdapter(adapter);
        mPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int PopupPosition, long id) {
                mChoseCondition.get(position).conditionValue = conditions.get(PopupPosition).conditionValue;
                mChoseCondition.get(position).color = conditions.get(PopupPosition).color;
                switch (position) {
                    case 0:
                        //如果查不到商品 自动修改第二选项, 第三选项条件确保商品存在
                        if (mChoseCondition.size() > position + 1 && TextUtils.isEmpty(getProductIDForChosenCondition())) {
                            for (ProductsForProperty p : mProductsForProperty) {
                                boolean flag = true;
                                for (int i = 0; i < position + 1; i++) {
                                    if (!mChoseCondition.get(i).conditionValue.trim().equals(p.conditions.get(i).conditionValue.trim())) {
                                        flag = false;
                                        break;
                                    }
                                }
                                if (flag) {
                                    if (mChoseCondition.size() == position + 2) {
                                        mChoseCondition.get(position + 1).conditionValue = p.conditions.get(position + 1).conditionValue;
                                        mChoseCondition.get(position + 1).color = p.conditions.get(position + 1).color;
                                        break;
                                    } else {
                                        if (mChoseCondition.get(position + 1).conditionValue.trim().equals(p.conditions.get(position + 1).conditionValue.trim())) {
                                            mChoseCondition.get(position + 2).conditionValue = p.conditions.get(position + 2).conditionValue;
                                            mChoseCondition.get(position + 2).color = p.conditions.get(position + 2).color;
                                            break;
                                        } else if (mChoseCondition.get(position + 2).conditionValue.trim().equals(p.conditions.get(position + 2).conditionValue.trim())) {
                                            mChoseCondition.get(position + 1).conditionValue = p.conditions.get(position + 1).conditionValue;
                                            mChoseCondition.get(position + 1).color = p.conditions.get(position + 1).color;
                                            break;
                                        } else {
                                            mChoseCondition.get(position + 1).conditionValue = p.conditions.get(position + 1).conditionValue;
                                            mChoseCondition.get(position + 1).color = p.conditions.get(position + 1).color;
                                            mChoseCondition.get(position + 2).conditionValue = p.conditions.get(position + 2).conditionValue;
                                            mChoseCondition.get(position + 2).color = p.conditions.get(position + 2).color;
                                        }
                                    }
                                }
                            }
                        }
                        break;
                    case 1:
                        //如果查不到商品 自动修改第三选项条件确保商品存在
                        if (mChoseCondition.size() > position + 1 && TextUtils.isEmpty(getProductIDForChosenCondition())) {
                            for (ProductsForProperty p : mProductsForProperty) {
                                boolean flag = true;
                                for (int i = 0; i < position + 1; i++) {
                                    if (!mChoseCondition.get(i).conditionValue.trim().equals(p.conditions.get(i).conditionValue.trim())) {
                                        flag = false;
                                        break;
                                    }
                                }
                                if (flag) {
                                    mChoseCondition.get(position + 1).conditionValue = p.conditions.get(position + 1).conditionValue;
                                    mChoseCondition.get(position + 1).color = p.conditions.get(position + 1).color;
                                    break;
                                }
                            }
                        }
                        break;
                }
                mAdapter.notifyDataSetChanged();
                mPopupWindow.dismiss();
                mProductID = getProductIDForChosenCondition();
                initData(true, true);
            }
        });

        //show
        mPopupWindow.setAnchorView(view);
        mPopupWindow.setWidth(ScreenUtil.getScreenWidth(this) / 3 * 2); //宽度为手机屏幕的三分之二
        int horizontalDimension = (int) getResources().getDimension(R.dimen.card_horizontal_spacing);
        mPopupWindow.setHorizontalOffset(ScreenUtil.getScreenWidth(this) / 3 - horizontalDimension); //横向位置居右

        int viewHeight = WidgetController.getHeight(view) / 3;

        if (conditions.size() > 4) { //PopupWindow高度定死
            int popupHeight = (int) getResources().getDimension(R.dimen.card_height);
            mPopupWindow.setHeight(popupHeight);
            int verticalDimension = (int) getResources().getDimension(R.dimen.card_vertical_spacing_debug);
            int height = popupHeight + viewHeight + verticalDimension;
            mPopupWindow.setVerticalOffset(-height);
        } else {
            mPopupWindow.setHeight(ListPopupWindow.WRAP_CONTENT);
            int verticalDimension = (int) getResources().getDimension(R.dimen.card_vertical_spacing);
            int height = ScreenUtil.dip2px(this, 38 * conditions.size()) + viewHeight + verticalDimension;
            mPopupWindow.setVerticalOffset(-height);
        }

        //true ---> If the user touches outside the popup window's content area the popup window will be dismissed.
        mPopupWindow.setModal(true);
        mPopupWindow.show();
    }

    /**
     * 根据已选条件获取商品id 查不到商品id返回""
     **/
    private String getProductIDForChosenCondition() {
        String productID = "";
        for (ProductsForProperty p : mProductsForProperty) {
            boolean flag = true;
            for (int i = 0; i < p.conditions.size(); i++) {
                if (!mChoseCondition.get(i).conditionValue.trim().equals(p.conditions.get(i).conditionValue.trim())) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                productID = p.productID;
                break;
            }
        }
        return productID;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.product_detail_photo && null != mProductDetail && mProductDetail.photos.size() > 0) {
            ArrayList<ImageModel> list = new ArrayList<>();
            for (Photo photo : mProductDetail.photos) {
                list.add(new ImageModel(photo.sourcePath, photo.thumbnailPath));
            }
            ActivitySwitcherAccessory.toPhoto(this, list);

        } else if (id == R.id.product_detail_car) {
            if (!drawerView.isMenuVisible()) drawerView.openMenu();
            mSelectCarModelFragment.initData(mProductID);

        } else if (id == R.id.product_detail_package && null != mProductDetail) {
            ActivitySwitcherAccessory.toPackageList(this, mProductDetail.packages);

        } else if (id == R.id.product_detail_service) {
            new AlertDialog.Builder(ProductDetailActivity.this, R.style.MaterialDialog)
                    .setTitle("拨打电话")
                    .setMessage("400-1868-555")
                    .setPositiveButton("拨打", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:400-1868-555")));
                        }
                    })
                    .setNegativeButton("取消", null)
                    .create()
                    .show();

        } else if (id == R.id.product_detail_share && null != mProductDetail && null != mProductDetail.share) {
            share();

        } else if (id == R.id.product_detail_shopping_car) {
            UserInfoHelper.getInstance().loginAction(ProductDetailActivity.this, new UserInfoHelper.OnLoginListener() {
                @Override
                public void onLoginSuccess() {
                    ActivitySwitcherAccessory.toShoppingCart(ProductDetailActivity.this);
                }
            });

        } else if (id == R.id.product_detail_add_car && null != mProductDetail) {
            showDialog();

        } else if (id == R.id.dialog_product_detail_close && null != mDialog) {
            mDialog.dismiss();

        } else if (id == R.id.dialog_product_detail_minus && null != mDialog) { //减
            int number = Integer.parseInt(mNumberView1.getText().toString().trim());
            if (number <= 1) {
                mNumberView1.setText("" + 1);
                mNumberView2.setText("已选 \"" + 1 + "件\"");
            } else {
                number--;
                mNumberView1.setText("" + number);
                mNumberView2.setText("已选 \"" + number + "件\"");
            }

        } else if (id == R.id.dialog_product_detail_add && null != mDialog) { //加
            int number = Integer.parseInt(mNumberView1.getText().toString().trim());
            if (number >= 99) {
                mNumberView1.setText("" + 99);
                mNumberView2.setText("已选 \"" + 99 + "件\"");
            } else {
                number++;
                mNumberView1.setText("" + number);
                mNumberView2.setText("已选 \"" + number + "件\"");
            }

        } else if (id == R.id.dialog_product_detail_ok && null != mDialog) {
            mDialog.dismiss();
            UserInfoHelper.getInstance().loginAction(this, 50, new UserInfoHelper.OnLoginListener() {
                @Override
                public void onLoginSuccess() {
                    addCar(Integer.parseInt(mNumberView1.getText().toString().trim()) + "");
                }
            });
        }
    }

    private void showDialog() {
        View mDialogView = LayoutInflater.from(this).inflate(R.layout.dialog_product_detail, null);
        ImageUtil.setImageSquare(this, ((ImageView) mDialogView.findViewById(R.id.dialog_product_detail_photo)), mProductDetail.cover);
        ((TextView) mDialogView.findViewById(R.id.dialog_product_detail_price)).setText(OtherUtil.amountFormat(mProductDetail.productInfo.price, true));
        mNumberView1 = (TextView) mDialogView.findViewById(R.id.dialog_product_detail_edit);
        mNumberView2 = (TextView) mDialogView.findViewById(R.id.dialog_product_detail_number);
        mNumberView2.setText("已选 \"1件\"");
        StringBuffer sb = new StringBuffer();
        sb.append("已选 ");
        for (ChoseCondition c : mProductDetail.choseCondition) {
            sb.append("\"" + c.conditionValue + "\" ");
        }
        ((TextView) mDialogView.findViewById(R.id.dialog_product_detail_condition)).setText(sb.toString());

        mDialogView.findViewById(R.id.dialog_product_detail_close).setOnClickListener(this);
        mDialogView.findViewById(R.id.dialog_product_detail_minus).setOnClickListener(this);
        mDialogView.findViewById(R.id.dialog_product_detail_add).setOnClickListener(this);
        mDialogView.findViewById(R.id.dialog_product_detail_ok).setOnClickListener(this);

        mDialog = new Dialog(this, R.style.FullWidthDialog);
        mDialog.setContentView(mDialogView);
        mDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT); //设置dialog宽高
        mDialog.getWindow().setGravity(Gravity.BOTTOM); //设置dialog显示的位置
        mDialog.show();
    }

    private void addCar(String productNum) {
        mAccessoryApiClient.addShoppingCart(productNum, "", mProductDetail.productInfo.productID, "",
                new LoadingAnimResponseHandler(this, true, false) {
                    @Override
                    public void onSuccess(String response) {
                        ToastHelper.showGreenToast(ProductDetailActivity.this, "加入购物车成功");
                        ShoppingCartActivity.sNeedRefresh = true;
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);
                        ToastHelper.showRedToast(ProductDetailActivity.this, "加入购物车失败");
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_tohome, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_to_home) {
            ActivitySwitchBase.toMain(this, 0);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        if (drawerView.isMenuVisible()) {
            drawerView.closeMenu();
        } else {
            finish();
        }
        return super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        if (drawerView.isMenuVisible()) {
            drawerView.closeMenu();
        } else {
            finish();
        }
    }

    private void share() {
        if (mShareController == null) {
            mShareController = new ShareController(this);
        }
        UMShareAPI.get(this);
        mShareController.showSharePopupWindow(mProductDetail.share);
    }

}
