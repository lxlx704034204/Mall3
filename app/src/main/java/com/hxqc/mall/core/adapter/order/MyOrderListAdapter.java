package com.hxqc.mall.core.adapter.order;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hxqc.aroundservice.config.OrderDetailContants;
import com.hxqc.aroundservice.util.ActivitySwitchAround;
import com.hxqc.fastreqair.util.CarWashActivitySwitcher;
import com.hxqc.mall.auto.config.AutoInfoContants;
import com.hxqc.mall.core.api.LoadingAnimResponseHandler;
import com.hxqc.mall.core.model.order.AccessoryBean;
import com.hxqc.mall.core.model.order.AnnualnspectionBean;
import com.hxqc.mall.core.model.order.BaseOrder;
import com.hxqc.mall.core.model.order.BaseOrderStatus;
import com.hxqc.mall.core.model.order.CarWashBean;
import com.hxqc.mall.core.model.order.ChangeLicenceBean;
import com.hxqc.mall.core.model.order.MaintenanceBean;
import com.hxqc.mall.core.model.order.OrderListBean;
import com.hxqc.mall.core.model.order.OrderModel;
import com.hxqc.mall.core.model.order.RepairBean;
import com.hxqc.mall.core.model.order.ShopSeckillAutoBean;
import com.hxqc.mall.core.model.order.WzServiceBean;
import com.hxqc.mall.core.util.ActivitySwitchBase;
import com.hxqc.mall.core.util.ImageUtil;
import com.hxqc.mall.core.util.OtherUtil;
import com.hxqc.mall.core.util.activityutil.ActivitySwitcher;
import com.hxqc.mall.core.views.Order.OrderDescription;
import com.hxqc.mall.core.views.dialog.NormalDialog;
import com.hxqc.mall.payment.util.PaymentActivitySwitch;
import com.hxqc.mall.thirdshop.accessory.api.AccessoryApiClient;
import com.hxqc.mall.thirdshop.accessory.model.SubmitOrderInfo;
import com.hxqc.mall.thirdshop.accessory.utils.ActivitySwitcherAccessory;
import com.hxqc.mall.thirdshop.accessory4s.api.Accessory4SApiClient;
import com.hxqc.mall.thirdshop.accessory4s.model.SubmitOrderInfo4S;
import com.hxqc.mall.thirdshop.accessory4s.utils.ActivitySwitcherAccessory4S;
import com.hxqc.mall.thirdshop.maintenance.api.MaintenanceClient;
import com.hxqc.mall.thirdshop.maintenance.model.order.CreateOrder;
import com.hxqc.mall.thirdshop.maintenance.utils.ActivitySwitcherMaintenance;
import com.hxqc.mall.thirdshop.model.ThirdOrderModel;
import com.hxqc.mall.thirdshop.utils.ActivitySwitcherThirdPartShop;
import com.hxqc.mall.views.order.OrderBottom;
import com.hxqc.mall.views.order.OrderDescriptionForMyOrder;
import com.hxqc.util.DebugLog;
import com.hxqc.widget.ListViewNoSlide;

import java.util.ArrayList;

import hxqc.mall.R;


/**
 * liaoguilong
 * Created by CPR113 on 2016/3/2.
 * 订单列表Adapter
 */
public class MyOrderListAdapter extends RecyclerView.Adapter {

    private final int ORDERTYPE_MAINTENANCE_FASTREPAIR = 0;//快修店保养
    private final int ORDERTYPE_MAINTENANCE_4SSHOP = 10;//4s店保养
    private final int ORDERTYPE_ACCESSORY = 20;//用品-
    private final int ORDERTYPE_NEWAUTO = 30;//新车自营
    private final int ORDERTYPE_REPAIR = 40;//维修
    private final int ORDERTYPE_SHOPPORMOTION = 50;//门店活动
    private final int ORDERTYPE_WZSERVICE = 60;//违章处理
    private final int ORDERTYPE_CHANGELICENCE = 70;//驾驶证换证
    private final int ORDERTYPE_ANNUALNSPECTION = 80;//年检服务
    private final int ORDERTYPE_SHOPSECKILLAUTO = 90;//4s店特价车
    private final int ORDERTYPE_CARWASH = 100;//洗车
    private final int ORDERTYPE_NEWENERG = 110;//新能源
    private final int ORDERTYPE_ACCESSORY_4SSHOP = 120;//4s店用品

    private ArrayList<OrderListBean> mOrderList;
    private Context mContext;
    private LayoutInflater mLayoutInflater;

    public void setOnAdapterChangeListener(OnAdapterChangeListener mOnAdapterChangeListener) {
        this.mOnAdapterChangeListener = mOnAdapterChangeListener;
    }

    private OnAdapterChangeListener mOnAdapterChangeListener;


