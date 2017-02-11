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
import com.hxqc.aroundservice.control.ChangeLicenseControl;
import com.hxqc.aroundservice.model.Licence;
import com.hxqc.aroundservice.util.ActivitySwitchAround;
import com.hxqc.aroundservice.view.ChoosePictureLayout;
import com.hxqc.aroundservice.view.DialogImageView;
import com.hxqc.aroundservice.view.DialogTextView;
import com.hxqc.aroundservice.view.IllegalAutoInfoView;
import com.hxqc.aroundservice.view.ListItemView;
import com.hxqc.aroundservice.view.QueryResultBarV2;
import com.hxqc.mall.auto.config.AutoInfoContants;
import com.hxqc.mall.auto.inter.CallBackControl;
import com.hxqc.mall.core.api.RequestFailView;
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
 * Des: 驾驶证更换详情页面
 * FIXME
 * Todo
 */
public class LicenseOrderDetailFragment extends BasePhotoChooseFragmentV2 {

    private static final String TAG = AutoInfoContants.LOG_J;
    private View rootView;
    //    private QueryProcessingPhotoFragment mOriginalFragment;//驾驶证正本
//    private QueryProcessingPhotoFragment mCopyFragment;//驾驶证副本
    private TextView mNumView;//订单号
    private TextView mNameView;
    private TextView mPhoneView;
    private TextView mPaymentView;
    private TextView mTransactionView;
    private TextView mPayStateView;
    private TextView mRefundStateView;
    private String imOrderID;
    private IllegalAutoInfoView mHeaderView;
    private QueryResultBarV2 mBarView;
    private Licence imLicence;
    private MenuItem imCancel;
    private LinearLayout mItemPaymentView;
    private LinearLayout mItemTransactionView;
    private LinearLayout mItemRefundStateView;
    private RelativeLayout mOriginalAreaView;
    private HeightVSWidthImageView mOriginalBGView;
    private Button mOriginalReuploadView;
    private DialogImageView mOriginalSampleView;
    private RelativeLayout mIdentityFrontAreaView;
    private HeightVSWidthImageView mIdentityFrontBGView;
    private HeightVSWidthImageView mIdentityFrontBG2View;
    private Button mIdentityFrontReuploadView;
    private DialogImageView mIdentityFrontSampleView;
    private RelativeLayout mIdentityBackAreaView;
    private HeightVSWidthImageView mIdentityBackBGView;
    private HeightVSWidthImageView mIdentityBackBG2View;
    private Button mIdentityBackReuploadView;
    private DialogImageView mIdentityBackSampleView;
    private RelativeLayout mCopyAreaView;
    private HeightVSWidthImageView mCopyBGView;
    private Button mCopyReuploadView;
    private DialogImageView mCopySampleView;
    private LinearLayout mCustomerServiceView;
    private RelativeLayout mParentView;
    private RequestFailView mRequestFailView;
    private TextView mOrderCreateTimeView;
    private ListItemView mPayTimeParentView;
    private TextView mPayTimeView;
    private DialogTextView mOriginalSampleTitleView;
    private DialogTextView mCopySampleTitleView;
    private TextView mAddressView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_order_detail_license, container, false);
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

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initView();

        initData();

        initEvent();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    private void initEvent() {
        mBarView.setOnClickPayListener(onClickPayListener);

        mOriginalAreaView.setOnClickListener(onClickOriginalAreaListener);
        mCopyAreaView.setOnClickListener(onClickCopyAreaListener);
        mIdentityFrontAreaView.setOnClickListener(onClickIdentityFrontAreaListener);
        mIdentityBackAreaView.setOnClickListener(onClickIdentityBackAreaListener);
//        mOriginalFragment.setOnReuploadListener(reuploadOriginalListener);
//        mCopyFragment.setOnReuploadListener(reuploadCopyListener);

        mCustomerServiceView.setOnClickListener(onClickCallServiceListener);

        mOriginalSampleView.showSamplePhoto(getActivity(), R.drawable.ic_sample_license_origianl);
        mCopySampleView.showSamplePhoto(getActivity(), R.drawable.ic_sample_license_copy);
        mIdentityFrontSampleView.showSamplePhoto(getActivity(), R.drawable.ic_sample_license_origianl);
        mIdentityBackSampleView.showSamplePhoto(getActivity(), R.drawable.ic_sample_license_copy);
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
//        imOrderID = "";
        ChangeLicenseControl.getInstance().getLicenceDetail(getActivity(), imOrderID, licenceDetailCallBack);
    }

    private void initView() {
        mParentView = (RelativeLayout) rootView.findViewById(R.id.license_parent);
        mParentView.setVisibility(View.GONE);

        mNumView = (TextView) rootView.findViewById(R.id.license_number);
        mHeaderView = (IllegalAutoInfoView) rootView.findViewById(R.id.license_info);
        mHeaderView.setBackground(R.drawable.ic_license);
        mHeaderView.setVisibility(View.VISIBLE, View.GONE, View.VISIBLE, View.VISIBLE);
        mNameView = (TextView) rootView.findViewById(R.id.license_name);
        mPhoneView = (TextView) rootView.findViewById(R.id.license_phone);
        mAddressView = (TextView) rootView.findViewById(R.id.license_address);
        mPaymentView = (TextView) rootView.findViewById(R.id.license_payment);
        mTransactionView = (TextView) rootView.findViewById(R.id.license_transaction);
        mPayStateView = (TextView) rootView.findViewById(R.id.license_pay_state);
        mRefundStateView = (TextView) rootView.findViewById(R.id.license_refund_state);
        mBarView = (QueryResultBarV2) rootView.findViewById(R.id.license_bar);
        mBarView.setBarVisibility(View.GONE, View.GONE, View.VISIBLE);
        mBarView.setIllegalText(View.GONE, "", "支付金额:", 4, OtherUtil.amountFormat("0", true));

//        mOriginalFragment = (QueryProcessingPhotoFragment) getChildFragmentManager().findFragmentById(R.id.license_original);
//        mCopyFragment = (QueryProcessingPhotoFragment) getChildFragmentManager().findFragmentById(R.id.license_copy);
//        mOriginalFragment.setTitle("行驶证正本:");
//        mCopyFragment.setTitle("行驶证副本:");

        ChoosePictureLayout mOriginalView = (ChoosePictureLayout) rootView.findViewById(R.id.license_original);
        TextView mOriginalTitleView = mOriginalView.getTitleView();
        mOriginalAreaView = mOriginalView.getAreaView();
        mOriginalBGView = mOriginalView.getBGView();
        mOriginalReuploadView = mOriginalView.getReuploadView();
        mOriginalSampleView = mOriginalView.getSampleView();
        mOriginalTitleView.setText(getString(R.string.licence_original));

        ChoosePictureLayout mCopyView = (ChoosePictureLayout) rootView.findViewById(R.id.license_copy);
        TextView mCopyTitleView = mCopyView.getTitleView();
        mCopyAreaView = mCopyView.getAreaView();
        mCopyBGView = mCopyView.getBGView();
        mCopyReuploadView = mCopyView.getReuploadView();
        mCopySampleView = mCopyView.getSampleView();
        mCopyTitleView.setText(getString(R.string.licence_copy));

        ChoosePictureLayout mIdentityFrontView = (ChoosePictureLayout) rootView.findViewById(R.id.license_identity_front);
        TextView mIdentityFrontTitleView = mIdentityFrontView.getTitleView();
        mIdentityFrontAreaView = mIdentityFrontView.getAreaView();
        mIdentityFrontBGView = mIdentityFrontView.getBGView();
        mIdentityFrontBGView.setVisibility(View.GONE);
        mIdentityFrontBG2View = mIdentityFrontView.getBG2View();
        mIdentityFrontBG2View.setVisibility(View.VISIBLE);
        mIdentityFrontReuploadView = mIdentityFrontView.getReuploadView();
        mIdentityFrontSampleView = mIdentityFrontView.getSampleView();
        mIdentityFrontTitleView.setText(getString(R.string.identity_front));

        ChoosePictureLayout mIdentityBackView = (ChoosePictureLayout) rootView.findViewById(R.id.license_identity_back);
        TextView mIdentityBackTitleView = mIdentityBackView.getTitleView();
        mIdentityBackAreaView = mIdentityBackView.getAreaView();
        mIdentityBackBGView = mIdentityBackView.getBGView();
        mIdentityBackBGView.setVisibility(View.GONE);
        mIdentityBackBG2View = mIdentityBackView.getBG2View();
        mIdentityBackBG2View.setVisibility(View.VISIBLE);
        mIdentityBackReuploadView = mIdentityBackView.getReuploadView();
        mIdentityBackSampleView = mIdentityBackView.getSampleView();
        mIdentityBackTitleView.setText(getString(R.string.identity_back));

        mItemPaymentView = (LinearLayout) rootView.findViewById(R.id.license_item_payment);
        mItemTransactionView = (LinearLayout) rootView.findViewById(R.id.license_item_transaction);
        mItemRefundStateView = (LinearLayout) rootView.findViewById(R.id.license_item_refund_state);

        mCustomerServiceView = (LinearLayout) rootView.findViewById(R.id.license_customer_service);

        mRequestFailView = (RequestFailView) rootView.findViewById(R.id.license_no_data);

        mOrderCreateTimeView = (TextView) rootView.findViewById(R.id.license_order_create_time);
        mPayTimeParentView = (ListItemView) rootView.findViewById(R.id.license_pay_time_parent);
        mPayTimeView = (TextView) rootView.findViewById(R.id.license_pay_time);
    }

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
            ActivitySwitchAround.toCancelOrderDetailActivity(LicenseOrderDetailFragment.this, imOrderID, OrderDetailContants.FRAGMENT_VEHICLES_ORDER_DETAIL);
        }
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            if (requestCode == OrderDetailContants.REQUEST_CANCEL) {
                DebugLog.i(TAG, "成功");
                ChangeLicenseControl.getInstance().getLicenceDetail(getActivity(), imOrderID, licenceDetailCallBack);
            }
        }
    }

    private String imIDCardFile2;
    private String imIDCardFile1;
    private String imDrivingLicenseFile2;
    private String imDrivingLicenseFile1;

    private CallBackControl.CallBack<Licence> licenceDetailCallBack = new CallBackControl.CallBack<Licence>() {
        @Override
        public void onSuccess(Licence response) {
            mParentView.setVisibility(View.VISIBLE);
            imLicence = response;
            if (imLicence != null) {

                mAddressView.setText(response.province + response.city + response.district + response.address);
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
                    mIdentityFrontReuploadView.setVisibility(View.VISIBLE);
                    mIdentityFrontReuploadView.setOnClickListener(clickIdentityFrontReuploadListener);
                    mIdentityBackReuploadView.setVisibility(View.VISIBLE);
                    mIdentityBackReuploadView.setOnClickListener(clickIdentityBackReuploadListener);
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
                    mIdentityFrontReuploadView.setVisibility(View.GONE);
                    mIdentityBackReuploadView.setVisibility(View.GONE);
                }

                mNumView.setText("订单号:  " + response.orederID);
                mHeaderView.setData("", "驾驶证换证", OtherUtil.amountFormat(response.serviceCharge, true));
                mNameView.setText(response.username);
                mPhoneView.setText(response.phone);
                if (TextUtils.isEmpty(response.paymentID)) {
                    mItemPaymentView.setVisibility(View.GONE);
                } else {
                    mItemPaymentView.setVisibility(View.VISIBLE);
                    mPaymentView.setText(response.paymentID);
                }

                if (TextUtils.isEmpty(response.tradeID)) {
                    mItemTransactionView.setVisibility(View.GONE);
                } else {
                    mItemTransactionView.setVisibility(View.VISIBLE);
                    mTransactionView.setText(response.tradeID);
                }

                mPayStateView.setText(response.orderStatusText);
//                mPayStateView.setTextColor(BaseOrderStatus.getStatusColor(getActivity(), response.orderStatusText));


                if (TextUtils.isEmpty(response.refundStatusText)) {
                    mItemRefundStateView.setVisibility(View.GONE);
                } else {
                    mItemRefundStateView.setVisibility(View.VISIBLE);
                    mRefundStateView.setText(response.refundStatusText);
//                    BaseOrderStatus.getWCAOrderStatus().setRefundStatusText(getActivity(), mRefundStateView, response.refundStatus);

                }

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

                 /*if (response.refundStatus.equals("20")) {
                    mRefundStateView.setText("待退款");
                    mRefundStateView.setTextColor(getResources().getColor(R.color.order_status_red));
                } else if (response.refundStatus.equals("30")) {
                    mRefundStateView.setText("退款中");
                    mRefundStateView.setTextColor(getResources().getColor(R.color.order_status_orange));
                } else if (response.refundStatus.equals("40")) {
                    mRefundStateView.setText("退款完成");
                    mRefundStateView.setTextColor(getResources().getColor(R.color.order_status_green));
                }*/

                mBarView.setIllegalMoney(OtherUtil.amountFormat(response.serviceCharge, true));

//                mOriginalFragment.setBackground(getActivity(), licence.drivingLicenseFile1);
//                mCopyFragment.setBackground(getActivity(), licence.drivingLicenseFile2);

                ImageUtil.setImage(getActivity(), mOriginalBGView, response.drivingLicenseFile1, R.drawable.ic_pic_camera);
                ImageUtil.setImage(getActivity(), mCopyBGView, response.drivingLicenseFile2, R.drawable.ic_pic_camera);
                ImageUtil.setImage(getActivity(), mIdentityFrontBG2View, response.IDCardFile1, R.drawable.ic_pic_camera);
                ImageUtil.setImage(getActivity(), mIdentityBackBG2View, response.IDCardFile2, R.drawable.ic_pic_camera);
                imDrivingLicenseFile1 = response.drivingLicenseFile1;
                imDrivingLicenseFile2 = response.drivingLicenseFile2;
                imIDCardFile1 = response.IDCardFile1;
                imIDCardFile2 = response.IDCardFile2;

            }
        }

        @Override
        public void onFailed(boolean offLine) {
            mRequestFailView.setRequestViewType(RequestFailView.RequestViewType.fail);
            mRequestFailView.setFailButtonClick("重新加载", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ChangeLicenseControl.getInstance().getLicenceDetail(getActivity(), imOrderID, licenceDetailCallBack);
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
            if (imLicence != null) {

                PaymentActivitySwitch.toAroundPaymentActivity(getActivity(), imLicence.serviceCharge, imOrderID, OrderDetailContants.FLAG_ACTIVITY_LICENSE, "-1");
            }
        }
    };

    /**
     * 上传后的回调操作
     */
    private CallBackControl.CallBack<String> licenceOrderCallBack = new CallBackControl.CallBack<String>() {
        @Override
        public void onSuccess(String response) {
            ToastHelper.showGreenToast(getActivity(), "上传成功");
            switch (photoType) {
                case LICENSE_ORIGINAL:
                    if (!TextUtils.isEmpty(cachePath.get(LICENSE_ORIGINAL))) {
                        DebugLog.i(TAG, FILE_PATH_HEAD + cachePath.get(LICENSE_ORIGINAL));
                        ImageUtil.setImage(getActivity(), mOriginalBGView, FILE_PATH_HEAD + cachePath.get(LICENSE_ORIGINAL), R.drawable.ic_pic_camera);
                        imDrivingLicenseFile1 = FILE_PATH_HEAD + cachePath.get(LICENSE_ORIGINAL);
                    }
                    break;
                case LICENSE_COPY:
                    if (!TextUtils.isEmpty(cachePath.get(LICENSE_COPY))) {
                        DebugLog.i(TAG, FILE_PATH_HEAD + cachePath.get(LICENSE_COPY));
                        ImageUtil.setImage(getActivity(), mCopyBGView, FILE_PATH_HEAD + cachePath.get(LICENSE_COPY), R.drawable.ic_pic_camera);
                        imDrivingLicenseFile2 = FILE_PATH_HEAD + cachePath.get(LICENSE_COPY);
                    }
                    break;
                case IDENTITY_FRONT:
                    if (!TextUtils.isEmpty(cachePath.get(IDENTITY_FRONT))) {
                        DebugLog.i(TAG, FILE_PATH_HEAD + cachePath.get(IDENTITY_FRONT));
                        ImageUtil.setImage(getActivity(), mIdentityFrontBG2View, FILE_PATH_HEAD + cachePath.get(IDENTITY_FRONT), R.drawable.ic_pic_camera);
                        imIDCardFile1 = FILE_PATH_HEAD + cachePath.get(IDENTITY_FRONT);
                    }
                    break;
                case IDENTITY_BACK:
                    if (!TextUtils.isEmpty(cachePath.get(IDENTITY_BACK))) {
                        DebugLog.i(TAG, FILE_PATH_HEAD + cachePath.get(IDENTITY_BACK));
                        ImageUtil.setImage(getActivity(), mIdentityBackBG2View, FILE_PATH_HEAD + cachePath.get(IDENTITY_BACK), R.drawable.ic_pic_camera);
                        imIDCardFile2 = FILE_PATH_HEAD + cachePath.get(IDENTITY_BACK);
                    }
                    break;
                default:
                    break;
            }
        }

        @Override
        public void onFailed(boolean offLine) {
            if (imLicence != null) {
                ImageUtil.setImage(getActivity(), mOriginalBGView, imDrivingLicenseFile1, R.drawable.ic_pic_camera);
                ImageUtil.setImage(getActivity(), mCopyBGView, imDrivingLicenseFile2, R.drawable.ic_pic_camera);
                ImageUtil.setImage(getActivity(), mIdentityFrontBG2View, imIDCardFile1, R.drawable.ic_pic_camera);
                ImageUtil.setImage(getActivity(), mIdentityBackBG2View, imIDCardFile2, R.drawable.ic_pic_camera);
            }
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
        switch (photoType) {
            case LICENSE_ORIGINAL:
//                ImageUtil.setImage2(getActivity(), mOriginalBGView, mFilePath, R.drawable.ic_uploadpictures);
                if (!TextUtils.isEmpty(photoPath)) {
                    cachePath.put(LICENSE_ORIGINAL, photoPath);
                    ChangeLicenseControl.getInstance().editLicenceOrder(getActivity(), imOrderID, photoPath, "", "", "", licenceOrderCallBack);
                    DebugLog.i(TAG, "chooseSuccess:" + cachePath.get(LICENSE_ORIGINAL));
                }
                break;
            case LICENSE_COPY:
//                ImageUtil.setImage2(getActivity(), mCopyBGView, mFilePath, R.drawable.ic_uploadpictures);
                if (!TextUtils.isEmpty(photoPath)) {
                    cachePath.put(LICENSE_COPY, photoPath);
                    ChangeLicenseControl.getInstance().editLicenceOrder(getActivity(), imOrderID, "", photoPath, "", "", licenceOrderCallBack);
                    DebugLog.i(TAG, "chooseSuccess:" + cachePath.get(LICENSE_COPY));
                }
                break;
            case IDENTITY_FRONT:
//                ImageUtil.setImage2(getActivity(), mIdentityFrontBG2View, mFilePath, R.drawable.ic_uploadpictures);
                if (!TextUtils.isEmpty(photoPath)) {
                    cachePath.put(IDENTITY_FRONT, photoPath);
                    ChangeLicenseControl.getInstance().editLicenceOrder(getActivity(), imOrderID, "", "", photoPath, "", licenceOrderCallBack);
                    DebugLog.i(TAG, "chooseSuccess:" + cachePath.get(IDENTITY_FRONT));
                }
                break;
            case IDENTITY_BACK:
//                ImageUtil.setImage2(getActivity(), mIdentityBackBG2View, mFilePath, R.drawable.ic_uploadpictures);
                if (!TextUtils.isEmpty(photoPath)) {
                    cachePath.put(IDENTITY_BACK, photoPath);
                    ChangeLicenseControl.getInstance().editLicenceOrder(getActivity(), imOrderID, "", "", "", photoPath, licenceOrderCallBack);
                    DebugLog.i(TAG, "chooseSuccess:" + cachePath.get(IDENTITY_BACK));
                }
                break;
            default:
                break;
        }
    }

    /*@Override
    public void chooseSuccess(String filePath) {
        String mFilePath = FILE_PATH_HEAD + filePath;
        DebugLog.i(TAG, "mFilePath:" + mFilePath);
        if (cachePath == null) {
            cachePath = new HashMap<>();
        }
        switch (photoType) {
            case LICENSE_ORIGINAL:
//                ImageUtil.setImage2(getActivity(), mOriginalBGView, mFilePath, R.drawable.ic_uploadpictures);
                if (!TextUtils.isEmpty(filePath)) {
                    cachePath.put(LICENSE_ORIGINAL, filePath);
                    ChangeLicenseControl.getInstance().editLicenceOrder(getActivity(), imOrderID, filePath, "", "", "", licenceOrderCallBack);
                    DebugLog.i(TAG, "chooseSuccess:" + cachePath.get(LICENSE_ORIGINAL));
                }
                break;
            case LICENSE_COPY:
//                ImageUtil.setImage2(getActivity(), mCopyBGView, mFilePath, R.drawable.ic_uploadpictures);
                if (!TextUtils.isEmpty(filePath)) {
                    cachePath.put(LICENSE_COPY, filePath);
                    ChangeLicenseControl.getInstance().editLicenceOrder(getActivity(), imOrderID, "", filePath, "", "", licenceOrderCallBack);
                    DebugLog.i(TAG, "chooseSuccess:" + cachePath.get(LICENSE_COPY));
                }
                break;
            case IDENTITY_FRONT:
//                ImageUtil.setImage2(getActivity(), mIdentityFrontBG2View, mFilePath, R.drawable.ic_uploadpictures);
                if (!TextUtils.isEmpty(filePath)) {
                    cachePath.put(IDENTITY_FRONT, filePath);
                    ChangeLicenseControl.getInstance().editLicenceOrder(getActivity(), imOrderID, "", "", filePath, "", licenceOrderCallBack);
                    DebugLog.i(TAG, "chooseSuccess:" + cachePath.get(IDENTITY_FRONT));
                }
                break;
            case IDENTITY_BACK:
//                ImageUtil.setImage2(getActivity(), mIdentityBackBG2View, mFilePath, R.drawable.ic_uploadpictures);
                if (!TextUtils.isEmpty(filePath)) {
                    cachePath.put(IDENTITY_BACK, filePath);
                    ChangeLicenseControl.getInstance().editLicenceOrder(getActivity(), imOrderID, "", "", "", filePath, licenceOrderCallBack);
                    DebugLog.i(TAG, "chooseSuccess:" + cachePath.get(IDENTITY_BACK));
                }
                break;
            default:
                break;
        }
      *//*  if (isOriginal) {
            ImageUtil.setImage2(getActivity(), mOriginalBGView, mFilePath, R.drawable.ic_uploadpictures);
            if (!TextUtils.isEmpty(filePath)) {
                ChangeLicenseControl.getInstance().editLicenceOrder(getActivity(), imOrderID, filePath, "", licenceOrderCallBack);
            }
        } else {
            ImageUtil.setImage2(getActivity(), mCopyBGView, mFilePath, R.drawable.ic_uploadpictures);
            if (!TextUtils.isEmpty(filePath)) {
                ChangeLicenseControl.getInstance().editLicenceOrder(getActivity(), imOrderID, "", filePath, licenceOrderCallBack);
            }
        }*//*
        *//*    if (imOnReuploadListener != null) {
                DebugLog.i(TAG, "Reupload");
                imOnReuploadListener.reuploadListener(mFilePath);
                isReupload = false;
            }*//*
    }*/

    /*@Override
    public boolean toCropPhoto() {
        return false;
    }

    @Override
    public String getCameraPath() {
        return SampleApplicationContext.application.getExternalCacheDir().toString()
                + "/drivinglicense" + System.currentTimeMillis() + ".png";
    }

    @Override
    protected String getCropCacheFilePath() {
        Context context = getActivity();
        cropCachePath = context.getExternalCacheDir() + File.separator + "crop_"
                + System.currentTimeMillis() + ".png";
        return cropCachePath;
    }

    private void changeUserImage() {
        try {
            new ListDialog(getActivity(), getActivity().getResources().getString(R.string.me_upload_photo),
                    new String[]{getResources().getString(R.string.me_take_picture_upload), getResources().getString
                            (R.string.me_local_upload)},
                    new int[]{R.drawable.ic_uploadpictures, R.drawable.ic_detail_image1}) {
                @Override
                protected void doNext(int position) {
                    switch (position) {
                        case 0:
                            // 相机
                            getPicFromCamera();
                            break;
                        case 1:
                            // 图库
                            getPicFromContent();
                            break;
                        default:
                            break;
                    }
                }
            }.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    /*private Transformation transformationOriginal = null;
    private Transformation getTransformation(final View v) {
        */
    /**
     * 图片大小等比例缩放
     *//*
        transformationOriginal = new Transformation() {

            @Override
            public Bitmap transform(Bitmap source) {
                int targetWidth = v.getWidth();
                double aspectRatio = (double) source.getHeight() / (double) source.getWidth();
                int targetHeight = (int) (targetWidth * aspectRatio);
                Bitmap result = Bitmap.createScaledBitmap(source, targetWidth, targetHeight, false);
                if (result != source) {
                    // Same bitmap is returned if sizes are the same
                    source.recycle();
                }
                return result;
            }

            @Override
            public String key() {
                return "transformation" + " desiredWidth";
            }
        };

        return transformationOriginal;
    }*/


    private int photoType = 0;
    private final int LICENSE_ORIGINAL = 1;
    private final int LICENSE_COPY = 2;
    private final int IDENTITY_FRONT = 3;
    private final int IDENTITY_BACK = 4;
    /**
     * 正本重新上传点击事件
     */
    private View.OnClickListener clickOriginalReuploadListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            DebugLog.i(TAG, "clickOriginalReuploadListener");
            changeDriverImage(true);
            photoType = LICENSE_ORIGINAL;
        }
    };

    /**
     * 副本重新上传点击事件
     */
    private View.OnClickListener clickCopyReuploadListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            DebugLog.i(TAG, "clickCopyReuploadListener");
            changeDriverImage(false);
            photoType = LICENSE_COPY;
        }
    };

    /**
     * 正面重新上传点击事件
     */
    private View.OnClickListener clickIdentityFrontReuploadListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            DebugLog.i(TAG, "clickIdentityFrontReuploadListener");
            changeIDImage(true);
            photoType = IDENTITY_FRONT;
        }
    };

    /**
     * 正面重新上传点击事件
     */
    private View.OnClickListener clickIdentityBackReuploadListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            DebugLog.i(TAG, "clickIdentityFrontReuploadListener");
            changeIDImage(false);
            photoType = IDENTITY_BACK;
        }
    };

    /**
     * 正本大图显示
     */
    private View.OnClickListener onClickOriginalAreaListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
