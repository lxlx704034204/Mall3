package com.hxqc.mall.core.bsdiff;

import android.os.Handler;
import android.os.Message;

import com.hxqc.util.DebugLog;
import com.skywds.android.bsdiffpatch.JniApi;

import java.io.File;

/**
 * Author:  wh
 * Date:  2016/8/16
 * FIXME
 * Todo
 */
public class BsdiffUtilManager {
    //旧文件获取地址
    private String mOldFileP;
    //新文件存放地址
    private String mNewFileP;
    //补丁地址
    private String mPatchP;

    private OnBsdiffExecuteListener onBsdiffExecuteListener;

    public void setOnBsdiffExecuteListener(OnBsdiffExecuteListener onBsdiffExecuteListener) {
        this.onBsdiffExecuteListener = onBsdiffExecuteListener;
    }

    private final int Bsdiff_Execute_Success = 1;
    private final int Bsdiff_Execute_Fail = 2;


    private Handler mH = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {

            //如果未设置监听  退出
            if (onBsdiffExecuteListener ==null){
                return false;
            }

            if (msg.what == Bsdiff_Execute_Success){
                onBsdiffExecuteListener.onBspatchSuccess();
            }else if (msg.what == Bsdiff_Execute_Fail){
                onBsdiffExecuteListener.onBspatchFail();
            }

            return false;
        }
    });


    public BsdiffUtilManager() {
    }


    /**
     * 开始合并
     */
    public void startCombine(String oldFilePath, String newFilePath, String patchFilePath) {

        this.mOldFileP = oldFilePath;
        this.mNewFileP = newFilePath;
        this.mPatchP = patchFilePath;

        new Thread(new BsPatchRunnable()).start();
    }

    /**
     * 增量更新库。需要放到异步线程中使用 !!!
     */
    private class BsPatchRunnable implements Runnable {
        @Override
        public void run() {

            DebugLog.i("rn_test", "mOldFileP "+mOldFileP);
            DebugLog.i("rn_test", "mPatchP "+mPatchP);
            DebugLog.i("rn_test", "mNewFileP "+mNewFileP);

            File fileOld = new File(mOldFileP);
            if (!fileOld.exists()){
                DebugLog.i("rn_test","老文件不存在");
                return;
            }

            File filePatch = new File(mPatchP);
            if (!filePatch.exists()){
                DebugLog.i("rn_test","更新文件不存在");
                return;
            }

            File fileNew = new File(mNewFileP);
            if (!fileNew.exists()){
                DebugLog.i("rn_test","新文件路径不存在");
            }

            //增量更新生成新版apk安装包
            int bspatch = JniApi.bspatch(mOldFileP, mNewFileP, mPatchP);
            DebugLog.i("rn_test", "增量更新文件处理完成:" + bspatch);

            //操作完 发送消息通知
            Message message = Message.obtain();
            if (bspatch == 0) {
                DebugLog.i("rn_test", "补丁包处理完毕, 开始安装新版本");
                message.what = Bsdiff_Execute_Success;
            } else {
                DebugLog.i("rn_test", "补丁包处理失败");
                message.what = Bsdiff_Execute_Fail;
            }
            mH.sendMessage(message);
        }
    }


    /**
     * 增量更新 执行状态监听
     */
  public   interface OnBsdiffExecuteListener{
        void onBspatchSuccess();
        void onBspatchFail();
    }
}
