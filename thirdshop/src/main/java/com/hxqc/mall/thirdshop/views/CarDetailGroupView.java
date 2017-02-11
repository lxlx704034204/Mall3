package com.hxqc.mall.thirdshop.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hxqc.util.DisplayTools;
import com.hxqc.mall.thirdshop.R;

/**
 * Function: 自定义的选择车辆参数view
 *
 * @author 袁秉勇
 * @since 2016年05月04日
 */
public class CarDetailGroupView extends LinearLayout {
    private final static String TAG = CarDetailGroupView.class.getSimpleName();
    private Context mContext;

    private String mGroupTitle;
    private String mGroupLabel;

    private TextView mGroupTitleView;
    private TextView mGroupLabelView;
    private GridLayout mGridLayout;

    private int lastClickPos = 0;

    private CallBack callBack;


    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }


    public CarDetailGroupView(Context context) {
        this(context, null);
    }


    public CarDetailGroupView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }


    public CarDetailGroupView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CarDetailGroupView);
        for (int i = 0; i < a.getIndexCount(); i++) {
            int attr = a.getIndex(i);
            if (attr == R.styleable.CarDetailGroupView_group_title) {
                mGroupTitle = a.getString(attr);
            } else if (attr == R.styleable.CarDetailGroupView_group_label) {
                mGroupLabel = a.getString(attr);
            }
        }

        a.recycle();

        initView(context);
    }


    private void initView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.view_car_detail_param_choose, this);

        mGroupTitleView = (TextView) findViewById(R.id.group_title);
        mGroupLabelView = (TextView) findViewById(R.id.group_label);
        mGridLayout = (GridLayout) findViewById(R.id.group_content);

        if (!TextUtils.isEmpty(mGroupTitle)) {
            mGroupTitleView.setText(mGroupTitle);
        } else {
            mGroupTitleView.setText("请填写title");
        }

        if (!TextUtils.isEmpty(mGroupLabel)) {
            mGroupLabelView.setText(mGroupLabel);
        }
    }


    /**
     *
     * @param columnCount   GridLayout的列数
     * @param defaultChoosed    默认希望选择的按钮的索引数
     * @param label     GridLayout中按钮显示的文字
     */
    public void initData(int columnCount, int defaultChoosed, final String[] label, final String[] key) {
        if (label.length <= 0) return;
        mGridLayout.setColumnCount(columnCount);
        lastClickPos = defaultChoosed;
        if (defaultChoosed >= label.length) defaultChoosed = 0;

        for (int i = 0; i < label.length; i++) {
            TextView textView = new TextView(mContext);
            GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams();
            layoutParams.width = DisplayTools.dip2px(mContext, 96);
            layoutParams.height = DisplayTools.dip2px(mContext, 32);
            layoutParams.setMargins(0, 0, DisplayTools.dip2px(mContext, 35), DisplayTools.dip2px(mContext, 20));
            textView.setLayoutParams(layoutParams);
            textView.setGravity(Gravity.CENTER);

            if (i == defaultChoosed) {
                textView.setBackgroundResource(R.drawable.orange_btn);
                textView.setTextColor(getResources().getColor(R.color.white));
                if (callBack != null) callBack.callBack(key[defaultChoosed]); // 一定要在initData前调用setCallBack方法；
            } else {
                textView.setBackgroundResource(R.drawable.car_detail_radiobutton_normal);
                textView.setTextColor(getResources().getColor(R.color.car_detail_button_gray));
            }
            textView.setTextSize(12);
            textView.setText(label[i]);
            textView.setTag(i);
            textView.setId(i);

            textView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if ((int) v.getTag() == lastClickPos) return;

                    TextView textView1 = (TextView) mGridLayout.findViewById((int) v.getTag());
                    textView1.setBackgroundResource(R.drawable.orange_btn);
                    textView1.setTextColor(getResources().getColor(R.color.white));

                    TextView textView2 = (TextView) mGridLayout.findViewById(lastClickPos);
                    textView2.setBackgroundResource(R.drawable.car_detail_radiobutton_normal);
                    textView2.setTextColor(getResources().getColor(R.color.car_detail_button_gray));

                    if (callBack != null) callBack.callBack(key[(int) v.getTag()]);
                    lastClickPos = (int) v.getTag();
                }
            });
            mGridLayout.addView(textView);
        }
    }

    public interface CallBack {
        void callBack(String string);
    }
}
