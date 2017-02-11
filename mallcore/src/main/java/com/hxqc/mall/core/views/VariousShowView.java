package com.hxqc.mall.core.views;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hxqc.mall.core.R;
import com.hxqc.util.DebugLog;

/**
 * Function:
 *
 * @author 袁秉勇
 * @since 2016年02月02日
 */
public class VariousShowView extends LinearLayout {
    private final static String TAG = VariousShowView.class.getSimpleName();
    Context mContext;
    private View mRootView;
    private ImageView mLeftIcon, mRightOperateIcon;
    private TextView mLabelTextView, mRightTipTextView;
    private View mDividerLineView;
    private View mFoldableView;

    private boolean textBold = false;
    private int mLeftIconImageRes;
    private String labelText, tipText;
    private float labelTextSize, tipTextSize;
    private ColorStateList labelTextColor, tipTextColor;
    private int mRightOperateImageRes, mFoldIconRes, mUnfoldIconRes;
    private int operateType = 0;
    private int foldStatus = 0;// 默认为展开


    public View getmRootView() {
        return mRootView;
    }


    public ImageView getmLeftIcon() {
        return mLeftIcon;
    }


    public ImageView getmRightOperateIcon() {
        return mRightOperateIcon;
    }


    public TextView getmLabelTextView() {
        return mLabelTextView;
    }


    public TextView getmRightTipTextView() {
        return mRightTipTextView;
    }


    public View getmDividerLineView() {
        return mDividerLineView;
    }


    public int getFoldStatus() {
        return foldStatus;
    }


    public VariousShowView(Context context) {
        super(context);
    }


