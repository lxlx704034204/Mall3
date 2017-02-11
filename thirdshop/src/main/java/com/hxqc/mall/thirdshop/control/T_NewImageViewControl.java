package com.hxqc.mall.thirdshop.control;


import com.hxqc.mall.thirdshop.model.promotion.AttachmentImageNewsModel;

import java.util.ArrayList;

/**
 * Author: wanghao
 * Date: 2015-12-09
 * FIXME 查看大图数据
 * Todo
 */
public class T_NewImageViewControl {

    protected static T_NewImageViewControl mInstance;

    ArrayList< AttachmentImageNewsModel > attachments;
    int position;

    protected T_NewImageViewControl() {
    }

    public static T_NewImageViewControl getInstance() {
        if (mInstance == null) {
            synchronized (T_NewImageViewControl.class) {
                if (mInstance == null) {
                    mInstance = new T_NewImageViewControl();
                }
            }
        }
        return mInstance;
    }

    public ArrayList< AttachmentImageNewsModel > getAttachments() {
        return attachments;
    }

    public int getPosition() {
        return position;
    }

    public void setAttachments(ArrayList< AttachmentImageNewsModel > attachments) {
        this.attachments = attachments;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void onDestroy() {
        mInstance = null;
    }

}
