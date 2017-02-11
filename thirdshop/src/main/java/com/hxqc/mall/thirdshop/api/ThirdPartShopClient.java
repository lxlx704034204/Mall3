package com.hxqc.mall.thirdshop.api;


import android.text.TextUtils;

import com.hxqc.mall.auto.model.Filter;
import com.hxqc.mall.core.api.ApiUtil;
import com.hxqc.mall.core.api.BaseApiClient;
import com.hxqc.mall.thirdshop.constant.FilterResultKey;
import com.hxqc.util.DebugLog;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Author:胡俊杰
 * Date: 2015/11/30
 * FIXME
 * Todo 第三方商铺接口
 */
public class ThirdPartShopClient extends BaseApiClient {

	@Override
	protected String completeUrl(String control) {
//        return HOST + "/Shop/" + API_VERSION + control;
		return ApiUtil.getShopURL(control);
	}

	public String getGrouponURL(String sitID) {
		RequestParams requestParams = new RequestParams();
		requestParams.put("site", sitID);
		return getRequestUrl(completeUrl("/Groupon"), requestParams);
	}

	/**
	 * 获取店铺信息
	 *
	 * @param shopID  店铺id
	 * @param handler
	 */
	public void shopInfo(String shopID, AsyncHttpResponseHandler handler) {
		String url = completeUrl("/Shop");
		RequestParams requestParams = new RequestParams();
		requestParams.put("shopID", shopID);
		requestParams.put("deviceType", "Android");
		gGetUrl(url, requestParams, handler);
	}


	/**
	 * 获取支付列表
	 */
	public void paymentList(AsyncHttpResponseHandler handler) {
		String url = completeUrl("/Payment");
		gGetUrl(url, handler);
	}


	/**
	 * TODO 获取促销信息列表
	 */
	public void salesPListDatas(String count,String page,String shopID, AsyncHttpResponseHandler handler) {
		String url = completeUrl("/Promotion/promotionList2");
		RequestParams requestParams = new RequestParams();
		requestParams.put("shopID", shopID);
		requestParams.put("count", count);
		requestParams.put("page", page);
		gGetUrl(url, requestParams, handler);
	}


	/**
	 * TODO 获取促销详情
	 */
	public void salesPItemDetail(String promotionID, AsyncHttpResponseHandler handler) {
		String url = completeUrl("/Promotion");
		RequestParams requestParams = new RequestParams();
		requestParams.put("promotionID", promotionID);
		gGetUrl(url, requestParams, handler);
	}


	/**
	 * TODO 获取新闻资讯列表
	 */
	public void newsInfoList(String shopID, AsyncHttpResponseHandler handler) {
		String url = completeUrl("/News/newsList");
		RequestParams requestParams = new RequestParams();
		requestParams.put("shopID", shopID);
		gGetUrl(url, requestParams, handler);
	}


	/**
	 * TODO 获取新闻资讯详情
	 */
	public void newsDetail(String newsID, AsyncHttpResponseHandler handler) {
		String url = completeUrl("/News/news");
		RequestParams requestParams = new RequestParams();
		requestParams.put("newsID", newsID);
		gGetUrl(url, requestParams, handler);
	}


	/**
	 * TODO 生成订单
	 */
	public void createOrder(String siteID, String siteName, String promotionID, String shopID, String fullname, String mobile,
	                        AsyncHttpResponseHandler handler) {

		String url = completeUrl("/Order");
		RequestParams requestParams = new RequestParams();
		requestParams.put("promotionID", promotionID);
		requestParams.put("shopID", shopID);
		requestParams.put("fullname", fullname);
		requestParams.put("mobile", mobile);
		requestParams.put("siteID", siteID);
		requestParams.put("siteName", siteName);
		gPostUrl(url, requestParams, handler);
	}


	/**
	 * TODO 付款
	 */
	public void shopToPay(String orderID, String paymentID, AsyncHttpResponseHandler handler) {
		String url = completeUrl("/Payment");
		RequestParams requestParams = new RequestParams();
		requestParams.put("orderID", orderID);
		requestParams.put("paymentID", paymentID);
		gPostUrl(url, requestParams, handler);
	}


