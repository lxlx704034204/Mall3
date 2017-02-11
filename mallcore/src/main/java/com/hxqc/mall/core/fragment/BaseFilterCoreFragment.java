package com.hxqc.mall.core.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.hxqc.mall.core.R;
import com.hxqc.mall.core.util.ToastHelper;
import com.hxqc.mall.core.views.FilterFactorView;

/**
 * Function: 筛选Fragment父类
 *
 * @author 袁秉勇
 * @since 2016年02月18日
 */
public abstract class BaseFilterCoreFragment extends FunctionFragment implements View.OnClickListener {
    private final static String TAG = BaseFilterCoreFragment.class.getSimpleName();
    private Context mContext;

    protected FilterMenuListener filterMenuListener;

    public FilterFactorView mBrandView;//品牌
    public FilterFactorView mSeriesView;//车系
    public FilterFactorView mAutoModelView;//车型
    public FilterFactorView mGoodsCategoryView;//品类
    protected BaseFilterCoreFragment.CallBack callBack;

    protected View.OnClickListener seriesViewOnClickListener;
//    protected View.OnClickListener modeViewOnclickListener;
//    protected View.OnClickListener goodsCategoryViewOnClickListener;


    /** 初始化fragment的view **/
    public abstract View initView(LayoutInflater inflater, ViewGroup container);


    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;

        filterMenuListener = (FilterMenuListener) context;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return initView(inflater, container);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBrandView = (FilterFactorView) view.findViewById(R.id.filter_brand);
        mSeriesView = (FilterFactorView) view.findViewById(R.id.filter_series);

        mGoodsCategoryView = (FilterFactorView) view.findViewById(R.id.filter_goods_category);

        seriesViewOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastHelper.showRedToast(getContext(), "请先选择品牌");
            }
        };

//        modeViewOnclickListener = new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ToastHelper.showRedToast(getContext(), "请先选择车系");
//            }
//        };
//
//        goodsCategoryViewOnClickListener = new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ToastHelper.showRedToast(getContext(), "请先选择车系");
//            }
//        };

        mBrandView.setOnClickListener(this);
        setListener(false);
        if( mGoodsCategoryView != null) mGoodsCategoryView.setOnClickListener(this);
    }


    public void setListener(boolean showChooseTip) {
        if(showChooseTip) {
            mSeriesView.setOnClickListener(this);
        } else {
            mSeriesView.setOnClickListener(seriesViewOnClickListener);
        }
    }


    @Override
    public String fragmentDescription() {
        return "筛选Fragment基类";
    }


    public interface CallBack {
        void baseFilterCoreFragmentCallBack(String key, String value);
    }


    public interface FilterMenuListener {
        void showFilterFactor(Fragment fragment);

        void closeFilterFactor();
    }
}
