package com.hxqc.mall.thirdshop.fragment.Filter;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.hxqc.mall.core.api.LoadingAnimResponseHandler;
import com.hxqc.mall.core.api.RequestFailView;
import com.hxqc.mall.core.db.area.AreaDBManager;
import com.hxqc.mall.core.fragment.FunctionFragment;
import com.hxqc.mall.core.util.ToastHelper;
import com.hxqc.mall.core.views.SpinnerPopWindow;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.api.ThirdPartShopClient;
import com.hxqc.mall.thirdshop.control.TFilterController;
import com.hxqc.mall.thirdshop.model.ThirdArea;
import com.hxqc.mall.thirdshop.model.ThirdAreaModel;
import com.hxqc.mall.thirdshop.utils.SharedPreferencesHelper;
import com.hxqc.mall.thirdshop.views.adpter.ThirdCityChooseAdapter;
import com.hxqc.mall.thirdshop.views.adpter.ThirdProvinceChooseAdapter;
import com.hxqc.util.DebugLog;
import com.hxqc.util.JSONUtils;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;


public class FilterAreaFragment extends FunctionFragment {
    public static final String ShowAreaLevelKey = "showAreaLevel";
    //=========================
    protected OnAreaChooseInteractionListener mListener;
    TFilterController tFilterController;
    FilterAreaFragmentCallBack filterAreaFragmentCallBack;
    ThirdPartShopClient ThirdPartShopClient;
    AreaDBManager areaDBManager;
    //省
    SpinnerPopWindow mClickProvince;
    TextView mProvinceView;
    //市
    SpinnerPopWindow mClickCity;
    TextView mCityView;
    //区
    SpinnerPopWindow mClickSubdivide;
    TextView mSubdivideView;
//    MyAreaChooseAdapter mAreaAdapter;

//    ArrayList< AreaModel > areaModels_p;//省
//    ArrayList< AreaModel > areaModels_c;//市
//    ArrayList< AreaModel > areaModels_d;//区
    Button mSave;
    ThirdAreaModel current_P;  //当前选中的省
//    AreaModel current_D;  //当前选中的区
    ThirdArea current_C;  //当前选中的市
    ThirdProvinceChooseAdapter adapter_p;
    ThirdCityChooseAdapter adapter_c;
    int showAreaLevel = 1;
    RequestFailView requestFailView;
    private View rootView;
//    MyAreaChooseAdapter adapter_d;
    //------选择的 进入的当前的省市区
    private String default_P = "";
    private String default_C = "";
    private String default_d = "";

    public FilterAreaFragment() {
    }

    public static FilterAreaFragment newInstance(int param) {
        FilterAreaFragment fragment = new FilterAreaFragment();
        Bundle args = new Bundle();
        args.putInt(ShowAreaLevelKey, param);
        fragment.setArguments(args);
        return fragment;
    }

    public void setFilterAreaFragmentCallBack(FilterAreaFragmentCallBack filterAreaFragmentCallBack) {
        this.filterAreaFragmentCallBack = filterAreaFragmentCallBack;
    }

    public void setmListener(OnAreaChooseInteractionListener mListener) {
        this.mListener = mListener;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ThirdPartShopClient = new ThirdPartShopClient();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_city_choose, container, false);
//        rootView = inflater.inflate(com.hxqc.mall.core.R.layout.fragment_city_choose, container, false);
//        rootView.setVisibility(View.GONE);
//        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rootView = view.findViewById(R.id.content_view);
        rootView.setVisibility(View.GONE);

        DebugLog.e("area", "onViewCreated");

        mClickProvince = (SpinnerPopWindow) view.findViewById(R.id.rl_province);
        mClickProvince.setPopHeight(480);

        mProvinceView = (TextView) view.findViewById(R.id.tv_province);

        mClickCity = (SpinnerPopWindow) view.findViewById(R.id.rl_city);
        mClickCity.setPopHeight(480);

        mCityView = (TextView) view.findViewById(R.id.tv_city);
        mClickSubdivide = (SpinnerPopWindow) view.findViewById(R.id.rl_subdivide);

        mSubdivideView = (TextView) view.findViewById(R.id.tv_subdivide);
        mSave = (Button) view.findViewById(R.id.btn_save);

        getData();

        if (getArguments() != null) {
            showAreaLevel = getArguments().getInt(ShowAreaLevelKey, 2);
        }
        DebugLog.i("Tag", "ssssss  " + showAreaLevel);
        showAreaLevel(showAreaLevel);

        ((ViewGroup) rootView).removeViewAt(0);

