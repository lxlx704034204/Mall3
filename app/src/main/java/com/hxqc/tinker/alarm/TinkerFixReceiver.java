package com.hxqc.tinker.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.hxqc.mall.core.api.LoadingAnimResponseHandler;
import com.hxqc.tinker.api.TinkerApiClient;
import com.hxqc.tinker.util.TinkerAutoFixStartUtil;

/**
 * Author:  wh
 * Date:  2016/12/29
 * FIXME
 * Todo
 */

public class TinkerFixReceiver extends BroadcastReceiver {

    /**
     * 定时开始   请求补丁
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        Log.i(TinkerAutoFixStartUtil.TFS_Tag, "onReceive......: " + System.currentTimeMillis());
//        String msg = intent.getStringExtra("msg");
//        Toast.makeText(context, "TinkerFixReceiver + :  " + System.currentTimeMillis(), Toast.LENGTH_SHORT).show();
        fetchForPatch(context);
    }

    private void fetchForPatch(final Context context) {
        TinkerApiClient apiClient = new TinkerApiClient();
        apiClient.fetchForAppPatch(new LoadingAnimResponseHandler(context, false) {
            @Override
            public void onSuccess(String response) {
                Log.i(TinkerAutoFixStartUtil.TFS_Tag, "fetchForPatch......: " + response);
                if (!TextUtils.isEmpty(response)) {
                    operateResponse(response, context);
                }
            }
        });
    }

    private void operateResponse(String json, Context context) {
        Log.i(TinkerAutoFixStartUtil.TFS_Tag, "operateResponse......: " + json);

    }
}
