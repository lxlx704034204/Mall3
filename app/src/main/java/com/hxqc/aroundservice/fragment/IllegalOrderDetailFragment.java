package com.hxqc.aroundservice.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hxqc.aroundservice.config.OrderDetailContants;
import com.hxqc.aroundservice.control.IllegalQueryControl;
import com.hxqc.aroundservice.model.IllegalOrderDetail;
import com.hxqc.aroundservice.util.ActivitySwitchAround;
import com.hxqc.aroundservice.view.ChoosePictureLayout;
import com.hxqc.aroundservice.view.DialogImageView;
import com.hxqc.aroundservice.view.DialogTextView;
import com.hxqc.aroundservice.view.IllegalAutoInfoView;
import com.hxqc.aroundservice.view.ListItemView;
import com.hxqc.aroundservice.view.QueryResultBarV2;
import com.hxqc.mall.auto.config.AutoInfoContants;
import com.hxqc.mall.auto.inter.CallBackControl;
import com.hxqc.mall.auto.view.CommonTwoTextView;
import com.hxqc.mall.core.api.RequestFailView;
import com.hxqc.mall.core.model.ImageModel;
import com.hxqc.mall.core.util.ActivitySwitchBase;
import com.hxqc.mall.core.util.ImageUtil;
import com.hxqc.mall.core.util.OtherUtil;
import com.hxqc.mall.core.util.ToastHelper;
import com.hxqc.mall.core.views.HeightVSWidthImageView;
import com.hxqc.mall.payment.util.PaymentActivitySwitch;
import com.hxqc.util.DebugLog;
import com.hxqc.xiaoneng.ChatManager;

import java.util.HashMap;
import java.util.Map;

import hxqc.mall.R;


/**
 * Author:胡仲俊
 * Date: 2016 - 05 - 04
 * Des: 违章详情页面
 * FIXME
 * Todo
 */
public class IllegalOrderDetailFragment extends BasePhotoChooseFragmentV2 {

    private static final String TAG = AutoInfoContants.LOG_J;
    private View rootView;
    private QueryResultBarV2 mBarView;
    private LinearLayout mInfoView;
    private LinearLayout mIllegalListView;
    private CommonTwoTextView mTotalView;
    private CommonTwoTextView mAgentsView;
    private CommonTwoTextView mFineView;
    private TextView mPaymentView;
    private TextView mPhoneView;
    private TextView mNameView;
    private TextView mPointView;
    private TextView mRecordView;
    private IllegalAutoInfoView mAutoView;
    private TextView mNumberView;
    private TextView mStateView;
    //    private QueryProcessingPhotoFragment mOriginalFragment;
//    private QueryProcessingPhotoFragment mCopyFragment;
    private String imOrderID;
    private IllegalOrderDetail imIllegalOrderDetail;
    private MenuItem imCancel;
    private Button mCopyReuploadView;
    private Button mOriginalReuploadView;
    private RelativeLayout mOriginalAreaView;
    private HeightVSWidthImageView mOriginalBGView;
    private RelativeLayout mCopyAreaView;
    private HeightVSWidthImageView mCopyBGView;
    private ImageModel imImageModel;
    private TextView mRefundStateView;
    private LinearLayout mItemPaymentView;
    private LinearLayout mItemRefundStateView;
    private LinearLayout mCustomerServiceView;
    private RelativeLayout mParentView;
    private RequestFailView mRequestFailView;
    private TextView mOrderCreateTimeView;
    private ListItemView mPayTimeParentView;
    private TextView mPayTimeView;
    private DialogImageView mOriginalSampleView;
    private DialogImageView mCopySampleView;
    private DialogTextView mOriginalSampleTitleView;
    private DialogTextView mCopySampleTitleView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_order_detail_illegal, container, false);
        }
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView();

        initData();

        initEvent();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    private void initEvent() {
        mBarView.setOnClickPayListener(onClickPayListener);
        mIllegalListView.setOnClickListener(onClickListListener);

//        mOriginalFragment.setOnReuploadListener(reuploadOriginalListener);
//        mCopyFragment.setOnReuploadListener(reuploadCopyListener);
        mOriginalAreaView.setOnClickListener(onClickOriginalAreaListener);
        mCopyAreaView.setOnClickListener(onClickCopyAreaListener);

        mCustomerServiceView.setOnClickListener(onClickCallServiceListener);

//        mOriginalSampleView.showSamplePhoto(getActivity(),R.drawable.ic_sample_license_origianl);
//        mCopySampleView.showSamplePhoto(getActivity(),R.drawable.ic_sample_license_copy);
//        mOriginalSampleTitleView.showSamplePhoto(getActivity(),R.drawable.ic_sample_license_origianl);
//        mCopySampleTitleView.showSamplePhoto(getActivity(),R.drawable.ic_sample_license_copy);

    }

    private void initData() {

        if (getActivity().getIntent() != null) {
//            imOrderID = getActivity().getIntent().getStringExtra("orderID");
            Bundle bundleExtra = getActivity().getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA);
            imOrderID = bundleExtra.getString("orderID");
            DebugLog.i(TAG, "orderID:" + imOrderID);
        }
