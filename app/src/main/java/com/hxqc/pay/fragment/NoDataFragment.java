package com.hxqc.pay.fragment;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hxqc.mall.core.api.RequestFailViewUtil;
import com.hxqc.mall.core.fragment.FunctionAppCompatFragment;

import hxqc.mall.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class NoDataFragment extends FunctionAppCompatFragment {


    public NoDataFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

//        return inflater.inflate(R.layout.fragment_no_data, container, false);
        return new RequestFailViewUtil().getEmptyView(getActivity(),"未获取到订单数据");
    }


    @Override
    public String fragmentDescription() {
        return getResources().getString(R.string.fragment_description_no_data);
    }
}
