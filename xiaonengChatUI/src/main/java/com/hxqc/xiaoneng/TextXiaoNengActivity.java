package com.hxqc.xiaoneng;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.hxqc.security.Base64Util;

import org.json.JSONException;
import org.json.JSONObject;

import cn.xiaoneng.coreapi.ChatParamsBody;
import cn.xiaoneng.uiapi.Ntalker;
import cn.xiaoneng.uiapi.XNSDKListener;
import cn.xiaoneng.utils.CoreData;
import cn.xiaoneng.utils.MD5Util;

/**
 * Author :liukechong
 * Date : 2016-01-16
 * FIXME
 * Todo
 */
public class TextXiaoNengActivity extends AppCompatActivity implements XNSDKListener{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Button button = new Button(this);
        button.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        button.setText("开始聊天");
        setContentView(button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startChat();
            }
        });


        initXiaoNengApi();
    }

    static String siteid = "kf_9149";
    //	static String siteid = "9149";
    static String sdkkey = "BEFB53EB-7492-4DD8-BB89-2D7AEC3F09CC";

    int initSDK = 0;
    int enableDebug = 0;
    int logIn = 0;
    int logOut = 0;
    int startChat = 0;
    int action = 0;
    int destory = 0;

    private void initXiaoNengApi(){
        /*************************************** 在onCreate中初始化SDK相关方法如下 *************************************/
        initSDK = Ntalker.getInstance().initSDK(getApplicationContext(),
                siteid, sdkkey);// 初始化SDK
        if (0 == initSDK) {
            Log.e("initSDK", "初始化SDK成功");
        } else {
            Log.e("initSDK", "初始化SDK失败，错误码:" + initSDK);
        }
        enableDebug = Ntalker.getInstance().enableDebug(true);// 选择debug模式
        if (0 == enableDebug) {
            Log.e("enableDebug", "执行成功");
        } else {
            Log.e("enableDebug", "执行失败，错误码:" + enableDebug);
        }
        Ntalker.getInstance().setSDKListener(this);
        /*******************************************************************************************************/
        Ntalker.getInstance().login(null, null, 0);
    }

    String settingid = "kf_9149_1452563900179";// 客服组id
    String groupName = "移动测试1";// 客服组名称
    String kfuid = null; //"kefu03";// 指定客服id
    String kfname = null;//"移动测试3";// 指定客服昵称

    private void startChat(){
        ChatParamsBody  chatparams = new ChatParamsBody();

        // 咨询发起页（专有参数）
        chatparams.startPageTitle = "女装/女士精品 - 服饰 - 搜索店铺 - ECMall演示站";
        chatparams.startPageUrl = "http://img.meicicdn.com/p/51/050010/h-050010-1.jpg";

        // 文本匹配参数
        chatparams.matchstr = "www.qq.com";
        /*
         * -----------------订单的配置参数-------------------------------------------------
         */
        String login_name = "17091613625";
        String order_id = "081604495322635348";
        String md5 = "hjd89s8933m-61df)8%0f09";

        String encode = MD5Util.encode((order_id + md5));
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("login_name",login_name);
            jsonObject.put("order_id",order_id);
            jsonObject.put("md5",encode);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String s = Base64Util.encode(jsonObject.toString().getBytes());

        Log.e("1111111", "onClick: "+s);
        /*
         * -----------------订单的配置参数,给erpParam-------------------------------------------------
         */
        // erp参数
        chatparams.erpParam = s;

        // 商品展示（专有参数）
        chatparams.itemparams.appgoodsinfo_type = CoreData.SHOW_GOODS_BY_WIDGET;
        chatparams.itemparams.clientgoodsinfo_type = CoreData.SHOW_GOODS_BY_ID;
        chatparams.itemparams.clicktoshow_type = CoreData.CLICK_TO_APP_COMPONENT;

		/*-------------------------商品ID ,前面加A_-----------------------*/
        chatparams.itemparams.goods_id = "A_1603210789662079";// ntalker_test
		/*-------------------------商品ID ,前面加A_-----------------------*/

        chatparams.itemparams.goods_name = "111111111111111111111111111111111111111";
        chatparams.itemparams.goods_price = "￥：11111111";
        chatparams.itemparams.goods_image = "http://test-s.newcar.hxqc.com/newcar/product/images/b5/21/b521a2284f06236c4e97f3fac7c25714_350_198.jpg";
        chatparams.itemparams.goods_url = "http://www.hxqc.com";
        chatparams.itemparams.goods_showurl = "http://www.hxqc.com";//

        startChat = Ntalker.getInstance().startChat(
                getApplicationContext(), settingid, groupName, kfuid, kfname,
                chatparams,false);

        if (0 == startChat) {
            Log.e("startChat", "打开聊窗成功");
        } else {
            Log.e("startChat", "打开聊窗失败，错误码:" + startChat);
        }
    }

    @Override
    public void onChatMsg(boolean b, String s, String s1, String s2, String s3, long l) {

    }

    @Override
    public void onUnReadMsg(String s, String s1, String s2, String s3, int i) {

    }

    @Override
    public void onClickMatchedStr(String s, String s1) {

    }

    @Override
    public void onClickShowGoods(int i, int i1, String s, String s1, String s2, String s3, String s4, String s5) {

    }

    @Override
    public void onError(int i) {

    }
}