	/**
	 * @author yuanbingyong
	 * <p/>
	 * 获取第三方店铺订单
	 */
	public void getThirdOrders(int page, int pageSize, AsyncHttpResponseHandler handler) {
//        String url = "http://10.0.12.223:8089" + "/Shop/" + API_VERSION + "/Order/orderList";
		String url = completeUrl("/Order/orderList");
		RequestParams requestParams = new RequestParams();
		requestParams.put("count", pageSize);
		requestParams.put("page", page);
		gGetUrl(url, requestParams, handler);
	}


	/**
	 * 获取订单详情
	 *
	 * @param orderID
	 */
	public void getThirdOrderDetail(String orderID, AsyncHttpResponseHandler handler) {
		String url = completeUrl("/Order");
		RequestParams requestParams = new RequestParams();
		requestParams.put("orderID", orderID);
		gGetUrl(url, requestParams, handler);
	}

	/**
	 * 取消订单
	 *
	 * @param orderID
	 */
	public void cancelOrder(String orderID, String reason, AsyncHttpResponseHandler handler) {
		String url = completeUrl("/Order/pormotionCancel");
		RequestParams requestParams = new RequestParams();
		requestParams.put("orderID", orderID);
		requestParams.put("reason", reason);
		gPostUrl(url, requestParams, handler);
	}

	/**
	 * 申请退款
	 *
	 * @param orderID
	 */
	public void refundOrder(String orderID, String reason, AsyncHttpResponseHandler handler) {
		String url = completeUrl("/Order/pormotionRefundApply");
		RequestParams requestParams = new RequestParams();
		requestParams.put("orderID", orderID);
		requestParams.put("reason", reason);
		gPostUrl(url, requestParams, handler);
	}


	/**
	 * 获取取消订单理由列表
	 *
	 * @param token
	 */
	public void getThirdOrderCancelReason(String token, AsyncHttpResponseHandler handler) {
		String url = completeUrl("");//TODO
		RequestParams requestParams = new RequestParams();
		requestParams.put("token", token);
		gGetUrl(url, requestParams, handler);
	}


	/**
	 * 取消订单
	 *
	 * @param orderID
	 */
	public void cancelThirdOrder(String orderID, AsyncHttpResponseHandler handler) {
		String url = completeUrl("/Shop/cancelOrder");
		RequestParams requestParams = new RequestParams();
		requestParams.put("orderID", orderID);
		gPutUrl(url, requestParams, handler);
	}


	/**
	 * 提交 询问底价或试乘试驾信息
	 *
	 * @param shopID      店铺ID
	 * @param itemID      车辆ID值
	 * @param city        城市
	 * @param province    省份
	 * @param mobile      手机号
	 * @param fullname    姓名
	 * @param gender      性别  10为男，20位女
	 * @param messageType 消息类型    10为询问底价，20为试乘试驾
	 * @param exchange    是否置换    0为不置换，1为置换
	 * @param handler
	 */
	public void submitMessage(String shopID, String itemID, String city, String cityID, String province, String provinceID, String mobile, String fullname, String gender, String messageType, String exchange, AsyncHttpResponseHandler handler) {
		String url = completeUrl("/Message");
		RequestParams requestParams = new RequestParams();
		requestParams.put("shopID", shopID);
		requestParams.put("itemID", itemID);
		requestParams.put("city", city);
		requestParams.put("province", province);
		requestParams.put("mobile", mobile);
		requestParams.put("cityID", cityID);
		requestParams.put("provinceID", provinceID);
		requestParams.put("fullname", fullname);
		requestParams.put("gender", gender);
		requestParams.put("messageType", messageType);
		requestParams.put("exchange", exchange);
		gPostUrl(url, requestParams, handler);
	}


	/**
	 * 询问底价
	 *
	 * @param shopID      店铺ID
	 * @param itemName    意向车型
	 * @param city        城市
	 * @param province    省份
	 * @param mobile      手机号
	 * @param fullname    姓名
	 * @param gender      性别  10为男，20位女
	 * @param messageType 消息类型    10为询问底价，20为试乘试驾
	 * @param exchange    是否置换    0为不置换，1为置换
	 * @param handler
	 */
	public void askLeastPrice(String shopID, String itemName, String city, String province, String mobile, String fullname, String gender, String messageType, String exchange, AsyncHttpResponseHandler handler) {
		String url = completeUrl("/Shop/leaveMessage");
		RequestParams requestParams = new RequestParams();
		requestParams.put("shopID", shopID);
		requestParams.put("itemName", itemName);
		requestParams.put("city", city);
		requestParams.put("province", province);
		requestParams.put("mobile", mobile);
		requestParams.put("fullname", fullname);
		requestParams.put("gender", gender);
		requestParams.put("messageType", messageType);
		requestParams.put("exchange", exchange);
		gPostUrl(url, requestParams, handler);
	}


