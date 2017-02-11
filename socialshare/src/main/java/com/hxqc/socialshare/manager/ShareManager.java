package com.hxqc.socialshare.manager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import com.hxqc.socialshare.pojo.ShareContent;
import com.hxqc.socialshare.utils.AppUtil;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import java.util.Map;

/**
 * Author:胡俊杰
 * Date: 2015/8/27
 * FIXME
 * Todo
 */
public class ShareManager {
    protected Context context;
    UMShareListener umShareListener;

    public ShareManager(Context context) {
//        Log.LOG = false;
//        Config.IsToastTip = false;
        try {
            addWXShare(context.getApplicationContext());
            addQQQZonePlatform(context.getApplicationContext());
            addSina(context.getApplicationContext());
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }


    public void onActivityResult(Context context, int requestCode, int resultCode, Intent data) {
        /**使用SSO授权必须添加如下代码 */
        UMShareAPI.get(context.getApplicationContext()).onActivityResult(requestCode, resultCode, data);
    }

    public void setShareCallBack(UMShareListener umShareListener) {
        if (umShareListener == null) return;
        this.umShareListener = umShareListener;
    }

    /**
     * 添加微信
     */
    private void addWXShare(Context context) throws PackageManager.NameNotFoundException {
        String appId = AppUtil.getApplicationMetaDataString(context, "WX_APP_ID");
        String appSecret = AppUtil.getApplicationMetaDataString(context, "WX_APP_SECRET");
        PlatformConfig.setWeixin(appId, appSecret);
    }

    /**
     * 添加新浪
     */
    private void addSina(Context context) throws PackageManager.NameNotFoundException {
        String appId = String.valueOf(AppUtil.getApplicationMetaDataInt(context, "Weibo_App_ID"));
        String appSecret = AppUtil.getApplicationMetaDataString(context, "Weibo_APP_SECRET");
        PlatformConfig.setSinaWeibo(appId, appSecret);
    }

    /**
     * @return
     * @功能描述 : 添加QQ平台支持 QQ分享的内容， 包含四种类型， 即单纯的文字、图片、音乐、视频. 参数说明 : title, summary,
     * image url中必须至少设置一个, targetUrl必须设置,网页地址必须以"http://"开头 . title :
     * 要分享标题 summary : 要分享的文字概述 image url : 图片地址 [以上三个参数至少填写一个] targetUrl
     * : 用户点击该分享时跳转到的目标地址 [必填] ( 若不填写则默认设置为友盟主页 )
     */
    private void addQQQZonePlatform(Context context) throws PackageManager.NameNotFoundException {
        String appId = String.valueOf(AppUtil.getApplicationMetaDataInt(context, "QQ_APP_ID"));
        String appKey = AppUtil.getApplicationMetaDataString(context, "QQ_APP_KEY");
        PlatformConfig.setQQZone(appId, appKey);
    }


    private ShareAction prepareShareAction(Context context, ShareContent shareContent, SHARE_MEDIA var1) {
        return new ShareAction((Activity) context)
                .setCallback(new ShareListener())
                .withText(shareContent.getContent()).withTitle(shareContent.getTitle())
                .withTargetUrl(shareContent.getUrl())
                .withMedia(new UMImage(shareContent.getContext(), shareContent.getThumb()))
                .setPlatform(var1);
//        return new ShareAction((Activity) shareContent.getContext())
//                .setCallback(new ShareListener())
//                .withText("分享内容").withTitle("分享头")
//                .withTargetUrl("http://www.wukocar.com")
//                .withMedia(new UMImage(shareContent.getContext(), "http://img.sootuu.com/vector/200801/070/0477.jpg"))
//                .setPlatform(var1);
    }

    public void toWX(Context context, ShareContent shareContent) {
        SHARE_MEDIA platform = SHARE_MEDIA.WEIXIN;

        prepareShareAction(context, shareContent, platform).share();
    }

    public boolean toWXCircle(Context context, ShareContent shareContent) throws Exception {
        SHARE_MEDIA platform = SHARE_MEDIA.WEIXIN_CIRCLE;
        prepareShareAction(context, shareContent, platform).share();
        return true;
    }


    public boolean toWeibo(Context context, ShareContent shareContent) throws Exception {
        SHARE_MEDIA platform = SHARE_MEDIA.SINA;

//        if (!mShareAPI.isInstall((Activity) context, platform)) {
//            Toast.makeText(context, "您还没有安装新浪微博", Toast.LENGTH_SHORT).show();
//            return false;
//        }
        shareContent.setTitle("");
        shareContent.setUrl("");
        if (!UMShareAPI.get(context.getApplicationContext()).isAuthorize((Activity) context, platform)) {
            UMShareAPI.get(context.getApplicationContext()).doOauthVerify((Activity) context, platform, new
                    AuthListener(prepareShareAction(context, shareContent,
                    platform)));
            return false;
        }
        prepareShareAction(context, shareContent, platform).share();
        return true;
    }

    public boolean toQQ(Context context, ShareContent shareContent) throws Exception {
        SHARE_MEDIA platform = SHARE_MEDIA.QQ;
//        if (!mShareAPI.isInstall((Activity) context, platform)) {
//            Toast.makeText(context, "您还没有安装QQ", Toast.LENGTH_SHORT).show();
//            return false;
//        }
        if (!UMShareAPI.get(context.getApplicationContext()).isAuthorize((Activity) context, platform)) {
            UMShareAPI.get(context.getApplicationContext()).doOauthVerify((Activity) context, platform, new
                    AuthListener(prepareShareAction(context, shareContent,
                    platform)));
            return false;
        }
        prepareShareAction(context, shareContent, platform).share();
        return true;
    }

    // 设置QQ空间分享内容
    public boolean toQQZone(Context context, ShareContent shareContent) throws Exception {
        SHARE_MEDIA platform = SHARE_MEDIA.QQ;
//        if (!mShareAPI.isInstall((Activity) context, platform)) {
//            Toast.makeText(context, "您还没有安装QQ", Toast.LENGTH_SHORT).show();
//            return false;
//        }
        if (!UMShareAPI.get(context.getApplicationContext()).isAuthorize((Activity) context, platform)) {
            UMShareAPI.get(context.getApplicationContext()).doOauthVerify((Activity) context, platform, new
                    AuthListener(prepareShareAction(context, shareContent,
                    platform)));
            return false;
        }
        prepareShareAction(context, shareContent, platform).share();
        return true;
    }

    public boolean toSms(Context context, ShareContent shareContent) throws Exception {
        SHARE_MEDIA platform = SHARE_MEDIA.SMS;
        prepareShareAction(context, shareContent, platform).share();
        return true;
    }

    public boolean toEmail(Context context, ShareContent shareContent) throws Exception {
        SHARE_MEDIA platform = SHARE_MEDIA.EMAIL;
        // 设置邮件分享内容， 如果需要分享图片则只支持本地图片
        prepareShareAction(context, shareContent, platform).withMedia(new UMImage(shareContent.getContext(),
                shareContent
                        .getDefaultResource()))
                .setPlatform(SHARE_MEDIA.EMAIL)
                .share();
        return true;
    }

    class ShareListener implements UMShareListener {

        @Override
        public void onResult(SHARE_MEDIA share_media) {
//            Log.i("Tag", "onResult" + share_media.toString());
        }

        @Override
        public void onError(SHARE_MEDIA share_media, Throwable throwable) {
//            Log.e("Tag", "onError" + throwable.toString());
        }

        @Override
        public void onCancel(SHARE_MEDIA share_media) {
//            Log.d("Tag", "onCancel" + share_media.toString());
        }
    }

    class AuthListener implements UMAuthListener {
        ShareAction shareAction;

        public AuthListener(ShareAction shareAction) {
            this.shareAction = shareAction;
        }

        @Override
        public void onComplete(SHARE_MEDIA share_media, int action, Map< String, String > map) {
            shareAction.share();
//            Log.i("Tag", "授权成功  ");
        }

        @Override
        public void onError(SHARE_MEDIA share_media, int action, Throwable throwable) {
//            Log.i("Tag", "授权onError  ");
        }

        @Override
        public void onCancel(SHARE_MEDIA share_media, int action) {
//            Log.i("Tag", "授权onCancel  ");
        }
    }

}
