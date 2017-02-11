package com.hxqc.mall.usedcar.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hxqc.mall.core.util.ImageUtil;
import com.hxqc.mall.core.util.OtherUtil;
import com.hxqc.mall.usedcar.R;
import com.hxqc.mall.usedcar.model.SellCarInfo;

import java.util.ArrayList;
import java.util.List;


/**
 * @Author : 钟学东
 * @Since : 2015-08-27
 * FIXME
 * Todo
 */
public class NewSellCarInfoAdapter extends BaseAdapter {

    Context context;
    List<SellCarInfo> infos = new ArrayList<>();

    public NewSellCarInfoAdapter(Context context, List<SellCarInfo> infos){
        this.context = context;
        this.infos = infos;
    }


    @Override
    public int getCount() {
        return infos.size();
    }

    @Override
    public Object getItem(int position) {
        return infos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if(convertView == null){
            convertView = View.inflate(context, R.layout.item_new_sell_car_info,null);
            holder = new ViewHolder();
            holder.mCarPhotoView = (ImageView) convertView.findViewById(R.id.iv_car_photo);
            holder.mCarInfoView = (TextView) convertView.findViewById(R.id.tv_car_info);
            holder.mPriceView = (TextView) convertView.findViewById(R.id.tv_price);
            holder.mStateView = (TextView) convertView.findViewById(R.id.tv_state);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.mCarInfoView.setText(infos.get(position).car_name);

        holder.mPriceView.setText(OtherUtil.amountFormat(infos.get(position).estimate_price, true) + "万");
        ImageUtil.setImage(context, holder.mCarPhotoView, infos.get(position).path);
        if(infos.get(position).check_status.equals("1")){//待审核
            holder.mStateView.setText("待审核");
        }else  if(infos.get(position).check_status.equals("2") && infos.get(position).product_status.equals("0")){//待上架
            holder.mStateView.setText("待上架");
        }else if(infos.get(position).check_status.equals("2") && infos.get(position).product_status.equals("1")){//通过审核，自主下架
            holder.mStateView.setText("下架");
        }else  if(infos.get(position).check_status.equals("2") && infos.get(position).product_status.equals("2")){//通过审核，举报下架
            holder.mStateView.setText("被举报下架");
        }else  if(infos.get(position).check_status.equals("2") && infos.get(position).product_status.equals("3")){//通过审核，上架
            holder.mStateView.setText("上架");
        }else  if(infos.get(position).check_status.equals("2") && infos.get(position).product_status.equals("4")) {//已售
            holder.mStateView.setText("已售");
        }else if(infos.get(position).check_status.equals("3")){//审核未通过
            holder.mStateView.setText("未通过审核");
        }
        return convertView;
    }

    class ViewHolder{
        ImageView mCarPhotoView;
        TextView mCarInfoView;
        TextView mPriceView;
        TextView mStateView;
    }

}
