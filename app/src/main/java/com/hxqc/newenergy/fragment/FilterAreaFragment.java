package com.hxqc.newenergy.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.hxqc.mall.core.api.RequestFailView;
import com.hxqc.mall.core.db.area.AreaDBManager;
import com.hxqc.mall.core.fragment.FunctionFragment;
import com.hxqc.mall.core.util.ToastHelper;
import com.hxqc.newenergy.adapter.EV_FilterAreaCityAdapter;
import com.hxqc.newenergy.adapter.EV_FilterAreaProvinceAdapter;
import com.hxqc.newenergy.bean.position.City;
import com.hxqc.newenergy.bean.position.Province;
import com.hxqc.util.DebugLog;

import java.util.ArrayList;

import hxqc.mall.R;

/**
 * Function:
 *
 * @author 袁秉勇
 * @since 2016年03月18日
 */
public class FilterAreaFragment extends FunctionFragment implements EV_FilterAreaProvinceAdapter.FilterProvinceCallBack, EV_FilterAreaCityAdapter.FilterAreaCityCallBack {
	private final static String TAG = FilterAreaFragment.class.getSimpleName();
	private Context mContext;

//    private FilterControllerInf filterController;

	private ArrayList<Province> provinces;

	private ListView mProvinceListView;
	private ListView mCityListView;
	private RequestFailView mRequestFailView;

	private EV_FilterAreaProvinceAdapter ev_filterAreaProvinceAdapter;
	private EV_FilterAreaCityAdapter ev_filterAreaCityAdapter;

	private FilterAreaCallBack filterAreaCallBack;

	private AreaDBManager areaDBManager;

	public static FilterAreaFragment newInstance(ArrayList<Province> provinces) {
		Bundle args = new Bundle();
		FilterAreaFragment fragment = new FilterAreaFragment();
		args.putParcelableArrayList("area", provinces);
		fragment.setArguments(args);
		return fragment;
	}

	public void setFilterAreaCallBack(FilterAreaCallBack filterAreaCallBack) {
		this.filterAreaCallBack = filterAreaCallBack;
	}

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
//        filterController = FilterControllerInf.getInstance();
		mContext = context;
	}


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		areaDBManager = AreaDBManager.getInstance(mContext.getApplicationContext());
	}


	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.ev_filter_area_activity, container, false);
	}


	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		mProvinceListView = (ListView) view.findViewById(R.id.province_list_view);
		mCityListView = (ListView) view.findViewById(R.id.city_list_view);
		mRequestFailView = (RequestFailView) view.findViewById(R.id.request_view);
	}


	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		provinces = getArguments().getParcelableArrayList("area");

		if (provinces == null || provinces.size() == 0) {
			onFailed();
			return;
		}

		ev_filterAreaProvinceAdapter = new EV_FilterAreaProvinceAdapter(getContext(), provinces);
		ev_filterAreaProvinceAdapter.setFilterProvinceCallBack(this);
		mProvinceListView.setAdapter(ev_filterAreaProvinceAdapter);

//        if (!TextUtils.isEmpty(EVSharePreferencesHelper.getLastHistoryCity(mContext))) {
//            for (int i = 0; i < provinces.size(); i++) {
//                if (provinces.get(i).provinceID == areaDBManager.searchPIDAndCIDByTitle(EVSharePreferencesHelper.getLastHistoryCity(mContext))[0]) {
//                    ArrayList< City > cities = provinces.get(i).areaGroup;
//                    showCityListData(cities, i);
//                    final int finalI = i;
//                    mProvinceListView.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            mProvinceListView.smoothScrollToPositionFromTop(finalI, 0, 300);
//                        }
//                    });
//                    return;
//                }
//            }
//
//            ArrayList< City > cities = provinces.get(0).areaGroup;
//            showCityListData(cities, 0);
//        } else {
//            ArrayList< City > cities = provinces.get(0).areaGroup;
//            showCityListData(cities, 0);
//        }

