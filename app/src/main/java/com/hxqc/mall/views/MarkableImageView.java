package com.hxqc.mall.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import hxqc.mall.R;

/**
 * Author:李烽
 * Date:2016-01-21
 * FIXME
 * Todo 可标记的imageView
 */
public class MarkableImageView extends RelativeLayout {
    private final ImageView mainIv;
    private ImageView markIv;
    private IconState iconState = IconState.READ;

    public MarkableImageView(Context context) {
        this(context, null);
    }

    public MarkableImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MarkableImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.view_markable_image_view, this);
        mainIv = (ImageView) findViewById(R.id.main_image_view);
        markIv = (ImageView) findViewById(R.id.mark_image_view);
        mark(iconState);
    }

    private void mark(IconState iconState) {
        switch (iconState) {
            case READ:
                markIv.setVisibility(GONE);
                break;
            case UNREAD:
                markIv.setVisibility(VISIBLE);
                break;
        }
    }

    /**
     * 改变状态
     *
     * @param iconState
     */
    public void markRead(IconState iconState) {
        this.iconState = iconState;
        mark(iconState);
    }

    public void setIcon(int iconID) {
        mainIv.setImageResource(iconID);
    }


    public enum IconState {
        READ, UNREAD
    }
}
