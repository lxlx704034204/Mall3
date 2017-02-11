package com.hxqc.mall.thirdshop.maintenance.views;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hxqc.mall.core.util.OtherUtil;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.maintenance.adapter.FourSShopMaintainAdapter;
import com.hxqc.mall.thirdshop.maintenance.adapter.MaintainItemDecoration;
import com.hxqc.mall.thirdshop.maintenance.control.FourSAndQuickHelper;
import com.hxqc.mall.thirdshop.maintenance.model.maintenance.MaintenanceGoods;
import com.hxqc.mall.thirdshop.maintenance.model.maintenance.MaintenanceItemN;
import com.hxqc.mall.thirdshop.maintenance.model.maintenance.MaintenanceReplaceGoods;
import com.hxqc.util.DebugLog;

import java.util.ArrayList;

/**
 * @Author : 钟学东
 * @Since : 2016-08-03
 * FIXME
 * Todo 4s店 保养的子view
 */
public class FourSShopMaintenanceFirstChild extends LinearLayout implements FourSShopMaintainAdapter.ReturnGoodsListener {

	private Context context;
	private ImageView mSelectView;
	private TextView mItemNameView; //项目名
    private TextView mGoodsNameView; //物品名
	private TextView mGoodsPriceView; //物品价格
	private TextView mWorkPriceView; //工时费
	private TextView mTextOneView; // type 材料费
	private RelativeLayout mRlGoodsView;
	private RelativeLayout mOneGoodsLayout;//单一商品layout
	private RecyclerView mRecyclerView;
	private FourSShopMaintainAdapter adapter;

	private MaintenanceGoods maintenanceGoods;
	private MaintenanceItemN maintenanceItemN;

	private FourSAndQuickHelper fourSAndQuickHelper;

	private int position;

	public interface CalculateMoneyListener {
		void CalculateMoney(MaintenanceItemN maintenanceItemN, int position);
	}

	public void setCaulateMoenyListener(CalculateMoneyListener caulateMoenyLisnter) {
		this.calculateMoneyListener = caulateMoenyLisnter;
	}

	public interface BothGroupListener {
		void bothGroup();
	}

	public void setBothGroupListener(BothGroupListener bothGroupListener) {
		this.bothGroupListener = bothGroupListener;
	}

	private BothGroupListener bothGroupListener;

	private CalculateMoneyListener calculateMoneyListener;

	private ArrayList<MaintenanceReplaceGoods> maintenanceReplaceGoodses = new ArrayList<>();

	public FourSShopMaintenanceFirstChild(Context context) {
		super(context);
		this.context = context;
		fourSAndQuickHelper = FourSAndQuickHelper.getInstance();
	}

	public FourSShopMaintenanceFirstChild(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		fourSAndQuickHelper = FourSAndQuickHelper.getInstance();
		LayoutInflater.from(context).inflate(R.layout.item_four_s_shop_first_child, this);
		initView();
		initEvent();
	}


	private void initView() {
		mSelectView = (ImageView) findViewById(R.id.iv_select);
		mItemNameView = (TextView) findViewById(R.id.item_name);
		mGoodsPriceView = (TextView) findViewById(R.id.goods_price);
		mRlGoodsView = (RelativeLayout) findViewById(R.id.goods);
		mWorkPriceView = (TextView) findViewById(R.id.work_price);
		mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
		mRecyclerView.addItemDecoration(new MaintainItemDecoration(0, 0, 16, 0));
		mOneGoodsLayout = (RelativeLayout) findViewById(R.id.one_goods_layout);
		mGoodsNameView = (TextView) findViewById(R.id.one_goods_name);
	}