    public MyOrderListAdapter(ArrayList<OrderListBean> mOrderList, Context mContext) {
        this.mOrderList = mOrderList;
        this.mContext = mContext;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getItemViewType(int position) {
        OrderListBean mOrderListBean = mOrderList.get(position);
        if (mOrderListBean.maintenance4SShop != null) {
            return ORDERTYPE_MAINTENANCE_4SSHOP;
        } else if (mOrderListBean.maintenanceFastrepair != null) {
            return ORDERTYPE_MAINTENANCE_FASTREPAIR;
        } else if (mOrderListBean.accessory != null) {
            return ORDERTYPE_ACCESSORY;
        } else if (mOrderListBean.newAuto != null) {
            return ORDERTYPE_NEWAUTO;
        } else if (mOrderListBean.repair != null) {
            return ORDERTYPE_REPAIR;
        } else if (mOrderListBean.shopPormotion != null) {
            return ORDERTYPE_SHOPPORMOTION;
        } else if (mOrderListBean.wzService != null) {
            return ORDERTYPE_WZSERVICE;
        } else if (mOrderListBean.changeLicence != null) {
            return ORDERTYPE_CHANGELICENCE;
        } else if (mOrderListBean.annualnspection != null) {
            return ORDERTYPE_ANNUALNSPECTION;
        } else if (mOrderListBean.shopSeckillAuto != null) {
            return ORDERTYPE_SHOPSECKILLAUTO;
        } else if (mOrderListBean.carWash != null) {
            return ORDERTYPE_CARWASH;
        } else if (mOrderListBean.newEnerg != null) {
            return ORDERTYPE_NEWENERG;
        } else if (mOrderListBean.accessory4SShop != null) {
            return ORDERTYPE_ACCESSORY_4SSHOP;
        }
        return -99;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mView;
        if (viewType == ORDERTYPE_MAINTENANCE_4SSHOP || viewType == ORDERTYPE_MAINTENANCE_FASTREPAIR) {//4s保养，修车保养
            mView = mLayoutInflater.inflate(R.layout.activity_my_order_maintain, parent, false);
            return new MaintenanceViewHolder(mView);
        } else if (viewType == ORDERTYPE_ACCESSORY || viewType == ORDERTYPE_ACCESSORY_4SSHOP) {//用品,4S点永平
            mView = mLayoutInflater.inflate(R.layout.activity_my_order_accessory, parent, false);
            return new AccessoryViewHolder(mView);
        } else if (viewType == ORDERTYPE_NEWAUTO || viewType == ORDERTYPE_NEWENERG) {//新车自营,新能源
            mView = mLayoutInflater.inflate(R.layout.activity_my_order_ebs, parent, false);
            return new NewAutoViewHolder(mView);
        } else if (viewType == ORDERTYPE_REPAIR) {//维修
            mView = mLayoutInflater.inflate(R.layout.activity_my_order_bookingcar, parent, false);
            return new RepairViewHolder(mView);
        } else if (viewType == ORDERTYPE_SHOPPORMOTION) {//门店活动
            mView = mLayoutInflater.inflate(R.layout.activity_my_order_online4s, parent, false);
            return new ShopPormotionViewHolder(mView);
        } else if (viewType == ORDERTYPE_WZSERVICE) {//违章处理
            mView = mLayoutInflater.inflate(R.layout.activity_my_order_wzservice, parent, false);
            return new WZServiceViewHolder(mView);
        } else if (viewType == ORDERTYPE_CHANGELICENCE) {//驾驶证换证
            mView = mLayoutInflater.inflate(R.layout.activity_my_order_changelicences, parent, false);
            return new ChangeLicenceViewHolder(mView);
        } else if (viewType == ORDERTYPE_ANNUALNSPECTION) {//年检服务
            mView = mLayoutInflater.inflate(R.layout.activity_my_order_annualnspection, parent, false);
            return new AnnualnspectionViewHolder(mView);
        } else if (viewType == ORDERTYPE_SHOPSECKILLAUTO) {//特价车
            mView = mLayoutInflater.inflate(R.layout.activity_my_order_seckill, parent, false);
            return new ShopSeckillViewHolder(mView);
        } else if (viewType == ORDERTYPE_CARWASH) {//洗车
            mView = mLayoutInflater.inflate(R.layout.activity_my_order_carwash, parent, false);
            return new CarWashViewHolder(mView);
        }
        return null;
    }


    /**
     * adapter变化监听
     */
    public interface OnAdapterChangeListener {
        void onChange();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int ItemType = getItemViewType(position);
        final OrderListBean mOrderListBean = mOrderList.get(position);
        switch (ItemType) {
            case ORDERTYPE_MAINTENANCE_4SSHOP:  //4S保养
                MaintenanceViewHolder mMaintenanceViewHolder = (MaintenanceViewHolder) holder;
                if (position == 0)
                    mMaintenanceViewHolder.mDividerLine.setVisibility(View.GONE);
                else
                    mMaintenanceViewHolder.mDividerLine.setVisibility(View.VISIBLE);
                mMaintenanceViewHolder.mShopAddressView.setText(mOrderListBean.maintenance4SShop.shopName);
                showMaintenanceOrderOperateView(mMaintenanceViewHolder, mOrderListBean.maintenance4SShop);//状态显示
                mMaintenanceViewHolder.mOrderPriceView.setText(String.format("支付金额：%s", OtherUtil.amountFormat(mOrderListBean.maintenance4SShop.amountPayable, true)));
                //保养项目List
                if (mOrderListBean.maintenance4SShop.maintenanceItem != null && mOrderListBean.maintenance4SShop.maintenanceItem.size() > 0) {
                    // mMaintenanceViewHolder.mOrderItemView.setAdapter(new MaintenanceItemAdapter(mContext, mOrderListBean.maintenance.maintenanceItem));
                    String mMaintenanceItemString = "";
                    for (int i = 0; i < mOrderListBean.maintenance4SShop.maintenanceItem.size(); i++) {
                        mMaintenanceItemString += String.format("%d.%s", (i + 1), mOrderListBean.maintenance4SShop.maintenanceItem.get(i).name);
                        if (i == 1 && mOrderListBean.maintenance4SShop.maintenanceItem.size() > 2) {
                            mMaintenanceItemString += "...等";
                            break;
                        }
                        mMaintenanceItemString += "\n";
                    }
                    mMaintenanceViewHolder.mOrderItemView.setText(mMaintenanceItemString);
                    String str = String.format("共%d个项目", mOrderListBean.maintenance4SShop.maintenanceItem.size());
                    SpannableStringBuilder mSpannableStringBuilder = new SpannableStringBuilder(str);
                    mSpannableStringBuilder.setSpan(new ForegroundColorSpan(Color.RED), str.indexOf("共") + 1, str.indexOf("个"), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                    mMaintenanceViewHolder.mOrderItemNumView.setText(mSpannableStringBuilder);
                }
                mMaintenanceViewHolder.mSendCommentBtn.setOnClickListener(new View.OnClickListener() {
                    //去评论
                    @Override
                    public void onClick(View v) {
                        //去评论
                        ActivitySwitcher.toOrderSendComment(v.getContext(), mOrderListBean.maintenance4SShop.orderID, mOrderListBean.maintenance4SShop.shopPhoto, mOrderListBean.maintenance4SShop.shopID);
                    }
                });
                mMaintenanceViewHolder.mBut.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //保养去支付
                        CreateOrder createOrder = new CreateOrder();
                        createOrder.orderID = mOrderListBean.maintenance4SShop.orderID;
                        createOrder.amount = Float.valueOf(mOrderListBean.maintenance4SShop.amountPayable);
                        ActivitySwitcherMaintenance.toPayChoice(v.getContext(), createOrder, "", "2");
                    }
                });
                mMaintenanceViewHolder.mLinearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //跳保养订单详情
                        ActivitySwitcher.toMaintain4SShopOrderDetail(mContext, mOrderListBean.maintenance4SShop.orderID);
                    }
                });
                mMaintenanceViewHolder.mShopAddressView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //店铺详情
                        ActivitySwitcherThirdPartShop.toShopDetails(mContext, mOrderListBean.maintenance4SShop.shopID);
                    }
                });
                break;
            case ORDERTYPE_MAINTENANCE_FASTREPAIR:  //快修店保养
                mMaintenanceViewHolder = (MaintenanceViewHolder) holder;
                if (position == 0)
                    mMaintenanceViewHolder.mDividerLine.setVisibility(View.GONE);
                else
                    mMaintenanceViewHolder.mDividerLine.setVisibility(View.VISIBLE);
                mMaintenanceViewHolder.mShopAddressView.setText(mOrderListBean.maintenanceFastrepair.shopName);
                showMaintenanceOrderOperateView(mMaintenanceViewHolder, mOrderListBean.maintenanceFastrepair);//状态显示
                mMaintenanceViewHolder.mOrderPriceView.setText(String.format("支付金额：%s", OtherUtil.amountFormat(mOrderListBean.maintenanceFastrepair.amountPayable, true)));
                //保养项目List
                if (mOrderListBean.maintenanceFastrepair.maintenanceItem != null && mOrderListBean.maintenanceFastrepair.maintenanceItem.size() > 0) {
                    // mMaintenanceViewHolder.mOrderItemView.setAdapter(new MaintenanceItemAdapter(mContext, mOrderListBean.maintenance.maintenanceItem));
                    String mMaintenanceItemString = "";
                    for (int i = 0; i < mOrderListBean.maintenanceFastrepair.maintenanceItem.size(); i++) {
                        mMaintenanceItemString += String.format("%d.%s", (i + 1), mOrderListBean.maintenanceFastrepair.maintenanceItem.get(i).name);
                        if (i == 1 && mOrderListBean.maintenanceFastrepair.maintenanceItem.size() > 2) {
                            mMaintenanceItemString += "...等";
                            break;
                        }
                        mMaintenanceItemString += "\n";
                    }
                    mMaintenanceViewHolder.mOrderItemView.setText(mMaintenanceItemString);
                    mMaintenanceViewHolder.mOrderItemView.setText(mMaintenanceItemString);
                    String str = String.format("共%d个项目", mOrderListBean.maintenanceFastrepair.maintenanceItem.size());
                    SpannableStringBuilder mSpannableStringBuilder = new SpannableStringBuilder(str);
                    mSpannableStringBuilder.setSpan(new ForegroundColorSpan(Color.RED), str.indexOf("共") + 1, str.indexOf("个"), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                    mMaintenanceViewHolder.mOrderItemNumView.setText(mSpannableStringBuilder);
                }
                mMaintenanceViewHolder.mSendCommentBtn.setOnClickListener(new View.OnClickListener() {
                    //去评论
                    @Override
                    public void onClick(View v) {
                        //去评论
                        ActivitySwitcher.toOrderSendComment(v.getContext(), mOrderListBean.maintenanceFastrepair.orderID, mOrderListBean.maintenanceFastrepair.shopPhoto, mOrderListBean.maintenanceFastrepair.shopID);
                    }
                });
                mMaintenanceViewHolder.mVerifyCompleteBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new NormalDialog(mContext, "保养已结束，确定服务完成？") {
                            @Override
                            protected void doNext() {
                                new MaintenanceClient().verifyComplete(mOrderListBean.maintenanceFastrepair.orderID, new LoadingAnimResponseHandler(mContext, true) {
                                    @Override
                                    public void onSuccess(String response) {
                                        mOnAdapterChangeListener.onChange();
                                    }
                                });
                            }
                        }.show();
                    }
                });
                mMaintenanceViewHolder.mBut.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //保养去支付
                        CreateOrder createOrder = new CreateOrder();
                        createOrder.orderID = mOrderListBean.maintenanceFastrepair.orderID;
                        createOrder.amount = Float.valueOf(mOrderListBean.maintenanceFastrepair.amountPayable);
                        ActivitySwitcherMaintenance.toPayChoice(v.getContext(), createOrder, "", "2");
                    }
                });
                mMaintenanceViewHolder.mLinearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //跳保养订单详情
                        ActivitySwitcher.toMaintainOrderDetail(mContext, mOrderListBean.maintenanceFastrepair.orderID);
                    }
                });
