package com.hxqc.mall.thirdshop.accessory.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.hxqc.mall.core.api.LoadingAnimResponseHandler;
import com.hxqc.mall.core.controler.UserInfoHelper;
import com.hxqc.mall.core.util.ActivitySwitchBase;
import com.hxqc.mall.core.util.ToastHelper;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.accessory.adapter.DialogPackageProductAdapter;
import com.hxqc.mall.thirdshop.accessory.adapter.PackageListAdapter;
import com.hxqc.mall.thirdshop.accessory.model.SinglePackage;
import com.hxqc.mall.thirdshop.accessory.model.SinglePackageProduct;
import com.hxqc.mall.thirdshop.accessory.model.SinglePackageProducts;
import com.hxqc.mall.thirdshop.accessory.utils.ActivitySwitcherAccessory;

import cz.msebera.android.httpclient.Header;

import java.util.ArrayList;

/**
 * 优惠套餐列表
 * Created by huangyi on 15/12/9.
 */
public class PackageListActivity extends AccessoryBackActivity implements PackageListAdapter.OnClickListener,
        View.OnClickListener, AdapterView.OnItemClickListener {

    public final static String PACKAGE_LIST = "package_list";

    ListView mListView;
    ArrayList<SinglePackage> mPackageList; //数据源
    PackageListAdapter mAdapter;

    Dialog mDialog;
    TextView mDialogNameView;
    ListView mDialogListView;
    DialogPackageProductAdapter mDialogAdapter;
    /**
     * 优惠套餐列表index, 单个优惠套餐  同名商品组index
     **/
    int mPackageListIndex, mSinglePackageIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acc_package_list);

        mPackageList = getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA).getParcelableArrayList(PACKAGE_LIST);
        //isChecked 初始化
        for (SinglePackage singlePackage : mPackageList) {
            for (SinglePackageProducts products : singlePackage.productList) {
                for (SinglePackageProduct product : products.products) {
                    if (product.productInfo.productID.trim().equals(products.productInfo.productID.trim())) {
                        product.isChecked = true;
                    }
                }
            }
        }
        mListView = (ListView) findViewById(R.id.package_list);
        mAdapter = new PackageListAdapter(this, mPackageList, this);
        mListView.setAdapter(mAdapter);
    }

    @Override
    public void onClickForDetail(View view, int position1, int position2) {
        for (SinglePackageProduct product : mPackageList.get(position1).productList.get(position2).products) {
            if (product.isChecked) {
                ActivitySwitcherAccessory.toProductDetail(this, product.productInfo.productID);
                break;
            }
        }
    }

    @Override
    public void onClickForArrow(View view, int position1, int position2) {
        mPackageListIndex = position1;
        mSinglePackageIndex = position2;
        showDialog();
    }

    @Override
    public void onClickForCar(View view, final int position) {
        UserInfoHelper.getInstance().loginAction(this, 50, new UserInfoHelper.OnLoginListener() {
            @Override
            public void onLoginSuccess() {
                SinglePackage singlePackage = mPackageList.get(position);
                StringBuilder productIDList = new StringBuilder();
                for (SinglePackageProducts products : singlePackage.productList) {
                    for (SinglePackageProduct product : products.products) {
                        if (product.isChecked) {
                            productIDList.append(product.productInfo.productID).append(",");
                        }
                    }
                }
                addCar(productIDList.toString().substring(0, productIDList.length() - 1), singlePackage.packageID);
            }
        });
    }

    private void addCar(String productIDList, String packageID) {
        mAccessoryApiClient.addShoppingCart("1", "", productIDList, packageID, new LoadingAnimResponseHandler(this, true, false) {
            @Override
            public void onSuccess(String response) {
                ToastHelper.showGreenToast(PackageListActivity.this, "加入购物车成功");
                ShoppingCartActivity.sNeedRefresh = true;
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                ToastHelper.showRedToast(PackageListActivity.this, "加入购物车失败");
            }
        });
    }

    private void showDialog() {
        if (null == mDialog) {
            View mDialogView = LayoutInflater.from(this).inflate(R.layout.dialog_package_product, null);
            mDialogNameView = (TextView) mDialogView.findViewById(R.id.package_product_name);
            mDialogListView = (ListView) mDialogView.findViewById(R.id.package_product_list);
            mDialogListView.setOnItemClickListener(this);
            mDialogView.findViewById(R.id.package_product_close).setOnClickListener(this); //关闭
            mDialogView.findViewById(R.id.package_product_ok).setOnClickListener(this); //确定

            mDialog = new Dialog(this, R.style.FullWidthDialog);
            mDialog.setContentView(mDialogView);
            mDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT); //设置dialog宽高
            mDialog.getWindow().setGravity(Gravity.BOTTOM); //设置dialog显示的位置
        }

        //isTempChecked 重置
        SinglePackageProducts products = mPackageList.get(mPackageListIndex).productList.get(mSinglePackageIndex);
        int temp = -1;
        for (int i = 0; i < products.products.size(); i++) {
            if (products.products.get(i).isChecked) {
                temp = i;
            }
            products.products.get(i).isTempChecked = false;
        }
        products.products.get(temp).isTempChecked = true;

        mDialogAdapter = new DialogPackageProductAdapter(this, products);
        mDialogListView.setAdapter(mDialogAdapter);
        mDialogNameView.setText(mPackageList.get(mPackageListIndex).packageName);

        mDialog.show();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.package_product_close && null != mDialog) {
            mDialog.dismiss();

        } else if (id == R.id.package_product_ok) {
            if (null != mDialog) mDialog.dismiss();
            //isChecked 重置
            SinglePackageProducts products = mPackageList.get(mPackageListIndex).productList.get(mSinglePackageIndex);
            int temp = -1;
            for (int i = 0; i < products.products.size(); i++) {
                if (products.products.get(i).isTempChecked) {
                    temp = i;
                }
                products.products.get(i).isChecked = false;
            }
            products.products.get(temp).isChecked = true;
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //isTempChecked 重置
        SinglePackageProducts products = mPackageList.get(mPackageListIndex).productList.get(mSinglePackageIndex);
        for (SinglePackageProduct product : products.products) {
            product.isTempChecked = false;
        }
        products.products.get(position).isTempChecked = true;
        mDialogAdapter.notifyDataSetChanged();
    }

}
