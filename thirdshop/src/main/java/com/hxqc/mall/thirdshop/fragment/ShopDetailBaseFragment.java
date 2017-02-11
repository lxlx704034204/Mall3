package com.hxqc.mall.thirdshop.fragment;

import android.content.Context;

import com.hxqc.mall.core.fragment.FunctionFragment;
import com.hxqc.mall.thirdshop.api.ThirdPartShopClient;

/**
 * Author:胡俊杰
 * Date: 2015/12/10
 * FIXME
 * Todo
 */
public class ShopDetailBaseFragment extends FunctionFragment {
    ThirdPartShopClient ThirdPartShopClient;
    OnShopDetailListener mShopDetailListener;

    public interface OnShopDetailListener {
        String getShopID();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mShopDetailListener = (OnShopDetailListener) context;
        ThirdPartShopClient =new ThirdPartShopClient();
    }

    @Override
    public String fragmentDescription() {
        return null;
    }
}