	/**
	 * 获取第三方店铺活动车辆详情
	 */
	public void getThirdFavorableCarDetail(String shopID, String itemID, AsyncHttpResponseHandler handler) {
		String url = completeUrl("/Auto/itemDetail");
		RequestParams requestParams = new RequestParams();
		requestParams.put("shopID", shopID);
		requestParams.put("itemID", itemID);
		gGetUrl(url, requestParams, handler);
	}


	/**
	 * 图集
	 */
	public void thirdCarItemAtlas(String itemID, AsyncHttpResponseHandler handler) {
		String url = completeUrl("/Auto/pictures");
		RequestParams params = new RequestParams();
		params.put("itemID", itemID);
		gGetUrl(url, params, handler);
	}


	public void thirdCarItemAtlas(String value, String extID, AsyncHttpResponseHandler handler) {
		String url = completeUrl("/Auto/pictures");
		RequestParams params = new RequestParams();
		params.put(value, extID);
		gGetUrl(url, params, handler);
	}


	/**
	 * 获取筛选数据条数
	 *
	 * @param hashMap 查询条件
	 * @param handler
	 */
	public void getFilterCarCount(Map<String, String> hashMap, AsyncHttpResponseHandler handler) {
		String url = completeUrl("/Filter/filterExamine");
		RequestParams requestParams = new RequestParams(hashMap);
		gGetUrl(url, requestParams, handler);
	}


	/**
	 * liaoguilong
	 * 获取车型
	 */
	public void getCarTypeDatas(String shopID, String seriesID, AsyncHttpResponseHandler handler) {
		String url = completeUrl("/Auto/items");
		RequestParams requestParams = new RequestParams();
		requestParams.put("shopID", shopID);
		requestParams.put("seriesID", seriesID);
		requestParams.put("deviceType", "Android");
		gGetUrl(url, requestParams, handler);
	}


	/**
	 * liaoguilong
	 * 获取车系列表，（店铺详情，车型报价列表）
	 */
	public void getModelsQuote(String shopID, AsyncHttpResponseHandler handler) {
		String url = completeUrl("/Auto/series");
		RequestParams requestParams = new RequestParams();
		requestParams.put("shopID", shopID);
		requestParams.put("deviceType", "Android");
		gGetUrl(url, requestParams, handler);
	}


	/**
	 * 搜索商店
	 *
	 * @param page  分页页数，不传时默认为1
	 * @param count 分页每页条目数，不传时默认为15
	 * @param sort  排序 |综合 1|距离升序 2| 不传默认1
	 */
	public void searchShop(int page, int count, int sort, HashMap<String, String> searchConditionMap, AsyncHttpResponseHandler handler) {
		DebugLog.d(TAG, "searchCar() called with: " + "page = [" + page + "], count = [" + count + "], sort = [" + sort + "], searchCondition = [" + searchConditionMap);
		String url = completeUrl("/Search/shop");
		RequestParams requestParams = new RequestParams(searchConditionMap);
//        requestParams.put("type", "shop");  // type 搜索类型 shop Yes
		requestParams.put("page", page);
		requestParams.put("count", count);
		requestParams.put("sort", sort);
		gGetUrl(url, requestParams, handler);
	}


	/**
	 * 搜索车型
	 *
	 * @param page  分页页数，不传时默认为1
	 * @param count 分页每页条目数，不传时默认为15
	 * @param sort  价格降序 1|价格升序 2|降幅降序 3, 默认值1
	 */
	public void searchCar(int page, int count, int sort, HashMap<String, String> searchCondition, AsyncHttpResponseHandler handler) {
		DebugLog.d(TAG, "searchCar() called with: " + "page = [" + page + "], count = [" + count + "], sort = [" + sort + "], searchCondition = [" + searchCondition);
		String url = completeUrl("/Search/auto");
		RequestParams requestParams = new RequestParams(searchCondition);
//        requestParams.put("type", "car"); // *              type        筛选类型 车car Yes
		requestParams.put("page", page);
		requestParams.put("count", count);
		requestParams.put("sort", sort);
		gGetUrl(url, requestParams, handler);
	}


