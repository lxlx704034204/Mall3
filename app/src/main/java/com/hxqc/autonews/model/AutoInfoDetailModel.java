package com.hxqc.autonews.model;

import android.content.Context;

import com.hxqc.autonews.api.AutoInformationApiClient;
import com.hxqc.autonews.model.pojos.AutoInfoDetail;
import com.hxqc.autonews.util.CacheAutoInfoUtil;
import com.hxqc.mall.core.api.LoadingAnimResponseHandler;
import com.hxqc.mall.core.interfaces.CacheDataListener;
import com.hxqc.mall.core.interfaces.LoadDataCallBack;
import com.hxqc.mall.core.util.ACache;
import com.hxqc.mall.core.util.NetWorkUtils;
import com.hxqc.util.DebugLog;
import com.hxqc.util.JSONUtils;

import cz.msebera.android.httpclient.Header;

/**
 * Author:李烽
 * Date:2016-09-01
 * FIXME
 * Todo 汽车资讯详情的数据管理类
 */
public class AutoInfoDetailModel implements IModel<AutoInfoDetail> {
    private static final String CACHE_NAME = "autoInfoDetailCache";
    private Context mContext;
    private AutoInformationApiClient apiClient;

    private String infoID = "";
    private ACache aCache;

    public AutoInfoDetailModel(Context context) {
        mContext = context;
        apiClient = new AutoInformationApiClient();
        aCache = ACache.get(context, CACHE_NAME);
    }

    @Override
    public void getData(final LoadDataCallBack<AutoInfoDetail> callBack) {
        apiClient.requestAutoInformationDetail(infoID, new LoadingAnimResponseHandler(mContext, true) {
            @Override
            public void onSuccess(String response) {
                AutoInfoDetail autoInfoDetail = JSONUtils.fromJson(response, AutoInfoDetail.class);
                callBack.onDataGot(autoInfoDetail);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                callBack.onDataNull(responseString);
            }
        });
    }

    @Override
    public void getData(final CacheDataListener<AutoInfoDetail> callBack) {
        final AutoInfoDetail cacheAutoDetial = CacheAutoInfoUtil.getCacheAutoDetial(infoID, aCache);
        if (cacheAutoDetial != null) {
            callBack.onCacheDataBack(cacheAutoDetial);
        } else
            callBack.onCacheDataNull();
        if (NetWorkUtils.getNetworkTypeName(mContext).equals(NetWorkUtils.NETWORK_TYPE_DISCONNECT))
            return;//情况下不进行请求
        apiClient.requestAutoInformationDetail(infoID,
                new LoadingAnimResponseHandler(mContext, cacheAutoDetial == null) {
                    @Override
                    public void onSuccess(String response) {
                        AutoInfoDetail autoInfoDetail = JSONUtils.fromJson(response, AutoInfoDetail.class);
                        CacheAutoInfoUtil.saveAutoInfoDetail(aCache, infoID, autoInfoDetail);
                        boolean equals = CacheAutoInfoUtil.equals(cacheAutoDetial, autoInfoDetail);
                        if (equals) {
                            DebugLog.d(getClass().getSimpleName(), "获取数据和缓存数据一样");
                            return;
                        }
                        callBack.onDataGot(autoInfoDetail);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);
                        callBack.onDataNull(responseString);
                    }
                });

    }

    /**
     * 获取汽车资讯详情
     *
     * @param infoID
     * @param callBack
     */
    public void getAutoDetail(String infoID, LoadDataCallBack<AutoInfoDetail> callBack) {
        this.infoID = infoID;
        getData(callBack);
    }

    /**
     * 获取汽车资讯详情
     *
     * @param infoID
     * @param callBack
     */
    public void getAutoDetail(String infoID, CacheDataListener<AutoInfoDetail> callBack) {
        this.infoID = infoID;
        getData(callBack);
    }
}
