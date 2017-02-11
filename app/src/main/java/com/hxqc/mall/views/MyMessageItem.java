package com.hxqc.mall.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import hxqc.mall.R;

/**
 * Author:李烽
 * Date:2016-01-21
 * FIXME
 * Todo 我的消息界面的横条
 */
public class MyMessageItem extends RelativeLayout {
    private TextView title;
    private TextView content;
    private TextView mark;
    private MarkableImageView icon;

    private String titleStr = "";
    private String contentStr = "";
    private String markStr = "";
    private int iconID = R.drawable.new_service_def;

    private MarkableImageView.IconState iconState = MarkableImageView.IconState.READ;

    public MyMessageItem(Context context) {
        this(context, null);
    }

    public MyMessageItem(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyMessageItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.view_my_message_item, this);
        init();
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MyMessageItem);
        titleStr = typedArray.getString(R.styleable.MyMessageItem_newsItemTitle);
        contentStr = typedArray.getString(R.styleable.MyMessageItem_newsItemContent);
        markStr = typedArray.getString(R.styleable.MyMessageItem_newsItemMark);
        iconID = typedArray.getResourceId(R.styleable.MyMessageItem_iconSrc, R.drawable.new_service_def);
        iconState = typedArray.getBoolean(R.styleable.MyMessageItem_iconState, false) ?
                MarkableImageView.IconState.UNREAD : MarkableImageView.IconState.READ;
        title.setText(titleStr);
        infData(contentStr, markStr, iconState);
        typedArray.recycle();
    }

    private void init() {
        title = (TextView) findViewById(R.id.message_item_title);
        content = (TextView) findViewById(R.id.message_item_content);
        mark = (TextView) findViewById(R.id.message_item_mark);
        icon = (MarkableImageView) findViewById(R.id.message_item_icon);
    }

    public void infData(String contentStr, String date, MarkableImageView.IconState iconState) {
        icon.setIcon(iconID);
        this.iconState = iconState;
        icon.markRead(iconState);
        content.setText(contentStr);
        mark.setText(getDate(date));
    }

    /**
     * 转换状态
     *
     * @param iconState
     */
    public void toggleState(MarkableImageView.IconState iconState) {
        if (this.iconState != iconState) {
            this.iconState = iconState;
            infData(contentStr, markStr, iconState);
        }
    }

    private String getDate(String date) {
        final long ONE_DAY = 1000 * 60 * 60 * 24;
        if (TextUtils.isEmpty(date))
            return dateFormat(date);

        if (date.length() > 10)
            date = date.substring(0, 10);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        long l = System.currentTimeMillis();
        String now = simpleDateFormat.format(new Date(l));
        if (now.equals(date))
            return "今天";

        long longTime = getLongTime(date);
        long longTime1 = getLongTime(now);

        if (longTime > 0 && longTime1 > 0) {
            long gap = longTime1 - longTime;
            if (gap > ONE_DAY)
                return dateFormat(date);
            else if (gap <= ONE_DAY && gap > 0)
                return "昨天";
            else if (gap < 0)
                return dateFormat(date);
            else return "今天";
        } else {
            return dateFormat(date);
        }

    }

    private String dateFormat(String date) {
        if (TextUtils.isEmpty(date))
            return "";
        else {
            date = date.replace("-", "/");
            try {
                date = date.substring(2);
                return date;
            } catch (StringIndexOutOfBoundsException e) {
                e.printStackTrace();
                return date;
            }
        }
    }

    /**
     * String时间格式转为long格式
     *
     * @param date
     * @return
     */
    private long getLongTime(String date) {
        if (TextUtils.isEmpty(date))
            return 0;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date parse = null;
        try {
            parse = simpleDateFormat.parse(date + " 00:00:00");
            return parse.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }


}