	protected String getInfoUrl(String control) {
		return ApiUtil.getAutoInfoURL(control);
	}

	/**
	 * 获取筛选品牌
	 *
	 * @param handler
	 */
	public void requestFilterBrand(AsyncHttpResponseHandler handler) {
		String url = getInfoUrl("/Filter/brand");
		RequestParams requestParams = new RequestParams();
		gGetUrl(url, requestParams, handler);
	}

	/**
	 * 获取筛选车系
	 *
	 * @param handler
	 */
	public void requestFilterSeries(String brandName, AsyncHttpResponseHandler handler) {
		String url = getInfoUrl("/Filter/series");
		RequestParams requestParams = new RequestParams();
		requestParams.put("brand", brandName);
		gGetUrl(url, requestParams, handler);
	}


	/**
	 * 获取筛选车型
	 *
	 * @param handler
	 */
	public void requestFilterModel(String brandName, String serieName, AsyncHttpResponseHandler handler) {
		String url = getInfoUrl("/Filter/model");
		RequestParams requestParams = new RequestParams();
		requestParams.put("brand", brandName);
		requestParams.put("serie", serieName);
		gGetUrl(url, requestParams, handler);
	}


	/**
	 * 获取筛选地区
	 *
	 * @param handler
	 */
	public void requestFilterArea(AsyncHttpResponseHandler handler) {
		String url = completeUrl("/Area");
		gGetUrl(url, handler);
	}


	/**
	 * 请求特价车筛选品牌
	 **/
	public void requestSpecialBrand(String siteID, AsyncHttpResponseHandler handler) {
		RequestParams requestParams = new RequestParams();
		requestParams.put("siteID", siteID);
		gGetUrl(completeUrl("/Filter/filterAutoBrand"), requestParams, handler);
	}


	/**
	 * 　请求特价车筛选车系
	 **/
	public void requestSpecialSeries(String brand, String siteID, AsyncHttpResponseHandler handler) {
		RequestParams requestParams = new RequestParams();
		requestParams.put("brand", brand);
		requestParams.put("siteID", siteID);
		gGetUrl(completeUrl("/Filter/filterAutoSeries"), requestParams, handler);
	}


	/**
	 * 请求特价车筛选车型
	 **/
	public void requestSpecialModel(String brand, String serie, String siteID, AsyncHttpResponseHandler handler) {
		RequestParams requestParams = new RequestParams();
		requestParams.put("brand", brand);
		requestParams.put("serie", serie);
		requestParams.put("siteID", siteID);
		gGetUrl(completeUrl("/Filter/filterAutoModel"), requestParams, handler);
	}


	/**
	 * 获取分站数据
	 **/
	public void requestSiteData(AsyncHttpResponseHandler handler) {
		gGetUrl(completeUrl("/Site"), handler);
	}


	/**
	 * 请求特价车店铺列表
	 **/
	public void requestSpecialFilterShop(int page, int count, HashMap<String, String> hashMap, AsyncHttpResponseHandler handler) {
		RequestParams requestParams = new RequestParams(hashMap);
		requestParams.put("page", page);
		requestParams.put("count", count);
		gGetUrl(completeUrl("/Site/searchShopList"), requestParams, handler);
	}


	/**
	 * 请求特价车车辆列表
	 **/
	public void requestSpecialFilterCar(int page, int count, HashMap<String, String> hashMap, AsyncHttpResponseHandler handler) {
		RequestParams requestParams = new RequestParams(hashMap);
		requestParams.put("page", page);
		requestParams.put("count", count);
		gGetUrl(completeUrl("/Site/searchShopModelList"), requestParams, handler);
	}


	/**
	 * 获取筛选地
	 *
	 * @param handler
	 */
//    public void requestFilterArea(AsyncHttpResponseHandler handler) {
//        String url = "http://10.0.15.203:8089/Shop/V2/Area";
//        gGetUrl(url, handler);
//    }


	/**
	 * 筛选条数
	 */
	public void requestFilterExamine(Map<String, Object> filterMap, AsyncHttpResponseHandler handler) {
		String url = completeUrl("/Shop/filterExamine");
		RequestParams requestParams = new RequestParams(filterMap);
		gGetUrl(url, requestParams, handler);
	}


