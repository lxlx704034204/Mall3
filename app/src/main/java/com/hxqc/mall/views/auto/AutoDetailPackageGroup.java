package com.hxqc.mall.views.auto;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.hxqc.mall.core.model.auto.AutoPackage;
import com.hxqc.mall.core.util.activityutil.ActivitySwitcher;

import java.util.ArrayList;

import hxqc.mall.R;

/**
 * Author:胡俊杰
 * Date: 2015/10/9
 * FIXME
 * Todo 详情用品
 */
public class AutoDetailPackageGroup extends RelativeLayout {

    RecyclerView mRecyclerView;
    AutoPackageAdapter adapter;
    ArrayList< AutoPackage > mAutoAutoPackages;

    public AutoDetailPackageGroup(Context context) {
        super(context);
    }

    public AutoDetailPackageGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.view_auto_packages, this);


        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivitySwitcher.toAutoPackageChoose(getContext(), -1);
            }
        });

        mRecyclerView = (RecyclerView) findViewById(R.id.auto_package2);
        RecyclerView.LayoutManager manager
                = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(manager);
        adapter = new AutoPackageAdapter();
        mRecyclerView.setAdapter(adapter);
    }

    AutoPackage mChoosePackage;

    public void notifyDataSetChanged(ArrayList< AutoPackage > autoAutoPackages, AutoPackage choosePackage) {
        if (autoAutoPackages == null || autoAutoPackages.size() == 0)
            this.setVisibility(View.GONE);
        this.mAutoAutoPackages = autoAutoPackages;
        adapter.notifyDataSetChanged();
        this.mChoosePackage = choosePackage;
    }

    public void notifyDataSetChanged() {
        adapter.notifyDataSetChanged();
    }

    class AutoPackageAdapter extends RecyclerView.Adapter< RecyclerView.ViewHolder > {

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(new AutoDetailPackageItemView(getContext()));
        }


        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            AutoPackage tAutoPackage = mAutoAutoPackages.get(position);
            tAutoPackage.position = position;
            if (mChoosePackage != null && tAutoPackage.equals(mChoosePackage)) {
                ((AutoDetailPackageItemView) holder.itemView).setAutoPackage(tAutoPackage, mChoosePackage);
            } else {
                ((AutoDetailPackageItemView) holder.itemView).setAutoPackage(tAutoPackage, null);
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ActivitySwitcher.toAutoPackageChoose(getContext(), position);
                }
            });
        }

        @Override
        public int getItemCount() {
            return mAutoAutoPackages == null ? 0 : mAutoAutoPackages.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            public ViewHolder(View itemView) {
                super(itemView);
            }
        }
    }


}
