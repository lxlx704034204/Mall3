package com.hxqc.mall.activity.me.bill;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.hxqc.aroundservice.config.OrderDetailContants;
import com.hxqc.aroundservice.util.ActivitySwitchAround;
import com.hxqc.mall.activity.BackActivity;
import com.hxqc.mall.auto.config.AutoInfoContants;
import com.hxqc.mall.control.BillTypeHelper;
import com.hxqc.mall.core.api.LoadingAnimResponseHandler;
import com.hxqc.mall.core.api.UserApiClient;
import com.hxqc.mall.core.model.bill.BillDetail;
import com.hxqc.mall.core.model.bill.Preferential;
import com.hxqc.mall.core.util.ActivitySwitchBase;
import com.hxqc.mall.core.util.activityutil.ActivitySwitcher;
import com.hxqc.mall.core.views.BaseMapView;
import com.hxqc.mall.thirdshop.accessory.utils.ActivitySwitcherAccessory;
import com.hxqc.mall.thirdshop.accessory4s.utils.ActivitySwitcherAccessory4S;
import com.hxqc.mall.thirdshop.utils.ActivitySwitcherThirdPartShop;
import com.hxqc.mall.views.bill.NormalMapListView;
import com.hxqc.mall.views.bill.NormalMapView;
import com.hxqc.util.DebugLog;
import com.hxqc.util.JSONUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import hxqc.mall.R;

/**
 * Author:李烽
 * Date:2016-03-05
 * FIXME
 * Todo 账单详情
 */
public class BillDetailActivity extends BackActivity implements BaseMapView.OnValueClickListener {
    public static final String TYPE = "detail_type";
    public static final String ID = "detail_id";
    //    public static final int TYPE_SCORE = 300;
//    public static final int TYPE_COMSUMPTION = 100;
    public String billID = "";
    //    String actionBarTxt = "账单详情";
    private ImageView icon;//图标
    //    private int iconId;//图标id
//    private TextView typeTextView;//类型
//    private String typeStr;//类型
    private TextView titleTextView;//标题
    private String title;//标题
    private TextView totalTextView;//总数
    private String totalCost;//总数
    private TextView totalUnitTextView;//单位
    private String unit = "元";//单位（分、元）
    private NormalMapListView detailList;
    private LinkedHashMap<String, String> listData = new LinkedHashMap<>();//列表数据
    private BillTypeHelper.Type type;//充值、退款、买车、活动，用品、保养
    private int billType;//积分or现金
    private BillDetail billDetail;
    private LinearLayout rootLayout;

    private String sign = "+";//符号