//                mMaintenanceViewHolder.mShopAddressView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        //店铺详情
//                        ActivitySwitcherThirdPartShop.toShopDetails(mContext, mOrderListBean.maintenanceFastrepair.shopID);
//                    }
//                });
                break;
            case ORDERTYPE_ACCESSORY://用品
                AccessoryViewHolder mAccessoryViewHolder = (AccessoryViewHolder) holder;
                if (position == 0)
                    mAccessoryViewHolder.mDividerLine.setVisibility(View.GONE);
                else
                    mAccessoryViewHolder.mDividerLine.setVisibility(View.VISIBLE);

                mAccessoryViewHolder.mShopAddressView.setText(mOrderListBean.accessory.shopName);
                showAccessoryOrderOperateView(mAccessoryViewHolder, mOrderListBean.accessory);//状态显示
                mAccessoryViewHolder.mOrderPic.setText(mOrderListBean.accessory.getActualPayment());
                if (mOrderListBean.accessory.itemList != null && mOrderListBean.accessory.itemList.size() > 0) {
                    mAccessoryViewHolder.mOrderItem.setAdapter(new AcessoryItemAdapter(mContext, mOrderListBean.accessory.itemList));
                    String str = String.format("共%d件商品", getItemProductNum(mOrderListBean.accessory.itemList));
                    SpannableStringBuilder mSpannableStringBuilder = new SpannableStringBuilder(str);
                    mSpannableStringBuilder.setSpan(new ForegroundColorSpan(Color.RED), str.indexOf("共") + 1, str.indexOf("件"), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                    mAccessoryViewHolder.mOrderItemNum.setText(mSpannableStringBuilder);
                }
                mAccessoryViewHolder.mButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //用品去支付
                        ArrayList<SubmitOrderInfo> submitOrderInfos = new ArrayList<>();
                        SubmitOrderInfo submitOrderInfo = new SubmitOrderInfo(mOrderListBean.accessory.orderID, mOrderListBean.accessory.shopName);
                        submitOrderInfos.add(submitOrderInfo);
                        ActivitySwitcherAccessory.toPaySubscription(mContext, submitOrderInfos, mOrderListBean.accessory.actualPayment);
                    }
                });
                mAccessoryViewHolder.mOrderItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        //跳订单详情
                        ActivitySwitcherAccessory.toOrderDetail(mContext, mOrderListBean.accessory.orderID);
                    }
                });
//                mAccessoryViewHolder.mShopAddressView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        //店铺详情
//                        ActivitySwitcherThirdPartShop.toShopDetails(mContext, mOrderListBean.accessory.shopID);
//                    }
//                });
                mAccessoryViewHolder.mVerifyCompleteBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new NormalDialog(mContext, "商品已收到，确认收货？") {
                            @Override
                            protected void doNext() {
                                new AccessoryApiClient().confirmReceived(mOrderListBean.accessory.orderID, new LoadingAnimResponseHandler(mContext) {
                                    @Override
                                    public void onSuccess(String response) {
                                        mOnAdapterChangeListener.onChange();
                                    }
                                });
                            }
                        }.show();
                    }
                });
                break;
            case ORDERTYPE_ACCESSORY_4SSHOP://4s店用品
                mAccessoryViewHolder = (AccessoryViewHolder) holder;
                if (position == 0)
                    mAccessoryViewHolder.mDividerLine.setVisibility(View.GONE);
                else
                    mAccessoryViewHolder.mDividerLine.setVisibility(View.VISIBLE);

                mAccessoryViewHolder.mShopAddressView.setText(mOrderListBean.accessory4SShop.shopName);
                showAccessoryOrderOperateView(mAccessoryViewHolder, mOrderListBean.accessory4SShop);//状态显示
                mAccessoryViewHolder.mOrderPic.setText(mOrderListBean.accessory4SShop.getActualPayment());
                if (mOrderListBean.accessory4SShop.itemList != null && mOrderListBean.accessory4SShop.itemList.size() > 0) {
                    mAccessoryViewHolder.mOrderItem.setAdapter(new AcessoryItemAdapter(mContext, mOrderListBean.accessory4SShop.itemList));
                    String str = String.format("共%d件商品", getItemProductNum(mOrderListBean.accessory4SShop.itemList));
                    SpannableStringBuilder mSpannableStringBuilder = new SpannableStringBuilder(str);
                    mSpannableStringBuilder.setSpan(new ForegroundColorSpan(Color.RED), str.indexOf("共") + 1, str.indexOf("件"), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                    mAccessoryViewHolder.mOrderItemNum.setText(mSpannableStringBuilder);
                }
                mAccessoryViewHolder.mButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //用品去支付
                        SubmitOrderInfo4S submitOrderInfo = new SubmitOrderInfo4S(mOrderListBean.accessory4SShop.orderID, mOrderListBean.accessory4SShop.actualPayment);
                        ActivitySwitcherAccessory4S.toPay(mContext, submitOrderInfo);
                    }
                });
                mAccessoryViewHolder.mOrderItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        //跳4s订单详情
                        ActivitySwitcherAccessory4S.toOrderDetail(mContext, mOrderListBean.accessory4SShop.orderID);
                    }
                });
                mAccessoryViewHolder.mVerifyCompleteBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new NormalDialog(mContext, "商品已收到，确认收货？") {
                            @Override
                            protected void doNext() {
                                new Accessory4SApiClient().confirmReceived(mOrderListBean.accessory4SShop.orderID, new LoadingAnimResponseHandler(mContext) {
                                    @Override
                                    public void onSuccess(String response) {
                                        mOnAdapterChangeListener.onChange();
                                    }
                                });
                            }
                        }.show();
                    }
                });
                break;
            case ORDERTYPE_NEWAUTO://新车自营
                NewAutoViewHolder mNewAutoViewHolder = (NewAutoViewHolder) holder;
                if (position == 0)
                    mNewAutoViewHolder.view.setVisibility(View.GONE);
                else
                    mNewAutoViewHolder.view.setVisibility(View.VISIBLE);

                showNewAutoOrderOperateView(mNewAutoViewHolder, mOrderListBean.newAuto);//状态显示
                mNewAutoViewHolder.mOrderDescView.setText("恒信自营");
                if (mOrderListBean.newAuto.orderType.equals("0")) {
                    mNewAutoViewHolder.mOrderTypeView.setImageResource(R.drawable.normal_order_type);
                } else if (mOrderListBean.newAuto.orderType.equals("1")) {
                    mNewAutoViewHolder.mOrderTypeView.setImageResource(R.drawable.special_order_type);
                }
                mNewAutoViewHolder.mOrderDescriptionView.initOrderDescription(mOrderListBean.newAuto);
                if (mNewAutoViewHolder.mOrderBottomView.hideBottom(mOrderListBean.newAuto)) {
                    mNewAutoViewHolder.mOrderBottomView.setVisibility(View.GONE);
                    mNewAutoViewHolder.mLineView.setVisibility(View.GONE);
                } else {
                    mNewAutoViewHolder.mOrderBottomView.setVisibility(View.VISIBLE);
                    mNewAutoViewHolder.mLineView.setVisibility(View.VISIBLE);
                    mNewAutoViewHolder.mOrderBottomView.initBottom(mContext, mOrderListBean.newAuto, false);
                }
                mNewAutoViewHolder.mOrderDescriptionView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ActivitySwitchBase.toOrderDetail(mContext, mOrderListBean.newAuto.orderID);
                    }
                });
                break;
            case ORDERTYPE_NEWENERG://新能源
                mNewAutoViewHolder = (NewAutoViewHolder) holder;
                if (position == 0)
                    mNewAutoViewHolder.view.setVisibility(View.GONE);
                else
                    mNewAutoViewHolder.view.setVisibility(View.VISIBLE);

                showNewAutoOrderOperateView(mNewAutoViewHolder, mOrderListBean.newEnerg);//状态显示
                mNewAutoViewHolder.mOrderDescView.setText("恒信自营");
                if (mOrderListBean.newEnerg.orderType.equals("0")) {
                    mNewAutoViewHolder.mOrderTypeView.setImageResource(R.drawable.normal_order_type);
                } else if (mOrderListBean.newEnerg.orderType.equals("1")) {
                    mNewAutoViewHolder.mOrderTypeView.setImageResource(R.drawable.special_order_type);
                }
                mNewAutoViewHolder.mOrderDescriptionView.initOrderDescription(mOrderListBean.newEnerg);
                if (mNewAutoViewHolder.mOrderBottomView.hideBottom(mOrderListBean.newEnerg)) {
                    mNewAutoViewHolder.mOrderBottomView.setVisibility(View.GONE);
                    mNewAutoViewHolder.mLineView.setVisibility(View.GONE);
                } else {
                    mNewAutoViewHolder.mOrderBottomView.setVisibility(View.VISIBLE);
                    mNewAutoViewHolder.mLineView.setVisibility(View.VISIBLE);
                    mNewAutoViewHolder.mOrderBottomView.initBottom(mContext, mOrderListBean.newEnerg, false);
                }
                mNewAutoViewHolder.mOrderDescriptionView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ActivitySwitchBase.toOrderDetail(mContext, mOrderListBean.newEnerg.orderID);
                    }
                });
                break;
            case ORDERTYPE_REPAIR://维修
                RepairViewHolder mRepairViewHolder = (RepairViewHolder) holder;
                if (position == 0)
                    mRepairViewHolder.mDividerLine.setVisibility(View.GONE);
                else
                    mRepairViewHolder.mDividerLine.setVisibility(View.VISIBLE);
                showRepairOrderOperateView(mRepairViewHolder, mOrderListBean.repair);
                mRepairViewHolder.mOrderState.setText(mOrderListBean.repair.orderStatusText);
                mRepairViewHolder.mOrderState.setTextColor(mOrderListBean.repair.getStatusColor(mContext));
                mRepairViewHolder.mShopAdrressView.setText(mOrderListBean.repair.shopName);
                mRepairViewHolder.mCheckTypeView.setText(mOrderListBean.repair.serviceType);
                mRepairViewHolder.mAboutTime.setText(mOrderListBean.repair.apppintmentDate);
                mRepairViewHolder.mLinearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ActivitySwitcher.toRepairOrderDetail(mContext, mOrderListBean.repair.orderID);
                    }
                });
                mRepairViewHolder.mSendComment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ActivitySwitcher.toOrderSendComment(v.getContext(), mOrderListBean.repair.orderID, mOrderListBean.repair.shopPhoto, mOrderListBean.repair.shopID);
                    }
                });
