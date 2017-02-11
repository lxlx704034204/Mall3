package com.hxqc.mall.drivingexam.ui.helpyoupass.lightandvoice;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hxqc.mall.drivingexam.R;
import com.hxqc.mall.core.base.BaseAdapter;
import com.hxqc.mall.core.model.Event;

import org.greenrobot.eventbus.EventBus;


/**
 * 播放音频九宫格按键的adapter
 * Created by zhaofan on 2016/8/15.
 */
public class LightButtonAdapter extends BaseAdapter<String, BaseAdapter.BaseViewHolder> {
    public static final String VOICE_MODE = "voice_mode";
    public static final String LIGHT_MODE = "light_mode";
    private GridView gv;
    private boolean isVoiceMode = false;
    private int[] resId;

    public LightButtonAdapter(Context context) {
        super(context);
    }

    public LightButtonAdapter(Context context, GridView gv) {
        super(context);
        this.gv = gv;
    }

    public void setVoiceMode(boolean isVoiceMode, int[] resId) {
        this.isVoiceMode = isVoiceMode;
        this.resId = resId;
    }

    @Override
    protected BaseViewHolder createViewHolder(int position, ViewGroup parent) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_light_simulate, parent, false));
    }

    @Override
    protected void bindViewHolder(BaseViewHolder holder, View convertView, final int position, final String data) {
        final ViewHolder vh = (ViewHolder) holder;
        vh.tv.setText(data);


        if (resId != null) {
            vh.img1.setImageResource(resId[position]);
        } else
            vh.img1.setImageResource(R.drawable.icon_light);


        convertView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (TextUtils.isEmpty(data)) {
                    return;
                }

                gv.setItemChecked(position, !gv.isItemChecked(position));
                notifyDataSetChanged();

                EventBus.getDefault().post(new Event(position, isVoiceMode ? VOICE_MODE : LIGHT_MODE));

            }
        });

        AnimationDrawable spinner = (AnimationDrawable) vh.img2.getBackground();


        if (gv.isItemChecked(position)) {
            vh.main.setBackgroundColor(Color.parseColor("#f5f5f5"));
            vh.img1.setVisibility(View.INVISIBLE);
            vh.img2.setVisibility(View.VISIBLE);
            spinner.start();
        } else {
            vh.main.setBackgroundColor(Color.parseColor("#ffffff"));
            vh.img1.setVisibility(View.VISIBLE);
            vh.img2.setVisibility(View.INVISIBLE);
            spinner.stop();
        }


    }


    private class ViewHolder extends BaseViewHolder {
        private TextView tv;
        private ImageView img1, img2;
        private RelativeLayout main;

        public ViewHolder(View itemView) {
            super(itemView);
            tv = getView(R.id.tv);
            img1 = getView(R.id.img1);
            img2 = getView(R.id.img2);
            main = getView(R.id.main);
        }
    }
}
