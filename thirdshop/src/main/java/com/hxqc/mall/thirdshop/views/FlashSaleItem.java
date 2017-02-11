package com.hxqc.mall.thirdshop.views;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hxqc.mall.core.util.ImageUtil;
import com.hxqc.mall.core.util.OtherUtil;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.model.SingleSeckillItem;
import com.hxqc.mall.thirdshop.utils.ActivitySwitcherThirdPartShop;
import com.hxqc.util.DateUtil;

/**
 * Author:李烽
 * Date:2016-05-04
 * FIXME
 * Todo 限时特价车item
 */
public class FlashSaleItem extends LinearLayout implements View.OnClickListener {
    private final CountDownView_1 countdownbar;
    private final TextView originalPrice;
    private final TextView originalPrice_1;
    private final TextView salePrice;
    private final TextView itemTitle;
    private final ImageView itemIcon;
    private final TextView mark;
    private final AreaLabel mAreaLabel;
    private SingleSeckillItem item;

    private final TextView modelName;

    public FlashSaleItem(Context context) {
        this(context, null);
    }

    public FlashSaleItem(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlashSaleItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.item_flash_sale, this);
        countdownbar = (CountDownView_1) findViewById(R.id.count_down_bar);
        originalPrice = (TextView) findViewById(R.id.original_price);
        originalPrice_1 = (TextView) findViewById(R.id.original_price_1);
        salePrice = (TextView) findViewById(R.id.sale_price);
        itemTitle = (TextView) findViewById(R.id.item_title);
        itemIcon = (ImageView) findViewById(R.id.item_icon);
        mark = (TextView) findViewById(R.id.mark);
        mAreaLabel = (AreaLabel) findViewById(R.id.area_label);
        modelName = (TextView) findViewById(R.id.model_name);
        setOnClickListener(this);
    }


    public void addData(final SingleSeckillItem item) {
        this.item = item;
        salePrice.setText(OtherUtil.amountFormat(item.itemPrice, true));
        originalPrice.setText(OtherUtil.amountFormat(item.itemOrigPrice, true));
        originalPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);//中间划横线
        originalPrice_1.setText(OtherUtil.amountFormat(item.itemOrigPrice, true));
        originalPrice_1.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);//中间划横线
        modelName.setText(item.itemName);
        itemTitle.setText(getTitle(item));
        ImageUtil.setImage(getContext(), itemIcon, item.itemThumb);
        mAreaLabel.setArea(item.salesArea);
        long startTime = DateUtil.getMillTime(item.startTime);
        long endTime = DateUtil.getMillTime(item.endTime);

//          long  now = DateUtil.getTime(item.serverTime);
//        DebugLog.i("vioson", "startTime:" + startTime + "|" + item.startTime
//                + "/endTime:" + endTime + "|" + item.endTime + "/" + countdownbar.isRunning());
        if (!countdownbar.isRunning()) {
//            DebugLog.i("vioson", countdownbar.isRunning() + "/");
            countdownbar.setStartAndEndTime(startTime, endTime);
            countdownbar.countdownStart();
        }
    }


    private SpannableStringBuilder getTitle(SingleSeckillItem item) {
        SpannableStringBuilder builder = null;
        if (item.itemFall > 0) {
            String str = String.format("【直降%s元】%s", item.itemFall, item.title);
            builder = new SpannableStringBuilder(str);
            ForegroundColorSpan orangeSpan = new ForegroundColorSpan(Color.parseColor("#E8585D"));
            ForegroundColorSpan blackSpan = new ForegroundColorSpan(Color.parseColor("#3F3F3F"));

            builder.setSpan(orangeSpan, 0, str.indexOf("】") + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            builder.setSpan(blackSpan, str.indexOf("】") + 1, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else {
            builder = new SpannableStringBuilder(item.title);
        }
        return builder;
    }

    @Override
    public void onClick(View v) {
        //跳转到特卖详情
        ActivitySwitcherThirdPartShop.toSpecialCarDetail(getContext(), item.itemID);
    }

    public void onDestroy() {
        countdownbar.onDestroy();
    }
}
