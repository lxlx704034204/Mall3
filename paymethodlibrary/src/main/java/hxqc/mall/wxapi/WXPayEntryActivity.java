package hxqc.mall.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.hxqc.mall.paymethodlibrary.manager.PayCallBackManager;
import com.hxqc.mall.paymethodlibrary.model.EventGetSuccessModel;
import com.hxqc.mall.paymethodlibrary.util.PayKeyConstants;
import com.hxqc.mall.paymethodlibrary.util.PayMethodConstant;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import org.greenrobot.eventbus.EventBus;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    private static final String TAG = "pay_test";

    private IWXAPI api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate, WXPayEntryActivity = ");
//        setContentView(R.layout.activity_wxpay_entry);
        try {
            api = WXAPIFactory.createWXAPI(this, PayKeyConstants.getWxAppId());
            api.handleIntent(getIntent(), this);
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        Log.d(TAG, "onNewIntent, WXPayEntryActivity = ");
        if (api != null)
            api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq baseReq) {
        Log.d(TAG, "onPayFinish, onReq = " + baseReq.toString());
    }

    @Override
    public void onResp(BaseResp resp) {
        Log.d(TAG, "onPayFinish, onResp,errCode = " + resp.errCode);

        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            EventBus.getDefault().post(new EventGetSuccessModel(resp.errCode, "", PayMethodConstant.WECHAT_PAY));

            PayCallBackManager
                    .getInstance()
                    .onPayCallBack(new EventGetSuccessModel(resp.errCode, "", PayMethodConstant.WECHAT_PAY));
        }

        finish();
    }
}
