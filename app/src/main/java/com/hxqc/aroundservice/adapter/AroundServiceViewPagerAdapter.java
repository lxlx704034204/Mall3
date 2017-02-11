package com.hxqc.aroundservice.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.services.core.PoiItem;
import com.hxqc.mall.core.adapter.BaseMapViewPagerAdapter;
import com.hxqc.mall.core.model.auto.PickupPointT;
import com.hxqc.mall.core.util.OtherUtil;

import java.util.ArrayList;

import hxqc.mall.R;

/**
 * Function: 周边服务地图ViewPager的Adapter（停车场和充电站）
 *
 * @author 袁秉勇
 * @since 2016年06月24日
 */
public class AroundServiceViewPagerAdapter extends BaseMapViewPagerAdapter {
    private final static String TAG = AroundServiceViewPagerAdapter.class.getSimpleName();
    private Context mContext;

    private ArrayList< PoiItem > poiItems = new ArrayList<>();

    private ArrayList< View > views;

    private CallBack callBack;

    private LayoutInflater mLayoutInflater;

    private PoiItem poiItem;


    public void setCallback(CallBack callback) {
        this.callBack = callback;
    }


    public AroundServiceViewPagerAdapter(Context context) {
        this.mContext = context;

        mLayoutInflater = LayoutInflater.from(mContext);

        views = new ArrayList<>();
    }


    public void setData(ArrayList< PoiItem > poiItems) {
        if (this.poiItems.size() > 0) {
            this.poiItems.clear();
        }

        if (views.size() > 0) {
            views.clear();
        }

        for (int i = 0; i < poiItems.size(); i++) {
            View view = mLayoutInflater.inflate(R.layout.item_map_list, null);
            views.add(view);
        }

        this.poiItems.addAll(poiItems);
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return views.size();
    }


    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        ViewHolder viewHolder = new ViewHolder();

        poiItem = poiItems.get(position);

        View view = views.get(position);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callBack != null) callBack.callBack(position);
            }
        });

        viewHolder.mDividierView = view.findViewById(R.id.divider);
        viewHolder.mShopNameView = (TextView) view.findViewById(R.id.shop_name);
        viewHolder.mShopDistanceView = (TextView) view.findViewById(R.id.shop_distance);
        viewHolder.mShopAddressView = (TextView) view.findViewById(R.id.shop_address);
        viewHolder.mGotoThereView = (LinearLayout) view.findViewById(R.id.goThereButton);

        viewHolder.mDividierView.setVisibility(View.GONE);
        viewHolder.mShopNameView.setText((position + 1) + "." + poiItem.getTitle());
        viewHolder.mShopDistanceView.setText(OtherUtil.reformatDistance(poiItem.getDistance()+""));
        viewHolder.mShopAddressView.setText("地址: " + poiItem.getSnippet());

        viewHolder.mGotoThereView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        Log.e("ViewClickTest", " -------------------- ACTION_DOWN");
                        break;

                    case MotionEvent.ACTION_MOVE:
                        Log.e("ViewClickTest", " -------------------- ACTION_MOVE");
                        break;

                    case MotionEvent.ACTION_UP:
                        Log.e("ViewClickTest", " -------------------- ACTION_UP");
                        break;
                }
                return false;
            }
        });
        viewHolder.mGotoThereView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickCallBack.clickCallBack(new PickupPointT(poiItem.getSnippet(), poiItem.getLatLonPoint().getLatitude() + "", poiItem.getLatLonPoint().getLongitude() + "", poiItem.getTel()));
            }
        });

        container.addView(views.get(position));

        return views.get(position);
    }


    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(views.get(position));
    }


    public interface CallBack {
        void callBack(int position);
    }


    class ViewHolder {
        View mDividierView;
        TextView mShopNameView, mShopDistanceView, mShopAddressView;
        LinearLayout mGotoThereView;
    }
}
