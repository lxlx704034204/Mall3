package com.hxqc.autonews.widget;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hxqc.autonews.model.pojos.AutoInformation;
import com.hxqc.autonews.model.pojos.InfoTag;
import com.hxqc.autonews.util.ListReadRecolder;
import com.hxqc.autonews.util.ToAutoInfoDetailUtil;
import com.hxqc.mall.core.util.ImageUtil;
import com.hxqc.util.DateUtil;

import java.util.ArrayList;
import java.util.List;

import hxqc.mall.R;

/**
 * Author:李烽
 * Date:2016-10-27
 * FIXME
 * Todo 图文类型的汽车资讯itemview
 */

public class InfoTextItem extends LinearLayout {
    private final TextView gTitle;
    private ImageView autoIcon;
    private TextView infoTitle;
    private TextView infoType;
    private TextView infoDate;

    private Context mContext;
    private static final String READ_COLOR = "#952a2a2a";
    public InfoTextItem(Context context) {
        this(context, null);
    }

    public InfoTextItem(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public InfoTextItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        LayoutInflater.from(context).inflate(R.layout.item_auto_information_text, this);
        gTitle = (TextView) findViewById(R.id.group_title);
        autoIcon = (ImageView) findViewById(R.id.auto_icon);
        infoDate = (TextView) findViewById(R.id.info_date);
        infoType = (TextView) findViewById(R.id.info_type);
        infoTitle = (TextView) findViewById(R.id.info_title);
    }

    public void addData(final AutoInformation information) {
        if (information == null) {
            return;
        }
        ArrayList<String> thumbImages = information.thumbImage;
        if (!TextUtils.isEmpty(information.title))
            infoTitle.setText(Html.fromHtml(information.title));
        infoTitle.setTextColor(information.isRead() ? Color.parseColor(READ_COLOR) : Color.parseColor("#2a2a2a"));
        infoDate.setText(DateUtil.fullDate2Day(information.date));
        if (information.tags != null) {
            setTypeTags(infoType, information.tags);
        }
        if (thumbImages != null && thumbImages.size() > 0) {
            ImageUtil.setImage(mContext, autoIcon, thumbImages.get(0));
        } else ImageUtil.setImage(mContext, autoIcon, "");
        setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToAutoInfoDetailUtil
                        .onToNextPage(information.infoID, information.getType(), mContext);
                ListReadRecolder.saveReadInfoId(information.infoID);
                isRead();
            }
        });
    }

    public void isRead() {
        infoTitle.setTextColor(Color.parseColor(READ_COLOR));
    }

    /**
     * 设置标签
     *
     * @param textView
     * @param tags
     */
    private void setTypeTags(TextView textView, List<InfoTag> tags) {
        StringBuilder stringBuilder = new StringBuilder();
        for (InfoTag tag : tags) {
            stringBuilder.append(tag.tagName).append(",");
        }
        String s = stringBuilder.toString();
        if (!TextUtils.isEmpty(s)) {
            s = s.substring(0, s.length() - 1);
            textView.setText(s);
        }
    }

//    public void hide() {
//        findViewById(R.id.info_line).setVisibility(GONE);
//    }

}