	/**
	 * 车型列表
	 */
	public void searchModel(String seriesName, int page, int pageItemCount, AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("page", page);
		params.put("seriesName", seriesName);
		params.put("count", pageItemCount);
		gGetUrl(completeUrl("/Search/model"), params, handler);
	}
	/**
	 * 分割线
	 * ------------------------------------------------------------------------------------------------------------------------------------------------
	 * 下面是v2.0
	 */


	/**
	 * 提交信息
	 */
	public void submitMessage(String shopID, String itemID, String mobile, String fullname, String gender, AsyncHttpResponseHandler handler) {
		String url = completeUrl("/Message");
		RequestParams requestParams = new RequestParams();
		requestParams.put("shopID", shopID);
		requestParams.put("itemID", itemID);
		requestParams.put("mobile", mobile);
		requestParams.put("fullname", fullname);
		requestParams.put("gender", gender);
		requestParams.put("messageType", 30);
		gPostUrl(url, requestParams, handler);
	}

	/**
	 * 付款方式
	 */
	public void getPaymentList(int orderType, AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("orderType", orderType);
		gGetUrl(completeUrl("/Payment"), params, handler);
	}

	/**
	 * 分站选择
	 */
	public void getArea(AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		gGetUrl(completeUrl("/Area"), params, handler);
	}

	/**
	 * 首页广告位
	 */
	public void getBanner(String siteID, AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("siteID", siteID);
		gGetUrl(completeUrl("/Site/indexBanner"), params, handler);
	}

	/**
	 * 找车条件列表，除品牌和车系除外的筛选条件
	 */
	public void filterAutoArg(String areaID, AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("siteID", areaID);
		gGetUrl(completeUrl("/Filter/filterAutoArg"), params, handler);
	}

	/**
	 * 找车条件列表，除品牌和车系除外的筛选条件
	 */
	public void filterArgumentSeckill(AsyncHttpResponseHandler handler) {
		gGetUrl(completeUrl("/Filter/filterArgumentSeckill"), new RequestParams(), handler);
	}

	/**
	 * 筛选条件 品牌列表
	 */
	public void filterAutoBrand(String areaID, AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("siteID", areaID);
		gGetUrl(completeUrl("/Filter/filterAutoBrandSeckill"), params, handler);
	}

	/**
	 * 筛选条件 车系列表
	 */
	public void filterAutoSeries(String brand, String areaID, AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("brand", brand);
		params.put("siteID", areaID);
		gGetUrl(completeUrl("/Filter/filterAutoSeriesSeckill"), params, handler);
	}

	/**
	 * 筛选条件 车型列表
	 */
	public void filterAutoSeries(String brand, String serie, String areaID, AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("brand", brand);
		params.put("serie", serie);
		params.put("siteID", areaID);
		gGetUrl(completeUrl("/Filter/filterAutoModelSeckill"), params, handler);
	}

	/**
	 * 店铺列表
	 */
	public void getShopList(int page, String areaID, String brand, String Serie, String latitude, String longitude, String keyword, AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("page", page);
		params.put("siteID", areaID);
		if (!TextUtils.isEmpty(brand)) {
			params.put("brand", brand);
		}
		if (!TextUtils.isEmpty(Serie)) {
			params.put("Serie", Serie);
		}
		if (!TextUtils.isEmpty(latitude)) {
			params.put("latitude", latitude);
		}
		if (!TextUtils.isEmpty(longitude)) {
			params.put("longitude", longitude);
		}
		if (!TextUtils.isEmpty(keyword)) {
			params.put("keyword", keyword);
		}
		gGetUrl(completeUrl("/Site/shopList"), params, handler);
	}

	/**
	 * 首页店铺列表
	 */
	public void getIndexShopList(int count, String siteID, AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("count", count);
		params.put("siteID", siteID);
		gGetUrl(completeUrl("/Site/indexShopList"), params, handler);
	}

	/**
	 * 新车销售列表
	 */
	public void getNewAutoList(int page, String areaID, String brand, String Serie, String filterArgKey, String filterArgValue, AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("page", page);
		params.put("siteID", areaID);
		if (!TextUtils.isEmpty(brand)) {
			params.put("brand", brand);
		}
		if (!TextUtils.isEmpty(Serie)) {
			params.put("Serie", Serie);
		}
		if (!TextUtils.isEmpty(filterArgKey)) {
			params.put("filterArgKey", filterArgKey);
		}
		if (!TextUtils.isEmpty(filterArgValue)) {
			params.put("filterArgValue", filterArgValue);
		}
		gGetUrl(completeUrl("/Site/newAutoList"), params, handler);
	}