    public VariousShowView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        setOrientation(VERTICAL);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.VariousShowView);

        for (int i = 0; i < typedArray.getIndexCount(); i++) {
            int attr = typedArray.getIndex(i);
            if (attr == R.styleable.VariousShowView_leftIcon) {
                mLeftIconImageRes = typedArray.getResourceId(attr, 0);
                DebugLog.e(TAG, " mLeftIconImageRes : " + mLeftIconImageRes);

            } else if (attr == R.styleable.VariousShowView_labelTextBold) {
                textBold = typedArray.getBoolean(attr, false);
            } else if (attr == R.styleable.VariousShowView_labelText) {
                labelText = typedArray.getString(attr);
                DebugLog.e(TAG, " labelText : " + labelText);

            } else if (attr == R.styleable.VariousShowView_labelTextSize) {
                labelTextSize = typedArray.getDimension(attr, 16);
                DebugLog.e(TAG, " labelTextSize : " + labelTextSize);

            } else if (attr == R.styleable.VariousShowView_labelTextColor) {
                labelTextColor = typedArray.getColorStateList(attr);
                DebugLog.e(TAG, " labelTextColor :  " + labelTextColor);

            } else if (attr == R.styleable.VariousShowView_tipText) {
                tipText = typedArray.getString(attr);
                DebugLog.e(TAG, " tipText : " + tipText);

            } else if (attr == R.styleable.VariousShowView_tipTextSize) {
                tipTextSize = typedArray.getDimension(attr, 14);
                DebugLog.e(TAG, " tipTextSize " + tipTextSize);

            } else if (attr == R.styleable.VariousShowView_tipTextColor) {
                tipTextColor = typedArray.getColorStateList(attr);
                DebugLog.e(TAG, " tipTextColor :  " + tipTextColor);

            } else if (attr == R.styleable.VariousShowView_rightOperateIcon) {
                mRightOperateImageRes = typedArray.getResourceId(attr, 0);

//                case R.styleable.VariousShowView_operateType:
//                    operateType = typedArray.getInt(attr, 0);
//                    break;

            } else if (attr == R.styleable.VariousShowView_foldIconRes) {
                mFoldIconRes = typedArray.getResourceId(attr, 0);

            } else if (attr == R.styleable.VariousShowView_unfoldIconRes) {
                mUnfoldIconRes = typedArray.getResourceId(attr, 0);

            } else if (attr == R.styleable.VariousShowView_foldStatus) {
                foldStatus = typedArray.getInt(attr, 0);
                DebugLog.e(TAG, " foldStatus : " + foldStatus);
            }
        }

        LayoutInflater.from(context).inflate(R.layout.view_various_show, this);
        mRootView = findViewById(R.id.root_view);
        mLeftIcon = (ImageView) findViewById(R.id.icon);
        mLabelTextView = (TextView) findViewById(R.id.label_text);
        mRightTipTextView = (TextView) findViewById(R.id.tip_text);
        mRightOperateIcon = (ImageView) findViewById(R.id.operate_type_icon);
        mDividerLineView = findViewById(R.id.divider_line);

        if (mLeftIconImageRes != 0) {
            mLeftIcon.setImageResource(mLeftIconImageRes);
        } else {
            mLeftIcon.setVisibility(GONE);
        }

        mLabelTextView.getPaint().setFakeBoldText(textBold);
        mLabelTextView.setText(labelText);
        mLabelTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, labelTextSize != 0 ? labelTextSize : TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 16, getResources().getDisplayMetrics()));
        mLabelTextView.setTextColor(labelTextColor != null ? labelTextColor : ColorStateList.valueOf(0xFFaaaaaa));

        mRightTipTextView.setText(tipText);
        mRightTipTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, tipTextSize != 0 ? tipTextSize : TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 14, getResources().getDisplayMetrics()));
        mRightTipTextView.setTextColor(tipTextColor != null ? tipTextColor : ColorStateList.valueOf(0xFFcccccc));

        if (mRightOperateImageRes != 0) {
            mRightOperateIcon.setImageResource(mRightOperateImageRes);
        }

        typedArray.recycle();
    }


    @Override
    protected void onFinishInflate() {
        DebugLog.e(TAG, " ----------- > onFinishInflate");
        super.onFinishInflate();

        mFoldableView = findViewById(R.id.VariousShowViewContent);
        if (mFoldableView != null) {
            operateType = 1;
            contentViewOnClick();
            setFold(foldStatus == 1);
        }
    }


    public void setFold(boolean fold) {
        if (fold) {
            //收起状态
            if (mFoldableView.getVisibility() == VISIBLE) {
                mFoldableView.setVisibility(GONE);
                mDividerLineView.setVisibility(VISIBLE);
            }
            if (mFoldIconRes != 0) {
                mRightOperateIcon.setImageResource(mFoldIconRes);
            } else {
                mRightOperateIcon.setImageResource(R.drawable.ic_cbb_arrow_up);
            }
        } else {
            //展开状态
            if (mFoldableView.getVisibility() == GONE) {
                mFoldableView.setVisibility(View.VISIBLE);
                mDividerLineView.setVisibility(GONE);
            }
            if (mUnfoldIconRes != 0) {
                mRightOperateIcon.setImageResource(mUnfoldIconRes);
            } else {
                mRightOperateIcon.setImageResource(R.drawable.ic_cbb_arrow_down);
            }
        }
    }


    /** 当该视图用于展示数据时，通过该方法修改视图默认展开为关闭 */
    public void foldContentView() {
        if (operateType == 1 && mFoldableView.getVisibility() == VISIBLE) {
            mFoldableView.setVisibility(View.GONE);
            if (mFoldIconRes != 0) {
                mRightOperateIcon.setImageResource(mFoldIconRes);
            } else {
                mRightOperateIcon.setImageResource(R.drawable.ic_cbb_arrow_up);
            }
        }
    }

//    @Override
//    public void setOnClickListener(OnClickListener l) {
//        if (operateType == 1) {
//            throw new OperationCanceledException("当前View是折叠事件，不支持重复设置Listener");
//        }
//        super.setOnClickListener(l);
//    }


    /**
     * 折叠内容
     */
    private void contentViewOnClick() {
        if (mFoldableView != null) {
            mRootView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    doClick();
                }
            });
        }
    }

    /** 当View可折叠时，点击触发的操作 **/
    public void doClick(){
        if (mFoldableView.getVisibility() == VISIBLE) {
            mFoldableView.setVisibility(View.GONE);
            mDividerLineView.setVisibility(VISIBLE);
            if (mFoldIconRes != 0) {
                mRightOperateIcon.setImageResource(mFoldIconRes);
            } else {
                mRightOperateIcon.setImageResource(R.drawable.ic_cbb_arrow_up);
            }
        } else {
            mFoldableView.setVisibility(View.VISIBLE);
            mDividerLineView.setVisibility(GONE);
            if (mUnfoldIconRes != 0) {
                mRightOperateIcon.setImageResource(mUnfoldIconRes);
            } else {
                mRightOperateIcon.setImageResource(R.drawable.ic_cbb_arrow_down);
            }
        }
    }

}