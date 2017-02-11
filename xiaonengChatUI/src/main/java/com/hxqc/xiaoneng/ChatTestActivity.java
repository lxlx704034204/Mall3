package com.hxqc.xiaoneng;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import cn.xiaoneng.coreapi.TrailActionBody;

/**
 * Author :liukechong
 * Date : 2016-01-19
 * FIXME
 * Todo
 */
public class ChatTestActivity extends AppCompatActivity {
    LinearLayout linearLayout;
    int i =0;
    UnReadMessageListener listener =null;

    String orderID ="091605184835779379";//"141603224099962405";//

    String loginNumber ="18707156083";// "13027169435";//"13027169435";//
    String goodsID = "1553624113376408";
    String userName = "liukechong";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        linearLayout = new LinearLayout(this);
        linearLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        setButton("初始化SDK").setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChatManager.getInstance().initChatSDK(ChatTestActivity.this);
            }
        });
        setButton("商品聊天").setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*-------------------------商品ID ,前面加A_-----------------------*/
                String goods_id = "1603220413986037";// ntalker_test
		/*-------------------------商品ID ,前面加A_-----------------------*/

                String goods_name = "111111111111111111111111111111111111111";
                String goods_price = "￥：11111111";
                String goods_image = "http://test-s.newcar.hxqc.com/newcar/product/images/b5/21/b521a2284f06236c4e97f3fac7c25714_350_198.jpg";
                ChatManager.getInstance().startChatWithGoods(goodsID, goods_name, goods_price, goods_image,"111","111");
            }
        });

        setButton("订单聊天").setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ChatManager.getInstance().startChatWithOrder(orderID,loginNumber,  "111", "111");
            }
        });

        setButton("登录").setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChatManager.getInstance().userLogin(getApplicationContext(),loginNumber, userName, 0);
            }
        });

        setButton("注销").setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChatManager.getInstance().userLogout();
            }
        });

//        setButton("跳转Activity").setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(ChatTestActivity.this,TextXiaoNengActivity.class);
//                startActivity(intent);
//            }
//        });
        setButton("添加轨迹").setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TrailActionBody trailActionBody = new TrailActionBody();
                trailActionBody.ttl= "这是一条轨迹"+i++;
                trailActionBody.url= "http://www.baidu.com";

                ChatManager.getInstance().upLoadTrack(trailActionBody);
            }
        });


        setButton("添加未读消息订阅").setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                listener = new UnReadMessageListener() {
                    @Override
                    public void unReadMessage(String message,long time,int count,boolean isRead) {
                        ((Button)v).setText(message+count);
                    }
                };
                ChatManager.getInstance().addUnReadMessageListener(listener);
            }
        });
        setButton("移除未读消息订阅").setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChatManager.getInstance().removeUnReadMessageListener(listener);
            }
        });
        setButton("添加未读消息订阅").setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final UnReadMessageListener listener = new UnReadMessageListener() {
                    @Override
                    public void unReadMessage(String message,long time,int count,boolean isRead) {
                        ((Button)v).setText(message+count);
                    }
                };
                ChatManager.getInstance().addUnReadMessageListener(listener);
            }
        });
        setButton("销毁SDK").setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChatManager.getInstance().destroySDK();
            }
        });
        setButton("清理缓存").setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChatManager.getInstance().clearCache();
            }
        });


        setContentView(linearLayout);





    }

    private Button setButton(String text){
        Button button2 = new Button(this);
        button2.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        button2.setText(text);
        linearLayout.addView(button2);
        return button2;
    }

}