	/**
	 * 首页新车销售列表
	 */
	public void getIndexSeriesList(String siteID, AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("page", 1);
		params.put("siteID", siteID);
		params.put("count", 3);
		gGetUrl(completeUrl("/Site/indexSeriesList"), params, handler);
	}

	/**
	 * 首页新车销售列表
	 */
	public void getIndexSeriesList(String siteID, int page, int count, AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("page", page);
		params.put("siteID", siteID);
		params.put("count", count);
		gGetUrl(completeUrl("/Site/indexSeriesList"), params, handler);
	}

	public void getIndexInfoList(String cityGroupID, AsyncHttpResponseHandler handler) {

	}

	/**
	 * 车辆筛选/搜索
	 */
	public void searchAutoList(int page, String areaID, String brand, String Serie, String areaInitial, String model, AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("page", page);
		params.put("siteID", areaID);
		if (!TextUtils.isEmpty(brand)) {
			params.put("brand", brand);
		}
		if (!TextUtils.isEmpty(Serie)) {
			params.put("Serie", Serie);
		}
		if (!TextUtils.isEmpty(areaInitial)) {
			params.put("filterArgKey", areaInitial);
		}
		if (!TextUtils.isEmpty(model)) {
			params.put("filterArgValue", model);
		}
		gGetUrl(completeUrl("/Site/searchAutoList"), params, handler);
	}

	/**
	 * 新车销售列表
	 */
	public void getNewAutoList(int page, String areaID, ArrayList<Filter> filters, AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("page", page);
		params.put("siteID", areaID);
//        if (!TextUtils.isEmpty(brand)) {
//            params.put("brand", brand);
//        }
//        if (!TextUtils.isEmpty(Serie)) {
//            params.put("Serie", Serie);
//        }
		if (filters != null) {
			for (int i = 0; i < filters.size(); i++) {
				params.put(filters.get(i).filterKey, filters.get(i).filterValue);
			}
		}
		gGetUrl(completeUrl("/NewAutoList"), params, handler);
	}


	/**
	 * 特买车列表
	 */
	public void getSeckill(int page, String areaID, String brand, String Serie, String filterArgKey, String filterArgValue, AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("page", page);
		params.put("count", 15);
		params.put("siteID", areaID);
		if (!TextUtils.isEmpty(brand)) {
			params.put("brand", brand);
		}
		if (!TextUtils.isEmpty(Serie)) {
			params.put("Serie", Serie);
		}
		if (!TextUtils.isEmpty(filterArgKey)) {
			params.put("filterArgKey", filterArgKey);
		}
		if (!TextUtils.isEmpty(filterArgValue)) {
			params.put("filterArgValue", filterArgValue);
		}

		gGetUrl(completeUrl("/Site/seckillList"), params, handler);
	}

	/**
	 * 首页特买车列表
	 */
	public void getIndexSeckillList(String siteID, AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("page", 1);
		params.put("count", 3);
		params.put("siteID", siteID);
		gGetUrl(completeUrl("/Site/indexSeckillList"), params, handler);
	}

	/**
	 * 首页特买车列表
	 */
	public void getIndexSeckillList(String siteID, int page, int count, AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("page", page);
		params.put("count", count);
		params.put("siteID", siteID);
		gGetUrl(completeUrl("/Site/indexSeckillList"), params, handler);
	}

	/**
	 * 特买车列表
	 */
	public void getSeckill(String areaID, int count, int page, String brand,
	                       String serie, String model, AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("siteID", areaID);
		params.put("count", count);
		params.put("page", page);
		if (!TextUtils.isEmpty(brand))
			params.put("brand", brand);
		if (!TextUtils.isEmpty(serie))
			params.put("serie", serie);
		if (!TextUtils.isEmpty(model))
			params.put("model", model);
		gGetUrl(completeUrl("/Site/searchSeckillList"), params, handler);
	}