//                mRepairViewHolder.mShopAdrressView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        ActivitySwitcherThirdPartShop.toShopDetails(mContext, mOrderListBean.repair.shopID);
//                    }
//                });
                break;
            case ORDERTYPE_SHOPPORMOTION://门店活动
                ShopPormotionViewHolder mShopPormotionViewHolder = (ShopPormotionViewHolder) holder;
                if (position == 0)
                    mShopPormotionViewHolder.dividerLineView.setVisibility(View.GONE);
                else
                    mShopPormotionViewHolder.dividerLineView.setVisibility(View.VISIBLE);
                mShopPormotionViewHolder.mShopAddress.setText(mOrderListBean.shopPormotion.promotion.shopTitle);
                if (mOrderListBean.shopPormotion.captcha != null) {
                    mShopPormotionViewHolder.mCaptchaView.setVisibility(View.VISIBLE);
                    mShopPormotionViewHolder.mCaptchaView.setText("验证码：" + mOrderListBean.shopPormotion.captcha);
                } else {
                    mShopPormotionViewHolder.mCaptchaView.setVisibility(View.GONE);
                }
                //订单状态显示
                showShopPormotionOrderOperateView(mShopPormotionViewHolder, mOrderListBean.shopPormotion);

                if (!TextUtils.isEmpty(mOrderListBean.shopPormotion.promotion.thumb))
                    ImageUtil.setImage(mContext, mShopPormotionViewHolder.mOrderImageView, mOrderListBean.shopPormotion.promotion.thumb);
                mShopPormotionViewHolder.mFavorableNameView.setText(mOrderListBean.shopPormotion.promotion.title);
                mShopPormotionViewHolder.mFavorableTimeView.setText(String.format("%s 至 %s", mOrderListBean.shopPormotion.promotion.startDate, mOrderListBean.shopPormotion.promotion.endDate));
                mShopPormotionViewHolder.mOrderDeatailView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //跳订单详情
                        ActivitySwitcherThirdPartShop.toOrderDetail(mContext, mOrderListBean.shopPormotion.orderID);
                    }
                });
                mShopPormotionViewHolder.mShopAddress.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //跳店铺详情
                        ActivitySwitcherThirdPartShop.toShopDetails(mContext, mOrderListBean.shopPormotion.shopID);
                    }
                });
                break;
            case ORDERTYPE_WZSERVICE: //违章处理
                WZServiceViewHolder mWzServiceViewHolder = (WZServiceViewHolder) holder;
                if (position == 0)
                    mWzServiceViewHolder.dividerLineView.setVisibility(View.GONE);
                else
                    mWzServiceViewHolder.dividerLineView.setVisibility(View.VISIBLE);

                showWzServiceOrderOperateView(mWzServiceViewHolder, mOrderListBean.wzService);
                mWzServiceViewHolder.mCarNumView.setText(mOrderListBean.wzService.plateNumber);
                mWzServiceViewHolder.mServerTypeView.setText(String.format("服务类型：%s", "违章查缴")); // TODO: 2016/4/26  根据大类判断
                mWzServiceViewHolder.mOrderPiceView.setText(String.format("支付金额：%s", OtherUtil.amountFormat(mOrderListBean.wzService.amount, true)));
                mWzServiceViewHolder.mOrderDetailView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //跳违章订单详情
                        ActivitySwitchAround.toOrderDetailActivity(mContext, mOrderListBean.wzService.orderID, OrderDetailContants.FRAGMENT_ILLEGAL_ORDER_DETAIL);
                    }
                });
                mWzServiceViewHolder.mButView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //去支付
                        PaymentActivitySwitch.toAroundPaymentActivity(mContext, mOrderListBean.wzService.amount, mOrderListBean.wzService.orderID, OrderDetailContants.ILLEGAL_AND_COMMISSION, "-1");
                    }
                });
                break;
            case ORDERTYPE_CHANGELICENCE: //驾驶证换证
                ChangeLicenceViewHolder mChangeLicenceViewHolder = (ChangeLicenceViewHolder) holder;
                if (position == 0)
                    mChangeLicenceViewHolder.dividerLineView.setVisibility(View.GONE);
                else
                    mChangeLicenceViewHolder.dividerLineView.setVisibility(View.VISIBLE);

                showChangeLicenceOrderOperateView(mChangeLicenceViewHolder, mOrderListBean.changeLicence);
                mChangeLicenceViewHolder.mServerTypeView.setText(String.format("服务类型：%s", "驾驶证换证")); // TODO: 2016/4/26  根据大类判断
                mChangeLicenceViewHolder.mOrderPiceView.setText(String.format("支付金额：%s", OtherUtil.amountFormat(mOrderListBean.changeLicence.amount, true)));
                mChangeLicenceViewHolder.mOrderDetailView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //跳驾驶证换证订单详情
//                        DebugLog.e("ORDERID--->",mOrderListBean.changeLicence.orderID);
                        ActivitySwitchAround.toOrderDetailActivity(mContext, mOrderListBean.changeLicence.orderID, OrderDetailContants.FRAGMENT_LICENSE_ORDER_DETAIL);
                    }
                });
                mChangeLicenceViewHolder.mButView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //去支付
                        PaymentActivitySwitch.toAroundPaymentActivity(mContext, mOrderListBean.changeLicence.amount + "", mOrderListBean.changeLicence.orderID, OrderDetailContants.FLAG_ACTIVITY_LICENSE, "-1");
//                        PaymentActivitySwitch.toAroundPaymentActivity(mContext, mOrderListBean.changeLicence.amount + "", mOrderListBean.changeLicence.orderID,OrderDetailContants.ILLEGAL_AND_COMMISSION);
                    }
                });
                break;
            case ORDERTYPE_ANNUALNSPECTION: //车辆年检
                AnnualnspectionViewHolder mAnnualnspectionViewHolder = (AnnualnspectionViewHolder) holder;
                if (position == 0)
                    mAnnualnspectionViewHolder.dividerLineView.setVisibility(View.GONE);
                else
                    mAnnualnspectionViewHolder.dividerLineView.setVisibility(View.VISIBLE);

                showAnnualnspectionOrderOperateView(mAnnualnspectionViewHolder, mOrderListBean.annualnspection);
                mAnnualnspectionViewHolder.mCarNumView.setText(mOrderListBean.annualnspection.plateNumber);
                mAnnualnspectionViewHolder.mServerTypeView.setText(String.format("服务类型：%s", "车辆年检")); // TODO: 2016/4/26  根据大类判断
                mAnnualnspectionViewHolder.mOrderPiceView.setText(String.format("支付金额：%s", OtherUtil.amountFormat(mOrderListBean.annualnspection.amount, true)));
                mAnnualnspectionViewHolder.mOrderDetailView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //跳驾驶证换证订单详情

                        ActivitySwitchAround.toOrderDetailActivity(mContext, mOrderListBean.annualnspection.orderID, OrderDetailContants.FRAGMENT_VEHICLES_ORDER_DETAIL);
                    }
                });
                mAnnualnspectionViewHolder.mButView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //去支付
