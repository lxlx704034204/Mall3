package com.hxqc.mall.drivingexam.api;


import com.hxqc.mall.core.api.ApiUtil;
import com.hxqc.mall.core.api.BaseApiClient;
import com.hxqc.mall.drivingexam.api.callback.FileDownloadCallBack;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.zhy.http.okhttp.OkHttpUtils;


/**
 * 驾考模拟接口
 */
public class ExamApiClient extends BaseApiClient {

    @Override
    protected String completeUrl(String control) {
        // return "http://app-interface.t.hxqctest.com/Service/V2" + control;

        return ApiUtil.getAroundURL(control);

    }


    /**
     * 获取题目
     *
     * @param kemu
     * @param questionID
     */
    public void getQusetion(String kemu, String questionID, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/DrivingLicenseExam");
        RequestParams requestParams = new RequestParams();
        requestParams.put("deviceType", "Android");
        requestParams.put("kemu", kemu);
        requestParams.put("questionID", questionID);
        gGetUrl(url, requestParams, handler);
    }


    /**
     * 获取全部题目id
     */
    public void QusetionNumber(String kemu, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/DrivingLicenseExam/QusetionNumber");
        RequestParams requestParams = new RequestParams();
        requestParams.put("deviceType", "Android");
        requestParams.put("kemu", kemu);
        gGetUrl(url, requestParams, handler);
    }

    /**
     * 下载视频
     */
    public void downloadMedia(FileDownloadCallBack callBack) {
        String url = completeUrl("/Service/downvideo");
        OkHttpUtils.get().tag("downloadMedia").url(url).build()
                .execute(callBack);
    }


    public void cancelDownload() {
        OkHttpUtils.getInstance().cancelTag("downloadMedia");
    }


}
