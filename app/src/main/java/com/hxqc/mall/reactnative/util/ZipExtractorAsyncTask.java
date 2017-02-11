package com.hxqc.mall.reactnative.util;


import android.content.Context;
import android.os.AsyncTask;

import com.hxqc.mall.reactnative.model.UnZipFileModel;
import com.hxqc.util.DebugLog;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;


/**
 * Author: wanghao
 * Date: 2016-03-11
 * FIXME    rn zip文件解压
 * Todo
 */
public class ZipExtractorAsyncTask extends AsyncTask<Void, Integer, Long> {

    public static String TAG = "bsdiff_rn";
    private final File mInput;
    private final File mOutput;
//    private final ProgressDialog mDialog;
    private final Context mContext;
    private OnUnZipFinishListener unZipFinishListener;
    private int mProgress = 0;
    private boolean mReplaceAll;
    private boolean isDialogShow = false;
    private boolean isUnZipFail = false;


    public ZipExtractorAsyncTask(boolean showProgress, String in, String out, Context context, boolean replaceAll) {
        super();
        DebugLog.e(TAG, "初始化解压缩");
        DebugLog.e(TAG, "in path: " + in);
        DebugLog.e(TAG, "out path: " + out);
        this.isDialogShow = showProgress;
        mInput = new File(in);
        mOutput = new File(out);

        DebugLog.e(TAG, "mInput: " + mInput.exists());

        if (!mOutput.exists()) {
            if (!mOutput.mkdirs()) {
                DebugLog.e(TAG, "Failed to make directories:" + mOutput.getAbsolutePath());
            }
        }
//        if (context != null && isDialogShow) {
//            mDialog = new ProgressDialog(context);
//        } else {
//            mDialog = null;
//        }
        mContext = context;
        mReplaceAll = replaceAll;
    }

    public void setUnZipFinishListener(OnUnZipFinishListener unZipFinishListener) {
        this.unZipFinishListener = unZipFinishListener;
    }

    @Override
    protected Long doInBackground(Void... params) {
        DebugLog.e(TAG, "doInBackground 解压缩");
        return unzip();
    }

    @Override
    protected void onPostExecute(Long result) {
        DebugLog.i(TAG, "   文件  解压完成  ");
        if (unZipFinishListener != null && !isUnZipFail) {
            UnZipFileModel model = new UnZipFileModel(mOutput.getAbsolutePath());
            unZipFinishListener.unZipFinish(model);
        }
//        if (mDialog != null && mDialog.isShowing()) {
//            mDialog.dismiss();
//        }
        if (isCancelled())
            return;
    }

    @Override
    protected void onPreExecute() {
//        if (mDialog != null) {
//            mDialog.setTitle("Extracting");
//            mDialog.setMessage(mInput.getName());
//            mDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//            mDialog.setOnCancelListener(new OnCancelListener() {
//
//                @Override
//                public void onCancel(DialogInterface dialog) {
//                    // TODO Auto-generated method stub
//                    cancel(true);
//                }
//            });
//            mDialog.show();
//        }
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        // TODO Auto-generated method stub
        //super.onProgressUpdate(values);
//        if (mDialog == null)
//            return;
//        if (values.length > 1) {
//            int max = values[1];
//            mDialog.setMax(max);
//        }
//        else
//            mDialog.setProgress(values[0].intValue());
    }

    private long unzip() {

        DebugLog.e(TAG, "unzip 解压缩");
        long extractedSize = 0L;
        Enumeration<ZipEntry> entries;
        ZipFile zip = null;

        if (!mInput.exists()) {
            DebugLog.e(TAG, "unzip 解压缩  文件不存在");
            isUnZipFail = true;
            return extractedSize;
        }

        try {
            zip = new ZipFile(mInput);
            long uncompressedSize = getOriginalSize(zip);
            publishProgress(0, (int) uncompressedSize);

            entries = (Enumeration<ZipEntry>) zip.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                if (entry.isDirectory()) {
                    continue;
                }
                File destination = new File(mOutput, entry.getName());
                if (!destination.getParentFile().exists()) {
                    DebugLog.e(TAG, "make=" + destination.getParentFile().getAbsolutePath());
                    destination.getParentFile().mkdirs();
                }
                if (destination.exists() && mContext != null && !mReplaceAll) {

                }
                ProgressReportingOutputStream outStream = new ProgressReportingOutputStream(destination);
                extractedSize += copy(zip.getInputStream(entry), outStream);
                outStream.close();
            }
        } catch (ZipException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                zip.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        return extractedSize;
    }

    private long getOriginalSize(ZipFile file) {
        Enumeration<ZipEntry> entries = (Enumeration<ZipEntry>) file.entries();
        long originalSize = 0l;
        while (entries.hasMoreElements()) {
            ZipEntry entry = entries.nextElement();
            if (entry.getSize() >= 0) {
                originalSize += entry.getSize();
            }
        }
        return originalSize;
    }

    private int copy(InputStream input, OutputStream output) {
        byte[] buffer = new byte[1024 * 8];
        BufferedInputStream in = new BufferedInputStream(input, 1024 * 8);
        BufferedOutputStream out = new BufferedOutputStream(output, 1024 * 8);
        int count = 0, n = 0;
        try {
            while ((n = in.read(buffer, 0, 1024 * 8)) != -1) {
                out.write(buffer, 0, n);
                count += n;
            }
            out.flush();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            try {
                in.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return count;
    }

    public interface OnUnZipFinishListener {
        void unZipFinish(UnZipFileModel model);
    }

    private final class ProgressReportingOutputStream extends FileOutputStream {

        public ProgressReportingOutputStream(File file)
                throws FileNotFoundException {
            super(file);
            // TODO Auto-generated constructor stub
        }

        @Override
        public void write(byte[] buffer, int byteOffset, int byteCount)
                throws IOException {
            // TODO Auto-generated method stub
            super.write(buffer, byteOffset, byteCount);
            mProgress += byteCount;
            publishProgress(mProgress);
        }

    }


}