//                        PaymentActivitySwitch.toAroundPaymentActivity(mContext, mOrderListBean.annualnspection.amount + "", mOrderListBean.annualnspection.orderID,OrderDetailContants.ILLEGAL_AND_COMMISSION);
                        DebugLog.i(AutoInfoContants.LOG_J, "exemption: " + mOrderListBean.annualnspection.exemption);
                        PaymentActivitySwitch.toAroundPaymentActivity(mContext, mOrderListBean.annualnspection.amount + "", mOrderListBean.annualnspection.orderID, OrderDetailContants.FLAG_ACTIVITY_VEHICLES, mOrderListBean.annualnspection.exemption);
                    }
                });
                break;
            case ORDERTYPE_SHOPSECKILLAUTO: //特价车
                ShopSeckillViewHolder mShopSeckillViewHolder = (ShopSeckillViewHolder) holder;
                if (position == 0)
                    mShopSeckillViewHolder.dividerLineView.setVisibility(View.GONE);
                else
                    mShopSeckillViewHolder.dividerLineView.setVisibility(View.VISIBLE);

                mShopSeckillViewHolder.mShopAddressView.setText(mOrderListBean.shopSeckillAuto.shopTitle);
                showSeckillOrderOperateView(mShopSeckillViewHolder, mOrderListBean.shopSeckillAuto);
                mShopSeckillViewHolder.mOrderPrice.setText(mOrderListBean.shopSeckillAuto.getOrderPrice());
                BaseOrder mBaseOrder = new BaseOrder();
                mBaseOrder.itemColor = mOrderListBean.shopSeckillAuto.itemColor;
                mBaseOrder.itemInterior = mOrderListBean.shopSeckillAuto.itemInterior;
                mBaseOrder.itemPrice = mOrderListBean.shopSeckillAuto.orderAmount;
                mBaseOrder.itemName = mOrderListBean.shopSeckillAuto.itemName;
                mBaseOrder.itemThumb = mOrderListBean.shopSeckillAuto.itemThumb;