	/**
	 * 特买车列表
	 */
	public void getSeckill(String areaID, int count, int page, HashMap<String, String> filterMap, AsyncHttpResponseHandler handler) {
		String serie = filterMap.get(FilterResultKey.SERIES_KEY);
		if (TextUtils.isEmpty(serie))
			filterMap.remove(FilterResultKey.SERIES_KEY);
		RequestParams params = new RequestParams(filterMap);
		params.put("siteID", areaID);
		params.put("count", count);
		params.put("page", page);
		gGetUrl(completeUrl("/Site/searchSeckillList"), params, handler);
	}

	/**
	 * 特买车列表
	 */
	public void getSeckill(String areaID, int count, int page, AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("siteID", areaID);
		params.put("count", count);
		params.put("page", page);
		gGetUrl(completeUrl("/Site/searchSeckillList"), params, handler);
	}

	/**
	 * 特买车列表
	 */
	public void getSeckill(int page, String areaID, ArrayList<Filter> filters, AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("page", page);
		params.put("count", 15);
		params.put("siteID", areaID);
//        if (!TextUtils.isEmpty(brand)) {
//            params.put("brand", brand);
//        }
//        if (!TextUtils.isEmpty(Serie)) {
//            params.put("Serie", Serie);
//        }
//        if (!TextUtils.isEmpty(filterArgKey)) {
//            params.put("filterArgKey", filterArgKey);
//        }
//        if (!TextUtils.isEmpty(filterArgValue)) {
//            params.put("filterArgValue", filterArgValue);
//        }
		if (filters != null) {
			for (int i = 0; i < filters.size(); i++) {
				params.put(filters.get(i).filterKey, filters.get(i).filterValue);
			}
		}
		gGetUrl(completeUrl("/Seckill/autoList"), params, handler);
	}

	/**
	 * 特价车辆详情
	 */
	public void getSeckillDetail(String itemID, AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("itemID", itemID);
		gGetUrl(completeUrl("/Special/itemDetail"), params, handler);
	}

	/**
	 * 分站新闻列表
	 */
	public void getSiteNews(String siteID, int page, AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("siteID", siteID);
		params.put("count", 15);
		params.put("page", page);
		gGetUrl(completeUrl("/Site/indexNewsList"), params, handler);
	}

	/**
	 * 首页新闻
	 */
	public void getIndexNews(String siteID, AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("siteID", siteID);
		params.put("count", 3);
		params.put("page", 1);
		gGetUrl(completeUrl("/Site/indexNewsList"), params, handler);
	}

	/**
	 * 店铺特买车列表
	 */
	public void getShopSeckill(String shopID, AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("shopID", shopID);
		gGetUrl(completeUrl("/Special/shopIndex"), params, handler);
	}

	/**
	 * 购买特价车 提交订单
	 */
	public void submitSeckill(String itemID, String method, String insurance, String decorate, String fullname, String mobile, AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("itemID", itemID);
		params.put("method", method);
		params.put("insurance", insurance);
		params.put("decorate", decorate);
		params.put("fullname", fullname);
		params.put("mobile", mobile);
		gPostUrl(completeUrl("/Order/seckillSubmit"), params, handler);
	}

	/**
	 * 特价车订单详情
	 */
	public void getSeckillOrderDetail(String orderID, AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("orderID", orderID);
		gGetUrl(completeUrl("/Order/seckillDetail"), params, handler);
	}

	/**
	 * 特价车订单取消
	 */
	public void cancelSeckillOrder(String orderID, String reason, AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("orderID", orderID);
		params.put("reason", reason);
		gDeleteUrl(completeUrl("/Order/seckillCancel"), params, handler);
	}

	/**
	 * 特价车订单申请退款
	 */
	public void refundSeckillOrder(String orderID, String reason, AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("orderID", orderID);
		params.put("reason", reason);
		gPostUrl(completeUrl("/Order/seckillRefundApply"), params, handler);
	}

	/**
	 * 新车销售 车系列表
	 */
	public void getNewCarSeries(String areaID, int page, AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("siteID", areaID);
		params.put("page", page);
		params.put("count", 15);
		gGetUrl(completeUrl("/Site/searchSeriesList"), params, handler);
	}

	/**
	 * 新车销售 车型列表
	 */
	public void searchModelList(String siteID, int page, int count, String brand, String serie, AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("siteID", siteID);
		params.put("page", page);
		params.put("count", count);
		if (!TextUtils.isEmpty(brand)) {
			params.put("brand", brand);
		}
		if (!TextUtils.isEmpty(serie)) {
			params.put("serie", serie);
		}
		gGetUrl(completeUrl("/Site/searchModelList"), params, handler);
	}

