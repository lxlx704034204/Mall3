package com.hxqc.mall.thirdshop.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.hxqc.mall.photolibrary.fragment.BaseImagePagerFragment;
import com.hxqc.mall.thirdshop.control.T_NewImageViewControl;
import com.hxqc.mall.thirdshop.model.promotion.AttachmentImageNewsModel;

import java.util.ArrayList;

/**
 * Author: wanghao
 * Date: 2015-12-09
 * FIXME  各种类似新闻资讯查看大图
 * Todo
 */
public class NewsImagesViewFragment extends BaseImagePagerFragment {

    ArrayList< AttachmentImageNewsModel > attachments;
    int position;

    T_NewImageViewControl control;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        control = T_NewImageViewControl.getInstance();
        attachments = control.getAttachments();
        position = control.getPosition();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewPager.setCurrentItem(position);
    }

    @Override
    public int getPageSize() {
        return attachments != null ? attachments.size() : 0;
    }

    @Override
    public String getShowPagePath(int position) {
        return attachments.get(position).url;
    }

    @Override
    public int getCreateRootViewTag() {
        return BaseImagePagerFragment.NEWS_VIEW_PAGE;
    }

    @Override
    public String fragmentDescription() {
        return "咨询大图详细信息查看";
    }
}