//        orderID = "1111";
        IllegalQueryControl.getInstance().getIllegalOrderDetail(getActivity(), imOrderID, illegalOrderInfoCallBack);

    }

    private void initView() {
        mParentView = (RelativeLayout) rootView.findViewById(R.id.illegal_order_info_parent);
        mParentView.setVisibility(View.GONE);

        mBarView = (QueryResultBarV2) rootView.findViewById(R.id.illegal_bar);
        mBarView.setBarVisibility(View.GONE, View.GONE, View.VISIBLE);
        mBarView.setIllegalText(View.VISIBLE, "去付款", "支付金额:", 4, OtherUtil.amountFormat("0", true));
        mInfoView = (LinearLayout) rootView.findViewById(R.id.query_result_v2_info);
        mIllegalListView = (LinearLayout) rootView.findViewById(R.id.illegal_list);
        mTotalView = (CommonTwoTextView) rootView.findViewById(R.id.illegal_order_info_total);
        mAgentsView = (CommonTwoTextView) rootView.findViewById(R.id.illegal_order_info_agents);
        mFineView = (CommonTwoTextView) rootView.findViewById(R.id.illegal_order_info_fine);
        mPhoneView = (TextView) rootView.findViewById(R.id.illegal_order_info_phone);
        mNameView = (TextView) rootView.findViewById(R.id.illegal_order_info_name);
        mPaymentView = (TextView) rootView.findViewById(R.id.illegal_payment);
        mPointView = (TextView) rootView.findViewById(R.id.illegal_order_info_point);
        mRecordView = (TextView) rootView.findViewById(R.id.illegal_order_info_record);
        mAutoView = (IllegalAutoInfoView) rootView.findViewById(R.id.illegal_order_info_auto);
        mAutoView.setBackground(R.drawable.pic_illegal);

        mNumberView = (TextView) rootView.findViewById(R.id.illegal_order_info_number);
        mStateView = (TextView) rootView.findViewById(R.id.illegal_order_info_state);

        ChoosePictureLayout mOriginalView = (ChoosePictureLayout) rootView.findViewById(R.id.illegal_original);
        TextView mOriginalTitleView = mOriginalView.getTitleView();
        mOriginalAreaView = mOriginalView.getAreaView();
        mOriginalBGView = mOriginalView.getBGView();
        mOriginalReuploadView = mOriginalView.getReuploadView();
        mOriginalSampleView = mOriginalView.getSampleView();
        mOriginalSampleTitleView = mOriginalView.getSampleTitleView();
        mOriginalTitleView.setText(getString(R.string.license_original));

        ChoosePictureLayout mCopyView = (ChoosePictureLayout) rootView.findViewById(R.id.illegal_copy);
        TextView mCopyTitleView = mCopyView.getTitleView();
        mCopyAreaView = mCopyView.getAreaView();
        mCopyBGView = mCopyView.getBGView();
        mCopyReuploadView = mCopyView.getReuploadView();
        mCopySampleView = mCopyView.getSampleView();
        mCopySampleTitleView = mCopyView.getSampleTitleView();
        mCopyTitleView.setText(getString(R.string.license_copy));

//        mOriginalFragment = (QueryProcessingPhotoFragment) getChildFragmentManager().findFragmentById(R.id.illegal_original);
//        mCopyFragment = (QueryProcessingPhotoFragment) getChildFragmentManager().findFragmentById(R.id.illegal_copy);
//        mOriginalFragment.setTitle("行驶证正本:");
//        mCopyFragment.setTitle("行驶证副本:");
        mRefundStateView = (TextView) rootView.findViewById(R.id.illegal_refund_state);
        mItemRefundStateView = (LinearLayout) rootView.findViewById(R.id.illegal_item_refund_state);
        mItemPaymentView = (LinearLayout) rootView.findViewById(R.id.illegal_item_payment);
        mCustomerServiceView = (LinearLayout) rootView.findViewById(R.id.illegal_order_info_customer_service);

        mRequestFailView = (RequestFailView) rootView.findViewById(R.id.illegal_no_data);

        mOrderCreateTimeView = (TextView) rootView.findViewById(R.id.illegal_order_create_time);
        mPayTimeParentView = (ListItemView) rootView.findViewById(R.id.illegal_pay_time_parent);
        mPayTimeView = (TextView) rootView.findViewById(R.id.illegal_pay_time);


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            if (requestCode == OrderDetailContants.REQUEST_CANCEL) {
                DebugLog.i(TAG, "成功");
                IllegalQueryControl.getInstance().getIllegalOrderDetail(getActivity(), imOrderID, illegalOrderInfoCallBack);
            }
        }
    }

    private int disposeIndex = 0;
    private int refund = 0;
    private String imDrivingLicenseFile2;
    private String imDrivingLicenseFile1;
    private CallBackControl.CallBack<IllegalOrderDetail> illegalOrderInfoCallBack = new CallBackControl.CallBack<IllegalOrderDetail>() {
        @Override
        public void onSuccess(IllegalOrderDetail response) {
            mParentView.setVisibility(View.VISIBLE);
            imIllegalOrderDetail = response;
            if (response != null) {
                if (response.orderStatus.equals("10")) {
                    imCancel.setVisible(true);
                    mBarView.setBarVisibility(View.GONE, View.GONE, View.VISIBLE);
                    mBarView.setIllegalText(View.GONE, "去付款", "支付金额:", 4, OtherUtil.amountFormat("0", true));
//                    mOriginalFragment.setReuploadVisibility(View.VISIBLE);
//                    mCopyFragment.setReuploadVisibility(View.VISIBLE);
//                    mOriginalFragment.setClickReuploadListener();
//                    mCopyFragment.setClickReuploadListener();
                    mOriginalReuploadView.setVisibility(View.VISIBLE);
                    mOriginalReuploadView.setOnClickListener(clickOriginalReuploadListener);
                    mCopyReuploadView.setVisibility(View.VISIBLE);
                    mCopyReuploadView.setOnClickListener(clickCopyReuploadListener);
                } else if (response.orderStatus.equals("20")) {
                    imCancel.setVisible(true);
                    mBarView.setBarVisibility(View.GONE, View.GONE, View.VISIBLE);
                    mBarView.setIllegalText(View.VISIBLE, "去付款", "支付金额:", 4, OtherUtil.amountFormat("0", true));
                } else {
                    imCancel.setVisible(false);
                    mBarView.setPayBtnState(View.GONE);
//                    mOriginalFragment.setReuploadVisibility(View.GONE);
//                    mCopyFragment.setReuploadVisibility(View.GONE);
                    mOriginalReuploadView.setVisibility(View.GONE);
                    mCopyReuploadView.setVisibility(View.GONE);
                }
                mNumberView.setText("订单号:  " + imOrderID);
                mAutoView.setData(response.plateNumber, "违章查缴", OtherUtil.amountFormat(response.amount, true));
                if (response.wzList != null && !response.wzList.isEmpty()) {
                    int size = response.wzList.size();
                    for (int i = 0; i < size; i++) {
                        if (!TextUtils.isEmpty(response.wzList.get(i).statusCode)) {
                            if (response.wzList.get(i).statusCode.equals("40")) {
                                ++disposeIndex;
                            } else if (response.wzList.get(i).statusCode.equals("50")) {
                                ++refund;
                            }
                        }
                    }
                }

                if (disposeIndex == 0 && refund == 0) {
                    mPointView.setVisibility(View.GONE);
                } else if (disposeIndex > 0 && refund == 0) {
                    mPointView.setText("已处理" + disposeIndex + "条");
                } else if (disposeIndex == 0 && refund > 0) {
                    mPointView.setText("已退款" + refund + "条");
                } else if (disposeIndex > 0 && refund > 0) {
                    mPointView.setText("已处理" + disposeIndex + "条," + "已退款" + refund + "条");
                }

                mRecordView.setText("共" + response.count + "条违章记录");
                mNameView.setText(response.username);
                mPhoneView.setText(response.phone);

                if (TextUtils.isEmpty(response.paymentID)) {
                    mItemPaymentView.setVisibility(View.GONE);
                } else {
                    mItemPaymentView.setVisibility(View.VISIBLE);
                    mPaymentView.setText(response.paymentID);
                }

                mStateView.setText(response.orderStatusText);

                if (TextUtils.isEmpty(response.orderCreateTime)) {
                    mOrderCreateTimeView.setText(response.orderCreateTime);
                } else {
                    mOrderCreateTimeView.setText(response.orderCreateTime);
                }

                if (TextUtils.isEmpty(response.paidTime)) {
                    mPayTimeParentView.setVisibility(View.GONE);
                } else {
                    mPayTimeParentView.setVisibility(View.VISIBLE);
                    mPayTimeView.setText(response.paidTime);
                }

//                mStateView.setTextColor(BaseOrderStatus.getStatusColor(getActivity(), response.orderStatusText));
                mFineView.setTwoText(OtherUtil.amountFormat(response.fine, true));
                mAgentsView.setTwoText(OtherUtil.amountFormat(response.serviceCharge, true));
                mTotalView.setTwoText(OtherUtil.amountFormat(response.amount, true));
                mBarView.setIllegalMoney(OtherUtil.amountFormat(response.amount, true));

//                mOriginalFragment.setBackground(getActivity(), illegalOrderInfo.drivingLicenseFile1);
//                mCopyFragment.setBackground(getActivity(), illegalOrderInfo.drivingLicenseFile2);
                ImageUtil.setImage(getActivity(), mOriginalBGView, response.drivingLicenseFile1, R.drawable.ic_common_pictures);
                ImageUtil.setImage(getActivity(), mCopyBGView, response.drivingLicenseFile2, R.drawable.ic_common_pictures);
                imDrivingLicenseFile1 = response.drivingLicenseFile1;
                imDrivingLicenseFile2 = response.drivingLicenseFile2;
                if (TextUtils.isEmpty(response.refundStatusText)) {
                    mItemRefundStateView.setVisibility(View.GONE);
                } else {
                    mItemRefundStateView.setVisibility(View.VISIBLE);
                    mRefundStateView.setText(response.refundStatusText);
                 /*   if (response.refundStatus.equals("20")) {
                        mRefundStateView.setText("待退款");
                        mRefundStateView.setTextColor(getResources().getColor(R.color.order_status_red));
                    } else if (response.refundStatus.equals("30")) {
                        mRefundStateView.setText("退款中");
                        mRefundStateView.setTextColor(getResources().getColor(R.color.order_status_orange));
                    } else if (response.refundStatus.equals("40")) {
                        mRefundStateView.setText("退款完成");
                        mRefundStateView.setTextColor(getResources().getColor(R.color.order_status_green));
                    }*/
                }
            }
        }

        @Override
        public void onFailed(boolean offLine) {
            mRequestFailView.setRequestViewType(RequestFailView.RequestViewType.fail);
            mRequestFailView.setFailButtonClick("重新加载", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    IllegalQueryControl.getInstance().getIllegalOrderDetail(getActivity(), imOrderID, illegalOrderInfoCallBack);
                }
            });
        }
    };

    /**
     * 支付点击事件
     */
    private QueryResultBarV2.OnClickPayListener onClickPayListener = new QueryResultBarV2.OnClickPayListener() {
        @Override
        public void clickPayListener(View v) {
            if (imIllegalOrderDetail != null) {
                PaymentActivitySwitch.toAroundPaymentActivity(getActivity(), imIllegalOrderDetail.amount, imOrderID, OrderDetailContants.ILLEGAL_AND_COMMISSION, "-1");
            }
        }
    };

    /**
     * 去违章列表的点击事件
     */
    private View.OnClickListener onClickListListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (imIllegalOrderDetail != null && !imIllegalOrderDetail.wzList.isEmpty()) {
                ActivitySwitchAround.toIllegalQueryResultActivity(getActivity(), OrderDetailContants.ILLEGAL_DETAIL_LIST, imIllegalOrderDetail);
            }
        }
    };

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_illegal_order_info, menu);
        imCancel = menu.findItem(R.id.illegal_order_info_cancel);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.illegal_order_info_cancel) {
            DebugLog.i(TAG, "取消订单");
            ActivitySwitchAround.toCancelOrderDetailActivity(IllegalOrderDetailFragment.this, imOrderID, OrderDetailContants.FRAGMENT_ILLEGAL_ORDER_DETAIL);
        }
        return false;
    }

    /**
     * 上传后的回调操作
     */
    private CallBackControl.CallBack<String> illegalOrderCallBack = new CallBackControl.CallBack<String>() {
        @Override
        public void onSuccess(String response) {
            ToastHelper.showGreenToast(getActivity(), "上传成功");
            if (isOriginal) {
                if (!TextUtils.isEmpty(cachePath.get(0))) {
                    DebugLog.i(TAG, FILE_PATH_HEAD + cachePath.get(0));
                    ImageUtil.setImage(getActivity(), mOriginalBGView, FILE_PATH_HEAD + cachePath.get(0), R.drawable.ic_common_pictures);
                    imDrivingLicenseFile1 = FILE_PATH_HEAD + cachePath.get(0);
                }
            } else {
                if (!TextUtils.isEmpty(cachePath.get(1))) {
                    DebugLog.i(TAG, FILE_PATH_HEAD + cachePath.get(1));
                    ImageUtil.setImage(getActivity(), mCopyBGView, FILE_PATH_HEAD + cachePath.get(1), R.drawable.ic_common_pictures);
                    imDrivingLicenseFile2 = FILE_PATH_HEAD + cachePath.get(1);
                }
            }
        }

        @Override
        public void onFailed(boolean offLine) {
            if (imIllegalOrderDetail != null) {
                ImageUtil.setImage(getActivity(), mOriginalBGView, imDrivingLicenseFile1, R.drawable.ic_common_pictures);
                ImageUtil.setImage(getActivity(), mCopyBGView, imDrivingLicenseFile2, R.drawable.ic_common_pictures);

            }
        }
    };

    private View.OnClickListener onClickCallServiceListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
