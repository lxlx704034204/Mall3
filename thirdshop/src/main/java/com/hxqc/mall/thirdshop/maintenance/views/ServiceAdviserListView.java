package com.hxqc.mall.thirdshop.maintenance.views;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hxqc.mall.core.util.ImageUtil;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.maintenance.model.ServiceAdviser;

import java.util.ArrayList;

/**
 * Author:李烽
 * Date:2016-02-19
 * FIXME
 * Todo 接待顾问
 */
public class ServiceAdviserListView extends LinearLayout {
    private RecyclerView mRecyclerView;
    private ArrayList<ServiceAdviser> mData;

    public ServiceAdviserListView(Context context) {
        this(context, null);
    }

    public ServiceAdviserListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ServiceAdviserListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.view_service_advisors_list, this);
        mRecyclerView = (RecyclerView) findViewById(R.id.service_advisors_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), HORIZONTAL, false));
        mData = new ArrayList<>();
        ListAdapter adapter = new ListAdapter(mData);
        mRecyclerView.setAdapter(adapter);
    }

    /**
     * 添加数据
     *
     * @param serviceAdvisers
     */
    public void addData(ArrayList<ServiceAdviser> serviceAdvisers) {
        mData.clear();
        mData.addAll(serviceAdvisers);
    }

    private void showDialog(int position) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_adviser_detail, null);
        TextView name = (TextView) view.findViewById(R.id.adviser_name);
        TextView post = (TextView) view.findViewById(R.id.adviser_post);
        TextView tel = (TextView) view.findViewById(R.id.adviser_tel);
        TextView experience = (TextView) view.findViewById(R.id.adviser_experience);
        TextView motto = (TextView) view.findViewById(R.id.adviser_motto);
        ImageView avatar = (ImageView) view.findViewById(R.id.adviser_avatar);
        ServiceAdviser serviceAdviser = mData.get(position);
        ImageUtil.setImageSquare(getContext(), avatar, serviceAdviser.avatar);
        name.setText(getContext().getString(R.string.name) + serviceAdviser.name);
        post.setText(getContext().getString(R.string.post) + serviceAdviser.station);
        tel.setText(getContext().getString(R.string.tel) + serviceAdviser.mobile);
        experience.setText(getContext().getString(R.string.experience) + serviceAdviser.experience);
        motto.setText(getContext().getString(R.string.motto) + serviceAdviser.logos);
        final AlertDialog alertDialog = new AlertDialog.Builder(getContext(), R.style.MaterialDialog)
                .setView(view)
                .create();
        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.setCancelable(true);
        alertDialog.show();
    }

    class ListAdapter extends RecyclerView.Adapter<ServiceAdviserListView.ViewHolder> {
        private static final int ITEM_TYPE_LAST = 1;
        private static final int ITEM_TYPE_NORMAL = 0;
        private ArrayList<ServiceAdviser> data;

        public ListAdapter(ArrayList<ServiceAdviser> data) {
            this.data = data;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.item_service_advisors_list, null);
            return new ViewHolder(view);
        }

        @Override
        public int getItemViewType(int position) {
            return position == getItemCount() - 1 ? ITEM_TYPE_LAST : ITEM_TYPE_NORMAL;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            holder.name.setText(data.get(position).name);
            holder.occupation.setText(data.get(position).station);
            ImageUtil.setImageSquare(getContext(), holder.icon, data.get(position).avatar);
//            Picasso.with(getContext()).load(data.get(position).avatar)
//                    .placeholder(R.drawable.pic_icon)
//                    .error(R.drawable.pic_icon)
//                    .into(holder.icon);
            if (getItemViewType(position) == ITEM_TYPE_LAST) {
                int dimensionPixelSize_16 = getContext().getResources().getDimensionPixelSize(R.dimen.item_padding_16);
                int dimensionPixelSize_6 = getContext().getResources().getDimensionPixelSize(R.dimen.item_padding_6);
                holder.rootLayout.setPadding(dimensionPixelSize_16, dimensionPixelSize_6,
                        dimensionPixelSize_16, dimensionPixelSize_6);
            }
            holder.rootLayout.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDialog(position);
                }
            });
        }


        @Override
        public int getItemCount() {
            return data == null ? 0 : data.size();
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView occupation;
        private ImageView icon;
        private TextView name;
        private LinearLayout rootLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            rootLayout = (LinearLayout) itemView;
            icon = (ImageView) itemView.findViewById(R.id.service_advisor_icon);
            name = (TextView) itemView.findViewById(R.id.service_advisor_name);
            occupation = (TextView) itemView.findViewById(R.id.service_advisor_occupation);
        }
    }
}