    public BillDetailActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_detail);
        initView();
    }

    private void initView() {
        rootLayout = (LinearLayout) findViewById(R.id.root_layout);
        icon = (ImageView) findViewById(R.id.bill_pic);
//        typeTextView = (TextView) findViewById(R.id.bill_type);
        titleTextView = (TextView) findViewById(R.id.bill_title);
        totalTextView = (TextView) findViewById(R.id.bill_total);
        totalUnitTextView = (TextView) findViewById(R.id.bill_total_unit);
        detailList = (NormalMapListView) findViewById(R.id.bill_detail_list);
        billType = getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA).getInt(TYPE, BillTypeHelper.TYPE_SCORE);
        billID = getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA).getString(ID);
        loadData();
    }

    private void loadData() {
        new UserApiClient().getBillDetail(billType + "", billID, new LoadingAnimResponseHandler(this) {
            @Override
            public void onSuccess(String response) {
                billDetail = JSONUtils.fromJson(response, new TypeToken<BillDetail>() {
                });
                if (billDetail != null)
                    updateUI();
            }
        });

    }

    private void updateUI() {
        type = BillTypeHelper.getInstance().getBillDetailType(billDetail);
        initSign();
        updateData();
    }

    private void initSign() {
        String transactionType = billDetail.transactionType;
        if (transactionType.equals("10"))
            sign = "+";
        else if (transactionType.equals("20"))
            sign = "-";
        else if (transactionType.equals("30"))
            sign = "+";
        else sign = "+";
    }


    private void updateData() {
        if (billType == BillTypeHelper.TYPE_SCORE) {
            upScoreData();
            DebugLog.i("BillType", "score");
        } else {
            if (type != null) {
                switch (type) {
                    case charge:
                        upChargeData();
                        DebugLog.i("BillType", "charge");
                        break;
                    case refund:
                    case refund_accessory:
                    case refund_buy_car:
                    case refund_inspection:
                    case refund_maintenance:
                    case refund_rapidAppointment:
                    case refund_rapidMaintenance:
                    case refund_repair:
                    case refund_replacement:
//                case refund_score:
                    case refund_selfAccessory:
                    case refund_store_activity:
                    case refund_wash_car:
                    case refund_wei_zhang:
                    case refund_store_skill:
                        upRefund();
                        DebugLog.i("BillType", "refund");
                        break;
                    case repair:
                        upRepairData();
                        DebugLog.i("BillType", "repair");
                        break;
                    case store_activity:
                        upStoreActivityData();
                        DebugLog.i("BillType", "store_activity");
                        break;
                    case buy_car:
                        upBuyCarData();
                        DebugLog.i("BillType", "buy_car");
                        break;
                    case maintenance:
                        upMaintenanceData();
                        DebugLog.i("BillType", "maintenance");
                        break;
                    case score:
                        upScoreData();
                        DebugLog.i("BillType", "score");
                        break;
                    case accessory:
                        upAccessoryData();
                        DebugLog.i("BillType", "accessory");
                        break;
//                case work_order:
//                    upWorkOrderData();
//                    break;
                    case charge_other:
                        upChargeOtherData();
                        break;
                    case rapidMaintenance:
                        upMaintenanceData();
                        break;
                    case rapidAppointment:
                        upRepairData();
                        break;
                    case selfAccessory:
                        upAccessoryData();
                        break;
                    case wei_zhang:
//                        upWZData();
//                        break;
                    case replacement:
//                        upReplacementData();
//                        break;
                    case inspection:
//                        upInspectionData();
//                        break;
                    case wash_car:
//                        upWashCarData();
//                        break;
                    case store_skill:
//                        upStoreSkillData();
//                        break;
                    case default_value:
                        upDefaultData();
                        DebugLog.i("BillType", "default_value");
                        break;
                }
            } else {
                rootLayout.setVisibility(View.GONE);
            }
        }
        refreshView();
        rootLayout.setVisibility(View.VISIBLE);
    }


//    private void upWorkOrderData() {
//        setAmount();
//        normalInfo();
//        payWay();
//        workOrderInfo();
//    }

    private void setAmount() {
        title = "本次消费金额：";
        totalCost = billDetail.amountToPay;
    }

    private void normalInfo() {
        if (!TextUtils.isEmpty(billDetail.description))
            listData.put("交易信息：", billDetail.description);
        if (!TextUtils.isEmpty(billDetail.orderCreateTime))
            listData.put("订单创建时间：", billDetail.orderCreateTime);
        if (!TextUtils.isEmpty(billDetail.orderID))
            listData.put("订单单号：", billDetail.orderID);
    }

    private void payWay() {
        if (!TextUtils.isEmpty(billDetail.paymentID))
            listData.put("支付方式：", billDetail.paymentID);
        if (!TextUtils.isEmpty(billDetail.shopName))
            listData.put("门        店：", billDetail.shopName);
        if (!TextUtils.isEmpty(billDetail.tradeID)) {
            listData.put("交易单号：", billDetail.tradeID);
        }

    }

    private void payWayNoShop() {
        if (!TextUtils.isEmpty(billDetail.paymentID)) {
            listData.put("支付方式：", billDetail.paymentID);
        }
        if (!TextUtils.isEmpty(billDetail.tradeID))
            listData.put("交易单号：", billDetail.tradeID);
    }

    private void workOrderInfo() {
        if (!TextUtils.isEmpty(billDetail.payTime))
            listData.put("交易时间：", billDetail.payTime);
//        if (!TextUtils.isEmpty(billDetail.workOrder) && !TextUtils.isEmpty(billDetail.erpCode))
//            listData.put("查看工单：", billDetail.workOrder);
    }

//    private void upWZData() {
//        setAmount();
//        normalInfo();
//        payWayNoShop();
//        workOrderInfo();
//    }

//    private void upReplacementData() {
//        setAmount();
//        normalInfo();
//        payWayNoShop();
//        workOrderInfo();
//    }

//    private void upInspectionData() {
//        setAmount();
//        normalInfo();
//        payWayNoShop();
//        workOrderInfo();
//    }