	/**
	 * 新车销售 车型列表
	 *
	 * @param siteID
	 * @param page
	 * @param handler
	 */
	public void searchModelList(String siteID, int page, AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("page", page);
		params.put("siteID", siteID);
		params.put("count", 15);
		gGetUrl(completeUrl("/Site/searchModelList"), params, handler);
	}

	/**
	 * 根据车辆获取店铺
	 *
	 * @param page
	 * @param count
	 * @param brand
	 * @param serie
	 * @param model
	 * @param siteID
	 * @param handler
	 */
	public void searchShopModelList(int page, int count, String brand, String serie, String model, String siteID
			, AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("page", page);
		params.put("siteID", siteID);
		params.put("count", count);
		if (!TextUtils.isEmpty(brand))
			params.put("brand", brand);
		if (!TextUtils.isEmpty(serie))
			params.put("serie", serie);
		if (!TextUtils.isEmpty(model))
			params.put("model", model);
		gGetUrl(completeUrl("/Site/searchShopModelList"), params, handler);
	}

	/**
	 * 店铺咨询列表
	 *
	 * @param page
	 * @param count
	 * @param shopID
	 * @param handler
	 */
	public void getConsultList(int page, int count, String shopID, AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("page", page);
		params.put("count", count);
		params.put("shopID", shopID);
		gGetUrl(completeUrl("/Shop/consultList"), params, handler);
	}


	/**
	 * 咨询信息提交
	 **/
	public void submitAsk(String shopID, String itemID, String askContent, String autoType, AsyncHttpResponseHandler handler) {
		String url = completeUrl("/Auto/consultSubmit");
		RequestParams params = new RequestParams();
		params.put("shopID", shopID);
		params.put("itemID", itemID);
		params.put("askContent", askContent);
		params.put("autoType", autoType);
		gPostUrl(url, params, handler);
	}


	/**
	 * 特卖车订单确认
	 **/
	public void confirmSpecialCarOrder(HashMap<String, String> hashMap, String fullname, String mobile, String siteID, String siteName, AsyncHttpResponseHandler handler) {
		String url = completeUrl("/Order/seckillSubmit");
		DebugLog.e(TAG, hashMap.toString());
		RequestParams params = new RequestParams(hashMap);
//        params.put("itemID", itemID);
//        params.put("method", method);
//        params.put("insurance", insurance);
//        params.put("decorate", decorate);
		params.put("fullname", fullname);
		params.put("mobile", mobile);
		params.put("siteID", siteID);
		params.put("siteName", siteName);
		gPostUrl(url, params, handler);
	}


	/**
	 * 车系介绍（新车）
	 *
	 * @param siteID
	 * @param series
	 * @param handler
	 */
	public void seriesIntroduce(String siteID, String brand, String series, AsyncHttpResponseHandler handler) {
		String url = completeUrl("/Introduce/series");
		RequestParams params = new RequestParams();
		params.put("siteID", siteID);
		params.put("brand", brand);
		params.put("series", series);
		gGetUrl(url, params, handler);
	}


	/**
	 * 车型介绍（新车）
	 *
	 * @param siteID
	 * @param model
	 * @param handler
	 */
	public void modelIntroduce(String siteID, String extID, String brand, String model, AsyncHttpResponseHandler handler) {
		String url = completeUrl("/Introduce/model");
		RequestParams params = new RequestParams();
		DebugLog.i("Tag", "extID " + extID);
		params.put("siteID", siteID);
		params.put("extID", extID);
		params.put("model", model);
		params.put("brand", brand);
		gGetUrl(url, params, handler);
	}

	/**
	 * 店铺车型报价（新车）
	 *
	 * @param siteID
	 * @param model
	 * @param handler
	 */
	public void ShopModelPrice(String siteID, String shopSiteFrom, String extID, String brand, String series, String model, AsyncHttpResponseHandler handler) {
		String url = completeUrl("/ShopModelPrice");
		RequestParams params = new RequestParams();
		params.put("siteID", siteID);
		params.put("shopSiteFrom", shopSiteFrom);
		params.put("extID", extID);
		params.put("model", model);
		params.put("series", series);
		params.put("brand", brand);
		gGetUrl(url, params, handler);
	}


}
