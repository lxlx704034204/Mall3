package com.hxqc.mall.core.adapter.me;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import hxqc.mall.R;

/**
 * Author:李烽
 * Date:2016-03-04
 * FIXME
 * Todo 个人中心界面的功能模块适配器
 */
@Deprecated
public class MeModuleAdapter extends RecyclerView.Adapter<MeModuleAdapter.ViewHolder> {
    private String[] mLabel;
    private int[] drawables;
    private Context context;

    public MeModuleAdapter(Context context) {
        this.context = context;
        mLabel = context.getResources().getStringArray(R.array.me_module);
        drawables = new int[]{
                R.drawable.ic_order,
                R.drawable.ic_wallet,
                R.drawable.ic_shoppingcart,
                R.drawable.ic_repair,
                R.drawable.ic_maintain,
                R.drawable.ic_rescue,
                R.drawable.ic_carinformation,
                R.drawable.ic_rule,
                R.drawable.ic_activity,
                R.drawable.ic_follow,
                R.drawable.ic_comment,
                R.drawable.ic_customer_service
        };
    }

    @Override
    public MeModuleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_me_button, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MeModuleAdapter.ViewHolder holder, int position) {
        holder.icon.setImageResource(drawables[position]);
        holder.label.setText(mLabel[position]);
    }

    @Override
    public int getItemCount() {
        return mLabel.length;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView icon;
        private TextView label;

        public ViewHolder(View itemView) {
            super(itemView);
            icon = (ImageView) itemView.findViewById(R.id.button_icon);
            label = (TextView) itemView.findViewById(R.id.button_label);
        }
    }
}
