package com.hxqc.autonews.model;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.reflect.TypeToken;
import com.hxqc.autonews.api.AutoInformationApiClient;
import com.hxqc.autonews.model.pojos.AutoInfoHomeData;
import com.hxqc.autonews.model.pojos.AutoInformation;
import com.hxqc.autonews.util.CacheAutoInfoUtil;
import com.hxqc.autonews.util.ListReadRecolder;
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
 * Date:2016-09-01
 * FIXME
 * Todo 汽车资讯首页数据管理类
 */
public class AutoInformationModel implements IModel<AutoInfoHomeData> {

    private static final String CACHE_HOME_DATA = "home_data_cache";
    private static final String CACHE_TYPE = "info_cache_type_";
    private static final String RECOMMENT = "recomment_auto_info";
    private AutoInformationApiClient apiClient;

    public static final int count = 15;

    private Context mContext;
    private int page = 1;
    private ACache aCache;

    public AutoInformationModel(Context context) {
        apiClient = new AutoInformationApiClient();
        mContext = context;
        aCache = ACache.get(context);
    }


    public void getAutoInfoHomeData(int page, final LoadDataCallBack<AutoInfoHomeData> callBack) {
        DebugLog.d(getClass().getSimpleName(), "getAutoInfoHomeData");
        this.page = page;
        getData(callBack);
    }

    public void getAutoInfoHomeData(int page, final CacheDataListener<AutoInfoHomeData> callBack) {
        DebugLog.d(getClass().getSimpleName(), "getAutoInfoHomeData");
        this.page = page;
        getData(callBack);
    }

    /**
     * 最新的分类下加载更多的请求
     *
     * @param page
     * @param listener
     */
    public void loadMoreRecommentAutoListData(final int page,
                                              final CacheDataListener<ArrayList<AutoInformation>> listener) {
        if (page == 1)//加载更多page必须大于1
            return;
        final ArrayList<AutoInformation> listDataPage = CacheAutoInfoUtil.getListDataPage(aCache, page, RECOMMENT);
        boolean hasCacheData = listDataPage != null && !listDataPage.isEmpty();
        if (hasCacheData) {
            //有缓存的数据
            ListReadRecolder.checkRead(listDataPage);
            listener.onCacheDataBack(listDataPage);
        } else listener.onCacheDataNull();
        if (NetWorkUtils.getNetworkTypeName(mContext).equals(NetWorkUtils.NETWORK_TYPE_DISCONNECT))
            return;//情况下不进行请求
        apiClient.requestAutoInformation(count, page,
                new LoadingAnimResponseHandler(mContext, !hasCacheData) {
                    @Override
                    public void onSuccess(String response) {
                        AutoInfoHomeData autoInfoHomeData = JSONUtils.fromJson(response, AutoInfoHomeData.class);
                        boolean equals = CacheAutoInfoUtil.equals(listDataPage, autoInfoHomeData.infoList);
                        if (equals) {
                            DebugLog.d(getClass().getSimpleName(), "获取数据和缓存数据一样");
                            return;//相同就没有必要在回调
                        }
                        if (autoInfoHomeData == null || (autoInfoHomeData.infoList == null || autoInfoHomeData.infoList.size() == 0)) {
                            listener.onDataNull("");
                        } else {
                            ListReadRecolder.markRead(autoInfoHomeData);
                            CacheAutoInfoUtil.saveListData(page, aCache, RECOMMENT, autoInfoHomeData.infoList);//存储
                            listener.onDataGot(autoInfoHomeData.infoList);
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);
                        listener.onDataNull(responseString);
                    }
                });
    }

    /**
     * 请求更多当前type下资讯列表数据
     *
     * @param page
     * @param guiCode
     * @param listener
     */
    public void loadMoreAutoListDataByType(final int page, final String guiCode,
                                           final CacheDataListener<ArrayList<AutoInformation>> listener) {
        if (page == 1)//加载更多page必须大于1
            return;
        final ArrayList<AutoInformation> listDataPage = CacheAutoInfoUtil.getListDataPage(aCache, page, guiCode);
        boolean hasCacheData = listDataPage != null && !listDataPage.isEmpty();
        if (hasCacheData) {
            //有缓存的数据
            ListReadRecolder.checkRead(listDataPage);
            listener.onCacheDataBack(listDataPage);
        } else listener.onCacheDataNull();
        if (NetWorkUtils.getNetworkTypeName(mContext).equals(NetWorkUtils.NETWORK_TYPE_DISCONNECT))
            return;//情况下不进行请求
        apiClient.requestAutoInformationByType(count, page, guiCode,
                new LoadingAnimResponseHandler(mContext, !hasCacheData) {
                    @Override
                    public void onSuccess(String response) {
                        ArrayList<AutoInformation> autoInformations = JSONUtils.fromJson(response, new TypeToken<ArrayList<AutoInformation>>() {
                        });
                        boolean equals = CacheAutoInfoUtil.equals(listDataPage, autoInformations);
                        if (equals) {
                            DebugLog.d(getClass().getSimpleName(), "获取数据和缓存数据一样");
                            return;//相同就没有必要在回调
                        }
                        if (autoInformations == null || autoInformations.isEmpty()) {
                            listener.onDataNull("");
                        } else {
                            ListReadRecolder.checkRead(autoInformations);//标记已读
                            CacheAutoInfoUtil.saveListData(page, aCache, guiCode, autoInformations);//存储
                            listener.onDataGot(autoInformations);
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);
                        listener.onDataNull(responseString);
                    }
                });
    }