//            ActivitySwitchAround.toViewLargePic(getActivity(), mOriginalAreaView, imLicence.drivingLicenseFile1);
            ActivitySwitchAround.toViewLargePic(getActivity(), mOriginalAreaView, imDrivingLicenseFile1);
        }
    };

    /**
     * 副本大图显示
     */
    private View.OnClickListener onClickCopyAreaListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
//            ActivitySwitchAround.toViewLargePic(getActivity(), mCopyAreaView, imLicence.drivingLicenseFile2);
            ActivitySwitchAround.toViewLargePic(getActivity(), mCopyAreaView, imDrivingLicenseFile2);
        }
    };

    /**
     * 正面大图显示
     */
    private View.OnClickListener onClickIdentityFrontAreaListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
//            ActivitySwitchAround.toViewLargePic(getActivity(), mIdentityFrontAreaView, imLicence.IDCardFile1);
            ActivitySwitchAround.toViewLargePic(getActivity(), mIdentityFrontAreaView, imIDCardFile1);
        }
    };

    /**
     * 正面大图显示
     */
    private View.OnClickListener onClickIdentityBackAreaListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
//            ActivitySwitchAround.toViewLargePic(getActivity(), mIdentityBackAreaView, imLicence.IDCardFile2);
            ActivitySwitchAround.toViewLargePic(getActivity(), mIdentityBackAreaView, imIDCardFile2);
        }
    };

    private View.OnClickListener onClickCallServiceListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
//            new CallBar.CallPhoneDialog(getActivity(), getActivity().getString(R.string.tv_service_phone)).show();
            ChatManager.getInstance().startHistoryGoods("消息", "null");
        }
    };


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (imLicence != null) {
            imLicence = null;
        }
        /*if (transformationOriginal != null) {
            transformationOriginal = null;
        }*/
        ChangeLicenseControl.getInstance().killInstance();
        ActivitySwitchAround.killDirty();
    }

}
