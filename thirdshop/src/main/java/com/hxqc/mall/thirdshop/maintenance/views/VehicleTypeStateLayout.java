package com.hxqc.mall.thirdshop.maintenance.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hxqc.mall.core.util.ImageUtil;
import com.hxqc.mall.thirdshop.R;

/**
 * Author:胡仲俊
 * Date: 2017 - 01 - 13
 * Des: 预约修车车辆信息状态控件
 * FIXME
 * Todo
 */

public class VehicleTypeStateLayout extends RelativeLayout {

    private RelativeLayout completeLayout;
    private RelativeLayout isCompleteLayout;
    private Button completeBtn;
    private ImageView isCompleteAutoImg;
    private TextView isCompleteAutoContent;
    private Context mContext;
    private OnClickListener mOnClickListener;

    public interface OnClickListener {
        void completeClickListener();

        void chooseAutoClickListener();
    }

    public void setVehicleTypeStateOnClickListener(OnClickListener l) {
        this.mOnClickListener = l;
    }

    public VehicleTypeStateLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        mContext = context;
        View view = LayoutInflater.from(context).inflate(R.layout.layout_vehicle_type_state, null);

        completeLayout = (RelativeLayout) view.findViewById(R.id.state_complete);
        isCompleteLayout = (RelativeLayout) view.findViewById(R.id.state_is_complete);

        completeBtn = (Button) view.findViewById(R.id.state_complete_btn);
        isCompleteAutoImg = (ImageView) view.findViewById(R.id.state_auto_img);
        isCompleteAutoContent = (TextView) view.findViewById(R.id.state_auto_content);

        isCompleteLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnClickListener != null) {
                    mOnClickListener.chooseAutoClickListener();
                }
            }
        });

        completeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnClickListener != null) {
                    mOnClickListener.completeClickListener();
                }
            }
        });

        addView(view);
    }

    /**
     * @param state
     */
    public void setLayoutChildState(boolean state) {
        if (state) {
            completeLayout.setVisibility(VISIBLE);
            isCompleteLayout.setVisibility(GONE);
        } else {
            completeLayout.setVisibility(GONE);
            isCompleteLayout.setVisibility(VISIBLE);
        }
    }

    /**
     * @param url
     * @param content
     */
    public void setIsCompleteAutoContent(Object url, String content) {
        ImageUtil.setImage(mContext, isCompleteAutoImg, url);
        isCompleteAutoContent.setText(content);
    }
}
