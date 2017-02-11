package com.hxqc.mall.usedcar.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hxqc.mall.core.fragment.FunctionFragment;
import com.hxqc.mall.usedcar.R;
import com.hxqc.mall.usedcar.model.AutoModel;

import java.util.ArrayList;

/**
 * 说明:选择品牌车型车系的侧滑菜单
 *
 * @author: 吕飞
 * @since: 2015-09-18
 * Copyright:恒信汽车电子商务有限公司
 */
public class ChooseFragment extends FunctionFragment implements ChooseBrandFragment.ChooseBrandListener, ChooseSeriesFragment.ChooseSeriesListener, ChooseModelFragment.ChooseModelListener, ChooseModelFragment.ChooseValuationModelListener, ChooseModelFragment.ChooseIntentModelListener, View.OnClickListener {
    public static final int CHOOSE_BRAND_FRAGMENT = 1;
    public static final int CHOOSE_SERIES_FRAGMENT = 2;
    public static final int CHOOSE_MODEL_FRAGMENT = 3;
    public static final int COMMON = 0;
    public static final int VALUATION = 1;
    public static final int INTENT = 2;
    TextView mTitleView;
    ImageView mBackView;
    ChooseBrandFragment mChooseBrandFragment;
    ChooseSeriesFragment mChooseSeriesFragment;
    ChooseModelFragment mChooseModelFragment;
    int mFragment;
    boolean mShowModel = true;
    ChooseListener mListener;
    String mBrand;
    String mSeries;
    String mModel;
    String mBrandId;
    String mSeriesId;
    String mModelId;
    String mValuationModelId;
    String mBrandIcon;
    ArrayList<AutoModel> mAutoModels;
    int mType = 0;
    ChooseValuationListener mValuationListener;
    ChooseIntentListener mIntentListener;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_choose, container, false);
        mTitleView = (TextView) rootView.findViewById(R.id.title);
        mBackView = (ImageView) rootView.findViewById(R.id.back);
        mBackView.setOnClickListener(this);
        initFragment();
        initBrandFragment();
        return rootView;
    }

    private void initFragment() {
        mChooseBrandFragment = new ChooseBrandFragment();
        mChooseBrandFragment.setType(mType);
        mChooseBrandFragment.setListener(this);
        mChooseSeriesFragment = new ChooseSeriesFragment();
        mChooseSeriesFragment.setType(mType);
        mChooseSeriesFragment.setListener(this);
        mChooseModelFragment = new ChooseModelFragment();
        mChooseModelFragment.setType(mType);
        if (mType == VALUATION) {
            mChooseModelFragment.setValuationListener(this);
        } else if (mType == INTENT) {
            mChooseModelFragment.setIntentListener(this);
        } else {
            mChooseModelFragment.setListener(this);
        }
    }

    private void initBrandFragment() {
        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.fragment_container, mChooseBrandFragment);
        fragmentTransaction.commit();
        mBackView.setVisibility(View.INVISIBLE);
        mTitleView.setText(getResources().getString(R.string.brand_navigation));
        mFragment = CHOOSE_BRAND_FRAGMENT;
    }

    public void setType(int type) {
        mType = type;
    }

    public void showModel(boolean showModel) {
        this.mShowModel = showModel;
    }

    @Override
    public String fragmentDescription() {
        return "选择品牌车型车系的侧滑菜单";
    }

    @Override
    public void completeBrandChoose(String brand, String brandId, String brandIcon) {
        this.mBrand = brand;
        this.mBrandId = brandId;
        this.mBrandIcon = brandIcon;
        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.fragment_right_in, R.anim.fragment_left_out);
        if (mChooseSeriesFragment.isAdded()) {
            fragmentTransaction.show(mChooseSeriesFragment);
            mChooseSeriesFragment.initSeries(brand, brandId, brandIcon);
        } else {
            fragmentTransaction.add(R.id.fragment_container, mChooseSeriesFragment);
        }
        fragmentTransaction.hide(mChooseBrandFragment).commit();
        mFragment = CHOOSE_SERIES_FRAGMENT;
        mBackView.setVisibility(View.VISIBLE);
        mTitleView.setText(getResources().getString(R.string.choose_series));
    }

    @Override
    public void completeChooseSeries(String seriesName, String seriesId, ArrayList<AutoModel> autoModels) {
        this.mSeries = seriesName;
        this.mSeriesId = seriesId;
        this.mAutoModels = autoModels;
        if (!mShowModel) {
            mListener.completeChoose(mBrand, mSeries, "", mBrandId, mSeriesId, "");
        } else {
            FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.fragment_right_in, R.anim.fragment_left_out);
            if (mChooseModelFragment.isAdded()) {
                fragmentTransaction.show(mChooseModelFragment);
                mChooseModelFragment.initModel(seriesName, seriesId, autoModels);
            } else {
                fragmentTransaction.add(R.id.fragment_container, mChooseModelFragment);
            }
            fragmentTransaction.hide(mChooseSeriesFragment).commit();
            mFragment = CHOOSE_MODEL_FRAGMENT;
            mBackView.setVisibility(View.VISIBLE);
            mTitleView.setText(getResources().getString(R.string.choose_model));
        }
    }

    @Override
    public void completeChooseModel(String model, String modelId) {
        this.mModel = model;
        this.mModelId = modelId;
        mListener.completeChoose(mBrand, mSeries, mModel, mBrandId, mSeriesId, mModelId);
    }

    @Override
    public void onClick(View v) {
        if (mFragment == CHOOSE_BRAND_FRAGMENT) {
            mListener.completeChoose("", "", "", "", "", "");
        } else if (mFragment == CHOOSE_SERIES_FRAGMENT) {
            FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.fragment_left_in, R.anim.fragment_right_out);
            fragmentTransaction.show(mChooseBrandFragment);
            fragmentTransaction.hide(mChooseSeriesFragment).commit();
            mFragment = CHOOSE_BRAND_FRAGMENT;
            mBackView.setVisibility(View.INVISIBLE);
            mTitleView.setText(getResources().getString(R.string.brand_navigation));
        } else if (mFragment == CHOOSE_MODEL_FRAGMENT) {
            FragmentTransaction fragmentTransaction2 = getChildFragmentManager().beginTransaction();
            fragmentTransaction2.setCustomAnimations(R.anim.fragment_left_in, R.anim.fragment_right_out);
            fragmentTransaction2.show(mChooseSeriesFragment);
            fragmentTransaction2.hide(mChooseModelFragment).commit();
            mFragment = CHOOSE_SERIES_FRAGMENT;
            mBackView.setVisibility(View.VISIBLE);
            mTitleView.setText(getResources().getString(R.string.choose_series));
        }
    }

    @Override
    public void completeValuationChooseModel(String model, String modelId, String valuationModelId) {
        mModel = model;
        mModelId = modelId;
        mValuationModelId = valuationModelId;
        mValuationListener.completeValuationChoose(mBrand, mSeries, mModel, mBrandId, mSeriesId, mModelId, mValuationModelId);
    }

    @Override
    public void completeIntentChooseModel(String model) {
        mModel = model;
        mIntentListener.completeIntentChoose(mBrand, mSeries, mModel);
    }

    public void setListener(ChooseListener mListener) {
        this.mListener = mListener;
    }

    public void setValuationListener(ChooseValuationListener mListener) {
        this.mValuationListener = mListener;
    }

    public void setIntentListener(ChooseIntentListener mListener) {
        this.mIntentListener = mListener;
    }

    //回调
    public interface ChooseListener {
        void completeChoose(String brand, String series, String model, String brandId, String seriesId, String modelId);
    }

    public interface ChooseValuationListener {
        void completeValuationChoose(String brand, String series, String model, String brandId, String seriesId, String modelId, String valuationModelId);
    }

    public interface ChooseIntentListener {
        void completeIntentChoose(String brand, String series, String model);
    }
}