        int padding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16, getContext().getResources().getDisplayMetrics());

        TextView textView = new TextView(getContext());
        LinearLayout.LayoutParams layoutParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 45, getContext().getResources().getDisplayMetrics()));
        layoutParam.setMargins(0, padding, 0, 0);
        textView.setLayoutParams(layoutParam);
        textView.setGravity(Gravity.CENTER_VERTICAL);
        textView.setPadding(padding, 0, padding, 0);
        textView.setText("不限");
        textView.setTextColor(getResources().getColor(R.color.straight_matter_and_secondary_text));
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterAreaFragmentCallBack.onFilterAreaCallBack();
            }
        });
        ((ViewGroup) rootView).addView(textView, 0);

        View divideLine = new View(getContext());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, getContext().getResources().getDisplayMetrics()));
        layoutParams.setMargins(padding, 0, padding, 0);
        divideLine.setLayoutParams(layoutParams);
        divideLine.setBackgroundColor(getResources().getColor(R.color.divider));
        ((ViewGroup) rootView).addView(divideLine, 1);

        tFilterController = TFilterController.getInstance();
        tFilterController.requestThirdShopArea();
    }


    private void initCityDataViews(ArrayList<ThirdAreaModel> thirdAreaModels) {
        adapter_p = new ThirdProvinceChooseAdapter(getActivity(), thirdAreaModels);
        mClickProvince.setAdapter(adapter_p);

        mClickCity.setEnabled(false);

        mClickProvince.setOnItemClickList(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                current_P = (ThirdAreaModel) parent.getAdapter().getItem(position);
                mClickCity.setEnabled(true);
                adapter_c = new ThirdCityChooseAdapter(getActivity(), current_P.areaGroup);
                mClickCity.setAdapter(adapter_c);

                mClickSubdivide.setEnabled(false);
                mProvinceView.setText(current_P.provinceName);
                mCityView.setText("选择城市");
                mSubdivideView.setText("选择区域");

                mClickCity.performClick();
            }
        });

        mClickCity.setOnItemClickList(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                current_C = (ThirdArea) parent.getAdapter().getItem(position);
                mCityView.setText(current_C.province);
                mSubdivideView.setText("选择区域");
                mClickSubdivide.setEnabled(true);

            }
        });

        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferencesHelper sharedPreferencesHelper = new SharedPreferencesHelper(getContext());

                String p = mProvinceView.getText().toString().trim();
                String c = mCityView.getText().toString().trim();
                String provinceInitial = "";
                String city = "";
                if (p.equals("选择省份")) {
                    p = "";
                } else {
                    provinceInitial = current_P.provinceInitial;
                }
                if (c.equals("选择城市")) {
                    c = "";
                } else {
                    city = current_C.province;
                }
                if (TextUtils.isEmpty(city)) {
                    ToastHelper.showRedToast(getContext(), "请选择具体城市");
                    return;
                }
                mListener.OnAreaChooseInteraction(p, provinceInitial, city);
            }
        });
    }

    /** 获取网络地理信息数据 */
    private void getData() {
        ThirdPartShopClient.requestFilterArea(new LoadingAnimResponseHandler(mContext, true) {
            @Override
            public void onSuccess(String response) {
                ArrayList<ThirdAreaModel> thirdAreaModels = JSONUtils.fromJson(response, new TypeToken<ArrayList<ThirdAreaModel>>() {
                });
                if (thirdAreaModels == null || thirdAreaModels.size() <= 0) {
                    showEmptyView();
                    return;
                }
                rootView.setVisibility(View.VISIBLE);
                if (requestFailView!= null && requestFailView.getVisibility() == View.VISIBLE) requestFailView.setVisibility(View.GONE);
                initCityDataViews(thirdAreaModels);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                showEmptyView();
            }
        });
    }

    private void showEmptyView() {
        if (requestFailView == null) {
            requestFailView = new RequestFailView(mContext);
            requestFailView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            requestFailView.setRequestViewType(RequestFailView.RequestViewType.fail);
            requestFailView.setFailButtonClick("重新加载", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (requestFailView != null && requestFailView.getVisibility() == View.VISIBLE) requestFailView.setVisibility(View.GONE);
                    getData();
                }
            });
            ((ViewGroup) getView()).addView(requestFailView);
        }
        if (requestFailView.getVisibility() == View.GONE) requestFailView.setVisibility(View.VISIBLE);
    }

    /**
     * 设置
     *
     * @param level 显示城市选择级别  0：省 1：省  市 2： 省市区
     */
    public void showAreaLevel(int level) {
        switch (level) {
            case 0:
                mClickCity.setVisibility(View.GONE);
                mCityView.setVisibility(View.GONE);

                mClickSubdivide.setVisibility(View.GONE);
                mSubdivideView.setVisibility(View.GONE);
                break;
            case 1:
                mClickSubdivide.setVisibility(View.GONE);
                mSubdivideView.setVisibility(View.GONE);
                break;
            case 2:
                break;
        }
    }

    @Override
    public String fragmentDescription() {
        return "省市区选择";
    }

    public interface FilterAreaFragmentCallBack {
        void onFilterAreaCallBack();
    }

    //回调
    public interface OnAreaChooseInteractionListener {
        void OnAreaChooseInteraction(String provinces, String provinceInitial, String city);
    }
}
