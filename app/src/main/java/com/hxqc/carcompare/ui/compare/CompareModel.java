package com.hxqc.carcompare.ui.compare;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hxqc.carcompare.api.CarCompareApiClient;
import com.hxqc.carcompare.model.CompareNew;
import com.hxqc.carcompare.model.comparedisc.Discuss;
import com.hxqc.mall.core.util.utils.disklurcache.DiskLruCacheHelper;
import com.hxqc.util.JSONUtils;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.util.List;

/**
 * Model
 * Created by zf
 */
public class CompareModel implements CompareContract.Model {
    public static final String BASE_DATAS_CACHE = "basic_datas_cache";
    public static final String DISCUSS_DATAS_CACHE = "discuss_datas_cache";
    private DiskLruCacheHelper mDiskLruCache;

    /**
     * 获取网络数据
     */
    @Override
    public void getCompareData(int type, String modelId, AsyncHttpResponseHandler handler) {
        new CarCompareApiClient().getCompareData(type, modelId, handler);
    }

    /**
     * 保存基本参数对比的缓存数据
     */
    @Override
    public void UpdataBasicParmListCache(List<List<CompareNew>> data) {
        mDiskLruCache.put(BASE_DATAS_CACHE, new Gson().toJson(data));
    }

    /**
     * 获取基本参数对比的缓存数据
     */
    @Override
    public List<List<CompareNew>> getBasicParmListCache() {
        return JSONUtils.fromJson(mDiskLruCache.getAsString(BASE_DATAS_CACHE), new TypeToken<List<List<CompareNew>>>() {
        });
    }

    /**
     * 保存评论对比的缓存数据
     */
    @Override
    public void saveDiscussDatas(List<Discuss> reponse_dics) {
        mDiskLruCache.put(DISCUSS_DATAS_CACHE, new Gson().toJson(reponse_dics));
    }

    /**
     * 获取评论对比的缓存数据
     */
    @Override
    public List<Discuss> getDiscussDatas() {
        return JSONUtils.fromJson(mDiskLruCache.getAsString(DISCUSS_DATAS_CACHE), new TypeToken<List<Discuss>>() {
        });
    }

    @Override
    public void clearCache() {
        mDiskLruCache.remove(BASE_DATAS_CACHE);
        mDiskLruCache.remove(DISCUSS_DATAS_CACHE);
    }


    @Override
    public void init(Context context) {
        mDiskLruCache = DiskLruCacheHelper.builder(context);
    }
}
