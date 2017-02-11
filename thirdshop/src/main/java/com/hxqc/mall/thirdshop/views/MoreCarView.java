package com.hxqc.mall.thirdshop.views;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.util.DebugLog;

/**
 * liaoguilong
 * 2016年11月16日 17:27:34
 * Created by CPR113 on 2016/11/16.
 * 分期购车 -展开收起 更多车型(暂时没用)
 */
@Deprecated
public class MoreCarView  extends LinearLayout implements View.OnClickListener{

    protected TextView mTextView; //文本
    protected ImageView mImageView; //箭头
    private Context mContext;
    boolean isOpen=false; //展开标识,默认收起
    protected  LinearLayout mLinearLayout;
    private int durationMillis = 400; //动画时长 毫秒

    public void setTextView(String text){
        if(mTextView !=null && !TextUtils.isEmpty(text))
        mTextView.setText(text);
    }


    public void setAnimatorUpdateListener(AnimatorUpdateListener animatorUpdateListener) {
        this.animatorUpdateListener = animatorUpdateListener;
    }

    private AnimatorUpdateListener animatorUpdateListener;

    /**
     * 展开和收起 动画过程监听接口
     */
    public interface AnimatorUpdateListener{
         void  OpenAnimatorUpdate(float AnimatedValue);
         void  CloseAnimatorUpdate(float AnimatedValue);
    }

    public MoreCarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext=context;
        initView();
    }

    private void initView() {
        LayoutInflater.from(mContext).inflate(R.layout.view_morecar_layout, this);
        mTextView= (TextView) findViewById(R.id.more_text);
        mImageView= (ImageView) findViewById(R.id.more_image);
        mLinearLayout= (LinearLayout) findViewById(R.id.more_bg);
        this.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        isOpen = !isOpen;

        mImageView.clearAnimation();
        if (isOpen) {
            openAnim();
        } else {
            closeAnim();
        }
    }

    private void closeAnim() {
        //负值即为逆时针旋转，正值是顺时针旋转。
        ObjectAnimator anim = ObjectAnimator.ofFloat(mImageView, "rotation", 0f, -180f);
        // 动画的持续时间，执行多久？
        anim.setDuration(durationMillis);
        // 回调监听
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (Float) animation.getAnimatedValue();
                DebugLog.d("addUpdateListener","closeAnim-->"+value);
                if(animatorUpdateListener !=null)
                    animatorUpdateListener.OpenAnimatorUpdate(value);
                if(value == -180)
                    setCloseEndStyle("");
            }
        });
        // 正式开始启动执行动画
        anim.start();
    }

    private void openAnim() {
        //负值即为逆时针旋转，正值是顺时针旋转。
        ObjectAnimator anim = ObjectAnimator.ofFloat(mImageView, "rotation", 0f, 180f);
        // 动画的持续时间，执行多久？
        anim.setDuration(durationMillis);
        // 回调监听
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (Float) animation.getAnimatedValue();
                DebugLog.d("addUpdateListener","openAnim-->"+value);
                if(animatorUpdateListener !=null)
                    animatorUpdateListener.CloseAnimatorUpdate(value);
                if(value ==180)
                    setOpenEndStyle("");
            }
        });
        // 正式开始启动执行动画
        anim.start();
    }


    /**
     * 展开后的样式
     * @param text 展开后的文本
     */
    public void setOpenEndStyle(String text){
        mImageView.setImageResource(R.drawable.icon_shou);
        mLinearLayout.setBackground(getResources().getDrawable(R.drawable.bg_morecar_orange));
        mTextView.setTextColor(getResources().getColor(R.color.text_color_orange));
        if(TextUtils.isEmpty(text))
            mTextView.setText("收起");
        else
            mTextView.setText(text);
    }

    /**
     * 收起后的样式
     * @param text 收起后文本
     */
    public void setCloseEndStyle(String text){
        mImageView.setImageResource(R.drawable.icon_zhan);
        mLinearLayout.setBackground(getResources().getDrawable(R.drawable.bg_morecar_gray));
        mTextView.setTextColor(getResources().getColor(R.color.text_gray));
        if(TextUtils.isEmpty(text))
         mTextView.setText("展开");
        else
         mTextView.setText(text);
    }

}
