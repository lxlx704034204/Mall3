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
import com.hxqc.aroundservice.control.AnnualVehicleControl;
import com.hxqc.aroundservice.model.AnnualInspection;
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
 * Des: 车辆年检详情页面
 * FIXME
 * Todo
 */
public class VehiclesOrderDetailFragment extends BasePhotoChooseFragmentV2 {

    private static final String TAG = AutoInfoContants.LOG_J;
    private View rootView;
    //    private QueryProcessingPhotoFragment mOriginalFragment;//行驶证正本控件
//    private QueryProcessingPhotoFragment mCopyFragment;//行驶证副本控件
    private IllegalAutoInfoView mHeaderView;
    private TextView mExemptionView;
    private TextView mNameView;
    private TextView mPhoneView;
    private TextView mDateView;
    private TextView mPaymentView;
    private TextView mDetailStateView;
    private QueryResultBarV2 mBarView;
    private String imOrderID;
    private TextView mTradeIDView;
    private AnnualInspection imAnnualInspection;
    private MenuItem imCancel;
    private Button mCopyReuploadView;
    private Button mOriginalReuploadView;
    private RelativeLayout mOriginalAreaView;
    private HeightVSWidthImageView mOriginalBGView;
    private RelativeLayout mCopyAreaView;
    private HeightVSWidthImageView mCopyBGView;
    private TextView mRefundStateView;
    private LinearLayout mItemRefundStateView;
    private LinearLayout mCustomerServiceView;
    private LinearLayout mItemPaymentView;
    private RelativeLayout mParentView;
    private RequestFailView mRequestFailView;
    private TextView mOrderCreateTimeView;
    private ListItemView mPayTimeParentView;
    private TextView mPayTimeView;
    private DialogImageView mOriginalSampleView;
    private DialogImageView mCopySampleView;
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
            rootView = inflater.inflate(R.layout.fragment_order_detail_vehicles, container, false);
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

    /**
     * 初始化事件
     */
    private void initEvent() {
        mBarView.setOnClickPayListener(onClickPayListener);
        mOriginalAreaView.setOnClickListener(onClickOriginalAreaListener);
        mCopyAreaView.setOnClickListener(onClickCopyAreaListener);

        mCustomerServiceView.setOnClickListener(onClickCallServiceListener);
//        mOriginalFragment.setOnReuploadListener(reuploadOriginalListener);
//        mCopyFragment.setOnReuploadListener(reuploadCopyListener);

//        mOriginalSampleView.showSamplePhoto(getActivity(),R.drawable.ic_sample_license_origianl);
//        mCopySampleView.showSamplePhoto(getActivity(),R.drawable.ic_sample_license_copy);
//        mOriginalSampleTitleView.showSamplePhoto(getActivity(),R.drawable.ic_sample_license_origianl);
//        mCopySampleTitleView.showSamplePhoto(getActivity(),R.drawable.ic_sample_license_copy);

    }