	public void initDate(MaintenanceItemN maintenanceItemN, int position) {
		this.position = position;
		this.maintenanceItemN = maintenanceItemN;
		maintenanceGoods = maintenanceItemN.goods.get(0);
		mItemNameView.setText(maintenanceItemN.name);

		Double tempWorkCost = Double.valueOf(maintenanceItemN.workCost);

		if (tempWorkCost < 0.01) {
			mWorkPriceView.setText("免费");
		} else {
			mWorkPriceView.setText(OtherUtil.amountFormat(maintenanceItemN.workCost, true));
		}

//		if (maintenanceItemN.selectGoodPrice == 0) {
//			mGoodsPriceView.setVisibility(View.GONE);
//		} else {
//			mGoodsPriceView.setText(OtherUtil.amountFormat(maintenanceItemN.selectGoodPrice, true));
//		}

		if (maintenanceGoods.replaceable == null || maintenanceGoods.replaceable.size() <= 0) {
			mRlGoodsView.setVisibility(View.GONE);
			if (maintenanceGoods.price < 0.01) {
				mOneGoodsLayout.setVisibility(View.GONE);
			} else {
				mOneGoodsLayout.setVisibility(View.VISIBLE);
				mGoodsPriceView.setText(OtherUtil.amountFormat(maintenanceItemN.selectGoodPrice, true));
			}
			mGoodsNameView.setText(maintenanceGoods.name);
		} else {
			mOneGoodsLayout.setVisibility(View.GONE);
			mRlGoodsView.setVisibility(View.VISIBLE);
			LinearLayoutManager layoutManager = new LinearLayoutManager(context);
			layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
			mRecyclerView.setLayoutManager(layoutManager);

			initGoods();

			adapter = new FourSShopMaintainAdapter(context, maintenanceReplaceGoodses, maintenanceItemN.isCheck);
			adapter.setReturnGoodsListener(this);
			mRecyclerView.setAdapter(adapter);
		}
		if (maintenanceItemN.isCheck) {
			mSelectView.setImageResource(R.drawable.maintain_for_4s_check);
		} else {
			mSelectView.setImageResource(R.drawable.maintain_for_4s_no_check);
		}

	}


	private void initGoods() {
		MaintenanceReplaceGoods tempGoods = new MaintenanceReplaceGoods();
		tempGoods.count = maintenanceGoods.count;
		tempGoods.name = maintenanceGoods.name;
		tempGoods.goodsID = maintenanceGoods.goodsID;
		tempGoods.thumb = maintenanceGoods.thumb;
		if (maintenanceItemN.isCheck) {
			tempGoods.isSelect = true;
		}
		tempGoods.price = maintenanceGoods.price;
		maintenanceReplaceGoodses.clear();
		maintenanceReplaceGoodses.add(tempGoods);
		maintenanceReplaceGoodses.addAll(maintenanceGoods.replaceable);
	}

	private void setGoodsState() {
		for (MaintenanceReplaceGoods replaceGoods : maintenanceReplaceGoodses) {
			replaceGoods.isSelect = false;
		}
		if (adapter != null)
			adapter.notifyDataSetChanged();
	}

	private void initEvent() {
		mSelectView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				DebugLog.i("TAG", maintenanceItemN.deleteable + "");
				if (maintenanceItemN.deleteable == 1) {
					if (maintenanceItemN.isCheck) {
						maintenanceItemN.isCheck = false;
						mSelectView.setImageResource(R.drawable.maintain_for_4s_no_check);
						setGoodsState();
					} else {
						maintenanceItemN.isCheck = true;
						mSelectView.setImageResource(R.drawable.maintain_for_4s_check);
//						fourSAndQuickHelper.getMutexItemsFor4S(maintenanceItemN);
						initGoods();
					}
//					fourSAndQuickHelper.getBothItemsFor4s(maintenanceItemN);
					if (bothGroupListener != null) {
						bothGroupListener.bothGroup();
					}

					if (calculateMoneyListener != null) {
						calculateMoneyListener.CalculateMoney(maintenanceItemN, position);
					}
				} else {
					AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.MaterialDialog);
					builder.setMessage(maintenanceItemN.name + "为必选").setPositiveButton("确定", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {

						}
					}).create().show();
				}
			}
		});
	}

	@Override
	public void returnGoods(MaintenanceReplaceGoods maintenanceReplaceGoods) {

//        maintenanceItemN.selectGoodID = maintenanceReplaceGoods.goodsID;
//        maintenanceItemN.selectCount = maintenanceReplaceGoods.count;


		float amount = (maintenanceReplaceGoods.price * maintenanceReplaceGoods.count);
		String upGoodID = maintenanceReplaceGoods.goodsID + "_" + maintenanceReplaceGoods.count;
		maintenanceItemN.selectGoodID.clear();
		maintenanceItemN.selectGoodID.add(upGoodID);
		for (int i = 0; i < maintenanceItemN.goods.size(); i++) {
			if (i != 0) {
				amount = amount + (maintenanceItemN.goods.get(i).price * maintenanceItemN.goods.get(i).count);
				maintenanceItemN.selectGoodID.add(maintenanceItemN.goods.get(i).goodsID + "_" + maintenanceItemN.goods.get(i).count);
			}
		}

		maintenanceItemN.selectGoodPrice = amount;
		mGoodsPriceView.setText(OtherUtil.amountFormat(amount, true));
		DebugLog.i("TAG", "returnGoods" + maintenanceItemN.selectGoodPrice);
		if (calculateMoneyListener != null) {
			calculateMoneyListener.CalculateMoney(maintenanceItemN, position);
		}
	}
}