//                DebugLog.d("mBaseOrder.itemThumb",mOrderListBean.shopSeckillAuto.itemThumb);
                mShopSeckillViewHolder.mOrderDescription.initOrderDescription(mContext, mBaseOrder, mOrderListBean.shopSeckillAuto.captcha);
                mShopSeckillViewHolder.mShopAddressView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //跳店铺
                        ActivitySwitcherThirdPartShop.toShopDetails(mContext, mOrderListBean.shopSeckillAuto.shopID);
                    }
                });
                mShopSeckillViewHolder.mButView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //去支付
                        ActivitySwitcherThirdPartShop.toPayDeposit(mOrderListBean.shopSeckillAuto.subscription, mOrderListBean.shopSeckillAuto.orderID,
                                mOrderListBean.shopSeckillAuto.shopTel, mContext);
                    }
                });
                mShopSeckillViewHolder.mOrderDescription.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //跳特价车详情
                        ActivitySwitcher.toSeckillOrderDetail(mContext, mOrderListBean.shopSeckillAuto.orderID);
                    }
                });
                break;
            case ORDERTYPE_CARWASH: //洗车
                CarWashViewHolder mCarWashViewHolder = (CarWashViewHolder) holder;
                if (position == 0)
                    mCarWashViewHolder.dividerLineView.setVisibility(View.GONE);
                else
                    mCarWashViewHolder.dividerLineView.setVisibility(View.VISIBLE);
                showCarWashOrderOperateView(mCarWashViewHolder, mOrderListBean.carWash);
                mCarWashViewHolder.mShopAddressView.setText(mOrderListBean.carWash.shopName);
                mCarWashViewHolder.mServerTypeView.setText(String.format("服务类型：%s", "洗车"));
                mCarWashViewHolder.mOrderPiceView.setText(String.format("支付金额：%s", OtherUtil.amountFormat(mOrderListBean.carWash.actualPayment, true)));
                mCarWashViewHolder.mSendCommentView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //跳评价
                        CarWashActivitySwitcher.CarWashSendComment(mContext, mOrderListBean.carWash.orderID, mOrderListBean.carWash.shopPhoto, mOrderListBean.carWash.shopID, mOrderListBean.carWash.shopName, mOrderListBean.carWash.actualPayment);
                    }
                });
                mCarWashViewHolder.mCarWashDetailView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //跳订单详情
                        ActivitySwitcher.toCarWashOrderDetail(mContext, mOrderListBean.carWash.orderID);
                    }
                });
                mCarWashViewHolder.mShopAddressView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        ActivitySwitcherThirdPartShop.toShopDetails(mContext, mOrderListBean.carWash.shopID);
                        CarWashActivitySwitcher.toWashCarShop(mContext, mOrderListBean.carWash.shopID);
                    }
                });
                break;
        }
    }

    /**
     * 获取商品数量
     *
     * @param itemList
     * @return
     */
    private int getItemProductNum(ArrayList<AccessoryBean.AccessoryItem> itemList) {
        int ItemProductNum = 0;
        for (AccessoryBean.AccessoryItem item : itemList) {
            ItemProductNum += Integer.parseInt(item.productNum);
        }
        return ItemProductNum;
    }


    /**
     * 洗车状态判断
     */
    private void showCarWashOrderOperateView(CarWashViewHolder mCarWashViewHolder, CarWashBean mCarWashBean) {
        mCarWashViewHolder.mOrderStatusView.setText(mCarWashBean.orderStatusText);
        mCarWashViewHolder.mOrderStatusView.setTextColor(mCarWashBean.getStatusColor(mContext));
        if (mCarWashBean.orderStatus.equals(mCarWashBean.ORDER_DDWC) && !mCarWashBean.getHasComment()) //订单完成 并且没有评论
            mCarWashViewHolder.mSendCommentView.setVisibility(View.VISIBLE);
        else
            mCarWashViewHolder.mSendCommentView.setVisibility(View.GONE);

    }


    /**
     * 特价车状态判断
     */
    private void showSeckillOrderOperateView(ShopSeckillViewHolder mShopSeckillViewHolder, ShopSeckillAutoBean mShopSeckillAutoBean) {

        mShopSeckillViewHolder.mOrderStatusView.setText(mShopSeckillAutoBean.orderStatus);
        mShopSeckillViewHolder.mOrderStatusView.setTextColor(mShopSeckillAutoBean.getStatusColor(mContext));
        mShopSeckillViewHolder.mRefundStatusView.setVisibility(View.GONE);
        mShopSeckillViewHolder.mButView.setVisibility(View.GONE);

        if (mShopSeckillAutoBean.orderStatusCode.equals(mShopSeckillAutoBean.ORDER_WFK))
            mShopSeckillViewHolder.mButView.setVisibility(View.VISIBLE);


        if (mShopSeckillAutoBean.orderStatusCode.equals(mShopSeckillAutoBean.ORDER_YFK) && !TextUtils.isEmpty(mShopSeckillAutoBean.refundStatus) && !mShopSeckillAutoBean.refundStatus.equals(mShopSeckillAutoBean.REFUND_DTK)) {
            mShopSeckillViewHolder.mRefundStatusView.setVisibility(View.VISIBLE);
            mShopSeckillViewHolder.mRefundStatusView.setText(mShopSeckillAutoBean.refundStatusText);
            mShopSeckillViewHolder.mRefundStatusView.setTextColor(mShopSeckillAutoBean.getRefundStatusColor(mContext));
        }

    }

    /**
     * 违章处理状态判断
     */
    private void showWzServiceOrderOperateView(WZServiceViewHolder mWzServiceViewHolder, WzServiceBean mWzServiceBean) {
        mWzServiceViewHolder.mOrderStatusView.setText(mWzServiceBean.orderStatusText);
        mWzServiceViewHolder.mOrderStatusView.setTextColor(mWzServiceBean.getStatusColor(mContext));
        mWzServiceViewHolder.mOrderRefundStatusView.setVisibility(View.GONE);
        mWzServiceViewHolder.mOrderPiceView.setVisibility(View.GONE);
        mWzServiceViewHolder.mButView.setVisibility(View.GONE);

        if (mWzServiceBean.refundStatus != null && mWzServiceBean.refundStatusText != null) {
            mWzServiceViewHolder.mOrderRefundStatusView.setVisibility(View.VISIBLE);
            mWzServiceViewHolder.mOrderRefundStatusView.setText(mWzServiceBean.refundStatusText);
            mWzServiceViewHolder.mOrderRefundStatusView.setTextColor(mWzServiceBean.getRefundStatusColor(mContext));
        }
        if (!mWzServiceBean.orderStatus.equals(mWzServiceBean.ORDER_DSL))
            mWzServiceViewHolder.mOrderPiceView.setVisibility(View.VISIBLE);

        if (mWzServiceBean.orderStatus.equals(mWzServiceBean.ORDER_DFK))
            mWzServiceViewHolder.mButView.setVisibility(View.VISIBLE);

    }

    /**
     * 驾驶证换证状态判断
     */
    private void showChangeLicenceOrderOperateView(ChangeLicenceViewHolder mChangeLicenceViewHolder, ChangeLicenceBean mChangeLicenceBean) {
        mChangeLicenceViewHolder.mOrderStatusView.setText(mChangeLicenceBean.orderStatusText);
        mChangeLicenceViewHolder.mOrderStatusView.setTextColor(mChangeLicenceBean.getStatusColor(mContext));
        mChangeLicenceViewHolder.mOrderRefundStatusView.setVisibility(View.GONE);
        mChangeLicenceViewHolder.mOrderPiceView.setVisibility(View.GONE);
        mChangeLicenceViewHolder.mButView.setVisibility(View.GONE);

        if (mChangeLicenceBean.refundStatus != null && mChangeLicenceBean.refundStatusText != null) {
            mChangeLicenceViewHolder.mOrderRefundStatusView.setVisibility(View.VISIBLE);
            mChangeLicenceViewHolder.mOrderRefundStatusView.setText(mChangeLicenceBean.refundStatusText);
            mChangeLicenceViewHolder.mOrderRefundStatusView.setTextColor(mChangeLicenceBean.getRefundStatusColor(mContext));
        }
        if (!mChangeLicenceBean.orderStatus.equals(mChangeLicenceBean.ORDER_DSL))
            mChangeLicenceViewHolder.mOrderPiceView.setVisibility(View.VISIBLE);

        if (mChangeLicenceBean.orderStatus.equals(mChangeLicenceBean.ORDER_DFK))
            mChangeLicenceViewHolder.mButView.setVisibility(View.VISIBLE);

    }

    /**
     * 车辆年检状态判断
     */
    private void showAnnualnspectionOrderOperateView(AnnualnspectionViewHolder mAnnualnspectionViewHolder, AnnualnspectionBean mAnnualnspectionBean) {
        mAnnualnspectionViewHolder.mOrderStatusView.setText(mAnnualnspectionBean.orderStatusText);
        mAnnualnspectionViewHolder.mOrderStatusView.setTextColor(mAnnualnspectionBean.getStatusColor(mContext));
        mAnnualnspectionViewHolder.mOrderRefundStatusView.setVisibility(View.GONE);
        mAnnualnspectionViewHolder.mOrderPiceView.setVisibility(View.GONE);
        mAnnualnspectionViewHolder.mButView.setVisibility(View.GONE);
        if (mAnnualnspectionBean.refundStatus != null && mAnnualnspectionBean.refundStatusText != null) {
            mAnnualnspectionViewHolder.mOrderRefundStatusView.setVisibility(View.VISIBLE);
            mAnnualnspectionViewHolder.mOrderRefundStatusView.setText(mAnnualnspectionBean.refundStatusText);
            mAnnualnspectionViewHolder.mOrderRefundStatusView.setTextColor(mAnnualnspectionBean.getRefundStatusColor(mContext));
        }
        if (!mAnnualnspectionBean.orderStatus.equals(mAnnualnspectionBean.ORDER_DSL))
            mAnnualnspectionViewHolder.mOrderPiceView.setVisibility(View.VISIBLE);

        if (mAnnualnspectionBean.orderStatus.equals(mAnnualnspectionBean.ORDER_DFK))
            mAnnualnspectionViewHolder.mButView.setVisibility(View.VISIBLE);
    }

    /**
     * 用品状态判断
     *
     * @param mAccessoryViewHolder
     * @param mAccessoryBean
     */
    private void showAccessoryOrderOperateView(AccessoryViewHolder mAccessoryViewHolder, AccessoryBean mAccessoryBean) {
        mAccessoryViewHolder.mOrderState.setText(mAccessoryBean.orderStatusText);
        mAccessoryViewHolder.mOrderState.setTextColor(mAccessoryBean.getStatusColor(mContext));
        mAccessoryViewHolder.mButton.setVisibility(View.GONE); //隐藏支付按钮  初始化
        mAccessoryViewHolder.mRefundStatusText.setVisibility(View.GONE);//隐藏退款状态 初始化
        mAccessoryViewHolder.mVerifyCompleteBtn.setVisibility(View.GONE);

        if (mAccessoryBean.orderStatus.equals(mAccessoryBean.ORDER_DFK)) {
            mAccessoryViewHolder.mButton.setVisibility(View.VISIBLE); //显示支付按钮
        }
        if (!TextUtils.isEmpty(mAccessoryBean.refundStatusText) && !TextUtils.isEmpty(mAccessoryBean.refundStatus)) {
            mAccessoryViewHolder.mRefundStatusText.setVisibility(View.VISIBLE);//显示退款状态
            mAccessoryViewHolder.mRefundStatusText.setText(mAccessoryBean.refundStatusText);
            mAccessoryViewHolder.mRefundStatusText.setTextColor(mAccessoryBean.getRefundStatusColor(mContext));
        }
        if (mAccessoryBean.orderStatus.equals(mAccessoryBean.ORDER_DDFWWC)) {
            mAccessoryViewHolder.mVerifyCompleteBtn.setVisibility(View.VISIBLE); //显示确认收货
        }
    }

    /**
     * 保养状态判断
     *
     * @param mMaintenanceViewHolder
     * @param mMaintenanceBean
     */
    private void showMaintenanceOrderOperateView(MaintenanceViewHolder mMaintenanceViewHolder, MaintenanceBean mMaintenanceBean) {
        mMaintenanceViewHolder.mOrderStateView.setText(mMaintenanceBean.orderStatusText);
        mMaintenanceViewHolder.mOrderStateView.setTextColor(mMaintenanceBean.getStatusColor(mContext));

        mMaintenanceViewHolder.mRefundStatusText.setVisibility(View.GONE);//隐藏退款状态 初始化
        mMaintenanceViewHolder.mSendCommentBtn.setVisibility(View.GONE);//隐藏评论按钮 初始化
        mMaintenanceViewHolder.mVerifyCompleteBtn.setVisibility(View.GONE);//隐藏确认按钮 初始化
        mMaintenanceViewHolder.mBut.setVisibility(View.GONE); //隐藏支付按钮  初始化

        if (mMaintenanceBean.orderStatus.equals(mMaintenanceBean.ORDER_WFK)) { //未付款
            mMaintenanceViewHolder.mBut.setVisibility(View.VISIBLE); //显示支付按钮
        }
        if (!TextUtils.isEmpty(mMaintenanceBean.refundStatus) && !TextUtils.isEmpty(mMaintenanceBean.refundStatusText)) {
            mMaintenanceViewHolder.mRefundStatusText.setVisibility(View.VISIBLE);//显示退款状态
            mMaintenanceViewHolder.mRefundStatusText.setText(mMaintenanceBean.refundStatusText);
            mMaintenanceViewHolder.mRefundStatusText.setTextColor(mMaintenanceBean.getRefundStatusColor(mContext));
        }
        if (mMaintenanceBean.orderStatus.equals(mMaintenanceBean.ORDER_DDWC) && !mMaintenanceBean.getHasComment()) {
            mMaintenanceViewHolder.mSendCommentBtn.setVisibility(View.VISIBLE); //订单已完成 并且 可评论时，显示评论按钮
        }
        if (mMaintenanceBean.orderStatus.equals(mMaintenanceBean.ORDER_DDFWWC)) {
            mMaintenanceViewHolder.mVerifyCompleteBtn.setVisibility(View.VISIBLE); //订单服务完成，显示确认按钮
        }
    }


    /**
     * 新车自营 状态 显示判断
     */
    private void showNewAutoOrderOperateView(NewAutoViewHolder mNewAutoViewHolder, OrderModel mUserOrder) {
        mNewAutoViewHolder.mOrderStatusView.setText(mUserOrder.getOrderStatus());
        mNewAutoViewHolder.mOrderStatusView.setTextColor(BaseOrderStatus.getStatusColor(mContext, mUserOrder.getOrderStatus()));
    }


    /**
     * 修车预约 状态 显示判断
     */
    private void showRepairOrderOperateView(RepairViewHolder mRepairViewHolder, RepairBean mRepairBean) {
        mRepairViewHolder.mSendCommentrly.setVisibility(View.GONE);

        if (mRepairBean.orderStatus.equals(BaseOrderStatus.RepairOrderStatus.ORDER_YWC) && !mRepairBean.getHasComment())
            mRepairViewHolder.mSendCommentrly.setVisibility(View.VISIBLE);
    }


    /**
     * 网上4S 状态 显示判断
     *
     * @param holder
     * @param mShopPormotionBean
     */
    public void showShopPormotionOrderOperateView(ShopPormotionViewHolder holder, final ThirdOrderModel mShopPormotionBean) {
        holder.mOrderStatusView.setVisibility(View.GONE);
        holder.mOrderStatusBottomView.setVisibility(View.GONE);
        holder.mOrderRefundStatusView.setVisibility(View.GONE);

        if (!TextUtils.isEmpty(mShopPormotionBean.refundStatus) && !TextUtils.isEmpty(mShopPormotionBean.refundStatusText) && !mShopPormotionBean.refundStatus.equals("10")) {
            holder.mOrderRefundStatusView.setVisibility(View.VISIBLE);
            holder.mOrderRefundStatusView.setText(mShopPormotionBean.refundStatusText);
            holder.mOrderRefundStatusView.setTextColor(BaseOrderStatus.getStatusColor(mContext, mShopPormotionBean.refundStatusText));
        }
        if (mShopPormotionBean.orderStatusCode == 0) {
            //1. 判断活动是否过期 2.判断活动是否下线 3.判断活动是否可支付
//            if (OtherUtil.date2TimeStamp(thirdOrderModel.promotion.getEndDateByTime(), "yyyy-MM-dd HH:mm:ss") <
// OtherUtil.date2TimeStamp(thirdOrderModel.promotion.serverTime, "yyyy-MM-dd HH:mm:ss") || "30".equals
// (thirdOrderModel.promotion.status) || !thirdOrderModel.promotion.getPaymentAvailable()) {
            if (OtherUtil.date2TimeStamp(mShopPormotionBean.promotion.getEndDateByTime(), "yyyy-MM-dd HH:mm:ss") <
                    OtherUtil.date2TimeStamp(mShopPormotionBean.promotion.serverTime, "yyyy-MM-dd HH:mm:ss") || "30"
                    .equals(mShopPormotionBean.promotion.status)) {
                holder.mOrderStatusView.setVisibility(View.VISIBLE);
                holder.mOrderStatusView.setText("已关闭");
                return;
            }
            //满足以上条件就显示付款按钮
            holder.mOrderStatusView.setVisibility(View.VISIBLE);
            holder.mOrderStatusView.setText("已下单");
            holder.mOrderStatusBottomView.setVisibility(View.VISIBLE);
            holder.mOrderStatusButtonView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ActivitySwitcherThirdPartShop.toPayDeposit(mShopPormotionBean.subscription, mShopPormotionBean.orderID,
                            mShopPormotionBean.shopTel, mContext);
                }
            });
        } else if (mShopPormotionBean.orderStatusCode == 10 || mShopPormotionBean.orderStatusCode == 20) {
            holder.mOrderStatusView.setVisibility(View.VISIBLE);

            holder.mOrderStatusView.setText("已付款");
        } else if (mShopPormotionBean.orderStatusCode == -40) {
            holder.mOrderStatusView.setVisibility(View.VISIBLE);
            holder.mOrderStatusView.setText("已关闭");

        } else {
            holder.mOrderStatusView.setVisibility(View.VISIBLE);
            holder.mOrderStatusView.setText(mShopPormotionBean.orderStatus);
        }
        holder.mOrderStatusView.setTextColor(BaseOrderStatus.getStatusColor(mContext, holder.mOrderStatusView.getText().toString()));
    }


    @Override
    public int getItemCount() {
        return mOrderList.size();
    }


    /**
     * 洗车
     */
    class CarWashViewHolder extends RecyclerView.ViewHolder {

        View dividerLineView;
        TextView mOrderStatusView;
        TextView mShopAddressView;
        TextView mServerTypeView;
        TextView mOrderPiceView;
        Button mSendCommentView;
        LinearLayout mCarWashDetailView;

        public CarWashViewHolder(View itemView) {
            super(itemView);
            dividerLineView = itemView.findViewById(R.id.myorder_carWash_divider_line);
            mShopAddressView = (TextView) itemView.findViewById(R.id.myorder_carWash_shopaddress);
            mOrderStatusView = (TextView) itemView.findViewById(R.id.myorder_carWash_status);
            mServerTypeView = (TextView) itemView.findViewById(R.id.myorder_carWash_server_type);
            mOrderPiceView = (TextView) itemView.findViewById(R.id.myorder_carWash_pice);
            mSendCommentView = (Button) itemView.findViewById(R.id.myorder_carWash_send_comment);
            mCarWashDetailView = (LinearLayout) itemView.findViewById(R.id.myorder_carWash_detail);
        }
    }


    /**
     * 保养
     */
    class MaintenanceViewHolder extends RecyclerView.ViewHolder {
        TextView mShopAddressView;//店铺名称 string
        TextView mOrderStateView;//订单状态 string
        ImageView mOrderImageView; //
        TextView mOrderItemView;//保养项目[]
        TextView mOrderItemNumView; //项目数量
        TextView mOrderPriceView; //实付金额(应付金额) number
        View mDividerLine;
        LinearLayout mLinearLayout;
        Button mBut;
        TextView mRefundStatusText;
        Button mSendCommentBtn;
        Button mVerifyCompleteBtn;

        public MaintenanceViewHolder(View v) {
            super(v);
            mDividerLine = v.findViewById(R.id.myorder_maintain_divider_line);
            mLinearLayout = (LinearLayout) v.findViewById(R.id.myorder_maintain_lly);
            mShopAddressView = (TextView) v.findViewById(R.id.myorder_maintain_shopaddress);
            mOrderStateView = (TextView) v.findViewById(R.id.myorder_maintain_orderState);
            mOrderImageView = (ImageView) v.findViewById(R.id.myorder_maintain_orderImage);
            mOrderItemView = (TextView) v.findViewById(R.id.myorder_maintain_orderItem);
            mOrderItemNumView = (TextView) v.findViewById(R.id.myorder_maintain_orderItemNum);
            mOrderPriceView = (TextView) v.findViewById(R.id.myorder_maintain_orderPrice);
            mBut = (Button) v.findViewById(R.id.myorder_maintain_but);
            mSendCommentBtn = (Button) v.findViewById(R.id.myorder_maintain_send_comment);
            mVerifyCompleteBtn = (Button) v.findViewById(R.id.myorder_maintain_verify_Complete);
            mRefundStatusText = (TextView) v.findViewById(R.id.myorder_maintain_refundStatusText);
        }
    }

    /**
     * 新车自营
     */
    class NewAutoViewHolder extends RecyclerView.ViewHolder {


        ImageView mOrderTypeView;
        View view;
        TextView mOrderStatusView;
        TextView mOrderDescView;
        OrderBottom mOrderBottomView;
        OrderDescriptionForMyOrder mOrderDescriptionView;
        View mLineView;

        public NewAutoViewHolder(View v) {
            super(v);
            view = v.findViewById(R.id.myorder_ebs_divider_line);
            mOrderDescView = (TextView) v.findViewById(R.id.myorder_ebs_orderdesc);
            mOrderTypeView = (ImageView) v.findViewById(R.id.myorder_ebs_type);
            mOrderBottomView = (OrderBottom) v.findViewById(R.id.myorder_ebs_bottom);
            mOrderStatusView = (TextView) v.findViewById(R.id.myorder_ebs_status);
            mOrderDescriptionView = (OrderDescriptionForMyOrder) v.findViewById(R.id.myorder_ebs_description);
            mLineView = v.findViewById(R.id.myorder_ebs_line);
        }
    }

    /**
     * 维修
     */
    class RepairViewHolder extends RecyclerView.ViewHolder {
        TextView mShopAdrressView; //店铺名称
        ImageView mOrderImageView; //图片
        TextView mCheckTypeView; //检查类型
        TextView mAboutTime; //预约时间
        View mDividerLine;
        LinearLayout mLinearLayout;
        TextView mOrderState;
        Button mSendComment;
        RelativeLayout mSendCommentrly;

        public RepairViewHolder(View v) {
            super(v);
            mDividerLine = v.findViewById(R.id.myorder_carbooking_divider_line);
            mLinearLayout = (LinearLayout) v.findViewById(R.id.myorder_carbooking_lly);
            mShopAdrressView = (TextView) v.findViewById(R.id.myorder_carbooking_shopaddress);
            mOrderImageView = (ImageView) v.findViewById(R.id.myorder_carbooking_orderImage);
            mCheckTypeView = (TextView) v.findViewById(R.id.myorder_carbooking_orderType);
            mAboutTime = (TextView) v.findViewById(R.id.myorder_carbooking_orderTime);
            mOrderState = (TextView) v.findViewById(R.id.myorder_carbooking_orderState);
            mSendCommentrly = (RelativeLayout) v.findViewById(R.id.myorder_carbooking_send_comment_rly);
            mSendComment = (Button) v.findViewById(R.id.myorder_carbooking_send_comment);
        }
    }

