
package com.hxqc.mall.activity.auto;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hxqc.mall.core.model.auto.AutoDetail;
import com.hxqc.mall.core.model.auto.AutoItem;
import com.hxqc.mall.core.model.auto.Promotion;
import com.hxqc.mall.core.views.ColorSquare;
import com.hxqc.mall.views.auto.AutoPromotionInformationViewGroup;

import hxqc.mall.R;

/**
 * Author: HuJunJie
 * Date: 2015-04-07
 * FIXME
 * Todo 特卖详情
 */
public class AutoItemDetailPromotionActivity extends AutoItemDetailActivity {
    AutoPromotionInformationViewGroup mInformationViewGroup;
    ColorSquare mAppearanceSquareView;
    ColorSquare mInteriorSquareView;
    //    View mNoticeView;
    TextView mTimeView;//倒计时
    CountDownTimer countDownTimer;
    ProgressBar mProgressBar;

    @Override
    int getContentView() {
        return R.layout.activity_auto_item_promotion_detail;
    }

    @Override
    public String getItemType() {
        return AutoItem.AUTO_PROMOTION;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mInformationViewGroup = (AutoPromotionInformationViewGroup) findViewById(R.id.promotion_information_view_group);
        mAppearanceSquareView = (ColorSquare) findViewById(R.id.color_square_appearance);
        mInteriorSquareView = (ColorSquare) findViewById(R.id.color_square_interior);
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        mTimeView = (TextView) findViewById(R.id.promotion_time);

    }

    @Override
    public void onItemDetailSuccess(final AutoDetail autoDetail) {
        super.onItemDetailSuccess(autoDetail);

        mInformationViewGroup.setInformation(autoDetail);
        mAppearanceSquareView.setColors(autoDetail.getItemColor());
        mInteriorSquareView.setColors(autoDetail.getItemInterior());
        startCountDown(autoDetail);
    }

    /**
     * 开启倒计时
     *
     * @param autoDetail
     */
    private void startCountDown(final AutoDetail autoDetail) {
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
        countDownTimer = new CountDownTimer(autoDetail.getPromotion().getEndTimeLong(), 1000) {
            public void onTick(long millisUntilFinished) {
                setBottomBarViewStatus(autoDetail);
                Promotion promotion = autoDetail.getPromotion();
                promotion.serverL++;
                String a = promotion.reckonByTime(AutoItemDetailPromotionActivity.this);
                showProgress(promotion);
                mTimeView.setText(a);
            }

            public void onFinish() {
                Promotion promotion = autoDetail.getPromotion();
                promotion.setEndPromotion();
                mTimeView.setText(getString(R.string.promotion_is_end_title));
            }
        }.start();
    }

    /**
     * 设置按钮状态
     */
    private void setBottomBarViewStatus(AutoDetail autoDetail) {
        switch (autoDetail.transactionStatus()) {
            case UNAVAILABLE:
                autoAvailable(true);
                break;
            case PREPARE:
                autoAvailable(false);
                bottomBarToBuyView.setNotCanBuy(getString(R.string.buy_it_prepare));
                bottomBarToBuyView.setEnabled(false);
                break;
            case NORMAL:
                autoAvailable(false);
                bottomBarToBuyView.setCanBuy(getString(R.string.buy_it_now_promotion));
                break;
            case SELLOUT:
                autoAvailable(false);
                bottomBarToBuyView.setNotCanBuy(getString(R.string.promotion_sell_out));
                bottomBarToBuyView.setEnabled(false);
                break;
            case END:
                autoAvailable(false);
                bottomBarToBuyView.setNotCanBuy(getString(R.string.promotion_is_end));
                bottomBarToBuyView.setEnabled(false);
                break;
        }
    }

    /**
     * 显示进度条
     *
     * @param promotion
     */
    private void showProgress(Promotion promotion) {
        mProgressBar.setMax((int) (promotion.getEndTimeLong() - promotion.getStartTimeLong()));
        if (promotion.getEndTimeLong() > promotion.serverL && promotion.getStartTimeLong() < promotion.serverL) {
            try {
                mProgressBar.setProgress((int) (promotion.getEndTimeLong() - promotion.serverL));
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            mProgressBar.setProgress(0);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
    }
}
