package com.hxqc.mall.qr.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.hxqc.mall.core.util.ActivitySwitchBase;
import com.hxqc.mall.qr.android.CaptureActivity;
import com.hxqc.mall.qr.android.QRTransferActivity;

/**
 * Author:  wh
 * Date:  2016/11/11
 * FIXME
 * Todo
 */

public class QRActivitySwitch extends ActivitySwitchBase{

    /**
     * 二维码相关 =====================================================================
     */
    public static final int REQUEST_CODE_SCAN = 0x0000;
    public static final String DECODED_CONTENT_KEY = "codedContent";
    public static final String DECODED_BITMAP_KEY = "codedBitmap";

    /**
     * 调到中转页面
     */
    public static void toQRTransferPage(Context mC){
        Intent intent = new Intent(mC, QRTransferActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mC.startActivity(intent);
    }

    /**
     * 开启二维码
     */
    public static void  toScanCode(Activity mA){
        Intent intent = new Intent(mA, CaptureActivity.class);
        mA.startActivityForResult(intent,REQUEST_CODE_SCAN);
    }


}
