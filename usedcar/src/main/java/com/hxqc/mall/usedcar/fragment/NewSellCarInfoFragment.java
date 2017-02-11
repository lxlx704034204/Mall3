package com.hxqc.mall.usedcar.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.gson.reflect.TypeToken;
import com.hxqc.mall.core.api.LoadingAnimResponseHandler;
import com.hxqc.mall.core.api.RequestFailView;
import com.hxqc.mall.core.controler.UserInfoHelper;
import com.hxqc.mall.core.fragment.FunctionFragment;
import com.hxqc.mall.core.util.BaseSharedPreferencesHelper;
import com.hxqc.mall.core.views.pullrefreshhandler.OnRefreshHandler;
import com.hxqc.mall.core.views.pullrefreshhandler.UltraPullRefreshHeaderHelper;
import com.hxqc.mall.usedcar.R;
import com.hxqc.mall.usedcar.adapter.NewSellCarInfoAdapter;
import com.hxqc.mall.usedcar.api.UsedCarApiClient;
import com.hxqc.mall.usedcar.model.SellCarInfo;
import com.hxqc.mall.usedcar.utils.UsedCarActivitySwitcher;
import com.hxqc.util.JSONUtils;
import com.hxqc.widget.ListViewNoSlide;

import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * @Author : 钟学东
 * @Since : 2015-08-27
 * FIXME
 * Todo
 */
public class NewSellCarInfoFragment extends FunctionFragment implements OnRefreshHandler {

	protected PtrFrameLayout mPtrFrameLayoutView;
	protected UltraPullRefreshHeaderHelper mPtrHelper;
	List<SellCarInfo> infos = new ArrayList<>();
	List<SellCarInfo> person = new ArrayList<>();
	List<SellCarInfo> platform = new ArrayList<>();
	ListViewNoSlide mPersonView;
	ListViewNoSlide mPlatformView;
	UsedCarApiClient usedCarApiClient;
	NewSellCarInfoAdapter mPersonAdapter;
	NewSellCarInfoAdapter mPlatformAdapter;

	RelativeLayout relativeLayout_person;
	RelativeLayout relativeLayout_paltform;

	LinearLayout linearLayout;
	RequestFailView requestFailView;

	BaseSharedPreferencesHelper baseSharedPreferencesHelper;
	String number;

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		mPersonView = (ListViewNoSlide) view.findViewById(R.id.person_list);
		mPlatformView = (ListViewNoSlide) view.findViewById(R.id.platform_list);
		mPtrFrameLayoutView = (PtrFrameLayout) view.findViewById(R.id.refresh_frame);
		mPtrHelper = new UltraPullRefreshHeaderHelper(getActivity(), mPtrFrameLayoutView);
		mPtrHelper.setOnRefreshHandler(this);
		relativeLayout_person = (RelativeLayout) view.findViewById(R.id.rl_person);
		relativeLayout_paltform = (RelativeLayout) view.findViewById(R.id.rl_platform);
		linearLayout = (LinearLayout) view.findViewById(R.id.ll);
		requestFailView = (RequestFailView) view.findViewById(R.id.request_fail_view);
	}

	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_new_sell_carinfo, null);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		usedCarApiClient = new UsedCarApiClient();
		baseSharedPreferencesHelper = new BaseSharedPreferencesHelper(getActivity());
		number = UserInfoHelper.getInstance().getPhoneNumber(getActivity());
		initEvent();
	}

	private void initEvent() {
		mPersonView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				SellCarInfo sellCarInfo = (SellCarInfo) mPersonAdapter.getItem(position);
				UsedCarActivitySwitcher.toSellCarDetail(getActivity(), number, sellCarInfo.car_source_no);
			}
		});

		mPlatformView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				SellCarInfo sellCarInfo = (SellCarInfo) mPlatformAdapter.getItem(position);
				UsedCarActivitySwitcher.toSellCarDetail(getActivity(), number, sellCarInfo.car_source_no);
			}
		});

	}

	private void initDate() {

		usedCarApiClient.getSellCarList(number, new LoadingAnimResponseHandler(getActivity()) {
			@Override
			public void onSuccess(String response) {
				infos = JSONUtils.fromJson(response, new TypeToken<List<SellCarInfo>>() {
				});

				if (infos != null) {
					if (infos.isEmpty()) {
						linearLayout.setVisibility(View.GONE);
						requestFailView.setVisibility(View.VISIBLE);
						requestFailView.setEmptyDescription("暂无数据");
						requestFailView.setRequestViewType(RequestFailView.RequestViewType.empty);

					} else {
						for (SellCarInfo carInfo : infos) {
							if (carInfo.publish_from.equals("1")) {
								platform.add(carInfo);
							} else if (carInfo.publish_from.equals("2")) {
								person.add(carInfo);
							}
						}
					}

					if (platform.isEmpty()) {
						relativeLayout_paltform.setVisibility(View.GONE);
					} else {
						relativeLayout_paltform.setVisibility(View.VISIBLE);
						mPlatformAdapter = new NewSellCarInfoAdapter(getActivity(), platform);
						mPlatformView.setAdapter(mPlatformAdapter);
					}
					if (person.isEmpty()) {
						relativeLayout_person.setVisibility(View.GONE);
					} else {
						relativeLayout_person.setVisibility(View.VISIBLE);
						mPersonAdapter = new NewSellCarInfoAdapter(getActivity(), person);
						mPersonView.setAdapter(mPersonAdapter);
					}
				} else {
					linearLayout.setVisibility(View.GONE);
					requestFailView.setVisibility(View.VISIBLE);
					requestFailView.setEmptyDescription("暂无数据");
					requestFailView.setRequestViewType(RequestFailView.RequestViewType.empty);

				}
			}

			@Override
			public void onFinish() {
				super.onFinish();
				mPtrHelper.refreshComplete(mPtrFrameLayoutView);
			}
		});


	}


	@Override
	public String fragmentDescription() {
		return "我的卖车信息";
	}

	@Override
	public void onResume() {
		super.onResume();
		platform.clear();
		person.clear();
		initDate();
	}

	@Override
	public boolean hasMore() {
		return false;
	}

	@Override
	public void onRefresh() {
		platform.clear();
		person.clear();
		initDate();
	}

	@Override
	public void onLoadMore() {
		mPtrHelper.refreshComplete(mPtrFrameLayoutView);
	}
}
