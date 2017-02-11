package com.hxqc.mall.thirdshop.activity;

import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hxqc.mall.activity.NoBackActivity;
import com.hxqc.mall.core.api.LoadingAnimResponseHandler;
import com.hxqc.mall.core.api.RequestFailView;
import com.hxqc.mall.core.util.ActivitySwitchBase;
import com.hxqc.mall.core.util.ImageUtil;
import com.hxqc.mall.core.util.OtherUtil;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.api.InfoApiClient;
import com.hxqc.mall.thirdshop.api.ThirdPartShopClient;
import com.hxqc.mall.thirdshop.model.ColorInfo;
import com.hxqc.mall.thirdshop.model.ShopInfo;
import com.hxqc.mall.thirdshop.model.newcar.AutoInformation;
import com.hxqc.mall.thirdshop.model.newcar.ModelListInfo;
import com.hxqc.mall.thirdshop.model.newcar.ParameterInfo;
import com.hxqc.mall.thirdshop.model.newcar.SeriesInfo;
import com.hxqc.mall.thirdshop.model.newcar.SeriesIntroduce;
import com.hxqc.mall.thirdshop.utils.ActivitySwitcherThirdPartShop;
import com.hxqc.mall.thirdshop.views.AppearanceToolbar;
import com.hxqc.mall.thirdshop.views.UserGradeView;
import com.hxqc.mall.thirdshop.views.adpter.ColorDeatailItemAdapter;
import com.hxqc.mall.thirdshop.views.adpter.NewCarModelListAdapter;
import com.hxqc.mall.thirdshop.views.adpter.NewCarShopListAdapter;
import com.hxqc.util.DebugLog;
import com.hxqc.util.JSONUtils;
import com.hxqc.widget.PinnedHeaderExpandableListView;

import cz.msebera.android.httpclient.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 新车 车型列表
 * Created by 赵帆
 * Date:2016-06-20
 */
