package com.hxqc.autonews.widget;

import android.content.Context;
import android.graphics.Color;
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
 * Todo 图集类型的汽车资讯itemview
 */

public class InfoImagesItem extends LinearLayout {
    private TextView itemTitle;
    private TextView itemDate;
    private TextView itemType;
    private ImageView img1, img2, img3;
    private TextView gTitle;

    private Context mContext;
    private static final String READ_COLOR = "#952a2a2a";
    public InfoImagesItem(Context context) {
        this(context, null);
    }

    public InfoImagesItem(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public InfoImagesItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        LayoutInflater.from(context).inflate(R.layout.item_auto_information_images, this);
        gTitle = (TextView) findViewById(R.id.group_title);
        img1 = (ImageView) findViewById(R.id.img1);
        img2 = (ImageView) findViewById(R.id.img2);
        img3 = (ImageView) findViewById(R.id.img3);
        itemTitle = (TextView) findViewById(R.id.item_title);
        itemType = (TextView) findViewById(R.id.item_type);
        itemDate = (TextView) findViewById(R.id.item_date);
    }

    public void addData(final AutoInformation information) {
        if (information == null) {
            return;
        }
        ArrayList<String> thumbImages = information.thumbImage;
        itemTitle.setText(information.title);
        itemTitle.setTextColor(information.isRead() ? Color.parseColor(READ_COLOR) : Color.parseColor("#2a2a2a"));
        itemDate.setText(DateUtil.fullDate2Day(information.date));
        if (information.tags != null) {
            setTypeTags(itemType, information.tags);
        }
        if (thumbImages != null && thumbImages.size() > 2) {
            ImageUtil.setImage(mContext, img1, thumbImages.get(0));
            ImageUtil.setImage(mContext, img2, thumbImages.get(1));
            ImageUtil.setImage(mContext, img3, thumbImages.get(2));
        } else {
            ImageUtil.setImage(mContext, img1, "");
            ImageUtil.setImage(mContext, img2, "");
            ImageUtil.setImage(mContext, img3, "");
        }
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
        itemTitle.setTextColor(Color.parseColor(READ_COLOR));
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
