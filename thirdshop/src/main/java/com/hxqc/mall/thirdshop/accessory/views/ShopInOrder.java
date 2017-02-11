package com.hxqc.mall.thirdshop.accessory.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.accessory.adapter.ProductInOrderAdapter;
import com.hxqc.mall.thirdshop.accessory.model.ProductInfo;
import com.hxqc.mall.thirdshop.accessory.model.ShoppingCartItem;
import com.hxqc.widget.ListViewNoSlide;

import java.util.ArrayList;


/**
 * 说明:订单中的一个店铺商品列表
 *
 * @author: 吕飞
 * @since: 2016-02-26
 * Copyright:恒信汽车电子商务有限公司
 */
public class ShopInOrder extends LinearLayout {
    ListViewNoSlide mProductListView;//商品列表
    boolean mToProductDetail;//点击商品，是否跳转到商品详情

    public ShopInOrder(Context context) {
        super(context);
        initView();
    }

    public ShopInOrder(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.ShopInOrder);
        mToProductDetail = typedArray.getBoolean(R.styleable.ShopInOrder_toProductDetail, false);
        typedArray.recycle();
    }

    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_shop_in_order, this);
        mProductListView = (ListViewNoSlide) findViewById(R.id.product_list);
    }

    public void fillData(ArrayList<ShoppingCartItem> itemList, boolean mToProductDetail) {
        mProductListView.setAdapter(new ProductInOrderAdapter(getContext(), getProductInOrders(itemList), mToProductDetail));
    }

    private ArrayList<ProductInfo> getProductInOrders(ArrayList<ShoppingCartItem> itemList) {
        ArrayList<ProductInfo> productInfos = new ArrayList<>();
        for (int i = 0; i < itemList.size(); i++) {
            productInfos.addAll(itemList.get(i).getProductInfos());
        }
        return productInfos;
    }
}
