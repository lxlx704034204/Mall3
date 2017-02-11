package com.hxqc.aroundservice.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hxqc.mall.amap.model.GasStationModel;
import com.hxqc.mall.core.adapter.BaseMapViewPagerAdapter;
import com.hxqc.mall.core.model.auto.PickupPointT;
import com.hxqc.mall.core.util.OtherUtil;

import java.util.ArrayList;

import hxqc.mall.R;

/**
 * Function:
 *
 * @author 袁秉勇
 * @since 2016年06月25日
 */
public class ViewPagerForGasStationAdapter extends BaseMapViewPagerAdapter {
    private final static String TAG = ViewPagerForGasStationAdapter.class.getSimpleName();
    private Context mContext;

    private ArrayList< GasStationModel > gasStationModels = new ArrayList<>();

    private ArrayList< View > views;

    private CallBack callBack;

    private LayoutInflater mLayoutInflater;

    private GasStationModel gasStationModel;


    public void setCallback(CallBack callback) {
        this.callBack = callback;
    }


    public ViewPagerForGasStationAdapter(Context context) {
        this.mContext = context;

        mLayoutInflater = LayoutInflater.from(mContext);

        views = new ArrayList<>();
    }


    public void setData(ArrayList< GasStationModel > gasStationModels) {
        if (this.gasStationModels.size() > 0) {
            this.gasStationModels.clear();
        }

        if (views.size() > 0) {
            views.clear();
        }

        for (int i = 0; i < gasStationModels.size(); i++) {
            View view = mLayoutInflater.inflate(R.layout.item_map_list_for_gas_station, null);
            views.add(view);
        }

        this.gasStationModels.addAll(gasStationModels);
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return views.size();
    }


    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        ViewHolder viewHolder = new ViewHolder();

        gasStationModel = gasStationModels.get(position);

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
        viewHolder.mGas90 = (TextView) view.findViewById(R.id.gas_90);
        viewHolder.mGas93 = (TextView) view.findViewById(R.id.gas_93);
        viewHolder.mGas97 = (TextView) view.findViewById(R.id.gas_97);
        viewHolder.mGas0 = (TextView) view.findViewById(R.id.gas_0);

        viewHolder.mDividierView.setVisibility(View.GONE);
        viewHolder.mShopNameView.setText((position + 1) + "." + gasStationModel.name);
        viewHolder.mShopDistanceView.setText(OtherUtil.reformatDistance(gasStationModel.distance));
        viewHolder.mShopAddressView.setText("地址: " + gasStationModel.address);
        viewHolder.mGas90.setText(gasStationModel.price.E90);
        viewHolder.mGas93.setText(gasStationModel.price.E93);
        viewHolder.mGas97.setText(gasStationModel.price.E97);
        viewHolder.mGas0.setText(gasStationModel.price.E0);
        viewHolder.mGotoThereView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickCallBack.clickCallBack(new PickupPointT(gasStationModel.address, gasStationModel.getLatitude() + "", gasStationModel.getLongitude() + "", ""));
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
        TextView mShopNameView, mShopDistanceView, mShopAddressView, mGas90, mGas93, mGas97, mGas0;
        LinearLayout mGotoThereView;
    }
}
