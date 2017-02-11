package com.hxqc.autonews.model;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.reflect.TypeToken;
import com.hxqc.autonews.api.AutoInformationApiClient;
import com.hxqc.autonews.model.pojos.InfoType;
import com.hxqc.mall.core.api.LoadingAnimResponseHandler;
import com.hxqc.mall.core.interfaces.CacheDataListener;
import com.hxqc.mall.core.interfaces.LoadDataCallBack;
import com.hxqc.mall.core.util.ACache;
import com.hxqc.mall.core.util.NetWorkUtils;
import com.hxqc.util.DebugLog;
import com.hxqc.util.JSONUtils;

import cz.msebera.android.httpclient.Header;

import java.util.ArrayList;

/**
 * Author:李烽
 * Date:2016-09-29
 * FIXME
 * Todo  汽车资讯类型管理类
 */

public class AutoInfoTypeModel implements IModel<ArrayList<InfoType>> {
    private static final String CACHE_TYPE = "info_type_cache";
    private AutoInformationApiClient apiClient;

    private Context mContext;

    public AutoInfoTypeModel(Context mContext) {
        this.apiClient = new AutoInformationApiClient();
        this.mContext = mContext;
    }

    /**
     * todo 获取数据（不带缓存检查）
     * @param callBack
     */
    @Override
    public void getData(final LoadDataCallBack<ArrayList<InfoType>> callBack) {
        apiClient.requestInfoType(new LoadingAnimResponseHandler(mContext, true) {
            @Override
            public void onSuccess(String response) {
                ArrayList<InfoType> infoTypes = JSONUtils.fromJson(response, new TypeToken<ArrayList<InfoType>>() {
                });
                if (infoTypes != null)
                    callBack.onDataGot(infoTypes);
                else callBack.onDataNull("");
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                callBack.onDataNull(responseString);
            }
        });
    }
    /**
     * todo 获取数据（带缓存检查）
     * @param callBack
     */
    @Override
    public void getData(final CacheDataListener<ArrayList<InfoType>> callBack) {
        final ACache aCache = ACache.get(mContext);
        final String jsonType = aCache.getAsString(CACHE_TYPE);
        if (!TextUtils.isEmpty(jsonType)) {
            ArrayList<InfoType> types = JSONUtils.fromJson(jsonType, new TypeToken<ArrayList<InfoType>>() {
            });
            if (types != null && !types.isEmpty()) {
                callBack.onCacheDataBack(types);
            } else callBack.onCacheDataNull();
        } else callBack.onCacheDataNull();

        if (NetWorkUtils.getNetworkTypeName(mContext).equals(NetWorkUtils.NETWORK_TYPE_DISCONNECT))
            return;//情况下不进行请求

        apiClient.requestInfoType(new LoadingAnimResponseHandler(mContext, TextUtils.isEmpty(jsonType)) {
            @Override
            public void onSuccess(String response) {
                if (!TextUtils.isEmpty(jsonType) && jsonType.equals(response)) {
                    DebugLog.d(getClass().getSimpleName(),"和缓存的数据一致");
                    return;//如果和缓存的数据一致就return出去，否则数据给到View
                }
                ArrayList<InfoType> infoTypes = JSONUtils.fromJson(response, new TypeToken<ArrayList<InfoType>>() {
                });
                aCache.put(CACHE_TYPE, response);

                if (infoTypes != null && !infoTypes.isEmpty())
                    callBack.onDataGot(infoTypes);
                else callBack.onDataNull("");
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                callBack.onDataNull(responseString);
            }
        });
    }
}
