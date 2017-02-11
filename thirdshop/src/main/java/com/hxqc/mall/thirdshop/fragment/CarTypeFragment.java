package com.hxqc.mall.thirdshop.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.hxqc.mall.core.api.LoadingAnimResponseHandler;
import com.hxqc.mall.core.api.RequestFailView;
import com.hxqc.mall.core.fragment.FunctionFragment;
import com.hxqc.mall.core.util.ImageUtil;
import com.hxqc.mall.core.util.OtherUtil;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.activity.shop.ModelsOfferActivity;
import com.hxqc.mall.thirdshop.api.ThirdPartShopClient;
import com.hxqc.mall.thirdshop.control.ShopDetailsController;
import com.hxqc.mall.thirdshop.model.AutoBaseInfoThirdShop;
import com.hxqc.mall.thirdshop.model.ModelsQuote;
import com.hxqc.mall.thirdshop.utils.ActivitySwitcherThirdPartShop;
import com.hxqc.mall.thirdshop.views.adpter.CarTypeAdapter;
import com.hxqc.util.JSONUtils;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;


/**
 * 车型列表Fragment
 * liaoguilong
 * date：2015-12-1
 */
public class CarTypeFragment  extends FunctionFragment{

    private  RecyclerView mCarTypeRecyclerView;
    private CarTypeAdapter mCarTypeAdapter;
    private  ImageView mImageView;
    private  TextView mAutoModelNameView, mPriceRangeView;
    private RequestFailView mRequestFailView;
    ThirdPartShopClient ThirdPartShopClient;
    ShopDetailsController mShopDetailsController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ThirdPartShopClient =new ThirdPartShopClient();
        mShopDetailsController=ShopDetailsController.getInstance();
        return inflater.inflate(R.layout.t_fragment_car_type, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mCarTypeRecyclerView = (RecyclerView) view.findViewById(R.id.car_type_list);
        mCarTypeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mCarTypeRecyclerView.setHasFixedSize(true);
        mCarTypeRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mImageView = (ImageView) view.findViewById(R.id.cartype_item_image);
        mAutoModelNameView = (TextView) view.findViewById(R.id.cartype_item_name);
        mPriceRangeView = (TextView) view.findViewById(R.id.cartype_item_price);
        mRequestFailView= (RequestFailView) view.findViewById(R.id.cartype_refresh_fail_view);
        mCarTypeRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                // TODO: 2015/12/9  跳转 车辆详情 
                AutoBaseInfoThirdShop mAutoBaseInfoThirdShop= (AutoBaseInfoThirdShop) view.getTag();
                ActivitySwitcherThirdPartShop.toCarDetail(mAutoBaseInfoThirdShop.itemID,mShopDetailsController.getShopID(),((ModelsOfferActivity)getActivity()).getSupportActionBar().getTitle().toString(),getActivity());
            }
        }));
    }

    /**
     * 绑定车系数据
     * @param mSeries
     * @param shopID
     * @param seriesID
     * @param showAnim
     */
    public void BindData(final ModelsQuote.Series mSeries, final String shopID, final String seriesID, final boolean showAnim) {
        if (mSeries != null) {
            mAutoModelNameView.setText(mSeries.getSeriesName());
            mPriceRangeView.setText(OtherUtil.formatPriceRange(mSeries.priceRange));
            ImageUtil.setImage(mContext, mImageView, mSeries.seriesThumb);
        }
        ThirdPartShopClient.getCarTypeDatas(shopID, seriesID, new LoadingAnimResponseHandler(getActivity(),showAnim) {
            @Override
            public void onSuccess(String response) {
                ArrayList<AutoBaseInfoThirdShop> mAtAutoBaseInfoThirdShops = JSONUtils.fromJson(response, new TypeToken<ArrayList<AutoBaseInfoThirdShop>>() {
                });
                if (mAtAutoBaseInfoThirdShops != null && mAtAutoBaseInfoThirdShops.size()>0)
                    mRequestFailView.setVisibility(View.GONE);
                else
                    requestFailView(mSeries,shopID,seriesID,showAnim);

                mCarTypeAdapter = new CarTypeAdapter(mAtAutoBaseInfoThirdShops, getActivity());
                mCarTypeRecyclerView.setAdapter(mCarTypeAdapter);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
               super.onFailure(statusCode, headers, responseString, throwable);
                requestFailView(mSeries,shopID,seriesID,showAnim);
            }

        });
    }

    private void requestFailView(final ModelsQuote.Series mSeries, final String shopID, final String seriesID, final boolean showAnim) {
        mRequestFailView.setEmptyDescription("获取数据失败");
        mRequestFailView.setEmptyButtonClick("刷新", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BindData(mSeries,shopID,seriesID,showAnim);
            }
        });
        mRequestFailView.setRequestViewType(RequestFailView.RequestViewType.empty);
        mRequestFailView.setVisibility(View.VISIBLE);
    } 
    

    @Override
    public String fragmentDescription() {
        return "车型列表";
    }


    /**
     * 定义Item点击事件
     */
    static class RecyclerItemClickListener implements RecyclerView.OnItemTouchListener {
        private OnItemClickListener mListener;

        public interface OnItemClickListener {
            void onItemClick(View view, int position);
        }

        GestureDetector mGestureDetector;

        public RecyclerItemClickListener(Context context, OnItemClickListener listener) {
            mListener = listener;
            mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView view, MotionEvent e) {
            View childView = view.findChildViewUnder(e.getX(), e.getY());
            if (childView != null && mListener != null && mGestureDetector.onTouchEvent(e)) {
                mListener.onItemClick(childView, view.getChildAdapterPosition(childView));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView view, MotionEvent motionEvent) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
            // do nothing
        }

    }


}