//    /**
//     * 新能源
//     */
//    class NewEnergViewHolder extends RecyclerView.ViewHolderTxt {
//
//        ImageView mOrderTypeView;
//        View view;
//        TextView mOrderIdView;
//        TextView mOrderStatusView;
//        OrderBottom mOrderBottomView;
//        OrderDescriptionForMyOrder mOrderDescriptionView;
//        View mLineView;
//
//        public NewEnergViewHolder(View v) {
//            super(v);
//            view = v.findViewById(R.id.divider_line);
//            mOrderTypeView = (ImageView) v.findViewById(R.id.order_type);
//            mOrderIdView = (TextView) v.findViewById(R.id.order_id);
//            mOrderBottomView = (OrderBottom) v.findViewById(R.id.order_bottom);
//            mOrderStatusView = (TextView) v.findViewById(R.id.order_status);
//            mOrderDescriptionView = (OrderDescriptionForMyOrder) v.findViewById(R.id.order_description);
//            mLineView = v.findViewById(R.id.line);
//        }
//    }

    /**
     * 门店活动
     */
    class ShopPormotionViewHolder extends RecyclerView.ViewHolder {

        TextView mShopAddress;
        View dividerLineView;
        TextView mOrderStatusView;
        ImageView mOrderImageView;
        TextView mFavorableNameView;
        TextView mFavorableTimeView;
        LinearLayout mOrderDeatailView;
        Button mOrderStatusButtonView;
        LinearLayout mOrderStatusBottomView;
        TextView mOrderRefundStatusView;
        TextView mCaptchaView;

        public ShopPormotionViewHolder(View itemView) {
            super(itemView);
            mShopAddress = (TextView) itemView.findViewById(R.id.myorder_online4s_shopaddress);
            dividerLineView = itemView.findViewById(R.id.myorder_online4s_divider_line);
            mOrderStatusView = (TextView) itemView.findViewById(R.id.myorder_online4s_status);
            mOrderRefundStatusView = (TextView) itemView.findViewById(R.id.myorder_online4s_refundStatus);
            mOrderImageView = (ImageView) itemView.findViewById(R.id.myorder_online4s_image);
            mFavorableNameView = (TextView) itemView.findViewById(R.id.myorder_online4s_name);
            mFavorableTimeView = (TextView) itemView.findViewById(R.id.myorder_online4s_time);
            mOrderDeatailView = (LinearLayout) itemView.findViewById(R.id.myorder_online4s_detail);
            mOrderStatusButtonView = (Button) itemView.findViewById(R.id.myorder_online4s_button);
            mOrderStatusBottomView = (LinearLayout) itemView.findViewById(R.id.myorder_online4s_bottom);
            mCaptchaView = (TextView) itemView.findViewById(R.id.myorder_online4s_captcha);
        }
    }

    /**
     * 违章处理
     */
    class WZServiceViewHolder extends RecyclerView.ViewHolder {

        View dividerLineView;
        TextView mOrderStatusView;
        TextView mCarNumView;
        TextView mServerTypeView;
        TextView mOrderRefundStatusView;
        TextView mOrderPiceView;
        LinearLayout mOrderDetailView;
        Button mButView;

        public WZServiceViewHolder(View itemView) {
            super(itemView);
            dividerLineView = itemView.findViewById(R.id.myorder_wzService_divider_line);
            mOrderStatusView = (TextView) itemView.findViewById(R.id.myorder_wzService_status);
            mCarNumView = (TextView) itemView.findViewById(R.id.myorder_wzService_carNum);
            mServerTypeView = (TextView) itemView.findViewById(R.id.myorder_wzService_server_type);
            mOrderPiceView = (TextView) itemView.findViewById(R.id.myorder_wzService_pice);
            mOrderDetailView = (LinearLayout) itemView.findViewById(R.id.myorder_wzService_detail);
            mButView = (Button) itemView.findViewById(R.id.myorder_wzService_button);
            mOrderRefundStatusView = (TextView) itemView.findViewById(R.id.myorder_wzService_refundStatus);
        }
    }

    /**
     * 驾驶证换证
     */
    class ChangeLicenceViewHolder extends RecyclerView.ViewHolder {

        View dividerLineView;
        TextView mOrderStatusView;
        TextView mServerTypeView;
        TextView mOrderPiceView;
        TextView mOrderRefundStatusView;
        LinearLayout mOrderDetailView;
        Button mButView;

        public ChangeLicenceViewHolder(View itemView) {
            super(itemView);
            dividerLineView = itemView.findViewById(R.id.myorder_changeLicence_divider_line);
            mOrderStatusView = (TextView) itemView.findViewById(R.id.myorder_changeLicence_status);
            mServerTypeView = (TextView) itemView.findViewById(R.id.myorder_changeLicence_server_type);
            mOrderPiceView = (TextView) itemView.findViewById(R.id.myorder_changeLicence_pice);
            mOrderDetailView = (LinearLayout) itemView.findViewById(R.id.myorder_changeLicence_detail);
            mButView = (Button) itemView.findViewById(R.id.myorder_changeLicence_button);
            mOrderRefundStatusView = (TextView) itemView.findViewById(R.id.myorder_changeLicence_refundStatus);
        }
    }

    /**
     * 车辆年检服务
     */
    class AnnualnspectionViewHolder extends RecyclerView.ViewHolder {

        View dividerLineView;
        TextView mOrderStatusView;
        TextView mCarNumView;
        TextView mServerTypeView;
        TextView mOrderPiceView;
        TextView mOrderRefundStatusView;
        LinearLayout mOrderDetailView;
        Button mButView;

        public AnnualnspectionViewHolder(View itemView) {
            super(itemView);
            dividerLineView = itemView.findViewById(R.id.myorder_annualnspection_divider_line);
            mOrderStatusView = (TextView) itemView.findViewById(R.id.myorder_annualnspection_status);
            mCarNumView = (TextView) itemView.findViewById(R.id.myorder_annualnspection_carNum);
            mServerTypeView = (TextView) itemView.findViewById(R.id.myorder_annualnspection_server_type);
            mOrderPiceView = (TextView) itemView.findViewById(R.id.myorder_annualnspection_pice);
            mOrderDetailView = (LinearLayout) itemView.findViewById(R.id.myorder_annualnspection_detail);
            mButView = (Button) itemView.findViewById(R.id.myorder_annualnspection_button);
            mOrderRefundStatusView = (TextView) itemView.findViewById(R.id.myorder_annualnspection_refundStatus);
        }
    }

    /**
     * 用品
     */
    class AccessoryViewHolder extends RecyclerView.ViewHolder {
        TextView mShopAddressView; //店铺地址
        TextView mOrderState; //订单状态
        ListViewNoSlide mOrderItem; //用品列表
        TextView mOrderItemNum; //商品总数
        TextView mOrderPic; //支付金额
        Button mButton;  //支付按钮
        Button mVerifyCompleteBtn;  //确认收货
        View mDividerLine;
        TextView mRefundStatusText;

        public AccessoryViewHolder(View v) {
            super(v);
            mDividerLine = v.findViewById(R.id.myorder_accessory_divider_line);
            mShopAddressView = (TextView) v.findViewById(R.id.myorder_accessory_shopaddress);
            mOrderState = (TextView) v.findViewById(R.id.myorder_accessory_orderState);
            mOrderItem = (ListViewNoSlide) v.findViewById(R.id.myorder_accessory_item);
            mOrderItemNum = (TextView) v.findViewById(R.id.myorder_accessory_orderTotal);
            mOrderPic = (TextView) v.findViewById(R.id.myorder_accessory_orderDeposit);
            mButton = (Button) v.findViewById(R.id.myorder_accessory_but);
            mVerifyCompleteBtn = (Button) v.findViewById(R.id.myorder_accessory_verify_Complete);
            mRefundStatusText = (TextView) v.findViewById(R.id.myorder_accessory_refundStatusText);
        }
    }

    /**
     * 特价车
     */
    class ShopSeckillViewHolder extends RecyclerView.ViewHolder {

        View dividerLineView;
        TextView mOrderStatusView;
        TextView mShopAddressView;
        TextView mOrderPrice;
        TextView mRefundStatusView;
        OrderDescription mOrderDescription;
        Button mButView;

        public ShopSeckillViewHolder(View itemView) {
            super(itemView);
            dividerLineView = itemView.findViewById(R.id.myorder_seckill_divider_line);
            mOrderStatusView = (TextView) itemView.findViewById(R.id.myorder_seckill_orderState);
            mShopAddressView = (TextView) itemView.findViewById(R.id.myorder_seckill_shopaddress);
            mOrderPrice = (TextView) itemView.findViewById(R.id.myorder_seckill_orderPrice);
            mOrderDescription = (OrderDescription) itemView.findViewById(R.id.myorder_seckill_description);
            mButView = (Button) itemView.findViewById(R.id.myorder_seckill_but);
            mRefundStatusView = (TextView) itemView.findViewById(R.id.myorder_seckill_refundStatusText);
        }
    }

}
