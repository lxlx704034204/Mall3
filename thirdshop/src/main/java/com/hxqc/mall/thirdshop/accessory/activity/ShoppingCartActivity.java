package com.hxqc.mall.thirdshop.accessory.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hxqc.mall.core.api.LoadingAnimResponseHandler;
import com.hxqc.mall.core.api.RequestFailView;
import com.hxqc.mall.core.util.OtherUtil;
import com.hxqc.mall.core.util.ToastHelper;
import com.hxqc.mall.core.views.dialog.NormalDialog;
import com.hxqc.mall.core.views.pullrefreshhandler.OnRefreshHandler;
import com.hxqc.mall.core.views.pullrefreshhandler.UltraPullRefreshHeaderHelper;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.accessory.adapter.ShoppingCartAdapter;
import com.hxqc.mall.thirdshop.accessory.model.ConfirmOrderItem;
import com.hxqc.mall.thirdshop.accessory.model.ShoppingCart;
import com.hxqc.mall.thirdshop.accessory.utils.ActivitySwitcherAccessory;
import com.hxqc.mall.thirdshop.accessory4s.utils.ActivitySwitcherAccessory4S;
import com.hxqc.util.JSONUtils;

import cz.msebera.android.httpclient.Header;

import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * 说明:购物车
 *
 * @author: 吕飞
 * @since: 2016-02-19
 * Copyright:恒信汽车电子商务有限公司
 */
public class ShoppingCartActivity extends AccessoryNoBackActivity implements View.OnClickListener, OnRefreshHandler {
    public static boolean sNeedRefresh = false;
    public TextView mToBillView;//结算按钮
    public CheckBox mSelectAllView;//全选
    public TextView mAmountView;//总额
    public Button mDeleteView;//删除按钮
    public Button mCollectView;//移入关注按钮
    public ShoppingCart mShoppingCart;//购物车数据
    protected PtrFrameLayout mPtrFrameLayoutView;
    protected UltraPullRefreshHeaderHelper mPtrHelper;
    ConfirmOrderItem mConfirmOrderItem;//去支付时需要的数据
    Toolbar mToolbar;
    TextView mMenuView;//右上角菜单
    ListView mListView;//主体列表
    TextView mAmountTextView;//总额
    RelativeLayout mBottomBarView;//底部栏
    RequestFailView mFailView;
    ShoppingCartAdapter mShoppingCartAdapter;//购物车适配器
    boolean mEdit;//是否在编辑状态

    @Override
    public void onResume() {
        super.onResume();
        if (sNeedRefresh) {
            getData();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);
        initView();
        getData();
    }

    private void getData() {
        sNeedRefresh = false;
        mAccessoryApiClient.getAccessoryShoppingCart(new LoadingAnimResponseHandler(this) {
            @Override
            public void onSuccess(String response) {
                mShoppingCart = JSONUtils.fromJson(response, ShoppingCart.class);
                if (mShoppingCart != null) {
                    mShoppingCart.initProductInfos();
                    if (!mShoppingCart.isNull()) {
                        fillData();
                    } else {
                        showNoData();
                    }
                } else {
                    showNoData();
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                mPtrFrameLayoutView.refreshComplete();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                showFailView();
            }
        });
    }

    private void fillData() {
        mFailView.setVisibility(View.GONE);
        mSelectAllView.setChecked(false);
        initMenu();
        initBottom();
        mShoppingCartAdapter = new ShoppingCartAdapter(this, mShoppingCart.mProductInfos);
        mListView.setAdapter(mShoppingCartAdapter);
    }

    //初始化底部栏
    public void initBottom() {
        mToBillView.setEnabled(!mShoppingCart.isAllUnSelected());
        mDeleteView.setEnabled(!mShoppingCart.isAllUnSelected());
//        mCollectView.setEnabled(!mShoppingCart.isAllUnSelected());
        OtherUtil.setVisible(mToBillView, !mEdit);
        OtherUtil.setVisible(mDeleteView, mEdit);
//        OtherUtil.setVisible(mCollectView, mEdit);
        OtherUtil.setVisible(mBottomBarView, !mShoppingCart.isAllInvalid());
        if (mShoppingCart.count > 99) {
            mToBillView.setText("去结算(99+)");
        } else {
            mToBillView.setText("去结算(" + mShoppingCart.count + ")");
        }
        mAmountView.setText(OtherUtil.amountFormat(mShoppingCart.amount, true));
    }

    //初始化菜单
    public void initMenu() {
        OtherUtil.setVisible(mMenuView, !mShoppingCart.isAllInvalid());
        if (mEdit) {
            mMenuView.setText("完成");
        } else {
            mMenuView.setText("编辑");
        }
    }

    private void initView() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle(R.string.accessory_title_activity_shopping_cart);
        mToolbar.setTitleTextColor(Color.WHITE);
        mToolbar.setNavigationIcon(R.drawable.ic_back);
        setSupportActionBar(mToolbar);
        mPtrFrameLayoutView = (PtrFrameLayout) findViewById(R.id.sub_message_ptr);
        mPtrHelper = new UltraPullRefreshHeaderHelper(this, mPtrFrameLayoutView);
        mPtrHelper.setOnRefreshHandler(this);
        mMenuView = (TextView) findViewById(R.id.menu);
        mListView = (ListView) findViewById(R.id.list_view);
        mToBillView = (TextView) findViewById(R.id.to_bill);
        mSelectAllView = (CheckBox) findViewById(R.id.select_all);
        mAmountView = (TextView) findViewById(R.id.amount);
        mAmountTextView = (TextView) findViewById(R.id.amount_text);
        mDeleteView = (Button) findViewById(R.id.delete);
//        mCollectView = (Button) findViewById(R.id.collect);
        mBottomBarView = (RelativeLayout) findViewById(R.id.bottom_bar);
        mFailView = (RequestFailView) findViewById(R.id.fail_view);
        mMenuView.setOnClickListener(this);
        mToBillView.setOnClickListener(this);
        mSelectAllView.setOnClickListener(this);
        mDeleteView.setOnClickListener(this);
//        mCollectView.setOnClickListener(this);
    }

