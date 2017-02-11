package com.hxqc.autonews.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.hxqc.autonews.model.pojos.AutoInformation;

import hxqc.mall.R;

/**
 * Author:李烽
 * Date:2016-10-27
 * FIXME
 * Todo 汽车资讯列表的itemView
 */

public class AutoInfoItem extends LinearLayout {

    private Context mContext;
    private InfoTextItem textItem;
    private InfoImagesItem imgsItem;

    public AutoInfoItem(Context context) {
        this(context, null);
    }

    public AutoInfoItem(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AutoInfoItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        LayoutInflater.from(context).inflate(R.layout.item_auto_information, this);
        textItem = (InfoTextItem) findViewById(R.id.text_item);
        imgsItem = (InfoImagesItem) findViewById(R.id.imgs_item);
    }

    /**
     * 添加数据
     * @param information
     */
    public void addData(final AutoInformation information) {
        if (information != null) {
            AutoInformation.Type type = information.getType();
            if (type == AutoInformation.Type.Images) {
                textItem.setVisibility(GONE);
                imgsItem.setVisibility(VISIBLE);
                imgsItem.addData(information);
            } else {
                textItem.setVisibility(VISIBLE);
                imgsItem.setVisibility(GONE);
                textItem.addData(information);
            }
        }
    }

//    public void hide() {
//        textItem.hide();
//        imgsItem.hide();
//    }

}