    /**
     * 初始化数据
     */
    private void initData() {

        if (getActivity().getIntent() != null) {
//            imOrderID = getActivity().getIntent().getStringExtra("orderID");
            Bundle bundleExtra = getActivity().getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA);
            imOrderID = bundleExtra.getString("orderID");
            DebugLog.i(TAG, "orderID:" + imOrderID);
        }
//        imOrderID = "";
        AnnualVehicleControl.getInstance().getAnnualnspectionDetail(getActivity(), imOrderID, annualnspectionDetailCallBack);
    }

    /**
     * 初始化控件
     */
    private void initView() {
        mParentView = (RelativeLayout) rootView.findViewById(R.id.vehicles_parent);
        mParentView.setVisibility(View.GONE);
        mTradeIDView = (TextView) rootView.findViewById(R.id.vehicles_trade_id);

        mHeaderView = (IllegalAutoInfoView) rootView.findViewById(R.id.vehicles_header);
        mHeaderView.setBackground(R.drawable.ic_inspection);

        mExemptionView = (TextView) rootView.findViewById(R.id.vehicles_exemption);
        mNameView = (TextView) rootView.findViewById(R.id.vehicles_name);
        mPhoneView = (TextView) rootView.findViewById(R.id.vehicles_phone);
        mAddressView = (TextView) rootView.findViewById(R.id.vehicles_address);
        mDateView = (TextView) rootView.findViewById(R.id.vehicles_date);
        mPaymentView = (TextView) rootView.findViewById(R.id.vehicles_payment);
        mDetailStateView = (TextView) rootView.findViewById(R.id.vehicles_order_detail);
        mBarView = (QueryResultBarV2) rootView.findViewById(R.id.vehicles_bar);

        ChoosePictureLayout mOriginalView = (ChoosePictureLayout) rootView.findViewById(R.id.vehicles_original);
        TextView mOriginalTitleView = mOriginalView.getTitleView();
        mOriginalAreaView = mOriginalView.getAreaView();
        mOriginalBGView = mOriginalView.getBGView();
        mOriginalReuploadView = mOriginalView.getReuploadView();
        mOriginalSampleView = mOriginalView.getSampleView();
        mOriginalSampleTitleView = mOriginalView.getSampleTitleView();
        mOriginalTitleView.setText(getString(R.string.license_original));

        ChoosePictureLayout mCopyView = (ChoosePictureLayout) rootView.findViewById(R.id.vehicles_copy);
        TextView mCopyTitleView = mCopyView.getTitleView();
        mCopyAreaView = mCopyView.getAreaView();
        mCopyBGView = mCopyView.getBGView();
        mCopyReuploadView = mCopyView.getReuploadView();
        mCopySampleView = mCopyView.getSampleView();
        mCopySampleTitleView = mCopyView.getSampleTitleView();
        mCopyTitleView.setText(getString(R.string.license_copy));

//        mOriginalFragment = (QueryProcessingPhotoFragment) getChildFragmentManager().findFragmentById(R.id.vehicles_original);
//        mCopyFragment = (QueryProcessingPhotoFragment) getChildFragmentManager().findFragmentById(R.id.vehicles_copy);
//        mOriginalFragment.setTitle("行驶证正本:");
//        mCopyFragment.setTitle("行驶证副本:");

        mRefundStateView = (TextView) rootView.findViewById(R.id.vehicles_refund_state);
        mItemRefundStateView = (LinearLayout) rootView.findViewById(R.id.vehicles_item_refund_state);

        mCustomerServiceView = (LinearLayout) rootView.findViewById(R.id.vehicles_customer_service);

        mRequestFailView = (RequestFailView) rootView.findViewById(R.id.vehicles_no_data);

        mItemPaymentView = (LinearLayout) rootView.findViewById(R.id.vehicles_item_payment);
        mOrderCreateTimeView = (TextView) rootView.findViewById(R.id.vehicles_order_create_time);
        mPayTimeParentView = (ListItemView) rootView.findViewById(R.id.vehicles_pay_time_parent);
        mPayTimeView = (TextView) rootView.findViewById(R.id.vehicles_pay_time);
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
            ActivitySwitchAround.toCancelOrderDetailActivity(VehiclesOrderDetailFragment.this, imOrderID, OrderDetailContants.FRAGMENT_VEHICLES_ORDER_DETAIL);
        }
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        DebugLog.i(TAG, "onActivityResult");
        if (data != null) {
            DebugLog.i(TAG, "data");
            if (resultCode == OrderDetailContants.CANDEL_SRCCESS) {
                DebugLog.i(TAG, "成功");
                AnnualVehicleControl.getInstance().getAnnualnspectionDetail(getActivity(), imOrderID, annualnspectionDetailCallBack);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private String imDrivingLicenseFile2;
    private String imDrivingLicenseFile1;
    /**
     *
     */
    private CallBackControl.CallBack<AnnualInspection> annualnspectionDetailCallBack = new CallBackControl.CallBack<AnnualInspection>() {
        @Override
        public void onSuccess(AnnualInspection response) {
            mParentView.setVisibility(View.VISIBLE);
            imAnnualInspection = response;
            if (imAnnualInspection != null) {

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

                if (!TextUtils.isEmpty(response.exemption)) {
                    if (response.exemption.equals("1")) {
                        mExemptionView.setText("免检期间");
                    } else if (response.exemption.equals("0")) {
                        mExemptionView.setText("非免检期间");
                    }
                }

                mTradeIDView.setText("订单号:  " + imOrderID);
//                mTradeIDView.setText("订单号:  " + response.tradeID);
                mHeaderView.setData(response.plateNumber, "车辆年检", OtherUtil.amountFormat(response.amount, true));
                mNameView.setText(response.username);
                mPhoneView.setText(response.phone);

                if (TextUtils.isEmpty(response.paymentID)) {
                    mItemPaymentView.setVisibility(View.GONE);
                } else {
                    mItemPaymentView.setVisibility(View.VISIBLE);
                    mPaymentView.setText(response.paymentID);
                }
                mDateView.setText(response.registrDate);
                mDetailStateView.setText(response.orderStatusText);

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

//                mDetailStateView.setTextColor(BaseOrderStatus.getStatusColor(getActivity(), response.orderStatusText));
                mBarView.setIllegalMoney(OtherUtil.amountFormat(response.amount, true));


                ImageUtil.setImage(getActivity(), mOriginalBGView, imAnnualInspection.drivingLicenseFile1, R.drawable.ic_pic_camera);


                ImageUtil.setImage(getActivity(), mCopyBGView, imAnnualInspection.drivingLicenseFile2, R.drawable.ic_pic_camera);
                imDrivingLicenseFile1 = imAnnualInspection.drivingLicenseFile1;
                imDrivingLicenseFile2 = imAnnualInspection.drivingLicenseFile2;
                if (TextUtils.isEmpty(response.refundStatusText)) {
                    mItemRefundStateView.setVisibility(View.GONE);
                } else {
                    mItemRefundStateView.setVisibility(View.VISIBLE);
                    mRefundStateView.setText(response.refundStatusText);
                  /*  if (response.refundStatus.equals("20")) {
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
                    AnnualVehicleControl.getInstance().getAnnualnspectionDetail(getActivity(), imOrderID, annualnspectionDetailCallBack);
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
            if (imAnnualInspection != null) {

                PaymentActivitySwitch.toAroundPaymentActivity(getActivity(), imAnnualInspection.amount, imOrderID, OrderDetailContants.FLAG_ACTIVITY_VEHICLES, imAnnualInspection.exemption);
            }
        }
    };

    /**
     * 上传后的回调操作
     */
    private CallBackControl.CallBack<String> annualnspectionOrderCallBack = new CallBackControl.CallBack<String>() {
        @Override
        public void onSuccess(String response) {
            ToastHelper.showGreenToast(getActivity(), "上传成功");
            if (isOriginal) {
                if (!TextUtils.isEmpty(cachePath.get(0))) {
                    DebugLog.i(TAG, FILE_PATH_HEAD + cachePath.get(0));
                    ImageUtil.setImage(getActivity(), mOriginalBGView, FILE_PATH_HEAD + cachePath.get(0), R.drawable.ic_pic_camera);
                    imDrivingLicenseFile1 = FILE_PATH_HEAD + cachePath.get(0);
                }
            } else {
                if (!TextUtils.isEmpty(cachePath.get(1))) {
                    DebugLog.i(TAG, FILE_PATH_HEAD + cachePath.get(1));
                    ImageUtil.setImage(getActivity(), mCopyBGView, FILE_PATH_HEAD + cachePath.get(1), R.drawable.ic_pic_camera);
                    imDrivingLicenseFile2 = FILE_PATH_HEAD + cachePath.get(1);
                }
            }
        }

        @Override
        public void onFailed(boolean offLine) {
            if (imAnnualInspection != null) {
                ImageUtil.setImage(getActivity(), mOriginalBGView, imDrivingLicenseFile1, R.drawable.ic_pic_camera);
                ImageUtil.setImage(getActivity(), mCopyBGView, imDrivingLicenseFile2, R.drawable.ic_pic_camera);
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
        if (isOriginal) {
            if (!TextUtils.isEmpty(photoPath)) {
                cachePath.put(0, photoPath);
                AnnualVehicleControl.getInstance().editAnnualnspectionDetail(getActivity(), imOrderID, photoPath, "", annualnspectionOrderCallBack);
                DebugLog.i(TAG, "chooseSuccess:" + cachePath.get(0));
            }

        } else {
            if (!TextUtils.isEmpty(photoPath)) {
                cachePath.put(1, photoPath);
                AnnualVehicleControl.getInstance().editAnnualnspectionDetail(getActivity(), imOrderID, "", photoPath, annualnspectionOrderCallBack);
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
                AnnualVehicleControl.getInstance().editAnnualnspectionDetail(getActivity(), imOrderID, filePath, "", annualnspectionOrderCallBack);
                DebugLog.i(TAG, "chooseSuccess:" + cachePath.get(0));
            }

        } else {
            if (!TextUtils.isEmpty(filePath)) {
                cachePath.put(1, filePath);
                AnnualVehicleControl.getInstance().editAnnualnspectionDetail(getActivity(), imOrderID, "", filePath, annualnspectionOrderCallBack);
                DebugLog.i(TAG, "chooseSuccess:" + cachePath.get(1));
            }
        }
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
            ActivitySwitchAround.toViewLargePic(getActivity(), mOriginalAreaView, imDrivingLicenseFile1);
        }
    };

    /**
     * 副本大图显示
     */
    private View.OnClickListener onClickCopyAreaListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ActivitySwitchAround.toViewLargePic(getActivity(), mCopyAreaView, imDrivingLicenseFile2);
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
        if (imAnnualInspection != null) {
            imAnnualInspection = null;
        }
        /*if (transformationOriginal != null) {
            transformationOriginal = null;
        }*/
        AnnualVehicleControl.getInstance().killInstance();
        ActivitySwitchAround.killDirty();
    }
}
