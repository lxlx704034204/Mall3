package com.hxqc.pay.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.maps.model.LatLng;
import com.hxqc.mall.core.model.auto.PickupPointT;
import com.hxqc.mall.core.views.dialog.ListDialog;
import com.hxqc.pay.activity.PickupPlaceActivity;
import com.hxqc.util.DebugLog;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import hxqc.mall.R;

/**
 * Author: wanghao
 * Date: 2015-03-16
 * FIXME
 * 自提点 adapter
 * 114.211157,30.553996
 */
public class PickPositionAdapter extends BaseAdapter {

    ArrayList<PickupPointT > data;
    Context context;
    PickupPlaceActivity mPickupPlaceActivity;
    PositionChange positionChange;

    public PickPositionAdapter(ArrayList<PickupPointT > data, Context context) {
        this.data = data;
        this.context = context;
        this.mPickupPlaceActivity = (PickupPlaceActivity) context;
        DebugLog.i("distance", "--PickPositionAdapter--" + data.toString() + "====");
    }

    public void setPositionChange(PositionChange positionChange) {
        this.positionChange = positionChange;
    }

    @Override
    public int getCount() {
        return data == null ? 0 : data.size();
    }

    @Override
    public PickupPointT getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View view;
        PickupViewHolder holder;

        if (convertView != null) {

            view = convertView;
            holder = (PickupViewHolder) view.getTag();

        } else {
            view = LayoutInflater.from(context).inflate(R.layout.item_pick_nearby_position, null);
            holder = new PickupViewHolder();

            holder.mLayout = (LinearLayout) view.findViewById(R.id.ll_item_layout);
            holder.mShopName = (TextView) view.findViewById(R.id.tv_pick_shop_name);
            holder.mShopAddress = (TextView) view.findViewById(R.id.tv_pick_shop_address);
            holder.mShopPhone = (TextView) view.findViewById(R.id.tv_pick_shop_phone);
            holder.mDistance = (TextView) view.findViewById(R.id.tv_distance_for_me);
            holder.mPickCarThis = (TextView) view.findViewById(R.id.btn_get_this_shop_position);
            holder.mDialPhone = (TextView) view.findViewById(R.id.btn_call_this_shop_phone);
            holder.num = (TextView) view.findViewById(R.id.tv_mappin_num);
            view.setTag(holder);
        }

        holder.num.setText(String.format("%d", position + 1));

        final PickupPointT pickupPoint = data.get(position);
        final LatLng thisLL = new LatLng(pickupPoint.getLatitude(), pickupPoint.getLongitude());
        setData(holder, pickupPoint, thisLL);
        /**
         * 打电话
         */
        holder.mDialPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = pickupPoint.tel;
                if (number.contains(",")) {
                    final String[] numbers = number.split(",");
                    ListDialog dialog1 = new ListDialog(context, "点击拨号", numbers) {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            openDial(numbers[position]);
                        }
                        @Override
                        protected void doNext(int position) {
                        }
                    };
                    dialog1.show();
                } else {
                    openDial(number);
                }
            }
        });


        holder.mPickCarThis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    DebugLog.i("pick", pickupPoint.toString());
                    DebugLog.i("pick", position + "---");
                    EventBus.getDefault().post(pickupPoint);
                    ((Activity) context).finish();
            }
        });

        holder.mLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DebugLog.i("pickup", " mLayout " + thisLL.toString());
                positionChange.ChangeMapPosition(thisLL);
            }
        });
        return view;
    }

    private void setData(final PickupViewHolder holder, final PickupPointT pickupPoint, final LatLng thisLL) {
                holder.mDistance.setText(String.format("%s", Math.round(pickupPoint.distance / 100d) / 10d));
                holder.mShopName.setText(pickupPoint.name);
                holder.mShopAddress.setText(String.format("地址：%s", pickupPoint.address));
                holder.mShopPhone.setText(String.format("电话：%s", pickupPoint.tel));
                DebugLog.e("distance", pickupPoint.toString());
    }

    //拨打电话
    public void openDial(String str_num) {
        if (str_num.contains("(")) {
            str_num = str_num.replace("(", "");
        }
        if (str_num.contains(")")) {
            str_num = str_num.replace(")", "");
        }
        Intent dialIntent = new Intent();
        dialIntent.setAction(Intent.ACTION_DIAL);
        dialIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        dialIntent.setData(Uri.parse("tel:" + str_num));
        context.startActivity(dialIntent);
    }

    //改变位置
    public interface PositionChange {
        void ChangeMapPosition(LatLng LL);
    }

    class PickupViewHolder {
        public LinearLayout mLayout;
        public TextView mShopName;
        public TextView mShopAddress;
        public TextView mShopPhone;
        public TextView mDistance;
        public TextView mPickCarThis;
        public TextView mDialPhone;
        public TextView num;
    }
}