//            new CallBar.CallPhoneDialog(getActivity(), getActivity().getString(R.string.tv_service_phone)).show();
            ChatManager.getInstance().startHistoryGoods("消息", "null");
        }
    };

    private Map<Integer, String> cachePath;//缓存本地图片地址
    private final String FILE_PATH_HEAD = "file://";

    @Override
    protected void chooseSuccess(String photoPath, int requestCode) {
        String mFilePath = FILE_PATH_HEAD + photoPath;
        DebugLog.i(TAG, "mFilePath:" + mFilePath);
        if (cachePath == null) {
            cachePath = new HashMap<>();
        }
        if (isOriginal) {
            if (!TextUtils.isEmpty(photoPath)) {
                cachePath.put(0, photoPath);
                IllegalQueryControl.getInstance().editIllegalOrder(getActivity(), imOrderID, photoPath, "", illegalOrderCallBack);
                DebugLog.i(TAG, "chooseSuccess:" + cachePath.get(0));
            }

        } else {
            if (!TextUtils.isEmpty(photoPath)) {
                cachePath.put(1, photoPath);
                IllegalQueryControl.getInstance().editIllegalOrder(getActivity(), imOrderID, "", photoPath, illegalOrderCallBack);
                DebugLog.i(TAG, "chooseSuccess:" + cachePath.get(1));
            }
        }
    }

    /*@Override
    public void chooseSuccess(String filePath) {
        String mFilePath = FILE_PATH_HEAD + filePath;
        DebugLog.i(TAG, "mFilePath:" + mFilePath);
        if (cachePath == null) {
            cachePath = new HashMap<>();
        }
        if (isOriginal) {
            if (!TextUtils.isEmpty(filePath)) {
                cachePath.put(0, filePath);
                IllegalQueryControl.getInstance().editIllegalOrder(getActivity(), imOrderID, filePath, "", illegalOrderCallBack);
                DebugLog.i(TAG, "chooseSuccess:" + cachePath.get(0));
            }

        } else {
            if (!TextUtils.isEmpty(filePath)) {
                cachePath.put(1, filePath);
                IllegalQueryControl.getInstance().editIllegalOrder(getActivity(), imOrderID, "", filePath, illegalOrderCallBack);
                DebugLog.i(TAG, "chooseSuccess:" + cachePath.get(1));
            }
        }
        *//*    if (imOnReuploadListener != null) {
                DebugLog.i(TAG, "Reupload");
                imOnReuploadListener.reuploadListener(mFilePath);
                isReupload = false;
            }*//*
    }*/

    private boolean isOriginal = true;
    /**
     * 正本重新上传点击事件
     */
    private View.OnClickListener clickOriginalReuploadListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            DebugLog.i(TAG, "clickOriginalReuploadListener");
            changeDriveringImage(true);
            isOriginal = true;
        }
    };

    /**
     * 副本重新上传点击事件
     */
    private View.OnClickListener clickCopyReuploadListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            DebugLog.i(TAG, "clickCopyReuploadListener");
            changeDriveringImage(false);
            isOriginal = false;
        }
    };

    /**
     * 正本大图显示
     */
    private View.OnClickListener onClickOriginalAreaListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
//            ActivitySwitchAround.toViewLargePic(getActivity(), mOriginalAreaView, imIllegalOrderDetail.drivingLicenseFile1);
            ActivitySwitchAround.toViewLargePic(getActivity(), mOriginalAreaView, imDrivingLicenseFile1);
        }
    };

    /**
     * 副本大图显示
     */
    private View.OnClickListener onClickCopyAreaListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
//            ActivitySwitchAround.toViewLargePic(getActivity(), mCopyAreaView, imIllegalOrderDetail.drivingLicenseFile2);
            ActivitySwitchAround.toViewLargePic(getActivity(), mCopyAreaView, imDrivingLicenseFile2);
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (imIllegalOrderDetail != null) {
            imIllegalOrderDetail = null;
        }
        IllegalQueryControl.getInstance().killInstance();
        ActivitySwitchAround.killDirty();
    }
}
