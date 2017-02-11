package com.hxqc.socialshare.manager;

import android.content.Context;
import android.content.Intent;

import com.hxqc.socialshare.R;
import com.hxqc.socialshare.pojo.ShareContent;
import com.hxqc.socialshare.view.SharePopupWindow;
import com.umeng.socialize.UMShareListener;

/**
 * Author:胡俊杰
 * Date: 2015/9/14
 * FIXME
 * Todo
 */
public class ShareController {
    SharePopupWindow mSharePopupWindow;

    ShareManager shareManager;

    public ShareController(Context context) {
        shareManager = new ShareManager(context);
        mSharePopupWindow = new SharePopupWindow(context, R.style.FullWidthDialog);
    }
    public void onActivityResult(Context context,int requestCode, int resultCode, Intent data) {
        /**使用SSO授权必须添加如下代码 */
        shareManager.onActivityResult(context,requestCode, resultCode, data);

    }
    public void showSharePopupWindow(ShareContent shareContent) {
        if (shareContent == null) return;
        mSharePopupWindow.showShare(shareManager, shareContent);
    }

    public void setShareCallBack(UMShareListener umShareListener) {

    }

}
