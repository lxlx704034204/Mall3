package com.hxqc.mall.core.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import com.hxqc.mall.core.R;
import com.hxqc.mall.core.adapter.MyAreaChooseAdapter;
import com.hxqc.mall.core.db.area.AreaDBManager;
import com.hxqc.mall.core.db.area.TCity;
import com.hxqc.mall.core.db.area.TDistrict;
import com.hxqc.mall.core.db.area.TProvince;
import com.hxqc.mall.core.model.DeliveryAddress;
import com.hxqc.mall.core.views.SpinnerPopWindow;
import com.hxqc.util.DebugLog;

import java.util.ArrayList;

/**
 * 省市区城市选择
 */
public class CityChooseFragment extends FunctionFragment {

	public static final String AddressKey = "AddressKey";
	public static final String ShowAreaLevelKey = "showAreaLevel";
	public AreaDBManager areaDBManager;
	//=========================
	protected OnAreaChooseInteractionListener mListener;
	//省
	SpinnerPopWindow mClickProvince;
	TextView mProvinceView;
	//市
	SpinnerPopWindow mClickCity;
	TextView mCityView;
	//区
	SpinnerPopWindow mClickSubdivide;
	TextView mSubdivideView;
	Button mSave;
	ArrayList<TProvince> areaModels_p;//省
	ArrayList<TCity> areaModels_c;//市
	ArrayList<TDistrict> areaModels_d;//区
	TProvince current_P;  //当前选中的省
	TCity current_C;  //当前选中的市
	TDistrict current_D;  //当前选中的区
	MyAreaChooseAdapter adapter_p;
	MyAreaChooseAdapter adapter_c;
	MyAreaChooseAdapter adapter_d;
	DeliveryAddress mDeliveryAddress;
	int showAreaLevel = 2;
	//------选择的 进入的当前的省市区
	private String default_P = "";
	private String default_C = "";
	private String default_D = "";

	{
		current_P = new TProvince();  //当前选中的省
		current_C = new TCity();  //当前选中的市
		current_D = new TDistrict();  //当前选中的区
	}

	public CityChooseFragment() {

	}

	public void setDefaultAreaData(String p, String c, String d) {
		if (TextUtils.isEmpty(p)) {
			return;
		}
		this.default_P = p;
		this.default_C = c;
		this.default_D = d;
		setCurrentAreaData();

	}

	//获取传入的省市区
	private void setCurrentAreaData() {
		AreaDBManager areaDBManager = AreaDBManager.getInstance(getContext());
		if (!TextUtils.isEmpty(default_P)) {
			mProvinceView.setText(default_P);
			adapter_p.setChooseColorChange(areaModels_p, default_P);
			current_P = areaDBManager.searchPIDByTitle(default_P);
			areaModels_c = areaDBManager.getCListByName(default_P);
			mClickCity.setAdapter(adapter_c);
			mClickCity.setEnabled(true);
		}
		if (!TextUtils.isEmpty(default_C)) {
			mCityView.setText(default_C);
			adapter_c.setChooseColorChange(areaModels_c, default_C);
			areaModels_d = AreaDBManager.getInstance(getContext()).getDListByName(default_C);
			mClickSubdivide.setAdapter(adapter_d);
			mClickSubdivide.setEnabled(true);
			current_C = areaDBManager.searchCIDByTitle(default_C);
		}
		if (!TextUtils.isEmpty(default_D)) {
			adapter_d.setChooseColorChange(areaModels_d, default_D);
			mSubdivideView.setText(default_D);
			current_D = areaDBManager.searchDIDByTitle(default_D);
		}


	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
//        initDataFromAssets();
		return inflater.inflate(R.layout.fragment_city_choose, container, false);
	}

