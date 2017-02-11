package com.hxqc.mall.thirdshop.accessory.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hxqc.mall.core.util.ImageUtil;
import com.hxqc.mall.core.util.OtherUtil;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.accessory.activity.ShoppingCartActivity;
import com.hxqc.mall.thirdshop.accessory.adapter.ShoppingCartAdapter;
import com.hxqc.mall.thirdshop.accessory.model.ProductInfo;
import com.hxqc.mall.thirdshop.accessory4s.utils.ActivitySwitcherAccessory4S;
import com.hxqc.widget.SquareImageView;

import java.util.ArrayList;

/**
 * 说明:有效购物车
 *
 * @author: 吕飞
 * @since: 2016-02-22
 * Copyright:恒信汽车电子商务有限公司
 */
public class ShoppingCartValid extends LinearLayout implements View.OnClickListener {
    RelativeLayout mHeadView;//套餐头部
    CheckBox mSelectPackageView;//套餐选择
    TextView mPackagePriceView;//套餐价格
    TextView mPackageNumView;//套餐数量
    ImageView mAddPackageView;//套餐+
    TextView mEditPackageNumView;//套餐数量编辑
    ImageView mMinusPackageView;//套餐-
    CheckBox mSelectProductView;//商品选择
    RelativeLayout mProductInfoView;//商品信息
    SquareImageView mPhotoView;//商品图片
    TextView mNameView;//商品名字
    TextView mProductNumView;//商品数目
    TextView mProductPriceView;//商品价格
    ImageView mMinusProductView;//商品-
    TextView mEditProductNumView;//商品数量编辑
    ImageView mAddProductView;//商品+
    ProductInfo mProductInfo;
    ArrayList<ProductInfo> mProductInfos;
    View mBottomLineView;//底部线
    View mBottomBlankView;//底部空白
    int mPosition;
    ShoppingCartAdapter mSuperShoppingCartAdapter;

    public ShoppingCartValid(Context context) {
        super(context);
    }

