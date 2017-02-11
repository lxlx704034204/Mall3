package com.hxqc.xiaoneng;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.hxqc.security.Base64Util;
import com.hxqc.util.DebugLog;
import com.hxqc.util.JSONUtils;
import com.xiaoneng.xnchatui.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cn.xiaoneng.coreapi.ChatParamsBody;
import cn.xiaoneng.coreapi.TrailActionBody;
import cn.xiaoneng.uiapi.IXNSDK;
import cn.xiaoneng.uiapi.Ntalker;
import cn.xiaoneng.uiapi.XNSDKListener;
import cn.xiaoneng.utils.CoreData;
import cn.xiaoneng.utils.MD5Util;

/**
 * Author :liukechong
 * Date : 2016-01-19
 * FIXME
 * Todo
 */

public class ChatManager {
    private static final String TAG = "ChatManager";
    private static final String siteid = "kf_9149";
    //	static String siteid = "9149";
    private static final String sdkkey = "BEFB53EB-7492-4DD8-BB89-2D7AEC3F09CC";
    //与后台通讯加密md5
    private static final String md5 = "hjd89s8933m-61df)8%0f09";
    //正式  kf_9149_1452564424328
    //测试客服组  kf_9149_1452563900179
    private static final String settingid = "kf_9149_1452563900179";// 客服组id

