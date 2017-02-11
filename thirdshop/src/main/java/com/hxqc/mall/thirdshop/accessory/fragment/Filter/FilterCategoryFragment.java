package com.hxqc.mall.thirdshop.accessory.fragment.Filter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hxqc.mall.core.api.RequestFailView;
import com.hxqc.mall.core.fragment.FunctionFragment;
import com.hxqc.mall.core.views.CustomRecyclerView;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.accessory.adapter.AccessoryGoodsCategoryAdapter;
import com.hxqc.mall.thirdshop.accessory.control.FilterController;
import com.hxqc.mall.thirdshop.accessory.model.AccessoryBigCategory;
import com.hxqc.mall.thirdshop.accessory.model.AccessorySmallCategory;

import java.util.ArrayList;

/**
 * Function: 品类筛选Fragment
 *
 * @author 袁秉勇
 * @since 2016年02月23日
 */
public class FilterCategoryFragment extends FunctionFragment implements AccessoryGoodsCategoryAdapter.CallBack, FilterController.AccessoryCategoryHandler {
    public final static String TAG = FilterCategoryFragment.class.getSimpleName();
    public CustomRecyclerView recyclerView;
    private Context mContext;
    private AccessoryGoodsCategoryAdapter accessoryGoodsCategoryAdapter;
    private FilterController filterController;
    private CallBack callBack;
    private View rootView;
    private RequestFailView requestFailView;

    /** 带ShopID的实例化方法 **/
    public static FilterCategoryFragment newInstance(String shopID) {
        Bundle args = new Bundle();
        args.putString("shopID", shopID);
        FilterCategoryFragment fragment = new FilterCategoryFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_filter_category, container, false);
        }

        ViewGroup parentView = (ViewGroup) rootView.getParent();
        if (parentView != null) {
            parentView.removeView(rootView);
        }
        return rootView;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = (CustomRecyclerView) view.findViewById(R.id.recycler_view);
        requestFailView = (RequestFailView) view.findViewById(R.id.request_view);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAnimation(null);

        filterController = FilterController.getInstance();
        getData();
    }


    public void getData() {
        if (requestFailView.getVisibility() == View.VISIBLE) requestFailView.setVisibility(View.GONE);
        if (accessoryGoodsCategoryAdapter == null) {
            recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
            recyclerView.setItemAnimator(null);
            if (getArguments()!= null && !TextUtils.isEmpty(getArguments().getString("shopID"))) {
                filterController.getAccessoryCategoryData(getContext(), getArguments().getString("shopID"), this);
            } else {
                filterController.getAccessoryCategoryData(getContext(), "", this);
            }
        }
    }


    @Override
    public void onGoodsCategoryChooseCallBack(AccessoryBigCategory accessoryBigCategory, final AccessorySmallCategory accessorySmallCategory) {
        if (accessoryBigCategory == null && accessorySmallCategory == null) {
            if (callBack != null) callBack.onFilterCategoryCallBack(null);
        } else {
            AccessoryBigCategory bigCategory = new AccessoryBigCategory(accessoryBigCategory.class1stName, accessoryBigCategory.class1stID, new ArrayList< AccessorySmallCategory >() {{
                add(accessorySmallCategory);
            }});
            if (callBack != null) callBack.onFilterCategoryCallBack(bigCategory);
        }
    }


    @Override
    public void onSucceed(ArrayList< AccessoryBigCategory > accessoryBigCategories) {
        if (accessoryBigCategories == null) {
            onFailed(false);
            return;
        }

        if (accessoryGoodsCategoryAdapter == null) {
            accessoryGoodsCategoryAdapter = new AccessoryGoodsCategoryAdapter(mContext, accessoryBigCategories);
            accessoryGoodsCategoryAdapter.setCallBack(this);
        }

        recyclerView.setAdapter(accessoryGoodsCategoryAdapter);
        if (requestFailView.getVisibility() == View.VISIBLE) requestFailView.setVisibility(View.GONE);
    }


    @Override
    public void onFailed(boolean offLine) {
        if (offLine) {
            requestFailView.setFailButtonClick("重新加载", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getData();
                }
            });
            requestFailView.setRequestViewType(RequestFailView.RequestViewType.fail);
        } else {
            requestFailView.setEmptyDescription("获取数据失败");
            requestFailView.setFailButtonClick("重新加载", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getData();
                }
            });
            requestFailView.setRequestViewType(RequestFailView.RequestViewType.empty);
        }
        if (requestFailView.getVisibility() == View.GONE) requestFailView.setVisibility(View.VISIBLE);
    }

    @Override
    public String fragmentDescription() {
        return "用品选择品系Fragment";
    }


    public interface CallBack {
        void onFilterCategoryCallBack(AccessoryBigCategory accessoryBigCategory);
    }
}