//        ev_filterAreaCityAdapter = new EV_FilterAreaCityAdapter(getContext(), provinces.get(1).areaGroup);
//        ev_filterAreaCityAdapter.setFilterAreaCityCallBack(this);
//        mCityListView.setAdapter(ev_filterAreaCityAdapter);

		if (mRequestFailView.getVisibility() == View.VISIBLE)
			mRequestFailView.setVisibility(View.GONE);

//        getData();
//        ev_filterAreaProvinceAdapter = new EV_FilterAreaProvinceAdapter(getContext(), provinces);
//        ev_filterAreaProvinceAdapter.setFilterProvinceCallBack(this);
//        mProvinceListView.setAdapter(ev_filterAreaProvinceAdapter);
//
//        ev_filterAreaCityAdapter = new EV_FilterAreaCityAdapter(getContext(), provinces.get(0).areaGroup);
//        ev_filterAreaCityAdapter.setFilterAreaCityCallBack(this);
//        mCityListView.setAdapter(ev_filterAreaCityAdapter);
	}


	private void getData() {
//        filterController.requestArea(getContext(), this);

//        provinces = new ArrayList<>();
//        for (int i = 0; i < 30; i++) {
//            EV_FilterAreaCityAdapter.Province name = new EV_FilterAreaCityAdapter().new Province();
//            name.provinceName = "省 " + i;
//            name.areaGroup = new ArrayList<>();
//             for (int j = 0; j < 10; j++) {
//                 EV_FilterAreaCityAdapter.City city = new EV_FilterAreaCityAdapter().new City();
//                 city.name = "省" + i + "的城市 " + j;
//                 name.areaGroup.add(city);
//             }
//            provinces.add(name);
//        }
	}


	@Override
	public String fragmentDescription() {
		return "新能源地区筛选fragment";
	}


	@Override
	public void onClickProvinceCallBack(final int position) {
//        if (position != 0) {
//		ArrayList<City> cities = provinces.get(position).areaGroup;
//		showCityListData(cities, position);
//
//		mProvinceListView.post(new Runnable() {
//			@Override
//			public void run() {
//				mProvinceListView.smoothScrollToPositionFromTop(position, 0, 300);
//			}
//		});
//
//		mCityListView.post(new Runnable() {
//			@Override
//			public void run() {
//				mCityListView.smoothScrollToPosition(0);
//			}
//		});
////        } else {
////            if(ev_filterAreaCityAdapter != null) {
////                ev_filterAreaCityAdapter.upDate(null);
////            }
////
////            if (filterAreaCallBack != null) filterAreaCallBack.onFilterAreaCallBack(null);
////        }
//
//		ev_filterAreaProvinceAdapter.notifyDataSetChanged();
	}


	@Override
	public void onClickCityCallBack(String cityName) {
		DebugLog.e(TAG, " xxxxxxxxxxx=============xxxxxxxxxx " + cityName);
		ev_filterAreaCityAdapter.notifyDataSetChanged();
		if (filterAreaCallBack != null) filterAreaCallBack.onFilterAreaCallBack(cityName);
	}


	public void onFailed() {
		mRequestFailView.setEmptyDescription("未找到结果");
		mRequestFailView.setRequestViewType(RequestFailView.RequestViewType.empty);
		mRequestFailView.setVisibility(View.VISIBLE);
	}

	public void showCityListData(ArrayList<City> cities, int position) {
		if (ev_filterAreaCityAdapter == null) {
			ev_filterAreaCityAdapter = new EV_FilterAreaCityAdapter(getContext(), cities);
			ev_filterAreaCityAdapter.setFilterAreaCityCallBack(this);
			mCityListView.setAdapter(ev_filterAreaCityAdapter);
		} else {
			ev_filterAreaCityAdapter.setLastClickPos(-1);
			if (cities != null && position < provinces.size()) {
				ev_filterAreaCityAdapter.refreshData(cities);
			} else {
				ev_filterAreaCityAdapter.refreshData(null);
				ToastHelper.showRedToast(getContext(), "抱歉，数据异常!");
			}
		}
		ev_filterAreaCityAdapter.setIsFirstChangedStyled(true);
	}


	public interface FilterAreaCallBack {
		void onFilterAreaCallBack(String cityName);
	}
}
