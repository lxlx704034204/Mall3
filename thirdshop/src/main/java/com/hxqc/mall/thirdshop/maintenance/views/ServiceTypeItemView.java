package com.hxqc.mall.thirdshop.maintenance.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hxqc.mall.core.views.materialedittext.MaterialEditText;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.maintenance.adapter.ServiceTypeChildAdapter;
import com.hxqc.mall.thirdshop.maintenance.model.maintenance.ServiceType;
import com.hxqc.util.DebugLog;


/**
 * Author:胡仲俊
 * Date: 2016 - 03 - 22
 * FIXME
 * Todo 服务类型条目的控件
 */
public class ServiceTypeItemView extends LinearLayout {

    public static final String TAG = "ServiceTypeItemView";

    private ImageView mServiceLogView;
    private TextView mServiceTitleView;
    private CustomGridView mServiceTypeItemListView;
    private CheckBox mServiceChooseView;
    private Context mContext;
    private OnCheckedListener mOnCheckedListener;
    private boolean mIsChecked = false;
    private RelativeLayout mServiceTypeRemarkView;
    private MaterialEditText mServiceTypeRemarkContentView;
    private final LinearLayout mServiceTypeRemarkBGView;
    private final RelativeLayout mServiceTypeItemLayout;

    /**
     * 选择接口
     */
    public interface OnCheckedListener {
        void setOnCheckedListener();
    }

    /**
     * 设置选择接口
     */
    public void setOnCheckedListener(OnCheckedListener l) {
        this.mOnCheckedListener = l;
    }


    public ServiceTypeItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        View view = LayoutInflater.from(context).inflate(R.layout.view_service_type_item, null);
        addView(view);
//        mServiceLogView = (ImageView) view.findViewById(R.id.service_type_group_img);
        mServiceTypeItemLayout = (RelativeLayout) view.findViewById(R.id.service_type_parent);
        mServiceTitleView = (TextView) view.findViewById(R.id.service_type_v3_tv);
        mServiceChooseView = (CheckBox) view.findViewById(R.id.serviec_type_group_cb);
        mServiceTypeItemListView = (CustomGridView) view.findViewById(R.id.service_type_child_list);
        mServiceTypeRemarkView = (RelativeLayout) findViewById(R.id.service_type_remark);
        mServiceTypeRemarkBGView = (LinearLayout) findViewById(R.id.service_type_remark_bg);
        mServiceTypeRemarkContentView = (MaterialEditText) findViewById(R.id.service_type_remark_content);
        mServiceTypeRemarkContentView.setHideUnderline(true);

    }

    /**
     * 服务类型单个项目下内容的显示
     */
    public void setListVisibility(int visibility) {
        mServiceTypeItemListView.setVisibility(visibility);
    }

    public void setRemarkVisibility(int visibility) {
        mServiceTypeRemarkView.setVisibility(visibility);
    }

    /**
     * 服务类型单个项目数据设置
     */
    public void setData(Context context, ServiceType serviceType) {
        /*if (TextUtils.isEmpty(serviceType.thumb)) {
            mServiceLogView.setImageResource(R.drawable.pic_normal);
        } else {
            ImageUtil.setImage(mContext, mServiceLogView, serviceType.thumb);
        }*/
        mServiceTitleView.setText(serviceType.kindTitle);
    }

    /**
     * 服务类型单个项目下内容的数据设置
     */
    public void setAdapter(Context context, final ServiceType serviceType) {
        ServiceTypeChildAdapter serviceTypeChildAdapter = new ServiceTypeChildAdapter(context, serviceType.items);
        mServiceTypeItemListView.setAdapter(serviceTypeChildAdapter);
        /*mServiceTypeItemListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DebugLog.i(TAG, "position:" + position);
                String title = serviceType.items.get(position).itemName;
                String url = serviceType.items.get(position).url;
                ActivitySwitchBase.toH5Activity(mContext, title, url);
            }
        });*/
    }

    public CheckBox getmServiceChooseView() {
        return mServiceChooseView;
    }

    public EditText getmServiceTypeRemarkContentView() {
        return mServiceTypeRemarkContentView;
    }

    public RelativeLayout getServiceTypeItemLayout() {
        return mServiceTypeItemLayout;
    }

    /**
     * 服务类型单个项目背景色的切换
     */
    public void isChecked() {
        if (!mIsChecked) {
            mServiceTitleView.setSelected(true);
            mServiceChooseView.setSelected(true);
            mIsChecked = true;
        } else {
            mServiceTitleView.setSelected(false);
            mServiceChooseView.setSelected(false);
            mIsChecked = false;
        }
    }

    /**
     * 服务类型单个项目背景色的切换
     */
    public void isChecked(boolean isChecked) {
        mServiceTitleView.setSelected(isChecked);
        mServiceChooseView.setSelected(isChecked);
    }

    public void setServiceTypeRemarkContent(CharSequence text) {
        mServiceTypeRemarkContentView.setText(text);
    }

    /**
     * 获取其他原因
     *
     * @return
     */
    public String getServiceTypeRemarkContent() {
        DebugLog.i(TAG, mServiceTypeRemarkContentView.getText().toString());
        return mServiceTypeRemarkContentView.getText().toString();
    }

    public void setServiceTypeRemarkContentBackground(boolean isRed) {
        if (isRed) {
            mServiceTypeRemarkBGView.setBackgroundResource(R.drawable.shape_service_type_remark_red_bg);
        } else {
            mServiceTypeRemarkBGView.setBackgroundResource(R.drawable.shape_service_type_remark_bg);
        }
    }
}
