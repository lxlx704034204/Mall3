package com.hxqc.mall.usedcar.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ListView;

import com.hxqc.mall.core.api.LoadingAnimResponseHandler;
import com.hxqc.mall.core.fragment.FunctionFragment;
import com.hxqc.mall.core.util.OtherUtil;
import com.hxqc.mall.usedcar.R;
import com.hxqc.mall.usedcar.adapter.ChooseBrandAdapter;
import com.hxqc.mall.usedcar.api.UsedCarApiClient;
import com.hxqc.mall.usedcar.model.BrandGroup;
import com.hxqc.mall.usedcar.model.Choose;
import com.hxqc.mall.usedcar.model.IdAndValue;
import com.hxqc.mall.usedcar.views.SideBar;
import com.hxqc.util.JSONUtils;
import com.hxqc.widget.PinnedHeaderExpandableListView;
import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;

import java.util.ArrayList;

/**
 * 车辆条件筛选 侧边栏列表Fragment
 * Created by huangyi on 15/10/21.
 */
public class BuyCarFilterDetailFragment extends FunctionFragment implements SideBar.OnTouchingLetterChangedListener {

    Choose mChoose; //数据源

    OnCompleteChooseListener mOnCompleteChooseListener;
    ListView mListView;
    PinnedHeaderExpandableListView mBrandListView;
    SideBar mBarView;
    View mBrandListHeaderView;
    ArrayList<IdAndValue> mArrayListData = new ArrayList<>(); //mListView数据源
    ArrayList<BrandGroup> mBrandListViewData; //mBrandListView数据源

    public void setOnCompleteChooseListener(OnCompleteChooseListener onCompleteChooseListener) {
        this.mOnCompleteChooseListener = onCompleteChooseListener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_filter_detailitem, container, false);
        mListView = (ListView) root.findViewById(R.id.lv);
        mBrandListView = (PinnedHeaderExpandableListView) root.findViewById(R.id.elv);
        mBarView = (SideBar) root.findViewById(R.id.bar);
        mBarView.setOnTouchingLetterChangedListener(this);
        mBrandListHeaderView = View.inflate(mContext, R.layout.fragment_filter_unlimited, null);
        mBrandListView.addHeaderView(mBrandListHeaderView);

        loadData();
        return root;
    }

    @Override
    public void onTouchingLetterChanged(String s, StringBuffer s1) {
        int position = 0;
        for (int i = 0; i < mChoose.brand.size(); i++) {
            if (mChoose.brand.get(i).groupTag.charAt(0) == (s.charAt(0))) {
                mBrandListView.setSelectedGroup(i);
                break;
            } else {
                position = position + mChoose.brand.get(i).group.size() + 1;
            }
        }
    }

    public void initData(int position) {
        if (null == mChoose) {
            loadData();
            mListView.setVisibility(View.GONE);
            mBrandListView.setVisibility(View.GONE);
            mBarView.setVisibility(View.GONE);
        } else {
            mArrayListData.clear();
            switch (position) {
                case 0: //价格
                    mArrayListData.addAll(mChoose.price);
                    break;

                case 1: //品牌
                    mBrandListViewData = mChoose.brand;
                    break;

                case 2: //年龄
                    mArrayListData.addAll(mChoose.age_limit);
                    break;

                case 3: //级别
                    mArrayListData.addAll(mChoose.level);
                    break;

                case 4: //来源
                    mArrayListData.addAll(mChoose.publish_from);
                    break;

                case 5: //排量
                    mArrayListData.addAll(mChoose.displacement);
                    break;

                case 6: //变速箱
                    mArrayListData.addAll(mChoose.gearbox);
                    break;

                case 7: //里程
                    mArrayListData.addAll(mChoose.mileage);
                    break;
            }

            if (position == 1) {
                mListView.setVisibility(View.GONE);
                mBrandListView.setVisibility(View.VISIBLE);
                mBarView.setVisibility(View.VISIBLE);
                initBrandFilter();
            } else {
                mBrandListView.setVisibility(View.GONE);
                mListView.setVisibility(View.VISIBLE);
                mBarView.setVisibility(View.GONE);
                initOtherFilter();
            }
        }
    }

    private void loadData() {
        new UsedCarApiClient().getFilter(new LoadingAnimResponseHandler(getContext(), false) {
            @Override
            public void onSuccess(String response) {
                mChoose = JSONUtils.fromJson(response, Choose.class);
            }
        });
    }

    /**
     * 处理 价格 年龄 级别 来源 排量 变速箱 里程 有关条件的筛选点击操作
     **/
    private void initOtherFilter() {
        QuickAdapter<IdAndValue> quickAdapter = new QuickAdapter<IdAndValue>(mContext, R.layout.item_buycar_filter_child) {
            @Override
            protected void convert(BaseAdapterHelper helper, IdAndValue item) {
                helper.setText(R.id.textview, item.value);
            }
        };
        quickAdapter.addAll(mArrayListData);
        mListView.setAdapter(quickAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mOnCompleteChooseListener != null)
                    mOnCompleteChooseListener.onCompleteChoose(mArrayListData.get(position).value, mArrayListData.get(position).id, position == 0);
            }
        });
    }

    /** 处理 品牌 有关条件的筛选点击操作 **/
    private void initBrandFilter() {
        ChooseBrandAdapter mChooseBrandAdapter = new ChooseBrandAdapter(getActivity(), mBrandListViewData, ChooseFragment.COMMON);
        mBrandListView.setAdapter(mChooseBrandAdapter);
        mBrandListView.setOnHeaderUpdateListener(mChooseBrandAdapter);
        OtherUtil.openAllChild(mChooseBrandAdapter, mBrandListView);
        mBrandListHeaderView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mOnCompleteChooseListener != null)
                    mOnCompleteChooseListener.onCompleteChoose("不限", "", true);
            }
        });
        mBrandListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return true;
            }
        }, false);
        mBrandListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                if(mOnCompleteChooseListener != null)
                    mOnCompleteChooseListener.onCompleteChoose(mBrandListViewData.get(groupPosition).group.get(childPosition).brand_name, mBrandListViewData.get(groupPosition).group.get(childPosition).id, false);
                return true;
            }
        });
    }

    @Override
    public String fragmentDescription() {
        return "车辆条件筛选侧边栏列表Fragment";
    }

    public interface OnCompleteChooseListener {
        /**
         * 监听侧边栏列表选择完成
         *
         * @param itemName 选择条件的名字
         * @param itemId 选择条件的id
         * @param isSelectedUnlimited 是否选择不限条件 true为是
         */
        void onCompleteChoose(String itemName, String itemId, boolean isSelectedUnlimited);
    }

}
