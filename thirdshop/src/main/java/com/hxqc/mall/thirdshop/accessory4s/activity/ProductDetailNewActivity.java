package com.hxqc.mall.thirdshop.accessory4s.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hxqc.mall.core.api.LoadingAnimResponseHandler;
import com.hxqc.mall.core.api.RequestFailView;
import com.hxqc.mall.core.controler.UserInfoHelper;
import com.hxqc.mall.core.model.ImageModel;
import com.hxqc.mall.core.model.auto.PickupPointT;
import com.hxqc.mall.core.util.ActivitySwitchBase;
import com.hxqc.mall.core.util.ImageUtil;
import com.hxqc.mall.core.util.OtherUtil;
import com.hxqc.mall.core.util.ToastHelper;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.accessory.model.ChoseCondition;
import com.hxqc.mall.thirdshop.accessory.model.Condition;
import com.hxqc.mall.thirdshop.accessory.model.Photo;
import com.hxqc.mall.thirdshop.accessory.model.ProductDetail;
import com.hxqc.mall.thirdshop.accessory.views.ItemView;
import com.hxqc.mall.thirdshop.accessory4s.adapter.ConditionListNewAdapter;
import com.hxqc.mall.thirdshop.accessory4s.utils.ActivitySwitcherAccessory4S;
import com.hxqc.mall.thirdshop.utils.ActivitySwitcherThirdPartShop;
import com.hxqc.mall.thirdshop.views.CallBar;
import com.hxqc.socialshare.manager.ShareController;
import com.hxqc.util.JSONUtils;
import com.hxqc.util.ScreenUtil;
import com.umeng.socialize.UMShareAPI;

import cz.msebera.android.httpclient.Header;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 商品详情
 * Created by huangyi on 16/2/22.
 */
public class ProductDetailNewActivity extends Accessory4SBackActivity implements View.OnClickListener, ConditionListNewAdapter.OnRefreshListener {

    public final static String PRODUCT_ID = "product_id";
    public final static String PRODUCT_GROUP_ID = "product_group_id";

    String mProductID, mProductGroupID;
    ProductDetail mProductDetail; //数据源
    ShareController mShareController; //分享