public class NewCarModelListActivity2 extends NoBackActivity {
    public static final String SERIES = "SERIES";
    public static final String AREAID = "AREAID";
    public static final String BRAND = "BRAND";
    private View series_view; //车系详情
    private View shop_view;  //店铺列表
    private UserGradeView mUserGradeView;  //口碑评价
    private ThirdPartShopClient apiClient;
    private GridView color_gv;  //车身颜色gv
    private GridView userGradeGv;  //口碑评分
    private ListView userDiscLv;
    private ListView articleLv;
    private ColorDeatailItemAdapter colorDeatailItemAdapter;
    private PinnedHeaderExpandableListView model_lv; //车型listview
    private ListView shop_lv;  //店铺listview
    private NewCarModelListAdapter modelListAdapter; //车型adapter
    private SeriesInfo seriesInfo;  //车系信息
    private List<ModelListInfo> modelList;//车型列表
    private List<ShopInfo> shopList; //店铺列表
    private ImageView series_img; //车系图片
    private AppearanceToolbar toolbar;
    RequestFailView mRequestFailView;
    private TextView guidePriceTv; //指导价
    private TextView mSeriesNameTv; // 车系名
    private TextView modelToal;
    private String siteId, seriesName, brand;
    private TextView mShopType;
    private String shopSiteFrom;  //店铺来源 10本地站点 20 周边站点 number
    private LinearLayout mArticleLay;
    List<AutoInformation> mAutoInformations;
    private TextView siteName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newcarmodellist);
        series_view = View.inflate(this, R.layout.view_newcarmodelist_header, null);
        shop_view = View.inflate(this, R.layout.view_newcarmodellist_shop, null);
        //     mUserGradeView = View.inflate(this, R.layout.view_serieslist_usergrade, null);
        mUserGradeView = new UserGradeView(this);
        bindView();
        initResource();
        getData();
    }


    private void bindView() {
        toolbar = (AppearanceToolbar) findViewById(R.id.toolbar);
        mRequestFailView = (RequestFailView) findViewById(R.id.refresh_fail_view);
        model_lv = (PinnedHeaderExpandableListView) findViewById(R.id.model_lv);
        series_img = (ImageView) series_view.findViewById(R.id.series_img);
        color_gv = (GridView) series_view.findViewById(R.id.color_gridView);
        mSeriesNameTv = (TextView) series_view.findViewById(R.id.title);
        guidePriceTv = (TextView) series_view.findViewById(R.id.guide_price);
        modelToal = (TextView) series_view.findViewById(R.id.model_total);
        shop_lv = (ListView) shop_view.findViewById(R.id.shop_lv);
        mShopType = (TextView) shop_view.findViewById(R.id.shop_type);
        siteName = (TextView) shop_view.findViewById(R.id.site_name);
        //口碑
        userGradeGv = (GridView) mUserGradeView.findViewById(R.id.user_grade_gv);
        userDiscLv = (ListView) mUserGradeView.findViewById(R.id.user_grade_lv);
        //文章推荐
        articleLv = (ListView) series_view.findViewById(R.id.article_lv);
        mArticleLay = (LinearLayout) series_view.findViewById(R.id.article_lay);
    }

    private void initResource() {
        siteId = getIntent().getStringExtra(AREAID);
        seriesName = getIntent().getStringExtra(SERIES);
        brand = getIntent().getStringExtra(BRAND);
        apiClient = new ThirdPartShopClient();
        model_lv.setGroupIndicator(null);
        model_lv.addHeaderView(series_view);
        model_lv.addFooterView(shop_view);
        model_lv.addFooterView(mUserGradeView);
        toolbar.setOnHomeClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivitySwitchBase.toMain(NewCarModelListActivity2.this, 0);
            }
        });
    }

    private void getData() {
        apiClient.seriesIntroduce(siteId, brand, seriesName, new LoadingAnimResponseHandler(this, true, false) {
            @Override
            public void onSuccess(String response) {
                //    forTest1(response);
                SeriesIntroduce mList
                        = JSONUtils.fromJson(response, SeriesIntroduce.class);
                if (mList == null || mList.seriesInfo == null) {
                    empty();
                    return;
                }
                seriesInfo = mList.seriesInfo;
                modelList = mList.modelList;
                shopList = mList.shopList;
                shopSiteFrom = mList.shopSiteFrom;
                showSucView();
                setSeriesDetail(seriesInfo);
                setModelList(modelList);
                setShopList(shopList);

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                requestFailView();
            }
        });
        //获取用户评价
        getUserGradeData();
        getAriticleData();
    }

    /**
     * 文章推荐
     */
    private void getAriticleData() {
        final int max = 3;
        new InfoApiClient().getAutoNews("", brand, seriesName, new LoadingAnimResponseHandler(this, true, false) {
            @Override
            public void onSuccess(String response) {
                mAutoInformations = new Gson().fromJson(response, new TypeToken<List<AutoInformation>>() {
                }.getType());
                if (mAutoInformations.isEmpty()) {
                    mArticleLay.setVisibility(View.GONE);
                    return;
                }
                List<String> list = new ArrayList<>();
                for (AutoInformation i : mAutoInformations.size() > max ? mAutoInformations.subList(0, max) : mAutoInformations)
                    list.add(i.title);
                ArrayAdapter mAdapter = new ArticleAdapter(getApplicationContext(), R.layout.item_newcar_article, list);
                articleLv.setAdapter(mAdapter);
            }
        });
        articleLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ActivitySwitcherThirdPartShop.toAutoInfoDetail(NewCarModelListActivity2.this, mAutoInformations.get(i).infoID);
            }
        });

    }


    /**
     * 用户评价
     */
    private void getUserGradeData() {
        mUserGradeView.getData("", brand, seriesName, null);
    }

    /**
     * 车型详情
     */
    private void setSeriesDetail(SeriesInfo seriesInfo) {
        initColor(seriesInfo.getAppearance());
        setParameter(seriesInfo.getParameter());
        toolbar.setTitle(seriesInfo.getSeriesName());
        mSeriesNameTv.setText(seriesInfo.getSeriesName());
        guidePriceTv.setText("¥" + OtherUtil.formatPriceSingleOrRange(seriesInfo.getItemOrigPrice()));
        ImageUtil.setImage(this, series_img, seriesInfo.getItemPic());
        mShopType.setText(seriesInfo.getSeriesName() + "经销商");
    }

    /**
     * 车子参数
     *
     * @param parameter
     */
    private void setParameter(List<ParameterInfo> parameter) {
        List<Map<String, Object>> mList = new ArrayList<Map<String, Object>>();
        for (ParameterInfo entity : parameter) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("item", entity.label + ":");
            map.put("item2", entity.value);
            mList.add(map);
        }
        ListView parameter_lv = (ListView) findViewById(R.id.parameter_lv);
        SimpleAdapter adapter = new SimpleAdapter(this, mList,
                R.layout.item_parameter_lv, new String[]{"item", "item2"},
                new int[]{R.id.tv1, R.id.tv2});
        parameter_lv.setAdapter(adapter);

    }


    /**
     * 车型列表
     */
    private void setModelList(List<ModelListInfo> modelList) {
        int modelTotal = 0;
        for (ModelListInfo i : modelList) {
            modelTotal += i.getModel().size();
        }
        modelToal.setText("(" + modelTotal + "款车型)");
        modelListAdapter = new NewCarModelListAdapter(this, modelList);
        modelListAdapter.setParameter(siteId, seriesInfo.getBrand(), seriesInfo.getSeriesName(), shopSiteFrom);
        if (shopList.isEmpty()) {
            modelListAdapter.showShopAskPrice(false);
        }
        model_lv.setAdapter(modelListAdapter);
        OtherUtil.openAllChild(modelListAdapter, model_lv); //展开PinnedHeaderExpandableListView
        model_lv.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return true;
            }
        });
    }

    /**
     * 店铺列表
     *
     * @param shopList
     */
    private void setShopList(List<ShopInfo> shopList) {
        if (shopList.isEmpty())
            return;

        shop_view.findViewById(R.id.shop_lay).setVisibility(View.VISIBLE);
        if (shopSiteFrom.equals("10"))
            siteName.setText(shopList.get(0).siteName + "站");
        else {
            shop_view.findViewById(R.id.site_lay).setVisibility(View.GONE);
            mShopType.setText("为您推荐周边经销商：");
        }
        NewCarShopListAdapter mNewCarShopListAdapter = new NewCarShopListAdapter(this);
        mNewCarShopListAdapter.setData(shopList);
        shop_lv.setAdapter(mNewCarShopListAdapter);

    }


    /**
     * 初始化车身颜色数据
     */
    public void initColor(ArrayList<ColorInfo> colorInfos) {
        ArrayList<ColorInfo> colors = new ArrayList<>();
        for (ColorInfo i : colorInfos) {
            if (!TextUtils.isEmpty(i.color))
                colors.add(i);
        }
        colorDeatailItemAdapter = new ColorDeatailItemAdapter(this, colors);
        color_gv.postDelayed(new Runnable() {
            @Override
            public void run() {
                int num = (int) (color_gv.getMeasuredWidth() / TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                        40, getResources().getDisplayMetrics()));
                color_gv.setNumColumns(num);
                color_gv.setAdapter(colorDeatailItemAdapter);
            }
        }, 50);

    }


    /**
     * 请求成功显示view
     */
    private void showSucView() {
        mRequestFailView.setVisibility(View.GONE);
        model_lv.setVisibility(View.VISIBLE);
        series_img.postDelayed(new Runnable() {
            @Override
            public void run() {
                toolbar.setAlphaAppearance(series_img.getHeight(), model_lv);
            }
        }, 20);
    }


    private void empty() {
        commonError();
        mRequestFailView.setEmptyButtonClick("返回", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mRequestFailView.setRequestViewType(RequestFailView.RequestViewType.empty);
    }

    private void requestFailView() {
        commonError();
        mRequestFailView.setEmptyDescription("获取数据失败");
        mRequestFailView.setFailButtonClick("刷新", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData();
            }
        });
        mRequestFailView.setRequestViewType(RequestFailView.RequestViewType.fail);
    }

    private void commonError() {
        toolbar.showToolbar();
        mRequestFailView.setVisibility(View.VISIBLE);

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    class ArticleAdapter extends ArrayAdapter {
        private int res;
        private List<String> objects;
        private Context context;

        public ArticleAdapter(Context context, int resource, List<String> objects) {
            super(context, resource, objects);
            this.context = context;
            this.res = resource;
            this.objects = objects;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(res, null);
            }
            TextView tv = (TextView) convertView.findViewById(R.id.tv);
            tv.setText(objects.get(position));
            tv.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
            tv.getPaint().setAntiAlias(true);//抗锯齿
            return convertView;
        }
    }

    private void forTest1(String response) {
        try {
            JSONObject jb = new JSONObject(response);
            String a = jb.getJSONObject("seriesInfo").toString();
            String b = jb.getJSONArray("modelList").toString();
            String c = jb.getJSONArray("shopList").toString();
            List<ModelListInfo> list1 = JSONUtils.fromJson(b, new TypeToken<ArrayList<ModelListInfo>>() {
            });
            List<ShopInfo> list2 = JSONUtils.fromJson(c, new TypeToken<ArrayList<ShopInfo>>() {
            });

            DebugLog.e("", a + list1 + list2);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
