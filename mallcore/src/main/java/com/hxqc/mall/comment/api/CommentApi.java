package com.hxqc.mall.comment.api;

import com.hxqc.mall.core.api.ApiUtil;
import com.hxqc.mall.core.api.BaseApiClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * @Author : 钟学东
 * @Since : 2016-05-30
 * FIXME
 * Todo  评论api
 */
public class CommentApi extends BaseApiClient{

    @Override
    protected String completeUrl(String control) {
        return ApiUtil.getAccountURL(control);
    }


    /**
     *
     * @param count 分页页数，不传时默认为1
     * @param page  分页每页条目数，不传时默认为15
     * @param handler
     */
    public void MyCommentList(int count , int page ,AsyncHttpResponseHandler handler ){
        String url = completeUrl("/MyCommentList");
        RequestParams requestParams = new RequestParams();
        requestParams.put("count", count);
        requestParams.put("page", page);
        gGetUrl(url, requestParams, handler);
    }

}