    LinearLayout mContentView;
    ImageView mPhotoView;
    TextView mNameView, mPriceView, mAddView;
    ItemView mAddressView, mColorView, mPackageView;
    WebView mIntroduceView;
    LinearLayout mBottomView; //底部固定View parent
    RequestFailView mFailView; //请求错误View
    Dialog mDialog; //加入购物车弹窗
    View mDialogView;
    ImageView mDialogPhotoView;
    TextView mDialogPriceView, mDialogNumberView, mDialogEditView;
    ListView mDialogListView;
    ConditionListNewAdapter mDialogAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail_new);

        mProductID = getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA).getString(PRODUCT_ID);
        mProductGroupID = getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA).getString(PRODUCT_GROUP_ID);
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
        mContentView = (LinearLayout) findViewById(R.id.detail_content);
        mPhotoView = (ImageView) findViewById(R.id.detail_photo);
        mPhotoView.setOnClickListener(this);
        mPhotoView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, ScreenUtil.getScreenWidth(this)));
        mNameView = (TextView) findViewById(R.id.detail_name);
        mPriceView = (TextView) findViewById(R.id.detail_price);
        mAddressView = (ItemView) findViewById(R.id.detail_address);
        mColorView = (ItemView) findViewById(R.id.detail_color);
        mPackageView = (ItemView) findViewById(R.id.detail_package);
        mAddressView.setOnClickListener(this);
        mColorView.setOnClickListener(this);
        mPackageView.setOnClickListener(this);
        mIntroduceView = (WebView) findViewById(R.id.detail_introduce);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mIntroduceView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING);
        } else {
            mIntroduceView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        }
        mIntroduceView.getSettings().setLoadWithOverviewMode(true);
        mIntroduceView.getSettings().setJavaScriptEnabled(true);
        mIntroduceView.getSettings().setUseWideViewPort(true);
        mBottomView = (LinearLayout) findViewById(R.id.detail_bottom);

        findViewById(R.id.detail_shop).setOnClickListener(this);
        findViewById(R.id.detail_shopping_car).setOnClickListener(this);
        mAddView = (TextView) findViewById(R.id.detail_add_car);
        mAddView.setOnClickListener(this);

        mFailView = (RequestFailView) findViewById(R.id.detail_fail);
        mFailView.setEmptyDescription("商品未找到");
        mFailView.setEmptyButtonClick("返回", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mFailView.setFailButtonClick("刷新", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initData(false, true);
            }
        });
    }

    private void initData(boolean showAnim, boolean cancelOutside) {
        mAccessoryApiClient.productDetail(mProductID, mProductGroupID, new LoadingAnimResponseHandler(this, showAnim, cancelOutside) {
            @Override
            public void onSuccess(String response) {
                mProductDetail = JSONUtils.fromJson(response, ProductDetail.class);
                if (mProductDetail == null) {
                    mContentView.setVisibility(View.GONE);
                    mBottomView.setVisibility(View.GONE);
                    mFailView.setVisibility(View.VISIBLE);
                    mFailView.setRequestViewType(RequestFailView.RequestViewType.empty);
                } else {
                    mContentView.setVisibility(View.VISIBLE);
                    mBottomView.setVisibility(View.VISIBLE);
                    mFailView.setVisibility(View.GONE);
                    bindData();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                mProductDetail = null;
                mContentView.setVisibility(View.GONE);
                mBottomView.setVisibility(View.GONE);
                mFailView.setVisibility(View.VISIBLE);
                mFailView.setRequestViewType(RequestFailView.RequestViewType.fail);
            }
        });
    }

    private void bindData() {
        ImageUtil.setImageSquare(this, mPhotoView, mProductDetail.cover);
        mNameView.setText(mProductDetail.productInfo.name);
        mPriceView.setText(mProductDetail.productInfo.price);
        mAddressView.setValue(mProductDetail.shopInfo.shopTitle);
        OtherUtil.setVisible(mPackageView, mProductDetail.packages != null && mProductDetail.packages.size() > 0);

        if (mProductDetail.productsForProperty.size() != 0) {
            mColorView.setVisibility(View.VISIBLE);
            StringBuilder sb = new StringBuilder();
            for (Condition c : mProductDetail.productsForProperty.get(0).conditions) {
                sb.append(c.conditionName).append(" ");
            }
            mColorView.setName(sb.toString());
        } else {
            mColorView.setVisibility(View.GONE);
        }
        if (mProductDetail.choseCondition.size() != 0) {
            StringBuilder sb = new StringBuilder();
            for (ChoseCondition c : mProductDetail.choseCondition) {
                sb.append(c.conditionValue).append(" ");
            }
            mColorView.setValue("已选 " + sb.toString());
        } else {
            mColorView.setValue("");
        }

        if (mProductDetail.inventory == -1) { //已下架
//            mAddView.setEnabled(false);
//            mAddView.setText("已下架");
//            mAddView.setTextColor(ContextCompat.getColor(this, R.color.divider));
//            mAddView.setBackgroundColor(Color.parseColor("#999999"));

            mContentView.setVisibility(View.GONE);
            mBottomView.setVisibility(View.GONE);
            mFailView.setVisibility(View.VISIBLE);
            mFailView.setRequestViewType(RequestFailView.RequestViewType.empty);
        } else {
            mAddView.setEnabled(true);
            mAddView.setText("加入购物车");
            mAddView.setTextColor(ContextCompat.getColor(this, R.color.white));
            mAddView.setBackgroundColor(Color.parseColor("#ffE02C37"));
        }

        ((CallBar) findViewById(R.id.detail_call)).setNumber(mProductDetail.shopInfo.tel);
        mIntroduceView.loadDataWithBaseURL(null, getImgWeb(mProductDetail.productIntroduce), "text/html", "utf-8", null);
        if (null != mDialog) {
            //更新数据
            ImageUtil.setImageSquare(this, mDialogPhotoView, mProductDetail.cover);
            mDialogPriceView.setText(mProductDetail.productInfo.price);
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.detail_photo && null != mProductDetail && mProductDetail.photos.size() > 0) {
            ArrayList<ImageModel> list = new ArrayList<>();
            for (Photo photo : mProductDetail.photos) {
                list.add(new ImageModel(photo.sourcePath, photo.thumbnailPath));
            }
            ActivitySwitcherAccessory4S.toPhoto(this, list);

        } else if (id == R.id.detail_address && null != mProductDetail) {
            ActivitySwitchBase.toAMapNvai(this, new PickupPointT(mProductDetail.shopInfo.address, mProductDetail.shopInfo.latitude,
                    mProductDetail.shopInfo.longitude, mProductDetail.shopInfo.tel));

        } else if (id == R.id.detail_color && null != mProductDetail) {
            showDialog();

        } else if (id == R.id.detail_package && null != mProductDetail) {
            if (mProductDetail.productsForProperty.size() == 0 || mProductDetail.productsForProperty.get(0).conditions.size() == 0) {
                //没有商品参数可选
                ActivitySwitcherAccessory4S.toPackageList(this, mProductDetail.packages, mProductDetail.shopInfo.shopTitle, mProductDetail.shopInfo.shopID);

            } else {
                if (!TextUtils.isEmpty(mProductID) && null == mDialogAdapter) {
                    ActivitySwitcherAccessory4S.toPackageList(this, mProductDetail.packages, mProductDetail.shopInfo.shopTitle, mProductDetail.shopInfo.shopID);
                } else {
                    if (null != mDialogAdapter && mDialogAdapter.isSelected())
                        ActivitySwitcherAccessory4S.toPackageList(this, mProductDetail.packages, mProductDetail.shopInfo.shopTitle, mProductDetail.shopInfo.shopID);
                }
            }

        } else if (id == R.id.detail_shop && null != mProductDetail) {
            ActivitySwitcherThirdPartShop.toShopHome(mProductDetail.shopInfo.shopID, this);

        } else if (id == R.id.detail_shopping_car) {
            UserInfoHelper.getInstance().loginAction(ProductDetailNewActivity.this, new UserInfoHelper.OnLoginListener() {
                @Override
                public void onLoginSuccess() {
                    ActivitySwitcherAccessory4S.toShoppingCart(ProductDetailNewActivity.this);
                }
            });

        } else if (id == R.id.detail_add_car && null != mProductDetail) {
            showDialog();

        } else if (id == R.id.dialog_product_detail_close && null != mDialog) {
            mDialog.dismiss();

        } else if (id == R.id.dialog_product_detail_minus && null != mDialog) { //减
            int number = Integer.parseInt(mDialogEditView.getText().toString().trim());
            if (number <= 1) {
                mDialogEditView.setText("" + 1);
                mDialogNumberView.setText("已选 \"" + 1 + "件\"");
            } else {
                number--;
                mDialogEditView.setText("" + number);
                mDialogNumberView.setText("已选 \"" + number + "件\"");
            }

        } else if (id == R.id.dialog_product_detail_add && null != mDialog) { //加
            int number = Integer.parseInt(mDialogEditView.getText().toString().trim());
            if (number >= 99) {
                mDialogEditView.setText("" + 99);
                mDialogNumberView.setText("已选 \"" + 99 + "件\"");
            } else {
                number++;
                mDialogEditView.setText("" + number);
                mDialogNumberView.setText("已选 \"" + number + "件\"");
            }

        } else if (id == R.id.dialog_product_detail_ok && null != mDialog) {
            if (mProductDetail.productsForProperty.size() == 0 || mProductDetail.productsForProperty.get(0).conditions.size() == 0) {
                //没有商品参数可选 直接加入购物车
                UserInfoHelper.getInstance().loginAction(this, 50, new UserInfoHelper.OnLoginListener() {
                    @Override
                    public void onLoginSuccess() {
                        addCar(mDialogEditView.getText().toString());
                    }
                });
            } else {
                if (mDialogAdapter.isSelected()) {
                    UserInfoHelper.getInstance().loginAction(this, 50, new UserInfoHelper.OnLoginListener() {
                        @Override
                        public void onLoginSuccess() {
                            addCar(mDialogEditView.getText().toString());
                        }
                    });
                } else {
                    ToastHelper.showYellowToast(ProductDetailNewActivity.this, "请选择" + mDialogAdapter.getToast());
                }
            }
        }
    }

    private void showDialog() {
        if (null == mDialog) {
            mDialogView = LayoutInflater.from(this).inflate(R.layout.dialog_product_detail_new, null);
            mDialogPhotoView = (ImageView) mDialogView.findViewById(R.id.dialog_product_detail_photo);
            mDialogPriceView = (TextView) mDialogView.findViewById(R.id.dialog_product_detail_price);
            mDialogNumberView = (TextView) mDialogView.findViewById(R.id.dialog_product_detail_number);
            mDialogEditView = (TextView) mDialogView.findViewById(R.id.dialog_product_detail_edit);
            mDialogListView = (ListView) mDialogView.findViewById(R.id.dialog_product_detail_list);

            mDialogView.findViewById(R.id.dialog_product_detail_close).setOnClickListener(this);
            mDialogView.findViewById(R.id.dialog_product_detail_minus).setOnClickListener(this);
            mDialogView.findViewById(R.id.dialog_product_detail_add).setOnClickListener(this);
            mDialogView.findViewById(R.id.dialog_product_detail_ok).setOnClickListener(this);

            //初始Dialog
            mDialog = new Dialog(this, R.style.FullWidthDialog);
            mDialog.setContentView(mDialogView);
            mDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT); //设置dialog宽高
            mDialog.getWindow().setGravity(Gravity.BOTTOM); //设置dialog显示的位置

            //初始数据
            ImageUtil.setImageSquare(this, mDialogPhotoView, mProductDetail.cover);
            mDialogPriceView.setText(mProductDetail.productInfo.price);
            mDialogNumberView.setText("已选 \"1件\"");
            mDialogAdapter = new ConditionListNewAdapter(this, mProductDetail.productsForProperty, this);
            switch (mProductDetail.choseCondition.size()) {
                case 1:
                    mDialogAdapter.init(mProductDetail.choseCondition.get(0).conditionValue, "", "");
                    break;

                case 2:
                    mDialogAdapter.init(mProductDetail.choseCondition.get(0).conditionValue, mProductDetail.choseCondition.get(1).conditionValue, "");
                    break;

                case 3:
                    mDialogAdapter.init(mProductDetail.choseCondition.get(0).conditionValue, mProductDetail.choseCondition.get(1).conditionValue,
                            mProductDetail.choseCondition.get(2).conditionValue);
                    break;
            }
            mDialogListView.setAdapter(mDialogAdapter);

            //控制ListView高度
            int height = 0;
            for (int i = 0; i < mDialogAdapter.getCount(); i++) { //listAdapter.getCount()返回数据项的数目
                View listItem = mDialogAdapter.getView(i, null, mDialogListView);
                listItem.measure(0, 0); //计算子项View的宽高
                height += listItem.getMeasuredHeight(); //统计所有子项的总高度
            }
            height += (mDialogListView.getDividerHeight() * (mDialogListView.getCount() - 1));
            int h = ScreenUtil.getScreenHeight(this) / 4;
            if (height > h) {
                ViewGroup.LayoutParams params = mDialogListView.getLayoutParams();
                params.height = h;
                mDialogListView.setLayoutParams(params);
            }
        }

        mDialog.show();
    }

    private void addCar(String productNum) {
        mAccessoryApiClient.addShoppingCart(mDialogAdapter.getProductID(), productNum, mProductDetail.shopInfo.shopTitle, mProductDetail.shopInfo.shopID, "",
                new LoadingAnimResponseHandler(this, true, false) {
                    @Override
                    public void onSuccess(String response) {
                        ToastHelper.showGreenToast(ProductDetailNewActivity.this, "加入购物车成功");
                        ShoppingCart4SActivity.sNeedRefresh = true;
                        mDialog.dismiss();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);
                        ToastHelper.showRedToast(ProductDetailNewActivity.this, "加入购物车失败");
                    }
                });
    }

    @Override
    public void onRefreshData() {
        mProductID = mDialogAdapter.getProductID();
        mProductGroupID = "";
        initData(true, true);
    }

    @Override
    public void onRefreshSelected(String selected) {
        mColorView.setValue(TextUtils.isEmpty(selected) ? "" : "已选 " + selected);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.t_menu_to_share_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == R.id.action_to_home) {
            ActivitySwitcherThirdPartShop.toMain(this, 0);

        } else if (item.getItemId() == R.id.action_to_share && null != mProductDetail && null != mProductDetail.share) {
            share();
        }
        return false;
    }

    private void share() {
        if (mShareController == null) {
            mShareController = new ShareController(this);
        }
        UMShareAPI.get(this);
        mShareController.showSharePopupWindow(mProductDetail.share);
    }

    private String getImgWeb(String content) {
        content = content.replace("<img data-lazyload=\"", "<img src=\"http:").replace("http:http:", "http:");
        String img = "";
        Pattern pImage;
        Matcher mImage;
        String str = "";
        String regexImg = "(<img.*src\\s*=\\s*(.*?)[^>]*?>)";
        pImage = Pattern.compile(regexImg, Pattern.CASE_INSENSITIVE);
        mImage = pImage.matcher(content);
        while (mImage.find()) {
            img = mImage.group();
            Matcher m = Pattern.compile("src\\s*=\\s*\"?(.*?)(\"|>|\\s+)").matcher(img);
            while (m.find()) {
                String temp = m.group(1);
                str = str + "<img width=100% src=\"" + temp + "\"/>";
            }
        }
        return str;
    }

}
