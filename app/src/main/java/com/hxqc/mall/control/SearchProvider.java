package com.hxqc.mall.control;

import com.hxqc.mall.api.NewAutoClient;
import com.hxqc.mall.core.model.HotKeyword;
import com.hxqc.util.JSONUtils;
import com.loopj.android.http.TextHttpResponseHandler;

import cz.msebera.android.httpclient.Header;

/**
 * Author: 胡俊杰
 * Date: 2015-07-02
 * FIXME
 * Todo 新车搜索
 */
public class SearchProvider {
    private static SearchProvider ourInstance = new SearchProvider();

    public static SearchProvider getInstance() {
        if (ourInstance == null) {
            synchronized (SearchProvider.class) {
                if (ourInstance == null) {
                    ourInstance = new SearchProvider();
                }
            }
        }
        return ourInstance;
    }

    public interface RequestHotKeywordListener {
        void onHotKeyword(HotKeyword hotKeyword);
    }

    NewAutoClient client = new NewAutoClient();
    HotKeyword mHotKeyword;
    RequestHotKeywordListener onRequestHotKeywordListener;

    public SearchProvider setOnRequestHotKeywordListener(
            RequestHotKeywordListener onRequestHotKeywordListener) {
        this.onRequestHotKeywordListener = onRequestHotKeywordListener;
        return ourInstance;
    }

    private SearchProvider() {
    }

    public void requestHotKeyword() {
        if (mHotKeyword != null) {
            if (onRequestHotKeywordListener != null) {
                onRequestHotKeywordListener.onHotKeyword(mHotKeyword);
            }
            return;
        }
        client.searchHotKetWord(new TextHttpResponseHandler() {

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString,
                                  Throwable throwable) {
//                requestHotKeyword();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                mHotKeyword = JSONUtils.fromJson(responseString, HotKeyword.class);
                if (onRequestHotKeywordListener != null) {
                    onRequestHotKeywordListener.onHotKeyword(mHotKeyword);
                }

            }
        });
    }

    public void onDestroy() {
        ourInstance = null;
    }
}
