//package com.hxqc.pay.activity;
//
//import android.content.Context;
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.v4.app.Fragment;
//import android.text.TextUtils;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.inputmethod.InputMethodManager;
//
//import com.google.gson.reflect.TypeToken;
//import com.hxqc.mall.activity.BackActivity;
//import com.hxqc.mall.core.api.LoadingAnimResponseHandler;
//import com.hxqc.mall.core.api.RequestFailView;
//import com.hxqc.mall.core.model.loan.InstallmentInfo;
//import com.hxqc.pay.api.PayApiClient;
//import com.hxqc.pay.event.EventUpLoadFile;
//import com.hxqc.pay.fragment.UploadInfoFragment;
//import com.hxqc.pay.util.ConstantValue;
//import com.hxqc.util.DebugLog;
//import com.hxqc.util.JSONUtils;
//
//import cz.msebera.android.httpclient.Header;
//import org.greenrobot.eventbus.EventBus;
//
//import hxqc.mall.R;
//
//public class UploadInfoActivity extends BackActivity {
//    private static final String TAG = "BackActivity";
//    InputMethodManager inputMethodManager;
//    String flagFromStep4 = "";
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_upload_info);
//        final RequestFailView request_view = (RequestFailView) findViewById(R.id.request_view);
//        final String order_id = getIntent().getStringExtra(ConstantValue.PAY_MAIN_ORDER_ID);
//        flagFromStep4 = getIntent().getStringExtra(ConstantValue.SWITCH_FROM_STEP_4);
//        final String orderStatus = getIntent().getStringExtra(ConstantValue.ORDER_STATUS);
//
//        DebugLog.d(TAG, "order_id:" + order_id + "----orderStatus:" + orderStatus + "----flagFromStep4:" + flagFromStep4);
//
//        if (TextUtils.isEmpty(order_id)) {
//            finish();
//
//        } else {
//            PayApiClient mPayApiClient = new PayApiClient();
//            setBundle(request_view, order_id, orderStatus, mPayApiClient);
//        }
//    }
//
//    /**
//     * 联网设置Bundle传递给Fragment,联网失败加载失败页面
//     */
//    private void setBundle(final RequestFailView request_view, final String order_id, final String orderStatus, final PayApiClient mPayApiClient) {
//        mPayApiClient.getInstallmentInfo(order_id, new LoadingAnimResponseHandler(this) {
//
//            @Override
//            public void onSuccess(String response) {
//                request_view.setVisibility(View.GONE);
//                InstallmentInfo installmentInfo = JSONUtils.fromJson(response, new TypeToken<InstallmentInfo>() {
//                });
//                Fragment uploadFragment = new UploadInfoFragment();
//                Bundle bundle = new Bundle();
//                bundle.putString(ConstantValue.PAY_MAIN_ORDER_ID, order_id);
//                bundle.putParcelable("InstallmentInfo", installmentInfo);
//                if (!TextUtils.isEmpty(orderStatus)) {
//                    bundle.putString(ConstantValue.ORDER_STATUS, orderStatus);
//                }
//
////        if (TextUtils.isEmpty(getIntent().getStringExtra(ConstantValue.SWITCH_FROM_STEP_4)))
////            bundle.putString(ConstantValue.SWITCH_FROM_STEP_4, getIntent().getStringExtra(ConstantValue.SWITCH_FROM_STEP_4));
//                uploadFragment.setArguments(bundle);
//                getSupportFragmentManager().beginTransaction().add(R.id.container, uploadFragment).commit();
//                inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//
//            }
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
//                super.onFailure(statusCode, headers, responseString, throwable);
//                request_view.setVisibility(View.VISIBLE);
//                request_view.setRequestViewType(RequestFailView.RequestViewType.fail);
//
//            }
//        });
//    }
//
//
//    //关闭软件盘 touch事件
//    @Override
//    public boolean dispatchTouchEvent(@Nullable MotionEvent ev) {
//
//
//        if (ev != null && ev.getAction() == MotionEvent.ACTION_DOWN) {
//            if (getCurrentFocus() != null && getCurrentFocus().getWindowToken() != null&&inputMethodManager!=null) {
//                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
//
//
//            }
//
////            if (isFastDoubleClick()) {
////                return true;
////            }
//        }
//
//        return super.dispatchTouchEvent(ev);
//    }
//
//    @Override
//    public void onBackPressed() {
//        if (!TextUtils.isEmpty(flagFromStep4) && ConstantValue.v_from_step_4.equals(flagFromStep4)) {
//            EventUpLoadFile eventUpLoadFile = new EventUpLoadFile();
//            eventUpLoadFile.operateStatus = EventUpLoadFile.do_nothing;
//            EventBus.getDefault().post(eventUpLoadFile);
//        }
//        super.onBackPressed();
//    }
//
//    @Override
//    public boolean onSupportNavigateUp() {
//        if (!TextUtils.isEmpty(flagFromStep4) && ConstantValue.v_from_step_4.equals(flagFromStep4)) {
//            EventUpLoadFile eventUpLoadFile = new EventUpLoadFile();
//            eventUpLoadFile.operateStatus = EventUpLoadFile.do_nothing;
//            EventBus.getDefault().post(eventUpLoadFile);
//        }
//        return super.onSupportNavigateUp();
//
//    }
//}