    private static ChatManager chatManager;
    //    int initSDK = -1;
    //    int enableDebug = -1;
    int logIn = -1;
    int logOut = -1;
    int startChat = -1;
    int action = -1;
    int destory = -1;
    String groupName = null;// 客服组名称
    String kfuid = null; //"kefu03";// 指定客服id
    String kfname = null;//"移动测试3";// 指定客服昵称
    ArrayList< UnReadMessageListener > list = new ArrayList<>();
    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == 100) {
                Bundle data = msg.getData();
                String unMessage = data.getString("unMessage");
                int count = data.getInt("count");
                long time = data.getLong("time");
                boolean isRead = data.getBoolean("is_read");
//                DebugLog.d(TAG, "handleMessage() called with: " + unmessage + "msg = [" + time + "]");


                if (list != null) {
                    for (UnReadMessageListener listener : list) {
                        if (listener != null) {
                            listener.unReadMessage(unMessage, time, count, isRead);
                        }
                    }
                }

                return true;
            } else {
                return false;
            }
        }
    });
    String userID = null;
    String userName = null;
    int userLever = 0;
    ChatParamsBody mGoodChatParams;
    private boolean isDebug = false;
    private IXNSDK xnSDK;
    private Context context = null;
    private boolean notify = false;
    private XNSDKListener mXnsdkListener = new XNSDKListener() {
        @Override
        public void onChatMsg(boolean b, String s, String s1, String s2, String s3, long l) {
//            Toast.makeText(context, s+"---"+s1+"----"+s2+"---"+s3, Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onUnReadMsg(String s, String s1, String s2, String s3, int i) {
            DebugLog.d(TAG, "onUnReadMsg() called with: " + "s = [" + s + "], s1 = [" + s1 + "], s2 = [" + s2 + "], " +
                    "s3 = [" + s3 + "], i = [" + i + "]");

            if (TextUtils.isEmpty(s3) || i == 0) {
                return;
            }
            if (notify) {
                setNotification(s3);
            }
            long l = System.currentTimeMillis();


            SharedPreferences.Editor edit = getSharedPreferenceEditor().edit();
            edit.putInt("count", i);
            edit.putLong("time", l);
            edit.putString("unMessage", s3);
            edit.putBoolean("is_read", false);
            edit.apply();

            Message message = Message.obtain();
            Bundle bundle = new Bundle();
            bundle.putInt("count", i);
            bundle.putLong("time", l);
            bundle.putString("unMessage", s3);
            bundle.putBoolean("is_read", false);
            message.setData(bundle);
            message.what = 100;
            handler.sendMessage(message);
        }

        @Override
        public void onClickMatchedStr(String s, String s1) {

//            Toast.makeText(context, s+"---"+s1, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onClickShowGoods(int i, int i1, String s, String s1, String s2, String s3, String s4, String s5) {
//            Toast.makeText(context, s+"---"+s1+"----"+s2+"---"+s3+"----"+s4+"---"+s5, Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onError(int i) {

        }
    };

    private ChatManager() {
        xnSDK = Ntalker.getInstance();
    }

    public static ChatManager getInstance() {
        if (chatManager == null) {
            chatManager = new ChatManager();
        }
        return chatManager;
    }

    private SharedPreferences getSharedPreferenceEditor() {
        return context.getSharedPreferences("HxqcChatMessage" + (TextUtils.isEmpty(userID) ? "" : userID), Context.MODE_PRIVATE);
    }

    public ChatManager setDebug(boolean debug) {
        isDebug = debug;
        return chatManager;
    }

    /**
     * 标记已读
     */
    public void markRead() {
        getSharedPreferenceEditor().edit().putBoolean("is_read", true).apply();
    }

    /**
     * 开启未读消息通知栏通知,默认关闭
     *
     * @param notify
     */
    public void setNotifyOpen(boolean notify) {
        this.notify = notify;
    }

    private void setNotification(String message) {
        NotificationManager systemService = (NotificationManager) context.getSystemService(Context
                .NOTIFICATION_SERVICE);

        Notification.Builder builder = new Notification.Builder(context);

//        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setDefaults(Notification.DEFAULT_ALL);
        builder.setContentText(message);
        builder.setContentTitle("未读消息");
        builder.setSmallIcon(R.drawable.visitor);
        builder.setAutoCancel(true);
        Intent intent = new Intent();
        intent.setAction("com.hxqc.chatManager");
        PendingIntent pi = PendingIntent.getBroadcast(context, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pi);
        systemService.notify(100, builder.build());

    }

    /**
     * 添加未读消息监听
     *
     * @param unMessageListener
     */
    public void addUnReadMessageListener(UnReadMessageListener unMessageListener) {
        if (list == null) {
            list = new ArrayList<>();
        }
        if (unMessageListener != null) {
            list.add(unMessageListener);
        }
    }

    /**
     * 移除未读消息监听
     *
     * @param unMessageListener
     */
    public void removeUnReadMessageListener(UnReadMessageListener unMessageListener) {
        list.remove(unMessageListener);
    }

    /**
     * 用户注销
     */
    public void userLogout() {
        xnSDK.logout();
    }

    /**
     * 订单咨询
     *
     * @param orderID
     *         订单id
     * @param loginName
     *         客户的电话号码
     * @param startPageTitle
     *         启动聊天时,客服看到的提示信息标题
     * @param startPageUrl
     *         启动聊天时,客服点击标题跳转的url
     */
    public void startChatWithOrder(String orderID, String loginName,
                                   String startPageTitle, String startPageUrl) {

        if (logIn != 0) {
            userLogin(context, userID, userName, userLever);
        }
        ChatParamsBody chatParams = new ChatParamsBody();
        DebugLog.d(TAG, "startChatWithOrder() called with: " + "orderID = [" + orderID + "], loginName = [" +
                loginName + "], startPageTitle = [" + startPageTitle + "], startPageUrl = [" + startPageUrl + "]");
        chatParams.startPageUrl = startPageTitle;
        chatParams.startPageTitle = startPageUrl;
        String encode = MD5Util.encode((orderID + md5));
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("login_name", loginName);
            jsonObject.put("order_id", orderID);
            jsonObject.put("md5", encode);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        chatParams.erpParam = Base64Util.encode(jsonObject.toString().getBytes());
//        chatParams.itemparams.appgoodsinfo_type = CoreData.SHOW_GOODS_BY_WIDGET;
//        chatParams.itemparams.clientgoodsinfo_type = CoreData.SHOW_GOODS_BY_ID;//SHOW_GOODS_BY_URL//SHOW_GOODS_BY_ID
//        chatParams.itemparams.clicktoshow_type = CoreData.CLICK_TO_APP_COMPONENT; //CLICK_TO_SDK_EXPLORER,点击回调
        DebugLog.d(TAG, "encode:" + encode + "--------json:" + jsonObject.toString() + "-----startChatWithOrder: " +
                chatParams.erpParam);
        startChat(chatParams, groupName);

    }

    /**
     * 商品咨询
     *
     * @param goodsID
     *         商品ID (手机标题栏显示)
     * @param goodsName
     *         商品名字 (手机标题栏显示)
     * @param goodsPrice
     *         商品价格 (手机标题栏显示)
     * @param goodsImageURL
     *         商品图片url (手机标题栏显示)
     * @param startPageTitle
     *         启动聊天时,客服看到的提示信息标题
     * @param startPageUrl
     *         启动聊天时,客服点击标题跳转的url
     */
    public void startChatWithGoods(String goodsID, String goodsName, String goodsPrice, String goodsImageURL, String
            startPageTitle, String startPageUrl) {
        ChatParamsBody chatParams = new ChatParamsBody();
        /*-------------------------商品ID ,前面加A_-----------------------*/
        chatParams.itemparams.goods_id = "A_" + goodsID;
        chatParams.itemparams.goods_name = goodsName;
        chatParams.itemparams.goods_price = goodsPrice;
        chatParams.itemparams.goods_image = goodsImageURL;

        DebugLog.d(TAG, "startChatWithGoods() called with: " + "goodsID = [" + goodsID + "], goodsName = [" +
                goodsName + "], goodsPrice = [" + goodsPrice + "], goodsImageURL = [" + goodsImageURL + "], " +
                "startPageTitle = [" + startPageTitle + "], startPageUrl = [" + startPageUrl + "]");
        /*启动聊天时候,给客服看到的提示*/
        chatParams.startPageUrl = startPageTitle;
        chatParams.startPageTitle = startPageUrl;

        chatParams.itemparams.appgoodsinfo_type = CoreData.SHOW_GOODS_BY_WIDGET;
        chatParams.itemparams.clientgoodsinfo_type = CoreData.SHOW_GOODS_BY_ID;//SHOW_GOODS_BY_URL//SHOW_GOODS_BY_ID
        chatParams.itemparams.clicktoshow_type = CoreData.CLICK_TO_APP_COMPONENT; //CLICK_TO_SDK_EXPLORER,点击回调

        SharedPreferences.Editor edit = getSharedPreferenceEditor().edit();
        edit.putString("ChatParamsBody", JSONUtils.toJson(chatParams));
        edit.apply();

        startChat(chatParams, groupName);
    }

    public void startHistoryGoods(String startPageTitle, String startPageUrl) {
        String chatParamsBodyString = getSharedPreferenceEditor().getString("ChatParamsBody", "");
        ChatParamsBody chatParams = null;
        if (!TextUtils.isEmpty(chatParamsBodyString)) {
            chatParams = JSONUtils.fromJson(chatParamsBodyString, ChatParamsBody.class);
        } else {
            chatParams = new ChatParamsBody();
            chatParams.itemparams.appgoodsinfo_type = CoreData.SHOW_GOODS_NO;
        }
        chatParams.startPageUrl = startPageTitle;
        chatParams.startPageTitle = startPageUrl;
        startChat(chatParams, groupName);
    }

    /**
     * 非商品页咨询
     */
    public void startChatWithNothing() {
        ChatParamsBody chatParams = new ChatParamsBody();
        chatParams.itemparams.appgoodsinfo_type = CoreData.SHOW_GOODS_NO;
        chatParams.itemparams.clicktoshow_type = CoreData.SHOW_GOODS_NO;
        startChat(chatParams, groupName);

    }


    /**
     * 初始化小能SDK,并选择Debug模式
     *
     * @param context
     *         上下文
     */
    public boolean initChatSDK(Context context) {
        this.context = context;
        int initSDK = xnSDK.initSDK(context, siteid, sdkkey);// 初始化SDK

        Ntalker.getInstance().setSDKListener(mXnsdkListener);
        if (0 == initSDK) {
            DebugLog.d(TAG, "初始化SDK成功");
        } else {
            DebugLog.d(TAG, "初始化SDK失败，错误码:" + initSDK);
        }
        // 选择debug模式
        int enableDebug = Ntalker.getInstance().enableDebug(isDebug);
        if (0 == enableDebug) {
            DebugLog.d(TAG, "执行成功");
        } else {
            DebugLog.d(TAG, "执行失败，错误码:" + enableDebug);
        }
        return initSDK == 0;
    }

    /**
     * 销毁SDK
     */
    public void destroySDK() {
        xnSDK.removeSDKListener(mXnsdkListener);
        xnSDK.destroy();
    }

    /**
     * 清除缓存
     */
    public void clearCache() {
        xnSDK.clearCache();
    }

    /**
     * 关掉所有聊天窗口
     */
    public void closeAllChatWindow() {
        xnSDK.destroyAllChatSession();
    }

    /**
     * 匿名登录
     *
     * @return
     */
    public int userLogin(Context context) {
        if (initChatSDK(context)) {
//            throw new RuntimeException("未初始化ChatSDK,或初始化失败");
        }
        this.userID = null;
        this.userName = null;
        this.userLever = 0;
        return userLogin(context, null, null, 0);

    }

    /**
     * @param userID
     *         用户id
     * @param userName
     *         用户名
     * @param userLever
     *         用户等级
     * @return login    0	成功
     * 401	传入userId错误
     * 402	传入userName错误
     * 403	传入userLevel错误
     * 404	登录失败
     */
    public int userLogin(Context context, String userID, String userName, int userLever) {
        DebugLog.d(TAG, "userLogin() called with: " + "userID = [" + userID + "], userName = [" + userName + "], " +
                "userLever = [" + userLever + "]");
        if (initChatSDK(context)) {
//            throw new RuntimeException("未初始化ChatSDK,或初始化失败");
        }
        this.userID = userID;
        this.userName = userName;
        this.userLever = userLever;
        logIn = Ntalker.getInstance().login(userID, userName, userLever);

        if (logIn == 0) {
            DebugLog.d(TAG, "userLogin: 登录成功");
        } else {
            DebugLog.d(TAG, "userLogin: 登录失败");
        }
        DebugLog.d(TAG, "userLogin() returned: " + logIn);

        if (logIn == 402) {
            throw new RuntimeException("402错误码：userName不能超过32位；userName只能是字母、汉字、数字、_、@的组合");
        }
        return logIn;

    }


    /**
     * @param chatParams
     * @return
     */
    private int startChat(ChatParamsBody chatParams, String groupName) {
        DebugLog.d(TAG, "startChat: " + chatParams.erpParam + chatParams.itemparams.goods_id);
        if (TextUtils.isEmpty(groupName)) {
            groupName = "恒信客服";
        }
        return startChat(chatParams, groupName, kfuid, kfname);
    }

    /**
     * @param chatParams
     *         启动聊天参数
     * @param groupName
     *         聊天窗口标题
     * @param kfuid
     *         客服id,默认为null
     * @param kfname
     *         客服名字,默认为null
     * @return 0成功; 601传入sellerid错误; 602传入settingId错误;604打开聊窗失败
     */
    public int startChat(ChatParamsBody chatParams, String groupName, String kfuid, String kfname) {
        startChat = xnSDK.startChat(
                context, settingid, groupName, kfuid, kfname,
                chatParams, false);
        if (0 == startChat) {
            DebugLog.d(TAG, "打开聊窗成功");
        } else {
            DebugLog.d(TAG, "打开聊窗失败，错误码:" + startChat);
        }
        return startChat;
    }

    /**
     * 上传用户浏览轨迹
     *
     * @param trailParams
     *         自定义所有内容
     */
    public int upLoadTrack(TrailActionBody trailParams) {
        int errorCode = xnSDK.startAction(trailParams);
        if (0 == errorCode) {
            DebugLog.d(TAG, "上传轨迹成功");
        } else {
            DebugLog.d(TAG, "上传轨迹失败，错误码:" + errorCode);
        }
        return errorCode;
    }

    /**
     * 上传用户浏览轨迹
     *
     * @param title
     *         浏览的本页标题
     * @param url
     *         本页的url
     */
    public void upLoadTrack(String title, String url) {
        TrailActionBody trailParams = new TrailActionBody();
        trailParams.ttl = title;
        trailParams.url = url;
        upLoadTrack(trailParams);
    }
}