	@Override
	public void onViewStateRestored(Bundle savedInstanceState) {
		super.onViewStateRestored(savedInstanceState);
		Log.e("Tag", "onViewStateRestored");
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		Log.e("Tag", "onViewCreated");

		adapter_p = new MyAreaChooseAdapter(getActivity());
		adapter_c = new MyAreaChooseAdapter(getActivity());
		adapter_d = new MyAreaChooseAdapter(getActivity());

		mClickProvince = (SpinnerPopWindow) view.findViewById(R.id.rl_province);
		mClickProvince.setPopHeight(480);

		mProvinceView = (TextView) view.findViewById(R.id.tv_province);

		mClickCity = (SpinnerPopWindow) view.findViewById(R.id.rl_city);
		mClickCity.setPopHeight(480);

		mCityView = (TextView) view.findViewById(R.id.tv_city);
		mClickSubdivide = (SpinnerPopWindow) view.findViewById(R.id.rl_subdivide);
//        mClickSubdivide.setPopHeight(480);

		mSubdivideView = (TextView) view.findViewById(R.id.tv_subdivide);
		mSave = (Button) view.findViewById(R.id.btn_save);
		initDataFromAssets();

		if (getArguments() != null) {
			showAreaLevel = getArguments().getInt("showAreaLevel", 2);
			mDeliveryAddress = getArguments().getParcelable(AddressKey);
			if (mDeliveryAddress != null) {
				setDefaultAreaData(mDeliveryAddress.province, mDeliveryAddress.city, "");
				if (!TextUtils.isEmpty(mDeliveryAddress.province))
					mProvinceView.setText(mDeliveryAddress.province);
				if (!TextUtils.isEmpty(mDeliveryAddress.city))
					mCityView.setText(mDeliveryAddress.city);
			}
		}
		showAreaLevel(showAreaLevel);
	}

	private void initCityDataViews() {
		adapter_p.setChooseColorChange(areaModels_p, default_P);
		mClickProvince.setAdapter(adapter_p);

		if (TextUtils.isEmpty(default_C))
			mClickCity.setEnabled(false);

		if (TextUtils.isEmpty(default_D))
			mClickSubdivide.setEnabled(false);

		mClickProvince.setOnItemClickList(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				current_P = (TProvince) parent.getAdapter().getItem(position);
				areaModels_c = areaDBManager.getCList(current_P.pid);
				mProvinceView.setText(current_P.title);
				mCityView.setText("选择城市");
				mSubdivideView.setText("选择区域");

				default_P = current_P.title;
				adapter_p.setChooseColorChange(areaModels_p, default_P);
				adapter_c.setChooseColorChange(areaModels_c, default_C);

				mClickCity.setAdapter(adapter_c);
				mClickCity.setEnabled(true);
			}
		});

		mClickCity.setOnItemClickList(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				current_C = (TCity) parent.getAdapter().getItem(position);
				areaModels_d = areaDBManager.getDList(current_C.cid);
				mCityView.setText(current_C.title);
				mSubdivideView.setText("选择区域");

				default_C = current_C.title;
				adapter_c.setChooseColorChange(areaModels_c, default_C);
				adapter_d.setChooseColorChange(areaModels_d, default_D);

				mClickSubdivide.setAdapter(adapter_d);
				mClickSubdivide.setEnabled(true);
			}
		});

		mClickSubdivide.setOnItemClickList(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				current_D = (TDistrict) parent.getAdapter().getItem(position);

				default_D = current_D.title;
				adapter_d.setChooseColorChange(areaModels_d, default_D);

				mSubdivideView.setText(current_D.title);
			}
		});

		mSave.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				DebugLog.i("test_city", current_P.pid + "--cid:" + current_C.cid + "--sid" + current_D.did);
				mListener.OnAreaChooseInteraction(current_P.title, current_C.title, current_D.title,
						current_P.pid, current_C.cid, current_D.did);
			}
		});
	}

	//初始化省市区文件
	private void initDataFromAssets() {
		areaDBManager = AreaDBManager.getInstance(getActivity().getApplicationContext());
		areaModels_p = areaDBManager.getPList();
		initCityDataViews();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		areaDBManager.destroy();
	}

	public void setAreaChooseListener(OnAreaChooseInteractionListener mListener) {
		this.mListener = mListener;
	}

	@Override
	public String fragmentDescription() {
		return getResources().getString(R.string.fragment_description_city_choose);
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

	//回调
	public interface OnAreaChooseInteractionListener {
		void OnAreaChooseInteraction(String provinces, String city, String district,
		                             String pID, String cID, String dID);
	}
}
