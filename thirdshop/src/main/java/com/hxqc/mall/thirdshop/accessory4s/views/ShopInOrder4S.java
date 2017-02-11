package com.hxqc.mall.thirdshop.accessory4s.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.accessory4s.adapter.ProductInOrder4SAdapter;
import com.hxqc.mall.thirdshop.accessory4s.model.ProductIn4S;
import com.hxqc.mall.thirdshop.accessory4s.model.ProductInfo4S;
import com.hxqc.widget.ListViewNoSlide;

import java.util.ArrayList;


/**
 * 说明:订单中的一个店铺商品列表
 *
 * @author: 吕飞
 * @since: 2016-02-26
 * Copyright:恒信汽车电子商务有限公司
 */
public class ShopInOrder4S extends LinearLayout {
    ListViewNoSlide mProductListView;//商品列表
    boolean mToProductDetail;//点击商品，是否跳转到商品详情

    public ShopInOrder4S(Context context) {
        super(context);
        initView();
    }

    public ShopInOrder4S(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.ShopInOrder4S);
        mToProductDetail = typedArray.getBoolean(R.styleable.ShopInOrder4S_toProductDetail4s, false);
        typedArray.recycle();
    }

    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_shop_in_order, this);
        mProductListView = (ListViewNoSlide) findViewById(R.id.product_list);
    }

    public void fillData(ArrayList<ProductIn4S> itemList, boolean mToProductDetail) {
        mProductListView.setAdapter(new ProductInOrder4SAdapter(getContext(), getProductInOrders(itemList), mToProductDetail));
    }

    private ArrayList<ProductInfo4S> getProductInOrders(ArrayList<ProductIn4S> itemList) {
        ArrayList<ProductInfo4S> productInfos = new ArrayList<>();
        for (int i = 0; i < itemList.size(); i++) {
            productInfos.addAll(itemList.get(i).getProductInfos());
        }
        return productInfos;
    }
}