    public ShoppingCartValid(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.view_shopping_cart_valid, this);
        mHeadView = (RelativeLayout) findViewById(R.id.head);
        mBottomLineView = findViewById(R.id.bottom_line);
        mSelectPackageView = (CheckBox) findViewById(R.id.select_package);
        mPackagePriceView = (TextView) findViewById(R.id.package_price);
        mPackageNumView = (TextView) findViewById(R.id.package_num);
        mAddPackageView = (ImageView) findViewById(R.id.add_package);
        mEditPackageNumView = (TextView) findViewById(R.id.edit_package_num);
        mMinusPackageView = (ImageView) findViewById(R.id.minus_package);
        mSelectProductView = (CheckBox) findViewById(R.id.select_product);
        mProductInfoView = (RelativeLayout) findViewById(R.id.product_info);
        mPhotoView = (SquareImageView) findViewById(R.id.photo);
        mNameView = (TextView) findViewById(R.id.name);
        mProductNumView = (TextView) findViewById(R.id.product_num);
        mProductPriceView = (TextView) findViewById(R.id.product_price);
        mMinusProductView = (ImageView) findViewById(R.id.minus_product);
        mEditProductNumView = (TextView) findViewById(R.id.edit_product_num);
        mAddProductView = (ImageView) findViewById(R.id.add_product);
        mBottomBlankView = findViewById(R.id.bottom_blank);
        mSelectPackageView.setOnClickListener(this);
        mAddPackageView.setOnClickListener(this);
        mMinusPackageView.setOnClickListener(this);
        mSelectProductView.setOnClickListener(this);
        mProductInfoView.setOnClickListener(this);
        mMinusProductView.setOnClickListener(this);
        mAddProductView.setOnClickListener(this);
    }

    public void fillData(ArrayList<ProductInfo> productInfos, int position, ShoppingCartAdapter superShoppingCartAdapter) {
        mProductInfo = productInfos.get(position);
        mProductInfos = productInfos;
        mPosition = position;
        mSuperShoppingCartAdapter = superShoppingCartAdapter;
        initView();
    }

    private void initView() {
        OtherUtil.setVisible(mBottomLineView, mProductInfo.isLastInPackage || !mProductInfo.isInPackage);
        OtherUtil.setVisible(mHeadView, mProductInfo.isFirstInPackage);
        OtherUtil.setVisible(mSelectPackageView, mProductInfo.isFirstInPackage);
        OtherUtil.setVisible(mSelectProductView, !mProductInfo.isInPackage);
        OtherUtil.setVisible(mBottomBlankView, !mProductInfo.isInPackage || mProductInfo.isLastInPackage);
        mPackagePriceView.setText(OtherUtil.amountFormat(mProductInfo.comboPrice, true));
        mPackageNumView.setText(mProductInfo.getPackageNum());
        OtherUtil.setVisible(mAddPackageView, mProductInfo.isEdit);
        OtherUtil.setVisible(mEditPackageNumView, mProductInfo.isEdit);
        mEditPackageNumView.setText(mProductInfo.packageNum);
        OtherUtil.setVisible(mMinusPackageView, mProductInfo.isEdit);
        ImageUtil.setImageSquare(getContext(), mPhotoView, mProductInfo.smallPhoto);
        OtherUtil.setVisible(mProductPriceView, !mProductInfo.isEdit);
        OtherUtil.setVisible(mProductNumView, !mProductInfo.isEdit);
        mNameView.setText(mProductInfo.name);
        mProductNumView.setText(mProductInfo.getProductNum());
        mProductPriceView.setText(OtherUtil.amountFormat(mProductInfo.price, true));
        OtherUtil.setVisible(mPackagePriceView, mProductInfo.isInPackage && !mProductInfo.isEdit);
        OtherUtil.setVisible(mPackageNumView, mProductInfo.isInPackage && !mProductInfo.isEdit);
        OtherUtil.setVisible(mMinusProductView, mProductInfo.isEdit && !mProductInfo.isInPackage);
        OtherUtil.setVisible(mEditProductNumView, mProductInfo.isEdit && !mProductInfo.isInPackage);
        mEditProductNumView.setText(mProductInfo.productNum);
        OtherUtil.setVisible(mAddProductView, mProductInfo.isEdit && !mProductInfo.isInPackage);
        mSelectPackageView.setChecked(mProductInfo.isSelected);
        mSelectProductView.setChecked(mProductInfo.isSelected);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.select_package) {
            for (int j = mPosition; j < mProductInfos.size(); j++) {
                mProductInfos.get(j).isSelected = mSelectPackageView.isChecked();
                if (mProductInfos.get(j).isLastInPackage) {
                    break;
                }
            }
            updateView();
        } else if (i == R.id.select_product) {
            mProductInfo.isSelected = mSelectProductView.isChecked();
            updateView();
        } else if (i == R.id.add_package) {
            if (Integer.parseInt(mProductInfo.packageNum) < 99) {
                mProductInfo.packageNum = Integer.parseInt(mProductInfo.packageNum) + 1 + "";
                mEditPackageNumView.setText(mProductInfo.packageNum);
            }
        } else if (i == R.id.minus_package) {
            if (Integer.parseInt(mProductInfo.packageNum) > 1) {
                mProductInfo.packageNum = Integer.parseInt(mProductInfo.packageNum) - 1 + "";
                mEditPackageNumView.setText(mProductInfo.packageNum);
            }
        } else if (i == R.id.add_product) {
            if (Integer.parseInt(mProductInfo.productNum) < 99) {
                mProductInfo.productNum = Integer.parseInt(mProductInfo.productNum) + 1 + "";
                mEditProductNumView.setText(mProductInfo.productNum);
            }
        } else if (i == R.id.minus_product) {
            if (Integer.parseInt(mProductInfo.productNum) > 1) {
                mProductInfo.productNum = Integer.parseInt(mProductInfo.productNum) - 1 + "";
                mEditProductNumView.setText(mProductInfo.productNum);
            }
        } else if (i == R.id.product_info) {
            if (!mProductInfo.isEdit) {
                ActivitySwitcherAccessory4S.toProductDetail(getContext(), mProductInfo.productID, "");
            }
        }
    }

    private void updateView() {
        updateBottom();
        mSuperShoppingCartAdapter.notifyDataSetChanged();
        ((ShoppingCartActivity) getContext()).mSelectAllView.setChecked(((ShoppingCartActivity) getContext()).mShoppingCart.isAllSelected());
    }

    private void updateBottom() {
        ((ShoppingCartActivity) getContext()).mShoppingCart.setCountAmount();
        ((ShoppingCartActivity) getContext()).initBottom();
    }
//
//    @Override
//    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//    }
//
//    @Override
//    public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//    }
//
//    @Override
//    public void afterTextChanged(Editable s) {
//        if (mProductInfo.isEdit) {
//            if (mProductInfo.isFirstInPackage) {
//                for (int i = mPosition; i < mProductInfos.size(); i++) {
//                    if (TextUtils.isEmpty(mEditPackageNumView.getText().toString())) {
//                        mProductInfos.get(i).packageNum = "1";
//                    } else {
//                        mProductInfos.get(i).packageNum = mEditPackageNumView.getText().toString();
//                    }
//                    if (mProductInfos.get(i).isLastInPackage) {
//                        break;
//                    }
//                }
//
//            } else if (!mProductInfo.isInPackage) {
//                if (TextUtils.isEmpty(mEditProductNumView.getText().toString())) {
//                    mProductInfo.productNum = "1";
//                } else {
//                    mProductInfo.productNum = mEditProductNumView.getText().toString();
//                }
//            }
//        }
//    }
}