    @Override
    public void getData(final LoadDataCallBack<AutoInfoHomeData> callBack) {
        DebugLog.d(getClass().getSimpleName(), "getData");
        apiClient.requestAutoInformation(count, page, new LoadingAnimResponseHandler(mContext, true) {
            @Override
            public void onSuccess(String response) {
                AutoInfoHomeData autoInfoHomeData = JSONUtils.fromJson(response, AutoInfoHomeData.class);
                if (autoInfoHomeData == null || (autoInfoHomeData.banner.size() == 0 && autoInfoHomeData.infoList.size() == 0)) {
                    callBack.onDataNull("");
                } else {
                    callBack.onDataGot(autoInfoHomeData);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                callBack.onDataNull(responseString);
            }
        });
    }

    @Override
    public void getData(final CacheDataListener<AutoInfoHomeData> callBack) {
        final String cacheString = aCache.getAsString(CACHE_HOME_DATA);
        DebugLog.d(getClass().getSimpleName(), cacheString);
        if (!TextUtils.isEmpty(cacheString)) {
            AutoInfoHomeData data = JSONUtils.fromJson(cacheString, AutoInfoHomeData.class);
            if (data != null) {
                ListReadRecolder.markRead(data);
                callBack.onCacheDataBack(data);
            } else callBack.onCacheDataNull();
        } else callBack.onCacheDataNull();
        if (NetWorkUtils.getNetworkTypeName(mContext).equals(NetWorkUtils.NETWORK_TYPE_DISCONNECT))
            return;//情况下不进行请求
        apiClient.requestAutoInformation(count, 1,
                new LoadingAnimResponseHandler(mContext, TextUtils.isEmpty(cacheString)) {
                    @Override
                    public void onSuccess(String response) {
                        if (!TextUtils.isEmpty(cacheString) && cacheString.equals(response)) {
                            DebugLog.d(getClass().getSimpleName(), "获取数据和缓存数据一样");
                            return;//如果和缓存的数据一致就return出去，否则数据给到View
                        }
                        AutoInfoHomeData autoInfoHomeData = JSONUtils.fromJson(response, AutoInfoHomeData.class);
                        //缓存
                        aCache.put(CACHE_HOME_DATA, response);

                        if (autoInfoHomeData == null || (autoInfoHomeData.banner.size() == 0 && autoInfoHomeData.infoList.size() == 0)) {
                            callBack.onDataNull("");
                        } else {
                            ListReadRecolder.markRead(autoInfoHomeData);
                            callBack.onDataGot(autoInfoHomeData);
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);
                        callBack.onDataNull(responseString);
                    }
                });
    }


    public void getDataByType(int p, String guideCode, final LoadDataCallBack<ArrayList<AutoInformation>> callBack) {
        apiClient.requestAutoInformationByType(count, p, guideCode, new LoadingAnimResponseHandler(mContext, true) {
            @Override
            public void onSuccess(String response) {
                ArrayList<AutoInformation> autoInformations = JSONUtils.fromJson(response, new TypeToken<ArrayList<AutoInformation>>() {
                });
                if (autoInformations != null) {
                    callBack.onDataGot(autoInformations);
                } else callBack.onDataNull("");
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                callBack.onDataNull(responseString);
            }
        });
    }

    public void getDataByType(int p, final String guideCode, final CacheDataListener<ArrayList<AutoInformation>> callBack) {
        final String cacheString = aCache.getAsString(CACHE_TYPE + guideCode);
        DebugLog.d(getClass().getSimpleName(), cacheString);
        if (!TextUtils.isEmpty(cacheString)) {
            ArrayList<AutoInformation> autoInformations = JSONUtils.fromJson(cacheString, new TypeToken<ArrayList<AutoInformation>>() {
            });
            if (autoInformations != null && !autoInformations.isEmpty()) {
                ListReadRecolder.checkRead(autoInformations);
                callBack.onCacheDataBack(autoInformations);
            } else callBack.onCacheDataNull();
        } else callBack.onCacheDataNull();
        if (NetWorkUtils.getNetworkTypeName(mContext).equals(NetWorkUtils.NETWORK_TYPE_DISCONNECT))
            return;//情况下不进行请求
        apiClient.requestAutoInformationByType(count, p, guideCode,
                new LoadingAnimResponseHandler(mContext, TextUtils.isEmpty(cacheString)) {
                    @Override
                    public void onSuccess(String response) {
                        if (!TextUtils.isEmpty(cacheString) && !response.equals("[]") && cacheString.equals(response)) {
                            DebugLog.d(getClass().getSimpleName(), "获取数据和缓存数据一样");
                            return;//如果和缓存的数据一致就return出去，否则数据给到View
                        }
                        ArrayList<AutoInformation> autoInformations = JSONUtils.fromJson(response, new TypeToken<ArrayList<AutoInformation>>() {
                        });
                        aCache.put(CACHE_TYPE + guideCode, response);
                        if (autoInformations != null && !autoInformations.isEmpty()) {
                            ListReadRecolder.checkRead(autoInformations);
                            callBack.onDataGot(autoInformations);
                        } else callBack.onDataNull("");
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);
                        callBack.onDataNull(responseString);
                    }
                });
    }
}
