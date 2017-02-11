package com.hxqc.mall.usedcar.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hxqc.mall.usedcar.R;
import com.hxqc.mall.usedcar.model.OffsaleReason;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author : 钟学东
 * @Since : 2015-09-10
 * FIXME
 * Todo
 */
public class UndercarriageReasonAdapter extends BaseAdapter {

    Context context;
    List<OffsaleReason> offsaleReasons = new ArrayList<>();

    public UndercarriageReasonAdapter(Context context, List<OffsaleReason> offsaleReasons){
        this.context =context;
        this.offsaleReasons = offsaleReasons;
    }

    @Override
    public int getCount() {
        return offsaleReasons.size();
    }

    @Override
    public OffsaleReason getItem(int position) {
        return offsaleReasons.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder  holder ;
        if(convertView == null){
            convertView = View.inflate(context, R.layout.item_undercarriage,null);
            holder = new ViewHolder();
            holder.mCheckView = (ImageView) convertView.findViewById(R.id.iv_check);
            holder.mContentView = (TextView) convertView.findViewById(R.id.tv_check);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.mContentView.setText(offsaleReasons.get(position).content);
        if(offsaleReasons.get(position).isSelect){
            holder.mCheckView.setImageResource(R.mipmap.ic_shelfcause_selected);
        }else {
            holder.mCheckView.setImageResource(R.mipmap.ic_shelfcause_choice);
        }

        return convertView;
    }

    class ViewHolder {
        TextView mContentView;
        ImageView mCheckView;
    }

}
