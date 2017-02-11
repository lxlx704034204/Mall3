package com.hxqc.mall.qr.android;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.hxqc.mall.core.util.ToastHelper;
import com.hxqc.mall.qr.util.QRActivitySwitch;
import com.hxqc.util.DebugLog;

/**
 * Author:  wh
 * Date:  2016/11/11
 * FIXME
 * Todo
 */

public class QRTransferActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        takePhoto();
    }

    /**
     * 防止横竖屏销毁    与manifest对应 复写此方法无作为
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        DebugLog.i("qr_c","onConfigurationChanged");
        super.onConfigurationChanged(newConfig);
    }

    private void takePhoto() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            //申请WRITE_EXTERNAL_STORAGE权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1);
        } else {
            QRActivitySwitch.toScanCode(this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        doNext(requestCode, grantResults);
    }

    private void doNext(int requestCode, int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                QRActivitySwitch.toScanCode(this);
            } else {
                ToastHelper.showYellowToast(this, "请在应用管理中打开“相机”访问权限！");
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        finish();
                    }
                }, 2300);
            }
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        DebugLog.i("scan_code", "QRTransferActivity: " + requestCode);
        // 扫描二维码/条码回传
        if (requestCode == QRActivitySwitch.REQUEST_CODE_SCAN && resultCode == RESULT_OK) {
            if (data != null) {
                String content = data.getStringExtra(QRActivitySwitch.DECODED_CONTENT_KEY);
                DebugLog.i("scan_code", "二维码获取的数据： \n" + content);
            }
        }
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DebugLog.i("scan_code", "onDestroy");
    }
}