    //空白数据
    public void showNoData() {
        mMenuView.setVisibility(View.GONE);
        mFailView.setVisibility(View.VISIBLE);
        mFailView.setEmptyDescription("您的购物车是空的");
        mFailView.setEmptyButtonClick("随便逛逛", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                ActivitySwitcherAccessory4S.toAccessorySaleFromHome(ShoppingCartActivity.this);
            }
        });
        mFailView.setRequestViewType(RequestFailView.RequestViewType.empty);
    }

    private void showFailView() {
        mFailView.setVisibility(View.VISIBLE);
        mFailView.setEmptyDescription("网络连接失败");
        mFailView.setFailButtonClick("重新加载", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData();
            }
        });
        mFailView.setRequestViewType(RequestFailView.RequestViewType.fail);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.menu) {
            if (mMenuView.getText().equals("编辑")) {
                mMenuView.setText("完成");
                mEdit = true;
                mShoppingCart.setEditStatus(true);
                mShoppingCartAdapter.notifyDataSetChanged();
                initBottom();
            } else {
                mMenuView.setClickable(false);
                mAccessoryApiClient.editShoppingCart(mShoppingCart.getIDNumList()[0], mShoppingCart.getIDNumList()[1], mShoppingCart.getIDNumList()[2], mShoppingCart.getIDNumList()[3], new LoadingAnimResponseHandler(this) {
                    @Override
                    public void onFinish() {
                        super.onFinish();
                        mMenuView.setClickable(true);
                    }

                    @Override
                    public void onSuccess(String response) {
                        mMenuView.setText("编辑");
                        mEdit = false;
                        mShoppingCart.setEditStatus(false);
                        mShoppingCart.selectAll(false);
                        mSelectAllView.setChecked(false);
                        mShoppingCartAdapter.notifyDataSetChanged();
                        mShoppingCart.setCountAmount();
                        initBottom();
                    }
                });
            }
        } else if (i == R.id.to_bill) {
            mToBillView.setClickable(false);
            mAccessoryApiClient.goToCheckout(mShoppingCart.getSubmitJson(), mShoppingCart.amount + "", mShoppingCart.count + "", new LoadingAnimResponseHandler(this) {
                @Override
                public void onFinish() {
                    super.onFinish();
                    mToBillView.setClickable(true);
                }

                @Override
                public void onSuccess(String response) {
                    mConfirmOrderItem= JSONUtils.fromJson(response,ConfirmOrderItem.class);
                    if (mConfirmOrderItem != null) {
                        ActivitySwitcherAccessory.toConfirmOrder(ShoppingCartActivity.this, mConfirmOrderItem);
                    } else {
                        ToastHelper.showRedToast(ShoppingCartActivity.this, "抱歉，您的订单提交失败");
                    }
                }
            });
        } else if (i == R.id.delete) {
            mDeleteView.setClickable(false);
            new NormalDialog(this, "您确定要删除已选商品吗？") {
                @Override
                protected void doNext() {
                    mAccessoryApiClient.deleteFromShoppingCart(mShoppingCart.getSelectProductIDList(), mShoppingCart.getSelectedPackageIDList(), new LoadingAnimResponseHandler(ShoppingCartActivity.this) {
                        @Override
                        public void onFinish() {
                            super.onFinish();
                            mDeleteView.setClickable(true);
                        }

                        @Override
                        public void onSuccess(String response) {
                            deleteItem();
                        }
                    });
                }
            }.show();
        } else if (i == R.id.collect) {
            mDeleteView.setClickable(false);
            mAccessoryApiClient.collect(1, mShoppingCart.getSelectProductIDList(), mShoppingCart.getSelectedPackageIDList(), 1, new LoadingAnimResponseHandler(this) {
                @Override
                public void onFinish() {
                    super.onFinish();
                    mDeleteView.setClickable(true);
                }

                @Override
                public void onSuccess(String response) {
                    deleteItem();
                }
            });
        } else if (i == R.id.select_all) {
            mShoppingCart.selectAll(mSelectAllView.isChecked());
            mShoppingCartAdapter.notifyDataSetChanged();
            initBottom();
        }
    }

    //删除项目
    private void deleteItem() {
        mShoppingCart.deleteItem();
        initMenu();
        checkNull();
        mShoppingCart.setCountAmount();
        initBottom();
        mShoppingCartAdapter.notifyDataSetChanged();
    }

    //判断是否为空数据
    public void checkNull() {
        if (mShoppingCart.isNull()) {
            showNoData();
        }
    }

    @Override
    public boolean hasMore() {
        return false;
    }

    @Override
    public void onRefresh() {
        if (mEdit) {
            mPtrFrameLayoutView.refreshComplete();
        } else {
            getData();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onLoadMore() {
        mPtrFrameLayoutView.refreshComplete();
    }

}

