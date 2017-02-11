package com.hxqc.aroundservice.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.hxqc.mall.core.db.area.AreaDBManager;
import com.hxqc.mall.core.db.area.TCity;
import com.hxqc.mall.core.db.area.TDistrict;
import com.hxqc.mall.core.db.area.TProvince;
import com.hxqc.mall.core.fragment.FunctionFragment;
import com.hxqc.mall.core.util.ToastHelper;
import com.hxqc.mall.core.views.materialedittext.MaterialEditText;
import com.hxqc.mall.usedcar.R;
import com.hxqc.mall.usedcar.adapter.CityAdapter;
import com.hxqc.mall.usedcar.adapter.ProvinceAdapter;
import com.hxqc.mall.usedcar.adapter.RegionAdapter;

import java.util.ArrayList;

/**
 * 选省市区
 * Created by huangyi on 16/4/29.
 */
@Deprecated
public class SelectAddressFragment extends FunctionFragment implements View.OnClickListener {

	MaterialEditText mProvinceView;
	MaterialEditText mCityView;
	MaterialEditText mRegionView;

	PopupWindow popupWindow;
	//pop 窗体的高度
	int mPopHeight = 550;

	ListView listView;

//    ArrayList<AreaModel> areaModels_p; //省
//    ArrayList<CityModel> areaModels_c; //市
//    ArrayList<RegionModel> areaModels_s; //区

//    AreaModel current_P;  //当前选中的省
//    CityModel current_C;  //当前选中的市
//    RegionModel current_S;  //当前选中的区

	TProvince current_P;  //当前选中的省
	TCity current_C;  //当前选中的市
	TDistrict current_S;  //当前选中的区

	ProvinceAdapter provinceAdapter;
	ArrayList<TProvince> pList;

	CityAdapter cityAdapter;
	ArrayList<TCity> areaModels_c;

	RegionAdapter regionAdapter;
	ArrayList<TDistrict> areaModels_s; //区

	View popView;
	OnSubmitListener mOnSubmitListener;
	AreaDBManager instance;

	public void setOnSubmitListener(OnSubmitListener mOnSubmitListener) {
		this.mOnSubmitListener = mOnSubmitListener;
	}

	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_look_car, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		//初始数据
		instance = AreaDBManager.getInstance(mContext);
		pList = instance.getPList(); //省级列表

//        for (TProvince a : pList) {
//            AreaModel areaModel = new AreaModel();
//            areaModel.p_name = a.title;
//
//            ArrayList<CityModel> city = new ArrayList<>();
//            ArrayList<com.hxqc.mall.core.model.AreaModel> cList = instance.getCList(a.areaID + ""); //市级列表
//
//            for (com.hxqc.mall.core.model.AreaModel b : cList) {
//                CityModel cityModel = new CityModel();
//                cityModel.c_name = b.title;
//                ArrayList<RegionModel> region = new ArrayList<>();
//                ArrayList<com.hxqc.mall.core.model.AreaModel> dList = instance.getDList(b.areaID + ""); //区级列表
//
//                for (com.hxqc.mall.core.model.AreaModel c : dList) {
//                    RegionModel regionModel = new RegionModel();
//                    regionModel.r_name = c.title;
//                    region.add(regionModel);
//                }
//                cityModel.region = region;
//                city.add(cityModel);
//            }
//            areaModel.city = city;
//            areaModels_p.add(areaModel);
//        }
//        instance.destroy();

		view.findViewById(R.id.title_parent).setVisibility(View.GONE);
		mProvinceView = (MaterialEditText) view.findViewById(R.id.province);
		mCityView = (MaterialEditText) view.findViewById(R.id.city);
		mRegionView = (MaterialEditText) view.findViewById(R.id.region);
		popView = View.inflate(getActivity(), R.layout.view_color_popwin, null);
		listView = (ListView) popView.findViewById(R.id.lv_get_Area);
		popupWindow = new PopupWindow(popView);

		mProvinceView.setOnClickListener(this);
		view.findViewById(R.id.bt_submit).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		int i = v.getId();
		if (i == R.id.province) {
			showPop(mProvinceView, "1");

		} else if (i == R.id.city) {
			showPop(mCityView, "2");

		} else if (i == R.id.region) {
			showPop(mRegionView, "3");

		} else if (i == R.id.bt_submit) {
			if (mProvinceView.getText().toString().trim().equals("")) {
				ToastHelper.showYellowToast(getActivity(), "请选择省");
				return;
			}

			if (mCityView.getText().toString().trim().equals("")) {
				ToastHelper.showYellowToast(getActivity(), "请选择市");
				return;
			}

			if (mRegionView.getText().toString().trim().equals("")) {
				ToastHelper.showYellowToast(getActivity(), "请选择区");
				return;
			}

			if (null != mOnSubmitListener)
				mOnSubmitListener.onSubmit(mProvinceView.getText().toString().trim(), mCityView.getText().toString().trim(), mRegionView.getText().toString().trim());
		}
	}

	private void showPop(View view, String flag) {
//		popupWindow.setWidth(view.getWidth() + 24);
//		popupWindow.setHeight(RelativeLayout.LayoutParams.WRAP_CONTENT);
//		popupWindow.setBackgroundDrawable(new BitmapDrawable());
//
//		if (flag.equals("1")) {
//			provinceAdapter = new ProvinceAdapter(getActivity(), pList, mProvinceView);
//			listView.setAdapter(provinceAdapter);
//			if (provinceAdapter.getCount() >= 4) popupWindow.setHeight(mPopHeight);
//			listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//				@Override
//				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//					current_P = provinceAdapter.getItem(position);
//
//					areaModels_c = instance.getCList(current_P.pid);
//
//					mProvinceView.setText(current_P.title);
//					mCityView.setText("");
//					mRegionView.setText("");
//					mCityView.setOnClickListener(SelectAddressFragment.this);
//					mRegionView.setOnClickListener(null);
//					popupWindow.dismiss();
//				}
//			});
//		}
//
//		if (flag.equals("2")) {
//			cityAdapter = new CityAdapter(getActivity(), areaModels_c, mCityView);
//			listView.setAdapter(cityAdapter);
//			if (cityAdapter.getCount() >= 4) popupWindow.setHeight(mPopHeight);
//			listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//				@Override
//				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//					current_C = cityAdapter.getItem(position);
//					areaModels_s = instance.getDList(current_C.cid);
//					mCityView.setText(current_C.title);
//					mRegionView.setText("");
//					mRegionView.setOnClickListener(SelectAddressFragment.this);
//					popupWindow.dismiss();
//				}
//			});
//		}
//
//		if (flag.equals("3")) {
//			regionAdapter = new RegionAdapter(getActivity(), areaModels_s, mRegionView);
//			listView.setAdapter(regionAdapter);
//			if (regionAdapter.getCount() >= 4) popupWindow.setHeight(mPopHeight);
//			listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//				@Override
//				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//					current_S = regionAdapter.getItem(position);
//					mRegionView.setText(current_S.title);
//					popupWindow.dismiss();
//				}
//			});
//		}
//
//		if (popupWindow.isShowing()) return;
//		popupWindow.showAsDropDown(view, -12, -view.getHeight());
//		popupWindow.setFocusable(true);
//		popupWindow.setOutsideTouchable(true);
//		popupWindow.update();
	}

	@Override
	public String fragmentDescription() {
		return "选省市区";
	}

	public interface OnSubmitListener {
		void onSubmit(String province, String city, String region);
	}

}