//    private void upWashCarData() {
//        setAmount();
//        normalInfo();
//        payWay();
//        workOrderInfo();
//    }

//    private void upStoreSkillData() {
//        setAmount();
//        normalInfo();
//        payWay();
//        workOrderInfo();
//    }

    /**
     * 默认
     */
    private void upDefaultData() {
        setAmount();
        normalInfo();
        payWay();
        workOrderInfo();
    }

    /**
     * 为他人充值
     */
    private void upChargeOtherData() {
        setAmount();
//        normalInfo();
        if (!TextUtils.isEmpty(billDetail.description))
            listData.put("交易信息：", billDetail.description);
        if (!TextUtils.isEmpty(billDetail.storedID))
            listData.put("被充值者账号：", billDetail.storedID);
        if (!TextUtils.isEmpty(billDetail.orderCreateTime))
            listData.put("订单创建时间：", billDetail.orderCreateTime);
        if (!TextUtils.isEmpty(billDetail.orderID))
            listData.put("订单单号：", billDetail.orderID);
        payWay();
        workOrderInfo();
    }

    /**
     * 退款
     */
    private void upRefund() {
        title = "本次退款金额：";
        totalCost = billDetail.amountToPay;
        if (!TextUtils.isEmpty(billDetail.description))
            listData.put("交易信息：", billDetail.description);
        if (!TextUtils.isEmpty(billDetail.orderID))
            listData.put("订单单号：", billDetail.orderID);
        if (!TextUtils.isEmpty(billDetail.paymentID))
            listData.put("退款方式：", billDetail.paymentID);
        if (!TextUtils.isEmpty(billDetail.shopName))
            listData.put("门        店：", billDetail.shopName);
        if (!TextUtils.isEmpty(billDetail.tradeID))
            listData.put("交易单号：", billDetail.tradeID);
        if (!TextUtils.isEmpty(billDetail.orderCreateTime))
            listData.put("申请时间：", billDetail.orderCreateTime);
    }

    /**
     * 维修
     */
    private void upRepairData() {
        setAmount();
        normalInfo();
        payWay();
        if (billDetail.preferential != null && billDetail.preferential.size() > 0)
            listData.put("优惠明细：", getPrefDetail());
        workOrderInfo();
    }

    /**
     * 店铺活动
     */
    private void upStoreActivityData() {
        setAmount();
//        if (!TextUtils.isEmpty(billDetail.description))
//            listData.put("交易信息：", billDetail.description);
        normalInfo();
        payWay();
        if (!TextUtils.isEmpty(billDetail.payTime))
            listData.put("交易时间：", billDetail.payTime);
    }

    /**
     * 买车
     */
    private void upBuyCarData() {
        setAmount();
        normalInfo();
        payWay();
        if (!TextUtils.isEmpty(billDetail.autoPrice))
            listData.put("车辆价格：", billDetail.autoPrice);
        if (!TextUtils.isEmpty(billDetail.payTime))
            listData.put("交易时间：", billDetail.payTime);
    }

    /**
     * 保养
     */
    private void upMaintenanceData() {
        title = "本次消费金额：";
        totalCost = billDetail.amountToPay;
        normalInfo();
        payWay();
        if (billDetail.preferential != null && billDetail.preferential.size() > 0)
            listData.put("优惠明细：", getPrefDetail());
        workOrderInfo();
    }

    /**
     * 获取优惠详情
     *
     * @return
     */
    private String getPrefDetail() {
        String result = "";
        ArrayList<Preferential> preferential = billDetail.preferential;
        if (preferential != null)
            for (int i = 0; i < preferential.size(); i++) {
                if (i == preferential.size() - 1)
//                    result = result + preferential.get(i).consumer + "  \t" + preferential.get(i).couponamount;
                    result = result + preferential.get(i).preferentialLabel + "  \t" + preferential.get(i).preferentialAmount;
                else
//                    result = result + preferential.get(i).consumer + "  \t" + preferential.get(i).couponamount + "\n";
                    result = result + preferential.get(i).preferentialLabel + "  \t" + preferential.get(i).preferentialAmount + "\n";
            }
        return result;
    }


    /**
     * 用品
     */
    private void upAccessoryData() {
        setAmount();
        unit = "元";
        normalInfo();
        payWay();
        if (!TextUtils.isEmpty(billDetail.payTime))
            listData.put("交易时间：", billDetail.payTime);
    }


    /**
     * 充值
     */
    private void upChargeData() {
        title = "本次充值金额：";
        totalCost = billDetail.amountToPay;
        if (!TextUtils.isEmpty(billDetail.description))
            listData.put("交易信息：", billDetail.description);
        if (!TextUtils.isEmpty(billDetail.storedID))
            listData.put("被充值者账号：", billDetail.storedID);
        payWay();
        if (!TextUtils.isEmpty(billDetail.payTime))
            listData.put("交易时间：", billDetail.payTime);
    }


    /**
     * 积分
     */
    private void upScoreData() {
        DebugLog.i(AutoInfoContants.LOG_J,billDetail.toString());
        title = "本次积分：";
        totalCost = billDetail.score;
        unit = "分";
        if (!TextUtils.isEmpty(billDetail.description))
            listData.put("交易信息：", billDetail.description);
        if (!TextUtils.isEmpty(billDetail.orderID))
            listData.put("订单单号：", billDetail.orderID);
        payWay();
        workOrderInfo();
        listData.remove("支付方式：");
    }

    /**
     * 订单号在同一个TextView上面显示，但是响应不同的事件
     *
     * @param strs
     */
    private void setOrderID(String[] strs) {
        StringBuilder actionText = new StringBuilder();
        for (int i = 0; i < strs.length; i++) {
            String str = strs[i];
            if (i == strs.length - 1)
                actionText.append("<a style=\"text-decoration:none;\" href='" + str + "'>" + str + "</a>");
            else
                actionText.append("<a style=\"text-decoration:none;\" href='" + str + "'>" + str + "</a><br><br>");
        }
        DebugLog.d(getClass().getSimpleName(), "actionText:" + actionText);
        NormalMapView mapView = detailList.getMapView("订单单号：");
        if (mapView != null) {
            mapView.setType(BaseMapView.Type.CLICKABLE);
            TextView valueTextView = mapView.getValueTextView();
            valueTextView.setText(Html.fromHtml(actionText.toString()));
            valueTextView.setMovementMethod(LinkMovementMethod.getInstance());
            CharSequence text = valueTextView.getText();
            int ends = text.length();
            Spannable spannable = (Spannable) valueTextView.getText();
            URLSpan[] urlSpen = spannable.getSpans(0, ends, URLSpan.class);
            SpannableStringBuilder stringBuilder = new SpannableStringBuilder(text);
            stringBuilder.clearSpans();
            for (URLSpan urlSpan : urlSpen) {
                TextViewURLSpan span = new TextViewURLSpan(urlSpan.getURL());
                stringBuilder.setSpan(span, spannable.getSpanStart(urlSpan), spannable.getSpanEnd(urlSpan)
                        , spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            valueTextView.setText(stringBuilder);
        }
    }

    private class TextViewURLSpan extends ClickableSpan {
        private String clickString;

        public TextViewURLSpan(String clickString) {
            this.clickString = clickString;
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setColor(Color.parseColor("#2196f3"));
            ds.setUnderlineText(false); //去掉下划线
        }

        @Override
        public void onClick(View widget) {
            toOrderDetail(clickString);
        }
    }

    private void refreshView() {
//        getSupportActionBar().setTitle(actionBarTxt);
        titleTextView.setText(title);
        totalTextView.setText(totalCost);
//        totalTextView.setText(sign + totalCost);
        totalUnitTextView.setText(unit);
        detailList.setKeyValues(listData);

        String order = billDetail.orderID;
        if (!TextUtils.isEmpty(order)) {
            if (order.contains(",")) {
                String[] split = order.split(",");
                setOrderID(split);
            } else {
                String[] split = {order};
                setOrderID(split);
            }
        }

        initClickItem("查看工单：");
//        initClickItem("订单单号");

//        NormalMapView normalMapView = detailList.getMapView("交易信息");
//        normalMapView.getValueTextView().setLines(2);
    }

    private void initClickItem(String key) {
        NormalMapView normalMapView = detailList.getMapView(key);
        if (normalMapView != null) {
            //设置蓝色的样式
            normalMapView.setType(BaseMapView.Type.VALUE_CLICK_MORE);
            normalMapView.setOnValueClickListener(this);
        }
    }

    @Override
    public void onValueClick(BaseMapView view, String value) {
        switch (view.getKey()) {
//            case "订单单号":
//                toOrderDetail(billDetail.orderID);
//                break;
            case "查看工单：":
                String url1 = new UserApiClient().getWorkOrderUrl(value, billDetail.erpCode);
                ActivitySwitchBase.toH5Activity(this, "订单详情", url1);
                break;
            default:
                break;
        }
    }


    /**
     * 跳订单详情
     */
    private void toOrderDetail(String orderID) {
        if (type != null) {
            switch (type) {
                case charge:
                    break;
                case charge_other:
                    ActivitySwitchBase.toOrderDetail(this, orderID);
                    break;
                case refund:
                    ActivitySwitchBase.toOrderDetail(this, orderID);
                    break;
                case refund_buy_car:
                    ActivitySwitchBase.toOrderDetail(this, orderID);
                    break;
                case refund_store_activity:
                    ActivitySwitcherThirdPartShop.toOrderDetail(this, orderID);
                    break;
                case refund_wei_zhang:
                    ActivitySwitchAround.toOrderDetailActivity(this, orderID, OrderDetailContants.FRAGMENT_ILLEGAL_ORDER_DETAIL);
                    break;
                case refund_inspection:
                    ActivitySwitchAround.toOrderDetailActivity(this, orderID, OrderDetailContants.FRAGMENT_VEHICLES_ORDER_DETAIL);
                    break;
                case refund_replacement:
                    ActivitySwitchAround.toOrderDetailActivity(this, orderID, OrderDetailContants.FRAGMENT_LICENSE_ORDER_DETAIL);
                    break;
                case refund_accessory:
                    ActivitySwitcherAccessory4S.toOrderDetail(this, orderID);
                    break;
                case refund_maintenance:
                    ActivitySwitcher.toMaintain4SShopOrderDetail(this, orderID);
                    break;
                case refund_store_skill:
                    ActivitySwitcher.toSeckillOrderDetail(this, orderID);
                    break;
                case refund_wash_car:
                    ActivitySwitcher.toCarWashOrderDetail(this, orderID);
                    break;
                case refund_rapidMaintenance:
                    ActivitySwitcher.toMaintainOrderDetail(this, orderID);
                    break;
                case refund_rapidAppointment:
                    ActivitySwitcher.toRepairOrderDetail(this, orderID);
                    break;
                case refund_selfAccessory:
                    ActivitySwitcherAccessory.toOrderDetail(this, orderID);
                    break;
                case repair:
                    ActivitySwitcher.toRepairOrderDetail(this, orderID);
                    break;
                case store_activity:
                    ActivitySwitcherThirdPartShop.toOrderDetail(this, orderID);
                    break;
                case buy_car:
                    ActivitySwitchBase.toOrderDetail(this, orderID);
                    break;
                case maintenance:
                    ActivitySwitcher.toMaintain4SShopOrderDetail(this, orderID);
                    break;
                case score:
                    break;
                case accessory:
//                    ActivitySwitcherAccessory.toOrderDetail(this, orderID);
                    ActivitySwitcherAccessory4S.toOrderDetail(this, orderID);
                    break;
                case wei_zhang:
                    ActivitySwitchAround.toOrderDetailActivity(this, orderID,
                            OrderDetailContants.FRAGMENT_ILLEGAL_ORDER_DETAIL);
                    break;
                case replacement:
                    ActivitySwitchAround.toOrderDetailActivity(this, orderID,
                            OrderDetailContants.FRAGMENT_LICENSE_ORDER_DETAIL);
                    break;
                case inspection:
                    ActivitySwitchAround.toOrderDetailActivity(this, orderID,
                            OrderDetailContants.FRAGMENT_VEHICLES_ORDER_DETAIL);
                    break;
                case wash_car:
                    ActivitySwitcher.toCarWashOrderDetail(this, orderID);
                    break;
                case store_skill:
                    ActivitySwitcher.toSeckillOrderDetail(this, orderID);
                    break;
                case rapidMaintenance:
                    ActivitySwitcher.toMaintainOrderDetail(this, orderID);
                    break;
                case rapidAppointment:
                    //快修预约
                    break;
                case selfAccessory:
//                    ActivitySwitcherAccessory4S.toOrderDetail(this, orderID);
                    ActivitySwitcherAccessory.toOrderDetail(this, orderID);
                    break;
                case default_value:
                    break;
            }
        } else {
            //没有数据

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BillTypeHelper.getInstance().destroy();
    }
}
