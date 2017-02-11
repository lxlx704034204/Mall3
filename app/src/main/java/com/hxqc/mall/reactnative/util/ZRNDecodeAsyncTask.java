package com.hxqc.mall.reactnative.util;

import android.content.Context;
import android.os.AsyncTask;

import com.hxqc.mall.core.bzinga.DES3;
import com.hxqc.util.DebugLog;
import com.hxqc.util.FileUtil;

/**
 * Author: wanghao
 * Date: 2016-03-18
 * FIXME
 * Todo  rn bundle 后台解密
 */
public class ZRNDecodeAsyncTask extends AsyncTask< Void, Integer, Long > {

    public static String TAG = "bsdiff_rn";
    Context ctx;
    String fileWholePath = "";
    String saveWholePath = "";
    OnDecodeFinishListener decodeFinishListener;

    public ZRNDecodeAsyncTask(Context ctx, String fileWholePath, String saveWholePath) {
        this.ctx = ctx;
        this.fileWholePath = fileWholePath;
        this.saveWholePath = saveWholePath;
    }

    public void setDecodeFinishListener(OnDecodeFinishListener decodeFinishListener) {
        this.decodeFinishListener = decodeFinishListener;
    }

    @Override
    protected Long doInBackground(Void... params) {
        deCodeAndSave();
        return null;
    }

    @Override
    protected void onPostExecute(Long result) {
        if (decodeFinishListener != null) {
            decodeFinishListener.decodeFinish();
        }
        if (isCancelled())
            return;
    }

    /**
     * 解密
     */
    private void deCodeAndSave() {
        String fileStr = FileUtil.convertCodeAndGetText(fileWholePath);
        try {
            DebugLog.i("rn_test", "deCode");
            FileUtil.writerFile(ctx, saveWholePath, DES3.decode(fileStr));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public interface OnDecodeFinishListener {
        void decodeFinish();
    }
}
