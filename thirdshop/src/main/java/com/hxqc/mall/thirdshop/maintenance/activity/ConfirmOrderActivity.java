//package com.hxqc.mall.thirdshop.maintenance.activity;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.text.TextUtils;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import com.google.gson.reflect.TypeToken;
//import com.hxqc.mall.activity.BackActivity;
//import com.hxqc.mall.core.api.LoadingAnimResponseHandler;
//import com.hxqc.mall.core.api.RequestFailView;
//import com.hxqc.mall.core.controler.UserInfoHelper;
//import com.hxqc.mall.core.model.User;
//import com.hxqc.mall.core.model.auto.PickupPointT;
//import com.hxqc.mall.core.util.ActivitySwitchBase;
//import com.hxqc.mall.core.util.FormatCheck;
//import com.hxqc.mall.core.util.OtherUtil;
//import com.hxqc.mall.core.util.ToastHelper;
//import com.hxqc.mall.thirdshop.R;
//import com.hxqc.mall.thirdshop.maintenance.api.MaintenanceClient;
//import com.hxqc.mall.thirdshop.maintenance.control.MaintenanceHelper;
//import com.hxqc.mall.thirdshop.maintenance.model.maintenance.MaintenanceItem;
//import com.hxqc.mall.thirdshop.maintenance.model.maintenance.UploadObject;
//import com.hxqc.mall.thirdshop.maintenance.model.order.CouponCombination;
//import com.hxqc.mall.thirdshop.maintenance.model.order.CreateOrder;
//import com.hxqc.mall.thirdshop.maintenance.model.order.OrderPrepare;
//import com.hxqc.mall.thirdshop.maintenance.model.order.VerifyOrder;
//import com.hxqc.mall.thirdshop.maintenance.utils.ActivitySwitcherMaintenance;
//import com.hxqc.mall.thirdshop.maintenance.utils.SharedPreferencesHelper;
//import com.hxqc.mall.thirdshop.maintenance.views.ConfirmOrderDetail;
//import com.hxqc.mall.thirdshop.maintenance.views.ReserveDateDialog;
//import com.hxqc.util.DebugLog;
//import com.hxqc.util.JSONUtils;
//
//import cz.msebera.android.httpclient.Header;
//
//import java.util.ArrayList;
//
///**
// * @Author : 钟学东
// * @Since : 2016-03-04
// * FIXME
// * Todo 确认订单页面
// */
//
//public class ConfirmOrderActivity extends BackActivity implements View.OnClickListener, ReserveDateDialog.OnFinishClickListener {
//
//	private EditText mContactView;
//	private EditText mPhoneView;
//	private LinearLayout mLLStoreView;
//	private TextView mStoreView;
//	private LinearLayout mLLTimeView;
//	private TextView mTimeView;
////    private LinearLayout mLLConsultantView;
////    private TextView mConsultantView;
////    private LinearLayout mLLEngineerView;
////    private TextView mEngineerView;
//
//	private TextView mAccessoryView;
//	private TextView mLaborHourView;
//	private TextView mPayableView;
//	private TextView mCouponMoneyDiscountView;
//	private TextView mPackageDiscountView;
//	private TextView mPointsDiscountView;
//	private TextView mPayView;
//
//	private TextView mTotalView;
//	private Button mToMaintainView;
//	private ConfirmOrderDetail confirmOrderDetail;
//
//	private ImageView mScoresCheckView;
//	private RelativeLayout mRlScoresCheckView;
//	private RelativeLayout mRelativeScoresView;
//	private TextView mScoresView;
//	private TextView mScoresDiscountView;
//	private RelativeLayout mRlCouponView;
//	private View mScoresLineView;
//	private View mCouponLineView;
//	private ImageView mCouponCheckView;
//	private RelativeLayout mRlCouponCheckView;
//	private TextView mCouponView;
//	private TextView mCouponDiscountView;
//
//	private LinearLayout mScoresMoneyView;
//	private LinearLayout mPackageMoneyView;
//	private LinearLayout mCouponMoneyView;
//	private View moneyLine1;
//
//	private LinearLayout mParentView;
//	private RequestFailView mFailView;
//
//	private boolean isScoresCheck = false;
//	private boolean isCouponCheck = false;
//
//	private OrderPrepare orderPrepare;
//
//	private ArrayList<MaintenanceItem> maintenanceItems = new ArrayList<>();
//
//	private static int TO_COUPON_CODE = 1;
//	private static int TO_CONSULTANT = 2;
//	private static int TO_ENGINEER = 3;
//	//时间dialog
//	private ReserveDateDialog dateDialog;
//
//
//	private String shopID;
//	private String autoModelID;
//	private UploadObject uploadObject;
//	private String items;
//
//	private String uploadScore;
//	private String uploadCouponID = "";
//	private String apppintmentDate;
//
//	private VerifyOrder verifyOrder;
//
//	private MaintenanceClient maintenanceClient;
//
//	private UserInfoHelper userInfoHelper;
//
//	private MaintenanceHelper maintenanceHelper;
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_confirm_order);
//
//		maintenanceHelper = MaintenanceHelper.getInstance();
//		shopID = maintenanceHelper.getShopID();
//		autoModelID = maintenanceHelper.getAutoModelID();
//		uploadObject = maintenanceHelper.getUploadObject();
//		maintenanceItems = maintenanceHelper.getMaintainItems();
//
//		maintenanceClient = new MaintenanceClient();
//		userInfoHelper = UserInfoHelper.getInstance();
//
//		initView();
//		getDate();
//		initEvent();
//	}
//
//	private void getDate() {
//		items = JSONUtils.toJson(uploadObject);
//
//		DebugLog.i("TAG", "ConfirmOrderActivity" + items.toString());
//		maintenanceClient.prepareOrder(shopID, items, new LoadingAnimResponseHandler(this) {
//			@Override
//			public void onSuccess(String response) {
//				orderPrepare = JSONUtils.fromJson(response, new TypeToken<OrderPrepare>() {
//				});
//				if (orderPrepare != null) {
//					initDate();
//				}
//				mParentView.setVisibility(View.VISIBLE);
//				mFailView.setVisibility(View.GONE);
//			}
//
//			@Override
//			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
//				super.onFailure(statusCode, headers, responseString, throwable);
//				showFailView();
//			}
//		});
//		confirmOrderDetail.seMaintenanceItemtDate(maintenanceItems, shopID);
//	}
//
//	//错误界面的显示
//	private void showFailView() {
//		mParentView.setVisibility(View.GONE);
//		mFailView.setVisibility(View.VISIBLE);
//		mFailView.setEmptyDescription("网络连接失败");
//		mFailView.setFailButtonClick("重新加载", new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				getDate();
//			}
//		});
//		mFailView.setRequestViewType(RequestFailView.RequestViewType.fail);
//	}
//
//	private void initEvent() {
//		mLLStoreView.setOnClickListener(this);
//		mLLTimeView.setOnClickListener(this);
////        mLLConsultantView.setOnClickListener(this);
////        mLLEngineerView.setOnClickListener(this);
//		mRlScoresCheckView.setOnClickListener(this);
//		mRlCouponCheckView.setOnClickListener(this);
//		mRlCouponView.setOnClickListener(this);
//		mToMaintainView.setOnClickListener(this);
//	}
//
//
//	private void initDate() {
//
//		userInfoHelper.getUserInfo(this, new UserInfoHelper.UserInfoAction() {
//			@Override
//			public void showUserInfo(User meData) {
//				mContactView.setText(meData.nickName);
//				mPhoneView.setText(meData.phoneNumber);
//			}
//
//			@Override
//			public void onFinish() {
//
//			}
//		}, false);
//
//
//		mStoreView.setText(orderPrepare.shopPoint.shopName);
//		mTimeView.setText(orderPrepare.recommendApppintmentDate);
//		apppintmentDate = orderPrepare.recommendApppintmentDate;
////        mConsultantView.setText(orderPrepare.serviceAdviser.name);
////        mEngineerView.setText(orderPrepare.mechanic.name);
//
//		mAccessoryView.setText(OtherUtil.amountFormat(orderPrepare.goodsAmount, true));
//		mLaborHourView.setText(OtherUtil.amountFormat(orderPrepare.workCostAmount, true));
//		mPayableView.setText(OtherUtil.amountFormat(orderPrepare.amount, true));
//		mPackageDiscountView.setText(OtherUtil.amountFormat((orderPrepare.discount - orderPrepare.amount), true));
//
//		mCouponMoneyDiscountView.setText(OtherUtil.amountFormat(0, true));
//		mPointsDiscountView.setText(OtherUtil.amountFormat(0, true));
//		mPayView.setText(OtherUtil.amountFormat(orderPrepare.amount, true));
//		mTotalView.setText(OtherUtil.amountFormat(orderPrepare.amount, true));
//
//
//		mScoresView.setText("可使用积分" + orderPrepare.score.count + "分");
//		mScoresDiscountView.setText("抵扣 " + OtherUtil.amountFormat(orderPrepare.score.count * orderPrepare.score.unitPrice, true));
//
//		if (orderPrepare.score.count > 0) {
//			mRelativeScoresView.setVisibility(View.VISIBLE);
//			uploadScore = "" + orderPrepare.score.count;
//			mScoresMoneyView.setVisibility(View.VISIBLE);
//			mScoresLineView.setVisibility(View.VISIBLE);
//			isScoresCheck = true;
//			mScoresCheckView.setImageResource(R.drawable.checkbox_selected);
//
//		} else {
//			uploadScore = 0 + "";
//			mRelativeScoresView.setVisibility(View.GONE);
//			mScoresMoneyView.setVisibility(View.GONE);
//			mScoresLineView.setVisibility(View.GONE);
//		}
//
//
//		if (orderPrepare.couponCombination.size() > 0) {
//			isCouponCheck = true;
//			mCouponLineView.setVisibility(View.VISIBLE);
//			mRlCouponView.setVisibility(View.VISIBLE);
//
//			mCouponCheckView.setImageResource(R.drawable.checkbox_selected);
//
//			mCouponDiscountView.setText(String.format("抵扣 %s", OtherUtil.amountFormat(orderPrepare.couponCombination.get(0).discountAmount, true)));
//			mCouponView.setText(getCouponName(orderPrepare.couponCombination.get(0)));
//
//			for (int i = 0; i < orderPrepare.couponCombination.get(0).couponIDs.size(); i++) {
//				uploadCouponID = uploadCouponID + orderPrepare.couponCombination.get(0).couponIDs.get(i) + ",";
//			}
//			uploadCouponID = uploadCouponID.substring(0, uploadCouponID.length() - 1);
//
//		} else {
//			mRlCouponView.setVisibility(View.GONE);
//			mCouponLineView.setVisibility(View.GONE);
//			mCouponMoneyView.setVisibility(View.GONE);
//			uploadCouponID = "";
//		}
//
//		if ((orderPrepare.amount - orderPrepare.discount) == 0) {
//			mPackageMoneyView.setVisibility(View.GONE);
//		}
//
//		if (orderPrepare.score.count == 0 && (orderPrepare.amount - orderPrepare.discount) == 0) {
//			moneyLine1.setVisibility(View.GONE);
//		}
//
//		verifyOrder();
//	}
//
//
//	//确认订单价格
//	private void verifyOrder() {
//		maintenanceClient.verifyOrder(shopID, uploadScore, uploadCouponID, items, new LoadingAnimResponseHandler(this) {
//			@Override
//			public void onSuccess(String response) {
//				verifyOrder = JSONUtils.fromJson(response, new TypeToken<VerifyOrder>() {
//				});
//
//				mAccessoryView.setText(OtherUtil.amountFormat(verifyOrder.goodsAmount, true));
//				mLaborHourView.setText(OtherUtil.amountFormat(verifyOrder.workCostAmount, true));
//				mPayableView.setText(OtherUtil.amountFormat(verifyOrder.amount, true));
//
//				mPointsDiscountView.setText(OtherUtil.amountFormat(-(verifyOrder.scoreAmount), true));
//				mCouponMoneyDiscountView.setText(OtherUtil.amountFormat(-(verifyOrder.couponAmount), true));
//
//				mPayView.setText(OtherUtil.amountFormat(verifyOrder.discount - verifyOrder.scoreAmount - verifyOrder.couponAmount, true));
//				mTotalView.setText(OtherUtil.amountFormat(verifyOrder.discount - verifyOrder.scoreAmount - verifyOrder.couponAmount, true));
//			}
//		});
//	}
//
//	private void initView() {
//		mContactView = (EditText) findViewById(R.id.et_contact);
//		mPhoneView = (EditText) findViewById(R.id.et_phone);
//		mLLStoreView = (LinearLayout) findViewById(R.id.ll_store);
//		mStoreView = (TextView) findViewById(R.id.tv_store);
//		mLLTimeView = (LinearLayout) findViewById(R.id.ll_time);
//		mTimeView = (TextView) findViewById(R.id.tv_time);
////        mLLConsultantView = (LinearLayout) findViewById(R.id.ll_consultant);
////        mConsultantView = (TextView) findViewById(R.id.tv_consultant);
////        mLLEngineerView = (LinearLayout) findViewById(R.id.ll_engineer);
////        mEngineerView = (TextView) findViewById(R.id.tv_engineer);
//
//		mAccessoryView = (TextView) findViewById(R.id.accessory);
//		mLaborHourView = (TextView) findViewById(R.id.labor_hour);
//		mPayableView = (TextView) findViewById(R.id.payable);
//		mCouponMoneyDiscountView = (TextView) findViewById(R.id.coupon_discount_money);
//		mPackageDiscountView = (TextView) findViewById(R.id.package_discount);
//		mPointsDiscountView = (TextView) findViewById(R.id.points_discount);
//		mPayView = (TextView) findViewById(R.id.pay);
//
//		mTotalView = (TextView) findViewById(R.id.total_pay);
//		mToMaintainView = (Button) findViewById(R.id.to_maintain);
//		mToMaintainView.setText("提交订单");
//
//		confirmOrderDetail = (ConfirmOrderDetail) findViewById(R.id.confirm_order);
//
//		mScoresCheckView = (ImageView) findViewById(R.id.iv_scores);
//		mRlScoresCheckView = (RelativeLayout) findViewById(R.id.rl_scores);
//		mRelativeScoresView = (RelativeLayout) findViewById(R.id.relative_scores);
//		mScoresView = (TextView) findViewById(R.id.tv_scores);
//		mScoresDiscountView = (TextView) findViewById(R.id.scores_discount);
//
//		mRlCouponView = (RelativeLayout) findViewById(R.id.rl_coupon);
//		mCouponCheckView = (ImageView) findViewById(R.id.iv_coupon);
//		mRlCouponCheckView = (RelativeLayout) findViewById(R.id.rl_coupon_check);
//		mCouponView = (TextView) findViewById(R.id.tv_coupon);
//		mScoresLineView = findViewById(R.id.scores_line);
//		mCouponLineView = findViewById(R.id.coupon_line);
//		mCouponDiscountView = (TextView) findViewById(R.id.coupon_discount);
//
//		mScoresMoneyView = (LinearLayout) findViewById(R.id.scores_money);
//		mPackageMoneyView = (LinearLayout) findViewById(R.id.package_money);
//		mCouponMoneyView = (LinearLayout) findViewById(R.id.coupon_money);
//		moneyLine1 = findViewById(R.id.line1);
//
//		mParentView = (LinearLayout) findViewById(R.id.ll_parent);
//		mFailView = (RequestFailView) findViewById(R.id.fail_view);
//	}
//
//	//获取优惠券名称
//	private String getCouponName(CouponCombination couponCombination) {
//		String couponName = "";
//		for (int i = 0; i < orderPrepare.coupon.size(); i++) {
//			for (int j = 0; j < couponCombination.couponIDs.size(); j++) {
//				if (couponCombination.couponIDs.get(j).equals(orderPrepare.coupon.get(i))) {
//					couponName = couponName + orderPrepare.coupon.get(i);
//				}
//			}
//		}
//
//		return couponName.substring(0, uploadCouponID.length() - 1);
//	}
//
//	@Override
//	public void onClick(View view) {
//		int i = view.getId();
//		if (i == R.id.ll_store) {
//			PickupPointT pickupPointT = new PickupPointT();
//			pickupPointT.address = orderPrepare.shopPoint.address;
//			pickupPointT.latitude = orderPrepare.shopPoint.latitude + "";
//			pickupPointT.longitude = orderPrepare.shopPoint.longitude + "";
//			pickupPointT.tel = orderPrepare.shopPoint.tel;
//
//			ActivitySwitchBase.toAMapNvai(this, pickupPointT);
//
//		} else if (i == R.id.ll_time) {
//			dateDialog = new ReserveDateDialog(this, orderPrepare.apppintmentDate);
//			dateDialog.setOnFinishClickListener(this);
//			dateDialog.build();
//			dateDialog.create();
//
//		}
////        else if (i == R.id.ll_consultant) {
////
////            ActivitySwitcherMaintenance.toServiceAdviserForResult(ConfirmOrderActivity.this,shopID,TO_CONSULTANT);
////
////        } else if (i == R.id.ll_engineer) {
////
////            ActivitySwitcherMaintenance.toServiceMechanicForResult(ConfirmOrderActivity.this, shopID, TO_ENGINEER);
////
////        }
//		else if (i == R.id.rl_scores) {
//			if (isScoresCheck) {
//				isScoresCheck = false;
//				mScoresCheckView.setImageResource(R.drawable.checkbox_selected_disabled);
//				uploadScore = 0 + "";
//
//
//				mPointsDiscountView.setText(OtherUtil.amountFormat(0, true));
//			} else {
//				isScoresCheck = true;
//				mScoresCheckView.setImageResource(R.drawable.checkbox_selected);
//				uploadScore = "" + orderPrepare.score.count;
//
//			}
//			verifyOrder();
//
//		} else if (i == R.id.rl_coupon_check) {
//			if (isCouponCheck) {
//				isCouponCheck = false;
//				mCouponCheckView.setImageResource(R.drawable.checkbox_selected_disabled);
//				uploadCouponID = "";
//				mCouponMoneyDiscountView.setText(OtherUtil.amountFormat(0, true));
//				verifyOrder();
//			} else {
//				if (orderPrepare.couponCombination.size() > 0) {
//					isCouponCheck = true;
//					mCouponCheckView.setImageResource(R.drawable.checkbox_selected);
//					for (int j = 0; j < orderPrepare.couponCombination.get(0).couponIDs.size(); j++) {
//						uploadCouponID = uploadCouponID + orderPrepare.couponCombination.get(0).couponIDs.get(j) + ",";
//					}
//					uploadCouponID = uploadCouponID.substring(0, uploadCouponID.length() - 1);
//
//					verifyOrder();
//				} else {
//					ToastHelper.showYellowToast(this, "没有可使用的优惠券");
//				}
//			}
//
//
//		} else if (i == R.id.rl_coupon) {
//			ActivitySwitcherMaintenance.toMyCouponForResult(ConfirmOrderActivity.this, orderPrepare.couponCombination, TO_COUPON_CODE);
//
//		} else if (i == R.id.to_maintain) {
//
//			ToMaintain();
//		}
//	}
//
//	private void ToMaintain() {
//		String name = mContactView.getText().toString().trim();
//		String phone = mPhoneView.getText().toString().trim();
//		String serviceAdviserID = orderPrepare.serviceAdviser.serviceAdviserID;
//		String mechanicID = orderPrepare.mechanic.mechanicID;
//
//
//		int phoneCode = FormatCheck.checkPhoneNumber(mPhoneView.getText().toString().trim(), this);
//
//		if (!FormatCheck.checkName(mContactView.getText().toString().trim(), this)) {
//			return;
//		}
//
//		if (phoneCode == 8) {
//			return;
//		} else if (phoneCode == 10) {
//			return;
//		}
//
//		if (TextUtils.isEmpty(apppintmentDate)) {
//			ToastHelper.showYellowToast(this, "请选择保养时间");
//			return;
//		}
//
//		if (TextUtils.isEmpty(serviceAdviserID)) {
//			ToastHelper.showYellowToast(this, "请选择服务顾问");
//			return;
//		}
//
//		if (TextUtils.isEmpty(mechanicID)) {
//			ToastHelper.showYellowToast(this, "请选择维修技师");
//			return;
//		}
//
//		maintenanceClient.createdOrder(shopID, name, phone, orderPrepare.serviceAdviser.serviceAdviserID, orderPrepare.mechanic.mechanicID, apppintmentDate, uploadScore,
//				uploadCouponID, items, new LoadingAnimResponseHandler(this) {
//					@Override
//					public void onSuccess(String response) {
//						new SharedPreferencesHelper(ConfirmOrderActivity.this).setOrderChange(true);
//						CreateOrder createOrder = JSONUtils.fromJson(response, new TypeToken<CreateOrder>() {
//						});
//						createOrder.amount = verifyOrder.discount - verifyOrder.scoreAmount - verifyOrder.couponAmount;
//						ActivitySwitcherMaintenance.toPayChoice(ConfirmOrderActivity.this, createOrder, shopID, "1");
//					}
//				});
//	}
//
//	@Override
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		super.onActivityResult(requestCode, resultCode, data);
//		if (requestCode == TO_COUPON_CODE) {
//			if (data != null) {
//				CouponCombination couponCombination = data.getParcelableExtra("couponCombination");
//
//				if (couponCombination != null) {
//					for (int i = 0; i < couponCombination.couponIDs.size(); i++) {
//						uploadCouponID = uploadCouponID + couponCombination.couponIDs.get(i) + ",";
//					}
//					uploadCouponID = uploadCouponID.substring(0, uploadCouponID.length() - 1);
//
//					mCouponDiscountView.setVisibility(View.VISIBLE);
//					mCouponDiscountView.setText(String.format("抵扣 %s", OtherUtil.amountFormat(couponCombination.discountAmount, true)));
//
//					if (isCouponCheck) {
//						verifyOrder();
//					}
//				}
//			}
//		}
//// else if(requestCode == TO_CONSULTANT){
////            if(data != null){
////                orderPrepare.serviceAdviser = data.getParcelableExtra("serviceAdviser");
////                if(orderPrepare.serviceAdviser != null)
//////                    mConsultantView.setText(orderPrepare.serviceAdviser.name);
////            }
////
////        }else if(requestCode == TO_ENGINEER){
////            if(data != null){
////                orderPrepare.mechanic = data.getParcelableExtra("mechanic");
////                if(orderPrepare.mechanic != null)
//////                    mEngineerView.setText(orderPrepare.mechanic.name);
////            }
////
////        }
//	}
//
//	@Override
//	public void onFinishClick(View v, String dateAndTime) {
//		apppintmentDate = dateAndTime;
//		mTimeView.setText(dateAndTime);
//	}
//}
